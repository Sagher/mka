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
                st.setSalesUnit(st.getSalesUnit() + entry.getQuantity());
                st.setStockUnits(st.getStockUnits() - entry.getQuantity());
                st.setSalesAmount(entry.getTotalPrice() + st.getSalesAmount());
                st.setStockAmount(st.getStockAmount() - entry.getTotalPrice());

            } else if (entry.getSubEntryType().equals(Constants.PURCHASE)) {
                st.setPurchaseUnit(st.getPurchaseUnit() + entry.getQuantity());
                st.setStockUnits(st.getStockUnits() + entry.getQuantity());
                st.setStockAmount(st.getStockAmount() + entry.getTotalPrice());
                st.setPurchaseAmount(st.getPurchaseAmount() + entry.getTotalPrice());

            } else if (entry.getSubEntryType().equals(Constants.PRODUCE)) {
                st.setStockUnits(st.getStockUnits() + entry.getQuantity());
            }

//            st.setAverageUnitPrice(ss.getAveragePricePerUnit(entry.getItem().getId()));
            st.setAverageUnitPrice(BigDecimal.valueOf((st.getStockAmount() / st.getStockUnits())));

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
    public void updateDirectAccountPayableReceivable(EntriesDirect entry) {
        try {
            if (entry.getSubEntryType().equalsIgnoreCase(Constants.SALE) && entry.getTotalPrice() != entry.getAdvance()) {
                // receivable
                AccountPayableReceivable receivable = new AccountPayableReceivable();
                receivable.setAccountName(entry.getBuyer());
                receivable.setAmount(BigInteger.valueOf(entry.getTotalPrice() - entry.getAdvance()));
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
                payable.setAmount(BigInteger.valueOf(entry.getTotalPrice() - entry.getAdvance()));
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

    public void updateIndirectAccountPayableReceivable(EntriesIndirect entry) {
        try {
            if (entry.getAmount() != entry.getAdvance()) {
                //payable
                AccountPayableReceivable payable = new AccountPayableReceivable();
                payable.setAccountName(entry.getName());
                payable.setAmount(BigInteger.valueOf(entry.getAmount() - entry.getAdvance()));
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

    public void logAmountPayable(BigInteger amount, String payableTo, int entryId, String project) {
        try {
            //payable
            AccountPayableReceivable payable = new AccountPayableReceivable();
            payable.setAccountName(payableTo);
            payable.setAmount(amount);
            payable.setEntryId(entryId);
            payable.setIsActive(true);
            payable.setProject(project);
            payable.setType(Constants.PAYABLE);
            payable.setItemType(new EntryItems(17));

            if (!accountsDao.logAccountPayableReceivable(payable)) {
                log.warn("*** Account Payable not logged ***");
            }
        } catch (Exception e) {

        }
    }

    public void updateMasterAccount(MasterAccount masterAccount, String payfrom, int advance) {
        if (payfrom.equals("1")) {
            masterAccount.setCashInHand(masterAccount.getCashInHand() - advance);
        } else if (payfrom.equals("0")) {
            masterAccount.setTotalCash(masterAccount.getTotalCash() - advance);
        }
        ss.updateMasterAccount(masterAccount);
    }

}
