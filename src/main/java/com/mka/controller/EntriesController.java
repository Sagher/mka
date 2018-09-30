/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.controller;

import static com.mka.controller.UserController.log;
import com.mka.model.Employees;
import com.mka.model.Entries;
import com.mka.model.EntryItems;
import com.mka.model.User;
import com.mka.model.response.DataTableResp;
import com.mka.service.EntriesService;
import com.mka.service.UserActivityService;
import com.mka.service.UserService;
import com.mka.utils.AsyncUtil;
import com.mka.utils.Constants;
import com.mka.utils.ImageUtil;
import java.util.Date;
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
    public ModelAndView entries(HttpServletRequest request, HttpSession session) {

        ModelAndView model = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.isAuthenticated()
                && auth.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
            User u = userService.getUser(auth.getName());
            if (u != null) {
                model.addObject("user", u);
                model.addObject("allRoles", Constants.ALL_ROLES);
                model.addObject("users", userService.getAllUsers());
                model.setViewName("subPages/entries");
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

    @RequestMapping(value = "/logEntry", method = RequestMethod.POST)
    public @ResponseBody
    String logEntry(HttpServletRequest request, HttpSession httpSession) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            String item = request.getParameter("itemType");
            String subItemType = request.getParameter("subItemType");
            String entryType = request.getParameter("entryType");
            String supplier = request.getParameter("supplier");
            String quantity = request.getParameter("quantity");
            String rate = request.getParameter("rate");
            String amount = request.getParameter("amount");
            String dateOfEntry = request.getParameter("doe");

            Entries entry = new Entries();
            entry.setItem(item);
            entry.setItemType(subItemType);
            entry.setEntryType(entryType);
            entry.setSupplier(supplier);
            entry.setQuantity(Integer.parseInt(quantity));
            entry.setRate(Integer.parseInt(rate));
            entry.setAmount(Integer.parseInt(amount));
            entry.setEntryDate(Constants.DATE_FORMAT.parse(dateOfEntry));
            entry.setIsActive(true);

            boolean entryLogged = entriesService.logEntry(entry);
            if (!entryLogged) {
                return ("01:Failed To Log Entry. Make sure all field are filled in.");
            } else {
                logActivity(request, auth.getName(), "ADDED ENTRY", entry.toString());
                return "00:Entry Logged Successfully";
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

        List<Entries> data = entriesService.getEntries(startIndex, fetchSize, orderBy, sortby, startDate, endDate);
        int totalSize = entriesService.getEntriesCount(startDate, endDate);

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
            String edoe = request.getParameter("edoe");
            String esupplier = request.getParameter("esupplier");
            String equantity = request.getParameter("equantity");
            String erate = request.getParameter("erate");
            String eamount = request.getParameter("eamount");

            Entries entry = entriesService.getEntry(Integer.parseInt(eid));
            if (entry != null) {
                entry.setSupplier(esupplier);
                entry.setQuantity(Integer.parseInt(equantity));
                entry.setRate(Integer.parseInt(erate));
                entry.setAmount(Integer.parseInt(eamount));
                entry.setEntryDate(Constants.DATE_FORMAT.parse(edoe));
            } else {
                return ("01:Invalid Entry ID");
            }

            boolean entryUpdated = entriesService.updateEntry(entry);
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
            Entries entry = entriesService.getEntry(Integer.parseInt(eid));
            if (entry != null) {
                entry.setIsActive(false);
            } else {
                return ("01:Invalid Entry ID");
            }
            boolean entryUpdated = entriesService.updateEntry(entry);
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

    private void logActivity(HttpServletRequest request, String username, String action, String desc) {
        String remoteAddr = request.getHeader("x-forwarded-for") != null ? request.getHeader("x-forwarded-for") : request.getRemoteAddr();
        String ua = request.getHeader("user-agent");
        asyncUtil.logActivity(request, username, remoteAddr, ua, action, desc);
    }

}
