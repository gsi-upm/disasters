<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="java.sql.Timestamp, java.util.Date" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>

<%@ include file="../jspf/database.jspf" %>
<% String modif = "'" + new Timestamp(new Date().getTime()).toString() + "'"; %>

<c:choose>
	<c:when test="${param.action eq 'id'}">
		<sql:update dataSource="${CatastrofesServer}">
			UPDATE catastrofes
			SET estado = (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased'), modificado = <%=modif%>
			WHERE id = ?
			<sql:param value="${param.id}"/>
		</sql:update>
	</c:when>
	<c:when test="${param.action eq 'events'}">
		<sql:update dataSource="${CatastrofesServer}">
			UPDATE catastrofes
			SET estado = (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased'), modificado = <%=modif%>
			WHERE marcador = 'event'
		</sql:update>
	</c:when>
	<c:when test="${param.action eq 'all'}">
		<sql:update dataSource="${CatastrofesServer}" >
			UPDATE catastrofes
			SET estado = (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased'), modificado = <%=modif%>
		</sql:update>
	</c:when>
	<c:when test="${param.action eq 'healthy'}">
		<sql:update dataSource="${CatastrofesServer}" >
			UPDATE catastrofes
			SET tipo = (SELECT id FROM tipos_catastrofes WHERE tipo_catastrofe = 'healthy'),
				estado = (SELECT id FROM tipos_estados WHERE tipo_estado = 'active'), modificado = <%=modif%>
			WHERE id = ?
			<sql:param value="${param.id}"/>
		</sql:update>
	</c:when>
</c:choose>
OK