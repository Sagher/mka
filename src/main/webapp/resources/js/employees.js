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
            "url": "employees/data"
        },
        "columns": [
            {"data": "id"},
            {"data": "name"},
            {"data": "phone"},
            {"data": "salary"},
            {"data": "email"},
            {"data": "role"},
            {"data": "joiningDate"},
            {"data": "", "render": function (data, type, full, meta) {
                    var html = "";
                    html += "<button id='edit-" + full.id + "' style='margin-left:3px' class=\"btn btn-warning\" onclick=\"editEmp(" + full.id + ")\"><i class=\"fa fa-edit\"></i></button>";
                    html += "<button id='del-" + full.id + "' style='margin-left:3px' class=\"btn btn-danger\" onclick=\"delEmp(" + full.id + ")\"><i class=\"fa fa-trash\"></i></button>";
                    return html;
                }
            }

        ],
        "processing": true,
        "serverSide": true,
        "pageLength": 10,
        "lengthMenu": [[10, 25, 50], ['10', '25', '50']],
        "searching": true,
        "ordering": true,
        "info": true,
        "stateSave": true,
        "responsive": true,
        "pagingType": "full_numbers"

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

function addEmployee() {
    $('#createEmpForm').ajaxForm({
        beforeSend: function () {
            $("#createUserBtn").hide();
        },
        uploadProgress: function (event, position, total, percentComplete) {
        },
        complete: function (xhr) {
            response = xhr.responseText.split(":");
            console.log(response);
            respCode = response[0];
            respMessage = response[1];
            if (respCode === '00') {
                $('#newUserModal').modal('hide');
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
                $("#createUserBtn").show();
            }

        }
    });
}


function editEmp(id) {
    console.log(id);
    console.log(clickedRow)

    if (clickedRow[0] == id) {
        $("#eid").val(clickedRow[0]);
        $("#ename").val(clickedRow[1]);
        $("#ephone").val(clickedRow[2]);
        $("#esalary").val(clickedRow[3]);
        $("#eemail").val(clickedRow[4]);
        $("#erole").val(clickedRow[5]);
        $("#edoj").val(clickedRow[6]);
        $("#editEmpModal").modal('show');
    } else {
        setTimeout(function () {
            $("#edit-" + id).click();
        }, 50);
    }
    return;
}

function updateEmp() {
    $('#editEmpForm').ajaxForm({
        beforeSend: function () {
            $("#updateEmpBtn").hide();
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
                $("#updateEmpBtn").show();
            }

        }
    });
}

function delEmp(id) {
    console.log(id);
    console.log(clickedRow)
    if (clickedRow[0] == id) {
        $("#erole").val(clickedRow[5]);
        $("#confirmDelModal .modal-body").html("");
        $("#confirmDelModal .modal-body")
                .append("<p> This would permanentely delete: <br><br><b>" + clickedRow[1] + "</b> (" + clickedRow[5] + ") </p>")
                .append(" <input name='eid' hidden='true' value='" + id + "'>")
        $("#confirmDelModal").modal('show');
    } else {
        setTimeout(function () {
            $("#del-" + id).click();
        }, 50);
    }
    return;
}

function deleteEmp() {
    $('#delEmpForm').ajaxForm({
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

