/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.controller;

import static com.mka.controller.UserController.log;
import com.mka.model.EntryItems;
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
import java.util.List;
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
            @RequestParam(value = "search[value]", required = false) String search) {

        DataTableResp resp = new DataTableResp();

        int order0Col = Integer.parseInt(request.getParameter("order[0][column]"));
        String orderBy = request.getParameter("order[0][dir]");
        String sortby = request.getParameter("columns[" + order0Col + "][data]");

        Object data;
        int totalSize;
        if (type.equalsIgnoreCase(Constants.PAYABLE)) {
            if (itemTypeId > 0) {
                EntryItems entryItem = entriesService.getEntryItemById(itemTypeId);
                data = accountsService.getAccountPayableReceivable(entryItem, Constants.PAYABLE, startIndex, fetchSize, orderBy, sortby, startDate, endDate, buyerSupplier, project);
                totalSize = accountsService.getAccountPayableReceivableCount(entryItem, Constants.PAYABLE, startDate, endDate, buyerSupplier, project);
            } else {
                data = accountsService.getAccountPayableReceivable(null, Constants.PAYABLE, startIndex, fetchSize, orderBy, sortby, startDate, endDate, buyerSupplier, project);
                totalSize = accountsService.getAccountPayableReceivableCount(null, Constants.PAYABLE, startDate, endDate, buyerSupplier, project);
            }
        } else {
            data = accountsService.getAccountPayableReceivable(null, Constants.RECEIVABLE, startIndex, fetchSize, orderBy, sortby, startDate, endDate, buyerSupplier, project);
            totalSize = accountsService.getAccountPayableReceivableCount(null, Constants.RECEIVABLE, startDate, endDate, buyerSupplier, project);
        }

        resp.setData(data != null ? data : "");
        resp.setRecordsFiltered(totalSize);
        resp.setRecordsTotal(totalSize);
        resp.setDraw(draw);

        return resp;
    }
}
