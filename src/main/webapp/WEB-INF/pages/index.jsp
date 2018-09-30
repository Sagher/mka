<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
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
                                            <button class="btn btn-transparent dropdown-toggle p-0" type="button">
                                                More
                                            </button>
                                        </div>
                                        <div class="text-value">9.123</div>
                                        <div>Total Sales</div>
                                    </div>
                                    <div class="chart-wrapper mt-3 mx-3" style="height:35px;">
                                        <canvas class="chart" id="card-chart1" height="35"></canvas>
                                    </div>
                                </div>
                            </div>
                            <!-- /.col-->
                            <div class="col-sm-6 col-lg-3">
                                <div class="card text-white bg-info">
                                    <div class="card-body pb-0">
                                        <div class="btn-group float-right">
                                            <button class="btn btn-transparent dropdown-toggle p-0" type="button">
                                                More
                                            </button>
                                        </div>
                                        <div class="text-value">9.823</div>
                                        <div>Total Purchases</div>
                                    </div>
                                    <div class="chart-wrapper mt-3 mx-3" style="height:35px;">
                                        <canvas class="chart" id="card-chart2" height="35"></canvas>
                                    </div>
                                </div>
                            </div>
                            <!-- /.col-->
                            <div class="col-sm-6 col-lg-3">
                                <div class="card text-white bg-warning">
                                    <div class="card-body pb-0">
                                        <div class="btn-group float-right">
                                            <button class="btn btn-transparent dropdown-toggle p-0" type="button">
                                                More
                                            </button>
                                        </div>
                                        <div class="text-value">9.823</div>
                                        <div>Total Stock Value</div>
                                    </div>
                                    <div class="chart-wrapper mt-3" style="height:35px;">
                                        <canvas class="chart" id="card-chart3" height="35"></canvas>
                                    </div>
                                </div>
                            </div>
                            <!-- /.col-->
                            <div class="col-sm-6 col-lg-3">
                                <div class="card text-white bg-danger">
                                    <div class="card-body pb-0">
                                        <div class="btn-group float-right">
                                            <button class="btn btn-transparent dropdown-toggle p-0" type="button">
                                                More
                                            </button>
                                        </div>
                                        <div class="text-value">9.823</div>
                                        <div>Total Users</div>
                                    </div>
                                    <div class="chart-wrapper mt-3 mx-3" style="height:35px;">
                                        <canvas class="chart" id="card-chart4" height="35"></canvas>
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
                            <div class="col-sm-6 col-lg-3">
                                <div class="brand-card item-summary">
                                    <div class="brand-card-header text-white bg-info">
                                        <div class="card-body">
                                            <div class="btn-group float-right">
                                                <button class="btn btn-transparent dropdown-toggle p-0" type="button">
                                                    More
                                                </button>
                                            </div>
                                            <div class="item-name">BITUMEN</div>
                                            <div>Total Sales: 12000</div>
                                        </div>
                                    </div>
                                    <div class="brand-card-body">
                                        <div>
                                            <div class="text-value">89</div>
                                            <div class="text-uppercase text-muted small">In Stock</div>
                                        </div>
                                        <div>
                                            <div class="text-value">459</div>
                                            <div class="text-uppercase text-muted small">Price</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6 col-lg-3">
                                <div class="brand-card item-summary">
                                    <div class="brand-card-header text-white bg-danger">
                                        <div class="card-body">
                                            <div class="btn-group float-right">
                                                <button class="btn btn-transparent dropdown-toggle p-0" type="button">
                                                    More
                                                </button>
                                            </div>
                                            <div class="item-name">LDO</div>
                                            <div>Total Sales: 12000</div>
                                        </div>
                                    </div>
                                    <div class="brand-card-body">
                                        <div>
                                            <div class="text-value">89</div>
                                            <div class="text-uppercase text-muted small">In Stock</div>
                                        </div>
                                        <div>
                                            <div class="text-value">459</div>
                                            <div class="text-uppercase text-muted small">Price</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6 col-lg-3">
                                <div class="brand-card item-summary">
                                    <div class="brand-card-header text-white bg-warning">
                                        <div class="card-body">
                                            <div class="btn-group float-right">
                                                <button class="btn btn-transparent dropdown-toggle p-0" type="button">
                                                    More
                                                </button>
                                            </div>
                                            <div class="item-name">DIESEL</div>
                                            <div>Total Sales: 12000</div>
                                        </div>
                                    </div>
                                    <div class="brand-card-body">
                                        <div>
                                            <div class="text-value">89</div>
                                            <div class="text-uppercase text-muted small">In Stock</div>
                                        </div>
                                        <div>
                                            <div class="text-value">459</div>
                                            <div class="text-uppercase text-muted small">Price</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6 col-lg-3">
                                <div class="brand-card item-summary">
                                    <div class="brand-card-header text-white bg-success">
                                        <div class="card-body">
                                            <div class="btn-group float-right">
                                                <button class="btn btn-transparent dropdown-toggle p-0" type="button">
                                                    More
                                                </button>
                                            </div>
                                            <div class="item-name">Prime Coat</div>
                                            <div>Total Sales: 12000</div>
                                        </div>
                                    </div>
                                    <div class="brand-card-body">
                                        <div>
                                            <div class="text-value">89</div>
                                            <div class="text-uppercase text-muted small">In Stock</div>
                                        </div>
                                        <div>
                                            <div class="text-value">459</div>
                                            <div class="text-uppercase text-muted small">Price</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-sm-6 col-lg-3">
                                <div class="brand-card item-summary">
                                    <div class="brand-card-header text-white bg-info">
                                        <div class="card-body">
                                            <div class="btn-group float-right">
                                                <button class="btn btn-transparent dropdown-toggle p-0" type="button">
                                                    More
                                                </button>
                                            </div>
                                            <div class="item-name">Tack Coat</div>
                                            <div>Total Sales: 12000</div>
                                        </div>
                                    </div>
                                    <div class="brand-card-body">
                                        <div>
                                            <div class="text-value">89</div>
                                            <div class="text-uppercase text-muted small">In Stock</div>
                                        </div>
                                        <div>
                                            <div class="text-value">459</div>
                                            <div class="text-uppercase text-muted small">Price</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6 col-lg-3">
                                <div class="brand-card item-summary">
                                    <div class="brand-card-header text-white bg-danger">
                                        <div class="card-body">
                                            <div class="btn-group float-right">
                                                <button class="btn btn-transparent dropdown-toggle p-0" type="button">
                                                    More
                                                </button>
                                            </div>
                                            <div class="item-name">ASPHALT</div>
                                            <div>Total Sales: 12000</div>
                                        </div>
                                    </div>
                                    <div class="brand-card-body">
                                        <div>
                                            <div class="text-value">89</div>
                                            <div class="text-uppercase text-muted small">In Stock</div>
                                        </div>
                                        <div>
                                            <div class="text-value">459</div>
                                            <div class="text-uppercase text-muted small">Price</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6 col-lg-3">
                                <div class="brand-card item-summary">
                                    <div class="brand-card-header text-white bg-warning">
                                        <div class="card-body">
                                            <div class="btn-group float-right">
                                                <button class="btn btn-transparent dropdown-toggle p-0" type="button">
                                                    More
                                                </button>
                                            </div>
                                            <div class="item-name">CRUSH</div>
                                            <div>Total Sales: 12000</div>
                                        </div>
                                    </div>
                                    <div class="brand-card-body">
                                        <div>
                                            <div class="text-value">89</div>
                                            <div class="text-uppercase text-muted small">In Stock</div>
                                        </div>
                                        <div>
                                            <div class="text-value">459</div>
                                            <div class="text-uppercase text-muted small">Price</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6 col-lg-3">
                                <div class="brand-card item-summary">
                                    <div class="brand-card-header text-white bg-success">
                                        <div class="card-body">
                                            <div class="btn-group float-right">
                                                <button class="btn btn-transparent dropdown-toggle p-0" type="button">
                                                    More
                                                </button>
                                            </div>
                                            <div class="item-name">BITUMEN</div>
                                            <div>Total Sales: 12000</div>
                                        </div>
                                    </div>
                                    <div class="brand-card-body">
                                        <div>
                                            <div class="text-value">89</div>
                                            <div class="text-uppercase text-muted small">In Stock</div>
                                        </div>
                                        <div>
                                            <div class="text-value">459</div>
                                            <div class="text-uppercase text-muted small">Price</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
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
                                                            <strong class="h4">9,123</strong>
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
                                                            <strong class="h4">22,643</strong>
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
                                                            <div class="progress-bar bg-info" role="progressbar" style="width: 34%" aria-valuenow="34" aria-valuemin="0" aria-valuemax="100"></div>
                                                        </div>
                                                        <div class="progress progress-xs">
                                                            <div class="progress-bar bg-danger" role="progressbar" style="width: 78%" aria-valuenow="78" aria-valuemin="0" aria-valuemax="100"></div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="progress-group mb-4">
                                                    <div class="progress-group-prepend">
                                                        <span class="progress-group-text">Tuesday</span>
                                                    </div>
                                                    <div class="progress-group-bars">
                                                        <div class="progress progress-xs">
                                                            <div class="progress-bar bg-info" role="progressbar" style="width: 56%" aria-valuenow="56" aria-valuemin="0" aria-valuemax="100"></div>
                                                        </div>
                                                        <div class="progress progress-xs">
                                                            <div class="progress-bar bg-danger" role="progressbar" style="width: 94%" aria-valuenow="94" aria-valuemin="0" aria-valuemax="100"></div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="progress-group mb-4">
                                                    <div class="progress-group-prepend">
                                                        <span class="progress-group-text">Wednesday</span>
                                                    </div>
                                                    <div class="progress-group-bars">
                                                        <div class="progress progress-xs">
                                                            <div class="progress-bar bg-info" role="progressbar" style="width: 12%" aria-valuenow="12" aria-valuemin="0" aria-valuemax="100"></div>
                                                        </div>
                                                        <div class="progress progress-xs">
                                                            <div class="progress-bar bg-danger" role="progressbar" style="width: 67%" aria-valuenow="67" aria-valuemin="0" aria-valuemax="100"></div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="progress-group mb-4">
                                                    <div class="progress-group-prepend">
                                                        <span class="progress-group-text">Thursday</span>
                                                    </div>
                                                    <div class="progress-group-bars">
                                                        <div class="progress progress-xs">
                                                            <div class="progress-bar bg-info" role="progressbar" style="width: 12%" aria-valuenow="12" aria-valuemin="0" aria-valuemax="100"></div>
                                                        </div>
                                                        <div class="progress progress-xs">
                                                            <div class="progress-bar bg-danger" role="progressbar" style="width: 67%" aria-valuenow="67" aria-valuemin="0" aria-valuemax="100"></div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="progress-group mb-4">
                                                    <div class="progress-group-prepend">
                                                        <span class="progress-group-text">Friday</span>
                                                    </div>
                                                    <div class="progress-group-bars">
                                                        <div class="progress progress-xs">
                                                            <div class="progress-bar bg-info" role="progressbar" style="width: 12%" aria-valuenow="12" aria-valuemin="0" aria-valuemax="100"></div>
                                                        </div>
                                                        <div class="progress progress-xs">
                                                            <div class="progress-bar bg-danger" role="progressbar" style="width: 67%" aria-valuenow="67" aria-valuemin="0" aria-valuemax="100"></div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="progress-group mb-4">
                                                    <div class="progress-group-prepend">
                                                        <span class="progress-group-text">Saturday</span>
                                                    </div>
                                                    <div class="progress-group-bars">
                                                        <div class="progress progress-xs">
                                                            <div class="progress-bar bg-info" role="progressbar" style="width: 12%" aria-valuenow="12" aria-valuemin="0" aria-valuemax="100"></div>
                                                        </div>
                                                        <div class="progress progress-xs">
                                                            <div class="progress-bar bg-danger" role="progressbar" style="width: 67%" aria-valuenow="67" aria-valuemin="0" aria-valuemax="100"></div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="progress-group mb-4">
                                                    <div class="progress-group-prepend">
                                                        <span class="progress-group-text">Sunday</span>
                                                    </div>
                                                    <div class="progress-group-bars">
                                                        <div class="progress progress-xs">
                                                            <div class="progress-bar bg-info" role="progressbar" style="width: 12%" aria-valuenow="12" aria-valuemin="0" aria-valuemax="100"></div>
                                                        </div>
                                                        <div class="progress progress-xs">
                                                            <div class="progress-bar bg-danger" role="progressbar" style="width: 67%" aria-valuenow="67" aria-valuemin="0" aria-valuemax="100"></div>
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

