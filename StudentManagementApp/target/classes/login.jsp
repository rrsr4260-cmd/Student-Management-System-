<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login - Student Management</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body class="login-body">
<div class="login-box">
    <h1>StudentMS Login</h1>
    <p class="sub">Use admin/admin123, staff/staff123, viewer/viewer123</p>

    <% if (request.getParameter("error") != null) { %>
        <div class="alert alert-error">Invalid username or password</div>
    <% } %>

    <form action="login" method="post">
        <div class="form-group">
            <label>Username</label>
            <input type="text" name="username" required>
        </div>
        <div class="form-group">
            <label>Password</label>
            <input type="password" name="password" required>
        </div>
        <button class="btn btn-primary full-btn" type="submit">Login</button>
    </form>

    <p class="sub mt">Run once: <code>/StudentManagementApp/setup</code></p>
</div>
</body>
</html>