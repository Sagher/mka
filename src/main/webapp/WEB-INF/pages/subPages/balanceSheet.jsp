<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<jsp:directive.page session="true" />
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
        <title>Balance Sheet</title>

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
                                                <span>Balance Sheet</span>
                                                <input value="${type}" id="type" hidden="true">
                                            </div>
                                            <div class="card-body">
                                                <div>
                                                    <br>
                                                    <table id="table" class="table table-responsive-sm table-bordered table-striped table-sm">
                                                        <thead>
                                                        <th>
                                                        <center>ASSETS</center>
                                                        </th>
                                                        <th>
                                                        <center>AMOUNT</center>
                                                        </th>
                                                        <th>
                                                        <center>LIABILITIES</center>
                                                        </th>
                                                        <th>
                                                        <center>AMOUNT</center>
                                                        </th>
                                                        </thead>
                                                        <tbody>
                                                            <tr>
                                                                <th>Accounts Receivable </th>
                                                                <td>
                                                                    ${allReceivable}
                                                                </td>
                                                                <th>Accounts Payable </th>
                                                                <td>
                                                                    ${allPayable}
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <th>Stock Available </th>
                                                                <td>
                                                                    ${totalStockAmount}
                                                                </td>
                                                                <th>Net Profit </th>
                                                                <td>
                                                                    ${totalSalesProfit}
                                                                </td>

                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                </td>                                                                
                                                                <td>
                                                                </td>
                                                                <td>
                                                                </td>
                                                                <td>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <th> </th>
                                                                <td>
                                                                    ${allReceivable+totalStockAmount}
                                                                </td>
                                                                <th> </th>
                                                                <td>
                                                                    ${totalSalesProfit+allPayable}
                                                                </td>
                                                            </tr>   
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
    <script src="<c:url value="/resources/js/balanceSheet.js"/>"></script>

</html>

