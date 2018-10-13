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
                        <div class="card">
                            <div class="card-header">
                                <a class="btn btn-sm btn-primary" 
                                   type="button" href="<c:url value="/entries?type=direct" />">
                                    View Direct Entries
                                </a>
                                <a class="btn btn-sm btn-primary" 
                                   type="button" href="<c:url value="/entries?type=indirect" />">
                                    View In-Direct Entries
                                </a>
                            </div>

                        </div>

<!--                        <div class="col-6 col-lg-3">
                            <div class="card">
                                <div class="card-body p-3 d-flex align-items-center">
                                    <i class="fa fa-cogs bg-primary p-3 font-2xl mr-3"></i>
                                    <div>
                                        <div class="text-value-sm text-primary">$1.999,50</div>
                                        <div class="text-muted text-uppercase font-weight-bold small">Income</div>
                                    </div>
                                </div>
                                <div class="card-footer px-3 py-2">
                                    <a class="btn-block text-muted d-flex justify-content-between align-items-center" href="#">
                                        <span class="small font-weight-bold">View More</span>
                                        <i class="fa fa-angle-right"></i>
                                    </a>
                                </div>
                            </div>
                        </div>-->
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

