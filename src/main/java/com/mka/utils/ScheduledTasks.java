/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.utils;

import com.mka.service.EmployeesService;
import com.mka.service.StatsService;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author Sagher Mehmood
 */
@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    @Autowired
    StatsService statsService;

    @Autowired
    EmployeesService employeesService;

    @Scheduled(cron = "0 10 22 28-31 * ?")
    public void doStuffOnLastDayOfMonth() {
        final Calendar c = Calendar.getInstance();
        if (c.get(Calendar.DATE) == c.getActualMaximum(Calendar.DATE)) {
            // copy current months stock as opening stock for next month
            if (!statsService.insertStockTraceForNewMonth()) {
                log.warn("FAILED TO INSERT CURRENT MONTH STOCK AS OPENING STOCK FOR NEXT MONTH");
            }

            // set employees current month salary paid flag =0
            employeesService.setCurrentMonthSalaryFlagToFalse();
        }
    }

//    @Scheduled(fixedDelay = 50000)
//    public void dostuff() {
//        log.info("scheduled task executed");
//        final Calendar c = Calendar.getInstance();
//        // copy current months stock as opening stock for next month
//        if (!statsService.insertStockTraceForNewMonth()) {
//            log.warn("FAILED TO INSERT CURRENT MONTH STOCK AS OPENING STOCK FOR NEXT MONTH");
//        }
//    }
}
