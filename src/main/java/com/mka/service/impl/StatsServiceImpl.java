/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.service.impl;

import com.mka.dao.StatsDao;
import com.mka.model.response.StatItem;
import com.mka.model.response.Stats;
import com.mka.service.StatsService;
import com.mka.utils.Constants;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Sagher Mehmood
 */
@Service
public class StatsServiceImpl implements StatsService {

    @Autowired
    StatsDao statsDao;

    private int purchase = 0, sale = 0, produce = 0;

    private Map<Integer, ItemStats> itemWiseStats = new HashMap<>();

    @Override
    public Stats getStats() {
        Stats stats = new Stats();

        List<StatItem> statItems = statsDao.getStats();
        statItems.parallelStream().filter((StatItem e) -> {
            if (e.getSubRntryType().equals(Constants.SALE)) {
                sale += (Integer) e.getTotalValue();
            } else if (e.getSubRntryType().equals(Constants.PURCHASE)) {
                purchase += (Integer) e.getTotalValue();
            } else if (e.getSubRntryType().equals(Constants.PRODUCE)) {
                produce += (Integer) e.getTotalValue();
            }
            if (itemWiseStats.containsKey((Integer) e.getItem())) {
                ItemStats is = itemWiseStats.get((Integer) e.getItem());
                itemWiseStats.put((Integer) e.getItem(), is);
            }else{
                ItemStats is = new ItemStats();
                is.setSale(sale);
            }
            return true;
        }).collect(Collectors.toList());
        return null;
    }

}

class ItemStats {

    private int sale, stock, price;

    public int getSale() {
        return sale;
    }

    public void setSale(int sale) {
        this.sale = sale;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

}
