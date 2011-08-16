<%@ page contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.Date" %>
<%@ page isELIgnored = "false" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %> 
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>

<%@ include file="database.jspf" %>

<% String modif = "'" + new Timestamp(new java.util.Date().getTime()).toString() + "'";%>  

<c:if test="${param.nombre ne null and param.localizacion eq null}">
	<sql:update dataSource="${CatastrofesServer}">
	    UPDATE CATASTROFES SET
		latitud = ?, longitud = ?, modificado = <%=modif%>
	    WHERE nombre = ?
		AND estado != 'erased';
		<sql:param value="${param.latitud}"/>
		<sql:param value="${param.longitud}"/>
		<sql:param value="${param.nombre}"/>
	</sql:update>
</c:if>
<c:if test="${param.id ne null}">
	<sql:update dataSource="${CatastrofesServer}">
	    UPDATE CATASTROFES SET
		latitud = ?, longitud = ?, modificado = <%=modif%>
	    WHERE id = ?;
		<sql:param value="${param.latitud}"/>
		<sql:param value="${param.longitud}"/>
		<sql:param value="${param.id}"/>
	</sql:update>
</c:if>
<c:if test="${param.porDefecto}">
	<sql:update dataSource="${CatastrofesServer}">
	    UPDATE USUARIOS SET
		latitud = ?, longitud = ?
	    WHERE nombre_usuario = ?;
		<sql:param value="${param.latitud}"/>
		<sql:param value="${param.longitud}"/>
		<sql:param value="${param.nombre}"/>
	</sql:update>
</c:if>
<c:if test="${param.localizacion ne null}">
	<sql:update dataSource="${CatastrofesServer}">
	    UPDATE USUARIOS SET
		localizacion = ?
	    WHERE nombre_usuario = ?;
		<sql:param value="${param.localizacion}"/>
		<sql:param value="${param.nombre}"/>
	</sql:update>
</c:if>
ok