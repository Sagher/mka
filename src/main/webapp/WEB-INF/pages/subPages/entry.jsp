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
            select, input, textarea{
                background-color: #f3f3f3 !important;
            }
            .form-control, .btn {
                -webkit-border-radius: 50px;
                -moz-border-radius: 50px;
                border-radius: 50px;
            }
            .help-block{
                margin-left: 5px;
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
                                            <li class="nav-item">
                                                <a class="nav-link" id="asphalt-tab" data-toggle="tab" href="#asphaltsaletab" role="tab" aria-controls="asphaltsaletab" aria-selected="false">Asphalt Sale</a>
                                            </li>
                                            <li class="nav-item">
                                                <a class="nav-link" id="machinery-tab" data-toggle="tab" href="#machinerycarriagetab" role="tab" aria-controls="machinerycarriagetab" aria-selected="false">Machinery Carriage</a>
                                            </li>
                                        </ul>

                                        <div class="tab-content" id="">

                                            <!-- direct entry -->
                                            <div class="tab-pane fade active show" id="directtab" role="tabpanel" aria-labelledby="direct-tab">
                                                <form id="dentryForm" class="form-horizontal" action="<c:url value="/logDirectEntry"/>"
                                                      method="post" enctype="multipart/form-data">
                                                    <br>
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
                                                            <span class="help-block">Buyer/Supplier</span>
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
                                                        <div class="col-md-8">
                                                            <span class="help-block">Project</span>
                                                            <select class="form-control dproj" id="projSelect" name="dproj">
                                                                <option selected value="">-- Please select A Project --</option>
                                                            </select>
                                                            <br>
                                                            <input hidden="true" class="form-control proj" id="dproject" type="text" 
                                                                   name="dproject" placeholder="Custom Project">
                                                        </div>
                                                    </div>
                                                    <div class="form-group row">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <span class="help-block">Description</span>
                                                            <textarea class="form-control" id="ddescription" type="text" name="ddescription" 
                                                                      placeholder="Description If Any"></textarea>
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>
                                                    <div class="form-group row" id="rateDiv">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-4">
                                                            <span class="help-block">Quantity of the material</span>
                                                            <input class="form-control" id="dquantity" type="number" value="0" name="dquantity" 
                                                                   step=".01" min="0"  placeholder="Quantity" onblur="if (this.value == '') {
                                                                               this.value = '0';
                                                                           }"  onfocus="if (this.value == '0') {
                                                                                       this.value = '';
                                                                                   }">
                                                        </div>
                                                        <div class="col-md-4">
                                                            <span class="help-block" id="pup">Per Unit Price</span>
                                                            <input class="form-control" id="drate" type="number" value="0" name="drate"
                                                                   step=".01" min="0" placeholder="Rate"
                                                                   onblur="if (this.value == '') {
                                                                               this.value = '0';
                                                                           }"  onfocus="if (this.value == '0') {
                                                                                       this.value = '';
                                                                                   }" >
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>
                                                    <div class="form-group row">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-4">
                                                            <span class="help-block" id="dtpLabel">Total Price</span>
                                                            <input class="form-control" id="damount" type="number" name="damount"  step=".01" min="0" placeholder="Total Price" required="true">
                                                        </div>
                                                        <div class="col-md-4" id="dadvanceDiv">
                                                            <span class="help-block">Advance Received/Paid</span>
                                                            <input class="form-control" id="dadvance" type="number" name="dadvance" placeholder="Advance Paid" value="0" 
                                                                   required="true" onblur="if (this.value == '') {
                                                                               this.value = '0';
                                                                           }"  onfocus="if (this.value == '0') {
                                                                                       this.value = '';
                                                                                   }">
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>
                                                    <div class="form-group row" id="directPayFromDiv">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <span class="help-block">Pay From</span>
                                                            <div class="help-block">
                                                                <div class="form-check form-check-inline mr-1">
                                                                    <input checked="true" class="form-check-input" id="inline-radio1" type="radio" value="1" name="payfrom">
                                                                    <label class="form-check-label" for="inline-radio1">Cash In Hand</label>
                                                                </div>
                                                                <label class="col-md-1"></label>
                                                                <div class="form-check form-check-inline mr-1">
                                                                    <input class="form-check-input" id="inline-radio0" type="radio" value="0" name="payfrom">
                                                                    <label class="form-check-label" for="inline-radio0">Main Account</label>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>
                                                    <div class="form-group row">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <span class="help-block">Date of Entry</span>
                                                            <div class="help-block">
                                                                <input class="form-control" id="doe" type="date" name="doe" required="true"  />
                                                            </div>
                                                            <scrip>
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>
                                                    <div class="form-group row">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <span class="help-block">Plant Bilty Number</span>
                                                            <div class="help-block">
                                                                <input class="form-control" id="biltee" type="number" name="pBilty">
                                                            </div>
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>
                                                    <div class="form-group row">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <span class="help-block">Recipient Bilty Number</span>
                                                            <div class="help-block">
                                                                <input class="form-control" id="biltee" type="number" name="cBilty">
                                                            </div>
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>
                                                    <br>
                                                    <!--<hr>-->

                                                    <div class="form-group row">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <div class="row">
                                                                <div  class="pull-left" id="addCarriageDiv" hidden="true">
                                                                    <button class="btn btn-default" type="button"
                                                                            id="addCarriageBtn" onclick="addCrushCarriage()">
                                                                        <i class="fa fa-plus"></i> Add Carriage
                                                                    </button>
                                                                </div>
                                                            </div>
                                                            <div class="" hidden="true" id="crushCarriageForm">
                                                                <br>
                                                                <div class="row">
                                                                    <div class="col-sm-12">
                                                                        <div class="form-group">
                                                                            <label for="unloadingParty">Carriage Contractor</label>
                                                                            <select class="form-control dcParty" id="dcParty" name="unloadingParty">
                                                                                <option selected value="">-- Please select A Carriage Contractor --</option>
                                                                            </select>
                                                                            <br>
                                                                            <input hidden="true" class="form-control" id="dcPartyInput" type="text" 
                                                                                   name="dcPartyInput" placeholder="Custom Contractor">
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div class="row">
                                                                    <div class="col-sm-12">
                                                                        <div class="form-group">
                                                                            <label for="unloadedCrush">Total Unloaded</label>
                                                                            <input class="form-control" id="unloadedCrush" name="unloadedCrush" step=".01" min="0"
                                                                                   type="number" placeholder="Enter Amount of Unloaded Crush in Cubic Ft.">
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div class="row">
                                                                    <div class="col-sm-12">
                                                                        <div class="form-group">
                                                                            <label for="unloadingCost">Unloading Cost Per Cubic Ft.</label>
                                                                            <input class="form-control" id="unloadingCost" name="unloadingCost" step=".01" min="0" type="number" value="0">
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div class="row">
                                                                    <div class="col-sm-12">
                                                                        <div class="form-group">
                                                                            <label for="unloadingCost">Total Unloading Cost</label>
                                                                            <input class="form-control" id="totalUnloadingCost" name="totalUnloadingCost" 
                                                                                   step=".01" min="0" value="0" type="number">
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                            <div class="pull-right">
                                                                <br>
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
                                                    <div class="animated fadeIn form-group row">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <div class="brand-card item-summary">
                                                                <div class="text-white bg-primary">
                                                                    <div class="card-body">
                                                                        <div class="item-name">
                                                                            <strong>Head Office Account</strong></div>
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
                                                            <span class="help-block">Name</span>
                                                            <!--<input class="form-control" id="iname" type="text" required="true" name="iname">-->
                                                            <select class="form-control" id="iname" name="iname" required="true">
                                                                <option selected value="">-- Please select A Payer/Receiver --</option>
                                                            </select>
                                                            <br>
                                                            <input hidden="true" class="form-control" id="ibuysupInput" type="text" 
                                                                   name="ibuysupInput" placeholder="Custom Buyer/Supplier">
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>
                                                    <div class="form-group row">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <span class="help-block">Description</span>
                                                            <input class="form-control" id="idesc" type="text" name="idesc" required="true">
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>
                                                    <div class="form-group row">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <span class="help-block">Amount of Expenditure</span>
                                                            <input class="form-control" id="icost" type="number" value="0" name="icost" placeholder="Cost" step=".01" min="0"
                                                                   onchange="toggleIndirectFullyPayed()"  onblur="if (this.value == '') {
                                                                               this.value = '0';
                                                                           }"  onfocus="if (this.value == '0') {
                                                                                       this.value = '';
                                                                                   }">
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>
                                                    <div class="form-group row">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <div class="help-block">
                                                                <div class="form-check form-check-inline mr-1">
                                                                    <input checked="true" class="form-check-input" id="indirectFullyPayed" type="checkbox" 
                                                                           name="indirectFullyPayed" onclick="toggleIndirectFullyPayed()">
                                                                    <label class="form-check-label" for="inline-radio1">Payed</label>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>

                                                    <div class="form-group row">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <span class="help-block">Advance paid</span>
                                                            <input class="form-control" id="iadvance" type="number" value="0" name="iadvance" step=".01" min="0"
                                                                   readonly="true" onblur="if (this.value == '') {
                                                                               this.value = '0';
                                                                           }"  onfocus="if (this.value == '0') {
                                                                                       this.value = '';
                                                                                   }">
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>
                                                    <div class="form-group row">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <span class="help-block">Pay From</span>
                                                            <div class="help-block">
                                                                <div class="form-check form-check-inline mr-1">
                                                                    <input checked="true" class="form-check-input" id="inline-radio1" type="radio" value="1" name="payfrom">
                                                                    <label class="form-check-label" for="inline-radio1">Cash In Hand</label>
                                                                </div>
                                                                <label class="col-md-1"></label>
                                                                <div class="form-check form-check-inline mr-1">
                                                                    <input class="form-check-input" id="inline-radio0" type="radio" value="0" name="payfrom">
                                                                    <label class="form-check-label" for="inline-radio0">Main Account</label>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>
                                                    <div class="form-group row">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <span class="help-block">Date of Entry</span>
                                                            <div class="help-block">
                                                                <input class="form-control" id="idoe" type="date" name="idoe" required="true">
                                                            </div>
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

                                                    <div class="animated fadeIn form-group row">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <div class="brand-card item-summary">
                                                                <div class="text-white bg-primary">
                                                                    <div class="card-body">
                                                                        <div class="item-name">
                                                                            <strong>Head Office Account</strong></div>
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
                                                            <span class="help-block">Transaction Type</span>
                                                            <select class="form-control" id="ttype" name="ttype" required="true">
                                                                <option value="">-- select an option --</option>
                                                                <option value="+">
                                                                    To Head Office Account
                                                                </option>
                                                                <option value="-">
                                                                    From Head Office Account
                                                                </option>
                                                                <option value="--">
                                                                    From Cash In Hand
                                                                </option>
                                                                <option value="++">
                                                                    To Cash In Hand
                                                                </option>
                                                            </select>
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>

                                                    <div class="form-group row" id="fromHoOptions" hidden="true">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <span class="help-block">To</span>
                                                            <div class="help-block">
                                                                <div class="form-check form-check-inline mr-1">
                                                                    <input checked="true" class="form-check-input" id="inline-radio0" type="radio" 
                                                                           value="0" name="payFromHo" onclick="togglePayeeDiv(0)" >
                                                                    <label class="form-check-label" for="inline-radio0">Other</label>
                                                                </div>
                                                                <label class="col-md-1"></label>
                                                                <div class="form-check form-check-inline mr-1">
                                                                    <input class="form-check-input" id="inline-radio1" type="radio" 
                                                                           onclick="togglePayeeDiv(1)" value="1" name="payFromHo">
                                                                    <label class="form-check-label" for="inline-radio1">Cash In Hand</label>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>

                                                    <div class="form-group row" id="toHoOptions" hidden="true">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <span class="help-block">From</span>
                                                            <div class="help-block">
                                                                <div class="form-check form-check-inline mr-1">
                                                                    <input checked="true" class="form-check-input" id="inline-radio2" type="radio" value="0" name="payToHo"  onclick="togglePayeeDiv2(0)">
                                                                    <label class="form-check-label" for="inline-radio0">Customer</label>
                                                                </div>
                                                                <label class="col-md-1"></label>
                                                                <div class="form-check form-check-inline mr-1">
                                                                    <input class="form-check-input" onclick="togglePayeeDiv2(1)" id="inline-radio3" type="radio" value="1" name="payToHo">
                                                                    <label class="form-check-label" for="inline-radio1">Cash In Hand</label>
                                                                </div>

                                                            </div>
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>

                                                    <div class="form-group row">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <span class="help-block">Amount</span>
                                                            <input class="form-control" name="tamount" type="number" required="true" step=".01" min="0" 
                                                                   onblur="if (this.value == '') {
                                                                               this.value = '0';
                                                                           }"  onfocus="if (this.value == '0') {
                                                                                       this.value = '';
                                                                                   }">
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>

                                                    <div class="form-group row">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8" id="cashTranPayeeDiv">
                                                            <span class="help-block" id="tPayeeLabel">Payee</span>
                                                            <select class="form-control" id="tbuysupSelect" name="tpayer" required="true">
                                                                <option selected value="">-- Please select An Option --</option>
                                                            </select>
                                                            <br>
                                                            <input hidden="true" class="form-control" id="tbuysupInput" type="text" 
                                                                   name="tbuysupInput" placeholder="Custom Buyer/Supplier">
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>
                                                    <!--
                                    <div class="form-group row">
                                        <label class="col-md-2"></label>
                                        <div class="col-md-8">
                                            <span class="help-block">Project</span>
                                            <select class="form-control tproj" id="tprojSelect" name="tproj" required="true">
                                                <option selected value="">-- Please select A Project --</option>
                                            </select>
                                            <br>
                                            <input hidden="true" class="form-control proj" id="tproject" type="text" 
                                                   name="tproject" placeholder="Custom Project">
                                        </div>
                                    </div>-->
                                                    <div class="form-group row">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <span class="help-block">Description</span>
                                                            <input class="form-control" type="text" name="tdesc">
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>

                                                    <div class="form-group row">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <span class="help-block">Date of Entry</span>
                                                            <div class="help-block">
                                                                <input class="form-control" id="cdoe" type="date" name="cdoe" required="true">
                                                            </div>
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>
                                                    <div class="form-group row" id="salaryDiv" hidden="true">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <div class="help-block">
                                                                <div class="form-check form-check-inline mr-1">
                                                                    <input class="form-check-input" id="isSalary" type="checkbox" name="isSalary">
                                                                    <label class="form-check-label" for="inline-radio1">Salary</label>
                                                                </div>
                                                            </div>
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

                                            <!--
                                
                                
                                                ASPHALT SALE                                               
                                
                                
                                            -->
                                            <div class="tab-pane fade" id="asphaltsaletab" role="tabpanel" aria-labelledby="asphalt-tab">
                                                <form id="asphaltSaleForm" class="form-horizontal" 
                                                      action="<c:url value="/logAsphaltSale"/>"  autocomplete="on"
                                                      method="post" enctype="multipart/form-data">
                                                    <br>
                                                    <div class="form-group row">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <span class="help-block">Customer</span>
                                                            <select class="form-control" id="asCusSelect" name="asCus" required="true" onchange="getAutoFillValues()">
                                                                <option selected value="">-- Please select A Customer --</option>
                                                            </select>
                                                            <br>
                                                            <input hidden="true" class="form-control" id="asCusInput" type="text" 
                                                                   name="asCusInput" placeholder="Custom Buyer">
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>
                                                    <div class="form-group row">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <span class="help-block">Project</span>
                                                            <select class="form-control" id="asProjSelect" name="asProj" required="true" onchange="getAutoFillValues()">
                                                                <option selected value="">-- Please select A Project --</option>
                                                            </select>
                                                            <br>
                                                            <input hidden="true" class="form-control" id="asProjInput" type="text" 
                                                                   name="asProjInput" placeholder="Custom Project">
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>
                                                    <div class="form-group row">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <span class="help-block">Total Asphalt in TONS</span>
                                                            <input class="form-control" name="tass" id="tass" type="number" 
                                                                   step=".01" min="0" required="true" onblur="if (this.value == '') {
                                                                               this.value = '0';
                                                                           }" onfocus="if (this.value == '0') {
                                                                                       this.value = '';
                                                                                   }" onchange="triggerAllChanges()">
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>

                                                    <div class="form-group row">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <span class="help-block">Bilty Number</span>
                                                            <input class="form-control" id="biltee" type="number" required="true" name="biltee">
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>

                                                    <div class="form-group row">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <span class="help-block">Vehicle Number</span>
                                                            <input class="form-control" id="vehicle" type="text" name="vehicle">
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>

                                                    <div class="form-group row">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <span class="help-block">Type</span>
                                                            <select name="asstype" class="form-control">
                                                                <option value="awc" selected="selected">AWC</option>
                                                                <option value="abc">ABC</option>
                                                            </select>
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>

                                                    <div class="form-group row">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <span class="help-block">Date of Entry</span>
                                                            <div class="help-block">
                                                                <input class="form-control" id="asdoe" type="date" name="asdoe" required="true">
                                                            </div>
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>

                                                    <div class="form-group row">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <span class="help-block">Ex-Plant Rate / Ton</span>
                                                            <input class="form-control" id="expRate" type="number" name="expRate">
                                                            <span class="help-block">Ex-Plant Total Cost</span>
                                                            <input class="form-control" id="expCost" type="number" name="expCost" tabindex="-1" readonly="true" value="0">
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>

                                                    <div class="form-group row" id="assCarriageCost">
                                                        <br>
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <h5>Carriage</h5>
                                                            <span class="help-block">Contractor</span>
                                                            <select class="form-control" id="assCarProvider" name="assCarProvider">
                                                                <option selected value="">-- Please select A Carriage Contractor --</option>
                                                            </select>
                                                            <br>
                                                            <input hidden="true" class="form-control" id="assCarProviderInput" type="text" 
                                                                   name="assCarProviderInput" placeholder="Custom Contractor">
                                                            <span class="help-block">Per Ton Carriage</span>
                                                            <input class="form-control" id="assCarCostPerTon" name="assCarCostPerTon" type="number" value="0">
                                                            <span class="help-block">Total Cost</span>
                                                            <input class="form-control" id="totalAssCarCost" name="totalAssCarCost" value="0" type="number" readonly="true" tabindex="-1">
                                                            <span class="help-block">Advance</span>
                                                            <input class="form-control" id="assCarAdvance" name="assCarAdvance" type="number" value="0">
                                                        </div>    
                                                        <label class="col-md-2"></label>
                                                    </div>

                                                    <div class="form-group row" id="assLayingCost">
                                                        <br>
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <h5>Laying Charges</h5>
                                                            <span class="help-block">Contractor</span>
                                                            <select class="form-control" id="assLayer" name="assLayer">
                                                                <option selected value="">-- Please select A Laying Contractor --</option>
                                                            </select>
                                                            <br>
                                                            <input hidden="true" class="form-control" id="assLayerInput" type="text" 
                                                                   name="assLayerInput" placeholder="Custom Contractor">
                                                            <span class="help-block">Per Ton Cost</span>
                                                            <input class="form-control" id="assLayingCostPerTon" name="assLayingCostPerTon" type="number" value="0">
                                                            <span class="help-block">Total Cost</span>
                                                            <input class="form-control" id="totalAssLayingCost" name="totalAssLayingCost" value="0" type="number" readonly="true" tabindex="-1">
                                                            <span class="help-block">Advance</span>
                                                            <input class="form-control" id="assLayingAdvance" name="assLayingAdvance" type="number" value="0">
                                                        </div>    
                                                        <label class="col-md-2"></label>
                                                    </div>

                                                    <c:forEach items="${stockTrace}" var="item">
                                                        <c:if test = "${item.itemId == 1}">
                                                            <div class="form-group row">
                                                                <label class="col-md-2"></label>
                                                                <div class="col-md-8">
                                                                    <h6>${item.itemName} ${item.subType}</h6>

                                                                    <span class="help-block">Kg / Ton</span>
                                                                    <input class="form-control" id="${item.id}rate" type="number" 
                                                                           name="${item.id}rate" value="0" 
                                                                           onchange="updateAssBitRate('${item.id}~rate', this.value, ${item.averageUnitPrice})">
                                                                    <input hidden="true" id="bitumenRateDynamicId" value="${item.id}rate">
                                                                    <br>                                                                
                                                                    <span class="help-block">Total ${item.itemName} ${item.subType} Quantity in ${item.itemUnit}</span>
                                                                    <input tabindex="-1" class="form-control" id="${item.id}quantity" type="number" readonly="readonly"
                                                                           name="${item.id}quantity" value="0" >
                                                                    <br>                                                                
                                                                    <span class="help-block">Total ${item.itemName} ${item.subType} Cost (Total Quantity x Avg Rate i.e, ${item.averageUnitPrice})</span>
                                                                    <input tabindex="-1" class="form-control" id="${item.id}cost" type="number" readonly="readonly"
                                                                           name="${item.id}cost" value="0">
                                                                </div>
                                                                <label class="col-md-2"></label>
                                                            </div>
                                                        </c:if>
                                                        <c:if test = "${item.itemId == 6}">
                                                            <div class="form-group row">
                                                                <label class="col-md-2"></label>
                                                                <div class="col-md-8">
                                                                    <h6>${item.itemName} ${item.subType}</h6>

                                                                    <span class="help-block">${item.itemUnit} / Ton</span>
                                                                    <input class="form-control" id="${item.id}rate" type="number" 
                                                                           name="${item.id}rate" value="0" 
                                                                           onchange="updateAssRate('${item.id}~rate', this.value, ${item.averageUnitPrice})">
                                                                    <input hidden="true" id="${item.itemName}${item.subType}RateDynamicId" value="${item.id}rate">
                                                                    <br>                                                                
                                                                    <span class="help-block">Total ${item.itemName} ${item.subType} Quantity in ${item.itemUnit}</span>
                                                                    <input tabindex="-1" class="form-control" id="${item.id}quantity" type="number" readonly="readonly"
                                                                           name="${item.id}quantity" value="0" >
                                                                    <br>                                                                
                                                                    <span class="help-block">Total ${item.itemName} ${item.subType} Cost (Total Quantity x Avg Rate i.e, ${item.averageUnitPrice})</span>
                                                                    <input tabindex="-1" class="form-control" id="${item.id}cost" type="number" readonly="readonly"
                                                                           name="${item.id}cost" value="0">
                                                                </div>
                                                                <label class="col-md-2"></label>
                                                            </div>
                                                        </c:if>
                                                    </c:forEach>
                                                    <br>           
                                                    <br>
                                                    <br>

                                                    <div class="form-group row" style="display : none;">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <span class="help-block">Total Sale Cost</span>
                                                            <input class="form-control" id="assSaleCost" type="number"  readonly="readonly"
                                                                   name="assSaleCost" value="0">
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>
                                                    <div class="form-group row" style="display : none;">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <span class="help-block">Per Ton Sale Rate</span>
                                                            <input class="form-control" id="assSaleRate" type="number"  readonly="readonly"
                                                                   name="assSaleRate" value="0">
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>


                                                    <br>

                                                    <div class="form-group row">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <div class="pull-right">
                                                                <button class="btn btn-lg btn-danger" type="reset">
                                                                    <i class="fa fa-ban"></i> Reset</button>
                                                                <button id="assSaleBtn" class="btn btn-lg btn-primary"
                                                                        onclick="logAssSale()" type="submit">
                                                                    <i class="fa fa-floppy-o"></i> Log Asphalt Sale</button>

                                                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                                            </div>
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>
                                                </form>
                                            </div> 


                                            <!--
                                            
                                            MACHINERY CARRIAGE
                                            
                                            -->

                                            <div class="tab-pane fade" id="machinerycarriagetab" role="tabpanel" aria-labelledby="machinery-tab">
                                                <form id="macCarForm" class="form-horizontal" 
                                                      action="<c:url value="/logMachinery"/>"  autocomplete="on"
                                                      method="post" enctype="multipart/form-data">
                                                    <br>
                                                    <div class="form-group row">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <span class="help-block">Carriage Contractor</span>
                                                            <select class="form-control" id="macCarConSelect" name="macCarCon" required="true" >
                                                                <option selected value="">-- Please select A Customer --</option>
                                                            </select>
                                                            <br>
                                                            <input hidden="true" class="form-control" id="macCarConInput" type="text" 
                                                                   name="macCarConInput" placeholder="Custom Contractor">
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>
                                                    <div class="form-group row">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <span class="help-block">From</span>
                                                            <input required="true" class="form-control" id="macCarFrom" type="text" 
                                                                   name="macCarFrom" placeholder="From">
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>

                                                    <div class="form-group row">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <span class="help-block">To</span>
                                                            <input required="true" class="form-control" id="macCarTo" type="text" 
                                                                   name="macCarTo" placeholder="To">
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>

                                                    <div class="form-group row">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <span class="help-block">Amount</span>
                                                            <input required="true" class="form-control" id="macCarAmount" type="number" 
                                                                   name="macCarAmount" placeholder="Amount">
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>

                                                    <div class="form-group row">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <span class="help-block">On Account Of</span>
                                                            <select class="form-control" id="macCarCon2Select" name="macCarCon2" required="true" >
                                                                <option selected value="">-- Please select A Customer --</option>
                                                            </select>
                                                            <br>
                                                            <input hidden="true" class="form-control" id="macCarCon2Input" type="text" 
                                                                   name="macCarCon2Input" placeholder="Custom Contractor">
                                                        </div>
                                                        <label class="col-md-2"></label>
                                                    </div>

                                                    <br>

                                                    <div class="form-group row">
                                                        <label class="col-md-2"></label>
                                                        <div class="col-md-8">
                                                            <div class="pull-right">
                                                                <button class="btn btn-lg btn-danger" type="reset">
                                                                    <i class="fa fa-ban"></i> Reset</button>
                                                                <button id="macCarBtn" class="btn btn-lg btn-primary"
                                                                        onclick="logMacCar()" type="submit">
                                                                    <i class="fa fa-floppy-o"></i> Log Entry</button>

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

