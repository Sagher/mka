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

        <!-- Data tables css -->
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
                    <div class="animated fadeIn">
                        <div class="row">
                            <div class="col-md-12">
                                <div class="card">

                                    <div class="card-header">
                                        <i class="fa fa-align-justify"></i>
                                        <span><strong>Log Entry</strong> Log a Sale/Purchase/Expense</span>
                                        <a class="btn btn-sm btn-primary pull-right" 
                                           type="button" href="<c:url value="/entries" />">
                                            View All Entries
                                        </a>
                                    </div>

                                    <form id="entryForm" class="form-horizontal" action="<c:url value="/logEntry"/>"
                                          method="post" enctype="multipart/form-data">
                                        <div class="card-body">
                                            <div class="form-group row">
                                                <label class="col-md-3 col-form-label" for="itemType">Item</label>
                                                <div class="col-md-9">
                                                    <select class="form-control" id="itemType" required="true" name="itemType">
                                                        <option selected value="">-- Please select --</option>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="animated fadeIn form-group row" id="subItemTypeDiv" hidden="true">
                                                <label class="col-md-3 col-form-label" for="subItemType">Item Type</label>
                                                <div class="col-md-9">
                                                    <select class="form-control" id="subItemType" name="subItemType">
                                                        <option selected value="">-- Please select --</option>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="form-group row">
                                                <label class="col-md-3 col-form-label" for="select1">Entry Type</label>
                                                <div class="col-md-9">
                                                    <select class="form-control" id="entryType" disabled="true" required="true" name="entryType">
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="form-group row">
                                                <label class="col-md-3 col-form-label" for="supplier">Supplier</label>
                                                <div class="col-md-9">
                                                    <input class="form-control" id="supplier" type="text" required="true" name="supplier" placeholder="ABC Corporation">
                                                    <span class="help-block">Name of the Seller/ Buyer</span>
                                                </div>
                                            </div>
                                            <div class="form-group row" id="quantityDiv">
                                                <label class="col-md-3 col-form-label" for="quantity">Quantity</label>
                                                <div class="col-md-9">
                                                    <input class="form-control" id="quantity" type="number" value="0" name="quantity" placeholder="Quantity">
                                                    <span class="help-block">Quantity of the material</span>
                                                </div>
                                            </div>
                                            <div class="form-group row" id="rateDiv">
                                                <label class="col-md-3 col-form-label" for="rate">Rate</label>
                                                <div class="col-md-9">
                                                    <input class="form-control" id="rate" type="number" value="0" name="rate" placeholder="Rate">
                                                    <span class="help-block">Rate of Sale/Purchase</span>
                                                </div>
                                            </div>
                                            <div class="form-group row">
                                                <label class="col-md-3 col-form-label" for="amount">Amount</label>
                                                <div class="col-md-9">
                                                    <input class="form-control" id="amount" type="text" name="amount" placeholder="Amount" required="true">
                                                    <span class="help-block">Amount Received/Paid</span>
                                                </div>
                                            </div>
                                            <div class="form-group row">
                                                <label class="col-md-3 col-form-label" for="doe">Date of Entry</label>
                                                <div class="col-md-9">
                                                    <input class="form-control" id="doe" type="date" name="doe" required="true">
                                                </div>
                                            </div>
                                        </div>
                                        <div class="card-footer">
                                            <!--<div class="pull-right">-->
                                            <button id="logEntryBtn" class="btn btn-primary"
                                                    onclick="logEntry()" type="submit">
                                                <i class="fa fa-dot-circle-o"></i> Submit</button>
                                            <button class="btn btn-danger" onclick="resetForm()" type="reset">
                                                <i class="fa fa-ban"></i> Reset</button>
                                            <!--</div>-->
                                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                        </div>
                                    </form>

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

