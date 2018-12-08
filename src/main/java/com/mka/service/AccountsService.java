/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.service;

import com.mka.model.AccountPayableReceivable;
import com.mka.model.EntryItems;
import java.util.List;

/**
 *
 * @author Sagher Mehmood
 */
public interface AccountsService {

    public boolean logAccountPayableReceivable(AccountPayableReceivable accountPayableReceivable);

    public List<AccountPayableReceivable> getAccountPayableReceivable(EntryItems entryItem,
            String type, String subType, int startIndex, int fetchSize,
            String orderBy, String sortBy, String startDate, String endDate, String buyerSupplier, String project);

    public int getAccountPayableReceivableCount(EntryItems entryItem, String type, String startDate, String endDate,
            String buyerSupplier, String project);
}
