/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var table;
var clickedRow = [];
var itemTypeId;
//-------------------------------
$(document).ready(function () {
    itemTypeId = $("#itemTypeId").val() !== '' ? $("#itemTypeId").val() : 0;
    table = $("#viewDatatable").DataTable({
        "dom": 'T<"clear">Blfrtip',
        "oLanguage": {
            "sProcessing": "<i class='fa fa-cogs fa-spin fa-5x'></i>"
        },
        "ajax": {
            "url": "entries/data?itemTypeId=" + itemTypeId
        },
        "columns": [
            /*
             <th>ID</th>
             <th>Entry Date</th>
             <th>Item</th>
             <th>Type</th>
             <th>Buyer</th>
             <th>Supplier</th>
             <th>Quantity</th>
             <th>Rate</th>
             <th>Amount</th>
             <th>Advance</th>
             <th>Action</th>
             */
//            {"data": "id", "render": function (data, type, full, meta) {
//                    var html = "<div class='id'>" + data + "</div>";
//                    return html;
//                }
//            },
            {"data": "entryDate"},
            {"data": "itemName", "render": function (data, type, full, meta) {
                    if (full.entriesDirectDetails != null) {
//                        console.log(data + " (" + full.itemType + ")")
                        return data + " (" + full.entriesDirectDetails.subType + ")";
                    }
                    return data;
                }
            },
            {"data": "subEntryType"},
            {"data": "buyer"},
            {"data": "supplier"},
            {"data": "project"},
            {"data": "quantity"},
            {"data": "rate"},
            {"data": "totalPrice"},
            {"data": "advance"}
//            {"data": "", "render": function (data, type, full, meta) {
//                    var html = "";
////                    html += "<button id='edit-" + full.id + "' style='margin-left:3px' class=\"btn btn-warning\" onclick=\"edit(" + full.id + ")\"><i class=\"fa fa-edit\"></i></button>";
//                    html += "<button id='del-" + full.id + "' style='margin-left:3px' class=\"btn btn-danger\" onclick=\"del(" + full.id + ")\"><i class=\"fa fa-trash\"></i></button>";
//                    return html;
//                }
//            }

        ],
        "processing": true,
        "serverSide": true,
        "pageLength": 10,
        "lengthMenu": [[10, 25, 50], ['10', '25', '50']],
        "searching": false,
        "ordering": true,
        "info": true,
        "stateSave": true,
        "responsive": true,
        "pagingType": "full_numbers",
        "oLanguage": {
            "sEmptyTable": "No Entries Found"
        }

    });
    $('.dataTables_filter input').attr("placeholder", "search by name");


    $('#viewDatatable tbody').on('click', 'tr', function () {
        clickedRow = [];
        $("td", this).each(function (j) {
//            console.log("".concat(" col[", j, "] = ", $(this).text()));
            clickedRow.push($(this).text());
        });
//        console.log(clickedRow)
    });

    if (itemTypeId > 0) {
        console.log(itemTypeId);
        $.ajax({
            url: "getEntryItem?id=" + itemTypeId,
            dataType: "json",
            success: function (data) {
                var itemType = data.subEntryType.split(";");
                for (var j in itemType) {
                    $("#subEntryType").append($("<option />").val(itemType[j]).text(itemType[j]));
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log(textStatus);
            }
        });

        // populate stock div
//        var dItemSubTypeSelection = '';
//        $.get("entries/stockTrace?entryTypeId=" + itemTypeId + "&subType=" + dItemSubTypeSelection, function (data) {
//            if (data !== undefined) {
//                stockData = data;
//                $("#stockDetailDiv").removeAttr("hidden");
//                var html = '';
//                html += '<div class="brand-card-header text-white bg-info">\n\
//                    <div class="card-body">\n\
//                    <div class="item-name">';
//                if (data.subType !== null) {
//                    html += ('<strong>' + data.itemName + '</strong> <small>' + data.subType + '</small></div>');
//                } else {
//                    html += ('<strong>' + data.itemName + '</strong></div>');
//                }
//                html += ('<div>Total Sales: ' + data.salesUnit + ' ' + data.itemUnit + '</div>\n\
//                    <div>Total Sales Amount: ' + data.salesAmount + ' PKR</div>\n\
//                    </div></div>\n\
//                    <div class="brand-card-body"><div>\n\
//                    <div class="text-value">' + data.stockUnits + ' ' + data.itemUnit + '</div>\n\
//                    <div class="text-uppercase text-muted small">In Stock</div>\n\
//                    </div>\n\
//                    <div>\n\
//                    <div class="text-value">' + data.stockAmount + ' PKR</div>\n\
//                    <div class="text-uppercase text-muted small">Total Stock Price</div>\n\
//                    </div>\n\
//                    <div>\n\
//                    <div class="text-value">' + data.averageUnitPrice + ' PKR</div>\n\
//                    <div class="text-uppercase text-muted small">Average Per ' + ' ' + data.itemUnit + ' Price</div>\n\
//                    </div>\n\
//                    </div>');
//
//                $("#stockDetail").html(html);
//            }
//            console.log(data)
//        });
    }

    $.ajax({
        url: "customersAndBuyers",
        dataType: "json",
        success: function (data) {
            for (var i in data) {
                $("#buySup").append($("<option />").val(data[i].name).text(data[i].name));
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log(textStatus);
        }
    });
    $.ajax({
        url: "projects",
        dataType: "json",
        success: function (data) {
            for (var i in data) {
                $("#proj").append($("<option />").val(data[i].name).text(data[i].name));
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log(textStatus);
        }
    });
});

function advanceSearch() {
    var fromDate = $('#from').val();
    var toDate = $('#to').val();
    var dEntryType = $("#subEntryType").val();
    var buySup = $("#buySup").val();
    var proj = $("#proj").val();


    console.log("from:" + fromDate);
    console.log("to:" + toDate);
    console.log("dEntryType:" + dEntryType);
    console.log("buySup:" + buySup);
    console.log("proj:" + toDate);


    table.ajax.url("entries/data?itemTypeId=" + itemTypeId + "&fromDate=" + fromDate + "&toDate=" + toDate
            + "&subEntryType=" + dEntryType + "&buyerSupplier=" + buySup + "&project=" + proj);
    table.ajax.reload();
}

function edit(id) {
    console.log(id);
    console.log(clickedRow)

    if (clickedRow[0] == id) {
        $("#eid").val(clickedRow[0]);
        $("#ebuyer").val(clickedRow[4]);
        $("#esupplier").val(clickedRow[5]);
        $("#eproject").val(clickedRow[6]);
        $("#equantity").val(clickedRow[7]);
        $("#erate").val(clickedRow[8]);
        $("#eamount").val(clickedRow[9]);
        $("#eadvance").val(clickedRow[10]);

        $("#editModal").modal('show');
    } else {
        setTimeout(function () {
            $("#edit-" + id).click();
        }, 50);
    }
    return;
}

$("#erate, #equantity").change(function () {
    if ($("#equantity").val().length > 0 && $("#erate").val().length > 0) {
        var am = $("#equantity").val() * $("#erate").val();
        console.log(am)
        $("#eamount").val(am);
    } else {

    }
});

function update() {
    $('#editForm').ajaxForm({
        beforeSend: function () {
            $("#editModal").hide();
        },
        uploadProgress: function (event, position, total, percentComplete) {
        },
        complete: function (xhr) {
            response = xhr.responseText.split(":");
            console.log(response);
            respCode = response[0];
            respMessage = response[1];
            if (respCode === '00') {
                $('#editEmpModal').modal('hide');
                noty({
                    text: respMessage,
                    type: "success", layout: "center", timeout: 2000
                });
                setTimeout(function () {
                    location.reload();
                }, 2000);

            } else {
                noty({
                    text: respMessage,
                    type: "error", layout: "center", timeout: 4000
                });
                $("#editModal").show();
            }

        }
    });
}

function del(id) {
    console.log(id);
    console.log(clickedRow)
    if (clickedRow[0] == id) {
        $("#erole").val(clickedRow[5]);
        $("#confirmDelModal .modal-body").html("");
        $("#confirmDelModal .modal-body")
                .append("<p> This would permanentely delete: <br><br><b>" + clickedRow[1] + "</b> (" + clickedRow[2] + ") </p>")
                .append(" <input name='eid' hidden='true' value='" + id + "'>")
        $("#confirmDelModal").modal('show');
    } else {
        setTimeout(function () {
            $("#del-" + id).click();
        }, 50);
    }
    return;
}

function delet() {
    $('#delForm').ajaxForm({
        beforeSend: function () {
        },
        uploadProgress: function (event, position, total, percentComplete) {
        },
        complete: function (xhr) {
            response = xhr.responseText.split(":");
            console.log(response);
            respCode = response[0];
            respMessage = response[1];
            if (respCode === '00') {
                $('#confirmDelModal').modal('hide');
                noty({
                    text: respMessage,
                    type: "success", layout: "center", timeout: 2000
                });
                setTimeout(function () {
                    location.reload();
                }, 2000);

            } else {
                noty({
                    text: respMessage,
                    type: "error", layout: "center", timeout: 4000
                });
            }

        }
    });
}

