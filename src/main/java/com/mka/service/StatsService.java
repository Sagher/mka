/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.service;

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

    public boolean logCashTransaction(MasterAccountHistory mah);

    public boolean updateMasterAccount(MasterAccount ma);
}
