<%-- 
    Document   : fragment-header
    Created on : Sep 18, 2018, 3:11:08 PM
    Author     : Sagher Mehmood
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<script>var ctx = "${pageContext.request.contextPath}"</script>

<header class="app-header navbar">
    <button class="navbar-toggler sidebar-toggler d-lg-none mr-auto" type="button" data-toggle="sidebar-show">
        <span class="navbar-toggler-icon"></span>
    </button>
    <a class="navbar-brand" href="<c:url value="/" />">
        <img style="margin-left:50px" class="navbar-brand-full" src="<c:url value="/resources/img/brand/logo.png"/>" width="" height="50" alt="">
        <img class="navbar-brand-full" src="<c:url value="/resources/img/brand/logo-text.svg"/>" width="130" height="50" alt="">
    </a>
    <button style="margin-left:40px" class="navbar-toggler sidebar-toggler d-md-down-none" type="button" data-toggle="sidebar-lg-show">
        <span class="navbar-toggler-icon"></span>
    </button>
    <!--    <ul class="nav navbar-nav d-md-down-none">
                  <li class="nav-item px-3">
                         <a class="nav-link" href="<c:url value="/" />">Dashboard</a>
                     </li>
    <sec:authorize access="hasAnyRole('ADMIN', 'ROLE_ADMIN')">
        <li class="nav-item px-3">
            <a class="nav-link" href="<c:url value="/users" />">Users</a>
        </li>
    </sec:authorize>
    <sec:authorize access="hasAnyRole('ADMIN','MANAGER', 'ROLE_ADMIN')">
        <li class="nav-item px-3">
            <a class="nav-link" href="<c:url value="/employees" />">Employees</a>
        </li>
    </sec:authorize>

    <li class="nav-item px-3">
        <a class="nav-link" href="<c:url value="/user/profile" />">Profile</a>
    </li>
</ul>-->
    <ul class="nav navbar-nav ml-auto">
        <li class="nav-item dropdown" style="margin-right: 5px">
            <a class="nav-link" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">
                <img class="img-avatar" src="<c:url value="${user.picture}"/>">
            </a>
            <div class="dropdown-menu dropdown-menu-right">
                <div class="dropdown-header text-center">
                    <strong>${user.username}</strong>
                </div>
                <a class="dropdown-item" href="<c:url value="/user/profile" />">
                    <i class="fa fa-user"></i> Profile</a>
                <button class="dropdown-item" onclick="formSubmit()"><i class="fa fa-lock"></i> Logout</button>
                <c:url value="/logout" var="logoutUrl" />
                <form action="${logoutUrl}" method="get" id="logoutForm">
                    <input type="hidden" name="${_csrf.parameterName}"
                           value="${_csrf.token}" />
                </form>
                <script>
                    function formSubmit() {
                        document.getElementById("logoutForm").submit();
                    }
                </script>
            </div>
        </li>
    </ul>
    <!-- right sidebar -->
    <!--    <button class="navbar-toggler aside-menu-toggler d-md-down-none" type="button" data-toggle="aside-menu-lg-show">
            <span class="navbar-toggler-icon"></span>
        </button>
        <button class="navbar-toggler aside-menu-toggler d-lg-none" type="button" data-toggle="aside-menu-show">
            <span class="navbar-toggler-icon"></span>
        </button>-->
</header>
