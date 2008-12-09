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
        
        <style type="text/css">
            body { 
                font-size: 100%;
                font-family: "Trebuchet MS",Verdana, Arial, Helvetica, sans-serif;
                padding-top: 100px;
            }
            label { display: block; margin-top: 10px; }
            #login { width: 300px; margin: 0 auto; border: 1px solid #eee; padding: 25px; background-color: #FFFFCC; }
            a { color: #0066CC; text-decoration: none; border-bottom: 1px dotted #0066cc; }
            #submit_butt { margin-top: 15px; }
            h3 { margin-top: 0; }
        </style> 
        
        <style type='text/css'>
            @import 'css/keyboardstyle.css';
        </style>
        
        <script
            type='text/javascript'
            src='js/jquery-1.2.6.min.js'>
        </script>
        <script
            type='text/javascript'
            src='js/jquery-fieldselection.js'>
        </script>
        <script
            type='text/javascript'
            src='js/jquery-ui-personalized-1.5.2.min.js'>
        </script>
        <script
            type='text/javascript'
            src='js/vkeyboard.js'>
        </script>
        
    </head>
    <body>
        <h1>Sign in</h1>
        
        <div id="login">
            <form action="j_security_check" method="post" id="loginform">
                <label for="username">Username:</label>
                <input type="text" name="j_username" id="username" />
                
                <label for="pwd">Password:</label>
                <input type="password" name="j_password" id="pwd"/>
                <a href="#" id="showkeyboard" title="Type in your password using a virtual keyboard.">Keyboard</a> <br />
                
                <input type="submit" name="Submit" id="submit_butt" value="Submit" />
            </form>
            
            <jsp:include page="keyboard.jsp"></jsp:include>
            
        </div>
        
    </body>
</html>
