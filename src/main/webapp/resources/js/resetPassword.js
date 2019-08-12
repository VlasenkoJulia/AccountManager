$(document).ready(function () {
    $(".resetBtn").click(function () {
        let user = {};
        user.userName = $("[name=userName]").val();
        user.email = $("[name=email]").val();
        user.resetToken = $("[name=token]").val();
        user.password = $("[name=password]").val();
        user.confirmPassword = $("[name=password-confirm]").val();
        let body = JSON.stringify(user);
        $.ajax({
            type: "POST",
            url: 'http://localhost:8080/resetPassword',
            contentType: 'application/json',
            data: body,
            async: false,
            success: function (data) {
                alert(data);
            },
            error: function (xhr) {
                let errorDto = JSON.parse(xhr.responseText);
                alert(errorDto.message);
                location.reload();
            }
        });
    });
});