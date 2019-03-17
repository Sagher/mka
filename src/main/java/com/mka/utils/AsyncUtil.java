/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.utils;

import com.mka.dao.AccountsDao;
import com.mka.model.AccountPayableReceivable;
import com.mka.model.CustomersBuyers;
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
import java.math.RoundingMode;
import java.util.Date;
import java.util.Iterator;
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
                st.setSalesUnit(st.getSalesUnit().add(entry.getQuantity()));
                st.setStockUnits(st.getStockUnits().subtract(entry.getQuantity()));

                BigDecimal totalSaleAm = entry.getTotalPrice().add(st.getSalesAmount());
                st.setSalesAmount(totalSaleAm);

                BigDecimal remainingSrock = st.getStockAmount().subtract(entry.getTotalPrice());
                st.setStockAmount(remainingSrock);

            } else if (entry.getSubEntryType().equals(Constants.PURCHASE)) {
                st.setPurchaseUnit(st.getPurchaseUnit().add(entry.getQuantity()));
                st.setStockUnits(st.getStockUnits().add(entry.getQuantity()));

                BigDecimal stockAm = st.getStockAmount().add(entry.getTotalPrice());
                st.setStockAmount(stockAm);

                BigDecimal pAm = st.getPurchaseAmount().add(entry.getTotalPrice());
                st.setPurchaseAmount(pAm);

            } else if (entry.getSubEntryType().equals(Constants.PRODUCE)) {
                st.setStockUnits(st.getStockUnits().add(entry.getQuantity()));

            } else if (entry.getSubEntryType().equals(Constants.CONSUME)) {
                st.setStockUnits(st.getStockUnits().subtract(entry.getQuantity()));

                BigDecimal stockAm = st.getStockAmount().subtract(entry.getTotalPrice());
                st.setStockAmount(stockAm);

                st.setConsumeUnit(st.getConsumeUnit().add(entry.getQuantity()));
                st.setConsumeAmount(st.getConsumeAmount().add(entry.getTotalPrice()));
            }

//            st.setAverageUnitPrice(ss.getAveragePricePerUnit(entry.getItem().getId()));
            BigDecimal avgUnitPrice = st.getStockAmount().divide(st.getStockUnits(), 2, RoundingMode.HALF_UP);
            st.setAverageUnitPrice((avgUnitPrice));

            ss.updateStockTrace(st);
        } catch (Exception e) {
            log.error("Exception in updateStockTrace: ", e);
        }
    }

    @Async
    public void updateStockTrace(StockTrace st) {
        try {
            if (st.getStockUnits().intValue() > 0) {
                BigDecimal avgUnitPrice = st.getStockAmount().divide(st.getStockUnits(), 2, RoundingMode.HALF_UP);
                st.setAverageUnitPrice((avgUnitPrice));
            }
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
    public void logDirectAccountPayableReceivable(EntriesDirect entry, String subType) {
        try {
            if (entry.getSubEntryType().equalsIgnoreCase(Constants.SALE) && entry.getTotalPrice() != entry.getAdvance()) {
                // receivable
                AccountPayableReceivable receivable = new AccountPayableReceivable();
                receivable.setAccountName(entry.getBuyer());
                receivable.setAmount(entry.getTotalPrice().subtract(entry.getAdvance()));
                receivable.setQuantity(entry.getQuantity());
                receivable.setRate(entry.getRate());
                receivable.setTotalAmount(entry.getTotalPrice());
                receivable.setEntryId(entry.getId());
                receivable.setIsActive(true);
                receivable.setProject(entry.getProject());
                receivable.setType(Constants.RECEIVABLE);
                receivable.setSubType(subType);
                receivable.setItemType(entry.getItem());
                receivable.setTimestamp(entry.getCreatedDate());
                receivable.setPlantBilty(entry.getPlantBilty());
                receivable.setRecipientBilty(entry.getRecipientBilty());
                receivable.setVehicleNo(entry.getVehicleNo());

                if (!accountsDao.logAccountPayableReceivable(receivable)) {
                    log.warn("*** Account Recevaible not logged ***");
                } else {
                    // update users account
                    updateAccount(receivable);
                }

            } else if (entry.getSubEntryType().equalsIgnoreCase(Constants.PURCHASE)
                    && entry.getTotalPrice() != entry.getAdvance()) {
                //payable
                AccountPayableReceivable payable = new AccountPayableReceivable();
                payable.setAccountName(entry.getSupplier());
                payable.setAmount(entry.getTotalPrice().subtract(entry.getAdvance()));
                payable.setQuantity(entry.getQuantity());
                payable.setRate(entry.getRate());
                payable.setTotalAmount(entry.getTotalPrice());
                payable.setEntryId(entry.getId());
                payable.setIsActive(true);
                payable.setProject(entry.getProject());
                payable.setType(Constants.PAYABLE);
                payable.setSubType(subType);
                payable.setItemType(entry.getItem());
                payable.setTimestamp(entry.getCreatedDate());
                payable.setPlantBilty(entry.getPlantBilty());
                payable.setRecipientBilty(entry.getRecipientBilty());
                payable.setVehicleNo(entry.getVehicleNo());

                if (!accountsDao.logAccountPayableReceivable(payable)) {
                    log.warn("*** Account Payable not logged ***");
                } else {
                    // update users account
                    updateAccount(payable);
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
                payable.setQuantity(BigDecimal.ZERO);
                payable.setRate(BigDecimal.ZERO);
                payable.setTotalAmount(entry.getAmount());
                payable.setEntryId(entry.getId());
                payable.setIsActive(true);
                payable.setProject(entry.getName());
                payable.setType(Constants.PAYABLE);
                payable.setSubType(Constants.EXPENSE);
                payable.setItemType(entry.getItem());
                payable.setTimestamp(entry.getCreatedDate());

                if (!accountsDao.logAccountPayableReceivable(payable)) {
                    log.warn("*** Account Payable not logged ***");
                } else {
                    // update users account
                    updateAccount(payable);
                }
            }
        } catch (Exception e) {

        }
    }

    @Async
    public void logAmountPayable(BigDecimal amount, String payableTo, int entryId, String project, String description,
            BigDecimal quantity, BigDecimal rate, BigDecimal totalAmount, EntryItems eItem,
            String subType, Date entryDate, int plantBilty, int recipientBilty, String vehicleNo) {
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
            payable.setSubType(subType);
            payable.setItemType(eItem);
            payable.setDescription(description);
            payable.setTimestamp(entryDate);
            payable.setPlantBilty(plantBilty);
            payable.setRecipientBilty(recipientBilty);
            payable.setVehicleNo(vehicleNo);

            if (!accountsDao.logAccountPayableReceivable(payable)) {
                log.warn("*** Account Payable not logged ***");
            } else {
                // update users account
                updateAccount(payable);
            }
        } catch (Exception e) {

        }
    }

    @Async
    public void logAmountReceivable(AccountPayableReceivable receivable) {
        try {

            if (!accountsDao.logAccountPayableReceivable(receivable)) {
                log.warn("*** Account Receivable not logged ***");
            } else {
                // update users account

                updateAccount(receivable);
                updateMaReceivable();
            }
        } catch (Exception e) {

        }
    }

    public void logAmountReceivablePayable(AccountPayableReceivable receivable) {
        try {

            if (!accountsDao.logAccountPayableReceivable(receivable)) {
                log.warn("*** Account Receivable/Payable not logged ***");
            } else {
                // update users account

                updateAccount(receivable);
                updateMaReceivable();
            }
        } catch (Exception e) {

        }
    }

    @Async
    public void logCashTran(MasterAccountHistory mah, String subType) {
        // log receivable
        AccountPayableReceivable receivable = new AccountPayableReceivable();
        receivable.setAccountName(mah.getPayee());
        receivable.setAmount(BigDecimal.ZERO);
        receivable.setQuantity(BigDecimal.ZERO);
        receivable.setRate(BigDecimal.ZERO);
        receivable.setTotalAmount(mah.getAmount());
        receivable.setEntryId(mah.getId());
        receivable.setIsActive(true);
        receivable.setProject(null);
        receivable.setSubType(subType);

        if (subType.equalsIgnoreCase(Constants.FROM_HEADOFFICE_TO_PERSON)
                || subType.equalsIgnoreCase(Constants.FROM_HEADOFFICE_TO_CASH_IN_HAND)
                || subType.equalsIgnoreCase(Constants.FROM_CASH_IN_HAND_TO_PERSON)) {
            receivable.setType(Constants.RECEIVABLE);

        } else if (subType.equals(Constants.PERSON_TO_HEADOFFICE)
                || subType.equals(Constants.CASH_IN_HAND_TO_HEADOFFICE)
                || subType.equalsIgnoreCase(Constants.FROM_PERSON_TO_CASH_IN_HAND)) {
            receivable.setType(Constants.PAYABLE);

        }
        receivable.setSubType(subType);
        receivable.setItemType(new EntryItems(18));
        receivable.setDescription(mah.getDescription());
        receivable.setTimestamp(new Date());

        logAmountReceivable(receivable);
    }

    @Async
    public void updateMasterAccount(MasterAccount masterAccount, String payfrom, BigDecimal advance,
            EntryItems entryItem, String entryType, Object entry) {

        if (advance.intValue() > 0) {
            MasterAccountHistory mah = null;
            if (payfrom.equals("1")) {
                if (entryType.equalsIgnoreCase(Constants.SALE)) {
                    masterAccount.setCashInHand(masterAccount.getCashInHand().add(advance));
                } else if (entryType.equalsIgnoreCase(Constants.PURCHASE) || entryType.equalsIgnoreCase(Constants.EXPENSE)) {
                    masterAccount.setCashInHand(masterAccount.getCashInHand().subtract(advance));
                }
            } else if (payfrom.equals("0")) {
                mah = new MasterAccountHistory();
                mah.setAmount(advance);
                mah.setTimestamp(new Date());
                if (entry instanceof EntriesIndirect) {
                    mah.setPayee(((EntriesIndirect) entry).getName());
                } else if (entry instanceof EntriesDirect) {
                    if (entryType.equalsIgnoreCase(Constants.SALE)) {
                        mah.setPayee(((EntriesDirect) entry).getBuyer());
                    } else {
                        mah.setPayee(((EntriesDirect) entry).getSupplier());
                    }
                }
                mah.setDescription(entryItem.getItemName() + " " + entryType);

                if (entryType.equalsIgnoreCase(Constants.SALE)) {
                    mah.setType(Constants.PERSON_TO_HEADOFFICE);
                    masterAccount.setTotalCash(masterAccount.getTotalCash().add(advance));

                } else if (entryType.equalsIgnoreCase(Constants.PURCHASE) || entryType.equalsIgnoreCase(Constants.EXPENSE)) {
                    mah.setType(Constants.FROM_HEADOFFICE_TO_PERSON);
                    masterAccount.setTotalCash(masterAccount.getTotalCash().subtract(advance));
                }
            }
            if (ss.updateMasterAccount(masterAccount) && mah != null) {
                ss.logCashTranHistory(mah);
                updateMaReceivable();
            }
        }
    }

    public void updateMasterAccount(BigDecimal advance, String item) {

        if (advance.intValue() > 0) {
            MasterAccount masterAccount = ss.getMasterAccount();
            MasterAccountHistory mah = new MasterAccountHistory();
            mah.setAmount(advance);
            mah.setTimestamp(new Date());
            mah.setDescription(item);

            mah.setType(Constants.FROM_HEADOFFICE_TO_PERSON);
            masterAccount.setTotalCash(masterAccount.getTotalCash().subtract(advance));
            if (ss.updateMasterAccount(masterAccount)) {
                ss.logCashTranHistory(mah);
                updateMaReceivable();
            }
        }

    }

//    @Async
    public void updateAccount(AccountPayableReceivable account) {
        CustomersBuyers cusBuy = userService.getCustomerAndBuyer(account.getAccountName());
        if (cusBuy != null) {
            String type = account.getType();
            BigDecimal amount = account.getAmount();
            BigDecimal payable = cusBuy.getPayable();
            BigDecimal receivable = cusBuy.getReceivable();

            if (account.getItemType().getId() == 18) {
                // cash tran
                amount = account.getTotalAmount();
            }

            if (type.equalsIgnoreCase(Constants.RECEIVABLE)) {

                if ((payable.compareTo(amount) >= 0)) {
                    cusBuy.setPayable(payable.subtract(amount));
                    cusBuy.setReceivable(BigDecimal.ZERO);

                } else if ((payable.compareTo(amount) < 0)) {
                    cusBuy.setReceivable(receivable.add(payable.subtract(amount).negate()));
                    cusBuy.setPayable(BigDecimal.ZERO);

                } else {
                    cusBuy.setReceivable(receivable.add(amount));
                }

            } else if (type.equalsIgnoreCase(Constants.PAYABLE)) {
                // payable
                if ((receivable.compareTo(amount) >= 0)) {
                    cusBuy.setReceivable(receivable.subtract(amount));
//                    cusBuy.setPayable(BigDecimal.ZERO);

                } else if ((receivable.compareTo(amount) < 0)) {
                    cusBuy.setPayable(payable.add(receivable.subtract(amount).negate()));
                    cusBuy.setReceivable(BigDecimal.ZERO);

                } else {
                    cusBuy.setPayable(cusBuy.getPayable().add(amount));
                }
            }

            if (userService.updateCustomerAndBuyer(cusBuy)) {
                updateAllDues();
            }
        }
    }

    private void updateAllDues() {
        MasterAccount ma = ss.getMasterAccount();
        BigDecimal allPayable = new BigDecimal(0);
        BigDecimal allReceivable = new BigDecimal(0);

        for (CustomersBuyers c : userService.getCustomersAndBuyers()) {
            allPayable = allPayable.add(c.getPayable());
            allReceivable = allReceivable.add(c.getReceivable());
        }

        ma.setAllPayable(allPayable);
        ma.setAllReceivable(allReceivable);

        ss.updateMasterAccount(ma);
    }

    public void updateMaReceivable() {
        int hr = accountsDao.getHeadOfficeReceivable();

        MasterAccount ma = ss.getMasterAccount();
        ma.setHeadofficeReceivable(new BigDecimal(hr));
        ss.updateMasterAccount(ma);

    }

}
