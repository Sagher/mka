<%-- 
    Document   : fragment-breadcrumb
    Created on : Sep 18, 2018, 4:41:58 PM
    Author     : Sagher Mehmood
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<ol class="breadcrumb">
    <!--    <li class="breadcrumb-item">Home</li>
        <li class="breadcrumb-item active">
            Dashboard
        </li>-->
    <!-- Bread crumb Menu-->
    <!--    <li class="breadcrumb-menu" style="margin-left: 0%">
            <div class="btn-group" role="group" aria-label="Button group">
                <a class="btn" href="<c:url value="/entry" />">
                    <i class="icon-pencil"></i>  Log Entry</a>
                <a class="btn" href="<c:url value="/reports" />">
                    <i class="icon-graph"></i>  View Reports</a>
            </div>
        </li>-->
   
    <li class="breadcrumb-menu" >
        <div class="btn-group" role="group" aria-label="Button group">
            <a class="btn" href="<c:url value="/" />">
                <i class="nav-icon icon-speedometer"></i>  Dashboard</a>
            <a class="btn" href="<c:url value="/user/profile" />">
                <i class="icon-user"></i>  Profile</a>
            <a class="btn" href="<c:url value="/reports" />">
                <i class="icon-graph"></i>  View Reports</a>
            <a class="btn" href="<c:url value="/entry" />">
                <i class="icon-pencil"></i>  Log Entry</a>
        </div>
    </li>

</ol>