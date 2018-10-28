/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.dao;

import com.mka.model.MasterAccount;
import com.mka.model.MasterAccountHistory;
import com.mka.model.StockTrace;
import java.util.List;

/**
 *
 * @author Sagher Mehmood
 */
public interface StatsDao {

    public List<StockTrace> getStats();

    public boolean updateStockTrace(StockTrace st);

    public int getAveragePricePerUnit(int itemId);
    
    public MasterAccount getMasterAccount();

    public boolean logCashTransaction(MasterAccountHistory mah);

    public boolean updateMasterAccount(MasterAccount ma);
}
