<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<jsp:directive.page session="true" />
<%@ page buffer="64kb" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
        <title>${accountName} Transactions</title>

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
                                                <center>
                                                    <span>
                                                        <h4>
                                                            MKA ASPHALT PLANT MARGALLA DISTRICT RAWALPINDI
                                                        </h4>
                                                        <h6>
                                                            ${accountName}
                                                        </h6>
                                                        <!--FROM ${from} TO ${to}-->
                                                    </span>
                                                </center>
                                            </div>
                                            <div class="card-body">
                                                <div class="form-group row">

                                                    <div class="col-sm-3">
                                                        <div class="input-group">
                                                            <div class="input-group-prepend">
                                                                <span class="input-group-text">
                                                                    From
                                                                </span>
                                                            </div>
                                                            <input class="form-control" value="${from}" id="from" type="date">
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-3">
                                                        <div class="input-group">
                                                            <div class="input-group-prepend">
                                                                <span class="input-group-text">
                                                                    To
                                                                </span>
                                                            </div>
                                                            <input class="form-control" value="${to}" id="to" type="date">
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-3">
                                                        <div class="input-group">
                                                            <div class="input-group-prepend">
                                                                <span class="input-group-text">
                                                                    Account
                                                                </span>
                                                            </div>
                                                            <select id="accountName" class="form-control">
                                                                <option selected="true" value="${accountName}">${accountName}</option>
                                                                <c:forEach items="${customerBuyers}" var="customerBuyer">
                                                                    <option value="${customerBuyer.name}">${customerBuyer.name}</option>
                                                                </c:forEach>
                                                            </select>
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-3">
                                                        <div class="input-group">
                                                            <div class="input-group-prepend">
                                                            </div>
                                                            <button id="applyDateFilter" class="btn btn-default">Apply</button>
                                                        </div>
                                                    </div>
                                                </div>
                                                <hr>
                                                <table id="table" class="table table-responsive table-striped table-bordered">
                                                    <thead>
                                                        <tr>
                                                            <th>SR. NO.</th>
                                                            <th>DATE</th>
                                                            <th>SITE NAME</th>
                                                            <th>DESCRIPTION</th>
                                                            <th>TYPE/SIZE</th>
                                                            <th>PLANT BILTY NO.</th>
                                                            <th>RECIPIENT BILTY NO.</th>
                                                            <th>VEHICLE NO.</th>
                                                            <th>QUANTITY</th>
                                                            <th>RATE</th>
                                                            <th>Dr.</th>
                                                            <th>Cr.</th>
                                                            <th>AMOUNT</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:set var="totalDr" value="0" scope="page" />
                                                        <c:set var="totalCr" value="0" scope="page" />
                                                        <c:set var="totalAm" value="0" scope="page" />
                                                        <c:set var="srNum" value="1" scope="page" />



                                                        <c:forEach items="${accountNameTransactions}" var="item">
                                                            <tr>
                                                                <td>${srNum}
                                                                    <c:set var="srNum" value="${srNum+1}" />
                                                                </td>
                                                                <td>
                                                                    <fmt:formatDate value="${item.timestamp}" pattern="dd-MM-yyyy" />
                                                                </td>
                                                                <td>${item.project}</td>

                                                                <c:if test = "${item.type == 'RECEIVABLE'}">
                                                                    <td>${item.itemType.itemName} SALE</td>
                                                                </c:if>
                                                                <c:if test = "${item.type == 'PAYABLE'}">
                                                                    <td>${item.itemType.itemName} PURCHASE</td>
                                                                </c:if>

<!--<td>${item.description}</td>-->
                                                                <td>${item.subType}</td>
                                                                <td>${item.plantBilty}</td>
                                                                <td>${item.recipientBilty}</td>
                                                                <td>${item.vehicleNo}</td>
                                                                <td>${item.quantity}</td>
                                                                <td>${item.rate}</td>
                                                                <c:if test = "${item.type == 'RECEIVABLE'}">
                                                                    <td></td>
                                                                    <td>${item.amount}</td>
                                                                    <c:set var="totalCr" value="${totalCr+item.amount}" />
                                                                </c:if>
                                                                <c:if test = "${item.type == 'PAYABLE'}">
                                                                    <td>${item.amount}</td>
                                                                    <td></td>
                                                                    <c:set var="totalDr" value="${totalDr+item.amount}" />
                                                                </c:if>

                                                                <td>
                                                                    <!--${item.totalAmount}-->
                                                                </td>

                                                            </tr>
                                                        </c:forEach>
                                                        <tr>
                                                            <td>${srNum}
                                                                <c:set var="srNum" value="${srNum+1}" />
                                                            </td>
                                                            <td></td>
                                                            <td>NET PROFIT</td>
                                                            <td></td>
                                                            <td></td>
                                                            <td></td>
                                                            <td></td>
                                                            <td></td>
                                                            <td></td>
                                                            <td></td>
                                                            <td>${totalDr}</td>
                                                            <td>${totalCr}</td>    
                                                            <td>${totalCr-totalDr}</td>
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


                <!-- editUserModal -->
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

    <script>
        $("#applyDateFilter").click(function () {
            var from = $("#from").val();
            var to = $("#to").val();
            var accountName = $("#accountName").val();
            if (from.length > 0 || to.length > 0 && accountName.length > 0) {
                console.log(from + " -> " + to);
                window.location = ctx + "/report?type=accountDetails&from=" + from + "&to=" + to + "&accountName=" + accountName.replace("&", "-");
            } else {

            }
        });
    </script>

    <script src="https://cdn.datatables.net/1.10.19/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/buttons/1.5.6/js/dataTables.buttons.min.js"></script>
    <script src="https://cdn.datatables.net/buttons/1.5.6/js/buttons.flash.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jszip/3.1.3/jszip.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.53/pdfmake.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.53/vfs_fonts.js"></script>
    <script src="https://cdn.datatables.net/buttons/1.5.6/js/buttons.html5.min.js"></script>
    <script src="https://cdn.datatables.net/buttons/1.5.6/js/buttons.print.min.js"></script>

    <script>

        $(document).ready(function () {
            var table = $('#table').DataTable({
                "dom": 'T<"clear">Blfrtip',
                buttons: [
//                    'copy', 'csv', 'excel'
                    {extend: 'csv', className: 'btn btn-default'},
                    {extend: 'excel', className: 'btn btn-default'},
                ],
                "searching": false,
                "ordering": false,
                "info": false,
                "stateSave": false,
                "responsive": false,
                "pageLength": -1,
                "bPaginate": false,
                "bLengthChange": false,
            });
            $('.dt-buttons').after('<div id="space"><br></div>');
        });
    </script>
</html>

