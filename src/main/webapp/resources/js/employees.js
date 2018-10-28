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
            /*
             <th>ID</th>
             <th>Name</th>
             <th>Phone</th>
             <th>CNIC</th>
             <th>Email</th>
             <th>Salary</th>
             <th>Role</th>
             <th>Joining Date</th>
             <th>Address</th>
             <th>Action</th>
             */
            {"data": "id"},
            {"data": "name"},
            {"data": "phone"},
            {"data": "cnic"},
            {"data": "email"},
            {"data": "salary"},
            {"data": "role"},
            {"data": "joiningDate"},
            {"data": "address"},
            {"data": "", "render": function (data, type, full, meta) {
                    var html = "";
                    html += "<td align=\"center\">";
                    if (full.isTerminated) {
                        html += "<span class=\"badge badge-danger\">Terminated</span>";
                        html += "<p class='small'>" + full.terminationDate + "</p>";

                    } else {
                        html += "<span class=\"badge badge-success\">Employeed</span>";
                    }
                    return html;
                }
            },
            {"data": "", "render": function (data, type, full, meta) {
                    var html = "";
                    html += "<button id='edit-" + full.id + "' style='margin-left:3px' class=\"btn btn-warning\" onclick=\"editEmp(" + full.id + ")\"><i class=\"fa fa-edit\"></i></button>";
                    if (!full.isTerminated) {
                        html += "<button id='ter-" + full.id + "' style='margin-left:3px' class=\"btn btn-warning\" onclick=\"terminateEmp(" + full.id + ")\"><i class=\"fa fa-ban\"></i></button>";
                    }
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
        "pagingType": "full_numbers",
        "oLanguage": {
            "sEmptyTable": "No Employees Founds At The Moment"
        },
        "columnDefs": [
            {"orderable": false, "targets": 9}
        ]

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
        $("#ecnic").val(clickedRow[3]);
        $("#eemail").val(clickedRow[4]);
        $("#esalary").val(clickedRow[5]);
        $("#erole").val(clickedRow[6]);
        $("#edoj").val(clickedRow[7]);
        $("#eaddress").val(clickedRow[8]);

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
        $("#erole").val(clickedRow[6]);
        $("#confirmDelModal .modal-title").html("");
        $("#confirmDelModal .modal-title").append("Confirm Deletion");
        $("#confirmDelModal .modal-body").html("");
        $("#confirmDelModal .modal-body")
                .append("<p> This would permanentely delete: <br><br><b>" + clickedRow[1] + "</b> (" + clickedRow[6] + ") </p>")
                .append(" <input name='eid' hidden='true' value='" + id + "'>")
        $("#confirmDelModal #confirmDelBtn").attr("onclick", "deleteEmp()");
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


function terminateEmp(id) {
    console.log(id);
    console.log(clickedRow)
    if (clickedRow[0] == id) {
        $("#erole").val(clickedRow[6]);
        $("#confirmDelModal .modal-title").html("");
        $("#confirmDelModal .modal-title").append("Confirm TERMINATION");
        $("#confirmDelModal .modal-body").html("");
        $("#confirmDelModal .modal-body")
                .append("<p> This would permanentely delete: <br><br><b>" + clickedRow[1] + "</b> (" + clickedRow[6] + ") </p>")
                .append(" <input name='eid' hidden='true' value='" + id + "'>")
                .append(" <label>Termination Date </label>")
                .append(" <input class='form-control' name='terminationDate' type='date' required='true'>")
        $("#confirmDelModal #confirmDelBtn").attr("onclick", "terminateEmployee()");
        $("#confirmDelModal").modal('show');
    } else {
        setTimeout(function () {
            $("#ter-" + id).click();
        }, 50);
    }
    return;
}

function terminateEmployee() {
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

function payAllEmployees() {
    $.get("payAllEmployees", {
        name: "Donald Duck",
        city: "Duckburg"
    }, function (data, status) {
        response = data.split(":");
        console.log(response);
        respCode = response[0];
        respMessage = response[1];
        if (respCode === '00') {
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
    });
}