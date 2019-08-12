$(document).ready(function () {
    $(".sendEmailBtn").click(function () {
        let email = $("[name=email]").val();
        $.ajax({
            type: "POST",
            url: 'http://localhost:8080/forgotPassword?email='+ email,
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