/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var albumId;
var table;

//-------------------------------
$(document).ready(function () {
    table = $("#userActivityTable").DataTable({
        "dom": 'T<"clear">Blfrtip',
//        "dom": '<"bottom"i>rt<"bottom"flp><"clear">',
        "oLanguage": {
            "sProcessing": "<i class='fa fa-cogs fa-spin fa-5x'></i>"
        },
        "ajax": {
            "url": "activity/data"
        },
        "columns": [
            {"data": "actionType"},
            {"data": "actionDescription"},
            {"data": "actionTimestamp"},
            {"data": "remoteAddr"}
        ],
        "processing": true,
        "serverSide": true,
        "pageLength": 10,
        "lengthMenu": [[10, 25, 50], ['10', '25', '50']],
        "searching": false,
        "ordering": false,
        "info": true,
        "stateSave": true,
        "pagingType": "full_numbers"
    });
//    $('.dataTables_filter input').attr("placeholder", "search by title");
//    $("#aDate").datepicker({});
//    $("#aDate").datepicker("option", "dateFormat", "yy-mm-dd");
//    $("#eDate").datepicker({});
//    $("#eDate").datepicker("option", "dateFormat", "yy-mm-dd");

});

