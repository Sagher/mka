/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.configuration;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Sagher Mehmood
 */
public class Filters implements HandlerInterceptor {

    private static final Logger log = Logger.getLogger(Filters.class);

    private final List<String> ips = new ArrayList<>();

    @PostConstruct
    public void init() {
        log.info("whitelisted ip's loaded");
    }

    // This method is called before the controller
    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {
        String userIp = request.getHeader("X-Real-IP");
        userIp = (userIp == null ? request.getRemoteAddr() : userIp);

        if (request.getMethod().equalsIgnoreCase("POST")) {
            Enumeration<String> params = request.getParameterNames();
            while (params.hasMoreElements()) {
                String paramName = params.nextElement();
                log.info("Parameter: " + paramName + ", Value: " + request.getParameter(paramName));
            }
        }
        if (ips.contains(userIp)) {
            return true;
        } else {
//            response.setStatus(HttpStatus.FORBIDDEN.value());
//            response.sendRedirect("/admin/403");
//            return false;
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler,
            ModelAndView modelAndView) {
        try {
//            modelAndView.addObject("props", Filters.props);
        } catch (Exception e) {
            log.error("Exception in postHandle()", e);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
            HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

    }

}
