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

        <!-- Data tables css -->
        <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/v/dt/dt-1.10.18/datatables.min.css"/>

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
                                        <div class="text-value">9.823</div>
                                        <div>Members online</div>
                                    </div>
                                    <div class="chart-wrapper mt-3 mx-3" style="height:70px;">
                                        <canvas class="chart" id="card-chart1" height="70"></canvas>
                                    </div>
                                </div>
                            </div>
                            <!-- /.col-->
                            <div class="col-sm-6 col-lg-3">
                                <div class="card text-white bg-info">
                                    <div class="card-body pb-0">
                                        <div class="text-value">9.823</div>
                                        <div>Members online</div>
                                    </div>
                                    <div class="chart-wrapper mt-3 mx-3" style="height:70px;">
                                        <canvas class="chart" id="card-chart2" height="70"></canvas>
                                    </div>
                                </div>
                            </div>
                            <!-- /.col-->
                            <div class="col-sm-6 col-lg-3">
                                <div class="card text-white bg-warning">
                                    <div class="card-body pb-0">
                                        <div class="text-value">9.823</div>
                                        <div>Members online</div>
                                    </div>
                                    <div class="chart-wrapper mt-3" style="height:70px;">
                                        <canvas class="chart" id="card-chart3" height="70"></canvas>
                                    </div>
                                </div>
                            </div>
                            <!-- /.col-->
                            <div class="col-sm-6 col-lg-3">
                                <div class="card text-white bg-danger">
                                    <div class="card-body pb-0">
                                        <div class="text-value">9.823</div>
                                        <div>Members online</div>
                                    </div>
                                    <div class="chart-wrapper mt-3 mx-3" style="height:70px;">
                                        <canvas class="chart" id="card-chart4" height="70"></canvas>
                                    </div>
                                </div>
                            </div>
                            <!-- /.col-->
                        </div>
                        <!-- /.row-->
                        <div class="row">
                            <div class="col-md-12">
                                <div class="card">
                                    <div class="card-header">Traffic & Sales</div>
                                    <div class="card-body">
                                        <div class="row">
                                            <div class="col-sm-6">
                                                <div class="row">
                                                    <div class="col-sm-6">
                                                        <div class="callout callout-info">
                                                            <small class="text-muted">New Clients</small>
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
                                                            <small class="text-muted">Recuring Clients</small>
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
                                            </div>
                                            <!-- /.col-->
                                            <div class="col-sm-6">
                                                <div class="row">
                                                    <div class="col-sm-6">
                                                        <div class="callout callout-warning">
                                                            <small class="text-muted">Pageviews</small>
                                                            <br>
                                                            <strong class="h4">78,623</strong>
                                                            <div class="chart-wrapper">
                                                                <canvas id="sparkline-chart-3" width="100" height="30"></canvas>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <!-- /.col-->
                                                    <div class="col-sm-6">
                                                        <div class="callout callout-success">
                                                            <small class="text-muted">Organic</small>
                                                            <br>
                                                            <strong class="h4">49,123</strong>
                                                            <div class="chart-wrapper">
                                                                <canvas id="sparkline-chart-4" width="100" height="30"></canvas>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <!-- /.col-->
                                                </div>
                                                <!-- /.row-->
                                                <hr class="mt-0">
                                                <div class="progress-group">
                                                    <div class="progress-group-header align-items-end">
                                                        <i class="icon-globe progress-group-icon"></i>
                                                        <div>Organic Search</div>
                                                        <div class="ml-auto font-weight-bold mr-2">191.235</div>
                                                        <div class="text-muted small">(56%)</div>
                                                    </div>
                                                    <div class="progress-group-bars">
                                                        <div class="progress progress-xs">
                                                            <div class="progress-bar bg-success" role="progressbar" style="width: 56%" aria-valuenow="56" aria-valuemin="0" aria-valuemax="100"></div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="progress-group">
                                                    <div class="progress-group-header align-items-end">
                                                        <i class="icon-social-facebook progress-group-icon"></i>
                                                        <div>Facebook</div>
                                                        <div class="ml-auto font-weight-bold mr-2">51.223</div>
                                                        <div class="text-muted small">(15%)</div>
                                                    </div>
                                                    <div class="progress-group-bars">
                                                        <div class="progress progress-xs">
                                                            <div class="progress-bar bg-success" role="progressbar" style="width: 15%" aria-valuenow="15" aria-valuemin="0" aria-valuemax="100"></div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="progress-group">
                                                    <div class="progress-group-header align-items-end">
                                                        <i class="icon-social-twitter progress-group-icon"></i>
                                                        <div>Twitter</div>
                                                        <div class="ml-auto font-weight-bold mr-2">37.564</div>
                                                        <div class="text-muted small">(11%)</div>
                                                    </div>
                                                    <div class="progress-group-bars">
                                                        <div class="progress progress-xs">
                                                            <div class="progress-bar bg-success" role="progressbar" style="width: 11%" aria-valuenow="11" aria-valuemin="0" aria-valuemax="100"></div>
                                                        </div>
                                                    </div>
                                                </div>
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
    <script src="<c:url value="/resources/js/main.js"/>"></script>

</html>

