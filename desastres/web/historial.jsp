<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>

<c:set var="databaseEmpotrado">
	<jsp:scriptlet>
		out.print(application.getRealPath("/WEB-INF/db/improvise"));
	</jsp:scriptlet>
</c:set>
<sql:setDataSource var="CatastrofesServer" driver="org.hsqldb.jdbcDriver"
	url="jdbc:hsqldb:file:${databaseEmpotrado}" user="sa" password=""/>

<!DOCTYPE HTML>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Historial</title>
    </head>
    <body>
        <sql:query var="entradas" dataSource="${CatastrofesServer}"
			sql="SELECT * FROM historial">
		</sql:query>
		<table border="1">
			<tr><th>ID</th><th>USUARIO</th><th>EVENTO</th><th>FECHA</th></tr>
			<c:forEach var="fila" items="${entradas.rows}">
				<tr>
					<td>${fila.id}</td>
					<td>${fila.usuario}</td>
					<td>${fila.evento}</td>
					<td>${fila.fecha}</td>
				</tr>
			</c:forEach>
		</table>
    </body>
</html>