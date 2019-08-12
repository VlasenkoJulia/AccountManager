<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <link href="<c:url value="/resources/css/login.css" />" rel="stylesheet">
    <title>Login</title>
</head>
<body>
<div class="container">
    <h1>Login page</h1>
    <div class="img_container">
        <img src="/resources/images/img_avatar.png" class="avatar" alt="avatar">
    </div>
    <form name='f' action="/login" method='POST'>
        <label><b>Username</b></label>
        <input type="text" placeholder="Enter Username" name="username" required>

        <label><b>Password</b></label>
        <input type="password" placeholder="Enter Password" name="password" required>

        <button type="submit">Login</button>
    </form>
    <div>
        <span>New user? <a href="/registration">Register here</a></span>
    </div>

    <div>
        <span>Forgot password? <a href="/forgotPassword">Password reset here</a></span>
    </div>
</div>
</body>
</html>
