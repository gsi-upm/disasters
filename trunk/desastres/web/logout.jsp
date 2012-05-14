<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="gsi.project.Constantes, java.sql.*"%>

<!DOCTYPE HTML>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>Log out</title>
    </head>
    <body>
        <%
			session.invalidate();
			
			String url = Constantes.DB_URL;
			if(Constantes.DB.equals("hsqldb")){
				url += application.getRealPath("/WEB-INF/db/" + Constantes.PROJECT);
			}
			try{
				Class.forName(Constantes.DB_DRIVER);
				java.sql.Connection conexion = DriverManager.getConnection(url, Constantes.DB_USER, Constantes.DB_PASS);
				Statement s = conexion.createStatement();
				s.executeUpdate("DELETE FROM CATASTROFES WHERE NOMBRE = '" + request.getParameter("nombre") +
					"' AND MARCADOR = (SELECT ID FROM TIPOS_MARCADORES WHERE TIPO_MARCADOR = 'resource')");
				conexion.close();
			}catch(Exception ex){
				System.out.println("Excepcion: " + ex);
			}
			
			response.sendRedirect("index.jsp");
		%>
    </body>
</html>
