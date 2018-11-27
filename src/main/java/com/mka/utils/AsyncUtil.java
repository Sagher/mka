/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.utils;

import com.mka.dao.AccountsDao;
import com.mka.model.AccountPayableReceivable;
import com.mka.model.EntriesDirect;
import com.mka.model.EntriesIndirect;
import com.mka.model.EntryItems;
import com.mka.model.MasterAccount;
import com.mka.model.MasterAccountHistory;
import com.mka.model.StockTrace;
import com.mka.model.User;
import com.mka.model.UserActivity;
import com.mka.service.StatsService;
import com.mka.service.UserActivityService;
import com.mka.service.UserService;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 *
 * @author Sagher Mehmood
 */
@Component
public class AsyncUtil {

    private static final Logger log = Logger.getLogger(AsyncUtil.class);

    @Autowired
    UserActivityService activityService;

    @Autowired
    UserService userService;

    @Autowired
    StatsService ss;

    @Autowired
    AccountsDao accountsDao;

//    @Autowired
//    EntriesService entriesService;
    @Async
    public void logActivity(HttpServletRequest request, String userName, String remoteAddr, String ua, String actionType, String actionDescription) {
        User user = userService.getUser(userName);
        if (user != null) {
            UserActivity activity = new UserActivity(actionType, actionDescription, remoteAddr, ua, new Date(), user);
            boolean activtyLogged = activityService.addUserActivity(activity);

            if (!activtyLogged) {
                log.warn("FAILED TO LOG USER ACTIVITY: " + activity);
            }
        } else {
            log.warn("Invalid User in logActivty: " + userName);
        }
    }

    @Async
    public void updateUser(User u) {
        boolean resp = userService.updateUser(u);
        if (!resp) {
            log.warn("Failed to update user Asynchronously" + u);
        }
    }

    @Async
    public void updateStockTrace(EntriesDirect entry) {
        try {
            StockTrace st = ss.getStockTrace(entry.getItem().getId(),
                    entry.getEntriesDirectDetails() != null ? entry.getEntriesDirectDetails().getSubType() : null);
            if (entry.getSubEntryType().equals(Constants.SALE)) {
                st.setSalesUnit(st.getSalesUnit() + entry.getQuantity().intValue());
                st.setStockUnits(st.getStockUnits() - entry.getQuantity().intValue());

                BigDecimal totalSaleAm = entry.getTotalPrice().add(st.getSalesAmount());
                st.setSalesAmount(totalSaleAm);

                BigDecimal remainingSrock = st.getStockAmount().subtract(entry.getTotalPrice());
                st.setStockAmount(remainingSrock);

            } else if (entry.getSubEntryType().equals(Constants.PURCHASE)) {
                st.setPurchaseUnit(st.getPurchaseUnit() + entry.getQuantity().intValue());
                st.setStockUnits(st.getStockUnits() + entry.getQuantity().intValue());

                BigDecimal stockAm = st.getStockAmount().add(entry.getTotalPrice());
                st.setStockAmount(stockAm);

                BigDecimal pAm = st.getPurchaseAmount().add(entry.getTotalPrice());
                st.setPurchaseAmount(pAm);

            } else if (entry.getSubEntryType().equals(Constants.PRODUCE)) {
                st.setStockUnits(st.getStockUnits() + entry.getQuantity().intValue());

            } else if (entry.getSubEntryType().equals(Constants.CONSUME)) {
                st.setStockUnits(st.getStockUnits() - entry.getQuantity().intValue());

                BigDecimal stockAm = st.getStockAmount().subtract(entry.getTotalPrice());
                st.setStockAmount(stockAm);
            }

//            st.setAverageUnitPrice(ss.getAveragePricePerUnit(entry.getItem().getId()));
            int avgUnitPrice = st.getStockAmount().intValue() / st.getStockUnits();
            st.setAverageUnitPrice(BigDecimal.valueOf(avgUnitPrice));

            ss.updateStockTrace(st);
        } catch (Exception e) {
            log.error("Exception in updateStockTrace: ", e);
        }
    }

    @Async
    public void updateStockTrace(StockTrace st) {
        try {

            st.setAverageUnitPrice(ss.getAveragePricePerUnit(st.getItemId()));
            ss.updateStockTrace(st);
        } catch (Exception e) {
            log.error("Exception in updateStockTrace: ", e);
        }
    }

    @Async
    public void addToCustomersAndBuyersList(String customerBuyerSupplier) {
        userService.addCustomerAndBuyer(customerBuyerSupplier);
    }

    @Async
    public void addToProjectsList(String projName) {
        userService.addProject(projName);
    }

//    @Async
//    public void addEntryDetail(EntriesDirectDetails entryDetail) {
//        entriesService.addEntryDetail(entryDetail);
//    }
    @Async
    public void updateDirectAccountPayableReceivable(EntriesDirect entry) {
        try {
            if (entry.getSubEntryType().equalsIgnoreCase(Constants.SALE) && entry.getTotalPrice() != entry.getAdvance()) {
                // receivable
                AccountPayableReceivable receivable = new AccountPayableReceivable();
                receivable.setAccountName(entry.getBuyer());
                receivable.setAmount(entry.getTotalPrice().subtract(entry.getAdvance()));
                receivable.setQuantity(entry.getQuantity().intValue());
                receivable.setRate(entry.getRate());
                receivable.setTotalAmount(entry.getTotalPrice());
                receivable.setEntryId(entry.getId());
                receivable.setIsActive(true);
                receivable.setProject(entry.getProject());
                receivable.setType(Constants.RECEIVABLE);
                receivable.setItemType(entry.getItem());

                if (!accountsDao.logAccountPayableReceivable(receivable)) {
                    log.warn("*** Account Recevaible not logged ***");
                }

            } else if (entry.getSubEntryType().equalsIgnoreCase(Constants.PURCHASE) && entry.getTotalPrice() != entry.getAdvance()) {
                //payable
                AccountPayableReceivable payable = new AccountPayableReceivable();
                payable.setAccountName(entry.getSupplier());
                payable.setAmount(entry.getTotalPrice().subtract(entry.getAdvance()));
                payable.setQuantity(entry.getQuantity().intValue());
                payable.setRate(entry.getRate());
                payable.setTotalAmount(entry.getTotalPrice());
                payable.setEntryId(entry.getId());
                payable.setIsActive(true);
                payable.setProject(entry.getProject());
                payable.setType(Constants.PAYABLE);
                payable.setItemType(entry.getItem());

                if (!accountsDao.logAccountPayableReceivable(payable)) {
                    log.warn("*** Account Payable not logged ***");
                }
            }
        } catch (Exception e) {

        }
    }

    @Async
    public void updateIndirectAccountPayableReceivable(EntriesIndirect entry) {
        try {
            if (entry.getAmount() != entry.getAdvance()) {
                //payable
                AccountPayableReceivable payable = new AccountPayableReceivable();
                payable.setAccountName(entry.getName());
                payable.setAmount(entry.getAmount().subtract(entry.getAdvance()));
                payable.setQuantity(0);
                payable.setRate(BigDecimal.ZERO);
                payable.setTotalAmount(entry.getAmount());
                payable.setEntryId(entry.getId());
                payable.setIsActive(true);
                payable.setProject(entry.getName());
                payable.setType(Constants.PAYABLE);
                payable.setItemType(entry.getItem());

                if (!accountsDao.logAccountPayableReceivable(payable)) {
                    log.warn("*** Account Payable not logged ***");
                }
            }
        } catch (Exception e) {

        }
    }

    @Async
    public void logAmountPayable(BigDecimal amount, String payableTo, int entryId, String project, String description,
            int quantity, BigDecimal rate, BigDecimal totalAmount, EntryItems eItem) {
        try {
            //payable
            AccountPayableReceivable payable = new AccountPayableReceivable();
            payable.setAccountName(payableTo);
            payable.setAmount(amount);
            payable.setQuantity(quantity);
            payable.setRate(rate);
            payable.setTotalAmount(totalAmount);
            payable.setEntryId(entryId);
            payable.setIsActive(true);
            payable.setProject(project);
            payable.setType(Constants.PAYABLE);
            payable.setItemType(eItem);
            payable.setDescription(description);

            if (!accountsDao.logAccountPayableReceivable(payable)) {
                log.warn("*** Account Payable not logged ***");
            }
        } catch (Exception e) {

        }
    }

    @Async
    public void logAmountReceivable(AccountPayableReceivable receivable) {
        try {

            if (!accountsDao.logAccountPayableReceivable(receivable)) {
                log.warn("*** Account Receivable not logged ***");
            }
        } catch (Exception e) {

        }
    }

    @Async
    public void logCashTran(MasterAccountHistory mah, String type) {
        // log receivable
        AccountPayableReceivable receivable = new AccountPayableReceivable();
        receivable.setAccountName(mah.getPayee());
        receivable.setAmount(BigDecimal.ZERO);
        receivable.setQuantity(0);
        receivable.setRate(BigDecimal.ZERO);
        receivable.setTotalAmount(mah.getAmount());
        receivable.setEntryId(mah.getId());
        receivable.setIsActive(true);
        receivable.setProject(null);
        receivable.setType(type);
        receivable.setItemType(new EntryItems(18));
        receivable.setDescription(mah.getDescription());

        logAmountReceivable(receivable);
    }

    @Async
    public void updateMasterAccount(MasterAccount masterAccount, String payfrom, int advance) {
        if (payfrom.equals("1")) {
            masterAccount.setCashInHand(masterAccount.getCashInHand().subtract(BigDecimal.valueOf(advance)));
        } else if (payfrom.equals("0")) {
            masterAccount.setTotalCash(masterAccount.getTotalCash().subtract(BigDecimal.valueOf(advance)));
        }
        ss.updateMasterAccount(masterAccount);
    }

}
