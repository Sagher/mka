/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.service.impl;

import com.mka.dao.StatsDao;
import com.mka.model.AsphaltSaleConsumption;
import com.mka.model.AsphaltSales;
import com.mka.model.Employees;
import com.mka.model.EmployeessPayments;
import com.mka.model.MasterAccount;
import com.mka.model.MasterAccountHistory;
import com.mka.model.StockTrace;
import com.mka.service.EmployeesService;
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

    @Autowired
    EmployeesService employeesService;

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
    public boolean logCashTranHistory(MasterAccountHistory mah) {
        return statsDao.logCashTransaction(mah);
    }

    @Override
    public String logCashTransaction(MasterAccountHistory mah, String from, String isSalary) {
        MasterAccount ma = getMasterAccount();
        boolean logSalary = false;
        if (mah.getType().equals("+")) {
            if (from.equals("0")) {
                mah.setType(Constants.PERSON_TO_HEADOFFICE);
                mah.setPayee(mah.getPayee());
                ma.setTotalCash(ma.getTotalCash().add(mah.getAmount()));
            } else {
                mah.setType(Constants.CASH_IN_HAND_TO_HEADOFFICE);
                mah.setPayee(Constants.HEADOFFICE);
                ma.setTotalCash(ma.getTotalCash().add(mah.getAmount()));
                ma.setCashInHand(ma.getCashInHand().subtract(mah.getAmount()));
            }

        } else if (mah.getType().equals("-")) {
            if (from.equals("0")) {
                if ((ma.getTotalCash().intValue() < mah.getAmount().intValue())) {
                    return null;
                }
                mah.setType(Constants.FROM_HEADOFFICE_TO_PERSON);
                mah.setPayee(mah.getPayee());
                ma.setTotalCash(ma.getTotalCash().subtract(mah.getAmount()));
                if (isSalary != null && !isSalary.isEmpty() && isSalary.equalsIgnoreCase("on")) {
                    logSalary = true;
                }
            } else {
                if ((ma.getTotalCash().intValue() < mah.getAmount().intValue())) {
                    return null;
                }
                mah.setType(Constants.FROM_HEADOFFICE_TO_CASH_IN_HAND);
                mah.setPayee(Constants.HEADOFFICE);
                ma.setTotalCash(ma.getTotalCash().subtract(mah.getAmount()));
                ma.setCashInHand(ma.getCashInHand().add(mah.getAmount()));
            }

        } else if (mah.getType().equals("--")) {
            if ((ma.getCashInHand().intValue() < mah.getAmount().intValue())) {
                return null;
            }
            mah.setType(Constants.FROM_CASH_IN_HAND_TO_PERSON);
            ma.setCashInHand(ma.getCashInHand().subtract(mah.getAmount()));
            if (isSalary != null && !isSalary.isEmpty() && isSalary.equalsIgnoreCase("on")) {
                logSalary = true;
            }

        } else if (mah.getType().equals("++")) {
            mah.setType(Constants.FROM_PERSON_TO_CASH_IN_HAND);
            ma.setCashInHand(ma.getCashInHand().add(mah.getAmount()));
        }

        boolean tranLogged = statsDao.logCashTransaction(mah);
        if (tranLogged) {
            updateMasterAccount(ma);
            masterAccount = null;
            if (logSalary) {
                Employees emp = employeesService.getEmployee(mah.getPayee());
                if (emp != null) {
                    EmployeessPayments empPayment = new EmployeessPayments();
                    empPayment.setAmountPayed(mah.getAmount());
                    empPayment.setEmployees(emp);
                    empPayment.setDescription("SALARY PAID");
                    empPayment.setPaymentDate(new Date());
                    empPayment.setType("DEBIT");

                    employeesService.logEmployeePayment(empPayment);
                }
            }
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

    @Override
    public Object getCashTransactions(int startIndex, int fetchSize, String orderBy, String sortby, String startDate, String endDate, String buyerSupplier) {
        return statsDao.getCashTransactions(startIndex, fetchSize, orderBy, sortby, startDate, endDate, buyerSupplier);
    }

    @Override
    public int getCashTransactionsCount(String startDate, String endDate, String buyerSupplier) {
        return statsDao.getCashTransactionsCount(startDate, endDate, buyerSupplier);
    }

    @Override
    public AsphaltSales getAsphaltSale(String buyerSupplier, String project) {
        return statsDao.getAsphaltSale(buyerSupplier, project);
    }

    @Override
    public List<AsphaltSaleConsumption> getAsphaltSaleConsumptions(AsphaltSales ass) {
        return statsDao.getAsphaltSaleConsumptions(ass);
    }

    @Override
    public List<AsphaltSales> getAsphaltSales(String from, String to) {
        return statsDao.getAsphaltSales(from, to);
    }

}
