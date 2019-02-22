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
                                                            SALARY ACCOUNT
                                                        </h6>
                                                        As On <text id="date"></text>
                                                        <script>
                                                            n = new Date();
                                                            y = n.getFullYear();
                                                            m = n.getMonth() + 1;
                                                            d = n.getDate();
                                                            document.getElementById("date").innerHTML = d + "-" + m + "-" + y;
                                                        </script>
                                                    </span>
                                                </center>
                                            </div>
                                            <div class="card-body">
                                                <div class="form-group row">

                                                    <div class="col-sm-5">
                                                        <div class="input-group">
                                                            <div class="input-group-prepend">
                                                                <span class="input-group-text">
                                                                    From
                                                                </span>
                                                            </div>
                                                            <input class="form-control" value="${from}" id="from" type="date">
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-5">
                                                        <div class="input-group">
                                                            <div class="input-group-prepend">
                                                                <span class="input-group-text">
                                                                    To
                                                                </span>
                                                            </div>
                                                            <input class="form-control" value="${to}" id="to" type="date">
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-2">
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
                                                            <th>NAME</th>
                                                            <th>DESCRIPTION</th>
                                                            <th>Dr.</th>
                                                            <th>Cr.</th>
                                                            <th>Dr. OR Cr.</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:set var="totalDr" value="0" scope="page" />
                                                        <c:set var="totalCr" value="0" scope="page" />
                                                        <c:set var="totalAm" value="0" scope="page" />


                                                        <c:forEach items="${employeesPayments}" var="item">
                                                        <td>${item.employees.name}</td>
                                                        <td>${item.description}</td>
                                                        <c:if test = "${item.type == 'CREDIT'}">
                                                            <td></td>
                                                            <td>${item.amountPayed}</td>
                                                            <c:set var="totalCr" value="${totalCr+item.amountPayed}" />
                                                        </c:if>
                                                        <c:if test = "${item.type == 'DEBIT'}">
                                                            <td>${item.amountPayed}</td>
                                                            <td></td>
                                                            <c:set var="totalDr" value="${totalDr+item.amountPayed}" />
                                                        </c:if>

                                                        <td></td>

                                                        </tr>
                                                    </c:forEach>
                                                    <tr>
                                                        <td colspan="12">
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td colspan="2">TOTAL</td>
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
                                                                if (from.length > 0 || to.length > 0) {
                                                                    console.log(from + " -> " + to);
                                                                    window.location = ctx + "/report?type=salaryAccount&from=" + from + "&to=" + to;
                                                                } else {

                                                                }
                                                            });
    </script>

</html>

