/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.service.impl;

import com.mka.dao.AccountsDao;
import com.mka.model.AccountPayableReceivable;
import com.mka.model.EntryItems;
import com.mka.service.AccountsService;
import com.mka.utils.Constants;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
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

    @Override
    public boolean logAccountPayableReceivable(AccountPayableReceivable accountPayableReceivable) {
        return accountsDao.logAccountPayableReceivable(accountPayableReceivable);
    }

    @Override
    public List<AccountPayableReceivable> getAccountPayableReceivable(EntryItems entryItem, String type,
            int startIndex, int fetchSize, String orderBy, String sortBy, String startDate,
            String endDate, String buyerSupplier, String project) {
        return accountsDao.getAccountPayableReceivable(entryItem, type, startIndex, fetchSize, orderBy, sortBy, startDate, endDate, buyerSupplier, project);
    }

    @Override
    public int getAccountPayableReceivableCount(EntryItems entryItem, String type, String startDate, String endDate, String buyerSupplier, String project) {
        return accountsDao.getAccountPayableReceivableCount(entryItem, type, startDate, endDate, buyerSupplier, project);
    }

}
