<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<jsp:directive.page session="true" />
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
        <title>Home</title>
        <!-- Icons-->
        <link href="<c:url value="/resources/vendors/@coreui/coreui-icons.min.css"/>" rel="stylesheet">
        <link href="<c:url value="/resources/vendors/flag-icon-css/flag-icon.min.css"/>" rel="stylesheet">
        <link href="<c:url value="/resources/vendors/font-awesome/font-awesome.min.css"/>" rel="stylesheet">
        <link href="<c:url value="/resources/vendors/simple-line-icons/simple-line-icons.css"/>" rel="stylesheet">
        <!-- Main styles for this application-->
        <link href="<c:url value="/resources/css/style.css"/>" rel="stylesheet">
        <link href="<c:url value="/resources/vendors/pace-progress/css/pace.min.css"/>" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/v/dt/dt-1.10.18/datatables.min.css"/>

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
                                                <span>Employees</span>
                                                <button class="btn btn-sm btn-primary pull-right" 
                                                        type="button" data-toggle="modal" data-target="#newUserModal">
                                                    Add New Employee
                                                </button>
                                            </div>
                                            <div class="card-body">
                                                <table id="viewDatatable"
                                                       class="table table-responsive-sm table-bordered table-striped table-sm">
                                                    <thead>
                                                        <tr>
                                                            <th>ID</th>
                                                            <th>Name</th>
                                                            <th>Phone</th>
                                                            <th>Salary</th>
                                                            <th>Email</th>
                                                            <th>Role</th>
                                                            <th>Joining Date</th>
                                                            <th>Action</th>

                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>

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
                                <h4 class="modal-title">Add New Employee</h4>
                                <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">×</span>
                                </button>
                            </div>
                            <form id="createEmpForm" method="post"
                                  action="<c:url value='/createEmployee'/>" enctype="multipart/form-data"> 
                                <div class="modal-body">
                                    <div class="row">
                                        <div class="col-sm-12">
                                            <div class="form-group">
                                                <label for="name">Name</label>
                                                <input class="form-control" id="name" name="name" required="true" type="text" placeholder="Enter Employee's Name">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-sm-12">
                                            <div class="form-group">
                                                <label for="phone">Phone</label>
                                                <input class="form-control" id="phone" name="phone" required="true" type="text" placeholder="Enter Employee's Phome">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-sm-6">
                                            <div class="form-group">
                                                <label for="salary">Salary</label>
                                                <input class="form-control" id="salary" name="salary" required="true" type="number" placeholder="Enter Employee's Salary in PKR">
                                            </div>
                                        </div>
                                        <div class="col-sm-6">
                                            <div class="form-group">
                                                <label for="doj">Date of Joining</label>
                                                <input class="form-control" id="doj" name="doj" required="false" type="date" placeholder="Enter Employee's Date of Joining">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-sm-12">
                                            <div class="form-group">
                                                <label for="email">Email</label>
                                                <input class="form-control" id="email" name="email" required="false" type="text" placeholder="Enter Employee's Email">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-sm-12">
                                            <div class="form-group">
                                                <label for="role">Role</label>
                                                <input class="form-control" id="role" name="role" required="true" type="text" placeholder="Enter Employee's Role i.e, Accountant, Manager, Labour">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button class="btn btn-secondary" type="button" data-dismiss="modal">Close</button>
                                    <button id="createUserBtn" class="btn btn-primary" 
                                            type="submit" onclick="addEmployee()">Add</button>
                                </div>
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            </form>

                        </div>
                    </div>
                </div>

                <!-- editUserModal -->
                <div class="modal fade" id="editEmpModal" tabindex="-1" role="dialog" 
                     aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h4 class="modal-title">Edit Employee</h4>
                                <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">×</span>
                                </button>
                            </div>
                            <form id="editEmpForm" method="post"
                                  action="<c:url value='/updateEmployee'/>" enctype="multipart/form-data"> 
                                <input id="eid" name="eid" hidden="true">

                                <div class="modal-body">
                                    <div class="row">
                                        <div class="col-sm-12">
                                            <div class="form-group">
                                                <label for="ename">Name</label>
                                                <input class="form-control" id="ename" name="ename" required type="text" placeholder="Enter Employee Name">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-sm-12">
                                            <div class="form-group">
                                                <label for="ephone">Phone</label>
                                                <input class="form-control" id="ephone" name="ephone" required type="text">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-sm-6">
                                            <div class="form-group">
                                                <label for="esalary">Salary (In PKR)</label>
                                                <input class="form-control" id="esalary" name="esalary" required type="number">
                                            </div>
                                        </div>
                                        <div class="col-sm-6">
                                            <div class="form-group">
                                                <label for="edoj">Date Of Joining</label>
                                                <input class="form-control" id="edoj" name="edoj" required type="date">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-sm-12">
                                            <div class="form-group">
                                                <label for="eemail">Email</label>
                                                <input class="form-control" id="eemail" name="eemail" type="email">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-sm-12">
                                            <div class="form-group">
                                                <label for="erole">Role</label>
                                                <input class="form-control" id="erole" name="erole" required type="text">
                                            </div>
                                        </div>
                                    </div>

                                </div>
                                <div class="modal-footer">
                                    <button class="btn btn-secondary" type="button" data-dismiss="modal">Close</button>
                                    <button id="updateEmpBtn" class="btn btn-primary" type="submit" onclick="updateEmp()">Save changes</button>
                                </div>
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            </form>
                        </div>
                    </div>
                </div>

                <!-- delete emp -->
                <div class="modal fade" id="confirmDelModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                    <div class="modal-dialog modal-danger" role="document">
                        <div class="modal-content">
                            <form id="delEmpForm" method="post"
                                  action="<c:url value='/deleteEmployee'/>" enctype="multipart/form-data"> 
                                <div class="modal-header">
                                    <h4 class="modal-title">Confirm Deletion</h4>
                                    <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                                        <span aria-hidden="true">×</span>
                                    </button>
                                </div>
                                <div class="modal-body">

                                </div>
                                <div class="modal-footer">
                                    <button class="btn btn-secondary" type="button" data-dismiss="modal">Close</button>
                                    <button class="btn btn-danger" type="submit" onclick="deleteEmp()">Confirm</button>
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                </div>
                            </form>
                        </div>

                    </div>

                </div>
            </main>

            <!-- right side bar -->
            <c:import url="../fragments/fragment-sidebar-right.jsp" />

        </div>

        <!-- footer -->
        <c:import url="../fragments/fragment-footer.jsp" />

        <!-- CoreUI and necessary plugins-->
        <script src="<c:url value="/resources/vendors/jquery/jquery.min.js"/>"></script>
        <script src="<c:url value="/resources/vendors/popper.js/popper.min.js"/>"></script>
        <script src="<c:url value="/resources/vendors/bootstrap/bootstrap.min.js"/>"></script>
        <script src="<c:url value="/resources/vendors/pace-progress/pace.min.js"/>"></script>
        <script src="<c:url value="/resources/vendors/perfect-scrollbar/perfect-scrollbar.min.js"/>"></script>
        <script src="<c:url value="/resources/vendors/@coreui/coreui.min.js"/>"></script>
        <script type="text/javascript" src="https://cdn.datatables.net/v/dt/dt-1.10.18/datatables.min.js"></script>     

        <!-- Plugins and scripts required by this view-->
        <script src="http://malsup.github.com/jquery.form.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-noty/2.3.7/packaged/jquery.noty.packaged.min.js"></script>
        <script src="<c:url value="/resources/js/noty.js"/>"></script>
        <script src="<c:url value="/resources/js/employees.js"/>"></script>


    </body>
</html>

