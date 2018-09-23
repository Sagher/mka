/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.controller;

import com.mka.model.User;
import com.mka.model.UserActivity;
import com.mka.model.response.DataTableResp;
import com.mka.service.UserActivityService;
import com.mka.service.UserService;
import com.mka.utils.AsyncUtil;
import com.mka.utils.Constants;
import com.mka.utils.ImageUtil;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Sagher Mehmood
 */
@Controller
public class UserController {

    //logger for logging
    public static Logger log = Logger.getLogger(UserController.class);

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
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelAndView usersPage(HttpServletRequest request, HttpSession session) {

        ModelAndView model = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.isAuthenticated()
                && auth.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
            User u = userService.getUser(auth.getName());
            if (u != null
                    && (u.getRole().equals(Constants.ROLE_ADMIN))) {
                model.addObject("user", u);
                model.addObject("allRoles", Constants.ALL_ROLES);
                model.addObject("users", userService.getAllUsers());
                model.setViewName("subPages/users");
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

    @RequestMapping(value = "/user/{userId}/activity", method = RequestMethod.GET)
    public ModelAndView userActivityPage(HttpServletRequest request, HttpSession session,
            @PathVariable int userId) {

        ModelAndView model = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.isAuthenticated()
                && auth.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
            model.addObject("username", auth.getName());
            User u = userService.getUser(userId);
            if (u != null
                    && (u.getRole().equals(Constants.ROLE_ADMIN))) {
                model.addObject("user", u);
                model.setViewName("subPages/usersActivity");
            } else {
                model.addObject("errorCode", "404");
                model.addObject("errorMessage", "User Not Found");
                model.addObject("errorDescription", "The Requested user does not exist.");
                model.setViewName("error");
            }
        } else {
            model.addObject("msg", "Invalid username and password!");
            log.info("Invalid username and password!");
            model.setViewName("login");
        }
        return model;
    }

    @RequestMapping(value = "/user/profile", method = RequestMethod.GET)
    public ModelAndView userProfilePage(HttpServletRequest request, HttpSession session) {

        ModelAndView model = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.isAuthenticated()
                && auth.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
            User u = userService.getUser(auth.getName());
            if (u != null) {
                model.addObject("user", u);
                model.setViewName("subPages/user");
            } else {
                model.addObject("errorCode", "404");
                model.addObject("errorMessage", "User Not Found");
                model.addObject("errorDescription", "The Requested user does not exist.");
                model.setViewName("error");
            }
        } else {
            model.addObject("msg", "Invalid username and password!");
            log.info("Invalid username and password!");
            model.setViewName("login");
        }
        return model;
    }

    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    public @ResponseBody
    String updateUser(HttpServletRequest request, HttpSession httpSession) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            String uid = request.getParameter("eid");
            String username = request.getParameter("eusername");
            String password = request.getParameter("epassword");
            String fullname = request.getParameter("efullname");
            String role = request.getParameter("erole");
            String status = request.getParameter("estatus");

            User user = userService.getUser(Integer.parseInt(uid));
            User oldUser = new User();
            BeanUtils.copyProperties(user, oldUser);
            if (user != null) {
                user.setUsername(username);
                user.setPassword(password);
                user.setFullname(fullname);

                // dont change status of ADMIN
                if (!user.getRole().equalsIgnoreCase(Constants.ROLE_ADMIN)) {
                    user.setEnabled((short) (Integer.parseInt(status)));
                }

                // changes role only if previous role isn't ADMIN
                user.setRole((!user.getRole().equalsIgnoreCase(Constants.ROLE_ADMIN)) ? role.toUpperCase() : Constants.ROLE_ADMIN);

                boolean updated = userService.updateUser(user);
                if (!updated) {
                    return ("01:FAILED TO UPDATE. Username is already in use.");
                } else {
                    logActivity(request, auth.getName(), "UPDATED USER", "Old: " + oldUser.toString() + ", NEW: " + user.toString());
                    return "00:User Updated Successfully";
                }
            }
        } catch (Exception e) {
            log.error("Exception while updating user:", e);
            return ("01:Invalid Values");
        }
        return "01:FAILED TO UPDATE.";
    }

    @RequestMapping(value = "/createUser", method = RequestMethod.POST)
    public @ResponseBody
    String createUser(HttpServletRequest request, HttpSession httpSession) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String fullname = request.getParameter("fullname");
            String role = request.getParameter("role");

            User u = new User();
            u.setUsername(username);
            u.setPassword(password);
            u.setFullname(fullname);
            u.setEnabled((short) 1);
            u.setPicture(Constants.DEFAULT_IMG);
            u.setRole(role.toUpperCase());

            boolean userCreated = userService.addUser(u);
            if (!userCreated) {
                return ("01:Failed To Create User. Username is already in use.");
            } else {
                logActivity(request, auth.getName(), "ADDED USER", u.toString());
                return "00:User Created Successfully";
            }
        } catch (Exception e) {
            log.error("Exception while updating user:", e);
            return ("01:Invalid Values");
        }
    }

    @RequestMapping(value = "/updateProfile", method = RequestMethod.POST)
    public @ResponseBody
    String updateProfile(
            HttpServletRequest request, HttpSession httpSession,
            @RequestParam(value = "epicture", required = false) MultipartFile picture) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            String uid = request.getParameter("eid");
            User user = userService.getUser(Integer.parseInt(uid));

            if (user != null) {
                String fullname = request.getParameter("efullname");

                String imagePath = Constants.IMAGES_BASE_PATH + user.getId() + "-profile-pic_" + System.currentTimeMillis() + ".jpg";
                boolean imageStored = imageUtil.processAndStoreImage(picture, Constants.BASE_PATH + imagePath);

                User oldUser = new User();
                BeanUtils.copyProperties(user, oldUser);

                if (imageStored) {
                    user.setPicture(imagePath);
                }
                user.setFullname(fullname);

                boolean updated = userService.updateUser(user);
                if (!updated) {
                    return ("01:FAILED TO UPDATE, Name cannot be empty");
                } else {
                    logActivity(request, auth.getName(), "UPDATED PROFILE", "Name: "
                            + oldUser.getFullname() + ", picture: " + oldUser.getPicture()
                            + ", TO -> Name: " + user.getFullname() + ", picture: " + user.getPicture());
                    return "00:Profile Updated Successfully";
                }
            }
        } catch (Exception e) {
            log.error("Exception while updating user:", e);
            return ("01:Invalid Values");
        }
        return "01:FAILED TO UPDATE.";
    }

    @RequestMapping(value = "/user/{userId}/activity/data", method = RequestMethod.GET)
    public @ResponseBody
    DataTableResp getUsersData(
            @PathVariable int userId,
            @RequestParam(value = "startDate", required = false, defaultValue = "-1") String startDate,
            @RequestParam(value = "endDate", required = false, defaultValue = "-1") String endDate,
            @RequestParam(value = "sortBy", required = false, defaultValue = "desc") String sortBy,
            @RequestParam(value = "start", required = false, defaultValue = "0") int start,
            @RequestParam(value = "length", required = false, defaultValue = "10") int length,
            @RequestParam(value = "draw", required = false, defaultValue = "0") Integer draw) {

        DataTableResp resp = new DataTableResp();

        User user = userService.getUser(userId);
        List<UserActivity> data = activityService.getUserActivity(user, start, length, sortBy);
        int totalActivityListSize = activityService.getUserActivityCount(user);

        resp.setData(data);
        resp.setRecordsFiltered(totalActivityListSize);
        resp.setRecordsTotal(totalActivityListSize);
        resp.setDraw(draw);

        return resp;
    }

    private void logActivity(HttpServletRequest request, String username, String action, String desc) {
        String remoteAddr = request.getHeader("x-forwarded-for") != null ? request.getHeader("x-forwarded-for") : request.getRemoteAddr();
        String ua = request.getHeader("user-agent");
        asyncUtil.logActivity(request, username, remoteAddr, ua, action, desc);
    }
}
