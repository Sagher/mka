<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<jsp:directive.page session="true" />
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
        <title>Accounts Payable & Receivable</title>

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
                                                            ACCOUNTS PAYABLE & ACCOUNTS RECEIVABLE
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
                                                <c:set var="srNum" value="1" scope="page" />

                                                <table class="table-striped table-bordered" style="float: left; width: 50%">
                                                    <thead>
                                                        <tr>
                                                            <th>SR. NO.</th>
                                                            <th>ACCOUNTS RECEIVABLE</th>
                                                            <th>AMOUNT</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:forEach items="${receivablees}" var="receivablee">
                                                            <tr>
                                                                <td>${srNum}
                                                                    <c:set var="srNum" value="${srNum+1}" />
                                                                </td>
                                                                <td>${receivablee.name}</td>
                                                                <td>${receivablee.receivable}</td>
                                                            </tr>
                                                        </c:forEach>
                                                    </tbody>
                                                </table>
                                                <table class="table-striped table-bordered" style="float: left; width: 50%">
                                                    <thead>
                                                        <tr>
                                                            <th>ACCOUNTS PAYABLE</th>
                                                            <th>AMOUNT</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:forEach items="${payablees}" var="payablee">
                                                            <tr>
                                                                <td>${payablee.name}</td>
                                                                <td>${payablee.payable}</td>
                                                            </tr>
                                                        </c:forEach>
                                                    </tbody>
                                                </table>
                                                <table class="table table-striped table-bordered">
                                                    <tbody>
                                                        <tr>
                                                            <td></td>
                                                            <td></td>
                                                            <td></td>
                                                            <td></td>
                                                        </tr>
                                                        <tr>
                                                            <td style="width: 25%">TOTAL</td>
                                                            <td style="width: 25%">${allReceivable}</td>
                                                            <td style="width: 25%">TOTAL</td>
                                                            <td style="width: 25%">${allPayable}</td>
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

</html>

