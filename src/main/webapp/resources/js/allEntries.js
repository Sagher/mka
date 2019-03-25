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
    table = $("#viewDatatable").DataTable({
        "dom": 'T<"clear">Blfrtip',
        "oLanguage": {
            "sProcessing": "<i class='fa fa-cogs fa-spin fa-5x'></i>"
        },
        "ajax": {
            "url": "entries/data?type=all"
        },
        "columns": [
            /*
             <td>SR. NO.</td>
             <th>DATE</th>
             <th>PERSON</th>
             <th>SITE NAME</th>
             <th>DESCRIPTION</th>
             <th>TYPE/SIZE</th>
             <th>PLANT BILTY NO.</th>
             <th>RECIPIENT BILTY NO.</th>
             <th>VEHICLE NO.</th>
             <th>QUANTITY</th>
             <th>RATE</th>
             <th>Dr.</th>
             <th>Cr.</th>
             <th>AMOUNT</th>
             
             <c:if test = "${item.type == 'RECEIVABLE'}">
             <td>${item.itemType.itemName} SALE</td>
             </c:if>
             <c:if test = "${item.type == 'PAYABLE'}">
             <td>${item.itemType.itemName} PURCHASE</td>
             </c:if>
             */
            {"data": "id", "render": function (data, type, full, meta) {
                    var html = "<div class='id'>" + data + "</div>";
                    return html;
                }
            },
            {"data": "timestamp"},
            {"data": "accountName"},
            {"data": "project"},
            {"data": "description", "render": function (data, type, full, meta) {
                    return full.itemType.itemName;
                }
            },
            {"data": "subType"},
            {"data": "plantBilty"},
            {"data": "recipientBilty"},
            {"data": "vehicleNo"},
            {"data": "quantity"},
            {"data": "rate"},
            {"data": "totalAmount", "render": function (data, type, full, meta) {
                    if (full.type == 'RECEIVABLE') {
                        return full.totalAmount;
                    } else {
                        return 0;
                    }
                }
            },
            {"data": "totalAmount", "render": function (data, type, full, meta) {
                    if (full.type == 'PAYABLE') {
                        return full.totalAmount;
                    } else {
                        return 0;
                    }
                }
            },
            {"data": "totalAmount", "render": function (data, type, full, meta) {
                    html = "<button id='del-" + full.id
                            + "' style='margin-left:3px' class=\"btn btn-danger\" onclick=\"delEntry(" + full.id
                            + ")\"><i class=\"fa fa-trash\"></i></button>";
                    return html;
                }
            }
        ],
        "processing": true,
        "serverSide": true,
        "pageLength": 25,
        "lengthMenu": [[10, 25, 50, -1], ['10', '25', '50', 'ALL']],
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


});

function advanceSearch() {
    var fromDate = $('#from').val();
    var toDate = $('#to').val();
    var buySup = $("#accountName").val();


    console.log("from:" + fromDate);
    console.log("to:" + toDate);
    console.log("buySup:" + buySup.replace('&', '$'));
    console.log("proj:" + toDate);


    table.ajax.url("entries/data?type=all&fromDate=" + fromDate + "&toDate=" + toDate
            + "&buyerSupplier=" + buySup.replace('&', '$'));
    table.ajax.reload();
}

function delEntry(id) {
    console.log(id);
    console.log(clickedRow)
    if (clickedRow[0] == id) {
        $("#confirmDelModal .modal-body").html("");
        $("#confirmDelModal .modal-body")
                .append("<p> This would permanentely delete Entry: <br><br> #" + clickedRow[0] + ": [" + clickedRow[1] + " - "
                        + clickedRow[2] + " - " + clickedRow[3] + " (" + clickedRow[4] + ") " + clickedRow[5] + "] </p>")
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
                    type: "success", layout: "top", timeout: 2000
                });
                setTimeout(function () {
                    table.ajax.reload();
                }, 2000);

            } else {
                noty({
                    text: respMessage,
                    type: "error", layout: "top", timeout: 4000
                });
            }

        }
    });
}

