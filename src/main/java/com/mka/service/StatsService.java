/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.service;

import com.mka.model.StockTrace;
import java.util.List;

/**
 *
 * @author Sagher Mehmood
 */
public interface StatsService {

    public List<StockTrace> getStats();

    public StockTrace getStockTrace(int typeId);

    public boolean updateStockTrace(StockTrace st);
    
    public int getAveragePricePerUnit(int itemId);
}
