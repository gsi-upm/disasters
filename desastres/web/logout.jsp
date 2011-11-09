<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE HTML>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>Log out</title>
    </head>
    <body>
        <% session.invalidate(); %>
        <p>You have been logged out of the SecurityFilter example application.</p>
        <% response.sendRedirect("index.jsp"); %>
    </body>
</html>
