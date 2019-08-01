<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script type="text/javascript" src="<c:url value="/resources/js/registration.js"/>"></script>
    <link href="<c:url value="/resources/css/registration.css" />" rel="stylesheet">
    <title>Registration</title>
</head>
<body>

<form>
    <div class="container">
        <h1>Register</h1>
        <p>Please fill in this form to create an account.</p>
        <hr>

        <label><b>User name</b></label>
        <input type="text" placeholder="Enter Username" name="username"  required>

        <label><b>Password</b></label>
        <input type="password" placeholder="Enter Password" name="password" required>

        <label><b>Confirm  Password</b></label>
        <input type="password" placeholder="Confirm Password" name="password-confirm" required>
        <hr>

        <button type="submit" class="registerbtn">Register</button>
    </div>

    <div class="container signin">
        <p>Already have an account? Please, <a href="/login">sign in</a>.</p>
    </div>
</form>

</body>
</html>
