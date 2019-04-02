<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <style type="text/css">
        #accounts {
            font-family: "Trebuchet MS", Arial, Helvetica, sans-serif;
            border-collapse: collapse;
            width: 100%;
        }

        #accounts td, #accounts th {
            border: 1px solid #ddd;
            padding: 8px;
        }

        #accounts tr:nth-child(even) {
            background-color: #f2f2f2;
        }

        #accounts tr:hover {
            background-color: #ddd;
        }

        #accounts th {
            padding-top: 12px;
            padding-bottom: 12px;
            text-align: left;
            background-color: #4CAF50;
            color: white;
        }

        #create-account {
            font-family: "Trebuchet MS", Arial, Helvetica, sans-serif;
            font-size: 16px;
            font-weight: bold;
            background-color: #4CAF50;
            border: none;
            color: white;
            padding: 8px;
            text-decoration: none;
            margin: 10px 0px;
            cursor: pointer;
        }

        input[type=text], select {
            width: 100%;
            padding: 12px 20px;
            margin: 8px 0;
            display: inline-block;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-sizing: border-box;
        }

        input[type=submit] {
            width: 100%;
            background-color: #4CAF50;
            color: white;
            padding: 14px 20px;
            margin: 8px 0;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        input[type=submit]:hover {
            background-color: #45a049;
        }

        div {
            font-family: "Trebuchet MS", Arial, Helvetica, sans-serif;
            border-radius: 5px;
            background-color: #f2f2f2;
            padding: 20px;
            visibility: hidden;
        }

        option {
            font-family: "Trebuchet MS", Arial, Helvetica, sans-serif;
            font-size: 16px;
        }

        h2 {
            font-family: "Trebuchet MS", Arial, Helvetica, sans-serif;
            color: #ddd;
            border-bottom: 5px solid #ddd;
        }
    </style>
</head>
<h2>Accounts</h2>

<table id="accounts">
    <tr>
        <th>#</th>
        <th>Account id</th>
        <th>Number</th>
        <th>Currency code</th>
        <th>Type</th>
        <th>Open date</th>
        <th>Balance</th>
        <th>Owner</th>
        <th></th>
        <th></th>
        <th></th>
    </tr>
    <c:forEach items="${accounts}" var="account">
        <tr>
            <td id="account_id" class="data" contenteditable="false">${account.id}</td>
            <td id="number" class="data" contenteditable="false">${account.number}</td>
            <td id="currency_code" class="data" contenteditable="false">${account.currencyCode}</td>
            <td id="type" class="data" contenteditable="false">${account.type}</td>
            <td id="open_date" class="data" contenteditable="false">${account.openDate}</td>
            <td id="balance" class="data" contenteditable="false">${account.balance}</td>
            <td id="owner_id" class="data" contenteditable="false">${account.ownerId}</td>
            <td>
                <button class="edit"><i class="material-icons">edit</i></button>
            </td>
            <td>
                <button class="delete"><i class="material-icons">delete</i></button>
            </td>
            <td>
                <button class="save" disabled><i class="material-icons">done</i></button>
            </td>
            <td>
                <button class="cancel" disabled><i class="material-icons">highlight_off</i></button>
            </td>
        </tr>
    </c:forEach>
</table>
<button id="create-account">Create new account</button>
<div>
    <form>
        <label>Account number</label>
        <input type="text" name="number">

        <label>Currency code</label>
        <select name="currencyCode">
            <option value="643">RUB</option>
            <option value="826">GBP</option>
            <option value="840">USD</option>
            <option value="978">EUR</option>
            <option value="980">UAH</option>
        </select>

        <label>Account type</label>
        <select name="type">
            <option value="deposit">Deposit</option>
            <option value="current">Current</option>
        </select>

        <input type="submit" value="Submit">
    </form>
</div>
<script>
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
            let xhr = new XMLHttpRequest();
            xhr.open('POST', 'http://localhost:8080/account-manager-app/account');
            xhr.send(body);
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
    });
</script>
</body>
</html>
