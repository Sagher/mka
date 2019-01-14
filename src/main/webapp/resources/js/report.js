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
    table = $("#viewDatatable").DataTable({
        "dom": 'T<"clear">Blfrtip',
        "oLanguage": {
            "sProcessing": "<i class='fa fa-cogs fa-spin fa-5x'></i>"
        },
        "ajax": {
            "url": "report/data?type=" + $("#type").val()
        },
        "columns": [
            /*
             <th>Date</th>
             <th>Project</th>
             <th>Name</th>
             <th>Item</th>
             <th>Type</th>
             <th>Quantity</th>
             <th>Rate</th>
             <th>Total Amount</th>
             <th>Advance</th>
             <th>${type}</th>
             <th>Plant Bilty</th>
             <th>Recipient Bilty</th>
             <th>Description</th>
             */
//            {"data": "id", "render": function (data, type, full, meta) {
//                    var html = "<div class='id'>" + data + "</div>";
//                    return html;
//                }
//            },
            {"data": "timestamp"},
            {"data": "project"},
            {"data": "accountName"},
            {"data": "accountName", "render": function (data, type, full, meta) {
                    if (full.itemType != null) {
                        return full.itemType.itemName;
                    } else {
                        return '';
                    }
                }
            },
            {"data": "subType"},
            {"data": "quantity"},
            {"data": "rate"},
            {"data": "totalAmount"},
            {"data": "rate", "render": function (data, type, full, meta) {
                    if (full.itemType != null && full.itemType.id == 18) {
                        return 0;
                    } else if (full.itemType != null) {
                        return (parseInt(full.totalAmount, 10) - parseInt(full.amount, 10));
                    } else {
                        return '';

                    }
                }
            },
            {"data": "amount"},
            {"data": "plantBilty"},
            {"data": "recipientBilty"},
            {"data": "description"}
        ],
        "processing": true,
        "serverSide": true,
//        "pageLength": 10,
//        "lengthMenu": [[10, 25, 50], ['10', '25', '50']],
        "pageLength": -1,
        "lengthMenu": [[10, 25, 50, -1], ['10', '25', '50', 'All']],
        "searching": false,
        "ordering": true,
        "info": true,
        "stateSave": false,
        "responsive": true,
        "pagingType": "full_numbers",
        "oLanguage": {
            "sEmptyTable": "No Entries Found"
        },

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
//    $.ajax({
//        url: "projects",
//        dataType: "json",
//        success: function (data) {
//            for (var i in data) {
//                $("#proj").append($("<option />").val(data[i].name).text(data[i].name));
//            }
//        },
//        error: function (jqXHR, textStatus, errorThrown) {
//            console.log(textStatus);
//        }
//    });
    renderTotalAmount();
});

function renderTotalAmount() {
//    setTimeout(function () {
//        $.ajax({
//            url: "report/total?type=" + $("#type").val(),
//            dataType: "json",
//            success: function (data) {
//                totalField.html(data);
//
//            },
//            error: function (jqXHR, textStatus, errorThrown) {
//                console.log(textStatus);
//            }
//        });
//    }, 500);

}

function advanceSearch() {
    var fromDate = $('#from').val();
    var toDate = $('#to').val();
    var buySup = $("#buySup").val();
    var type = $("#type").val();


    console.log("from:" + fromDate);
    console.log("to:" + toDate);
    console.log("buySup:" + buySup.replace('&', '$'));

    totalAmount = 0;
    totalField.html(0);

    table.ajax.url("report/data?type=" + type + "&fromDate=" + fromDate + "&toDate=" + toDate
            + "&buyerSupplier=" + buySup.replace('&', '$'));
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

