/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.controller;

import com.mka.model.AccountPayableReceivable;
import com.mka.model.AsphaltSaleConsumption;
import com.mka.model.AsphaltSales;
import com.mka.model.CustomersBuyers;
import com.mka.model.EntriesDirect;
import com.mka.model.EntriesIndirect;
import com.mka.model.EntryItems;
import com.mka.model.MasterAccount;
import com.mka.model.MasterAccountHistory;
import com.mka.model.Projects;
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
import com.sun.javafx.tk.quantum.MasterTimer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
public class EntriesController {

    //logger for logging
    public static Logger log = Logger.getLogger(EntriesController.class);

    @Autowired
    UserService userService;

    @Autowired
    AsyncUtil asyncUtil;

    @Autowired
    UserActivityService activityService;

    @Autowired
    ImageUtil imageUtil;

    @Autowired
    EntriesService entriesService;

    @Autowired
    StatsService ss;

    @Autowired
    AccountsService accountsService;

    /* 
     * 
     * Model And View Controller for the /users page
     */
    @RequestMapping(value = "/entry", method = RequestMethod.GET)
    public ModelAndView reportsPage(HttpServletRequest request, HttpSession session) {

        ModelAndView model = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.isAuthenticated()
                && auth.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
            User u = userService.getUser(auth.getName());
            if (u != null && (u.getRole().equals(Constants.ROLE_ADMIN) || u.getRole().equals(Constants.ROLE_MANAGER))) {
                model.addObject("user", u);
                model.addObject("allRoles", Constants.ALL_ROLES);
                model.addObject("users", userService.getAllUsers());
                model.addObject("masterAccount", ss.getMasterAccount());
                model.addObject("stockTrace", ss.getStats());

                model.setViewName("subPages/entry");
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

    @RequestMapping(value = "/entries", method = RequestMethod.GET)
    public ModelAndView entries(
            HttpServletRequest request, HttpSession session,
            @RequestParam(value = "type", required = false, defaultValue = Constants.DIRECT) String type,
            @RequestParam(value = "eitem", required = false, defaultValue = "0") int itemId) {

        ModelAndView model = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.isAuthenticated()
                && auth.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
            User u = userService.getUser(auth.getName());
            if (u != null) {
                model.addObject("user", u);
                model.addObject("allRoles", Constants.ALL_ROLES);
                model.addObject("users", userService.getAllUsers());
                if (type.equalsIgnoreCase(Constants.DIRECT)) {
                    StockTrace st = ss.getStockTrace(itemId, null);
                    if (itemId != 0 && st != null) {
                        model.addObject("itemType", st.getType());
                        model.addObject("stockTrace", st);
                    }
                    model.setViewName("subPages/directEntries");
                } else if (type.equalsIgnoreCase("indirect")) {
                    model.setViewName("subPages/indirectEntries");

                } else if (type.equalsIgnoreCase(Constants.CASH_TRANSACTIONS)) {
                    model.setViewName("subPages/cashTransactions");

                } else if (type.equalsIgnoreCase(Constants.CUSTOMERS_BUYERS)) {
                    model.setViewName("subPages/customersBuyers");

                } else {
                    model.setViewName("redirect:/");

                }
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

    @RequestMapping(value = "/entries/{id}/detail", method = RequestMethod.GET)
    public ModelAndView entriesDetail(
            HttpServletRequest request, HttpSession session,
            @PathVariable(value = "id", required = true) int id) {

        ModelAndView model = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.isAuthenticated()
                && auth.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
            User u = userService.getUser(auth.getName());
            CustomersBuyers cusBuy = userService.getCustomerAndBuyer(id);
            if (u != null && cusBuy != null) {
                model.addObject("user", u);
                model.addObject("cusBuy", cusBuy);
                model.addObject("allRoles", Constants.ALL_ROLES);
                model.addObject("users", userService.getAllUsers());
                model.setViewName("subPages/customerBuyerAccountDetail");

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

    @RequestMapping(value = "/getEntryItemsList")
    public @ResponseBody
    List<EntryItems> getEntryItemsList() {
        return entriesService.getAllEntryItems();
    }

    @RequestMapping(value = "/getEntryItem")
    public @ResponseBody
    EntryItems getEntryItem(int id) {
        return entriesService.getEntryItemById(id);
    }

    @RequestMapping(value = "/logDirectEntry", method = RequestMethod.POST)
    public @ResponseBody
    String logDirectEntry(HttpServletRequest request, HttpSession httpSession) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object entryLogged = entriesService.logDirectEntry(request);
        if (entryLogged instanceof EntriesDirect) {
            logActivity(request, auth.getName(), "ADDED ENTRY", entryLogged.toString());
            return "00:Entry Logged Successfully";
        } else if (entryLogged instanceof String) {
            return ((String) entryLogged);
        }
        return ("01:Invalid Values");
    }

    @RequestMapping(value = "/logInDirectEntry", method = RequestMethod.POST)
    public @ResponseBody
    String logInDirectEntry(HttpServletRequest request, HttpSession httpSession) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Object entryLogged = entriesService.logInDirectEntry(request);
        if (entryLogged instanceof EntriesIndirect) {
            logActivity(request, auth.getName(), "ADDED ENTRY", entryLogged.toString());
            return "00:Entry Logged Successfully";
        } else {
            return ("01:Failed To Log Entry. Make sure all field are filled in.");

        }
    }

    @RequestMapping(value = "/entries/data", method = RequestMethod.GET)
    public @ResponseBody
    DataTableResp entriesData(
            HttpServletRequest request,
            @RequestParam(value = "type", required = false, defaultValue = Constants.DIRECT) String type,
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

        int order0Col = Integer.parseInt(request.getParameter("order[0][column]") != null
                ? request.getParameter("order[0][column]") : "0");
        String orderBy = request.getParameter("order[0][dir]") != null ? request.getParameter("order[0][dir]") : "";
        String sortby = request.getParameter("columns[" + order0Col + "][data]");
        buyerSupplier = (!buyerSupplier.isEmpty()) ? buyerSupplier.replace('$', '&') : "";

        Object data = null;
        int totalSize = 0;
        if (type.equalsIgnoreCase(Constants.DIRECT)) {
            if (itemTypeId > 0) {
                EntryItems entryItem = entriesService.getEntryItemById(itemTypeId);

                data = entriesService.getDirectEntries(entryItem, subEntryType, startIndex, fetchSize, orderBy, sortby, startDate, endDate, buyerSupplier, project);
                totalSize = entriesService.getDirectEntriesCount(entryItem, subEntryType, startDate, endDate, buyerSupplier, project);
            } else {
                data = entriesService.getDirectEntries(null, subEntryType, startIndex, fetchSize, orderBy, sortby, startDate, endDate, buyerSupplier, project);
                totalSize = entriesService.getDirectEntriesCount(null, subEntryType, startDate, endDate, buyerSupplier, project);
            }

        } else if (type.equalsIgnoreCase(Constants.INDIRECT)) {
            data = entriesService.getInDirectEntries(startIndex, fetchSize, orderBy, sortby, startDate, endDate, buyerSupplier);
            totalSize = entriesService.getInDirectEntriesCount(startDate, endDate, buyerSupplier);

        } else if (type.equalsIgnoreCase(Constants.CASH_TRANSACTIONS)) {
            // cash transactions
            data = ss.getCashTransactions(startIndex, fetchSize, orderBy, sortby, startDate, endDate, buyerSupplier);
            totalSize = ss.getCashTransactionsCount(startDate, endDate, buyerSupplier);

        } else if (type.equalsIgnoreCase(Constants.CUSTOMERS_BUYERS)) {
            // customersBuyers
            try {
                data = userService.getCustomersAndBuyers();
                totalSize = ((List<CustomersBuyers>) data).size();
            } catch (Exception e) {

            }
        } else if (type.equalsIgnoreCase("customersBuyersDetail")) {
            try {
                data = accountsService.getAllTransactions(orderBy, sortby, startDate, endDate, buyerSupplier);
                totalSize = ((List<AccountPayableReceivable>) data).size();
            } catch (Exception e) {

            }
        }

        resp.setData(data != null ? data : "");
        resp.setRecordsFiltered(totalSize);
        resp.setRecordsTotal(totalSize);
        resp.setDraw(draw);

        return resp;
    }

    @RequestMapping(value = "/updateEntry", method = RequestMethod.POST)
    public @ResponseBody
    String updateEntry(HttpServletRequest request, HttpSession httpSession) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            String eid = request.getParameter("eid");
            String ebuyer = request.getParameter("ebuyer");
            String esupplier = request.getParameter("esupplier");
            String eproject = request.getParameter("eproject");
            String equantity = request.getParameter("equantity");
            String erate = request.getParameter("erate");
            String eamount = request.getParameter("eamount");
            String eadvance = request.getParameter("eadvance");

            EntriesDirect entry = entriesService.getDirectEntry(Integer.parseInt(eid));
            if (entry != null) {
                entry.setBuyer(ebuyer);
                entry.setSupplier(esupplier);
                entry.setProject(eproject);
                entry.setQuantity(BigDecimal.valueOf(Long.parseLong(equantity)));
                entry.setRate(BigDecimal.valueOf(Long.valueOf(erate)));
                entry.setTotalPrice(BigDecimal.valueOf(Long.parseLong(eamount)));
                entry.setAdvance(BigDecimal.valueOf(Long.valueOf(eadvance)));
            } else {
                return ("01:Invalid Entry ID");
            }

            boolean entryUpdated = entriesService.updateDirectEntry(entry);
            if (!entryUpdated) {
                return ("01:Failed To Update Entry. Make sure all field are filled in.");
            } else {
                logActivity(request, auth.getName(), "UPDATED ENTRY", entry.toString());
                return "00:Entry Updated Successfully";
            }
        } catch (Exception e) {
            log.error("Exception while Updating Entry:", e);
            return ("01:Invalid Values");
        }
    }

    @RequestMapping(value = "/deleteEntry", method = RequestMethod.POST)
    public @ResponseBody
    String deleteEntry(HttpServletRequest request, HttpSession httpSession) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            String eid = request.getParameter("eid");
            EntriesDirect entry = entriesService.getDirectEntry(Integer.parseInt(eid));
            if (entry != null) {
                entry.setIsActive(false);
            } else {
                return ("01:Invalid Entry ID");
            }
            boolean entryUpdated = entriesService.updateDirectEntry(entry);
            if (!entryUpdated) {
                return ("01:Failed To Delete Entry.");
            } else {
                logActivity(request, auth.getName(), "DELETED ENTRY", entry.toString());
                return "00:Entry Deleted Successfully";
            }
        } catch (Exception e) {
            log.error("Exception while deleting Entry:", e);
            return ("01:Invalid Values");
        }
    }

    @RequestMapping(value = "/updateIndirectEntry", method = RequestMethod.POST)
    public @ResponseBody
    String updateIndirectEntry(HttpServletRequest request, HttpSession httpSession) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            String eid = request.getParameter("eid");
            String ename = request.getParameter("ename");
            String edesc = request.getParameter("edesc");
            String eamount = request.getParameter("eamount");
            String eadvance = request.getParameter("eadvance");

            EntriesIndirect entry = entriesService.getInDirectEntry(Integer.parseInt(eid));
            if (entry != null) {
                entry.setName(ename);
                entry.setDescription(edesc);
                entry.setAmount(BigDecimal.valueOf(Long.valueOf(eamount)));
                entry.setAdvance(BigDecimal.valueOf(Long.valueOf(eadvance)));
            } else {
                return ("01:Invalid Entry ID");
            }

            boolean entryUpdated = entriesService.updateInDirectEntry(entry);
            if (!entryUpdated) {
                return ("01:Failed To Update Entry. Make sure all field are filled in.");
            } else {
                logActivity(request, auth.getName(), "UPDATED ENTRY", entry.toString());
                return "00:Entry Updated Successfully";
            }
        } catch (Exception e) {
            log.error("Exception while Updating Entry:", e);
            return ("01:Invalid Values");
        }
    }

    @RequestMapping(value = "/deleteIndirectEntry", method = RequestMethod.POST)
    public @ResponseBody
    String deleteIndirectEntry(HttpServletRequest request, HttpSession httpSession) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            String eid = request.getParameter("eid");
            EntriesIndirect entry = entriesService.getInDirectEntry(Integer.parseInt(eid));
            if (entry != null) {
                entry.setIsActive(false);
            } else {
                return ("01:Invalid Entry ID");
            }
            boolean entryUpdated = entriesService.updateInDirectEntry(entry);
            if (!entryUpdated) {
                return ("01:Failed To Delete Entry.");
            } else {
                logActivity(request, auth.getName(), "DELETED ENTRY", entry.toString());
                return "00:Entry Deleted Successfully";
            }
        } catch (Exception e) {
            log.error("Exception while deleting Entry:", e);
            return ("01:Invalid Values");
        }
    }

    @RequestMapping(value = "/entries/stockTrace", method = RequestMethod.GET)
    public @ResponseBody
    StockTrace getStockTrace(
            HttpServletRequest request,
            @RequestParam(value = "entryTypeId", required = false, defaultValue = "0") int id,
            @RequestParam(value = "subType", required = false, defaultValue = "") String subType) {

        return ss.getStockTrace(id, subType);
    }

    @RequestMapping(value = "/customersAndBuyers", method = RequestMethod.GET)
    public @ResponseBody
    List<CustomersBuyers> getCustomersAndBuyers(
            HttpServletRequest request) {
        List<CustomersBuyers> respList = userService.getCustomersAndBuyers();
        if (respList == null || respList.isEmpty()) {
            respList = new ArrayList<>();
        }
        return respList;
    }

    @RequestMapping(value = "/projects", method = RequestMethod.GET)
    public @ResponseBody
    List<Projects> getProjects(
            HttpServletRequest request) {
        List<Projects> respList = userService.getProjects();
        if (respList == null || respList.isEmpty()) {
            respList = new ArrayList<>();
        }
        return respList;
    }

    @RequestMapping(value = "/updateMasterAccountCash", method = RequestMethod.GET)
    public @ResponseBody
    String updateMasterAccountCash(
            HttpServletRequest request,
            @RequestParam String value) {
        MasterAccount ma = ss.getMasterAccount();
        ma.setTotalCash(BigDecimal.valueOf(Integer.parseInt(value)));
        ss.updateMasterAccount(ma);
        return "Success";
    }

    @RequestMapping(value = "/logCashTransaction", method = RequestMethod.POST)
    public @ResponseBody
    String logCashTransaction(HttpServletRequest request, HttpSession httpSession) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            String ttype = request.getParameter("ttype");
            String tamount = request.getParameter("tamount");
            String tdesc = request.getParameter("tdesc");
            String tpayer = request.getParameter("tpayer");

            String payfrom;
            if (ttype.equals("+")) {
                payfrom = request.getParameter("payToHo");
            } else if (ttype.equals("-")) {
                payfrom = request.getParameter("payFromHo");
            } else {
                payfrom = null; //cash in hand in or out
            }

            if (tpayer != null && tpayer.equalsIgnoreCase("other")) {
                tpayer = request.getParameter("tbuysupInput");
                asyncUtil.addToCustomersAndBuyersList(tpayer);
            }

            MasterAccountHistory mah = new MasterAccountHistory();
            mah.setType(ttype);
            mah.setAmount(BigDecimal.valueOf(Long.valueOf(tamount)));
            mah.setDescription(tdesc);
            mah.setPayee(tpayer);

            String transactionLogged = ss.logCashTransaction(mah, payfrom);
            if (transactionLogged == null) {
                return ("01:Failed To Log Transaction. Make sure all fields are Properly Filled.");
            } else {
//                logActivity(request, auth.getName(), "CASH TRANSACTION", mah.toString());
                asyncUtil.logCashTran(mah, transactionLogged);
                return "00:Transaction Logged Successfully";
            }
        } catch (Exception e) {
            log.error("Exception while logging Transaction:", e);
            return ("01:Invalid Values");
        }
    }

    @RequestMapping(value = "/logAsphaltSale", method = RequestMethod.POST)
    public @ResponseBody
    String logAsphaltSale(HttpServletRequest request, HttpSession httpSession) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object entryLogged = entriesService.logAsphaltSale(request);
        if (entryLogged instanceof AsphaltSales) {
            logActivity(request, auth.getName(), "Asphalt Sale Logged", entryLogged.toString());
            return "00:Sale Logged Successfully";
        } else if (entryLogged instanceof String) {
            return ((String) entryLogged);
        }
        return ("01:Invalid Values");
    }

    @RequestMapping(value = "/previousAssValues", method = RequestMethod.GET)
    public @ResponseBody
    Object config(
            HttpServletRequest request,
            @RequestParam String buyerSupplier,
            @RequestParam String project) {

        AsphaltSales ass = ss.getAsphaltSale(buyerSupplier, project);

        if (ass != null) {
            List<AsphaltSaleConsumption> cons = ss.getAsphaltSaleConsumptions(ass);
            AsphaltSaleConsumption assVals = new AsphaltSaleConsumption(0, ass.getVehicle(), BigDecimal.valueOf(ass.getBiltee()), ass.getExPlantRate(), ass.getExPlantCost());
            if (cons == null) {
                cons = new ArrayList<>();
            }
            cons.add(assVals);
            return cons;
        }
        return new ArrayList<>();
    }

    @RequestMapping(value = "/logMachinery", method = RequestMethod.POST)
    public @ResponseBody
    String logMachinery(HttpServletRequest request, HttpSession httpSession) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String entryLogged = entriesService.logMachinery(request);
        if (entryLogged.equals("00:Sale Logged Successfully")) {
            logActivity(request, auth.getName(), "Asphalt Sale Logged", entryLogged.toString());
            return entryLogged;
        } else {
            return entryLogged;
        }
    }

    private void logActivity(HttpServletRequest request, String username, String action, String desc) {
        String remoteAddr = request.getHeader("x-forwarded-for") != null ? request.getHeader("x-forwarded-for") : request.getRemoteAddr();
        String ua = request.getHeader("user-agent");
        asyncUtil.logActivity(request, username, remoteAddr, ua, action, desc);
    }

}
