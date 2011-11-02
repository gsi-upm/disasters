<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page isELIgnored = "false" %>
<%@ page import="java.sql.Timestamp, java.util.Date" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %> 
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>

<%@ include file="../jspf/database.jspf" %>
<% String modif = "'" + new Timestamp(new Date().getTime()).toString() + "'"; %>

<c:if test="${param.nombre != null && param.localizacion == null}">
	<sql:update dataSource="${CatastrofesServer}">
		UPDATE catastrofes
		SET latitud = ?, longitud = ?, modificado = <%=modif%>
		WHERE nombre = ?
		AND estado != (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased')
		<sql:param value="${param.latitud}"/>
		<sql:param value="${param.longitud}"/>
		<sql:param value="${param.nombre}"/>
	</sql:update>
</c:if>
<c:if test="${param.id != null}">
	<sql:update dataSource="${CatastrofesServer}">
		UPDATE catastrofes
		SET latitud = ?, longitud = ?, modificado = <%=modif%>
		WHERE id = ?
		<sql:param value="${param.latitud}"/>
		<sql:param value="${param.longitud}"/>
		<sql:param value="${param.id}"/>
	</sql:update>
</c:if>
<c:if test="${param.porDefecto == true}">
	<sql:update dataSource="${CatastrofesServer}">
		UPDATE usuarios
		SET latitud = ?, longitud = ?
		WHERE nombre_usuario = ?
		<sql:param value="${param.latitud}"/>
		<sql:param value="${param.longitud}"/>
		<sql:param value="${param.nombre}"/>
	</sql:update>
</c:if>
<c:if test="${param.localizacion != null}">
	<sql:update dataSource="${CatastrofesServer}">
		UPDATE usuarios
		SET localizacion = ?
		WHERE nombre_usuario = ?
		<sql:param value="${param.localizacion}"/>
		<sql:param value="${param.nombre}"/>
	</sql:update>
</c:if>
ok