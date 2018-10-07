/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.model.response;

/**
 *
 * @author Sagher Mehmood
 */
public class StatItem {

    private Object item, subRntryType, averageRate, totalQuantity, totalValue;

    public StatItem(Object item, Object subRntryType, Object averageRate, Object totalQuantity, Object totalValue) {
        this.item = item;
        this.subRntryType = subRntryType;
        this.averageRate = averageRate;
        this.totalValue = totalValue;
    }

    public Object getItem() {
        return item;
    }

    public void setItem(Object item) {
        this.item = item;
    }

    public Object getSubRntryType() {
        return subRntryType;
    }

    public void setSubRntryType(Object subRntryType) {
        this.subRntryType = subRntryType;
    }

    public Object getAverageRate() {
        return averageRate;
    }

    public void setAverageRate(Object averageRate) {
        this.averageRate = averageRate;
    }

    public Object getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Object totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Object getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Object totalValue) {
        this.totalValue = totalValue;
    }

}
