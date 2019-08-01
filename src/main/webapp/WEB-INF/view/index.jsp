<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <script type="text/javascript" src="<c:url value="/resources/js/index.js"/>"></script>
    <link href="<c:url value="/resources/css/index.css" />" rel="stylesheet">
    <title>Account manager</title>
</head>
<body>
<header>
    <h1>Welcome to Account manager! You are logged in as ${username} </h1>
    <nav>
        <ul>
            <li><a href="/account?accountId=3">Accounts</a>
            <li><a href="">Clients</a>
            <li><a href="">Cards</a>
        </ul>
    </nav>
</header>
<div class="content">
    <h2>Content</h2>
    <p>Content</p>
</div>

<div class="footer">
    <p>Footer</p>
</div>

</body>
</html>