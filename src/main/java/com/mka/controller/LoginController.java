package com.mka.controller;

/**
 *
 * @author Sagher Mehmood
 */
import static com.mka.controller.EmployeesController.log;
import com.mka.model.User;
import com.mka.service.UserService;
import com.mka.utils.AsyncUtil;
import com.mka.utils.Constants;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

    //logger for logging
    public static Logger log = Logger.getLogger(LoginController.class);

    @Autowired
    UserService userService;

    @Autowired
    AsyncUtil asyncUtil;

//    @RequestMapping(value = "/login", method = RequestMethod.POST)
//    public ModelAndView loginPOST(
//            @RequestParam(value = "username") String username,
//            @RequestParam(value = "password") String password,
//            HttpSession session) {
//        ModelAndView model = new ModelAndView();
//        User user = userService.loginUser(username.trim(), password.trim());
//        if (user != null && user.getEnabled() == 1) {
//            model.setViewName("redirect:/index");
//        } else {
//            model.setViewName("/login?error=1");
//        }
//        return model;
//    }
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout) {
        ModelAndView model = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.isAuthenticated()
                && auth.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
            model.setViewName("redirect:/index");
            return model;
        }
        if (error != null) {
            log.info("Invalid username and password!");
            model.addObject("error", "Invalid username or password!");
        }
        if (logout != null) {
            log.info("User logged out successfully.!");
            model.addObject("msg", "You have been logged out successfully.");
        }
        model.setViewName("/login");
        return model;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpServletRequest request, HttpSession session, HttpServletResponse response) {
        ModelAndView model = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.isAuthenticated() && auth.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
            User u = userService.getUser(auth.getName());
            if (u != null) {
                u.setLastLoginDate(new Date());
                asyncUtil.updateUser(u);
            }
        }
        new SecurityContextLogoutHandler().logout(request, response, auth);
        log.info("User logged out ...");
        model.setViewName("redirect:/login?logout");
        return model;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ModelAndView singout(HttpSession session) {
        session.invalidate();
        ModelAndView model = new ModelAndView();
        model.addObject("logout", "You have been logged out successfully.");
        model.addObject("msg", "You have been logged out successfully.");
        log.info("User logged out ...");
        model.setViewName("/login");
        return model;
    }

    //for 403 access denied page
    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public ModelAndView accesssDenied() {
        ModelAndView model = new ModelAndView();
        model.setViewName("403");
        return model;
    }

    //for 403 access denied page
    @RequestMapping(value = "/403", method = RequestMethod.POST)
    public ModelAndView accesssDeniedPost() {
        ModelAndView model = new ModelAndView();
        model.setViewName("403");
        return model;
    }
}
