/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.service.impl;

import com.mka.dao.AccountsDao;
import com.mka.model.AccountPayableReceivable;
import com.mka.model.CustomersBuyers;
import com.mka.model.EntryItems;
import com.mka.service.AccountsService;
import com.mka.service.UserService;
import com.mka.utils.Constants;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Sagher Mehmood
 */
@Service
public class AccountsServiceImpl implements AccountsService {

    private static final Logger log = Logger.getLogger(AccountsServiceImpl.class);

    @Autowired
    private AccountsDao accountsDao;

    @Autowired
    private UserService userService;

    @Override
    public boolean logAccountPayableReceivable(AccountPayableReceivable accountPayableReceivable) {
        return accountsDao.logAccountPayableReceivable(accountPayableReceivable);
    }

    @Override
    public List<AccountPayableReceivable> getAccountPayableReceivable(EntryItems entryItem, String type, String subType,
            int startIndex, int fetchSize, String orderBy, String sortBy, String startDate,
            String endDate, String buyerSupplier, String project) {
        return accountsDao.getAccountPayableReceivable(entryItem, type, subType, startIndex, fetchSize, orderBy, sortBy, startDate, endDate, buyerSupplier, project);
    }

    @Override
    public int getAccountPayableReceivableCount(EntryItems entryItem, String type, String startDate, String endDate, String buyerSupplier, String project) {
        return accountsDao.getAccountPayableReceivableCount(entryItem, type, startDate, endDate, buyerSupplier, project);
    }

    @Override
    public Object getAllTransactions(String orderBy, String sortby, String startDate, String endDate, String buyerSupplier) {
        return accountsDao.getAllTransactions(orderBy, sortby, startDate, endDate, buyerSupplier);
    }

    @Override
    public Object getAccountPayableReceivable(String type, int startIndex, int fetchSize, String orderBy, String sortby, String startDate, String endDate, String buyerSupplier) {
        if (type.equalsIgnoreCase(Constants.PAYABLE)) {
            List<String> payablees = new ArrayList<>();
            for (CustomersBuyers c : userService.getCustomersAndBuyers()) {
                if (c.getPayable().intValue() > 0) {
                    payablees.add(c.getName());
                }
            }
            if (payablees.isEmpty()) {
                return null;
            }
            List<AccountPayableReceivable> s = (List<AccountPayableReceivable>) accountsDao.getAccountPayableReceivable(Constants.PAYABLE, payablees.toArray(new String[payablees.size()]), startIndex, fetchSize, orderBy, sortby, startDate, endDate, buyerSupplier);

            return s;
        } else {
            List<String> receivablees = new ArrayList<>();
            for (CustomersBuyers c : userService.getCustomersAndBuyers()) {
                if (c.getReceivable().intValue() > 0) {
                    receivablees.add(c.getName());
                }
            }
            if (receivablees.isEmpty()) {
                List<AccountPayableReceivable> s = new ArrayList<>();
//                s.add(new AccountPayableReceivable(0, "Head Office Receivable ", "", 0, true, hr));
                return s;
            }
            List<AccountPayableReceivable> data = (List<AccountPayableReceivable>) accountsDao.getAccountPayableReceivable(Constants.RECEIVABLE, receivablees.toArray(new String[receivablees.size()]), startIndex, fetchSize, orderBy, sortby, startDate, endDate, buyerSupplier);
            return data;
        }
    }

    @Override
    public int getAccountPayableReceivableCount(String type, String startDate, String endDate, String buyerSupplier) {
        if (type.equalsIgnoreCase(Constants.PAYABLE)) {
            List<String> payablees = new ArrayList<>();
            for (CustomersBuyers c : userService.getCustomersAndBuyers()) {
                if (c.getPayable().intValue() > 0) {
                    payablees.add(c.getName());
                }
            }
            return accountsDao.getAccountPayableReceivableCounts(Constants.PAYABLE, payablees.toArray(new String[payablees.size()]), startDate, endDate, buyerSupplier);
        } else {
            List<String> receivablees = new ArrayList<>();
            for (CustomersBuyers c : userService.getCustomersAndBuyers()) {
                if (c.getReceivable().intValue() > 0) {
                    receivablees.add(c.getName());
                }
            }
            return accountsDao.getAccountPayableReceivableCounts(Constants.RECEIVABLE, receivablees.toArray(new String[receivablees.size()]), startDate, endDate, buyerSupplier);
        }
    }

    @Override
    public int getHeadOfficeReceivable() {
        return accountsDao.getHeadOfficeReceivable();
    }

}
