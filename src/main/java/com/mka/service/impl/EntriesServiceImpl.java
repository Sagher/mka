package com.mka.service.impl;

/**
 *
 * @author Sagher Mehmood
 */
import com.mka.dao.EntriesDao;
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
import java.math.BigInteger;
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

                if (item.getItemName().equalsIgnoreCase("CRUSH")) {

                    if (!totalUnloadedCrush.isEmpty() || totalUnloadedCrush.equals("0")
                            || !unloadingRate.isEmpty() || unloadingRate.equals("0")
                            || !unloadingParty.isEmpty() || unloadingParty.equals("0")) {
                        int cRate = Integer.parseInt(unloadingRate);
                        int cAmount = Integer.parseInt(totalUnloadingCost);
                        int tCost = Integer.parseInt(amount);
                        int totalUnloaded = Integer.parseInt(totalUnloadedCrush);

                        int newRate = (cAmount + tCost) / totalUnloaded;
                        int newQuantity = totalUnloaded;
                        entryDetail = new EntriesDirectDetails();
                        entryDetail.setSubType(subItemType);
                        entryDetail.setTotalUnloaded(totalUnloaded);
                        entryDetail.setUnloadingCost(cAmount);
                        entryDetail.setUnloadingParty(unloadingParty);

                        entry.setQuantity(newQuantity);
                        entry.setRate(newRate);
                    } else {
                        entryDetail = new EntriesDirectDetails();
                        entryDetail.setSubType(subItemType);
                        entryDetail.setTotalUnloaded(Integer.parseInt(quantity));
                        entryDetail.setUnloadingCost(0);
                        entryDetail.setUnloadingParty(null);

                        entry.setQuantity(Integer.parseInt(quantity));
                        entry.setRate(Integer.parseInt(rate));
                    }
                } else {

                    entry.setQuantity(Integer.parseInt(quantity));
                    entry.setRate(Integer.parseInt(rate));
                }
                entry.setTotalPrice(Integer.parseInt(amount));
                entry.setAdvance(Integer.parseInt(dadvance));
                entry.setEntryDate(Constants.DATE_FORMAT.parse(dateOfEntry));
                entry.setIsActive(true);

                if (ss.getStockTrace(item.getId(), subItemType).getStockUnits() < entry.getQuantity()
                        && entryType.equalsIgnoreCase(Constants.SALE)) {
                    return ("01:Not Enough stock to make this sale");
                } else if (ss.getMasterAccount().getCashInHand() < entry.getAdvance()
                        && entryType.equalsIgnoreCase(Constants.PURCHASE) && payfrom.equals("1")) {
                    return ("01:Not Enough cash in hand to make this purchase");
                } else if (ss.getMasterAccount().getTotalCash() < entry.getAdvance()
                        && entryType.equalsIgnoreCase(Constants.PURCHASE) && payfrom.equals("0")) {
                    return ("01:Not Enough cash in Main Account to make this purchase");
                }

                boolean entryLogged = entriesDao.logDirectEntry(entry);
                if (!entryLogged) {
                    return ("01:Failed To Log Entry. Make sure all field are filled in.");
                } else {
                    if (entryDetail != null) {
                        entryDetail.setEntryId(entry);
                        addEntryDetail(entryDetail);
                    }
                    entry.setEntriesDirectDetails(entryDetail);
                    asyncUtil.updateStockTrace(entry);
                    asyncUtil.updateDirectAccountPayableReceivable(entry);
                    asyncUtil.updateMasterAccount(ss.getMasterAccount(), payfrom, entry.getAdvance());
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
    public boolean logInDirectEntry(EntriesIndirect entry) {
        boolean logged = entriesDao.logInDirectEntry(entry);
        return logged;
    }

    @Override
    public List<EntriesIndirect> getInDirectEntries(int startIndex, int fetchSize, String orderBy, String sortBy, String startDate, String endDate) {
        return entriesDao.getInDirectEntries(startIndex, fetchSize, orderBy, sortBy, startDate, endDate);
    }

    @Override
    public int getInDirectEntriesCount(String startDate, String endDate) {
        return entriesDao.getInDirectEntriesCount(startDate, endDate);
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
            String totalCostHQ = request.getParameter("totalCostHQ");
            String costPerTon = request.getParameter("costPerTon");
            String totalCostCustomer = request.getParameter("totalCostCustomer");
            String advancePaid = request.getParameter("advancePaid");

            if (customer.equalsIgnoreCase("other")) {
                customer = customerNew;
                asyncUtil.addToCustomersAndBuyersList(customer);
            }
            if (asProj.equalsIgnoreCase("other")) {
                asProj = asProjInput;
                asyncUtil.addToProjectsList(asProj);
            }

            for (StockTrace s : ss.getStats()) {
                log.info(s.getItemName() + ", " + s.getSubType() + " rate:" + request.getParameter(s.getId() + "rate") + ", quantity:"
                        + request.getParameter(s.getId() + "quantity") + ", cost: " + request.getParameter(s.getId() + "cost"));

                if (ss.getStockTrace(s.getItemId(), s.getSubType()).getStockUnits()
                        < Integer.parseInt(request.getParameter(s.getId() + "quantity"))) {
                    return ("01:Not Enough " + s.getItemName() + (s.getSubType() != null ? s.getSubType() : "") + " to make this sale");
                }
            }

            AsphaltSales realAss = new AsphaltSales();
            realAss.setBuyer(customer);
            realAss.setProject(asProj);
            realAss.setQuantity(Integer.parseInt(totalAss));
            realAss.setTotalSaleAmount(Integer.parseInt(totalCostHQ));

            if (entriesDao.logAsphaltSale(realAss)) {
                List<AsphaltSaleConsumption> assConsumptions = new ArrayList<>();
                for (StockTrace s : ss.getStats()) {
                    log.info(s.getItemName() + ", " + s.getSubType() + " rate:" + request.getParameter(s.getId() + "rate") + ", quantity:"
                            + request.getParameter(s.getId() + "quantity") + ", cost: " + request.getParameter(s.getId() + "cost"));

                    AsphaltSaleConsumption ass = new AsphaltSaleConsumption();
                    ass.setItemName(s.getItemName() + (s.getSubType() != null ? s.getSubType() : ""));
                    ass.setItemQuantity(Integer.parseInt(request.getParameter(s.getId() + "quantity")));
                    ass.setItemRate(Integer.parseInt(request.getParameter(s.getId() + "rate")));
                    ass.setItemAmount(Integer.parseInt(request.getParameter(s.getId() + "cost")));
                    ass.setAsphlatSaleId(realAss);

                    assConsumptions.add(ass);
                }

                String assLayer = request.getParameter("assLayer");
                String assLayingCostPerTon = request.getParameter("assLayingCostPerTon");
                String totalAssLayingCost = request.getParameter("totalAssLayingCost");
                String assLayingAdvance = request.getParameter("assLayingAdvance");

                AsphaltSaleConsumption ass = new AsphaltSaleConsumption();
                ass.setItemName("LAYED BY " + assLayer);
                ass.setItemQuantity(0);
                ass.setItemRate(Integer.parseInt(assLayingCostPerTon));
                ass.setItemAmount(Integer.parseInt(totalAssLayingCost));
                assConsumptions.add(ass);
                ass.setAsphlatSaleId(realAss);

                boolean logged = entriesDao.logAsphaltSaleConsumptions(assConsumptions);

                if (logged) {
                    if (Integer.parseInt(assLayingAdvance) != Integer.parseInt(totalAssLayingCost)) {
                        // advance payable
                        int payableAmount = Integer.parseInt(totalAssLayingCost) - Integer.parseInt(assLayingAdvance);
                        String payableTo = assLayer;
                        asyncUtil.logAmountPayable(BigInteger.valueOf(payableAmount), payableTo, realAss.getId(), realAss.getProject());
                    }
                    // update stockTrace
                    for (StockTrace s : ss.getStats()) {

                        s.setStockUnits(s.getStockUnits() - Integer.parseInt(request.getParameter(s.getId() + "quantity")));
                        s.setStockAmount(s.getStockAmount() - Integer.parseInt(request.getParameter(s.getId() + "cost")));

                        asyncUtil.updateStockTrace(s);
                    }
                }
            }

            return realAss;
        } catch (Exception e) {
            log.error("Exception while logging Entry:", e);
            return ("01:Failed To Log Entry. Make sure all field are filled in.");
        }
    }
}
