/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.service.impl;

import com.mka.dao.StatsDao;
import com.mka.model.AccountPayableReceivable;
import com.mka.model.EntryItems;
import com.mka.model.MasterAccount;
import com.mka.model.MasterAccountHistory;
import com.mka.model.StockTrace;
import com.mka.service.StatsService;
import com.mka.utils.Constants;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
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

    private MasterAccount masterAccount = null;

    @Override
    public List<StockTrace> getStats() {

        if (statItems == null) {
            statItems = statsDao.getStats();

        }
        return statItems;
    }

    @Override
    public StockTrace getStockTrace(int typeId, String subType) {
        try {

            if (subType != null && !subType.isEmpty() && !subType.equals("null")) {
                return getStats().parallelStream().filter(e -> e.getType().getId() == typeId && e.getSubType().equalsIgnoreCase(subType))
                        .collect(Collectors.toList()).get(0);

            }
            return getStats().parallelStream().filter(e -> e.getType().getId() == typeId).collect(Collectors.toList()).get(0);
        } catch (Exception e) {

        }
        return null;
    }

    @Override
    public boolean updateStockTrace(StockTrace st) {
        return statsDao.updateStockTrace(st);
    }

    @Override
    public BigDecimal getAveragePricePerUnit(int itemId) {
        return statsDao.getAveragePricePerUnit(itemId);
    }

    @Override
    public MasterAccount getMasterAccount() {
        if (masterAccount == null) {
            masterAccount = statsDao.getMasterAccount();
        }
        return masterAccount;
    }

    @Override
    public String logCashTransaction(MasterAccountHistory mah, String from) {
        MasterAccount ma = getMasterAccount();
        if (mah.getType().equals("+")) {
            mah.setType("To Head Office");
            ma.setTotalCash(ma.getTotalCash().add(mah.getAmount()));
        } else if (mah.getType().equals("-")) {
            mah.setType("From Head Office");
            ma.setTotalCash(ma.getTotalCash().subtract(mah.getAmount()));

        } else if (mah.getType().equals("-+")) {
            if (from.equals("1")) {
                mah.setType("HeadOffice To Cash In Hand");// CASH IN HAND, GIVEN FROM HQ ACCOUNT
                ma.setTotalCash(ma.getTotalCash().subtract(mah.getAmount()));
                ma.setCashInHand(ma.getCashInHand().add(mah.getAmount()));
            } else {
                mah.setType("Person To Cash In Hand");// CASH IN HAND, GIVEN FROM HQ ACCOUNT
                ma.setCashInHand(ma.getCashInHand().add(mah.getAmount()));
            }
        }
        boolean tranLogged = statsDao.logCashTransaction(mah);
        if (tranLogged) {
            updateMasterAccount(ma);
            masterAccount = null;
            return mah.getType();
        }
        return null;
    }

    @Override
    public boolean updateMasterAccount(MasterAccount ma) {
        return statsDao.updateMasterAccount(ma);
    }

    @Override
    public boolean insertStockTraceForNewMonth() {
        statItems = getStats();
        if (statItems != null && !statItems.isEmpty()) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            Date nextMonthFirstDay = calendar.getTime();
            for (StockTrace s : statItems) {
                StockTrace ss = new StockTrace();
                BeanUtils.copyProperties(s, ss);
                ss.setId(null);
                ss.setMonth(Constants.MONTH_FORMAT.format(nextMonthFirstDay));

                statsDao.insertStockTraceForNewMonth(ss);
            }
            return true;
        }
        return false;
    }

}
