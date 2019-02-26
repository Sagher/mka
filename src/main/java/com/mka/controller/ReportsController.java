/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.controller;

import com.mka.model.AccountPayableReceivable;
import com.mka.model.AsphaltSales;
import com.mka.model.CustomersBuyers;
import com.mka.model.EmployeessPayments;
import com.mka.model.EntriesDirect;
import com.mka.model.EntryItems;
import com.mka.model.MasterAccount;
import com.mka.model.StockTrace;
import com.mka.model.User;
import com.mka.model.response.DataTableResp;
import com.mka.service.AccountsService;
import com.mka.service.EmployeesService;
import com.mka.service.EntriesService;
import com.mka.service.StatsService;
import com.mka.service.UserActivityService;
import com.mka.service.UserService;
import com.mka.utils.AsyncUtil;
import com.mka.utils.Constants;
import com.mka.utils.ImageUtil;
import java.time.LocalDate;
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

    @Autowired
    EmployeesService employeesService;

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
            @RequestParam(value = "eitem", required = false, defaultValue = "0") int itemId,
            @RequestParam(value = "from", required = false, defaultValue = "") String from,
            @RequestParam(value = "to", required = false, defaultValue = "") String to,
            @RequestParam(value = "accountName", required = false, defaultValue = "") String accountName) {

        ModelAndView model = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.isAuthenticated()
                && auth.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
            User u = userService.getUser(auth.getName());
            if (u != null) {
                model.addObject("user", u);

                if (type.equalsIgnoreCase(Constants.PAYABLE)) {
                    model.addObject("type", Constants.PAYABLE);
                    return model;

                } else if (type.equalsIgnoreCase(Constants.PROFITLOSS)) {
                    return getProfitAndLossModel();

                } else if (type.equalsIgnoreCase(Constants.CLOSINGSTOCK)) {

                    model.addObject("masterAccount", ss.getMasterAccount());
                    model.addObject("stockTrace", ss.getStats());
                    model.setViewName("subPages/closingStock");
                    return model;

                } else if (type.equalsIgnoreCase(Constants.BALANCESHEET)) {
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

                    model.addObject("allReceivable", ma.getAllReceivable().add(ma.getHeadofficeReceivable()));
                    model.addObject("allPayable", ma.getAllPayable());
                    model.addObject("cashInHand", ma.getCashInHand());
                    model.addObject("totalSalesProfit", getNetProfit());
                    model.addObject("totalStockAmount", totalStockAmount);

                    return model;

                } else if (type.equalsIgnoreCase(Constants.TOTALSALES)) {
                    if (from.isEmpty() && to.isEmpty()) {
                        LocalDate todaydate = LocalDate.now();
                        from = todaydate.withDayOfMonth(1).toString();
                        to = todaydate.toString();
                    }
                    List<AsphaltSales> assSales = ss.getAsphaltSales(from, to);
                    model.addObject("assSales", assSales);
                    model.addObject("from", from);
                    model.addObject("to", to);

                    model.setViewName("subPages/totalSales");
                    return model;

                } else if (type.equalsIgnoreCase(Constants.ACCOUNT_DETAILS)) {
                    accountName = accountName.replaceAll("-", "&");
                    if (from.isEmpty() && to.isEmpty()) {
                        LocalDate todaydate = LocalDate.now();
                        from = todaydate.withDayOfMonth(1).toString();
                        to = todaydate.toString();
                    }
                    // customersBuyers
                    if (!accountName.isEmpty() && (userService.getCustomerAndBuyer(accountName) != null)) {
                        List<AccountPayableReceivable> accountNameTransactions = (List<AccountPayableReceivable>) accountsService.getAllTransactions("", "", from, to, accountName);
                        model.addObject("accountNameTransactions", accountNameTransactions);
                        model.addObject("accountName", accountName);
                    } else {
                        model.addObject("accountName", "");
                    }

                    List<CustomersBuyers> customerBuyers = userService.getCustomersAndBuyers();
                    model.addObject("customerBuyers", customerBuyers);
                    model.addObject("from", from);
                    model.addObject("to", to);

                    model.setViewName("subPages/accountDetails");
                    return model;

                } else if (type.equals("salaryAccount")) {
                    if (from.isEmpty() && to.isEmpty()) {
                        LocalDate todaydate = LocalDate.now();
                        from = todaydate.withDayOfMonth(1).toString();
                        to = todaydate.toString();
                    }

                    List<EmployeessPayments> employeesPayments = employeesService.getEmployeesPaymentRecord(from, to);
                    model.addObject("employeesPayments", employeesPayments);
                    model.addObject("from", from);
                    model.addObject("to", to);

                    model.setViewName("subPages/salaryAccount");
                    return model;

                } else if (type.equals("incomeAndExpenditureAccount")) {
                    if (from.isEmpty() && to.isEmpty()) {
                        LocalDate todaydate = LocalDate.now();
                        from = todaydate.withDayOfMonth(1).toString();
                        to = todaydate.toString();
                    }

                    List<AccountPayableReceivable> accPayRec = accountsService.getAccountPayableReceivable(
                            new EntryItems(18), null, "Cash In Hand To Person", 0,
                            Integer.MAX_VALUE, "", "", from, to, "", "");
                    model.addObject("cashTrans", accPayRec);
                    model.addObject("from", from);
                    model.addObject("to", to);

                    model.setViewName("subPages/incomeAndExpenditureAccount");
                    return model;

                } else if (type.equals("consumptionAndReceiving")) {
                    if (from.isEmpty() && to.isEmpty()) {
                        LocalDate todaydate = LocalDate.now();
                        from = todaydate.withDayOfMonth(1).toString();
                        to = todaydate.toString();
                    }

                    if (itemId > 0) {

                        EntryItems entryItem = entriesService.getEntryItemById(itemId);
                        List<EntriesDirect> data = entriesService.getDirectEntries(entryItem, "", 0, Integer.MAX_VALUE, "", "", from, to, "", "");
                        model.addObject("type", entryItem);
                        model.addObject("data", data);

                    }
                    model.addObject("from", from);
                    model.addObject("to", to);

                    model.setViewName("subPages/consumptionAndReceiving");
                    return model;

                } else if (type.equalsIgnoreCase(Constants.RECEIVABLE)) {
                    model.addObject("type", Constants.RECEIVABLE);
                    return model;

                } else {
                    model.setViewName("redirect:/reports");
                    return model;
                }
            } else {
                model.addObject("errorCode", "401");
                model.addObject("errorMessage", "Unauthorized Access");
                model.addObject("errorDescription", "You are not authorized to view this page");
                model.setViewName("error");
                return model;
            }
        } else {
            model.addObject("msg", "Invalid username and password!");
            log.info("Invalid username and password!");
            model.setViewName("login");
            return model;

        }

    }

    @RequestMapping(value = "/report/data", method = RequestMethod.GET)
    public @ResponseBody
    DataTableResp entriesData(
            HttpServletRequest request,
            @RequestParam(value = "type", required = false, defaultValue = Constants.PAYABLE) String type,
            @RequestParam(value = "start", required = false, defaultValue = "0") int startIndex,
            @RequestParam(value = "buyerSupplier", required = false, defaultValue = "") String buyerSupplier,
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
        buyerSupplier = (!buyerSupplier.isEmpty()) ? buyerSupplier.replace('$', '&') : "";

        Object data;
        int totalSize;
        data = accountsService.getAccountPayableReceivable(type, startIndex, fetchSize, orderBy, sortby, startDate, endDate, buyerSupplier);
        totalSize = accountsService.getAccountPayableReceivableCount(type, startDate, endDate, buyerSupplier);

        if (data != null) {
            List<AccountPayableReceivable> dataList = (List<AccountPayableReceivable>) data;
            if (type.equalsIgnoreCase(Constants.PAYABLE)) {
                if (!buyerSupplier.isEmpty()) {
                    dataList.add(new AccountPayableReceivable(0, "Total " + type, "", 0, true, userService.getCustomerAndBuyer(buyerSupplier).getPayable().intValue()));
                } else {
                    dataList.add(new AccountPayableReceivable(0, "Total " + type, "", 0, true, ss.getMasterAccount().getAllPayable().intValue()));
                }
            } else {
                if (!buyerSupplier.isEmpty()) {
                    dataList.add(new AccountPayableReceivable(0, "Total " + type, "", 0, true, userService.getCustomerAndBuyer(buyerSupplier).getReceivable().intValue()));
                } else {
                    int hr = accountsService.getHeadOfficeReceivable();
                    dataList.add(new AccountPayableReceivable(0, "Head Office Receivable ", "", 0, true, hr));
                    dataList.add(new AccountPayableReceivable(0, "Total " + type, "", 0, true, ss.getMasterAccount().getAllReceivable().intValue() + hr));
                }

            }

        }
        resp.setData(data != null ? data : "");
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

    private int getNetProfit() {
        int totalSales = Constants.STARTING_SALES, directGrossProfit = 0, totalCrushSales = 0, totalAssLaying = 0, totalAssCarr = 0, netProfit = 0;

        for (StockTrace e : ss.getStats()) {
            totalSales += e.getSalesAmount().intValue();
            directGrossProfit += e.getConsumeAmount().intValue();

            if (e.getItemId() == 6) {
                totalCrushSales += e.getConsumeAmount().intValue();
            }
        }

        List<AccountPayableReceivable> indirectExpenses = accountsService.getAccountPayableReceivable(null, Constants.PAYABLE, Constants.EXPENSE, 0, Integer.MAX_VALUE, "", "", "", "", "", "");
        Map<String, Integer> indirectExpMap = new HashMap<>();
        int totalIndirectExpenses = 0;

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
                        indirectExpMap.put(acc.getItemType().getItemName(), (val + acc.getTotalAmount().intValue()));
                        totalIndirectExpenses += acc.getTotalAmount().intValue();
                    } else {
                        indirectExpMap.put(acc.getItemType().getItemName(), acc.getTotalAmount().intValue());
                        totalIndirectExpenses += acc.getTotalAmount().intValue();

                    }
                }
            }
        }

        return (totalSales - directGrossProfit - totalIndirectExpenses);
    }

    private ModelAndView getProfitAndLossModel() {
        ModelAndView model = new ModelAndView();
        model.addObject("stockTrace", ss.getStats());
        model.addObject("openingNetProfit", Constants.STARTING_SALES);
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
        int totalIndirectExpenses = 0;

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
                        indirectExpMap.put(acc.getItemType().getItemName(), (val + acc.getTotalAmount().intValue()));
                        totalIndirectExpenses += acc.getTotalAmount().intValue();
                    } else {
                        indirectExpMap.put(acc.getItemType().getItemName(), acc.getTotalAmount().intValue());
                        totalIndirectExpenses += acc.getTotalAmount().intValue();

                    }
                }
            }
        }

        model.addObject("totalSales", totalSales);
        model.addObject("directGrossProfit", directGrossProfit);
        model.addObject("totalCrushSales", totalCrushSales);
        model.addObject("indirectExpenses", indirectExpMap);
        model.addObject("totalAssLaying", totalAssLaying);
        model.addObject("totalAssCarr", totalAssCarr);
        model.addObject("totalIndirectExpenses", totalIndirectExpenses);

        model.setViewName("subPages/profitLossReport");
        return model;
    }
}
