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
            .form-control, .btn {
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
                                        <span><strong>LOG ENTRY</strong> 
                                            <!--Log a Sale/Purchase/Expense-->
                                        </span>
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
                                            <li class="nav-item">
                                                <a class="nav-link" id="cashtran-tab" data-toggle="tab" href="#cashtrantab" role="tab" aria-controls="cashtrantab" aria-selected="false">Cash Transaction</a>
                                            </li>
                                        </ul>

                                        <div class="tab-content" id="">

                                            <!-- direct entry -->
                                            <div class="tab-pane fade active show" id="directtab" role="tabpanel" aria-labelledby="direct-tab">
                                                <form id="dentryForm" class="form-horizontal" action="<c:url value="/logDirectEntry"/>"
                                                      method="post" enctype="multipart/form-data">
                                                    <div class="form-group row">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <select class="form-control" id="dItemType" required="true" name="dItemType">
                                                                <option selected value="">SELECT ITEM</option>
                                                            </select>
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>

                                                    <div class="animated fadeIn form-group row" id="subItemTypeDiv" hidden="true">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <select class="form-control" id="dItemSubType" name="dItemSubType">
                                                                <option selected value="">-- Please select --</option>
                                                            </select>
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                        <input style="display: none" name="unloadedCrush" class="unloadedCrush">
                                                        <input style="display: none" name="unloadingCost" class="unloadingCost">
                                                    </div>

                                                    <div id="stockDetailDiv" class="animated fadeIn form-group row" hidden="true">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <div class="brand-card item-summary" id="stockDetail">
                                                            </div>
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>

                                                    <div class="form-group row" id="entryTypeDiv" hidden="true">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <select class="form-control" id="dEntryType" required="true" name="dEntryType">
                                                                <option selected value="">ENTRY TYPE</option>
                                                            </select>
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>
                                                    <div class="form-group row" id="supBuyDiv" hidden="true">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8" id="buysupDiv">
                                                            <select class="form-control buysup" id="dbuysupSelect" name="dbuysup" required="true">
                                                                <option selected value="">-- Please select A Buyer/Supplier --</option>
                                                            </select>
                                                            <br>
                                                            <input hidden="true" class="form-control buysup" id="dbuysupInput" type="text" 
                                                                   name="dbuysupInput" placeholder="Custom Buyer/Supplier">
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>
                                                    <div class="form-group row">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-4">
                                                            <input class="form-control" id="dproject" type="text" name="dproject" placeholder="Name of Project">
                                                            <span class="help-block">Project</span>
                                                        </div>
                                                        <div class="col-md-4">
                                                            <input class="form-control" id="ddescription" type="text" name="ddescription" 
                                                                   placeholder="Description If Any">
                                                            <span class="help-block">Description</span>
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>
                                                    <div class="form-group row" id="rateDiv">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-4">
                                                            <input class="form-control" id="dquantity" type="number" value="0" name="dquantity" placeholder="Quantity">
                                                            <span class="help-block">Quantity of the material</span>
                                                        </div>
                                                        <div class="col-md-4">
                                                            <input class="form-control" id="drate" type="number" value="0" name="drate" placeholder="Rate">
                                                            <span class="help-block">Per Unit Price</span>
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>
                                                    <div class="form-group row">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-4">
                                                            <input class="form-control" id="damount" type="number" name="damount" placeholder="Total Price" required="true">
                                                            <span class="help-block">Total Price</span>
                                                        </div>
                                                        <div class="col-md-4">
                                                            <input class="form-control" id="dadvance" type="number" name="dadvance" placeholder="Advance Paid" value="0" required="true">
                                                            <span class="help-block">Advance Received/Paid</span>
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>
                                                    <div class="form-group row" style="display: none">
                                                        <label class="col-md-3 col-form-label" for="doe">Date of Entry</label>
                                                        <div class="col-md-9">
                                                            <input class="form-control" id="doe" type="date" name="doe" required="true">
                                                        </div>
                                                    </div>
                                                    <br>
                                                    <!--<hr>-->

                                                    <div class="form-group row">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <div  class="pull-left">
                                                                <button class="btn btn-default" disabled="true" type="button"
                                                                        id="addCarriageBtn" onclick="addCrushCarriage()">
                                                                    <i class="fa fa-plus"></i> Add Carriage
                                                                </button>
                                                            </div>
                                                            <div class="pull-right">
                                                                <button class="btn btn-lg btn-danger" onclick="resetdForm()" type="reset">
                                                                    <i class="fa fa-ban"></i> Reset</button>
                                                                <button id="logdEntryBtn" class="btn btn-lg btn-primary"
                                                                        onclick="logdEntry()" type="submit">
                                                                    <i class="fa fa-floppy-o"></i> Save Entry</button>
                                                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                                            </div>
                                                        </div>
                                                        <label class="col-md-2"></label>
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
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8" id="iItemTypeDiv">
                                                            <select class="form-control" id="iItemType" name="iItemType">
                                                                <option selected value="">Select Item</option>
                                                            </select>
                                                            <br>
                                                            <input hidden="true" class="form-control animated fadeIn" id="iItemTypeInput" type="text" 
                                                                   name="iItemTypeInput" placeholder="Custom Indirect Entry Type">
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>
                                                    <div class="animated fadeIn form-group row" id="isubItemTypeDiv" hidden="true">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <select class="form-control" id="iItemSubType" name="iItemSubType">
                                                                <option value="">-- Please select --</option>
                                                            </select>
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>
                                                    <div class="form-group row">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <input class="form-control" id="iname" type="text" required="true" name="iname" placeholder="ABC Corporation">
                                                            <span class="help-block">Name</span>
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>
                                                    <div class="form-group row">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <input class="form-control" id="idesc" type="text" name="idesc">
                                                            <span class="help-block">Description</span>
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>
                                                    <div class="form-group row" id="rateDiv">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <input class="form-control" id="icost" type="number" value="0" name="icost" placeholder="Cost">
                                                            <span class="help-block">Amount of Expenditure</span>
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>
                                                    <div class="form-group row" id="rateDiv">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <input class="form-control" id="iadvance" type="number" value="0" name="iadvance" placeholder="Cost">
                                                            <span class="help-block">Advance paid</span>
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>
                                                    <div class="form-group row" style="display: none">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <input class="form-control" id="idoe" type="date" name="idoe" required="true">
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>
                                                    <br>
                                                    <!--<hr>-->
                                                    <div class="form-group row">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <div class="pull-right">
                                                                <button class="btn btn-lg btn-danger" onclick="resetiForm()" type="reset">
                                                                    <i class="fa fa-ban"></i> Reset</button>
                                                                <button id="logiEntryBtn" class="btn btn-lg btn-primary"
                                                                        onclick="logiEntry()" type="submit">
                                                                    <i class="fa fa-floppy-o"></i> Save Entry</button>

                                                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                                            </div>
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>
                                                </form>
                                            </div>

                                            <!--
                                                
                                                
                                                   CASH TRANSACTION                                                 
                                                
                                                
                                            -->
                                            <div class="tab-pane fade" id="cashtrantab" role="tabpanel" aria-labelledby="cashtran-tab">
                                                <form id="cashTranForm" class="form-horizontal" action="<c:url value="/logCashTransaction"/>"
                                                      method="post" enctype="multipart/form-data">

                                                    <div id="masterAccountDetailDiv" class="animated fadeIn form-group row">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <div class="brand-card item-summary">
                                                                <div class="brand-card-header text-white bg-primary">
                                                                    <div class="card-body">
                                                                        <div class="item-name">
                                                                            <strong>HeadQuarter Account</strong></div>
                                                                        <div>Total Cash: ${masterAccount.totalCash} PKR</div>
                                                                        <div>Cash In Hand: ${masterAccount.cashInHand} PKR</div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>

                                                    <div class="form-group row">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <select class="form-control" id="ttype" name="ttype">
                                                                <option value="+">
                                                                    To Headquarter Account
                                                                </option>
                                                                <option value="-">
                                                                    From Headquarter Account
                                                                </option>
                                                                <option value="-+">
                                                                    Cash In Hand
                                                                </option>
                                                            </select>
                                                            <span class="help-block">Transaction Type</span>
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>
                                                    <div class="form-group row">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <input class="form-control" name="tamount" type="number" required="true">
                                                            <span class="help-block">Amount</span>
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>
                                                    <div class="form-group row">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-4">
                                                            <input class="form-control" name="tpayer" type="text"  required="true">
                                                            <span class="help-block">Payer</span>
                                                        </div>
                                                        <div class="col-md-4">
                                                            <input class="form-control" name="tproject" type="text">
                                                            <span class="help-block">Project</span>
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>
                                                    <div class="form-group row">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <input class="form-control" type="text" name="tdesc">
                                                            <span class="help-block">Description</span>
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>

                                                    <div class="form-group row">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <div class="pull-right">
                                                                <button class="btn btn-lg btn-danger" onclick="resetCtForm()" type="reset">
                                                                    <i class="fa fa-ban"></i> Reset</button>
                                                                <button id="cashTranBtn" class="btn btn-lg btn-primary"
                                                                        onclick="logCashTransaction()" type="submit">
                                                                    <i class="fa fa-floppy-o"></i> Log Transaction</button>

                                                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                                            </div>
                                                        </div>
                                                        <label class="col-md-2"></label>
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

                <div class="modal fade" id="crushCarriageModal" tabindex="-1" role="dialog" 
                     aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h4 class="modal-title">Add Crush Carriage</h4>
                                <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">×</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <div class="row">
                                    <div class="col-sm-12">
                                        <div class="form-group">
                                            <label for="username">Total Unloaded</label>
                                            <input class="form-control" id="unloadedCrush" name="unloadedCrush" required="true" type="number" placeholder="Enter Amount of Unloaded Crush in Cubic Ft.">
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-sm-12">
                                        <div class="form-group">
                                            <label for="password">Unloading/Carriage Cost</label>
                                            <input class="form-control" id="unloadingCost" name="unloadingCost" required="true" type="number" placeholder="Unloading/Carriage Cost">
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button class="btn btn-secondary" type="button" data-dismiss="modal">Close</button>
                                <button id="createUserBtn" class="btn btn-primary" 
                                        type="submit" onclick="addCarriage()">Add Carriage</button>
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

