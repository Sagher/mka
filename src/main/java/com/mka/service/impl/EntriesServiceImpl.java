package com.mka.service.impl;

/**
 *
 * @author Sagher Mehmood
 */
import com.mka.dao.EntriesDao;
import com.mka.model.AccountPayableReceivable;
import com.mka.model.AsphaltSaleConsumption;
import com.mka.model.AsphaltSales;
import com.mka.model.EntriesDirect;
import com.mka.model.EntriesDirectDetails;
import com.mka.model.EntriesIndirect;
import com.mka.model.EntryItems;
import com.mka.model.MachineryCarriage;
import com.mka.model.MasterAccount;
import com.mka.model.StockTrace;
import com.mka.service.EntriesService;
import com.mka.service.StatsService;
import com.mka.utils.AsyncUtil;
import com.mka.utils.Constants;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("entriesService")
public class EntriesServiceImpl implements EntriesService {

    private static final Logger log = Logger.getLogger(EntriesServiceImpl.class);

    @Autowired
    AsyncUtil asyncUtil;

    @Autowired
    EntriesDao entriesDao;

    @Autowired
    StatsService ss;

    private List<EntryItems> entryItems = null;

    @Override
    public List<EntryItems> getAllEntryItems() {
        if (entryItems == null) {
            entryItems = entriesDao.getAllEntryItems();
        }
        return entryItems;
    }

    public EntryItems getEntryItemById(int id) {
        EntryItems item = null;
        try {
            item = getAllEntryItems().parallelStream().filter(e -> e.getId() == id).collect(Collectors.toList()).get(0);
        } catch (Exception e) {
            log.error("Exception in getEntryItemById(" + id + "): ", e);
        }
        return item;
    }

    @Override
    public Object logDirectEntry(HttpServletRequest request) {
        EntriesDirectDetails entryDetail = null;
        try {
            String dItemType = request.getParameter("dItemType");
            String subItemType = request.getParameter("dItemSubType");
            String entryType = request.getParameter("dEntryType");
            String customerBuyerSupplier = request.getParameter("dbuysup");
            if (customerBuyerSupplier.equalsIgnoreCase("other")) {
                customerBuyerSupplier = request.getParameter("dbuysupInput");
                asyncUtil.addToCustomersAndBuyersList(customerBuyerSupplier);
            }
            String dProj = request.getParameter("dproj");
            if (dProj != null && !dProj.isEmpty() && dProj.equalsIgnoreCase("other")) {
                dProj = request.getParameter("dproject");
                asyncUtil.addToProjectsList(dProj);
            }

            String description = request.getParameter("ddescription") != null
                    ? request.getParameter("ddescription") : null;
            String quantity = request.getParameter("dquantity");
            String rate = request.getParameter("drate");
            String amount = request.getParameter("damount");
            String dadvance = request.getParameter("dadvance");
            String dateOfEntry = request.getParameter("doe");
            String payfrom = request.getParameter("payfrom");
            String pBilty = request.getParameter("pBilty") != null ? request.getParameter("pBilty") : "0";
            String cBilty = request.getParameter("cBilty") != null ? request.getParameter("cBilty") : "0";
            String totalAmount = amount;

            EntryItems item = getEntryItemById(Integer.parseInt(dItemType));
            if (item != null) {
                EntriesDirect entry = new EntriesDirect();
                entry.setItem(item);
                entry.setSubEntryType(entryType);
                if (entryType.equalsIgnoreCase(Constants.SALE)) {
                    entry.setBuyer(customerBuyerSupplier);
                    entry.setSupplier(null);
                } else {
                    entry.setSupplier(customerBuyerSupplier);
                    entry.setBuyer(null);
                }
                entry.setProject(dProj);
                entry.setDescription(description);
                entry.setPlantBilty(Integer.parseInt(!pBilty.isEmpty() ? pBilty : "0"));
                entry.setRecipientBilty(Integer.parseInt(!cBilty.isEmpty() ? cBilty : "0"));

                // OPTIONAL unloadedCrush & unloadingCost in case of crush
                String totalUnloadedCrush = request.getParameter("unloadedCrush");
                String unloadingRate = request.getParameter("unloadingCost");
                String totalUnloadingCost = request.getParameter("totalUnloadingCost");

                String unloadingParty = request.getParameter("unloadingParty");
                if (unloadingParty.equalsIgnoreCase("other")) {
                    unloadingParty = request.getParameter("dcPartyInput");
                    asyncUtil.addToCustomersAndBuyersList(unloadingParty);
                }

                if (item.getItemName().equalsIgnoreCase("CRUSH")) {

                    if (!totalUnloadedCrush.isEmpty() && !totalUnloadedCrush.equals("0")
                            && !unloadingRate.isEmpty() && !unloadingRate.equals("0")
                            && !unloadingParty.isEmpty() && !unloadingParty.equals("0")) {
                        //Carriage =((10*700) + (800*5))/800
                        if (rate.isEmpty() || rate.equals("0")) {
                            rate = String.valueOf(Float.parseFloat(amount) / Float.parseFloat(quantity));
                        }
                        Float c1 = Float.parseFloat(unloadingRate) * Float.parseFloat(totalUnloadedCrush);
                        Float c2 = Float.parseFloat(rate) * Float.parseFloat(quantity);
                        Float c3 = (c1 + c2) / Float.parseFloat(totalUnloadedCrush);

                        Float cAmount = Float.parseFloat(totalUnloadingCost);
                        Float totalUnloaded = Float.parseFloat(totalUnloadedCrush);

                        totalAmount = String.valueOf(c1 + c2);

                        Float newQuantity = totalUnloaded;
                        entryDetail = new EntriesDirectDetails();
                        entryDetail.setSubType(subItemType);
                        entryDetail.setTotalUnloaded(BigDecimal.valueOf(totalUnloaded));
                        entryDetail.setUnloadingCost(BigDecimal.valueOf(cAmount));
                        entryDetail.setUnloadingParty(unloadingParty);

                        entry.setQuantity(BigDecimal.valueOf(newQuantity));
                        entry.setRate(BigDecimal.valueOf(c3));

                    } else {
                        entryDetail = new EntriesDirectDetails();
                        entryDetail.setSubType(subItemType);
                        entryDetail.setTotalUnloaded(BigDecimal.valueOf(Float.parseFloat(quantity)));
                        entryDetail.setUnloadingCost(BigDecimal.ZERO);
                        entryDetail.setUnloadingParty(null);

                        entry.setQuantity(BigDecimal.valueOf(Float.parseFloat(quantity)));
                        entry.setRate(BigDecimal.valueOf(Float.parseFloat(rate)));
                    }
                } else {

                    entry.setQuantity(BigDecimal.valueOf(Float.parseFloat(quantity)));
                    entry.setRate(BigDecimal.valueOf(Float.parseFloat(rate)));
                }
                entry.setTotalPrice(BigDecimal.valueOf(Float.parseFloat(amount)));
                entry.setAdvance(BigDecimal.valueOf(Float.parseFloat(dadvance)));

                if (Constants.DATE_FORMAT.parse((Constants.DATE_FORMAT.format(new Date())))
                        .after(Constants.DATE_FORMAT.parse(dateOfEntry))) {
                    entry.setCreatedDate(Constants.TIMESTAMP_FORMAT.parse(dateOfEntry + " 01:00:00"));
                } else {
                    entry.setCreatedDate(new Date());

                }
                entry.setIsActive(true);

                if (item.getId() != 1 && ss.getStockTrace(item.getId(), subItemType).getStockUnits().intValue() < entry.getQuantity().intValue()
                        && (entryType.equalsIgnoreCase(Constants.SALE))) {
                    return ("01:Not Enough stock to make this sale");
                } else if (item.getId() != 1 && ss.getStockTrace(item.getId(), subItemType).getStockUnits().intValue() < entry.getQuantity().intValue()
                        && (entryType.equalsIgnoreCase(Constants.CONSUME))) {
                    return ("01:Not Enough stock to log this consumption");
                } else if (ss.getMasterAccount().getCashInHand().intValue() < entry.getAdvance().intValue()
                        && entryType.equalsIgnoreCase(Constants.PURCHASE) && payfrom.equals("1")) {
                    return ("01:Not Enough cash in hand to make this purchase");
                } else if (ss.getMasterAccount().getTotalCash().intValue() < entry.getAdvance().intValue()
                        && entryType.equalsIgnoreCase(Constants.PURCHASE) && payfrom.equals("0")) {
                    return ("01:Not Enough cash in Main Account to make this purchase");
                }

                boolean entryLogged = entriesDao.logDirectEntry(entry);
                if (!entryLogged) {
                    return ("01:Failed To Log Entry. Make sure all field are filled in.");
                } else {
                    if (entryDetail != null) {
                        // crush carriage only
                        entryDetail.setEntryId(entry);
                        addEntryDetail(entryDetail);
                        if (unloadingParty != null && !unloadingParty.isEmpty()) {
                            asyncUtil.logAmountPayable(entryDetail.getUnloadingCost(), unloadingParty,
                                    entry.getId(), entry.getProject(), entry.getDescription(),
                                    entry.getQuantity(), BigDecimal.valueOf(Float.parseFloat(unloadingRate)),
                                    entryDetail.getUnloadingCost(), new EntryItems(19), subItemType, entry.getCreatedDate(),
                                    entry.getPlantBilty(), entry.getRecipientBilty());

                            asyncUtil.addToCustomersAndBuyersList(unloadingParty);
                        }

                    }

                    entry.setEntriesDirectDetails(entryDetail);

                    EntriesDirect entry2 = new EntriesDirect();
                    BeanUtils.copyProperties(entry, entry2);
                    entry2.setRate(BigDecimal.valueOf(Float.parseFloat(rate)));
                    entry2.setTotalPrice(BigDecimal.valueOf(Float.parseFloat(amount)));
                    entry2.setQuantity(BigDecimal.valueOf(Float.parseFloat(quantity)));
                    if (entry2.getRate().equals(BigDecimal.ZERO)) {
                        entry2.setRate(entry2.getTotalPrice().divide(entry2.getQuantity()));
                    }
                    asyncUtil.logDirectAccountPayableReceivable(entry2, subItemType);

                    entry.setTotalPrice(BigDecimal.valueOf(Float.parseFloat(totalAmount)));
                    asyncUtil.updateStockTrace(entry);
                    asyncUtil.updateMasterAccount(ss.getMasterAccount(), payfrom, entry.getAdvance(), item, entryType, entry2);
                    return entry;
                }

            } else {
                return "01:Invalid Entry Type";
            }
        } catch (Exception e) {
            log.error("Exception while logging Entry:", e);
            return ("01:Failed To Log Entry. Make sure all field are filled in.");
        }

    }

    @Override
    public List<EntriesDirect> getDirectEntries(EntryItems entryItem, String subEntryType, int startIndex, int fetchSize,
            String orderBy, String sortBy, String startDate, String endDate, String buyerSupplier, String project) {
        return entriesDao.getDirectEntries(entryItem, subEntryType, startIndex, fetchSize, orderBy, sortBy, startDate, endDate, buyerSupplier, project);
    }

    @Override
    public int getDirectEntriesCount(EntryItems entryItem, String subEntryType, String startDate, String endDate,
            String buyerSupplier, String project) {
        return entriesDao.getDirectEntriesCount(entryItem, subEntryType, startDate, endDate, buyerSupplier, project);
    }

    @Override
    public EntriesDirect getDirectEntry(int id) {
        return entriesDao.getDirectEntry(id);
    }

    @Override
    public boolean updateDirectEntry(EntriesDirect entry) {
        return entriesDao.updateDirectEntry(entry);
    }

    @Override
    public Object logInDirectEntry(HttpServletRequest request) {
        try {
            EntryItems item;

            String iItemType = request.getParameter("iItemType");
            if (iItemType.equalsIgnoreCase("other")) {
                item = createNewEntryItem(request.getParameter("iItemTypeInput"));
            } else {
                item = getEntryItemById(Integer.parseInt(iItemType));
            }
            String iItemSubType = request.getParameter("iItemSubType");
            String iname = request.getParameter("iname");
            if (iname.equalsIgnoreCase("other")) {
                iname = request.getParameter("ibuysupInput");
                asyncUtil.addToCustomersAndBuyersList(iname);
            }

            String idesc = request.getParameter("idesc");
            String icost = request.getParameter("icost");
            String iAdvance = request.getParameter("iadvance");
            String dateOfEntry = request.getParameter("idoe");
            String payfrom = request.getParameter("payfrom");

            if (item != null) {
                EntriesIndirect entry = new EntriesIndirect();
                entry.setItem(item);
                if (item.getItemType() != null) {
                    entry.setItemType(iItemSubType);
                } else {
                    entry.setItemType(null);
                }
                entry.setName(iname);
                entry.setDescription(idesc);
                entry.setAmount(BigDecimal.valueOf(Long.parseLong(icost)));
                entry.setAdvance(BigDecimal.valueOf(Long.parseLong(iAdvance)));
                if (Constants.DATE_FORMAT.parse((Constants.DATE_FORMAT.format(new Date())))
                        .after(Constants.DATE_FORMAT.parse(dateOfEntry))) {
                    entry.setCreatedDate(Constants.TIMESTAMP_FORMAT.parse(dateOfEntry + " 01:00:00"));
                } else {
                    entry.setCreatedDate(new Date());

                }
                entry.setIsActive(true);

                MasterAccount ma = ss.getMasterAccount();
                if (ma.getCashInHand().intValue() < entry.getAdvance().intValue()) {
                    return ("01:Not Enough Cash In Hand to Log this Expense");
                } else if (ma.getCashInHand().intValue() < entry.getAdvance().intValue() && payfrom.equals("1")) {
                    return ("01:Not Enough cash in hand to make this purchase");
                } else if (ma.getTotalCash().intValue() < entry.getAdvance().intValue() && payfrom.equals("0")) {
                    return ("01:Not Enough cash in Main Account to make this purchase");
                }

                boolean entryLogged = entriesDao.logInDirectEntry(entry);
                if (!entryLogged) {
                    return ("01:Failed To Log Entry. Make sure all field are filled in.");
                } else {
                    asyncUtil.updateMasterAccount(ma, payfrom, entry.getAdvance(), item, Constants.EXPENSE, entry);
                    asyncUtil.updateIndirectAccountPayableReceivable(entry);
                    return entry;
                }
            } else {
                return ("01:Invalid Item Type");
            }
        } catch (Exception e) {
            log.error("Exception while logging Entry:", e);
            return ("01:Invalid Values");
        }
    }

    @Override
    public List<EntriesIndirect> getInDirectEntries(int startIndex, int fetchSize, String orderBy, String sortBy, String startDate, String endDate, String buyerSupplier) {
        return entriesDao.getInDirectEntries(startIndex, fetchSize, orderBy, sortBy, startDate, endDate, buyerSupplier);
    }

    @Override
    public int getInDirectEntriesCount(String startDate, String endDate, String buyerSupplier) {
        return entriesDao.getInDirectEntriesCount(startDate, endDate, buyerSupplier);
    }

    @Override
    public EntriesIndirect getInDirectEntry(int id) {
        return entriesDao.getInDirectEntry(id);
    }

    @Override
    public boolean updateInDirectEntry(EntriesIndirect entry) {
        return entriesDao.updateInDirectEntry(entry);
    }

    @Override
    public EntryItems createNewEntryItem(String parameter) {
        EntryItems item = new EntryItems();
        item.setItemName(parameter);
        item.setItemType(null);
        item.setItemUnit(null);
        item.setEntryType(Constants.INDIRECT);
        item.setSubEntryType(Constants.EXPENSE);

        List<EntryItems> items = getAllEntryItems().parallelStream().filter((EntryItems e) -> {
            if (e.getItemName().equalsIgnoreCase(parameter)) {
                return e.getEntryType().equalsIgnoreCase(Constants.INDIRECT);
            }
            return false;
        }).collect(Collectors.toList());

        if (items != null && items.size() > 0) {
            return items.get(0);
        } else {
            item = entriesDao.createNewEntryItem(item);
            if (item != null) {
                entryItems.add(item);
            }
            return item;
        }
    }

    public void addEntryDetail(EntriesDirectDetails entryDetail) {
        entriesDao.addEntryDetail(entryDetail);
    }

    @Override
    public Object logAsphaltSale(HttpServletRequest request) {
        try {

            String totalAss = request.getParameter("tass");
            String customer = request.getParameter("asCus");
            String customerNew = request.getParameter("asCusInput");
            String asProj = request.getParameter("asProj");
            String asProjInput = request.getParameter("asProjInput");
            String doe = request.getParameter("asdoe");

            if (customer.equalsIgnoreCase("other")) {
                customer = customerNew;
                asyncUtil.addToCustomersAndBuyersList(customer);
            }
            if (asProj.equalsIgnoreCase("other")) {
                asProj = asProjInput;
                asyncUtil.addToProjectsList(asProj);
            }

            for (StockTrace s : ss.getStats()) {
                if (s.getItemId() == 1 || s.getItemId() == 6) {
                    log.info(s.getItemName() + ", " + s.getSubType() + " rate:" + request.getParameter(s.getId() + "rate") + ", quantity:"
                            + request.getParameter(s.getId() + "quantity") + ", cost: " + request.getParameter(s.getId() + "cost"));

                    if (s.getItemId() != 1 && ss.getStockTrace(s.getItemId(), s.getSubType()).getStockUnits().intValue()
                            < Float.parseFloat(request.getParameter(s.getId() + "quantity"))) {
                        return ("01:Not Enough " + s.getItemName() + (s.getSubType() != null ? s.getSubType() : "") + " to make this sale");
                    }
                }
            }

            String biltee = request.getParameter("biltee");
            String vehicle = request.getParameter("vehicle");
            String asstype = request.getParameter("asstype");
            String expRate = request.getParameter("expRate");
            String expCost = request.getParameter("expCost");

            AsphaltSales realAss = new AsphaltSales();
            realAss.setBuyer(customer);
            realAss.setProject(asProj);
            realAss.setBiltee(Integer.parseInt(biltee));
            realAss.setVehicle(vehicle);
            realAss.setType(asstype);
            realAss.setExPlantRate(BigDecimal.valueOf(Float.parseFloat(expRate)));
            realAss.setExPlantCost(BigDecimal.valueOf(Float.parseFloat(expCost)));

            if (Constants.DATE_FORMAT.parse((Constants.DATE_FORMAT.format(new Date())))
                    .after(Constants.DATE_FORMAT.parse(doe))) {
                realAss.setCreatedDate(Constants.TIMESTAMP_FORMAT.parse(doe + " 01:00:00"));
            } else {
                realAss.setCreatedDate(new Date());

            }

            realAss.setQuantity(BigDecimal.valueOf(Float.parseFloat(totalAss)));
            realAss.setTotalSaleAmount(BigDecimal.valueOf(Float.parseFloat(request.getParameter("assSaleCost"))));

            if (entriesDao.logAsphaltSale(realAss)) {
                // log receivable
                AccountPayableReceivable receivable = new AccountPayableReceivable();
                receivable.setAccountName(customer);
                receivable.setAmount(realAss.getTotalSaleAmount());
                receivable.setQuantity(realAss.getQuantity());
                receivable.setRate(BigDecimal.valueOf(Float.parseFloat(request.getParameter("assSaleRate"))));
                receivable.setTotalAmount(realAss.getTotalSaleAmount());
                receivable.setEntryId(realAss.getId());
                receivable.setIsActive(true);
                receivable.setProject(realAss.getProject());
                receivable.setType(Constants.RECEIVABLE);
                receivable.setItemType(new EntryItems(17));
                receivable.setDescription(realAss.getDescription());
                receivable.setTimestamp(new Date());
                receivable.setPlantBilty(realAss.getBiltee());
                receivable.setVehicleNo(vehicle);
                asyncUtil.logAmountReceivable(receivable);

                List<AsphaltSaleConsumption> assConsumptions = new ArrayList<>();
                for (StockTrace s : ss.getStats()) {
                    if (s.getItemId() == 1 || s.getItemId() == 6) {

                        log.info(s.getItemName() + ", " + s.getSubType() + " rate:" + request.getParameter(s.getId() + "rate") + ", quantity:"
                                + request.getParameter(s.getId() + "quantity") + ", cost: " + request.getParameter(s.getId() + "cost"));

                        AsphaltSaleConsumption ass = new AsphaltSaleConsumption();
                        ass.setItemName(s.getItemName() + (s.getSubType() != null ? s.getSubType() : ""));
                        ass.setItemQuantity(BigDecimal.valueOf(Float.parseFloat(request.getParameter(s.getId() + "quantity"))));
                        ass.setItemRate(BigDecimal.valueOf(Float.parseFloat(request.getParameter(s.getId() + "rate"))));
                        ass.setItemAmount(BigDecimal.valueOf(Float.parseFloat(request.getParameter(s.getId() + "cost"))));
                        ass.setAsphlatSaleId(realAss);

                        assConsumptions.add(ass);

                    }

                }

                String assLayer = request.getParameter("assLayer");
                if (assLayer.equalsIgnoreCase("other")) {
                    assLayer = request.getParameter("assLayerInput");
                    asyncUtil.addToCustomersAndBuyersList(assLayer);
                }
                String assLayingCostPerTon = request.getParameter("assLayingCostPerTon");
                String totalAssLayingCost = request.getParameter("totalAssLayingCost");
                String assLayingAdvance = !request.getParameter("assLayingAdvance").isEmpty() ? request.getParameter("assLayingAdvance") : "0";

                try {
                    if (!assLayingCostPerTon.isEmpty() && !totalAssLayingCost.isEmpty()
                            && Float.parseFloat(assLayingCostPerTon) > 0 && Float.parseFloat(totalAssLayingCost) > 0) {
                        AsphaltSaleConsumption ass = new AsphaltSaleConsumption();
                        ass.setItemName("LAYED BY " + assLayer);
                        ass.setItemQuantity(realAss.getQuantity());
                        ass.setItemRate(BigDecimal.valueOf(Float.parseFloat(assLayingCostPerTon)));
                        ass.setItemAmount(BigDecimal.valueOf(Float.parseFloat(totalAssLayingCost)));
                        assConsumptions.add(ass);
                        ass.setAsphlatSaleId(realAss);

                        if (Float.parseFloat(assLayingAdvance) != Float.parseFloat(totalAssLayingCost)) {
                            // advance payable
                            Float payableAmount = Float.parseFloat(totalAssLayingCost) - Float.parseFloat(assLayingAdvance);
                            String payableTo = assLayer;
                            asyncUtil.logAmountPayable(BigDecimal.valueOf(payableAmount), payableTo, realAss.getId(),
                                    realAss.getProject(), realAss.getDescription(), ass.getItemQuantity(), ass.getItemRate(),
                                    ass.getItemAmount(), new EntryItems(20), realAss.getType(), realAss.getCreatedDate(),
                                    0, realAss.getBiltee());

                            asyncUtil.updateMasterAccount(BigDecimal.valueOf(Float.parseFloat(assLayingAdvance)), "Asphalt Laying");
                        }

                    }
                } catch (Exception e) {

                }

                String assCarProvider = request.getParameter("assCarProvider");
                if (assCarProvider.equalsIgnoreCase("other")) {
                    assCarProvider = request.getParameter("assCarProviderInput");
                    asyncUtil.addToCustomersAndBuyersList(assCarProvider);
                }
                String assCarCostPerTon = request.getParameter("assCarCostPerTon");
                String totalAssCarCost = request.getParameter("totalAssCarCost");
                String assCarAdvance = request.getParameter("assCarAdvance");

                try {
                    if (!assCarCostPerTon.isEmpty() && !totalAssCarCost.isEmpty()
                            && Float.parseFloat(assCarCostPerTon) > 0 && Float.parseFloat(totalAssCarCost) > 0) {
                        AsphaltSaleConsumption assCarriage = new AsphaltSaleConsumption();
                        assCarriage.setItemName("Carriage Provided By " + assCarProvider);
                        assCarriage.setItemQuantity(realAss.getQuantity());
                        assCarriage.setItemRate(BigDecimal.valueOf(Float.parseFloat(assCarCostPerTon)));
                        assCarriage.setItemAmount(BigDecimal.valueOf(Float.parseFloat(totalAssCarCost)));
                        assConsumptions.add(assCarriage);
                        assCarriage.setAsphlatSaleId(realAss);

                        if (Float.parseFloat(assCarAdvance) != Float.parseFloat(totalAssCarCost)) {
                            // advance payable
                            Float payableAmount = Float.parseFloat(totalAssCarCost) - Float.parseFloat(assCarAdvance);
                            String payableTo = assCarProvider;
                            asyncUtil.logAmountPayable(BigDecimal.valueOf(payableAmount), payableTo, realAss.getId(), realAss.getProject(), realAss.getDescription(),
                                    assCarriage.getItemQuantity(), (assCarriage.getItemRate()),
                                    (assCarriage.getItemAmount()), new EntryItems(21), realAss.getType(), realAss.getCreatedDate(),
                                    0, realAss.getBiltee());

                            asyncUtil.updateMasterAccount(BigDecimal.valueOf(Float.parseFloat(assCarAdvance)), "Asphalt Carriage");

                        }
                    }
                } catch (Exception e) {

                }
                boolean logged = entriesDao.logAsphaltSaleConsumptions(assConsumptions);

                if (logged) {
                    try {
                        // update stockTrace
                        for (StockTrace s : ss.getStats()) {
                            if (s.getItemId() == 1 || s.getItemId() == 6) {

                                s.setStockUnits(s.getStockUnits().subtract(BigDecimal.valueOf(Float.parseFloat(request.getParameter(s.getId() + "quantity")))));
                                s.setStockAmount(s.getStockAmount().subtract(BigDecimal.valueOf(Math.round(Float.parseFloat(request.getParameter(s.getId() + "cost"))))));

                                s.setConsumeUnit(s.getConsumeUnit().add(BigDecimal.valueOf(Float.parseFloat(request.getParameter(s.getId() + "quantity")))));
                                s.setConsumeAmount(s.getConsumeAmount().add(BigDecimal.valueOf(Math.round(Float.parseFloat(request.getParameter(s.getId() + "cost"))))));

                                asyncUtil.updateStockTrace(s);

                            } else if (s.getItemId() == 17) {
                                s.setSalesUnit(s.getSalesUnit().add(realAss.getQuantity()));
                                s.setSalesAmount(s.getSalesAmount().add(realAss.getTotalSaleAmount()));
                                asyncUtil.updateStockTrace(s);

                            }
                        }
                    } catch (Exception e) {

                    }
                }
            }

            return realAss;
        } catch (Exception e) {
            log.error("Exception while logging Entry:", e);
            return ("01:Failed To Log Entry. Make sure all field are filled in.");
        }
    }

    @Override
    public boolean logInDirectEntry(EntriesIndirect entry) {
        boolean entryLogged = entriesDao.logInDirectEntry(entry);
        if (entryLogged) {
            asyncUtil.updateIndirectAccountPayableReceivable(entry);
        }
        return true;
    }

    @Override
    public String logMachinery(HttpServletRequest request) {

        try {

            String customer = request.getParameter("macCarCon");
            String customerNew = request.getParameter("macCarConInput");

            String customer2 = request.getParameter("macCarCon2");
            String customer2New = request.getParameter("macCarCon2Input");

            if (customer.equalsIgnoreCase("other")) {
                customer = customerNew;
                asyncUtil.addToCustomersAndBuyersList(customer);
            }

            if (customer2.equalsIgnoreCase("other")) {
                customer2 = customer2New;
                asyncUtil.addToCustomersAndBuyersList(customer2);
            }

            String macCarFrom = request.getParameter("macCarFrom");
            String macCarTo = request.getParameter("macCarTo");
            String amount = request.getParameter("macCarAmount");

            MachineryCarriage mac = new MachineryCarriage();
            mac.setContractor(customer);
            mac.setOnAccountOf(customer2);
            mac.setSource(macCarFrom);
            mac.setDestination(macCarTo);
            mac.setAmount(BigDecimal.valueOf(Float.parseFloat(amount)));

            boolean logged = entriesDao.logMachineryCarriage(mac);

            if (logged) {
                AccountPayableReceivable payable = new AccountPayableReceivable();
                payable.setAccountName(mac.getContractor());
                payable.setAmount(mac.getAmount());
                payable.setQuantity(BigDecimal.ZERO);
                payable.setRate(BigDecimal.ZERO);
                payable.setTotalAmount(mac.getAmount());
                payable.setEntryId(mac.getId());
                payable.setIsActive(true);
                payable.setProject(null);
                payable.setSubType("Carriage Contractor");
                payable.setTimestamp(new Date());
                payable.setItemType(new EntryItems(23));
                payable.setType(Constants.PAYABLE);
                asyncUtil.logAmountReceivablePayable(payable);

                AccountPayableReceivable receivable = new AccountPayableReceivable();
                BeanUtils.copyProperties(payable, receivable);
                receivable.setType(Constants.RECEIVABLE);
                receivable.setSubType("On Account Of");
                receivable.setAccountName(mac.getOnAccountOf());

                asyncUtil.logAmountReceivablePayable(receivable);

                return "00:Sale Logged Successfully";

            }
        } catch (Exception e) {
            log.error("Exception while logging Entry:", e);
        }
        return ("01:Failed To Log Entry. Make sure all field are filled in.");

    }
}
