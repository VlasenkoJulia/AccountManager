<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/handlebars@latest/dist/handlebars.js"></script>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <script type="text/javascript" src="<c:url value="/resources/js/index.js"/>"></script>
    <link href="<c:url value="/resources/css/index.css" />" rel="stylesheet">
    <title>Account manager</title>
</head>
<body>
<header>
    <h1>Welcome to Account manager! You are logged in as ${username} </h1>
    <p>Want to go back to login page? Please, <a href="<c:url value="/logout" />">Log out</a>.</p>
    <nav>
        <ul>
            <li><a href="/account?accountId=3">Accounts</a>
            <li><a href="">Clients</a>
            <li><a href="">Cards</a>
        </ul>
    </nav>
</header>
<label>Keywords to search </label>
<input type="text" name="value">
<br>
<input id="submit" type="button" value="Submit">

<br>
<br>
<br>
<h3>Results</h3>
<div id="results"></div>
<script id="table" type="text/x-handlebars-template">
    <table id="clients">
        <thead>
        <tr>
            {{#each array.[0]}}
            <th>{{@key}}</th>
            {{/each}}
            <th>Accounts</th>
        </tr>
        </thead>
        <tbody>
        {{#each array}}
        <tr>
            {{#each this}}
            <td>{{this}}</td>
            {{/each}}
            <td><button class="view">View</button></td>
        </tr>
        {{/each}}
        </tbody>
    </table>
</script>
</body>
</html>