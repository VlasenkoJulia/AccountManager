$(document).ready(function () {
    $(".registerbtn").click(function () {
        let user = {};
        user.userName = $("[name=username]").val();
        user.password = $("[name=password]").val();
        user.confirmPassword = $("[name=password-confirm]").val();
        let body = JSON.stringify(user);
        let ajax = $.ajax({
            type: "POST",
            url: 'http://localhost:8080/user',
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