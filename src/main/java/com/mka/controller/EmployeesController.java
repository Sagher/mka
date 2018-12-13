/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.controller;

import com.mka.model.Employees;
import com.mka.model.User;
import com.mka.model.response.DataTableResp;
import com.mka.service.EmployeesService;
import com.mka.service.UserService;
import com.mka.utils.AsyncUtil;
import com.mka.utils.Constants;
import com.mka.utils.ImageUtil;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Sagher Mehmood
 */
@Controller
public class EmployeesController {

    //logger for logging
    public static Logger log = Logger.getLogger(EmployeesController.class);

    @Autowired
    UserService userService;

    @Autowired
    AsyncUtil asyncUtil;

    @Autowired
    EmployeesService employeesService;

    @Autowired
    ImageUtil imageUtil;

    /* 
     * 
     * Model And View Controller for the /users page
     */
    @RequestMapping(value = "/employees", method = RequestMethod.GET)
    public ModelAndView usersPage(HttpServletRequest request, HttpSession session) {

        ModelAndView model = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.isAuthenticated() && auth.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
            User u = userService.getUser(auth.getName());
            if (u != null
                    && (u.getRole().equals(Constants.ROLE_ADMIN) || u.getRole().equals(Constants.ROLE_MANAGER))) {
                model.addObject("user", u);
                model.setViewName("subPages/employees");
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

    @RequestMapping(value = "/updateEmployee", method = RequestMethod.POST)
    public @ResponseBody
    String updateEmployee(HttpServletRequest request, HttpSession httpSession) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            int id = Integer.parseInt(request.getParameter("eid"));
            String name = request.getParameter("ename");
            String phone = request.getParameter("ephone");
            String cnic = request.getParameter("ecnic");
            String email = request.getParameter("eemail");
            String salary = request.getParameter("esalary");
            String role = request.getParameter("erole");
            String doj = request.getParameter("edoj");
            String address = request.getParameter("eaddress");

            Employees emp = employeesService.getEmployee(id);
            Employees oldEmp = new Employees();
            BeanUtils.copyProperties(emp, oldEmp);
            if (emp != null) {
                emp.setName(name);
                emp.setEmail(!email.isEmpty() ? email : emp.getEmail());
                emp.setCnic(cnic);
                emp.setPhone(phone);
                emp.setSalary(BigDecimal.valueOf(Long.parseLong(salary)));
                emp.setRole(role);
                emp.setJoiningDate(doj);
                emp.setAddress(!address.isEmpty() ? address : emp.getAddress());

                boolean updated = employeesService.updateEmployee(emp);
                if (!updated) {
                    return ("01:FAILED TO UPDATE.");
                } else {
                    logActivity(request, auth.getName(), "UPDATED EMPLOYEE", "Old: " + oldEmp.toString() + ", NEW: " + emp.toString());
                    return "00:Employee Updated Successfully";
                }
            }
        } catch (Exception e) {
            log.error("Exception while updating employee:", e);
            return ("01:Invalid Values");
        }
        return "01:FAILED TO UPDATE.";
    }

    @RequestMapping(value = "/createEmployee", method = RequestMethod.POST)
    public @ResponseBody
    String createEmployee(HttpServletRequest request, HttpSession httpSession) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String address = request.getParameter("address");
            String cnic = request.getParameter("cnic");
            String phone = request.getParameter("phone");
            String salary = request.getParameter("salary");
            String role = request.getParameter("role");
            String doj = request.getParameter("doj");

            Employees emp = new Employees();
            emp.setName(name);
            emp.setEmail(!email.isEmpty() ? email : null);
            emp.setPhone(phone);
            emp.setCnic(cnic);
            emp.setAddress(!address.isEmpty() ? address : null);
            emp.setSalary(BigDecimal.valueOf(Long.parseLong(salary)));
            emp.setRole(role);
            emp.setJoiningDate(doj);
            emp.setCreatedDate(new Date());
            emp.setIsActive(true);

            boolean empCreated = employeesService.addEmployee(emp);
            if (!empCreated) {
                return ("01:Failed To Create Employee.");
            } else {
                logActivity(request, auth.getName(), "ADDED EMPLOYEE", emp.toString());
                return "00:Employee Added Successfully";
            }
        } catch (Exception e) {
            log.error("Exception while adding employee:", e);
            return ("01:Invalid Values");
        }
    }

    @RequestMapping(value = "/deleteEmployee", method = RequestMethod.POST)
    public @ResponseBody
    String deleteEmployee(HttpServletRequest request, HttpSession httpSession) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            int id = Integer.parseInt(request.getParameter("eid"));
            String terminationDate = request.getParameter("terminationDate");

            Employees emp = employeesService.getEmployee(id);
            if (emp != null) {
                if (terminationDate != null && !terminationDate.isEmpty()) {
                    //terminate employee
//                    emp.setIsActive(false);
                    emp.setIsTerminated(true);
                    emp.setTerminationDate(Constants.DATE_FORMAT.parse(terminationDate));
                    boolean updated = employeesService.updateEmployee(emp);
                    if (!updated) {
                        return ("01:FAILED TO TERMINATE.");
                    } else {
                        logActivity(request, auth.getName(), "TERMINATED EMPLOYEE ", emp.toString());
                        return "00:Employee Terminated Successfully";
                    }
                } else {
                    emp.setIsActive(false);
                    boolean updated = employeesService.updateEmployee(emp);
                    if (!updated) {
                        return ("01:FAILED TO DELETE.");
                    } else {
                        logActivity(request, auth.getName(), "DELETED EMPLOYEE ", emp.toString());
                        return "00:Employee Deleted Successfully";
                    }
                }

            }
        } catch (Exception e) {
            log.error("Exception while deleting employee:", e);
            return ("01:Invalid Values");
        }
        return "01:FAILED TO DELETE.";
    }

    @RequestMapping(value = "/employees/data", method = RequestMethod.GET)
    public @ResponseBody
    DataTableResp getUsersData(
            HttpServletRequest request,
            @RequestParam(value = "start", required = false, defaultValue = "0") int startIndex,
            @RequestParam(value = "length", required = false, defaultValue = "10") int fetchSize,
            @RequestParam(value = "draw", required = false, defaultValue = "0") Integer draw,
            @RequestParam(value = "search[value]", required = false) String search) {

        DataTableResp resp = new DataTableResp();

        int order0Col = Integer.parseInt(request.getParameter("order[0][column]"));
        String orderBy = request.getParameter("order[0][dir]");
        String sortby = request.getParameter("columns[" + order0Col + "][data]");

        List<Employees> data = employeesService.getEmployees(startIndex, fetchSize, orderBy, sortby, search);
        int totalSize = employeesService.getEmployeesCount(search);

        resp.setData(data != null ? data : "");
        resp.setRecordsFiltered(totalSize);
        resp.setRecordsTotal(totalSize);
        resp.setDraw(draw);

        return resp;
    }

    @RequestMapping(value = "/payAllEmployees", method = RequestMethod.GET)
    public @ResponseBody
    String payAllEmployees(HttpServletRequest request, HttpSession httpSession) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.isAuthenticated() && auth.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
            User u = userService.getUser(auth.getName());
            if (u != null
                    && (u.getRole().equals(Constants.ROLE_ADMIN) || u.getRole().equals(Constants.ROLE_MANAGER))) {
                if (!employeesService.payAllEmployees()) {
                    return ("01:EMPLOYEES SALARIES HAVE ALREADY BEEN PAYED IN THIS MONTH");
                } else {
                    logActivity(request, auth.getName(), "PAY EMPLOYEES", "Employees Salaries Have been Successfully Payed");
                    return "00:Employees Salaries Have been Successfully Payed";
                }
            } else {
            }
        }
        return ("01:FAILED TO PAY ALL EMPLOYEES.");
    }

    private void logActivity(HttpServletRequest request, String username, String action, String desc) {
        String remoteAddr = request.getHeader("x-forwarded-for") != null ? request.getHeader("x-forwarded-for") : request.getRemoteAddr();
        String ua = request.getHeader("user-agent");
        asyncUtil.logActivity(request, username, remoteAddr, ua, action, desc);
    }
}
