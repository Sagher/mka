/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var entryItemsList, customersBuyersList, projectsList;
var stockData;
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
var projectSelect = $("#projSelect");
var customerBuyerInput = $("#dbuysupInput");
var projectInput = $("#dproject");
var dcPartySelect = $("#dcParty");
var dcPartyInput = $("#dcPartyInput");
var quantity = $("#dquantity");
var rate = $("#drate");
var amount = $("#damount");
var advance = $("#dadvance");
var dateOfEntry = $("#doe");
var cashDateOfEntry = $("#cdoe");

$(document).ready(function () {
    $.ajax({
        url: "getEntryItemsList",
        dataType: "json",
        success: function (data) {
            entryItemsList = data;
//            console.log(entryItemsList);
            for (var i in entryItemsList) {
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
                customerBuyerSelect.append($("<option />").val(customersBuyersList[i].name).text(customersBuyersList[i].name));
                tCustomerBuyerSelect.append($("<option />").val(customersBuyersList[i].name).text(customersBuyersList[i].name));
                iname.append($("<option />").val(customersBuyersList[i].name).text(customersBuyersList[i].name));
                asCusSelect.append($("<option />").val(customersBuyersList[i].name).text(customersBuyersList[i].name));
                dcPartySelect.append($("<option />").val(customersBuyersList[i].name).text(customersBuyersList[i].name));
                assCarProviderSelect.append($("<option />").val(customersBuyersList[i].name).text(customersBuyersList[i].name));
                assLayerSelect.append($("<option />").val(customersBuyersList[i].name).text(customersBuyersList[i].name));

            }
            customerBuyerSelect.append($("<option />").val('Other').text('Other'));
            tCustomerBuyerSelect.append($("<option />").val('Other').text('Other'));
            iname.append($("<option />").val('Other').text('Other'));
            asCusSelect.append($("<option />").val('Other').text('Other'));
            dcPartySelect.append($("<option />").val('Other').text('Other'));
            assCarProviderSelect.append($("<option />").val('Other').text('Other'));
            assLayerSelect.append($("<option />").val('Other').text('Other'));

        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log(textStatus);
        }
    });
    $.ajax({
        url: "projects",
        dataType: "json",
        success: function (data) {
            projectsList = data;
            for (var i in projectsList) {
                projectSelect.append($("<option />").val(projectsList[i].name).text(projectsList[i].name));
                tProjSelect.append($("<option />").val(projectsList[i].name).text(projectsList[i].name));
                asProjSelect.append($("<option />").val(projectsList[i].name).text(projectsList[i].name));

            }
            projectSelect.append($("<option />").val('Other').text('Other'));
            tProjSelect.append($("<option />").val('Other').text('Other'));
            asProjSelect.append($("<option />").val('Other').text('Other'));

        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log(textStatus);
        }
    });
    dateOfEntry.val(new Date().toDateInputValue());
    idateOfEntry.val(new Date().toDateInputValue());
    cashDateOfEntry.val(new Date().toDateInputValue());

    dateOfEntry.attr("max", new Date().toDateInputValue());
    idateOfEntry.attr("max", new Date().toDateInputValue());
    cashDateOfEntry.attr("max", new Date().toDateInputValue());

    var firstDay = new Date(new Date().getFullYear(), new Date().getMonth(), 1);

    dateOfEntry.attr("min", firstDay.toDateInputValue());
    idateOfEntry.attr("min", firstDay.toDateInputValue());
    cashDateOfEntry.attr("min", firstDay.toDateInputValue());

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
                    $("#addCarriageDiv").removeAttr("hidden");
                } else {
                    $("#addCarriageDiv").attr("hidden", "true");
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
        $.get("entries/stockTrace?entryTypeId=" + selectItemId + "&subType=" + dItemSubTypeSelection.val(), function (data) {
            if (data !== undefined) {
                stockData = data;
                $("#stockDetailDiv").removeAttr("hidden");
                var html = '';
                html += '<div class="brand-card-header text-white bg-info">\n\
                    <div class="card-body">\n\
                    <div class="item-name">';
                if (data.subType !== null) {
                    html += ('<strong>' + data.itemName + '</strong> <small>' + data.subType + '</small></div>');
                } else {
                    html += ('<strong>' + data.itemName + '</strong></div>');
                }

                if (data.itemId == 4 || data.itemId == 5) {
                    // prime coat tack coat drums
                    html += ('<div>Total Sales: ' + data.salesUnit + ' ' + data.itemUnit + '</div>\n\
                    <div>Total Sales Amount: ' + data.salesAmount + ' PKR</div>\n\
                    </div></div>\n\
                    <div class="brand-card-body"><div>');

                    if (data.stockUnits > 0) {
                        html += ('<div class="text-value">' + data.stockUnits + ' ' + data.itemUnit
                                + ' <small>[ ' + parseFloat((parseInt(data.stockUnits) / 218)).toFixed(2) + ' Drum(s) ]</small></div>');
                    } else {
                        html += ('<div class="text-value">' + data.stockUnits + ' ' + data.itemUnit + '</div>');

                    }

                    html += ('<div class="text-uppercase text-muted small">In Stock</div>\n\
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
                } else {
                    html += ('<div>Total Sales: ' + data.salesUnit + ' ' + data.itemUnit + '</div>\n\
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
                $("#pup").html("Per " + data.itemUnit + " Price");
                $("#stockDetail").html(html);
            }
        });
    } else {
        $("#stockDetailDiv").attr("hidden", "true");
        entryTypeSelection.attr("hidden", "true");
        $("#buysupDiv").attr("hidden", "true");
    }
});
dItemSubTypeSelection.change(function () {
    $.get("entries/stockTrace?entryTypeId=" + ditemTypeSelection.val() + "&subType=" + dItemSubTypeSelection.val(), function (data) {
        if (data !== undefined) {
            stockData = data;
            $("#stockDetailDiv").removeAttr("hidden");
            var html = '';
            html += '<div class="brand-card-header text-white bg-info">\n\
                    <div class="card-body">\n\
                    <div class="item-name">';
            if (data.subType !== null) {
                html += ('<strong>' + data.itemName + '</strong> <small>' + data.subType + '</small></div>');
            } else {
                html += ('<strong>' + data.itemName + '</strong></div>');
            }
            html += ('<div>Total Sales: ' + data.salesUnit + ' ' + data.itemUnit + '</div>\n\
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
            $("#pup").html("Per " + data.itemUnit + " Price");

            $("#stockDetail").html(html);
        }
    });
});

entryTypeSelection.change(function () {
    var type = entryTypeSelection.val();
    if (type === 'CONSUME') {
        console.log('consumate');
        $("#damount").attr("readonly", "true");
        $("#dadvanceDiv").attr("hidden", "true");
        $("#drate").attr("readonly", "true");
        $("#drate").val(stockData.averageUnitPrice);
        $("#supBuyDiv").attr("hidden", "true");
        $("#dbuysupSelect").removeAttr("required");
        $("#dtpLabel").html("Total Cost");
        $("#directPayFromDiv").attr("hidden", "true");
        projectSelect.removeAttr("required");
        var selectItemId = ditemTypeSelection.val();
        if (selectItemId == '4' || selectItemId == '5') {
            // pc,tc
            projectSelect.attr("required", "true");
        }
    } else {
        $("#damount").removeAttr("readonly");
        $("#dadvanceDiv").removeAttr("hidden");
        $("#drate").removeAttr("readonly");
        $("#supBuyDiv").removeAttr("hidden");
        $("#dbuysupSelect").attr("required", "true");
        $("#dtpLabel").html("Total Price");
        $("#directPayFromDiv").removeAttr("hidden");
        projectSelect.removeAttr("required");

    }
});

function addCrushCarriage() {
    $("#crushCarriageForm").removeAttr("hidden");
    $("#addCarriageDiv").attr("hidden", "true");
}


// attach change event listener to rate and quantity to calculate amount select box
$("#drate, #dquantity").change(function () {
    if (quantity.val().length > 0 || rate.val().length > 0) {
        var am = quantity.val() * rate.val();
        amount.val(am);
    } else {

    }
});

// attach change event listener to unloading rate and quantity 
$("#unloadedCrush, #unloadingCost").change(function () {
    if ($("#unloadedCrush").val().length > 0 || $("#unloadingCost").val().length > 0) {
        var am = $("#unloadedCrush").val() * $("#unloadingCost").val();
        $("#totalUnloadingCost").val(am);
    } else {

    }
});
//attach change event to carriage contractor in direct entry
dcPartySelect.change(function () {
    if ($('#dcParty option:selected').text() == "Other") {
        dcPartyInput.removeAttr("hidden");
        dcPartyInput.attr("required", "true");
    } else {
        dcPartyInput.attr("hidden", "true");
        dcPartyInput.removeAttr("required");
    }
});

//attach change event to customer/buyer and supplier select
customerBuyerSelect.change(function () {
    if ($('#dbuysupSelect option:selected').text() == "Other") {
        customerBuyerInput.removeAttr("hidden");
        customerBuyerInput.attr("required", "true");
    } else {
        customerBuyerInput.attr("hidden", "true");
        customerBuyerInput.removeAttr("required");
    }
});
//attach change event to project select
projectSelect.change(function () {
    if ($('#projSelect option:selected').text() == "Other") {
        projectInput.removeAttr("hidden");
        projectInput.attr("required", "true");
    } else {
        projectInput.removeAttr("required");
        projectInput.attr("hidden", "true");
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
                    type: "success", layout: "top", timeout: 4000
                });
                resetdForm();
                $("#logdEntryBtn").show();
                setTimeout(function () {
                    location.reload();
                }, 2000);
            } else {
                noty({
                    text: respMessage,
                    type: "error", layout: "top", timeout: 4000
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

var iname = $("#iname");

//attach change event to customer/buyer and supplier select
iname.change(function () {
    if ($('#iname option:selected').text() == "Other") {
        $("#ibuysupInput").removeAttr("hidden");
        $("#ibuysupInput").attr("required", "true");
    } else {
        $("#ibuysupInput").attr("hidden", "true");
        $("#ibuysupInput").removeAttr("required");
    }
});

function toggleIndirectFullyPayed() {
    var checkBox = document.getElementById("indirectFullyPayed");
    if (checkBox.checked == true) {
        $("#iadvance").val($("#icost").val());
        $("#iadvance").attr("readonly", "true");
    } else {
        $("#iadvance").removeAttr("readonly");

    }

}

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
                    type: "success", layout: "top", timeout: 4000
                });
                resetdForm();
                $("#logiEntryBtn").show();
                setTimeout(function () {
                    location.reload();
                }, 5000);
            } else {
                noty({
                    text: respMessage,
                    type: "error", layout: "top", timeout: 4000
                });
                $("#logiEntryBtn").show();
            }

        }
    });
}
/*
 * 
 * CASH TRAN
 * 
 */
var ttype = $("#ttype");
var tCustomerBuyerSelect = $("#tbuysupSelect");
var tCusByInput = $("#tbuysupInput");
var tProjSelect = $("#tprojSelect");
var tprojectInput = $("#tproject");
var cashInHandOptionsDiv = $("#cashInHandOptionsDiv");

//attach change event to customer/buyer and supplier select
tCustomerBuyerSelect.change(function () {
    if ($('#tbuysupSelect option:selected').text() == "Other") {
        tCusByInput.removeAttr("hidden");
        tCusByInput.attr("required", "true");
    } else {
        tCusByInput.attr("hidden", "true");
        tCusByInput.removeAttr("required");
    }
});
//
//attach change event to project select
ttype.change(function () {
    if ($('#ttype option:selected').val() == "+") {
        $("#toHoOptions").removeAttr("hidden");
        $("#fromHoOptions").attr("hidden", "true");
    } else {
        $("#fromHoOptions").removeAttr("hidden");
        $("#toHoOptions").attr("hidden", "true");
    }
});

function togglePayeeDiv(value) {
    if (value === 0) {
        $("cashTranPayeeDiv").removeAttr("hidden");
        $("#tbuysupSelect").attr("required", "true")

    } else {
        $("#cashTranPayeeDiv").attr("hidden", "true");
        $("#tbuysupSelect").removeAttr("required")
    }
}
//function changeCashTranCusLabel(value) {
//    if (value === 0) {
//        $("#tPayeeLabel").html("Customer");
//        $("#cashTranPayeeDiv").html('<span class="help-block" id="tPayeeLabel">Payee</span><select class="form-control" id="tbuysupSelect" name="tpayer" required="true"><option selected value="">-- Please select An Option --</option></select><br><input hidden="true" class="form-control" id="tbuysupInput" type="text" name="tbuysupInput" placeholder="Custom Buyer/Supplier">');
//        for (var i in customersBuyersList) {
//            $("#tbuysupSelect").append($("<option />").val(customersBuyersList[i].name).text(customersBuyersList[i].name));
//        }
//        $("#tbuysupSelect").append($("<option />").val('Other').text('Other'));
//    } else {
//        $("#tPayeeLabel").html("Payee");
//        $("#cashTranPayeeDiv").html('<span class="help-block" id="tPayeeLabel">Payee</span><select class="form-control" style="display:none" id="tbuysupSelect" name="tpayer" required="true"><option selected value="Other">Other</option></select><input readonly="true" class="form-control" id="tbuysupInput" type="text" name="tbuysupInput" value="HeadOffice">');
//
//    }
//}

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
                    type: "success", layout: "top", timeout: 4000
                });
                resetdForm();
                $("#cashTranBtn").show();
                setTimeout(function () {
                    location.reload();
                }, 5000);
            } else {
                noty({
                    text: respMessage,
                    type: "error", layout: "top", timeout: 4000
                });
                $("#cashTranBtn").show();
            }

        }
    });
}


/*
 * 
 * ASPHALT SALE
 * 
 */

var asCusSelect = $("#asCusSelect");
var asCusInput = $("#asCusInput");
var asProjSelect = $("#asProjSelect");
var asProjInput = $("#asProjInput");
var assCarProviderSelect = $("#assCarProvider");
var assCarProviderInput = $("#assCarProviderInput");
var assLayerSelect = $("#assLayer");
var assLayerInput = $("#assLayerInput");

//attach change event to customer/buyer and supplier select
asCusSelect.change(function () {
    if ($('#asCusSelect option:selected').text() == "Other") {
        asCusInput.removeAttr("hidden");
        asCusInput.attr("required", "true");
    } else {
        asCusInput.attr("hidden", "true");
        asCusInput.removeAttr("required");
    }
});
//attach change event to project select
asProjSelect.change(function () {
    if ($('#asProjSelect option:selected').text() == "Other") {
        asProjInput.removeAttr("hidden");
        asProjInput.attr("required", "true");
    } else {
        asProjInput.removeAttr("required");
        asProjInput.attr("hidden", "true");
    }
});
assCarProviderSelect.change(function () {
    if ($('#assCarProvider option:selected').text() == "Other") {
        assCarProviderInput.removeAttr("hidden");
        assCarProviderInput.attr("required", "true");
    } else {
        assCarProviderInput.attr("hidden", "true");
        assCarProviderInput.removeAttr("required");
    }
});
assLayerSelect.change(function () {
    if ($('#assLayer option:selected').text() == "Other") {
        assLayerInput.removeAttr("hidden");
        assLayerInput.attr("required", "true");
    } else {
        assLayerInput.attr("hidden", "true");
        assLayerInput.removeAttr("required");
    }
});
$("#assLayingCostPerTon").change(function () {
    if ($("#assLayingCostPerTon").val().length > 0) {
        var am = $("#assLayingCostPerTon").val() * $("#tass").val();
        $("#totalAssLayingCost").val(am);
    }
    updateTotalAssCostAndPerTonAssRate();
});

$("#assCarCostPerTon").change(function () {
    if ($("#assCarCostPerTon").val().length > 0) {
        var am = $("#assCarCostPerTon").val() * $("#tass").val();
        $("#totalAssCarCost").val(am);
    }
    updateTotalAssCostAndPerTonAssRate();
});

$("#expRate").change(function () {
    if ($("#expRate").val().length > 0) {
        var am = $("#expRate").val() * $("#tass").val();
        $("#expCost").val(am);
    }
    updateTotalAssCostAndPerTonAssRate();
});

function updateTotalAssCostAndPerTonAssRate() {
    var expCost = $("#expCost").val();
    var tAssCarCost = $("#totalAssCarCost").val();
    var tAssLayingCost = $("#totalAssLayingCost").val();
    var totalSaleCost = parseFloat(expCost) + parseFloat(tAssCarCost) + parseFloat(tAssLayingCost);
    console.log(totalSaleCost);
    $("#assSaleCost").val(totalSaleCost);
    $("#assSaleRate").val(totalSaleCost / parseFloat($("#tass").val()));
}


function updateAssRate(itemName, value, avgRate) {
    console.log(itemName + ", " + value + ", " + avgRate);
    var item = itemName.split("~")[0];
    var totalQuantity = parseInt($("#tass").val()) * value;
    var qId = "#" + item + "quantity";
    $(qId).val(totalQuantity);
    var costId = "#" + item + "cost";
    var itemCost = totalQuantity * Math.ceil(avgRate);
    $(costId).val(itemCost);
}

function updateAssBitRate(itemName, value, avgRate) {
    console.log(itemName + ", " + value + ", " + avgRate);
    var item = itemName.split("~")[0];
    value = value / 1000;
    var totalQuantity = parseInt($("#tass").val()) * value;
    var qId = "#" + item + "quantity";
    $(qId).val(totalQuantity);
    var costId = "#" + item + "cost";
    var itemCost = totalQuantity * Math.ceil(avgRate);
    $(costId).val(itemCost);

}

function getAutoFillValues() {
    var ass = $("#asCusSelect").val();
    var proj = $("#asProjSelect").val();
    console.log(ass)
    console.log(proj)

    if (ass != '' && proj != '') {
        getAutoFillers(ass, proj);
    }
}

function getAutoFillers(cusBuy, proj) {
    $.ajax({
        url: "previousAssValues?buyerSupplier=" + cusBuy + "&project=" + proj,
        dataType: "json",
        success: function (data) {
            for (var i in data) {
//                console.log(data[i]);
                if (data[i].id === 0) {
                    console.log(data[i]);
                    var vehicle = data[i].itemName;
                    var biltee = data[i].itemQuantity;
                    var explantRate = data[i].itemRate;

                    $("#biltee").val(biltee);
                    $("#vehicle").val(vehicle);
                    $("#expRate").val(explantRate).trigger('change');
                }
                if (data[i].itemName.startsWith('LAYED BY ')) {
                    var layer = data[i].itemName.substring(9, data[i].itemName.length);
                    $("#assLayer").val(layer);
                    $("#assLayingCostPerTon").val(data[i].itemRate).trigger('change');
                    ;

                }
                if (data[i].itemName.startsWith('Carriage Provided By ')) {
                    var layer = data[i].itemName.substring(21, data[i].itemName.length);
                    $("#assCarProvider").val(layer);
                    $("#assCarCostPerTon").val(data[i].itemRate).trigger('change');
                    ;
                }
                if (data[i].itemName === 'BITUMEN') {
                    var bitumenRateDynamicId = $("#bitumenRateDynamicId").val();
                    $("#" + bitumenRateDynamicId).val(data[i].itemRate).trigger('change');
                }
                if (data[i].itemName.startsWith("CRUSH")) {
                    var cId = data[i].itemName + "RateDynamicId";
                    console.log(cId)
                    var crushRateDynamicId = document.getElementById(cId).value;
                    console.log(crushRateDynamicId)
                    $("#" + crushRateDynamicId).val(data[i].itemRate).trigger('change');
                }
            }

        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log(textStatus);
        }
    });
}

function triggerAllChanges() {
    $("#assCarCostPerTon").trigger('change');
    $("#assLayingCostPerTon").trigger('change');
    $("#expRate").trigger('change');
    $("#assCarCostPerTon").trigger('change');
    $("#assCarCostPerTon").trigger('change');
    $("#assCarCostPerTon").trigger('change');
    getAutoFillValues();
}

function logAssSale() {
    $('#asphaltSaleForm').ajaxForm({
        beforeSend: function () {
            $("#assSaleBtn").hide();
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
                    type: "success", layout: "top", timeout: 4000
                });
                $("#assSaleBtn").show();
                setTimeout(function () {
                    location.reload();
                }, 5000);
            } else {
                noty({
                    text: respMessage,
                    type: "error", layout: "top", timeout: 4000
                });
                $("#assSaleBtn").show();
            }

        }
    });
}