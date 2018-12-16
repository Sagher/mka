/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.controller;

import com.mka.model.AccountPayableReceivable;
import com.mka.model.EntryItems;
import com.mka.model.MasterAccount;
import com.mka.model.StockTrace;
import com.mka.model.User;
import com.mka.model.response.DataTableResp;
import com.mka.service.AccountsService;
import com.mka.service.EntriesService;
import com.mka.service.StatsService;
import com.mka.service.UserActivityService;
import com.mka.service.UserService;
import com.mka.utils.AsyncUtil;
import com.mka.utils.Constants;
import com.mka.utils.ImageUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Sagher Mehmood
 */
@Controller
public class ReportsController {

    //logger for logging
    public static Logger log = Logger.getLogger(ReportsController.class);

    @Autowired
    UserService userService;

    @Autowired
    AsyncUtil asyncUtil;

    @Autowired
    UserActivityService activityService;

    @Autowired
    StatsService ss;

    @Autowired
    ImageUtil imageUtil;

    @Autowired
    AccountsService accountsService;

    @Autowired
    EntriesService entriesService;

    private List<AccountPayableReceivable> dataList = null;

    /* 
     * 
     * Model And View Controller for the /users page
     */
    @RequestMapping(value = "/reports", method = RequestMethod.GET)
    public ModelAndView reportsPage(HttpServletRequest request, HttpSession session) {

        ModelAndView model = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.isAuthenticated()
                && auth.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
            User u = userService.getUser(auth.getName());
            if (u != null) {
                model.addObject("user", u);
                List<StockTrace> stockTrace = ss.getStats();
                model.addObject("stockTrace", stockTrace);
                model.setViewName("subPages/reports");
            } else {
                model.addObject("errorCode", "401");
                model.addObject("errorMessage", "Unauthorized Access");
                model.addObject("errorDescription", "You are not authorized to view this page");
                model.setViewName("error");
            }
        } else {
            model.addObject("msg", "Invalid username and password!");
            log.info("Invalid username and password!");
            model.setViewName("login");
        }

        return model;

    }

    @RequestMapping(value = "/report", method = RequestMethod.GET)
    public ModelAndView reportPage(HttpServletRequest request, HttpSession session,
            @RequestParam(value = "type", required = false, defaultValue = Constants.PAYABLE) String type,
            @RequestParam(value = "eitem", required = false, defaultValue = "0") int itemId) {

        ModelAndView model = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.isAuthenticated()
                && auth.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
            User u = userService.getUser(auth.getName());
            if (u != null) {
                model.addObject("user", u);
                if (type.equalsIgnoreCase(Constants.PAYABLE)) {
                    model.addObject("type", Constants.PAYABLE);

                } else if (type.equalsIgnoreCase("profitLoss")) {
                    model.addObject("stockTrace", ss.getStats());
                    int totalSales = 0, directGrossProfit = 0, totalCrushSales = 0, totalAssLaying = 0, totalAssCarr = 0, netProfit = 0;

                    for (StockTrace e : ss.getStats()) {
                        totalSales += e.getSalesAmount().intValue();
                        directGrossProfit += e.getConsumeAmount().intValue();

                        if (e.getItemId() == 6) {
                            totalCrushSales += e.getConsumeAmount().intValue();
                        }
                    }

                    List<AccountPayableReceivable> indirectExpenses = accountsService.getAccountPayableReceivable(null, Constants.PAYABLE, Constants.EXPENSE, 0, Integer.MAX_VALUE, "", "", "", "", "", "");
                    Map<String, Integer> indirectExpMap = new HashMap<>();

                    if (indirectExpenses != null && !indirectExpenses.isEmpty()) {
                        for (AccountPayableReceivable acc : indirectExpenses) {
                            if (!acc.getSubType().equalsIgnoreCase(Constants.EXPENSE)) {
                                if (acc.getItemType().getId() == 20) {
                                    totalAssLaying += acc.getTotalAmount().intValue();
                                }
                                if (acc.getItemType().getId() == 21) {
                                    totalAssCarr += acc.getTotalAmount().intValue();
                                }
                                directGrossProfit += acc.getTotalAmount().intValue();
                            } else {
                                if (indirectExpMap.containsKey(acc.getItemType().getItemName())) {
                                    Integer val = indirectExpMap.get(acc.getItemType().getItemName());
                                    val = val + acc.getTotalAmount().intValue();
                                    indirectExpMap.put(acc.getItemType().getItemName(), val);
                                } else {
                                    indirectExpMap.put(acc.getItemType().getItemName(), acc.getTotalAmount().intValue());
                                }
                            }
                        }
                    }

                    model.addObject("totalSales", totalSales);
                    model.addObject("directGrossProfit", directGrossProfit);
                    model.addObject("totalCrushSales", totalCrushSales);
                    model.addObject("netProfit", netProfit);
                    model.addObject("indirectExpenses", indirectExpMap);
                    model.addObject("totalAssLaying", totalAssLaying);
                    model.addObject("totalAssCarr", totalAssCarr);

                    model.setViewName("subPages/profitLossReport");
                    return model;

                } else if (type.equalsIgnoreCase("closingStock")) {

                    model.addObject("masterAccount", ss.getMasterAccount());
                    model.addObject("stockTrace", ss.getStats());
                    model.setViewName("subPages/closingStock");
                    return model;

                } else if (type.equalsIgnoreCase("balanceSheet")) {
                    MasterAccount ma = ss.getMasterAccount();
                    model.setViewName("subPages/balanceSheet");

                    int totalStockAmount = 0, totalSale = 0;
                    List<StockTrace> stock = ss.getStats();
                    if (stock != null && !stock.isEmpty()) {
                        for (StockTrace s : stock) {
                            totalStockAmount += s.getStockAmount().intValue();
                            totalSale += s.getSalesAmount().intValue();

                        }
                    }

                    model.addObject("allReceivable", ma.getAllReceivable());
                    model.addObject("allPayable", ma.getAllPayable());
                    model.addObject("totalSalesProfit", totalSale);
                    model.addObject("totalStockAmount", totalStockAmount);

                    return model;
                } else {
                    model.addObject("type", Constants.RECEIVABLE);

                }
                model.setViewName("subPages/report");
            } else {
                model.addObject("errorCode", "401");
                model.addObject("errorMessage", "Unauthorized Access");
                model.addObject("errorDescription", "You are not authorized to view this page");
                model.setViewName("error");
            }
        } else {
            model.addObject("msg", "Invalid username and password!");
            log.info("Invalid username and password!");
            model.setViewName("login");
        }

        return model;
    }

    @RequestMapping(value = "/report/data", method = RequestMethod.GET)
    public @ResponseBody
    DataTableResp entriesData(
            HttpServletRequest request,
            @RequestParam(value = "type", required = false, defaultValue = Constants.PAYABLE) String type,
            @RequestParam(value = "start", required = false, defaultValue = "0") int startIndex,
            @RequestParam(value = "itemTypeId", required = false, defaultValue = "0") int itemTypeId,
            @RequestParam(value = "subEntryType", required = false, defaultValue = "") String subEntryType,
            @RequestParam(value = "buyerSupplier", required = false, defaultValue = "") String buyerSupplier,
            @RequestParam(value = "project", required = false, defaultValue = "") String project,
            @RequestParam(value = "length", required = false, defaultValue = "10") int fetchSize,
            @RequestParam(value = "draw", required = false, defaultValue = "0") Integer draw,
            @RequestParam(value = "fromDate", required = false, defaultValue = "") String startDate,
            @RequestParam(value = "toDate", required = false, defaultValue = "") String endDate,
            @RequestParam(value = "search[value]", required = false) String search
    ) {

        DataTableResp resp = new DataTableResp();

        int order0Col = Integer.parseInt(request.getParameter("order[0][column]"));
        String orderBy = request.getParameter("order[0][dir]");
        String sortby = request.getParameter("columns[" + order0Col + "][data]");

        Object data;
        int totalSize;
        if (type.equalsIgnoreCase(Constants.PAYABLE)) {
            if (itemTypeId > 0) {
                EntryItems entryItem = entriesService.getEntryItemById(itemTypeId);
                data = accountsService.getAccountPayableReceivable(entryItem, Constants.PAYABLE, null, startIndex, fetchSize, orderBy, sortby, startDate, endDate, buyerSupplier, project);
                totalSize = accountsService.getAccountPayableReceivableCount(entryItem, Constants.PAYABLE, startDate, endDate, buyerSupplier, project);
            } else {
                data = accountsService.getAccountPayableReceivable(null, Constants.PAYABLE, null, startIndex, fetchSize, orderBy, sortby, startDate, endDate, buyerSupplier, project);
                totalSize = accountsService.getAccountPayableReceivableCount(null, Constants.PAYABLE, startDate, endDate, buyerSupplier, project);
            }
        } else {
            data = accountsService.getAccountPayableReceivable(null, Constants.RECEIVABLE, null, startIndex, fetchSize, orderBy, sortby, startDate, endDate, buyerSupplier, project);
            totalSize = accountsService.getAccountPayableReceivableCount(null, Constants.RECEIVABLE, startDate, endDate, buyerSupplier, project);
        }

        dataList = (data != null) ? (List<AccountPayableReceivable>) data : null;
        if (dataList != null && !dataList.isEmpty()) {
            int tAmount = getTotalAmount(type);
            dataList.add(new AccountPayableReceivable(0, "Total " + type, "", 0, true, tAmount));

            if (!buyerSupplier.isEmpty()) {
                if (type.equalsIgnoreCase(Constants.PAYABLE)
                        && userService.getCustomerAndBuyer(buyerSupplier).getPayable().intValue() > 0) {

                } else if (type.equalsIgnoreCase(Constants.RECEIVABLE)
                        && userService.getCustomerAndBuyer(buyerSupplier).getReceivable().intValue() > 0) {

                } else {
                    dataList = null;
                }
            }
        }
        resp.setData(dataList != null ? dataList : "");
        resp.setRecordsFiltered(totalSize);
        resp.setRecordsTotal(totalSize);
        resp.setDraw(draw);

        return resp;
    }
    //    @RequestMapping(value = "/report/total", method = RequestMethod.GET)
    //    public @ResponseBody
    //    int getTotal(HttpServletRequest request,
    //            @RequestParam(value = "type", required = false, defaultValue = Constants.PAYABLE) String type) {
    //        return getTotalAmount(type);
    //    }

    private int getTotalAmount(String type) {
        int totalAmount = 0;
        if (dataList != null && !dataList.isEmpty()) {
            if (type.equalsIgnoreCase(Constants.PAYABLE)) {
                for (AccountPayableReceivable ac : dataList) {
                    if (ac.getItemType().getId() == 18 && ac.getSubType().startsWith("From")) {
                        totalAmount -= (ac.getTotalAmount().intValue());
                    } else if (ac.getItemType().getId() == 18 && ac.getSubType().equalsIgnoreCase("to head office")) {
                        totalAmount += (ac.getTotalAmount().intValue());
                    } else if (ac.getItemType().getId() == 18 && ac.getSubType().equalsIgnoreCase("Person To Cash In Hand")) {
                        totalAmount += (ac.getTotalAmount().intValue());
                    } else {
                        totalAmount += ac.getAmount().intValue();
                    }
                }
            } else {
                for (AccountPayableReceivable ac : dataList) {
                    if (ac.getItemType().getId() == 18 && ac.getSubType().startsWith("From")) {
                        totalAmount += (ac.getTotalAmount().intValue());
                    } else if (ac.getItemType().getId() == 18 && ac.getSubType().equalsIgnoreCase("to head office")) {
                        totalAmount -= (ac.getTotalAmount().intValue());
                    } else if (ac.getItemType().getId() == 18 && ac.getSubType().equalsIgnoreCase("Person To Cash In Hand")) {
                        totalAmount -= (ac.getTotalAmount().intValue());
                    } else {
                        totalAmount += ac.getAmount().intValue();
                    }
                }
            }

        }
        return totalAmount;
    }

    /*
     *  PROFIT & LOSS
     */
    @RequestMapping(value = "/report/profitLoss", method = RequestMethod.GET)
    public @ResponseBody
    DataTableResp profitLoss(
            HttpServletRequest request,
            @RequestParam(value = "type", required = false, defaultValue = Constants.PAYABLE) String type,
            @RequestParam(value = "start", required = false, defaultValue = "0") int startIndex,
            @RequestParam(value = "itemTypeId", required = false, defaultValue = "0") int itemTypeId,
            @RequestParam(value = "subEntryType", required = false, defaultValue = "") String subEntryType,
            @RequestParam(value = "buyerSupplier", required = false, defaultValue = "") String buyerSupplier,
            @RequestParam(value = "project", required = false, defaultValue = "") String project,
            @RequestParam(value = "length", required = false, defaultValue = "10") int fetchSize,
            @RequestParam(value = "draw", required = false, defaultValue = "0") Integer draw,
            @RequestParam(value = "fromDate", required = false, defaultValue = "") String startDate,
            @RequestParam(value = "toDate", required = false, defaultValue = "") String endDate,
            @RequestParam(value = "search[value]", required = false) String search) {

        DataTableResp resp = new DataTableResp();

        int order0Col = Integer.parseInt(request.getParameter("order[0][column]"));
        String orderBy = request.getParameter("order[0][dir]");
        String sortby = request.getParameter("columns[" + order0Col + "][data]");
        profitLossTotal = 0;
        dataList = accountsService.getAccountPayableReceivable(null, null, null, 0, Integer.MAX_VALUE, orderBy, sortby, startDate, endDate, buyerSupplier, project);

        List<AccountPayableReceivable> toBeRemoved = new ArrayList<>();
        if (dataList != null && !dataList.isEmpty()) {
            for (AccountPayableReceivable acc : dataList) {
                if (acc.getItemType().getId() == 18) {// don't include cash transaction
                    toBeRemoved.add(acc);
                    profitLossTotal = profitLossTotal - 0;
                } else {
                    if (acc.getType().equalsIgnoreCase(Constants.RECEIVABLE)) {
                        profitLossTotal += acc.getAmount().intValue();
                    } else {
                        profitLossTotal = profitLossTotal - acc.getAmount().intValue();
                    }
                }
            }

            dataList.removeAll(toBeRemoved);
            toBeRemoved.clear();
        }
        resp.setData(dataList != null ? dataList : "");
        resp.setRecordsFiltered(dataList != null ? dataList.size() : 0);
        resp.setRecordsTotal(dataList != null ? dataList.size() : 0);
        resp.setDraw(draw);

        return resp;
    }

    int profitLossTotal = 0;

    @RequestMapping(value = "/report/profitLoss/total", method = RequestMethod.GET)
    public @ResponseBody
    int profitLossTotal(HttpServletRequest request) {
        return profitLossTotal;
    }

}
