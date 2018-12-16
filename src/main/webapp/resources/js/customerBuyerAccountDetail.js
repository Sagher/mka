/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var table;
var clickedRow = [];
//-------------------------------
$(document).ready(function () {
    var cusBuy = $("#cusBuy").val();
    table = $("#viewDatatable").DataTable({
        "dom": 'T<"clear">Blfrtip',
        "oLanguage": {
            "sProcessing": "<i class='fa fa-cogs fa-spin fa-5x'></i>"
        },
        "ajax": {
            "url": ctx + "/entries/data?type=customersBuyersDetail&buyerSupplier=" + cusBuy
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
             <th>Amount</th>
             <th>Transaction Type</th>
             <th>Description</th>
             */
//            {"data": "id", "render": function (data, type, full, meta) {
//                    var html = "<div class='id'>" + data + "</div>";
//                    return html;
//                }
//            },
            {"data": "timestamp"},
            {"data": "project"},
            {"data": "type", "render": function (data, type, full, meta) {
                    if (full.type == 'NA') {
                        return ' ';
                    } else {
                        return data;
                    }
                }
            },
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
            {"data": "description"}
        ],
        "processing": true,
        "serverSide": true,
        "pageLength": -1,
        "lengthMenu": [[-1], ['ALL']],
        "searching": false,
        "ordering": false,
        "oLanguage": {
            "sEmptyTable": "No Entries Found"
        }

    });
});

function viewAllTransactions(id) {
    console.log(id)
}


