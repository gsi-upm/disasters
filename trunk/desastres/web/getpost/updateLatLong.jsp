<%@page contentType="application/x-sql" pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@page import="java.sql.Timestamp, java.util.Date"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@taglib prefix="json" uri="http://www.atg.com/taglibs/json"%>

<%@include file="../jspf/database.jspf"%>
<% String modif = "'" + new Timestamp(new Date().getTime()).toString() + "'"; %>

<c:if test="${param.nombre != null && param.localizacion == null && param.planta == null}">
	<sql:update dataSource="${CatastrofesServer}">
		UPDATE CATASTROFES	
		SET LATITUD = ?, LONGITUD = ?, MODIFICADO = <%=modif%>
		WHERE NOMBRE = ?
		AND ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
		<sql:param value="${param.latitud}"/>
		<sql:param value="${param.longitud}"/>
		<sql:param value="${param.nombre}"/>
	</sql:update>
	<c:if test="${param.porDefecto == true}">
		<sql:update dataSource="${CatastrofesServer}">
			UPDATE USUARIOS
			SET LATITUD = ?, LONGITUD = ?
			WHERE NOMBRE_USUARIO = ?
			<sql:param value="${param.latitud}"/>
			<sql:param value="${param.longitud}"/>
			<sql:param value="${param.nombre}"/>
		</sql:update>
	</c:if>
</c:if>
<c:if test="${param.id != null}">
	<sql:update dataSource="${CatastrofesServer}">
		UPDATE CATASTROFES
		SET LATITUD = ?, LONGITUD = ?, MODIFICADO = <%=modif%>
		WHERE ID = ?
		<sql:param value="${param.latitud}"/>
		<sql:param value="${param.longitud}"/>
		<sql:param value="${param.id}"/>
	</sql:update>
</c:if>
<c:if test="${param.localizacion == true}">
	<sql:update dataSource="${CatastrofesServer}">
		UPDATE USUARIOS
		SET LOCALIZACION IS TRUE
		WHERE NOMBRE_USUARIO = ?
		<sql:param value="${param.nombre}"/>
	</sql:update>
</c:if>
<c:if test="${param.localizacion == false}">
	<sql:update dataSource="${CatastrofesServer}">
		UPDATE USUARIOS
		SET LOCALIZACION IS FALSE
		WHERE NOMBRE_USUARIO = ?
		<sql:param value="${param.nombre}"/>
	</sql:update>
</c:if>
<c:if test="${param.planta != null}">
	<sql:update dataSource="${CatastrofesServer}">
		UPDATE CATASTROFES
		SET PLANTA = ?, MODIFICADO = <%=modif%>
		WHERE NOMBRE = ?
		<sql:param value="${param.planta}"/>
		<sql:param value="${param.nombre}"/>
	</sql:update>
	<c:if test="${param.plantaPorDefecto == true}">
		<sql:update dataSource="${CatastrofesServer}">
			UPDATE USUARIOS
			SET PLANTA = ?
			WHERE NOMBRE_USUARIO = ?
			<sql:param value="${param.planta}"/>
			<sql:param value="${param.nombre}"/>
		</sql:update>
	</c:if>
</c:if>