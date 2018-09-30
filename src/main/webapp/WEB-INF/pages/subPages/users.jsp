<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<jsp:directive.page session="true" />
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
        <title>Users</title>

        <c:import url="../fragments/global-css.jsp" />

        <style>
            thead tr td {
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
                                                <span>All Users</span>
                                                <button class="btn btn-sm btn-primary pull-right" 
                                                        type="button" data-toggle="modal" data-target="#newUserModal">
                                                    Create New User
                                                </button>
                                            </div>
                                            <div class="card-body">
                                                <table class="table table-responsive-sm table-bordered table-striped table-sm">
                                                    <thead>
                                                        <tr>
                                                            <td>UserName</td>
                                                            <td>Role</td>
                                                            <td>Password</td>
                                                            <td>Full Name</td>
                                                            <td align="center">Status</td>
                                                            <td>Created At</td>
                                                            <td>last Login</td>
                                                            <td align="center">Edit</td>
                                                            <td align="center">Activity</td>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:forEach items="${users}" var="user">
                                                            <tr id="user-${user.id}">
                                                                <td>${user.username}</td>
                                                                <td>${user.role}</td>
                                                                <td>
                                                                    <c:choose>
                                                                        <c:when test="${user.role=='ADMIN'}">
                                                                            ******
                                                                        </c:when>    
                                                                        <c:otherwise>
                                                                            ${user.password}
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                                <td>${user.fullname}</td>
                                                                <td align="center">
                                                                    <c:if test="${user.enabled == 1}">
                                                                        <span class="badge badge-success">Active</span>
                                                                    </c:if>
                                                                    <c:if test="${user.enabled == 0}">
                                                                        <span class="badge badge-danger">Inactive</span>
                                                                    </c:if>
                                                                </td>
                                                                <td>${user.createdDate}</td>
                                                                <td>${user.lastLoginDate}</td>
                                                                <td align="center">
                                                                    <button class="btn btn-warning" onclick="editUser(${user.id})">
                                                                        <i class="fa fa-edit"></i>
                                                                    </button>
                                                                </td>
                                                                <td align="center">
                                                                    <a href="<c:url value="/user/${user.id}/activity" />" 
                                                                       class="btn btn-sm">View</a>
                                                                </td>
                                                                <!-- for edit modal -->
                                                        <input value="${user.username}" class="iusername" type="hidden">
                                                        <input value="${user.role}" class="irole" type="hidden">
                                                        <input value="${user.password}" class="ipassword" type="hidden">
                                                        <input value="${user.fullname}" class="ifullname" type="hidden">
                                                        <input value="${user.enabled}" class="ienabled" type="hidden">

                                                        </tr>
                                                    </c:forEach>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <!-- newUserModal -->
                                <div class="modal fade" id="newUserModal" tabindex="-1" role="dialog" 
                                     aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
                                    <div class="modal-dialog" role="document">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h4 class="modal-title">Create New User</h4>
                                                <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                                                    <span aria-hidden="true">×</span>
                                                </button>
                                            </div>
                                            <form id="createUserForm" method="post"
                                                  action="<c:url value='/createUser'/>" enctype="multipart/form-data"> 
                                                <div class="modal-body">
                                                    <div class="row">
                                                        <div class="col-sm-12">
                                                            <div class="form-group">
                                                                <label for="username">UserName</label>
                                                                <input class="form-control" id="username" name="username" required="true" type="text" placeholder="Enter unique username">
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="row">
                                                        <div class="col-sm-12">
                                                            <div class="form-group">
                                                                <label for="password">Password</label>
                                                                <input class="form-control" id="password" name="password" required="true" type="text" placeholder="Enter a password">
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="row">
                                                        <div class="col-sm-12">
                                                            <div class="form-group">
                                                                <label for="fullname">Full Name</label>
                                                                <input class="form-control" id="fullname" name="fullname" required="true" type="text" placeholder="Enter User's FullName">
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="row">
                                                        <div class="form-group col-sm-12">
                                                            <label for="role">User Role </label>
                                                            <select class="form-control" name="role" id="role" required="true">
                                                                <option value="">-- Select Role --</option>
                                                                <c:forEach items="${allRoles}" var="role">
                                                                    <c:if test="${role != 'ADMIN'}">
                                                                        <option value="${role}">
                                                                            ${role}
                                                                        </option>
                                                                    </c:if>
                                                                </c:forEach>
                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="modal-footer">
                                                    <button class="btn btn-secondary" type="button" data-dismiss="modal">Close</button>
                                                    <button id="createUserBtn" class="btn btn-primary" 
                                                            type="submit" onclick="createUser()">Create User</button>
                                                </div>
                                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                            </form>

                                        </div>
                                    </div>
                                </div>

                                <!-- editUserModal -->
                                <div class="modal fade" id="editUserModal" tabindex="-1" role="dialog" 
                                     aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
                                    <div class="modal-dialog" role="document">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h4 class="modal-title">Edit User</h4>
                                                <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                                                    <span aria-hidden="true">×</span>
                                                </button>
                                            </div>
                                            <form id="editUserForm" method="post"
                                                  action="<c:url value='/updateUser'/>" enctype="multipart/form-data"> 
                                                <input id="eid" name="eid" hidden="true">

                                                <div class="modal-body">
                                                    <div class="row">
                                                        <div class="col-sm-12">
                                                            <div class="form-group">
                                                                <label for="eusername">UserName</label>
                                                                <input class="form-control" id="eusername" name="eusername" readonly="true" required="true" type="text" placeholder="Enter unique username">
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="row">
                                                        <div class="col-sm-12">
                                                            <div class="form-group">
                                                                <label for="epassword">Password</label>
                                                                <input class="form-control" id="epassword" name="epassword" required="true" type="text" placeholder="Enter a password">
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="row">
                                                        <div class="col-sm-12">
                                                            <div class="form-group">
                                                                <label for="efullname">Full Name</label>
                                                                <input class="form-control" id="efullname" name="efullname" required="true" type="text" placeholder="Enter User's FullName">
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="row">
                                                        <div class="col-sm-12">
                                                            <div class="form-group">
                                                                <div class="col-sm-4 form-check form-check-inline mr-1">
                                                                    <input class="form-check-input" id="inline-radio1" type="radio" value="1" name="estatus">
                                                                    <label class="form-check-label" for="inline-radio1">Active</label>
                                                                </div>
                                                                <div class="col-sm-4 form-check form-check-inline mr-1">
                                                                    <input class="form-check-input" id="inline-radio0" type="radio" value="0" name="estatus">
                                                                    <label class="form-check-label" for="inline-radio0">Inactive</label>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="row">
                                                        <div class="form-group col-sm-12">
                                                            <label for="erole">User Role </label>
                                                            <select class="form-control" name="erole" id="erole" required="true">
                                                                <c:forEach items="${allRoles}" var="role">
                                                                    <c:choose>
                                                                        <c:when test="${role=='ADMIN'}">
                                                                            <option disabled="disabled" value="${role}">
                                                                                ${role}
                                                                            </option>                                                                   
                                                                        </c:when>    
                                                                        <c:otherwise>
                                                                            <option value="${role}">
                                                                                ${role}
                                                                            </option>                                                                    
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </c:forEach>
                                                            </select>
                                                        </div>
                                                    </div>

                                                </div>
                                                <div class="modal-footer">
                                                    <button class="btn btn-secondary" type="button" data-dismiss="modal">Close</button>
                                                    <button id="updateUserBtn" class="btn btn-primary" type="submit" onclick="updateUser()">Save changes</button>
                                                </div>
                                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                            </form>
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
    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/v/dt/dt-1.10.18/datatables.min.css"/>
    <script type="text/javascript" src="https://cdn.datatables.net/v/dt/dt-1.10.18/datatables.min.js"></script>     
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.form/4.2.2/jquery.form.min.js"></script>
    <script src="<c:url value="/resources/js/users.js"/>"></script>
</html>

