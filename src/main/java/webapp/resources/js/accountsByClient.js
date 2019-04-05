$(document).ready(function () {

    $(".edit").click(function () {
        let currentTD = $(this).closest('tr').find('.data');
        $.each(currentTD, function () {
            $(this).attr('contenteditable', true)
        });
        let buttons = $(this).closest('tr').find("button");
        $.each(buttons, function () {
            let name = $(this).attr("class");
            if (name === "edit" || name === "delete") {
                $(this).attr('disabled', true);
            } else {
                $(this).attr('disabled', false);
            }
        });
    });

    $(".cancel").click(function () {
        let currentTD = $(this).closest('tr').find('.data');
        $.each(currentTD, function () {
            $(this).attr('contenteditable', false)
        });
        let buttons = $(this).closest('tr').find("button");
        $.each(buttons, function () {
            let name = $(this).attr("class");
            if (name === "edit" || name === "delete") {
                $(this).attr('disabled', false);
            } else {
                $(this).attr('disabled', true);
            }
        });
    });

    $("#create-account").click(function () {
        $("div").css('visibility', 'visible');
    });

    $(".save").click(function () {
        let currentTD = $(this).closest('tr').find('.data');
        $.each(currentTD, function () {
            $(this).attr('contenteditable', false)
        });
        let account = {};
        $.each(currentTD, function () {
            let id = $(this).attr("id");
            switch (id) {
                case "account_id":
                    account.id = $(this).text();
                    break;
                case "number":
                    account.number = $(this).text();
                    break;
                case "currency_code":
                    account.currencyCode = $(this).text();
                    break;
                case "type":
                    account.type = $(this).text();
                    break;
                case "open_date":
                    account.openDate = $(this).text();
                    break;
                case "balance":
                    account.balance = $(this).text();
                    break;
                case "owner_id":
                    account.ownerId = $(this).text();
                    break;
            }
        });
        let body = JSON.stringify(account);
        let buttons = $(this).closest('tr').find("button");
        $.ajax({
            type: "PUT",
            url: 'http://localhost:8080/account-manager-app/account',
            data: body,
            success: function () {
                $.each(buttons, function () {
                    let name = $(this).attr("class");
                    if (name === "edit" || name === "delete") {
                        $(this).attr('disabled', false);
                    } else {
                        $(this).attr('disabled', true);
                    }
                });
            }
        });
    });

    $(".delete").click(function () {
        let row = $(this).closest('tr');
        let accountId = row.find('#account_id').text();
        $.ajax({
            type: "DELETE",
            url: 'http://localhost:8080/account-manager-app/account?accountId=' + accountId,
            success: function (response) {
                row.remove();
                alert(response);
            }
        });
    });

    $("#submit").click(function () {
        let formData = $("#form").find('.data');
        let account = {};
        $.each(formData, function () {
            let name = $(this).attr("name");
            switch (name) {
                case "number":
                    account.number = $(this).val();
                    break;
                case "currencyCode":
                    account.currencyCode = $(this).find('option:selected').val();
                    break;
                case "type":
                    account.type = $(this).find('option:selected').val();
                    break;
                case "ownerId":
                    account.ownerId = $(this).val();
                    break;
            }
        });
        let body = JSON.stringify(account);
        $.ajax({
            url: 'http://localhost:8080/account-manager-app/account',
            type: "POST",
            contentType: 'application/json',
            data: body,
            success: function () {
                location.reload();
            }
        });
    });

});