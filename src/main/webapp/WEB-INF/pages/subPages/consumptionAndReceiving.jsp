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
        <title>Consumption & Receiving</title>

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
                                                            ${type.itemName} Consumption & Receiving
                                                        </h6>
                                                        FROM ${from} TO ${to}
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
                                                                    Material
                                                                </span>
                                                            </div>
                                                            <select id="accountName" class="form-control">
                                                                <option value="0"></option>
                                                                <option value="1">BITUMEN</option>
                                                                <option value="2">LDO</option>
                                                                <option value="3">DIESEL</option>
                                                                <option value="4">TACK COAT</option>
                                                                <option value="5">PRIME COAT</option>
                                                                <option value="6">CRUSH</option>

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

                                                <table class="table table-striped table-bordered">
                                                    <thead>
                                                        <tr>
                                                            <th>DATE</th>
                                                            <th>TRANSACTION TYPE</th>
                                                            <th>CUSTOMER</th>
                                                            <th>SITE NAME</th>
                                                            <th>ASPHALT TYPE</th>
                                                            <th>ASPHALT (TON)</th>
                                                            <th>VEHICLE NO.</th>
                                                            <th>PLANT BILTY NO.</th>
                                                            <th>RECIPIENT BILTY NO.</th>
                                                            <th>TOTAL QUANTITY</th>
                                                            <th>RATE</th>
                                                            <th>AMOUNT</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:set var="q" value="0" scope="page" />
                                                        <c:set var="r" value="0" scope="page" />
                                                        <c:set var="a" value="0" scope="page" />


                                                        <c:forEach items="${data}" var="item">
                                                            <tr>
                                                                <td>
                                                                    <fmt:formatDate value="${item.createdDate}" pattern="dd-MM-yyyy" />
                                                                </td>
                                                                <td>${item.subEntryType}</td>  
                                                                <td>${item.supplier}</td>
                                                                <td>${item.project}</td>
                                                                <td>${item.project}</td>
                                                                <td>${item.asphaltType}</td>
                                                                <td>${item.asphaltTon}</td>
                                                                <td>${item.plantBilty}</td>
                                                                <td>${item.recipientBilty}</td>
                                                                <td>${item.quantity}</td>
                                                                <td>${item.rate}</td>
                                                                <td>${item.totalPrice}</td>
                                                                <c:set var="q" value="${q+item.quantity}" />
                                                                <c:set var="r" value="${r+item.rate}" />
                                                                <c:set var="a" value="${a+item.totalPrice}" />
                                                            </tr>
                                                        </c:forEach>
                                                        <tr>
                                                            <td colspan="12">
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td colspan="9">Closing Stock</td>
                                                            <td>${q}</td>
                                                            <td>${r}</td>    
                                                            <td>${a}</td>
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
                window.location = ctx + "/report?type=consumptionAndReceiving&from=" + from + "&to=" + to + "&eitem=" + accountName.replace("&", "-");
            } else {

            }
        });
    </script>

</html>

