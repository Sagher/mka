<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<jsp:directive.page session="true" />
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
        <title>Reports</title>

        <c:import url="../fragments/global-css.jsp" />

        <!-- Data tables css -->
        <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/v/dt/dt-1.10.18/datatables.min.css"/>
        <style>
            .item-summary{
                max-height: 150px !important;
            }
            .item-name{
                font-size: 1.125rem;
            }
            .text-value {
                font-size: 1.00125rem !important;
                font-weight: 600 !important;
            }
            .info-box-bottom-div{
                padding-bottom: 8px;
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
                            <c:forEach items="${stockTrace}" var="item">
                                <div class="col-sm-6 col-lg-3">
                                    <div class="brand-card item-summary">
                                        <div class="brand-card-header text-white ${item.divBgThemeClass}">
                                            <div class="card-body">
                                                <div class="btn-group float-right">
                                                    <a href="<c:url value="/entries?type=direct&eitem=${item.itemId}" />">
                                                        <button class="btn btn-transparent dropdown-toggle p-0" type="button">
                                                            Detail
                                                        </button>
                                                    </a>
                                                </div>
                                                <div class="item-name">${item.itemName} <small>${item.subType}</small></div>
                                                <div>Total Sales: ${item.salesUnit} ${item.itemUnit}</div>
                                                <div>Total Sales Amount: ${item.salesAmount} PKR</div>
                                            </div>
                                        </div>
                                        <div class="brand-card-body">
                                            <div>
                                                <div class="text-value">${item.stockUnits} ${item.itemUnit}</div>
                                                <div class="text-uppercase text-muted small">In Stock</div>
                                            </div>
                                            <div>
                                                <div class="text-value">${item.stockAmount} PKR</div>
                                                <div class="text-uppercase text-muted small">Stock Value</div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                        <hr>

                        <div class="row">
                            <div class="col-sm-6 col-lg-3">
                                <div class="brand-card item-summary">
                                    <div class="brand-card-header text-white">
                                        <div class="card-body">
                                            <a href="<c:url value="/entries?type=indirect" />">
                                                <div class="item-name">ALL EXPENSES DETAILS</div>
                                            </a>
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
    <script src="<c:url value="/resources/vendors/chart.js/Chart.min.js"/>"></script>
    <script src="<c:url value="/resources/vendors/@coreui/custom-tooltips.min.js"/>"></script>

</html>

