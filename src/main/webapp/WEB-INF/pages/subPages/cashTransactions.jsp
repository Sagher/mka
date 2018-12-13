<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<jsp:directive.page session="true" />
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
        <title>Cash Transactions</title>

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
                                                <span>Cash Transactions</span>
                                            </div>
                                            <div class="card-body">
                                                <div id="filtersDiv">
                                                    <div class="form-group row">

                                                        <div class="col-sm-4">
                                                            <label>From</label>
                                                            <div class="input-group">
                                                                <div class="input-group-prepend">
                                                                    <span class="input-group-text">
                                                                        From
                                                                    </span>
                                                                </div>
                                                                <input class="form-control" id="from" type="date" name="from" placeholder="Search By Entry Date">
                                                            </div>
                                                        </div>
                                                        <div class="col-sm-4">
                                                            <label>To</label>
                                                            <div class="input-group">
                                                                <div class="input-group-prepend">
                                                                    <span class="input-group-text">
                                                                        To
                                                                    </span>
                                                                </div>
                                                                <input class="form-control" id="to" type="date" name="to" placeholder="Search By Entry Date">
                                                            </div>
                                                        </div>
                                                        <div class="col-sm-2">
                                                            <label>Buyer/Supplier</label>
                                                            <select class="form-control" id="buySup" name="buySup">
                                                                <option selected value="">All</option>
                                                            </select>
                                                        </div>
                                                        <div class="col-sm-2">
                                                            <button class="btn btn-primary pull-right" style="margin-top: 30px"
                                                                    onclick="advanceSearch()" type="button">Apply</button>
                                                        </div>
                                                    </div>

                                                </div>
                                                <hr>

                                                <table id="viewDatatable"
                                                       class="table table-responsive-sm table-bordered table-striped table-sm">
                                                    <thead>
                                                        <tr>
                                                            <th>Date</th>
                                                            <th>Type</th>
                                                            <th>Payee</th>
                                                            <th>Description</th>
                                                            <th>Amount</th>
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


                <!-- editUserModal -->
                <div class="modal fade" id="editModal" tabindex="-1" role="dialog" 
                     aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h4 class="modal-title">Edit Entry</h4>
                                <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">×</span>
                                </button>
                            </div>
                            <form id="editForm" method="post"
                                  action="<c:url value='/updateIndirectEntry'/>" enctype="multipart/form-data"> 
                                <input id="eid" name="eid" hidden="true">

                                <div class="modal-body">
                                    <div class="row">
                                        <div class="col-sm-12">
                                            <div class="form-group">
                                                <label for="ename">Name</label>
                                                <input class="form-control" id="ename" name="ename" required type="text">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-sm-12">
                                            <div class="form-group">
                                                <label for="edesc">Description</label>
                                                <input class="form-control" id="edesc" name="edesc" required type="text">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-sm-6">
                                            <div class="form-group">
                                                <label for="eamount">Amount</label>
                                                <input class="form-control" id="eamount" name="eamount" required type="number">
                                            </div>
                                        </div>
                                        <div class="col-sm-6">
                                            <div class="form-group">
                                                <label for="eadvance">Advance</label>
                                                <input class="form-control" id="eadvance" name="eadvance" required type="number">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button class="btn btn-secondary" type="button" data-dismiss="modal">Close</button>
                                    <button id="editModal" class="btn btn-primary" type="submit" onclick="update()">Save changes</button>
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
                            <form id="delForm" method="post"
                                  action="<c:url value='/deleteIndirectEntry'/>" enctype="multipart/form-data"> 
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
                                    <button class="btn btn-danger" type="submit" onclick="delet()">Confirm</button>
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
    <script src="<c:url value="/resources/js/cashTransactions.js"/>"></script>

</html>

