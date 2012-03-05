<%@page contentType="application/json" pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@taglib prefix="json" uri="http://www.atg.com/taglibs/json"%>

<%@include file="../jspf/database.jspf"%>

<sql:query var="datos" dataSource="${CatastrofesServer}">
	SELECT LATITUD, LONGITUD, LOCALIZACION
	FROM USUARIOS
	WHERE NOMBRE_USUARIO = ?
	<sql:param value="${param.nombre}"/>
</sql:query>

<json:array>
	<c:forEach var="dato" items="${datos.rows}">
		<json:object>
			<json:property name="latitud" value="${dato.latitud}"/>
			<json:property name="longitud" value="${dato.longitud}"/>
			<json:property name="localizacion" value="${dato.localizacion}"/>
		</json:object>
	</c:forEach>
</json:array>