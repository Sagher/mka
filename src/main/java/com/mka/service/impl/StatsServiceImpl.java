/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.service.impl;

import com.mka.dao.StatsDao;
import com.mka.model.StockTrace;
import com.mka.service.StatsService;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Sagher Mehmood
 */
@Service
public class StatsServiceImpl implements StatsService {

    private static final Logger log = Logger.getLogger(StatsServiceImpl.class);

    @Autowired
    StatsDao statsDao;

    private List<StockTrace> statItems = null;

    @Override
    public List<StockTrace> getStats() {

        if (statItems == null) {
            statItems = statsDao.getStats();

        }
        return statItems;
    }

    @Override
    public StockTrace getStockTrace(int typeId) {
        try {

            return statItems.parallelStream().filter(e -> e.getType().getId() == typeId).collect(Collectors.toList()).get(0);
        } catch (Exception e) {

        }
        return null;
    }

    @Override
    public boolean updateStockTrace(StockTrace st) {
        return statsDao.updateStockTrace(st);
    }

    @Override
    public int getAveragePricePerUnit(int itemId) {
        return statsDao.getAveragePricePerUnit(itemId);
    }

}
