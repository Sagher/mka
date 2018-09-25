<%-- 
    Document   : fragment-sidebar-left
    Created on : Sep 18, 2018, 3:11:08 PM
    Author     : Sagher Mehmood
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<div class="sidebar">
    <nav class="sidebar-nav">
        <ul class="nav">
            <li class="nav-item">
                <a class="nav-link" href="<c:url value="/" />">
                    <i class="nav-icon icon-speedometer"></i> Dashboard
                </a>
            </li>
            <li class="nav-title">Entry & Reporting</li>
            <li class="nav-item">
                <a class="nav-link" href="<c:url value="/logEntry" />">
                    <i class="nav-icon icon-pencil"></i>Log Entry
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<c:url value="/reports" />">
                    <i class="nav-icon icon-graph"></i>View Reports
                </a>
            </li>
            <sec:authorize access="hasAnyRole('ADMIN')">
                <li class="nav-title">Users</li>
                <li class="nav-item">
                    <a class="nav-link" href="<c:url value="/users" />">
                        <i class="nav-icon icon-user"></i>Manage Users
                    </a>
                </li>
            </sec:authorize>
            <sec:authorize access="hasAnyRole('ADMIN','MANAGER')">
                <li class="nav-title">Employees</li>
                <li class="nav-item">
                    <a class="nav-link" href="<c:url value="/employees" />">
                        <i class="nav-icon icon-people"></i>Manage Employees
                    </a>
                </li>
            </sec:authorize>
            <li class="nav-title">Profile</li>
            <li class="nav-item">
                <a class="nav-link" href="<c:url value="/user/profile" />">
                    <i class="nav-icon icon-user-follow"></i>View Profile
                </a>
            </li>
        </ul>
    </nav>
    <button class="sidebar-minimizer brand-minimizer" type="button"></button>
</div>
