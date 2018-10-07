/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.model.response;

import com.mka.model.EntryItems;
import java.util.Map;

/**
 *
 * @author Sagher Mehmood
 */
public class Stats {

    private int totalSales, totalPurchases, totalStockValue;

    private Map<EntryItems, ItemStats> stockStats;

    public int getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(int totalSales) {
        this.totalSales = totalSales;
    }

    public int getTotalPurchases() {
        return totalPurchases;
    }

    public void setTotalPurchases(int totalPurchases) {
        this.totalPurchases = totalPurchases;
    }

    public int getTotalStockValue() {
        return totalStockValue;
    }

    public void setTotalStockValue(int totalStockValue) {
        this.totalStockValue = totalStockValue;
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
