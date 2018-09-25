/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.controller;

import static com.mka.controller.UserController.log;
import com.mka.model.User;
import com.mka.service.UserActivityService;
import com.mka.service.UserService;
import com.mka.utils.AsyncUtil;
import com.mka.utils.Constants;
import com.mka.utils.ImageUtil;
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
    ImageUtil imageUtil;

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
                model.addObject("allRoles", Constants.ALL_ROLES);
                model.addObject("users", userService.getAllUsers());
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
}
