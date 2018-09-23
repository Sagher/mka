package com.mka.controller;

import com.mka.model.User;
import com.mka.service.UserService;
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
            model.addObject("user", u);
            model.setViewName("index");
        } else {
            log.warn("Invalid username and password!");
            model.setViewName("login");
        }

        return model;

    }

}
