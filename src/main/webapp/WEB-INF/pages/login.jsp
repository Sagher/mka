<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<jsp:directive.page session="true" />
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
        <title>Login</title>

        <c:import url="fragments/global-css.jsp" />

    </head>

    <body class="app flex-row align-items-center">
        <sec:authorize access="hasAnyRole('ADMIN', 'MANAGER', 'READONLY')">
            <c:redirect url="index"/>
        </sec:authorize>
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-md-8">
                    <div class="card-group">

                        <div class="card p-4">
                            <div class="card-body">
                                <form name='loginForm' id="loginForm"
                                      action="<c:url value='/login' />" method='POST'>
                                    <h1>Login</h1>
                                    <p class="text-muted">Sign In to your account</p>
                                    <div class="input-group mb-3">
                                        <div class="input-group-prepend">
                                            <span class="input-group-text">
                                                <i class="icon-user"></i>
                                            </span>
                                        </div>
                                        <input type="text" required="false" placeholder="Username" name="username" class="form-control">

                                    </div>
                                    <div class="input-group mb-4">
                                        <div class="input-group-prepend">
                                            <span class="input-group-text">
                                                <i class="icon-lock"></i>
                                            </span>
                                        </div>
                                        <input type="password"  required="false" placeholder="Password" name="password" class="form-control">
                                    </div>
                                    <c:if test="${not empty error}">
                                        <div class="mb-4">
                                            <script>
                                                document.addEventListener("DOMContentLoaded", function () {
                                                    noty({
                                                        text: "${error}",
                                                        type: "error", layout: "center", timeout: 4000
                                                    });
                                                });
                                            </script>
                                        </div>
                                    </c:if>
                                    <c:if test="${not empty msg}">
                                        <script>
                                            document.addEventListener("DOMContentLoaded", function () {
                                                noty({
                                                    text: "${msg}",
                                                    type: "information", layout: "topRight", timeout: 4000
                                                });
                                            });
                                        </script>
                                    </c:if>

                                    <div class="row">
                                        <div class="col-6">
                                            <button class="btn btn-primary px-4" type="submit" name="submit">Login</button>
                                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
<!--                        <div class="card text-white bg-primary py-5 d-md-down-none" style="width:44%">
                            <div class="card-body text-center">
                                <div>
                                    <h2>Sign up</h2>
                                    <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, 
                                        sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.</p>
                                    <button class="btn btn-primary active mt-3" type="button">
                                        Register Now!
                                    </button>
                                </div>
                            </div>
                        </div>-->
                    </div>
                </div>
            </div>
        </div>

    </body>

    <!-- global js -->
    <c:import url="fragments/global-js.jsp" />
</html>