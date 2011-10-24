<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="java.sql.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>

<%@ include file="database.jspf" %>
<% String modif = "'" + new Timestamp(new java.util.Date().getTime()).toString() + "'"; %>

<c:choose>
	<c:when test="${param.action eq 'id'}">
		<sql:update dataSource="${CatastrofesServer}">
			UPDATE catastrofes
			SET estado = 'erased', modificado = <%=modif%>
			WHERE id = ?
			<sql:param value="${param.id}"/>
		</sql:update>
	</c:when>
	<c:when test="${param.action eq 'events'}">
		<sql:update dataSource="${CatastrofesServer}">
			UPDATE catastrofes
			SET estado = 'erased', modificado = <%=modif%>
			WHERE marcador = 'event'
		</sql:update>
	</c:when>
	<c:when test="${param.action eq 'all'}">
		<sql:update dataSource="${CatastrofesServer}" >
			UPDATE catastrofes
			SET estado = 'erased', modificado = <%=modif%>
		</sql:update>
	</c:when>
	<c:when test="${param.action eq 'healthy'}">
		<sql:update dataSource="${CatastrofesServer}" >
			UPDATE catastrofes
			SET tipo = 'healthy', estado ='active', modificado = <%=modif%>
			WHERE id = ?
			<sql:param value="${param.id}"/>
		</sql:update>
	</c:when>
</c:choose>
OK