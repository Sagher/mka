package com.mka.service.impl;

/**
 *
 * @author Sagher Mehmood
 */
import static com.mka.controller.EntriesController.log;
import com.mka.dao.EntriesDao;
import com.mka.model.AccountPayableReceivable;
import com.mka.model.AsphaltSaleConsumption;
import com.mka.model.AsphaltSales;
import com.mka.model.EntriesDirect;
import com.mka.model.EntriesDirectDetails;
import com.mka.model.EntriesIndirect;
import com.mka.model.EntryItems;
import com.mka.model.MasterAccount;
import com.mka.model.StockTrace;
import com.mka.service.EntriesService;
import com.mka.service.StatsService;
import com.mka.utils.AsyncUtil;
import com.mka.utils.Constants;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
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
            if (dProj.equalsIgnoreCase("other")) {
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
                        int c1 = Integer.parseInt(unloadingRate) * Integer.parseInt(totalUnloadedCrush);
                        int c2 = Integer.parseInt(rate) * Integer.parseInt(quantity);
                        int c3 = (c1 + c2) / Integer.parseInt(totalUnloadedCrush);

                        int cAmount = Integer.parseInt(totalUnloadingCost);
                        int tCost = Integer.parseInt(amount);
                        int totalUnloaded = Integer.parseInt(totalUnloadedCrush);

                        amount = String.valueOf(c1 + c2);

                        int newQuantity = totalUnloaded;
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
                        entryDetail.setTotalUnloaded(BigDecimal.valueOf(Integer.parseInt(quantity)));
                        entryDetail.setUnloadingCost(BigDecimal.ZERO);
                        entryDetail.setUnloadingParty(null);

                        entry.setQuantity(BigDecimal.valueOf(Integer.parseInt(quantity)));
                        entry.setRate(BigDecimal.valueOf(Integer.parseInt(rate)));
                    }
                } else {

                    entry.setQuantity(BigDecimal.valueOf(Integer.parseInt(quantity)));
                    entry.setRate(BigDecimal.valueOf(Integer.parseInt(rate)));
                }
                entry.setTotalPrice(BigDecimal.valueOf(Integer.parseInt(amount)));
                entry.setAdvance(BigDecimal.valueOf(Integer.parseInt(dadvance)));
                entry.setEntryDate(Constants.DATE_FORMAT.parse(dateOfEntry));
                entry.setIsActive(true);

                if (item.getId() != 1 && ss.getStockTrace(item.getId(), subItemType).getStockUnits() < entry.getQuantity().intValue()
                        && (entryType.equalsIgnoreCase(Constants.SALE))) {
                    return ("01:Not Enough stock to make this sale");
                } else if (item.getId() != 1 && ss.getStockTrace(item.getId(), subItemType).getStockUnits() < entry.getQuantity().intValue()
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
                                    entry.getQuantity().intValue(), entry.getRate(),
                                    entryDetail.getUnloadingCost(), new EntryItems(19), "");

                            asyncUtil.addToCustomersAndBuyersList(unloadingParty);

                        }
                    }
                    entry.setEntriesDirectDetails(entryDetail);
                    asyncUtil.updateStockTrace(entry);
                    asyncUtil.logDirectAccountPayableReceivable(entry);
                    asyncUtil.updateMasterAccount(ss.getMasterAccount(), payfrom, entry.getAdvance().intValue());
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
                entry.setEntryDate(Constants.DATE_FORMAT.parse(dateOfEntry));
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
                    asyncUtil.updateMasterAccount(ma, payfrom, entry.getAdvance().intValue());
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

                    if (s.getItemId() != 1 && ss.getStockTrace(s.getItemId(), s.getSubType()).getStockUnits()
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
            realAss.setExPlantRate(BigDecimal.valueOf(Integer.parseInt(expRate)));
            realAss.setExPlantCost(BigDecimal.valueOf(Integer.parseInt(expCost)));

            realAss.setQuantity(Integer.parseInt(totalAss));
            realAss.setTotalSaleAmount(BigDecimal.valueOf(Integer.parseInt(request.getParameter("assSaleCost"))));

            if (entriesDao.logAsphaltSale(realAss)) {
                // log receivable
                AccountPayableReceivable receivable = new AccountPayableReceivable();
                receivable.setAccountName(customer);
                receivable.setAmount(realAss.getTotalSaleAmount());
                receivable.setQuantity(realAss.getQuantity());
                receivable.setRate(BigDecimal.valueOf(Integer.parseInt(request.getParameter("assSaleRate"))));
                receivable.setTotalAmount(realAss.getTotalSaleAmount());
                receivable.setEntryId(realAss.getId());
                receivable.setIsActive(true);
                receivable.setProject(realAss.getProject());
                receivable.setType(Constants.RECEIVABLE);
                receivable.setItemType(new EntryItems(17));
                receivable.setDescription(realAss.getDescription());

                asyncUtil.logAmountReceivable(receivable);

                List<AsphaltSaleConsumption> assConsumptions = new ArrayList<>();
                for (StockTrace s : ss.getStats()) {
                    if (s.getItemId() == 1 || s.getItemId() == 6) {

                        log.info(s.getItemName() + ", " + s.getSubType() + " rate:" + request.getParameter(s.getId() + "rate") + ", quantity:"
                                + request.getParameter(s.getId() + "quantity") + ", cost: " + request.getParameter(s.getId() + "cost"));

                        AsphaltSaleConsumption ass = new AsphaltSaleConsumption();
                        ass.setItemName(s.getItemName() + (s.getSubType() != null ? s.getSubType() : ""));
                        ass.setItemQuantity(Math.round(Float.parseFloat(request.getParameter(s.getId() + "quantity"))));
                        ass.setItemRate(Math.round(Float.parseFloat(request.getParameter(s.getId() + "rate"))));
                        ass.setItemAmount(Math.round(Float.parseFloat(request.getParameter(s.getId() + "cost"))));
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
                String assLayingAdvance = request.getParameter("assLayingAdvance");

                try {
                    if (Integer.parseInt(assLayingCostPerTon) > 0 && Integer.parseInt(totalAssLayingCost) > 0) {
                        AsphaltSaleConsumption ass = new AsphaltSaleConsumption();
                        ass.setItemName("LAYED BY " + assLayer);
                        ass.setItemQuantity(realAss.getQuantity());
                        ass.setItemRate(Integer.parseInt(assLayingCostPerTon));
                        ass.setItemAmount(Integer.parseInt(totalAssLayingCost));
                        assConsumptions.add(ass);
                        ass.setAsphlatSaleId(realAss);

                        if (Integer.parseInt(assLayingAdvance) != Integer.parseInt(totalAssLayingCost)) {
                            // advance payable
                            int payableAmount = Integer.parseInt(totalAssLayingCost) - Integer.parseInt(assLayingAdvance);
                            String payableTo = assLayer;
                            asyncUtil.logAmountPayable(BigDecimal.valueOf(payableAmount), payableTo, realAss.getId(),
                                    realAss.getProject(), realAss.getDescription(), ass.getItemQuantity(), BigDecimal.valueOf(ass.getItemRate()),
                                    BigDecimal.valueOf(ass.getItemAmount()), new EntryItems(20), realAss.getType());
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
                    if (Integer.parseInt(assCarCostPerTon) > 0 && Integer.parseInt(totalAssCarCost) > 0) {
                        AsphaltSaleConsumption assCarriage = new AsphaltSaleConsumption();
                        assCarriage.setItemName("Carriage Provided By " + assCarProvider);
                        assCarriage.setItemQuantity(realAss.getQuantity());
                        assCarriage.setItemRate(Integer.parseInt(assCarCostPerTon));
                        assCarriage.setItemAmount(Integer.parseInt(totalAssCarCost));
                        assConsumptions.add(assCarriage);
                        assCarriage.setAsphlatSaleId(realAss);

                        if (Integer.parseInt(assCarAdvance) != Integer.parseInt(totalAssCarCost)) {
                            // advance payable
                            int payableAmount = Integer.parseInt(totalAssCarCost) - Integer.parseInt(assCarAdvance);
                            String payableTo = assCarProvider;
                            asyncUtil.logAmountPayable(BigDecimal.valueOf(payableAmount), payableTo, realAss.getId(), realAss.getProject(), realAss.getDescription(),
                                    assCarriage.getItemQuantity(), BigDecimal.valueOf(assCarriage.getItemRate()),
                                    BigDecimal.valueOf(assCarriage.getItemAmount()), new EntryItems(21), realAss.getType());
                        }
                    }
                } catch (Exception e) {

                }
                boolean logged = entriesDao.logAsphaltSaleConsumptions(assConsumptions);

                if (logged) {

                    // update stockTrace
                    for (StockTrace s : ss.getStats()) {
                        if (s.getItemId() == 1 || s.getItemId() == 6) {

                            s.setStockUnits(s.getStockUnits() - Math.round(Float.parseFloat(request.getParameter(s.getId() + "quantity"))));
                            s.setStockAmount(s.getStockAmount().subtract(BigDecimal.valueOf(Math.round(Float.parseFloat(request.getParameter(s.getId() + "cost"))))));

                            s.setConsumeUnit(s.getConsumeUnit() + Math.round(Float.parseFloat(request.getParameter(s.getId() + "quantity"))));
                            s.setConsumeAmount(s.getConsumeAmount().add(BigDecimal.valueOf(Math.round(Float.parseFloat(request.getParameter(s.getId() + "cost"))))));

                            asyncUtil.updateStockTrace(s);
                        }
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
}
