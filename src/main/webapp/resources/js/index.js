$(document).ready(function () {
    $("#submit").click(function () {
        $.ajax({
            url: 'http://localhost:8080/client/search?query=' + $(document).find("[name=value]").val(),
            type: "GET",
            success: function (message) {
                let template = $('#table').html();
                let templateScript = Handlebars.compile(template);
                $('#results').html(templateScript({array: message}));
                $(".view").click(function () {
                    let id = $(this).closest('tr').find("td").eq(0).text();
                    location.replace('http://localhost:8080/account/get-by-client?clientId=' + id)
                });
            }
        });
    });
})