<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sec"
          uri="http://www.springframework.org/security/tags"%>
<jsp:directive.page session="true" />
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
        <title>Error</title>

        <c:import url="fragments/global-css.jsp" />


    </head>
    <body class="app flex-row align-items-center">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-md-6">
                    <div class="clearfix">
                        <h1 class="float-left display-3 mr-4">
                            ${errorCode}
                        </h1>
                        <h4 class="pt-3">
                            ${errorMessage}
                        </h4>
                        <p class="text-muted">
                            ${errorDescription}
                        </p>
                    </div>
                    <div class="input-prepend input-group">
                        <a class="form-control" size="13" href="<c:url value="/"/> ">
                            <center>Go To Home</center>
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </body>
    <!-- global js -->
    <c:import url="fragments/global-js.jsp" />
</html>