<%@ page contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.Date" %>
<%@ page isELIgnored = "false" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %> 
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>

<%@ include file="database.jspf" %> 

<sql:query var="datos" dataSource="${CatastrofesServer}" sql="
	SELECT latitud, longitud, localizacion FROM usuarios
	WHERE nombre_usuario = ?;">
	<sql:param value="${param.nombre}"/>
</sql:query>
[
<c:forEach var="dato" items="${datos.rows}">
    <json:object name="temp">
        <json:property name="latitud" value="${dato.latitud}"/>
        <json:property name="longitud" value="${dato.longitud}"/>
		<json:property name="localizacion" value="${dato.localizacion}"/>
	</json:object> ,
</c:forEach>
]
ok