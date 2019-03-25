<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<jsp:directive.page session="true" />
<%@ page buffer="64kb" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
        <title>Consumption & Receiving</title>
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
                                                            ${type.itemName}
                                                            <c:if test = "${subType!= null}">
                                                                (${subType})
                                                            </c:if>
                                                            Consumption & Receiving
                                                        </h6>
                                                        FROM ${from} TO ${to}
                                                    </span>
                                                </center>
                                            </div>
                                            <div class="card-body">
                                                <div class="form-group row">

                                                    <div class="col-sm-3">
                                                        <div class="input-group">
                                                            <div class="input-group-prepend">
                                                                <span class="input-group-text">
                                                                    From
                                                                </span>
                                                            </div>
                                                            <input class="form-control" value="${from}" id="from" type="date">
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-3">
                                                        <div class="input-group">
                                                            <div class="input-group-prepend">
                                                                <span class="input-group-text">
                                                                    To
                                                                </span>
                                                            </div>
                                                            <input class="form-control" value="${to}" id="to" type="date">
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-3">
                                                        <div class="input-group">
                                                            <div class="input-group-prepend">
                                                                <span class="input-group-text">
                                                                    Account
                                                                </span>
                                                            </div>
                                                            <select id="accountName" class="form-control">
                                                                <option value="">SELECT A MATERIAL</option>
                                                                <c:forEach items="${stockTrace}" var="item">
                                                                    <c:if test = "${item.type.id <= 5}">
                                                                        <option value="${item.type.id}">${item.type.itemName}</option>
                                                                    </c:if>
                                                                    <c:if test = "${item.type.id == 6}">
                                                                        <option value="${item.type.id}:${item.subType}">
                                                                            ${item.type.itemName} (${item.subType})
                                                                        </option>
                                                                    </c:if>
                                                                </c:forEach>
                                                            </select>
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-3">
                                                        <div class="input-group">
                                                            <div class="input-group-prepend">
                                                            </div>
                                                            <button id="applyDateFilter" class="btn btn-default">Apply</button>
                                                        </div>
                                                    </div>
                                                </div>
                                                <table class="table table-responsive table-striped table-bordered">
                                                    <thead>
                                                        <tr>
                                                            <td>SR. NO.</td>
                                                            <th>DATE</th>
                                                            <th>TRANSACTION TYPE</th>
                                                            <th>CUSTOMER</th>
                                                            <th>SITE NAME</th>
                                                            <th>ASPHALT TYPE</th>
                                                            <th>ASPHALT (TON)</th>
                                                            <th>VEHICLE NO.</th>
                                                            <th>PLANT BILTY NO.</th>
                                                            <th>RECIPIENT BILTY NO.</th>
                                                            <th>TOTAL QUANTITY</th>
                                                            <th>RATE</th>
                                                            <th>Dr.</th>
                                                            <th>Cr.</th>
                                                            <th>Dr.</th>

                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:set var="q" value="0" scope="page" />
                                                        <c:set var="a" value="0" scope="page" />
                                                        <c:set var="totalDr" value="0" scope="page" />
                                                        <c:set var="totalCr" value="0" scope="page" />
                                                        <c:set var="srNum" value="1" scope="page" />

                                                        <tr>
                                                            <td>${srNum}
                                                                <c:set var="srNum" value="${srNum+1}" />
                                                            </td>
                                                            <td>
                                                                <fmt:formatDate 
                                                                    value="${openingStock.createdDate}" pattern="dd-MM-yyyy" />
                                                            </td>
                                                            <td>Opening Stock</td>  
                                                            <td></td>

                                                            <td></td>
                                                            <td></td>
                                                            <td></td>
                                                            <td></td>
                                                            <td></td>
                                                            <td></td>

                                                            <td>${openingStock.quantity}</td>
                                                            <td>${openingStock.rate}</td>
                                                            <td>${openingStock.totalPrice}</td>
                                                            <td></td>
                                                            <td></td>

                                                            <c:set var="q" value="${q+openingStock.quantity}" />
                                                            <c:set var="a" value="${a+openingStock.totalPrice}" />
                                                            <c:set var="totalDr" value="${totalDr+openingStock.totalPrice}" />
                                                        </tr>

                                                        <c:forEach items="${data}" var="item">
                                                            <tr>
                                                                <td>${srNum}
                                                                    <c:set var="srNum" value="${srNum+1}" />
                                                                </td>
                                                                <td>
                                                                    <fmt:formatDate value="${item.createdDate}" pattern="dd-MM-yyyy" />
                                                                </td>
                                                                <td>${item.subEntryType} 
                                                                    <c:if test = "${item.subType !=null}">
                                                                        (${item.subType})
                                                                    </c:if>
                                                                </td>  
                                                                <c:if test = "${item.subEntryType == 'CONSUME'}">
                                                                    <td>${item.buyer}</td>
                                                                </c:if>
                                                                <c:if test = "${item.subEntryType == 'PURCHASE'}">
                                                                    <td>${item.supplier}</td>
                                                                </c:if>
                                                                <td>${item.project}</td>
                                                                <td>${item.project}</td>
                                                                <td>${item.asphaltType}</td>
                                                                <td>${item.asphaltTon}</td>
                                                                <td>${item.plantBilty}</td>
                                                                <td>${item.recipientBilty}</td>
                                                                <td>${item.quantity}</td>
                                                                <td>${item.rate}</td>
                                                                <c:if test = "${item.subEntryType == 'PURCHASE'}">
                                                                    <td>${item.totalPrice}</td>
                                                                    <td></td>
                                                                </c:if>
                                                                <c:if test = "${item.subEntryType == 'CONSUME'}">
                                                                    <td></td>
                                                                    <td>${item.totalPrice}</td>
                                                                </c:if>
                                                                <td></td>


                                                                <c:if test = "${item.subEntryType == 'CONSUME'}">
                                                                    <c:set var="q" value="${q-item.quantity}" />
                                                                    <c:set var="a" value="${a-item.totalPrice}" />
                                                                    <c:set var="totalCr" value="${totalCr+item.totalPrice}" />
                                                                </c:if>
                                                                <c:if test = "${item.subEntryType == 'PURCHASE'}">
                                                                    <c:set var="q" value="${q+item.quantity}" />
                                                                    <c:set var="a" value="${a+item.totalPrice}" />
                                                                    <c:set var="totalDr" value="${totalDr+item.totalPrice}" />
                                                                </c:if>

                                                            </tr>
                                                        </c:forEach>
                                                        <tr>
                                                            <td colspan="15">
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td colspan="10">Closing Stock</td>
                                                            <td>${q}</td>
                                                            <td></td>    
                                                            <td>${totalDr}</td>
                                                            <td>${totalCr}</td>
                                                            <td>${totalDr-totalCr}</td>

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

    <script>
        $("#applyDateFilter").click(function () {
            var from = $("#from").val();
            var to = $("#to").val();
            var accountName = $("#accountName").val();
            if (from.length > 0 || to.length > 0 && accountName.length > 0) {
                console.log(from + " -> " + to);
                console.log(accountName)
                if (accountName.includes(":")) {
                    itemId = accountName.split(":")[0];
                    console.log(itemId)
                    itemSubType = accountName.split(":")[1];
                    console.log(itemSubType)

                    window.location = ctx + "/report?type=consumptionAndReceiving&from=" + from + "&to=" + to + "&eitem=" + itemId + "&subType=" + itemSubType;
                } else {
                    window.location = ctx + "/report?type=consumptionAndReceiving&from=" + from + "&to=" + to + "&eitem=" + accountName;

                }
            }
        });
    </script>

</html>

