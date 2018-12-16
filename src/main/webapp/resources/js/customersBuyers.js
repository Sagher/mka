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
            "url": "entries/data?type=customersBuyers"
        },
        "columns": [
            /*
             <th>Name</th>
             <th>Payable</th>
             <th>Receivable</th>
             */
            {"data": "name"},
            {"data": "payable"},
            {"data": "receivable"},
            {"data": "id", "render": function (data, type, full, meta) {
                    var html = "<a class='btn btn-primary' href=\"" + ctx + "/entries/" + data + "/detail\"'>View All Transactions</a>";
                    return html;
                }
            }
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


