/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.controller;

import com.mka.model.CustomersBuyers;
import com.mka.model.EntriesDirect;
import com.mka.model.EntriesDirectDetails;
import com.mka.model.EntriesIndirect;
import com.mka.model.EntryItems;
import com.mka.model.MasterAccount;
import com.mka.model.MasterAccountHistory;
import com.mka.model.Projects;
import com.mka.model.StockTrace;
import com.mka.model.User;
import com.mka.model.response.DataTableResp;
import com.mka.service.EntriesService;
import com.mka.service.StatsService;
import com.mka.service.UserActivityService;
import com.mka.service.UserService;
import com.mka.utils.AsyncUtil;
import com.mka.utils.Constants;
import com.mka.utils.ImageUtil;
import java.util.ArrayList;
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
                    }
                    model.setViewName("subPages/directEntries");
                } else {
                    model.setViewName("subPages/indirectEntries");
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
        try {
            EntriesDirectDetails entryDetail = null;

            String dItemType = request.getParameter("dItemType");
            String subItemType = request.getParameter("dItemSubType");
            String entryType = request.getParameter("dEntryType");
            String customerBuyerSupplier = request.getParameter("dbuysup");
            if (customerBuyerSupplier.equalsIgnoreCase("other")) {
                customerBuyerSupplier = request.getParameter("dbuysupInput");
                asyncUtil.addToCustomersAndBuyersList(customerBuyerSupplier);
            }
            String dProj = request.getParameter("dproj");
            if (dProj.equalsIgnoreCase("other")) {
                dProj = request.getParameter("dproject");
                asyncUtil.addToProjectsList(dProj);
            }

            String description = request.getParameter("ddescription") != null
                    ? request.getParameter("ddescription") : null;
            String quantity = request.getParameter("dquantity");
            String rate = request.getParameter("drate");
            String amount = request.getParameter("damount");
            String dadvance = request.getParameter("dadvance");
            String dateOfEntry = request.getParameter("doe");

            EntryItems item = entriesService.getEntryItemById(Integer.parseInt(dItemType));
            if (item != null) {
                EntriesDirect entry = new EntriesDirect();
                entry.setItem(item);
                entry.setSubEntryType(entryType);
                if (entryType.equalsIgnoreCase(Constants.SALE)) {
                    entry.setBuyer(customerBuyerSupplier);
                    entry.setSupplier(null);
                } else {
                    entry.setSupplier(customerBuyerSupplier);
                    entry.setBuyer(null);
                }
                entry.setProject(dProj);
                entry.setDescription(description);

                // OPTIONAL unloadedCrush & unloadingCost in case of crush
                String totalUnloadedCrush = request.getParameter("unloadedCrush");
                String carriageCost = request.getParameter("unloadingCost");
                String unloadingParty = request.getParameter("unloadingParty");

                if (item.getItemName().equalsIgnoreCase("CRUSH")) {

                    if (!totalUnloadedCrush.isEmpty() || totalUnloadedCrush.equals("0")
                            || !carriageCost.isEmpty() || carriageCost.equals("0")
                            || !unloadingParty.isEmpty() || unloadingParty.equals("0")) {
                        int cCost = Integer.parseInt(carriageCost);
                        int tCost = Integer.parseInt(amount);
                        int totalUnloaded = Integer.parseInt(totalUnloadedCrush);

                        int newRate = (cCost + tCost) / totalUnloaded;
                        int newQuantity = totalUnloaded;
                        entryDetail = new EntriesDirectDetails();
                        entryDetail.setSubType(subItemType);
                        entryDetail.setTotalUnloaded(totalUnloaded);
                        entryDetail.setUnloadingCost(cCost);
                        entryDetail.setUnloadingParty(unloadingParty);

                        entry.setQuantity(newQuantity);
                        entry.setRate(newRate);
                    } else {
                        entryDetail = new EntriesDirectDetails();
                        entryDetail.setSubType(subItemType);
                        entryDetail.setTotalUnloaded(Integer.parseInt(quantity));
                        entryDetail.setUnloadingCost(0);
                        entryDetail.setUnloadingParty(null);

                        entry.setQuantity(Integer.parseInt(quantity));
                        entry.setRate(Integer.parseInt(rate));
                    }
                } else {

                    entry.setQuantity(Integer.parseInt(quantity));
                    entry.setRate(Integer.parseInt(rate));
                }
                entry.setTotalPrice(Integer.parseInt(amount));
                entry.setAdvance(Integer.parseInt(dadvance));
                entry.setEntryDate(Constants.DATE_FORMAT.parse(dateOfEntry));
                entry.setIsActive(true);

                if (ss.getStockTrace(item.getId(), subItemType).getStockUnits() < entry.getQuantity()
                        && entryType.equalsIgnoreCase(Constants.SALE)) {
                    return ("01:Not Enough stock to make this sale");
                } else if (ss.getMasterAccount().getCashInHand() < entry.getAdvance()
                        && entryType.equalsIgnoreCase(Constants.PURCHASE)) {
                    return ("01:Not Enough cash in hand to make this purchase");
                }

                boolean entryLogged = entriesService.logDirectEntry(entry);
                if (!entryLogged) {
                    return ("01:Failed To Log Entry. Make sure all field are filled in.");
                } else {
                    logActivity(request, auth.getName(), "ADDED ENTRY", entry.toString());
                    // async update stock trace
                    if (entryDetail != null) {
                        entryDetail.setEntryId(entry);
                        asyncUtil.addEntryDetail(entryDetail);
                    }
                    entry.setEntriesDirectDetails(entryDetail);
                    asyncUtil.updateStockTrace(entry);

                    return "00:Entry Logged Successfully";
                }
            } else {
                return ("01:Invalid Item Type");
            }
        } catch (Exception e) {
            log.error("Exception while logging Entry:", e);
            return ("01:Invalid Values");
        }
    }

    @RequestMapping(value = "/logInDirectEntry", method = RequestMethod.POST)
    public @ResponseBody
    String logInDirectEntry(HttpServletRequest request, HttpSession httpSession) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            EntryItems item;

            String iItemType = request.getParameter("iItemType");
            if (iItemType.equalsIgnoreCase("other")) {
                item = entriesService.createNewEntryItem(request.getParameter("iItemTypeInput"));
            } else {
                item = entriesService.getEntryItemById(Integer.parseInt(iItemType));
            }
            String iItemSubType = request.getParameter("iItemSubType");
            String iname = request.getParameter("iname");
            String idesc = request.getParameter("idesc");
            String icost = request.getParameter("icost");
            String iAdvance = request.getParameter("iadvance");
            String dateOfEntry = request.getParameter("idoe");

            if (item != null) {
                EntriesIndirect entry = new EntriesIndirect();
                entry.setItem(item);
                if (item.getItemType() != null) {
                    entry.setItemType(iItemSubType);
                } else {
                    entry.setItemType(null);
                }
                entry.setName(iname);
                entry.setDescription(idesc);
                entry.setAmount(Integer.parseInt(icost));
                entry.setAdvance(Integer.parseInt(iAdvance));
                entry.setEntryDate(Constants.DATE_FORMAT.parse(dateOfEntry));
                entry.setIsActive(true);

                MasterAccount ma = ss.getMasterAccount();
                if (ma.getCashInHand() < entry.getAdvance()) {
                    return ("01:Not Enough Cash In Hand to Log this Expense");
                }

                boolean entryLogged = entriesService.logInDirectEntry(entry);
                if (!entryLogged) {
                    return ("01:Failed To Log Entry. Make sure all field are filled in.");
                } else {
                    ma.setCashInHand(ma.getCashInHand() - entry.getAmount());
                    ss.updateMasterAccount(ma);
                    logActivity(request, auth.getName(), "ADDED ENTRY", entry.toString());
                    return "00:Entry Logged Successfully";
                }
            } else {
                return ("01:Invalid Item Type");
            }
        } catch (Exception e) {
            log.error("Exception while logging Entry:", e);
            return ("01:Invalid Values");
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

        int order0Col = Integer.parseInt(request.getParameter("order[0][column]"));
        String orderBy = request.getParameter("order[0][dir]");
        String sortby = request.getParameter("columns[" + order0Col + "][data]");

        Object data;
        int totalSize;
        if (type.equalsIgnoreCase(Constants.DIRECT)) {
            if (itemTypeId > 0) {
                EntryItems entryItem = entriesService.getEntryItemById(itemTypeId);

                data = entriesService.getDirectEntries(entryItem, subEntryType, startIndex, fetchSize, orderBy, sortby, startDate, endDate, buyerSupplier, project);
                totalSize = entriesService.getDirectEntriesCount(entryItem, subEntryType, startDate, endDate, buyerSupplier, project);
            } else {
                data = entriesService.getDirectEntries(null, subEntryType, startIndex, fetchSize, orderBy, sortby, startDate, endDate, buyerSupplier, project);
                totalSize = entriesService.getDirectEntriesCount(null, subEntryType, startDate, endDate, buyerSupplier, project);
            }
        } else {
            data = entriesService.getInDirectEntries(startIndex, fetchSize, orderBy, sortby, startDate, endDate);
            totalSize = entriesService.getInDirectEntriesCount(startDate, endDate);
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
                entry.setQuantity(Integer.parseInt(equantity));
                entry.setRate(Integer.parseInt(erate));
                entry.setTotalPrice(Integer.parseInt(eamount));
                entry.setAdvance(Integer.parseInt(eadvance));
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
                entry.setAmount(Integer.parseInt(eamount));
                entry.setAdvance(Integer.parseInt(eadvance));
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

    @RequestMapping(value = "/logCashTransaction", method = RequestMethod.POST)
    public @ResponseBody
    String logCashTransaction(HttpServletRequest request, HttpSession httpSession) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            String ttype = request.getParameter("ttype");
            String tamount = request.getParameter("tamount");
            String tdesc = request.getParameter("tdesc");

            MasterAccountHistory mah = new MasterAccountHistory();
            mah.setType(ttype);
            mah.setAmount(Integer.parseInt(tamount));
            mah.setDescription(tdesc);

            boolean transactionLogged = ss.logCashTransaction(mah);
            if (!transactionLogged) {
                return ("01:Failed To Log Transaction. Make sure all field are filled in.");
            } else {
//                logActivity(request, auth.getName(), "CASH TRANSACTION", mah.toString());
                return "00:Transaction Logged Successfully";
            }
        } catch (Exception e) {
            log.error("Exception while logging Transaction:", e);
            return ("01:Invalid Values");
        }
    }

    private void logActivity(HttpServletRequest request, String username, String action, String desc) {
        String remoteAddr = request.getHeader("x-forwarded-for") != null ? request.getHeader("x-forwarded-for") : request.getRemoteAddr();
        String ua = request.getHeader("user-agent");
        asyncUtil.logActivity(request, username, remoteAddr, ua, action, desc);
    }

}
