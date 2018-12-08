<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<jsp:directive.page session="true" />
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
        <title>Profit & Loss</title>

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
                                                <span>Profit & Loss</span>
                                                <input value="${type}" id="type" hidden="true">
                                            </div>
                                            <div class="card-body">
                                                <!--                                                <div id="filtersDiv">
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
                                                                                                        <div class="col-sm-3">
                                                                                                            <label>Buyer/Supplier</label>
                                                                                                            <select class="form-control" id="buySup" name="buySup">
                                                                                                                <option selected value="">All</option>
                                                                                                            </select>
                                                                                                        </div>
                                                                                                        <div class="col-sm-3">
                                                                                                            <label>Project</label>
                                                                                                            <select class="form-control" id="proj" name="proj">
                                                                                                                <option selected value="">All</option>
                                                                                                            </select>
                                                                                                        </div>
                                                                                                    </div>
                                                                                                    <div class="form-group row">
                                                                                                        <div class="col-sm-12">
                                                                                                            <button class="btn btn-primary pull-right" onclick="advanceSearch()" type="button">Apply</button>
                                                                                                        </div>
                                                                                                    </div>
                                                                                                </div>-->
                                                <hr>

                                                <table id="viewDatatable"
                                                       class="table table-responsive-sm table-bordered table-striped table-sm">
                                                    <thead>
                                                        <tr>
                                                            <th>Description</th>
                                                            <th>Amount</th>
                                                            <th>Amount</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:set var="totalSales" value="0" scope="page" />
                                                        <c:set var="directGrossProfit" value="0" scope="page"/>
                                                        <c:set var="totalCrushSales" value="0" scope="page" />
                                                        <c:set var="totalIndirectExpenses" value="0" scope="page" />
                                                        <c:set var="netProfit" value="0" scope="page" />


                                                        <c:forEach items="${stockTrace}" var="item">
                                                            <c:set var="totalSales" value="${totalSales + item.salesAmount}" scope="page"/>
                                                            <c:set var="directGrossProfit" value="${directGrossProfit + item.salesAmount}" scope="page"/>

                                                        </c:forEach>
                                                        <tr>
                                                            <td>
                                                                Sales
                                                            </td>
                                                            <td>
                                                                ${totalSales}
                                                            </td>
                                                            <td>
                                                                ${totalSales}
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th>
                                                                Direct Expenses
                                                            </th>
                                                            <td>
                                                            </td>
                                                            <td>
                                                            </td>
                                                        </tr>
                                                        <c:forEach items="${stockTrace}" var="item">
                                                            <c:if test = "${item.itemId == 6}">
                                                                <c:set var="totalCrushSales" value="${totalCrushSales+item.salesAmount}" scope="page" />
                                                            </c:if>
                                                            <c:if test = "${item.itemId != 6 && item.itemId !=17}">
                                                                <tr>
                                                                    <td>
                                                                        ${item.itemName} ${item.subType}
                                                                    </td>
                                                                    <td>
                                                                        ${item.salesAmount}
                                                                    </td>
                                                                    <td></td>
                                                                </tr>
                                                            </c:if>
                                                        </c:forEach>
                                                        <tr>
                                                            <td>
                                                                Crush All Types
                                                            </td>
                                                            <td>
                                                                ${totalCrushSales}
                                                            </td>
                                                            <td></td>
                                                        </tr>
                                                        <tr>
                                                            <th>
                                                                Gross Profit
                                                            </th>
                                                            <td>
                                                                ${directGrossProfit}
                                                            </td>
                                                            <td>
                                                                ${directGrossProfit}
                                                            </td>
                                                        </tr>

                                                        <tr>
                                                            <th>
                                                            </th>
                                                            <td>
                                                            </td>
                                                            <td>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th>
                                                                In-Direct Expenses
                                                            </th>
                                                            <td>
                                                            </td>
                                                            <td>
                                                            </td>
                                                        </tr>
                                                        <c:forEach var="entry" items="${indirectExpenses}">
                                                            <tr>
                                                                <td>
                                                                    <c:out value="${entry.key}"/>
                                                                </td> 
                                                                <td>
                                                                    <c:out value="${entry.value}"/>
                                                                    <c:set var="totalIndirectExpenses" 
                                                                           value="${totalIndirectExpenses+entry.value}" scope="page" />
                                                                </td>
                                                                <td></td>
                                                            </tr>
                                                        </c:forEach>

                                                        <tr>
                                                            <th>
                                                                Total In-Direct Expenses
                                                            </th>
                                                            <td>
                                                                ${totalIndirectExpenses}
                                                            </td>
                                                            <td>
                                                                ${totalIndirectExpenses}
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th>
                                                            </th>
                                                            <td>
                                                            </td>
                                                            <td>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <th>
                                                                Net Profit 
                                                            </th>
                                                            <td>
                                                            </td>
                                                            <td>
                                                                <c:set var="netProfit" 
                                                                       value="${totalSales-directGrossProfit-totalIndirectExpenses}" 
                                                                       scope="page" />

                                                                ${netProfit}
                                                            </td>
                                                        </tr>
                                                    </tbody>
                                                </table>
                                                <!--                                                <div>
                                                                                                    <hr>
                                                                                                    <h4>Summary</h4>
                                                                                                    <br>
                                                                                                    <h5>Total ${type} Amount: 
                                                                                                        <text id="totalField">
                                                
                                                                                                        </text>
                                                                                                    </h5>
                                                                                                </div>-->
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
    <script src="<c:url value="/resources/js/profitLossReport.js"/>"></script>

</html>

