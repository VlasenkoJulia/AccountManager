<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script type="text/javascript" src="<c:url value="/resources/js/forgotPassword.js"/>"></script>
    <link href="<c:url value="/resources/css/forgotPassword.css" />" rel="stylesheet">
    <title>Forgot password</title>
</head>
<body>
<div class="container">
    <h1>Please enter your email to reset password:</h1>
    <form>
        <label><b>Email</b></label>
        <input type="text" placeholder="Enter Email" name="email" required>
        <input type="button" class="sendEmailBtn" value="Submit">
        <div class="signin">
            <p>Back to <a href="/login">login</a> page.</p>
        </div>
    </form>
</div>
</body>
</html>
