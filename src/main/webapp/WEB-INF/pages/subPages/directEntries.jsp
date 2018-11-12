<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<jsp:directive.page session="true" />
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
        <title>Entries</title>

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
                                                <span>
                                                    <b>${itemType.itemName}</b> 
                                                    Entries
                                                </span>
                                            </div>
                                            <div class="card-body">
                                                <div id="filtersDiv">
                                                    <input hidden="true" id="itemTypeId" value="${itemType.id}">
<!--                                                    <div id="stockDetailDiv" class="animated fadeIn form-group row" hidden="true">
                                                        <div class="col-md-6">
                                                            <div class="brand-card item-summary" id="stockDetail">
                                                            </div>
                                                        </div>
                                                    </div>-->
                                                    <div class="form-group row">

                                                        <div class="col-sm-3">
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
                                                        <div class="col-sm-3">
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
                                                            <label>Entry Type</label>
                                                            <select class="form-control" id="subEntryType" name="subEntryType">
                                                                <option selected value="">ALL</option>
                                                            </select>
                                                        </div>
                                                        <div class="col-sm-2">
                                                            <label>Buyer/Supplier</label>
                                                            <select class="form-control" id="buySup" name="buySup">
                                                                <option selected value="">All</option>
                                                            </select>
                                                        </div><div class="col-sm-2">
                                                            <label>Project</label>
                                                            <select class="form-control" id="proj" name="proj">
                                                                <option selected value="">All</option>
                                                            </select>
                                                        </div>
                                                    </div>

                                                    <div class="form-group row">

                                                        <div class="col-sm-4">
                                                            <!--                                                            <div class="input-group">
                                                                                                                            <div class="input-group-prepend">
                                                                                                                                <span class="input-group-text">
                                                                                                                                    From
                                                                                                                                </span>
                                                                                                                            </div>
                                                                                                                            <input class="form-control" id="from" type="date" name="from" placeholder="Search By Entry Date">
                                                                                                                        </div>-->
                                                        </div>
                                                        <div class="col-sm-4">
                                                            <!--                                                            <div class="input-group">
                                                                                                                            <div class="input-group-prepend">
                                                                                                                                <span class="input-group-text">
                                                                                                                                    To
                                                                                                                                </span>
                                                                                                                            </div>
                                                                                                                            <input class="form-control" id="to" type="date" name="to" placeholder="Search By Entry Date">
                                                                                                                        </div>-->
                                                        </div>
                                                        <div class="col-sm-2">
                                                            <!--                                                            <select class="form-control" id="subEntryType" name="subEntryType">
                                                                                                                            <option selected value="">ALL TYPES</option>
                                                                                                                        </select>-->
                                                        </div>
                                                        <div class="col-sm-2">
                                                            <button class="btn btn-primary pull-right" onclick="advanceSearch()" type="button">Apply</button>
                                                        </div>
                                                    </div>
                                                </div>
                                                <hr>

                                                <table id="viewDatatable" style="width: 100%"
                                                       class="table table-responsive-sm table-bordered table-striped table-sm">
                                                    <thead>
                                                        <tr>
                                                            <th class="id">ID</th>
                                                            <th>Entry Date</th>
                                                            <th>Item</th>
                                                            <th>Type</th>
                                                            <th>Buyer</th>
                                                            <th>Supplier</th>
                                                            <th>Project</th>
                                                            <th>Quantity</th>
                                                            <th>Rate</th>
                                                            <th>Amount</th>
                                                            <th>Advance</th>
                                                            <th style="min-width: 80px">Action</th>
                                                        </tr>
                                                    </thead>
                                                    <style>
                                                        /*                                                        thead tr th.id{
                                                                                                                    display: none;
                                                                                                                }
                                                                                                                tbody tr td div.id{
                                                                                                                    display: none;
                                                                                                                }*/
                                                    </style>
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
                                  action="<c:url value='/updateEntry'/>" enctype="multipart/form-data"> 
                                <input id="eid" name="eid" hidden="true">

                                <div class="modal-body">
                                    <div class="row">
                                        <div class="col-sm-12">
                                            <div class="form-group">
                                                <label for="ebuyer">Customer/Buyer</label>
                                                <input class="form-control" id="ebuyer" name="ebuyer" required type="text">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-sm-12">
                                            <div class="form-group">
                                                <label for="esupplier">Supplier</label>
                                                <input class="form-control" id="esupplier" name="esupplier" required type="text">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-sm-12">
                                            <div class="form-group">
                                                <label for="eproject">Project</label>
                                                <input class="form-control" id="eproject" name="eproject" required type="text">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-sm-6">
                                            <div class="form-group">
                                                <label for="equantity">Quantity</label>
                                                <input class="form-control" id="equantity" name="equantity" required type="number">
                                            </div>
                                        </div>
                                        <div class="col-sm-6">
                                            <div class="form-group">
                                                <label for="erate">Rate</label>
                                                <input class="form-control" id="erate" name="erate" required type="number">
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
                                  action="<c:url value='/deleteEntry'/>" enctype="multipart/form-data"> 
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
    <script src="<c:url value="/resources/js/directEntries.js"/>"></script>

</html>

