<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<jsp:directive.page session="true" />
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
        <title>Employees</title>

        <c:import url="../fragments/global-css.jsp" />

    </head>
    <body class="app header-fixed sidebar-fixed aside-menu-fixed">
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
                                                <div class="pull-right">
                                                    <button class="btn btn-sm btn-default" onclick="payAllEmployees()">
                                                        Pay All
                                                    </button>
                                                    <button class="btn btn-sm btn-primary" 
                                                            type="button" data-toggle="modal" data-target="#newUserModal">
                                                        Add New Employee
                                                    </button>
                                                </div>
                                            </div>
                                            <div class="card-body">
                                                <table id="viewDatatable"
                                                       class="table table-responsive table-bordered table-striped table-sm">
                                                    <thead>
                                                        <tr>
                                                            <th>ID</th>
                                                            <th>Name</th>
                                                            <th>Phone</th>
                                                            <th>CNIC</th>
                                                            <th>Email</th>
                                                            <th>Salary</th>
                                                            <th>Role</th>
                                                            <th>Joining Date</th>
                                                            <th>Address</th>
                                                            <th>Status</th>
                                                            <th style="min-width: 120px">Action</th>
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
                    <div class="modal-dialog modal-lg" role="document">
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
                                        <div class="col-sm-6">
                                            <div class="form-group">
                                                <label for="phone">Phone</label>
                                                <input class="form-control" id="phone" name="phone" required="true" type="tel" placeholder="Enter Employee's Phone">
                                            </div>
                                        </div>
                                        <div class="col-sm-6">
                                            <div class="form-group">
                                                <label for="email">Email</label>
                                                <input class="form-control" id="email" name="email" type="email" placeholder="Enter Employee's Email">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-sm-6">
                                            <div class="form-group">
                                                <label for="cnic">CNIC # <small>( Format: 12345-1234567-0 )</small></label>
                                                <input class="form-control" id="cnic" name="cnic" type="text" placeholder="12345-1234567-0" pattern="\d{5}-?\d{7}-?\d{1}">
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
                                        <div class="col-sm-6">
                                            <div class="form-group">
                                                <label for="salary">Salary</label>
                                                <input class="form-control" id="salary" name="salary" required="true" type="number" placeholder="Enter Employee's Salary in PKR">
                                            </div>
                                        </div>
                                        <div class="col-sm-6">
                                            <div class="form-group">
                                                <label for="role">Role</label>
                                                <input class="form-control" id="role" name="role" required="true" type="text" placeholder="Enter Employee's Role i.e, Accountant, Manager, Labour">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-sm-12">
                                            <div class="form-group">
                                                <label for="address">Address</label>
                                                <input class="form-control" id="email" name="address" type="text" placeholder="Enter Employee's address">
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
                    <div class="modal-dialog modal-lg" role="document">
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
                                        <div class="col-sm-6">
                                            <div class="form-group">
                                                <label for="ephone">Phone</label>
                                                <input class="form-control" id="ephone" name="ephone" required type="text" placeholder="Enter Employee's Phone">
                                            </div>
                                        </div>
                                        <div class="col-sm-6">
                                            <div class="form-group">
                                                <label for="eemail">Email</label>
                                                <input class="form-control" id="eemail" name="eemail" type="email" placeholder="Enter Employee's Email">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-sm-6">
                                            <div class="form-group">
                                                <label for="ecnic">CNIC # <small>( Format: 12345-1234567-0 )</small></label>
                                                <input class="form-control" id="ecnic" name="ecnic" type="text" placeholder="12345-1234567-0" pattern="\d{5}-?\d{7}-?\d{1}">
                                            </div>
                                        </div>
                                        <div class="col-sm-6">
                                            <div class="form-group">
                                                <label for="edoj">Date of Joining</label>
                                                <input class="form-control" id="edoj" name="edoj" required="false" type="date" placeholder="Enter Employee's Date of Joining">
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
                                                <label for="erole">Role</label>
                                                <input class="form-control" id="erole" name="erole" required type="text">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-sm-12">
                                            <div class="form-group">
                                                <label for="eaddress">Address</label>
                                                <input class="form-control" id="eaddress" name="eaddress" type="text">
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
                                    <button id="confirmDelBtn" class="btn btn-danger" type="submit">Confirm</button>
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

    </body>

    <!-- global js -->
    <c:import url="../fragments/global-js.jsp" />

    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/v/dt/dt-1.10.18/datatables.min.css"/>
    <script type="text/javascript" src="https://cdn.datatables.net/v/dt/dt-1.10.18/datatables.min.js"></script>     
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.form/4.2.2/jquery.form.min.js"></script>
    <script src="<c:url value="/resources/js/employees.js"/>"></script>

</html>

