<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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
<h2>Accounts</h2>

<table id="accounts">
    <tr>
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
        <c:set var="ownerId" scope="session" value="${account.ownerId}"/>
        <tr>
            <td id="account_id" class="data" contenteditable="false">${account.id}</td>
            <td id="number" class="data" contenteditable="false">${account.number}</td>
            <td id="currency_code" class="data" contenteditable="false">${account.currencyCode}</td>
            <td id="type" class="data" contenteditable="false">${account.type}</td>
            <td id="open_date" class="data" contenteditable="false"><fmt:formatDate value="${account.openDate}"
                                                                                    pattern="dd.MM.yyyy"/></td>
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
    <form id="form">
        <label>Account number</label>
        <input class="data" type="text" name="number">
        <label>Currency code</label>
        <select class="data" name="currencyCode">
            <option value="643">RUB</option>
            <option value="826">GBP</option>
            <option value="840">USD</option>
            <option value="978">EUR</option>
            <option value="980">UAH</option>
        </select>
        <label>Account type</label>
        <select class="data" name="type">
            <option value="DEPOSIT">Deposit</option>
            <option value="CURRENT">Current</option>
        </select>
        <input class="data" type="text" name="ownerId" value="${ownerId}" style="display: none;">
        <input id="submit" type="button" value="Submit">
    </form>
</div>
</body>
</html>
