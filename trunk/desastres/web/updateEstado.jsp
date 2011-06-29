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

<c:if test="${param.estadoEvento != 'controlled2' && param.accion != 'curado'}">
	<sql:update dataSource="${CatastrofesServer}">
		UPDATE catastrofes
		SET estado = ?, modificado = <%=modif%>
		WHERE id = ?;
		<sql:param value="${param.estadoEvento}"/>
		<sql:param value="${param.idEvento}"/>
	</sql:update>
</c:if>
<c:if test="${param.accion == 'curado'}">
	<sql:update dataSource="${CatastrofesServer}">
		UPDATE catastrofes
		SET estado = 'active', tipo = 'healthy', modificado = <%=modif%>
		WHERE id = ?;
		<sql:param value="${param.idEvento}"/>
	</sql:update>
</c:if>
<sql:update dataSource="${CatastrofesServer}">
	UPDATE catastrofes
	SET estado = ?, modificado = <%=modif%>, idAssigned = ?
	WHERE nombre = ?
	AND estado != 'erased';
	<sql:param value="${param.estadoUsuario}"/>
	<sql:param value="${param.idEvento}"/>
	<sql:param value="${param.nombreUsuario}"/>
</sql:update>
<c:if test="${param.estadoEvento == 'erased'}">
	<sql:update dataSource="${CatastrofesServer}">
		UPDATE catastrofes
		SET estado = 'active', modificado = <%=modif%>, idAssigned = 0
		WHERE idAssigned = ?
		AND marcador != 'people'
		AND estado != 'erased';
		<sql:param value="${param.idEvento}"/>
	</sql:update>
</c:if>
ok