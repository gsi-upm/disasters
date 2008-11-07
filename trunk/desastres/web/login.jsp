<%-- 
    Document   : login
    Created on : 01-nov-2008, 18:22:23
    Author     : Administrador
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
  <head>
    <title>Login Page</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  </head>
  <body>
  <h1>Sign in</h1>

  <form action="j_security_check" method="post">
      <table>
          <tr>
            <td>User</td>
            <td><input type="text" name="j_username"/></td>
          </tr>
          <tr>
            <td>Password</td>

            <td><input type="password" name="j_password"/></td>
          </tr>  
          <tr>
            <td><input type="submit" value="enter"/></td>
            <td><input type="reset" value="clean"/></td>
          </tr>
      </table>      
  </form>
  </body>
</html>
