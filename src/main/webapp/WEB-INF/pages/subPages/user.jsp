<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<jsp:directive.page session="true" />
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
        <title>Profile</title>

        <c:import url="../fragments/global-css.jsp" />

        <style>
            .col-md-3.col-form-label{
                font-weight: bold;
            }
        </style>

    </head>
    <body class="app header-fixed sidebar-fixed aside-menu-fixed sidebar-lg-show">
        <!-- top navigation bar -->
        <c:import url="../fragments/fragment-header.jsp" />

        <!-- app-body -->
        <div class="app-body">

            <!-- left side bar -->
            <c:import url="../fragments/fragment-sidebar-left.jsp" />

            <!-- main container -->
            <main class="main">

                <!-- Bread crumb -->
                <c:import url="../fragments/fragment-breadcrumb.jsp" />

                <div class="container-fluid">
                    <div id="ui-view">
                        <div>
                            <div class="animated fadeIn">

                                <div class="row">
                                    <div class="col-lg-12">
                                        <div class="card">
                                            <div class="card-header">
                                                <i class="fa fa-align-justify"></i>
                                                <span>${user.username} 's Profile</span>
                                            </div>
                                            <div class="card-body">
                                                <form class="form-horizontal" action="<c:url value="/updateProfile"/>"
                                                      id="updateProfileForm" method="post" enctype="multipart/form-data">
                                                    <div class="form-group row">
                                                        <label class="col-md-3 col-form-label">Username</label>
                                                        <label class="col-md-9 col-form-label">${user.username}</label>
                                                    </div>
                                                    <div class="form-group row">
                                                        <label class="col-md-3 col-form-label" for="efullname">Full Name</label>
                                                        <div class="col-md-9">
                                                            <input class="form-control" id="efullname" type="text" name="efullname" value="${user.fullname}">
                                                        </div>
                                                    </div>
                                                    <div class="form-group row">
                                                        <label class="col-md-3 col-form-label">Password</label>
                                                        <div class="col-md-9">
                                                            <input class="form-control" readonly="true" value="${user.password}">
                                                            <span class="help-block">Please contact Admin for a password change</span>
                                                        </div>
                                                    </div>
                                                    <div class="form-group row">
                                                        <label class="col-md-3 col-form-label">Created At</label>
                                                        <label class="col-md-9 col-form-label">${user.createdDate}</label>
                                                    </div>
                                                    <div class="form-group row">
                                                        <label class="col-md-3 col-form-label">Role</label>
                                                        <label class="col-md-9 col-form-label">${user.role}</label>
                                                    </div>
                                                    <div class="form-group row">
                                                        <label class="col-md-3 col-form-label">Status</label>
                                                        <label class="col-md-9 col-form-label">
                                                            <c:if test="${user.enabled == 1}">
                                                                <span class="badge badge-success">Active</span>
                                                            </c:if>
                                                            <c:if test="${user.enabled == 0}">
                                                                <span class="badge badge-danger">Inactive</span>
                                                            </c:if>
                                                        </label>
                                                    </div>
                                                    <div class="form-group row">
                                                        <label class="col-md-3 col-form-label">Profile Picture</label>
                                                        <label class="col-md-9 col-form-label">
                                                            <img style="max-height: 150px" src="<c:url value="${user.picture}"/>">

                                                        </label>
                                                    </div>
                                                    <div class="form-group row">
                                                        <label class="col-md-3 col-form-label">Upload New Picture</label>
                                                        <div class="col-md-9">
                                                            <input id="epicture" name="epicture" accept="image/*" type="file" name="file-input">
                                                        </div>
                                                    </div>
                                                    <hr/>
                                                    <div class="row">
                                                        <label class="col-md-3 col-form-label" for="text-input"></label>
                                                        <label class="col-md-9 col-form-label">
                                                            <button id="updateProfileBtn" class="btn btn-lg btn-primary" 
                                                                    type="submit" onclick="updateProfile()">UPDATE PROFILE</button>
                                                        </label>
                                                    </div>
                                                    <input id="eid" name="eid" value="${user.id}" hidden="true">
                                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                                </form>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                            </div>

                        </div>
                    </div>
                </div>


            </main>

            <!-- right side bar -->
            <c:import url="../fragments/fragment-sidebar-right.jsp" />

        </div>

        <!-- footer -->
        <c:import url="../fragments/fragment-footer.jsp" />

    </body>

    <!-- global js -->
    <c:import url="../fragments/global-js.jsp" />
    <!-- Plugins and scripts required by this view-->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.form/4.2.2/jquery.form.min.js"></script>
    <script src="<c:url value="/resources/js/user.js"/>"></script>
</html>

