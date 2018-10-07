<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<jsp:directive.page session="true" />
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
        <title>Log Entry</title>

        <c:import url="../fragments/global-css.jsp" />

        <style>
            select, input{
                background-color: #f3f3f3 !important;
            }
            .form-control {
                -webkit-border-radius: 50px;
                -moz-border-radius: 50px;
                border-radius: 50px;
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
                    <div class="animated fadeIn">
                        <div class="row">
                            <div class="col">
                                <div class="card">
                                    <div class="card-header bg-info">
                                        <i class="fa fa-file-text"></i>
                                        <span><strong>LOG ENTRY</strong> Log a Sale/Purchase/Expense</span>
                                        <div class="pull-right">
                                            <i class="fa fa-eye"></i>
                                            <a style="text-decoration: none; color:white" href="<c:url value="/reports" />">
                                                <span>View All Entries</span>
                                            </a>
                                        </div>
                                    </div>
                                    <div class="card-body">
                                        <ul class="nav nav-tabs" id="" role="tablist">
                                            <li class="nav-item">
                                                <a class="nav-link active show" id="direct-tab" data-toggle="tab" href="#directtab" role="tab" aria-controls="directtab" aria-selected="true">DIRECT</a>
                                            </li>
                                            <li class="nav-item">
                                                <a class="nav-link" id="indirect-tab" data-toggle="tab" href="#indirecttab" role="tab" aria-controls="indirecttab" aria-selected="false">IN DIRECT</a>
                                            </li>
                                        </ul>

                                        <div class="tab-content" id="">

                                            <!-- direct entry -->
                                            <div class="tab-pane fade active show" id="directtab" role="tabpanel" aria-labelledby="direct-tab">
                                                <form id="dentryForm" class="form-horizontal" action="<c:url value="/logDirectEntry"/>"
                                                      method="post" enctype="multipart/form-data">
                                                    <div class="form-group row">
                                                        <label class="col-md-3 col-form-label" for="dItemType">Item</label>
                                                        <div class="col-md-9">
                                                            <select class="form-control" id="dItemType" required="true" name="dItemType">
                                                                <option selected value="">SELECT ITEM</option>
                                                            </select>
                                                        </div>
                                                    </div>
                                                    <div class="animated fadeIn form-group row" id="subItemTypeDiv" hidden="true">
                                                        <label class="col-md-3 col-form-label" for="dItemSubType">Item Type</label>
                                                        <div class="col-md-9">
                                                            <select class="form-control" id="dItemSubType" name="dItemSubType">
                                                                <option selected value="">-- Please select --</option>
                                                            </select>
                                                        </div>
                                                    </div>
                                                    <div class="form-group row">
                                                        <label class="col-md-3 col-form-label" for="dEntryType">Entry Type</label>
                                                        <div class="col-md-9">
                                                            <select class="form-control" id="dEntryType" required="true" name="dEntryType">
                                                            </select>
                                                        </div>
                                                    </div>
                                                    <div class="form-group row">
                                                        <label class="col-md-3 col-form-label" for="dcusbuy">Customer/Buyer</label>
                                                        <div class="col-md-9">
                                                            <input class="form-control" id="dcusbuy" type="text" required="true" name="dcusbuy" placeholder="ABC Corporation">
                                                            <span class="help-block">Name of the Customer/Buyer</span>
                                                        </div>
                                                    </div>
                                                    <div class="form-group row">
                                                        <label class="col-md-3 col-form-label" for="dsupplier">Supplier</label>
                                                        <div class="col-md-9">
                                                            <input class="form-control" id="dsupplier" type="text" required="true" name="dsupplier" placeholder="ABC Corporation">
                                                            <span class="help-block">Name of the Supplier</span>
                                                        </div>
                                                    </div>
                                                    <div class="form-group row">
                                                        <label class="col-md-3 col-form-label" for="dproject">Project</label>
                                                        <div class="col-md-9">
                                                            <input class="form-control" id="dproject" type="text" name="dproject">
                                                            <span class="help-block">Name of the Project</span>
                                                        </div>
                                                    </div>
                                                    <div class="form-group row" id="quantityDiv">
                                                        <label class="col-md-3 col-form-label" for="dquantity">Quantity</label>
                                                        <div class="col-md-9">
                                                            <input class="form-control" id="dquantity" type="number" value="0" name="dquantity" placeholder="Quantity">
                                                            <span class="help-block">Quantity of the material</span>
                                                        </div>
                                                    </div>
                                                    <div class="form-group row" id="rateDiv">
                                                        <label class="col-md-3 col-form-label" for="drate">Rate</label>
                                                        <div class="col-md-9">
                                                            <input class="form-control" id="drate" type="number" value="0" name="drate" placeholder="Rate">
                                                            <span class="help-block">Per Unit Price</span>
                                                        </div>
                                                    </div>
                                                    <div class="form-group row">
                                                        <label class="col-md-3 col-form-label" for="damount">Amount</label>
                                                        <div class="col-md-9">
                                                            <input class="form-control" id="damount" type="number" name="damount" placeholder="Amount" required="true">
                                                            <span class="help-block">Amount Received/Paid</span>
                                                        </div>
                                                    </div>
                                                    <div class="form-group row">
                                                        <label class="col-md-3 col-form-label" for="dadvance">Advance</label>
                                                        <div class="col-md-9">
                                                            <input class="form-control" id="dadvance" type="number" name="dadvance" placeholder="Advance Paid" value="0" required="true">
                                                            <span class="help-block">Advance Received/Paid</span>
                                                        </div>
                                                    </div>
                                                    <div class="form-group row" style="display: none">
                                                        <label class="col-md-3 col-form-label" for="doe">Date of Entry</label>
                                                        <div class="col-md-9">
                                                            <input class="form-control" id="doe" type="date" name="doe" required="true">
                                                        </div>
                                                    </div>
                                                    <br>
                                                    <hr>
                                                    <div>
                                                        <button id="logdEntryBtn" class="btn btn-primary"
                                                                onclick="logdEntry()" type="submit">
                                                            <i class="fa fa-dot-circle-o"></i> Submit</button>
                                                        <button class="btn btn-danger" onclick="resetdForm()" type="reset">
                                                            <i class="fa fa-ban"></i> Reset</button>
                                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                                    </div>
                                                </form>
                                            </div>
                                            <!--
                                            
                                            
                                               INDIRECT ENTRIES                                                 
                                            
                                            
                                            -->
                                            <div class="tab-pane fade" id="indirecttab" role="tabpanel" aria-labelledby="indirect-tab">
                                                <form id="ientryForm" class="form-horizontal" action="<c:url value="/logInDirectEntry"/>"
                                                      method="post" enctype="multipart/form-data">
                                                    <div class="form-group row">
                                                        <label class="col-md-3 col-form-label" for="iItemType">Item</label>
                                                        <div class="col-md-9">
                                                            <select class="form-control" id="iItemType" required="true" name="iItemType">
                                                                <option selected value="">-- Please select --</option>
                                                            </select>
                                                        </div>
                                                    </div>
                                                    <div class="animated fadeIn form-group row" id="isubItemTypeDiv" hidden="true">
                                                        <label class="col-md-3 col-form-label" for="iItemSubType">Item Type</label>
                                                        <div class="col-md-9">
                                                            <select class="form-control" id="iItemSubType" name="iItemSubType">
                                                                <option value="">-- Please select --</option>
                                                            </select>
                                                        </div>
                                                    </div>
                                                    <div class="form-group row">
                                                        <label class="col-md-3 col-form-label" for="iname">Name</label>
                                                        <div class="col-md-9">
                                                            <input class="form-control" id="iname" type="text" required="true" name="iname" placeholder="ABC Corporation">
                                                            <span class="help-block">Name</span>
                                                        </div>
                                                    </div>
                                                    <div class="form-group row">
                                                        <label class="col-md-3 col-form-label" for="idesc">Description</label>
                                                        <div class="col-md-9">
                                                            <input class="form-control" id="idesc" type="text" name="idesc">
                                                            <span class="help-block">Description</span>
                                                        </div>
                                                    </div>
                                                    <div class="form-group row" id="rateDiv">
                                                        <label class="col-md-3 col-form-label" for="icost">Cost</label>
                                                        <div class="col-md-9">
                                                            <input class="form-control" id="icost" type="number" value="0" name="icost" placeholder="Cost">
                                                            <span class="help-block">Amount of Expenditure</span>
                                                        </div>
                                                    </div>
                                                    <div class="form-group row" id="rateDiv">
                                                        <label class="col-md-3 col-form-label" for="iadvance">Advance</label>
                                                        <div class="col-md-9">
                                                            <input class="form-control" id="iadvance" type="number" value="0" name="iadvance" placeholder="Cost">
                                                            <span class="help-block">Advance paid</span>
                                                        </div>
                                                    </div>
                                                    <div class="form-group row" style="display: none">
                                                        <label class="col-md-3 col-form-label" for="idoe">Date of Entry</label>
                                                        <div class="col-md-9">
                                                            <input class="form-control" id="idoe" type="date" name="idoe" required="true">
                                                        </div>
                                                    </div>
                                                    <br>
                                                    <hr>
                                                    <div>
                                                        <button id="logiEntryBtn" class="btn btn-primary"
                                                                onclick="logiEntry()" type="submit">
                                                            <i class="fa fa-dot-circle-o"></i> Submit</button>
                                                        <button class="btn btn-danger" onclick="resetiForm()" type="reset">
                                                            <i class="fa fa-ban"></i> Reset</button>
                                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                                    </div>
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
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.form/4.2.2/jquery.form.min.js"></script>
    <script src="<c:url value="/resources/js/entry.js"/>"></script>

</html>

