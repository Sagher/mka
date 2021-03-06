/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.dao;

import com.mka.model.AccountPayableReceivable;
import com.mka.model.EntryItems;
import java.util.List;

/**
 *
 * @author Sagher Mehmood
 */
public interface AccountsDao {

    public boolean logAccountPayableReceivable(AccountPayableReceivable accountPayableReceivable);

    public List<AccountPayableReceivable> getAccountPayableReceivable(EntryItems entryItem, String type, String subType,
            int startIndex, int fetchSize,
            String orderBy, String sortBy, String startDate, String endDate, String buyerSupplier, String project);

    public int getAccountPayableReceivableCount(EntryItems entryItem, String type, String startDate, String endDate,
            String buyerSupplier, String project);

    public Object getAllTransactions(String orderBy, String sortby, String startDate, String endDate, String buyerSupplier);

    public Object getAccountPayableReceivable(String type, String[] payablees, int startIndex, int fetchSize, String orderBy, String sortby, String startDate, String endDate, String buyerSupplier);

    public int getAccountPayableReceivableCounts(String type, String[] payablees, String startDate, String endDate, String buyerSupplier);

    public int getHeadOfficeReceivable();
}
