/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.utils;

import com.mka.model.User;
import com.mka.model.UserActivity;
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

}
