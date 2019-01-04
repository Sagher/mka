/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var table;
var clickedRow = [];
//-------------------------------
$(document).ready(function () {
    table = $("#viewDatatable").DataTable({
        "dom": 'T<"clear">Blfrtip',
        "oLanguage": {
            "sProcessing": "<i class='fa fa-cogs fa-spin fa-5x'></i>"
        },
        "ajax": {
            "url": "entries/data?type=cashTransactions"
        },
        "columns": [
            /*
             <th>Date</th>
             <th>Type</th>
             <th>Payee</th>
             <th>Description</th>
             <th>Amount</th>
             */
//            {"data": "id", "render": function (data, type, full, meta) {
//                    var html = "<div class='id'>" + data + "</div>";
//                    return html;
//                }
//            },
            {"data": "timestamp"},
            {"data": "type"},
            {"data": "payee"},
            {"data": "description"},
            {"data": "amount"},
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
});

function advanceSearch() {
    var fromDate = $('#from').val();
    var toDate = $('#to').val();
    var buySup = $("#buySup").val();

    console.log("from:" + fromDate);
    console.log("to:" + toDate);

    table.ajax.url("entries/data?type=indirect&fromDate=" + fromDate + "&toDate=" + toDate + "&buyerSupplier=" + buySup.replace('&', '$'));
    table.ajax.reload();
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

