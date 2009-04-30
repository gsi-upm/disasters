<%-- 
    Document   : logout
    Created on : 15-dic-2008, 17:07:16
    Author     : Administrador
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Log out</title>
    </head>
    <body>
        <% session.invalidate(); %>
        You have been logged out of the SecurityFilter example application.<p>
        <% response.sendRedirect("index.jsp"); %>
    </body>
</html>
