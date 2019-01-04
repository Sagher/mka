/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.service;

import com.mka.model.AsphaltSaleConsumption;
import com.mka.model.AsphaltSales;
import com.mka.model.MasterAccount;
import com.mka.model.MasterAccountHistory;
import com.mka.model.StockTrace;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Sagher Mehmood
 */
public interface StatsService {

    public List<StockTrace> getStats();

    public boolean insertStockTraceForNewMonth();

    public StockTrace getStockTrace(int typeId, String subType);

    public boolean updateStockTrace(StockTrace st);

    public BigDecimal getAveragePricePerUnit(int itemId);

    public MasterAccount getMasterAccount();

    public boolean logCashTranHistory(MasterAccountHistory mah);

    public String logCashTransaction(MasterAccountHistory mah, String from);

    public boolean updateMasterAccount(MasterAccount ma);

    public Object getCashTransactions(int startIndex, int fetchSize, String orderBy, String sortby, String startDate, String endDate, String buyerSupplier);

    public int getCashTransactionsCount(String startDate, String endDate, String buyerSupplier);

    public AsphaltSales getAsphaltSale(String buyerSupplier, String project);

    public List<AsphaltSaleConsumption> getAsphaltSaleConsumptions(AsphaltSales ass);

}
