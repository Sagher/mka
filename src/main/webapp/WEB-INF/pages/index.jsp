<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:directive.page session="true" />
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
        <title>Home</title>

        <c:import url="fragments/global-css.jsp" />

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
        <c:import url="fragments/fragment-header.jsp" />

        <!-- app-body -->
        <div class="app-body">

            <!-- left side bar -->
            <c:import url="fragments/fragment-sidebar-left.jsp" />

            <!-- main container -->
            <main class="main">

                <!-- Bread crumb -->
                <c:import url="fragments/fragment-breadcrumb.jsp" />

                <div class="container-fluid">
                    <div class="animated fadeIn">
                        <div class="row">
                            <div class="col-sm-6 col-lg-3">
                                <div class="card text-white bg-primary">
                                    <div class="card-body pb-0">
                                        <div class="btn-group float-right">
                                            <a href="<c:url value="/reports" />">
                                                <button class="btn btn-transparent dropdown-toggle p-0" type="button">
                                                    More
                                                </button>
                                            </a>
                                        </div>
                                        <div class="text-value">${totalSaleUnits}</div>
                                        <div>Total Sales</div>
                                        <div class="text-value">${totalSale}</div>    
                                        <div>Total Price</div>
                                        <div class="info-box-bottom-div"></div>
                                    </div>
                                </div>
                            </div>
                            <!-- /.col-->
                            <div class="col-sm-6 col-lg-3">
                                <div class="card text-white bg-info">
                                    <div class="card-body pb-0">
                                        <div class="btn-group float-right">
                                            <a href="<c:url value="/reports" />">
                                                <button class="btn btn-transparent dropdown-toggle p-0" type="button">
                                                    More
                                                </button>
                                            </a>
                                        </div>
                                        <div class="text-value">${totalPurchaseUnits}</div>
                                        <div>Total Purchases</div>
                                        <div class="text-value">${totalPurchase}</div>    
                                        <div>Total Price</div>
                                        <div class="info-box-bottom-div"></div>
                                    </div>
                                </div>
                            </div>
                            <!-- /.col-->
                            <div class="col-sm-6 col-lg-3">
                                <div class="card text-white bg-warning">
                                    <div class="card-body pb-0">
                                        <div class="btn-group float-right">
                                            <a href="<c:url value="/reports" />">
                                                <button class="btn btn-transparent dropdown-toggle p-0" type="button">
                                                    More
                                                </button>
                                            </a>
                                        </div>
                                        <div class="text-value">${totalStockUnits}</div>
                                        <div>Total Stock</div>
                                        <div class="text-value">${totalStock}</div>
                                        <div>Stock Value</div>
                                        <div class="info-box-bottom-div"></div>
                                    </div>
                                </div>
                            </div>
                            <!-- /.col-->
                            <div class="col-sm-6 col-lg-3">
                                <div class="card text-white bg-danger">
                                    <div class="card-body pb-0">
                                        <div class="text-value">${masterAccount.cashInHand} PKR</div>
                                        <div>Cash In Hand</div>
                                        <div class="text-value">${masterAccount.totalCash} PKR</div>
                                        <div>Total Cash </div>
                                        <div class="info-box-bottom-div"></div>

                                    </div>
                                </div>
                            </div>
                            <!-- /.col-->
                        </div>
                        <!-- /.row-->


                        <div class="card">
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-sm-5">
                                        <h4 class="card-title mb-0">Sales & Purchase Information </h4>
                                        <!--<div class="small text-muted">Last 30 Days</div>-->
                                    </div>

                                    <div class="col-sm-7 d-none d-md-block">
                                        <div class="btn-group btn-group-toggle float-right mr-3">
                                            <label class="btn btn-outline-secondary">
                                                <input type="radio" name="options"> Last 30 Days
                                            </label>
                                        </div>
                                    </div>

                                </div>

                                <div class="chart-wrapper" style="height:300px;margin-top:40px;">
                                    <div class="chartjs-size-monitor" 
                                         style="position: absolute; left: 0px; top: 0px; right: 0px; bottom: 0px; overflow: hidden; pointer-events: none; visibility: hidden; z-index: -1;">
                                        <div class="chartjs-size-monitor-expand" style="position:absolute;left:0;top:0;right:0;bottom:0;overflow:hidden;pointer-events:none;visibility:hidden;z-index:-1;"><div style="position:absolute;width:1000000px;height:1000000px;left:0;top:0"></div></div><div class="chartjs-size-monitor-shrink" style="position:absolute;left:0;top:0;right:0;bottom:0;overflow:hidden;pointer-events:none;visibility:hidden;z-index:-1;"><div style="position:absolute;width:200%;height:200%;left:0; top:0"></div></div></div>
                                    <canvas class="chart chartjs-render-monitor" id="main-chart" height="300" width="1047" style="display: block;"></canvas>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <c:forEach items="${stockTrace}" var="item">
                                <div class="col-sm-6 col-lg-3">
                                    <div class="brand-card item-summary">
                                        <div class="brand-card-header text-white ${item.divBgThemeClass}">
                                            <div class="card-body">
                                                <div class="btn-group float-right">
                                                    <a href="<c:url value="/entries?type=direct&eitem=${item.itemId}" />">
                                                        <button class="btn btn-transparent dropdown-toggle p-0" type="button">
                                                            More
                                                        </button>
                                                    </a>
                                                </div>
                                                <div class="item-name">${item.itemName} <small>${item.subType}</small></div>
                                                <div>Total Sales: ${item.salesUnit} ${item.itemUnit}</div>
                                                <div>Total Sales Amount: 
                                                    <fmt:formatNumber type="number" maxFractionDigits="0" minFractionDigits="0" value="${item.salesAmount}"/>
                                                    PKR</div>
                                            </div>
                                        </div>
                                        <div class="brand-card-body">
                                            <div>
                                                <div class="text-value small">${item.stockUnits} ${item.itemUnit}</div>
                                                <div class="text-uppercase text-muted small">In Stock</div>
                                            </div>
                                            <div>
                                                <div class="text-value small">
                                                    <fmt:formatNumber type="number" maxFractionDigits="0" minFractionDigits="0" value="${item.stockAmount}"/>
                                                    PKR
                                                </div>
                                                <div class="text-uppercase text-muted small">Stock Value</div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>

                        </div>

                        <div class="row">
                            <div class="col-md-12">
                                <div class="card">
                                    <div class="card-body">
                                        <div class="row">
                                            <div class="col-sm-6">
                                                <div class="row">
                                                    <div class="col-sm-6">
                                                        <div class="callout callout-info">
                                                            <small class="text-muted">Sales</small>
                                                            <br>
                                                            <strong class="h4">${totalSale}</strong>
                                                            <div class="chart-wrapper">
                                                                <canvas id="sparkline-chart-1" width="100" height="30"></canvas>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <!-- /.col-->
                                                    <div class="col-sm-6">
                                                        <div class="callout callout-danger">
                                                            <small class="text-muted">Purchases</small>
                                                            <br>
                                                            <strong class="h4">${totalPurchase}</strong>
                                                            <div class="chart-wrapper">
                                                                <canvas id="sparkline-chart-2" width="100" height="30"></canvas>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <!-- /.col-->
                                                </div>
                                                <!-- /.row-->
                                                <hr class="mt-0">
                                                <div class="progress-group mb-4">
                                                    <div class="progress-group-prepend">
                                                        <span class="progress-group-text">Monday</span>
                                                    </div>
                                                    <div class="progress-group-bars">
                                                        <div class="progress progress-xs">
                                                            <div class="progress-bar bg-info" role="progressbar" style="width: ${totalPurchases}%" aria-valuenow="34" aria-valuemin="0" aria-valuemax="100"></div>
                                                        </div>
                                                        <div class="progress progress-xs">
                                                            <div class="progress-bar bg-danger" role="progressbar" style="width: ${totalSale}%" aria-valuenow="78" aria-valuemin="0" aria-valuemax="100"></div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="progress-group mb-4">
                                                    <div class="progress-group-prepend">
                                                        <span class="progress-group-text">Tuesday</span>
                                                    </div>
                                                    <div class="progress-group-bars">
                                                        <div class="progress progress-xs">
                                                            <div class="progress-bar bg-info" role="progressbar" style="width: ${totalPurchases}%" aria-valuenow="34" aria-valuemin="0" aria-valuemax="100"></div>
                                                        </div>
                                                        <div class="progress progress-xs">
                                                            <div class="progress-bar bg-danger" role="progressbar" style="width: ${totalSale}%" aria-valuenow="78" aria-valuemin="0" aria-valuemax="100"></div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="progress-group mb-4">
                                                    <div class="progress-group-prepend">
                                                        <span class="progress-group-text">Wednesday</span>
                                                    </div>
                                                    <div class="progress-group-bars">
                                                        <div class="progress progress-xs">
                                                            <div class="progress-bar bg-info" role="progressbar" style="width: ${totalPurchases}%" aria-valuenow="34" aria-valuemin="0" aria-valuemax="100"></div>
                                                        </div>
                                                        <div class="progress progress-xs">
                                                            <div class="progress-bar bg-danger" role="progressbar" style="width: ${totalSale}%" aria-valuenow="78" aria-valuemin="0" aria-valuemax="100"></div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="progress-group mb-4">
                                                    <div class="progress-group-prepend">
                                                        <span class="progress-group-text">Thursday</span>
                                                    </div>
                                                    <div class="progress-group-bars">
                                                        <div class="progress progress-xs">
                                                            <div class="progress-bar bg-info" role="progressbar" style="width: ${totalPurchases}%" aria-valuenow="34" aria-valuemin="0" aria-valuemax="100"></div>
                                                        </div>
                                                        <div class="progress progress-xs">
                                                            <div class="progress-bar bg-danger" role="progressbar" style="width: ${totalSale}%" aria-valuenow="78" aria-valuemin="0" aria-valuemax="100"></div>
                                                        </div>
                                                    </div>                                                </div>
                                                <div class="progress-group mb-4">
                                                    <div class="progress-group-prepend">
                                                        <span class="progress-group-text">Friday</span>
                                                    </div>
                                                    <div class="progress-group-bars">
                                                        <div class="progress progress-xs">
                                                            <div class="progress-bar bg-info" role="progressbar" style="width: ${totalPurchases}%" aria-valuenow="34" aria-valuemin="0" aria-valuemax="100"></div>
                                                        </div>
                                                        <div class="progress progress-xs">
                                                            <div class="progress-bar bg-danger" role="progressbar" style="width: ${totalSale}%" aria-valuenow="78" aria-valuemin="0" aria-valuemax="100"></div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="progress-group mb-4">
                                                    <div class="progress-group-prepend">
                                                        <span class="progress-group-text">Saturday</span>
                                                    </div>
                                                    <div class="progress-group-bars">
                                                        <div class="progress progress-xs">
                                                            <div class="progress-bar bg-info" role="progressbar" style="width: ${totalPurchases}%" aria-valuenow="34" aria-valuemin="0" aria-valuemax="100"></div>
                                                        </div>
                                                        <div class="progress progress-xs">
                                                            <div class="progress-bar bg-danger" role="progressbar" style="width: ${totalSale}%" aria-valuenow="78" aria-valuemin="0" aria-valuemax="100"></div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="progress-group mb-4">
                                                    <div class="progress-group-prepend">
                                                        <span class="progress-group-text">Sunday</span>
                                                    </div>
                                                    <div class="progress-group-bars">
                                                        <div class="progress progress-xs">
                                                            <div class="progress-bar bg-info" role="progressbar" style="width: ${totalPurchases}%" aria-valuenow="34" aria-valuemin="0" aria-valuemax="100"></div>
                                                        </div>
                                                        <div class="progress progress-xs">
                                                            <div class="progress-bar bg-danger" role="progressbar" style="width: ${totalSale}%" aria-valuenow="78" aria-valuemin="0" aria-valuemax="100"></div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>


                                            <!--<div class="col-sm-1" style="border-left:0.5px solid gainsboro;height:380px"></div>-->
                                            <!-- /.col-->
                                            <div class="col-sm-6" >
                                                <div class="row" style="margin-top: 20px">
                                                    <div class="col-sm-12">
                                                        <div class="progress-group-header">
                                                            <i class="icon-user progress-group-icon"></i>
                                                            <div class="font-weight-bold">User</div>
                                                            <div class="ml-auto font-weight-bold" style="margin-right:10px">
                                                                Last Logged In At</div>
                                                        </div>
                                                    </div>
                                                    <!-- /.col-->
                                                </div>
                                                <!-- /.row-->
                                                <br>
                                                <hr class="mt-0">
                                                <c:forEach items="${users}" var="user">
                                                    <div class="row" style="padding-top: 10px">
                                                        <div class="col-sm-12">
                                                            <div class="progress-group-header">
                                                                <div>
                                                                    <img class="img-avatar" style="max-height: 30px"
                                                                         src="<c:url value="${user.picture}"/>">
                                                                    &nbsp;
                                                                    ${user.fullname}
                                                                </div>
                                                                <div class="ml-auto">${user.lastLoginDate}</div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </c:forEach>
                                            </div>
                                            <!-- /.col-->
                                        </div>
                                        <!-- /.row-->
                                    </div>
                                </div>
                            </div>
                            <!-- /.col-->
                        </div>
                        <!-- /.row-->

                    </div>
                </div>
            </main>

            <!-- right side bar -->
            <c:import url="fragments/fragment-sidebar-right.jsp" />

        </div>

        <!-- footer -->
        <c:import url="fragments/fragment-footer.jsp" />

    </body>

    <!-- global js -->
    <c:import url="fragments/global-js.jsp" />
    <script src="<c:url value="/resources/vendors/chart.js/Chart.min.js"/>"></script>
    <script src="<c:url value="/resources/vendors/@coreui/custom-tooltips.min.js"/>"></script>
    <script src="<c:url value="/resources/js/main.js"/>"></script>

</html>

