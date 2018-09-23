<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<jsp:directive.page session="true" />
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
        <title>Home</title>
        <!-- Icons-->
        <link href="<c:url value="/resources/vendors/@coreui/coreui-icons.min.css"/>" rel="stylesheet">
        <link href="<c:url value="/resources/vendors/flag-icon-css/flag-icon.min.css"/>" rel="stylesheet">
        <link href="<c:url value="/resources/vendors/font-awesome/font-awesome.min.css"/>" rel="stylesheet">
        <link href="<c:url value="/resources/vendors/simple-line-icons/simple-line-icons.css"/>" rel="stylesheet">
        <!-- Main styles for this application-->
        <link href="<c:url value="/resources/css/style.css"/>" rel="stylesheet">
        <link href="<c:url value="/resources/vendors/pace-progress/css/pace.min.css"/>" rel="stylesheet">
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
                    <div id="ui-view">
                        <div>
                            <div class="animated fadeIn">

                                <div class="row">
                                    <div class="col-lg-12">
                                        <div class="card">
                                            <div class="card-header">
                                                <i class="fa fa-align-justify"></i>
                                                <span>${user.username} 's Activity</span>
                                            </div>
                                            <div class="card-body">
                                                <table id="userActivityTable"
                                                       class="table table-responsive-sm table-bordered table-striped table-sm">
                                                    <thead>
                                                        <tr>
                                                            <th>Actions</th>
                                                            <th>Details</th>
                                                            <th>Timestamp</th>
                                                            <th>IP</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
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


            </main>

            <!-- right side bar -->
            <c:import url="../fragments/fragment-sidebar-right.jsp" />

        </div>

        <!-- footer -->
        <c:import url="../fragments/fragment-footer.jsp" />

        <!-- CoreUI and necessary plugins-->
        <script src="<c:url value="/resources/vendors/jquery/jquery.min.js"/>"></script>
        <script src="<c:url value="/resources/vendors/popper.js/popper.min.js"/>"></script>
        <script src="<c:url value="/resources/vendors/bootstrap/bootstrap.min.js"/>"></script>
        <script src="<c:url value="/resources/vendors/pace-progress/pace.min.js"/>"></script>
        <script src="<c:url value="/resources/vendors/perfect-scrollbar/perfect-scrollbar.min.js"/>"></script>
        <script src="<c:url value="/resources/vendors/@coreui/coreui.min.js"/>"></script>
        <script type="text/javascript" src="https://cdn.datatables.net/v/dt/dt-1.10.18/datatables.min.js"></script>     
        <!-- Plugins and scripts required by this view-->
        <script src="<c:url value="/resources/js/usersActivity.js"/>"></script>


    </body>
</html>

