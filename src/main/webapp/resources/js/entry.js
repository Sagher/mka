/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var entryItemsList;

/*
 * DIRECT ENTRIES JS 
 */
var subItemTypeDiv = $("#subItemTypeDiv");
var quantityDiv = $("#quantityDiv");
var rateDiv = $("#rateDiv");

var ditemTypeSelection = $("#dItemType");
var dItemSubTypeSelection = $("#dItemSubType");
var entryTypeSelection = $("#dEntryType");

var customerBuyer = $("#dcusbuy");
var supplier = $("#dsupplier");
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
    console.log(ditemTypeSelection.val());
    subItemTypeDiv.attr("hidden", "true");
    dItemSubTypeSelection.html("");
    entryTypeSelection.html("");
    if (ditemTypeSelection.val() !== "" || ditemTypeSelection.val().length > 0) {
        for (var i in entryItemsList) {
            if (entryItemsList[i].id === parseInt(ditemTypeSelection.val(), 10)) {

                // show subItemTypeDiv if there is further type
                if (entryItemsList[i].itemType !== null) {
                    console.log(entryItemsList[i].itemType);
                    if (entryItemsList[i].itemType.includes(";")) {
                        // there multiple entry type, user must chose one
                        var itemType = entryItemsList[i].itemType.split(";");
                        for (var j in itemType) {
//                        console.log(entryTypes[j])
                            dItemSubTypeSelection.append($("<option />").val(itemType[j]).text(itemType[j]));
                        }
                        subItemTypeDiv.removeAttr("hidden");
                    }
                }

                // there multiple entry type, user must chose one
                if (entryItemsList[i].subEntryType.includes(";")) {
                    var entryTypes = entryItemsList[i].subEntryType.split(";");
                    for (var j in entryTypes) {
//                        console.log(entryTypes[j])
                        entryTypeSelection.append($("<option />").val(entryTypes[j]).text(entryTypes[j]));
                    }
//                    entryTypeSelection.attr("size", entryTypes.length)
                } else {
                    // there is only one entry type
                    entryTypeSelection.append($("<option />").val(entryItemsList[i].entryType).text(entryItemsList[i].entryType));

                }
            }
        }
    } else {
        entryTypeSelection.removeAttr("size");

    }
});

// attach change event listener to rate and quantity to calculate amount select box
$("#drate, #dquantity").change(function () {
    if (quantity.val().length > 0 && rate.val().length > 0) {
        var am = quantity.val() * rate.val();
        console.log(am)
        amount.val(am);
    } else {

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