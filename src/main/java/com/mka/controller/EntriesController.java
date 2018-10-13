/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.controller;

import com.mka.model.EntriesDirect;
import com.mka.model.EntriesIndirect;
import com.mka.model.EntryItems;
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
            @RequestParam(value = "type", required = false, defaultValue = Constants.DIRECT) String type) {

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

    @RequestMapping(value = "/logDirectEntry", method = RequestMethod.POST)
    public @ResponseBody
    String logDirectEntry(HttpServletRequest request, HttpSession httpSession) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            String dItemType = request.getParameter("dItemType");
            String subItemType = request.getParameter("dItemSubType");
            String entryType = request.getParameter("dEntryType");
            String customerBuyer = request.getParameter("dcusbuy");
            String supplier = request.getParameter("dsupplier");
            String project = request.getParameter("dproject");
            String quantity = request.getParameter("dquantity");
            String rate = request.getParameter("drate");
            String amount = request.getParameter("damount");
            String dadvance = request.getParameter("dadvance");
            String dateOfEntry = request.getParameter("doe");

            EntryItems item = entriesService.getEntryItemById(Integer.parseInt(dItemType));
            if (item != null) {
                EntriesDirect entry = new EntriesDirect();
                entry.setItem(item);
                entry.setItemType(subItemType);
                entry.setSubEntryType(entryType);
                entry.setSupplier(supplier);
                entry.setProject(project);
                entry.setBuyer(customerBuyer);
                entry.setQuantity(Integer.parseInt(quantity));
                entry.setRate(Integer.parseInt(rate));
                entry.setTotalPrice(Integer.parseInt(amount));
                entry.setAdvance(Integer.parseInt(dadvance));
                entry.setEntryDate(Constants.DATE_FORMAT.parse(dateOfEntry));
                entry.setIsActive(true);

                boolean entryLogged = entriesService.logDirectEntry(entry);
                if (!entryLogged) {
                    return ("01:Failed To Log Entry. Make sure all field are filled in.");
                } else {
                    logActivity(request, auth.getName(), "ADDED ENTRY", entry.toString());
                    // async update stock trace
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
            String iItemType = request.getParameter("iItemType");
            String iItemSubType = request.getParameter("iItemSubType");
            String iname = request.getParameter("iname");
            String idesc = request.getParameter("idesc");
            String icost = request.getParameter("icost");
            String iAdvance = request.getParameter("iadvance");
            String dateOfEntry = request.getParameter("idoe");

            EntryItems item = entriesService.getEntryItemById(Integer.parseInt(iItemType));
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

                boolean entryLogged = entriesService.logInDirectEntry(entry);
                if (!entryLogged) {
                    return ("01:Failed To Log Entry. Make sure all field are filled in.");
                } else {
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
            data = entriesService.getDirectEntries(startIndex, fetchSize, orderBy, sortby, startDate, endDate);
            totalSize = entriesService.getDirectEntriesCount(startDate, endDate);
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
            @RequestParam(value = "entryTypeId", required = false, defaultValue = "0") int id) {

        return ss.getStockTrace(id);
    }

    private void logActivity(HttpServletRequest request, String username, String action, String desc) {
        String remoteAddr = request.getHeader("x-forwarded-for") != null ? request.getHeader("x-forwarded-for") : request.getRemoteAddr();
        String ua = request.getHeader("user-agent");
        asyncUtil.logActivity(request, username, remoteAddr, ua, action, desc);
    }

}
