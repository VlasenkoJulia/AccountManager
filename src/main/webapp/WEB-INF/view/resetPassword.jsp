<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script type="text/javascript" src="<c:url value="/resources/js/resetPassword.js"/>"></script>
    <link href="<c:url value="/resources/css/resetPassword.css" />" rel="stylesheet">
    <title>Forgot password</title>
</head>
<body>
<div class="container">
    <h1>Please enter new password:</h1>
    <form>
        <label><b>Password</b></label>
        <input type="password" placeholder="Enter Password" name="password" required>

        <label><b>Confirm Password</b></label>
        <input type="password" placeholder="Confirm Password" name="password-confirm" required>
        <hr>
        <input type="text" name="userName" value="${userName}" style="display: none;">
        <input type="text" name="email" value="${email}" style="display: none;">
        <input type="text" name="token" value="${token}" style="display: none;">

        <input type="button" class="resetBtn" value="Change password">


        <div class="signin">
            <p>Back to <a href="/login">login</a> page.</p>
        </div>
    </form>
</div>
</body>
</html>
