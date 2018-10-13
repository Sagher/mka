/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.utils;

import com.mka.model.EntriesDirect;
import com.mka.model.StockTrace;
import com.mka.model.User;
import com.mka.model.UserActivity;
import com.mka.service.StatsService;
import com.mka.service.UserActivityService;
import com.mka.service.UserService;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 *
 * @author Sagher Mehmood
 */
@Component
public class AsyncUtil {

    private static final Logger log = Logger.getLogger(AsyncUtil.class);

    @Autowired
    UserActivityService activityService;

    @Autowired
    UserService userService;

    @Autowired
    StatsService ss;

    @Async
    public void logActivity(HttpServletRequest request, String userName, String remoteAddr, String ua, String actionType, String actionDescription) {
        User user = userService.getUser(userName);
        if (user != null) {
            UserActivity activity = new UserActivity(actionType, actionDescription, remoteAddr, ua, new Date(), user);
            boolean activtyLogged = activityService.addUserActivity(activity);

            if (!activtyLogged) {
                log.warn("FAILED TO LOG USER ACTIVITY: " + activity);
            }
        } else {
            log.warn("Invalid User in logActivty: " + userName);
        }
    }

    @Async
    public void updateUser(User u) {
        boolean resp = userService.updateUser(u);
        if (!resp) {
            log.warn("Failed to update user Asynchronously" + u);
        }
    }

    @Async
    public void updateStockTrace(EntriesDirect entry) {
        try {
            StockTrace st = ss.getStockTrace(entry.getItem().getId());
            if (entry.getSubEntryType().equals(Constants.SALE)) {
                st.setSalesUnit(st.getSalesUnit() + entry.getQuantity());
                st.setStockUnits(st.getStockUnits() - entry.getQuantity());
                st.setSalesAmount(entry.getTotalPrice() + st.getSalesAmount());
                st.setStockAmount(st.getStockAmount() - entry.getTotalPrice());

            } else if (entry.getSubEntryType().equals(Constants.PURCHASE)) {
                st.setPurchaseUnit(st.getPurchaseUnit() + entry.getQuantity());
                st.setStockUnits(st.getStockUnits() + entry.getQuantity());
                st.setStockAmount(st.getStockAmount() + entry.getTotalPrice());
                st.setPurchaseAmount(st.getPurchaseAmount() + entry.getTotalPrice());

            } else if (entry.getSubEntryType().equals(Constants.PRODUCE)) {
                st.setStockUnits(st.getStockUnits() + entry.getQuantity());
            }

            st.setAverageUnitPrice(ss.getAveragePricePerUnit(entry.getItem().getId()));
            ss.updateStockTrace(st);
        } catch (Exception e) {
            log.error("Exception in updateStockTrace: ", e);
        }
    }

}
