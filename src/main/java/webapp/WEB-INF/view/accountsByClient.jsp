<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    int i = 1;
%>
<!DOCTYPE html>
<html>
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <script type="text/javascript" src="<c:url value="/resources/js/accountsByClient.js"/>"></script>
    <link href="<c:url value="/resources/css/accountsByClient.css" />" rel="stylesheet">
    <title>Accounts</title>
</head>
<body>
<h2>Accounts of the client: ${client.firstName} ${client.lastName}</h2>

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
        <th></th>
    </tr>
    <c:forEach items="${accounts}" var="account">
        <tr>
            <td><%= i++ %>
            </td>
            <td class="account_id">${account.id}</td>
            <td class="number data">${account.number}</td>
            <td class="currency_code data">${account.currencyCode}</td>
            <td class="type data">${account.type}</td>
            <td class="open_date data"><fmt:formatDate value="${account.openDate}" pattern="dd.MM.yyyy"/></td>
            <td class="balance data">${account.balance}</td>
            <td class="owner_id data">${account.ownerId}</td>
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
<div class="wrapper">
    <button id="create-account">Create new account</button>
    <button id="convert">Convert money</button>
</div>

<div id="conversion-wrapper">
    <form id="conversion-form">
        <label>Source account</label>
        <select name="sourceAccountId">
            <option value="" hidden disabled selected>Choose account to withdraw money for conversion</option>
            <c:forEach items="${accounts}" var="account">
                <option value="${account.id}">(#${account.id}) ${account.number};
                    current balance: ${account.balance}(${account.currencyCode})
                </option>
            </c:forEach>
        </select>

        <label>Target account</label>
        <select name="targetAccountId">
            <option value="" hidden disabled selected>Choose account to deposit converted money</option>
            <c:forEach items="${accounts}" var="account">
                <option value="${account.id}">(#${account.id}) ${account.number};
                    current balance: ${account.balance}(${account.currencyCode})
                </option>
            </c:forEach>
        </select>

        <label>Amount</label>
        <input type="number" name="amount" min="0" step=".01">
        <div class="wrapper">
            <input id="convert-submit" type="button" value="Ok">
            <input id="convert-cancel" type="button" value="Cancel">
        </div>
    </form>
</div>

<div id="create-account-wrapper">
    <form id="create-account-form">
        <label>Account number</label>
        <input class="data" type="text" name="number">
        <label>Currency code</label>
        <select class="data" name="currencyCode">
            <option value="" hidden disabled selected>Choose currency</option>
            <option value="643">RUB</option>
            <option value="826">GBP</option>
            <option value="840">USD</option>
            <option value="978">EUR</option>
            <option value="980">UAH</option>
        </select>
        <label>Account type</label>
        <select class="data" name="type">
            <option value="" hidden disabled selected>Choose account type</option>
            <option value="DEPOSIT">Deposit</option>
            <option value="CURRENT">Current</option>
        </select>
        <input class="data" type="text" name="ownerId" value="${client.id}" style="display: none;">
        <div class="wrapper">
            <input id="create-submit" type="button" value="Ok">
            <input id="create-cancel" type="button" value="Cancel">
        </div>
    </form>
</div>
</body>
</html>