<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="gsi.project.Constantes, java.sql.DriverManager, java.sql.Statement, java.sql.Timestamp, java.util.Date"%>

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
				String modif = "'" + new Timestamp(new Date().getTime()).toString() + "'";
				java.sql.Connection conexion = DriverManager.getConnection(url, Constantes.DB_USER, Constantes.DB_PASS);
				Statement s = conexion.createStatement();
				s.executeUpdate("UPDATE CATASTROFES SET ESTADO = (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased'), MODIFICADO = " +
					modif + " WHERE NOMBRE = '" + request.getParameter("nombre") +
					"' AND MARCADOR = (SELECT ID FROM TIPOS_MARCADORES WHERE TIPO_MARCADOR = 'resource')");
				conexion.close();
			}catch(ClassNotFoundException ex){
				System.out.println("ClassNotFoundExcepcion: " + ex);
			}
			
			response.sendRedirect("index.jsp");
		%>
    </body>
</html>
