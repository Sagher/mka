/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function updateProfile() {
    $('#updateProfileForm').ajaxForm({
        beforeSend: function () {
            $("#updateProfileBtn").attr("disabled", "true");
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
                    type: "success", layout: "center", timeout: 3000
                });
                setTimeout(function () {
                    location.reload();
                }, 3000);

            } else {
                noty({
                    text: respMessage,
                    type: "error", layout: "center", timeout: 4000
                });
                $("#updateProfileBtn").removeAttr("disabled");
            }
        }
    });
}

