/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var entryItemsList;

var subItemTypeDiv = $("#subItemTypeDiv");
var quantityDiv = $("#quantityDiv");
var rateDiv = $("#rateDiv");

var itemTypeSelection = $("#itemType");
var subItemTypeSelection = $("#subItemType");
var entryTypeSelection = $("#entryType");
var supplier = $("#supplier");
var quantity = $("#quantity");
var rate = $("#rate");
var amount = $("#amount");
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
                itemTypeSelection.append($("<option />").val(entryItemsList[i].itemName).text(entryItemsList[i].itemName));
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log(textStatus);
        }
    });

    dateOfEntry.val(new Date().toDateInputValue());
});

// attach change event listener to 'itemType' select box
itemTypeSelection.change(function () {
    console.log(itemTypeSelection.val());
    entryTypeSelection.html("");
    subItemTypeSelection.html("");
    subItemTypeDiv.attr("hidden", "true");
    entryTypeSelection.removeAttr("disabled");
    entryTypeSelection.removeAttr("size");
    quantityDiv.removeAttr("hidden");
    rateDiv.removeAttr("hidden");

    if (itemTypeSelection.val() !== "" || itemTypeSelection.val().length > 0) {
        for (var i in entryItemsList) {
            if (entryItemsList[i].itemName === itemTypeSelection.val()) {

                // show subItemTypeDiv if there is further type
                if (entryItemsList[i].itemType !== null) {
                    console.log(entryItemsList[i].itemType);
                    subItemTypeDiv.removeAttr("hidden");
                    if (entryItemsList[i].itemType.includes(";")) {
                        // there multiple entry type, user must chose one
                        var itemType = entryItemsList[i].itemType.split(";");
                        for (var j in itemType) {
//                        console.log(entryTypes[j])
                            subItemTypeSelection.append($("<option />").val(itemType[j]).text(itemType[j]));
                        }
                    } else {
                        // there is only one entry type
                        subItemTypeSelection.append($("<option />").val(entryItemsList[i].itemType).text(entryItemsList[i].itemType));
                        subItemTypeSelection.attr("disabled", "true");
                        subItemTypeDiv.attr("hidden", "true");
                    }
                }

                // there multiple entry type, user must chose one
                if (entryItemsList[i].entryType.includes(";")) {
                    var entryTypes = entryItemsList[i].entryType.split(";");
                    for (var j in entryTypes) {
//                        console.log(entryTypes[j])
                        entryTypeSelection.append($("<option />").val(entryTypes[j]).text(entryTypes[j]));
                    }
                    entryTypeSelection.attr("size", entryTypes.length)
                } else {
                    // there is only one entry type
                    entryTypeSelection.append($("<option />").val(entryItemsList[i].entryType).text(entryItemsList[i].entryType));
                    entryTypeSelection.attr("readonly", "true");
                    quantityDiv.attr("hidden", "true");
                    rateDiv.attr("hidden", "true");

                }
            }
        }
    } else {
        entryTypeSelection.attr("disabled", "true");
        entryTypeSelection.removeAttr("size");

    }
});

// attach change event listener to rate and quantity to calculate amount select box
$("#rate, #quantity").change(function () {
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

function resetForm() {
    document.getElementById("entryForm").reset();
    entryTypeSelection.html("");
    subItemTypeSelection.html("");
    entryTypeSelection.attr("disabled", "true");
    entryTypeSelection.removeAttr("size");
    dateOfEntry.val(new Date().toDateInputValue());
    subItemTypeDiv.attr("hidden", "true");
    quantityDiv.removeAttr("hidden");
    rateDiv.removeAttr("hidden");
}

function logEntry() {
    $('#entryForm').ajaxForm({
        beforeSend: function () {
            $("#logEntryBtn").hide();
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
                resetForm();
                $("#logEntryBtn").show();

//                setTimeout(function () {
//                    location.reload();
//                }, 2000);

            } else {
                noty({
                    text: respMessage,
                    type: "error", layout: "center", timeout: 4000
                });
                $("#logEntryBtn").show();
            }

        }
    });
}