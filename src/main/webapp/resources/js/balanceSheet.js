/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var table;
var clickedRow = [];
var totalField = $("#totalField");
var totalAmount = 0;
//-------------------------------
$(document).ready(function () {
//    table = $("#viewDatatable").DataTable({
//        "dom": 'T<"clear">Blfrtip',
//        "oLanguage": {
//            "sProcessing": "<i class='fa fa-cogs fa-spin fa-5x'></i>"
//        },
//        "ajax": {
//            "url": "report/profitLoss"
//        },
//        "columns": [
//            /*
//             <th>Date</th>
//             <th>Project</th>
//             <th>Name</th>
//             <th>Item</th>
//             <th>Quantity</th>
//             <th>Rate</th>
//             <th>Total Amount</th>
//             <th>Remaining</th>
//             <th>Description</th>             */
////            {"data": "id", "render": function (data, type, full, meta) {
////                    var html = "<div class='id'>" + data + "</div>";
////                    return html;
////                }
////            },
//            {"data": "createdDate"},
//            {"data": "project"},
//            {"data": "accountName"},
//            {"data": "itemType.itemName"},
//            {"data": "quantity"},
//            {"data": "rate"},
//            {"data": "totalAmount"},
//            {"data": "amount"},
//            {"data": "description"}
//        ],
//        "processing": true,
//        "serverSide": true,
//        "pageLength": -1,
//        "lengthMenu": [[10, 25, 50, -1], ['10', '25', '50', 'All']],
//        "searching": false,
//        "ordering": true,
//        "info": false,
//        "stateSave": false,
//        "responsive": true,
//        "oLanguage": {
//            "sEmptyTable": "No Entries Found"
//        },
//
//    });
    $('.dataTables_filter input').attr("placeholder", "search by name");


    $('#viewDatatable tbody').on('click', 'tr', function () {
        clickedRow = [];
        $("td", this).each(function (j) {
//            console.log("".concat(" col[", j, "] = ", $(this).text()));
            clickedRow.push($(this).text());
        });
//        console.log(clickedRow)
    });

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
    renderTotalAmount();
});

function renderTotalAmount() {
    setTimeout(function () {
        $.ajax({
            url: "report/profitLoss/total",
            dataType: "json",
            success: function (data) {
                totalField.html(data);

            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log(textStatus);
            }
        });
    }, 500);

}

function advanceSearch() {
    var fromDate = $('#from').val();
    var toDate = $('#to').val();
    var buySup = $("#buySup").val();
    var proj = $("#proj").val();
    var type = $("#type").val();


    console.log("from:" + fromDate);
    console.log("to:" + toDate);
    console.log("buySup:" + buySup);
    console.log("proj:" + toDate);

    totalAmount = 0;
    totalField.html(0);

    table.ajax.url("report/data?type=" + type + "&fromDate=" + fromDate + "&toDate=" + toDate
            + "&buyerSupplier=" + buySup + "&project=" + proj);
    table.ajax.reload();
    renderTotalAmount();

}

function edit(id) {
    console.log(id);
    console.log(clickedRow)

    if (clickedRow[0] == id) {
        $("#eid").val(clickedRow[0]);
        $("#ename").val(clickedRow[4]);
        $("#edesc").val(clickedRow[5]);
        $("#eamount").val(clickedRow[6]);
        $("#eadvance").val(clickedRow[7]);

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

