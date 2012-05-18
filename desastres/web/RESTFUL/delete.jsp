<%@page contentType="applicacion/x-sql" pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@page import="java.sql.Timestamp, java.util.Date"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>

<%@include file="../jspf/database.jspf"%>
<% String modif = "'" + new Timestamp(new Date().getTime()).toString() + "'"; %>

<c:choose>
	<c:when test="${param.action eq 'id'}">
		<sql:update dataSource="${CatastrofesServer}">
			UPDATE CATASTROFES
			SET ESTADO = (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased'), MODIFICADO = <%=modif%>
			WHERE ID = ?
			<sql:param value="${param.id}"/>
		</sql:update>
	</c:when>
	<c:when test="${param.action eq 'events'}">
		<sql:update dataSource="${CatastrofesServer}">
			UPDATE CATASTROFES
			SET ESTADO = (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased'), MODIFICADO = <%=modif%>
			WHERE MARCADOR = (SELECT ID FROM TIPOS_MARCADORES WHERE TIPO_MARCADOR = 'event')
		</sql:update>
	</c:when>
	<c:when test="${param.action eq 'all'}">
		<sql:update dataSource="${CatastrofesServer}">
			UPDATE CATASTROFES
			SET ESTADO = (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased'), MODIFICADO = <%=modif%>
		</sql:update>
	</c:when>
	<c:when test="${param.action eq 'healthy'}">
		<sql:update dataSource="${CatastrofesServer}">
			UPDATE CATASTROFES
			SET tipo = (SELECT ID FROM TIPOS_CATASTROFES WHERE TIPO_CATASTROFE = 'healthy'),
				ESTADO = (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'active'), MODIFICADO = <%=modif%>
			WHERE ID = ?
			<sql:param value="${param.id}"/>
		</sql:update>
	</c:when>
	<c:when test="${param.action eq 'resource'}">
		<sql:update dataSource="${CatastrofesServer}">
			UPDATE CATASTROFES
			SET ESTADO = (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased'), MODIFICADO = <%=modif%>
			WHERE NOMBRE = ? AND MARCADOR = (SELECT ID FROM TIPOS_MARCADORES WHERE TIPO_MARCADOR = 'resource')
			<sql:param value="${param.nombre}"/>
		</sql:update>
	</c:when>
</c:choose>