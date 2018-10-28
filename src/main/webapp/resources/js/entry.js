/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var entryItemsList, customersBuyersList;

/*
 * DIRECT ENTRIES JS 
 */
var subItemTypeDiv = $("#subItemTypeDiv");
var quantityDiv = $("#quantityDiv");
var rateDiv = $("#rateDiv");

var ditemTypeSelection = $("#dItemType");
var dItemSubTypeSelection = $("#dItemSubType");
var entryTypeSelection = $("#dEntryType");

var customerBuyerSelect = $("#dbuysupSelect");
var customerBuyerInput = $("#dbuysupInput");
var quantity = $("#dquantity");
var rate = $("#drate");
var amount = $("#damount");
var advance = $("#dadvance");
var dateOfEntry = $("#doe");


$(document).ready(function () {
    $.ajax({
        url: "getEntryItemsList",
        dataType: "json",
        success: function (data) {
            entryItemsList = data;
//            console.log(entryItemsList);
            for (var i in entryItemsList) {
                console.log(entryItemsList[i])
                if (entryItemsList[i].entryType === 'DIRECT') {
                    ditemTypeSelection.append($("<option />").val(entryItemsList[i].id).text(entryItemsList[i].itemName));
                } else {
                    iItemTypeSelection.append($("<option />").val(entryItemsList[i].id).text(entryItemsList[i].itemName));
                }
            }
            //enable custom/new type from indirect entries tab
            iItemTypeSelection.append($("<option />").val('Other').text('Other'));

        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log(textStatus);
        }
    });
    $.ajax({
        url: "customersAndBuyers",
        dataType: "json",
        success: function (data) {
            customersBuyersList = data;
            for (var i in customersBuyersList) {
                console.log(customersBuyersList[i])
                customerBuyerSelect.append($("<option />").val(customersBuyersList[i].id).text(customersBuyersList[i].name));
            }
            customerBuyerSelect.append($("<option />").val('Other').text('Other'));
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log(textStatus);
        }
    });
    dateOfEntry.val(new Date().toDateInputValue());
    idateOfEntry.val(new Date().toDateInputValue());
});

// attach change event listener to 'itemType' select box
ditemTypeSelection.change(function () {
    var selectItemId = ditemTypeSelection.val();
    console.log(selectItemId);
    subItemTypeDiv.attr("hidden", "true");
    dItemSubTypeSelection.html("");
    entryTypeSelection.html("");
    if (selectItemId !== "" || selectItemId.length > 0) {
        entryTypeSelection.removeAttr("hidden");
        $("#buysupDiv").removeAttr("hidden");
        for (var i in entryItemsList) {
            if (entryItemsList[i].id === parseInt(selectItemId, 10)) {

                // show add carriage button for crush
                if (selectItemId == 6) {
                    console.log("show add carriage button for crush")
                    $("#addCarriageBtn").removeAttr("disabled");
                } else {
                    $("#addCarriageBtn").attr("disabled", "true");
                }

                // show subItemTypeDiv if there is further type
                if (entryItemsList[i].itemType !== null) {
                    console.log(entryItemsList[i].itemType);
                    if (entryItemsList[i].itemType != null && entryItemsList[i].itemType.includes(";")) {
                        // there multiple entry type, user must chose one
                        var itemType = entryItemsList[i].itemType.split(";");
                        for (var j in itemType) {
                            dItemSubTypeSelection.append($("<option />").val(itemType[j]).text(itemType[j]));
                        }
                        subItemTypeDiv.removeAttr("hidden");
                    }
                }

                // there multiple entry type, user must chose one
                if (entryItemsList[i].subEntryType.includes(";")) {
                    var entryTypes = entryItemsList[i].subEntryType.split(";");
                    for (var j in entryTypes) {
                        entryTypeSelection.append($("<option />").val(entryTypes[j]).text(entryTypes[j]));
                    }
                } else {
                    // there is only one entry type
                    entryTypeSelection.append($("<option />").val(entryItemsList[i].entryType).text(entryItemsList[i].entryType));
                }
                $("#supBuyDiv").removeAttr("hidden");
                $("#entryTypeDiv").removeAttr("hidden");

            }
        }

        // populate stock div
        $.get("entries/stockTrace?entryTypeId=" + selectItemId, function (data) {
            if (data !== undefined) {
                $("#stockDetailDiv").removeAttr("hidden");
                $("#stockDetail").html('<div class="brand-card-header text-white bg-info">\n\
                    <div class="card-body">\n\
                    <div class="item-name">\n\
                    <strong>' + data.itemName + '</strong></div>\n\
                    <div>Total Sales: ' + data.salesUnit + ' ' + data.itemUnit + '</div>\n\
                    <div>Total Sales Amount: ' + data.salesAmount + ' PKR</div>\n\
                    </div></div>\n\
                    <div class="brand-card-body"><div>\n\
                    <div class="text-value">' + data.stockUnits + ' ' + data.itemUnit + '</div>\n\
                    <div class="text-uppercase text-muted small">In Stock</div>\n\
                    </div>\n\
                    <div>\n\
                    <div class="text-value">' + data.stockAmount + ' PKR</div>\n\
                    <div class="text-uppercase text-muted small">Total Stock Price</div>\n\
                    </div>\n\
                    <div>\n\
                    <div class="text-value">' + data.averageUnitPrice + ' PKR</div>\n\
                    <div class="text-uppercase text-muted small">Average Per ' + ' ' + data.itemUnit + ' Price</div>\n\
                    </div>\n\
                    </div>');
            }
            console.log(data)
        });
    } else {
        $("#stockDetailDiv").attr("hidden", "true");
        entryTypeSelection.attr("hidden", "true");
        $("#buysupDiv").attr("hidden", "true");

    }
});

function addCrushCarriage() {
    console.log("POPUP");
    $('#crushCarriageModal').modal('show');
}

function addCarriage() {
    $('#crushCarriageModal').modal('hide');
    console.log($("#unloadedCrush").val());
    console.log($("#unloadingCost").val());

    $("#dentryForm .unloadedCrush").val($("#unloadedCrush").val());
    $("#dentryForm .unloadingCost").val($("#unloadingCost").val());

}

// attach change event listener to 'dEntryType' select box
//entryTypeSelection.change(function () {
//    var selectEntryType = entryTypeSelection.val();
//    console.log(selectEntryType);
//    $("#supBuyDiv").removeAttr("hidden");
//    if (selectEntryType === 'SALE') {
////        $(".buysup-label").html("Name Of Buyer");
//    } else {
////        $(".buysup-label").html("Name Of Supplier");
//
//    }
//
//});

// attach change event listener to rate and quantity to calculate amount select box
$("#drate, #dquantity").change(function () {
    if (quantity.val().length > 0 && rate.val().length > 0) {
        var am = quantity.val() * rate.val();
        console.log(am)
        amount.val(am);
    } else {

    }
});

//attach change event to customer/buyer and supplier select
customerBuyerSelect.change(function () {
    if ($('#dbuysupSelect option:selected').text() == "Other") {
        customerBuyerInput.removeAttr("hidden");
    } else {
        customerBuyerInput.attr("hidden", "true");
    }
});

// current date
Date.prototype.toDateInputValue = (function () {
    var local = new Date(this);
    local.setMinutes(this.getMinutes() - this.getTimezoneOffset());
    return local.toJSON().slice(0, 10);
});

function resetdForm() {
//    location.reload();
    document.getElementById("dentryForm").reset();
    entryTypeSelection.html("");
    dItemSubTypeSelection.html("");
    dateOfEntry.val(new Date().toDateInputValue());
    subItemTypeDiv.attr("hidden", "true");
}

function logdEntry() {
    $('#dentryForm').ajaxForm({
        beforeSend: function () {
            $("#logdEntryBtn").hide();
        },
        uploadProgress: function (event, position, total, percentComplete) {
        },
        complete: function (xhr) {
            response = xhr.responseText.split(":");
            console.log(response);
            respCode = response[0];
            respMessage = response[1];
            if (respCode === '00') {
                noty({
                    text: respMessage,
                    type: "success", layout: "center", timeout: 4000
                });
                resetdForm();
                $("#logdEntryBtn").show();

                setTimeout(function () {
                    location.reload();
                }, 2000);

            } else {
                noty({
                    text: respMessage,
                    type: "error", layout: "center", timeout: 4000
                });
                $("#logdEntryBtn").show();
            }

        }
    });
}

/*
 * IN-DIRECT ENTRIES JS
 */

var iItemTypeSelection = $("#iItemType");
var iItemSubTypeSelection = $("#iItemSubType");

var icustomerBuyer = $("#icusbuy");
var isupplier = $("#isupplier");
var iquantity = $("#iquantity");
var irate = $("#irate");
var iamount = $("#iamount");
var iadvance = $("#iadvance");
var idateOfEntry = $("#idoe");

function resetiForm() {
//    location.reload();
    document.getElementById("ientryForm").reset();
}

iItemTypeSelection.change(function () {
    console.log(iItemTypeSelection.val());
    $("#isubItemTypeDiv").attr("hidden", "true");
    entryTypeSelection.html("");
    if (iItemTypeSelection.val() !== "" || iItemTypeSelection.val().length > 0) {
        for (var i in entryItemsList) {
            if (entryItemsList[i].id === parseInt(iItemTypeSelection.val(), 10)) {

                // show subItemTypeDiv if there is further type
                if (entryItemsList[i].itemType !== null) {
                    console.log(entryItemsList[i].itemType);
                    if (entryItemsList[i].itemType.includes(";")) {
                        // there multiple entry type, user must chose one
                        var itemType = entryItemsList[i].itemType.split(";");
                        for (var j in itemType) {
//                        console.log(entryTypes[j])
                            iItemSubTypeSelection.append($("<option />").val(itemType[j]).text(itemType[j]));
                        }
                        $("#isubItemTypeDiv").removeAttr("hidden");
                    }
                }

            }
        }
    } else {
        entryTypeSelection.removeAttr("size");

    }
});
var iItemTypeInput = $("#iItemTypeInput");
iItemTypeSelection.change(function () {
    if ($('#iItemType option:selected').text() == "Other") {
        iItemTypeInput.removeAttr("hidden");
        iItemTypeInput.attr("required", "true");
    } else {
        iItemTypeInput.attr("hidden", "true");
        iItemTypeInput.removeAttr("required");

    }
});

function logiEntry() {
    $('#ientryForm').ajaxForm({
        beforeSend: function () {
            $("#logiEntryBtn").hide();
        },
        uploadProgress: function (event, position, total, percentComplete) {
        },
        complete: function (xhr) {
            response = xhr.responseText.split(":");
            console.log(response);
            respCode = response[0];
            respMessage = response[1];
            if (respCode === '00') {
                noty({
                    text: respMessage,
                    type: "success", layout: "center", timeout: 4000
                });
                resetdForm();
                $("#logiEntryBtn").show();

                setTimeout(function () {
                    location.reload();
                }, 5000);

            } else {
                noty({
                    text: respMessage,
                    type: "error", layout: "center", timeout: 4000
                });
                $("#logiEntryBtn").show();
            }

        }
    });
}

function logCashTransaction() {
    $('#cashTranForm').ajaxForm({
        beforeSend: function () {
            $("#cashTranBtn").hide();
        },
        uploadProgress: function (event, position, total, percentComplete) {
        },
        complete: function (xhr) {
            response = xhr.responseText.split(":");
            console.log(response);
            respCode = response[0];
            respMessage = response[1];
            if (respCode === '00') {
                noty({
                    text: respMessage,
                    type: "success", layout: "center", timeout: 4000
                });
                resetdForm();
                $("#cashTranBtn").show();

                setTimeout(function () {
                    location.reload();
                }, 5000);

            } else {
                noty({
                    text: respMessage,
                    type: "error", layout: "center", timeout: 4000
                });
                $("#cashTranBtn").show();
            }

        }
    });
}