<%@page contentType="application/json" pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@page import="java.sql.Timestamp, java.util.Date"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@taglib prefix="json" uri="http://www.atg.com/taglibs/json"%>

<%@include file="../jspf/database.jspf"%>
<!--  Fecha de hace 5 minutos -->
<% String hace5min = "'" + new Timestamp(new Date().getTime() - 300000).toString() + "'"; %>

<c:if test="${param.action == 'firstTime'}">
	<sql:query var="mensajes" dataSource="${CatastrofesServer}">
		SELECT * FROM mensajes
		WHERE fecha > <%= hace5min %> AND nivel <= ?
		<sql:param value="${param.nivel}"/>
	</sql:query>
</c:if>
<c:if test="${param.action == 'notFirst'}">
	<sql:query var="mensajes" dataSource="${CatastrofesServer}">
		SELECT * FROM mensajes
		WHERE id > ? AND nivel <= ?
		<sql:param value="${param.id}"/>
		<sql:param value="${param.nivel}"/>
	</sql:query>
</c:if>

<json:array>
	<c:forEach var="mensaje" items="${mensajes.rows}">
		<json:object>
			<json:property name="id" value="${mensaje.id}"/>
			<json:property name="creador" value="${mensaje.creador}"/>
			<json:property name="mensaje" value="${mensaje.mensaje}"/>
			<json:property name="nivel" value="${mensaje.nivel}"/>
			<json:property name="fecha" value="${mensaje.fecha}"/>
		</json:object>
	</c:forEach>
</json:array>