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
                <a class="nav-link" href="index.html">
                    <i class="nav-icon icon-speedometer"></i> Dashboard
                    <span class="badge badge-primary">NEW</span>
                </a>
            </li>
            <li class="nav-title">Theme</li>
            <li class="nav-item">
                <a class="nav-link" href="colors.html">
                    <i class="nav-icon icon-drop"></i> Colors</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="typography.html">
                    <i class="nav-icon icon-pencil"></i> Typography</a>
            </li>
            <li class="nav-title">Components</li>
            <li class="nav-item nav-dropdown">
                <a class="nav-link nav-dropdown-toggle" href="#">
                    <i class="nav-icon icon-puzzle"></i> Base</a>
                <ul class="nav-dropdown-items">
                    <li class="nav-item">
                        <a class="nav-link" href="base/breadcrumb.html">
                            <i class="nav-icon icon-puzzle"></i> Bread crumb</a>
                    </li>
                </ul>
            </li>
        </ul>
    </nav>
    <button class="sidebar-minimizer brand-minimizer" type="button"></button>
</div>
