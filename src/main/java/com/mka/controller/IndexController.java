package com.mka.controller;

import com.mka.model.StockTrace;
import com.mka.model.User;
import com.mka.service.StatsService;
import com.mka.service.UserService;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Sagher Mehmood
 */
@Controller
public class IndexController {

    //logger for logging
    public static Logger log = Logger.getLogger(IndexController.class);

    @Autowired
    UserService userService;

    @Autowired
    StatsService ss;

    private int totalPurchase = 0, purchaseUnits = 0, totalSale = 0, saleUnits = 0, totalStock = 0, stockUnits = 0;

    /* 
     * 
     * Model And View Controller for the /index page
     */
    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public ModelAndView indexPage(HttpServletRequest request, HttpSession session) {
        ModelAndView model = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.isAuthenticated()
                && auth.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
            User u = userService.getUser(auth.getName());
            totalPurchase = 0;
            totalSale = 0;
            purchaseUnits = 0;
            saleUnits = 0;
            totalStock = 0;
            List<StockTrace> stockTrace = ss.getStats();
            try {
                stockTrace.parallelStream().filter((StockTrace e) -> {
                    totalSale += e.getSalesAmount();
                    totalPurchase += e.getPurchaseAmount();
                    totalStock += e.getStockAmount();

                    saleUnits += e.getSalesUnit();
                    purchaseUnits += e.getPurchaseUnit();
                    stockUnits += e.getStockUnits();

//                log.info(e.toString());
                    return true;
                }).collect(Collectors.toList());
            } catch (Exception e) {

            }
            model.addObject("user", u);
            model.addObject("users", userService.getAllUsers());
            model.addObject("totalSaleUnits", saleUnits + " Units");
            model.addObject("totalPurchaseUnits", purchaseUnits + " Units");
            model.addObject("totalStockUnits", stockUnits + " Units");
            model.addObject("totalSale", totalSale + " PKR");
            model.addObject("totalPurchase", totalPurchase + " PKR");
            model.addObject("totalStock", totalStock + " PKR");
            model.addObject("totalUsers", userService.getUsersCount());
            model.addObject("masterAccount", ss.getMasterAccount());
            model.addObject("stockTrace", stockTrace);

            model.setViewName("index");
        } else {
            log.warn("Invalid username and password!");
            model.setViewName("login");
        }

        return model;

    }

}
