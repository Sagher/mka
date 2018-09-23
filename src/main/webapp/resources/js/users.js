/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function editUser(userId) {
    console.log(userId);
    var username = $('#user-' + userId).find('.iusername').val();
    var role = $('#user-' + userId).find('.irole').val();
    var password = $('#user-' + userId).find('.ipassword').val();
    var fullname = $('#user-' + userId).find('.ifullname').val();
    var status = $('#user-' + userId).find('.ienabled').val();
    console.log(username, role, password, fullname, status)
    $('#eid').val(userId);
    $('#eusername').val(username);
    $('#epassword').val(password);
    $('#efullname').val(fullname);
    $('#erole').val(role);

    $('#inline-radio0').removeAttr("checked");
    $('#inline-radio0').removeAttr("disabled");
    $('#inline-radio1').removeAttr("checked");
    $('#inline-radio1').removeAttr("disabled");
    if (status == 1) {
        $('#inline-radio1').attr("checked", "true");
    } else {
        $('#inline-radio0').attr("checked", "true");
    }
    if (role == 'ADMIN') {
        $('#erole').attr("disabled", "true");
        $('#estatus').attr("disabled", "true");
        $('#inline-radio0').attr("disabled", "true");
    } else {
        $('#erole').removeAttr("disabled");
        $('#estatus').removeAttr("disabled");
    }
    $('#editUserModal').modal('show');
}

function updateUser() {
    $('#editUserForm').ajaxForm({
        beforeSend: function () {
            $("#updateUserBtn").hide();
        },
        uploadProgress: function (event, position, total, percentComplete) {
        },
        complete: function (xhr) {
            response = xhr.responseText.split(":");
            console.log(response);
            respCode = response[0];
            respMessage = response[1];
            if (respCode === '00') {
                noty({
                    text: respMessage,
                    type: "success", layout: "center", timeout: 2000
                });
                setTimeout(function () {
                    $('#editUserModal').modal('hide');
                    location.reload();
                }, 2000);

            } else {
                noty({
                    text: respMessage,
                    type: "error", layout: "center", timeout: 4000
                });
                $("#updateUserBtn").show();
            }

        }
    });
}


function createUser() {
    $('#createUserForm').ajaxForm({
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
                noty({
                    text: respMessage,
                    type: "success", layout: "center", timeout: 2000
                });
                setTimeout(function () {
                    $('#newUserModal').modal('hide');
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