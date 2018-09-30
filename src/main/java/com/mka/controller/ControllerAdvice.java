/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.controller;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 *
 * @author Sagher Mehmood
 */
@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    private static final Logger log = Logger.getLogger(ControllerAdvice.class);

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(HttpServletRequest request, Exception ex) {
        log.error("Exception occurred in: '" + request.getRequestURI() + "' ", ex);
        ModelAndView model = new ModelAndView();
        model.addObject("errorCode", "500");
        model.addObject("errorMessage", "Something Went Wrong");
        model.addObject("errorDescription", "An error occured: " + ex.getMessage());

        model.setViewName("error");
        return model;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView noRoute(HttpServletRequest request, Exception ex) {
        log.error("Exception occurred in: '" + request.getRequestURI() + "' ", ex);
        ModelAndView model = new ModelAndView();
        model.addObject("errorCode", "404");
        model.addObject("errorMessage", "Page Not Found");
        model.addObject("errorDescription", "The Requested url does not exist.");

        model.setViewName("error");
        return model;
    }
}
