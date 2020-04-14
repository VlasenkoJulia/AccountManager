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
        $("#create-account-wrapper").css('display', 'block');
        $(this).attr('disabled', true)
    });

    $("#create-cancel").click(function () {
        $("#create-account-wrapper").css('display', 'none');
        $("#create-account").attr('disabled', false)
    });

    $("#convert").click(function () {
        $("#conversion-wrapper").css('display', 'block');
        $(this).attr('disabled', true)
    });

    $("#convert-cancel").click(function () {
        $("#conversion-wrapper").css('display', 'none');
        $("#convert").attr('disabled', false)
    });

    $(".save").click(function () {
        let editableData = $(this).closest('tr').find('.data');
        $.each(editableData, function () {
            $(this).attr('contenteditable', false)
        });
        let row = $(this).closest('tr');
        let account = {};
        account.id = $(row).find(".account_id").text();
        account.number = $(row).find(".number").text();
        account.ownerId = $(row).find(".owner_id").text();
        account.currencyCode = $(row).find(".currency_code").text();
        account.type = $(row).find(".type").text();
        account.balance = $(row).find(".balance").text();
        account.openDate = $(row).find(".open_date").text();
        let body = JSON.stringify(account);
        let buttons = $(this).closest('tr').find("button");
        $.ajax({
            type: "PUT",
            url: 'http://localhost:8080/account',
            contentType: 'application/json',
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
                location.reload();
            },
            error: function (xhr) {
                let errorDto = JSON.parse(xhr.responseText);
                alert(errorDto.message);
                location.reload();
            }
        });
    });

    $(".delete").click(function () {
        let row = $(this).closest('tr');
        let accountId = $(row).find('.account_id').text();
        $.ajax({
            type: "DELETE",
            url: 'http://localhost:8080/account?accountId=' + accountId,
            success: function () {
                location.reload();
            }
        });
    });

    $("#create-submit").click(function () {
        let form = $("#create-account-form");
        let account = {};
        account.ownerId = $(form).find("[name=ownerId]").val();
        account.currencyCode = $(form).find("[name=currencyCode]").find('option:selected').val();
        account.number = $(form).find("[name=number]").val();
        account.type = $(form).find("[name=type]").find('option:selected').val();
        if (account.type === "") {
            account.type = null;
        }
        let body = JSON.stringify(account);
        $.ajax({
            url: 'http://localhost:8080/account',
            type: "POST",
            contentType: 'application/json',
            data: body,
            success: function () {
                location.reload();
            },
            error: function (xhr) {
                let errorDto = JSON.parse(xhr.responseText);
                alert(errorDto.message);
                location.reload();
            }
        });
    });

    $("#convert-submit").click(function () {
        let form = $("#conversion-form");
        let conversionDto = {};
        conversionDto.sourceAccountId = $(form).find("[name=sourceAccountId]").find('option:selected').val();
        conversionDto.targetAccountId = $(form).find("[name=targetAccountId]").find('option:selected').val();
        conversionDto.amount = $(form).find("[name=amount]").val();
        let body = JSON.stringify(conversionDto);
        $.ajax({
            url: 'http://localhost:8080/converter',
            type: "POST",
            contentType: 'application/json',
            data: body,
            success: function () {
                location.reload();
            },
            error: function (xhr) {
                let errorDto = JSON.parse(xhr.responseText);
                alert(errorDto.message);
                location.reload();
            }
        });
    });
});
