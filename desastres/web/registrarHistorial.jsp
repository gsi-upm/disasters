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

<c:catch var="errorUpdate">
	<sql:update dataSource="${CatastrofesServer}">
		INSERT INTO historial (usuario,evento)
		VALUES (SELECT id_usuarios FROM usuarios WHERE nombre_usuario = ?,?)
		<sql:param value="${param.usuario}"/>
		<sql:param value="${param.evento}"/>
	</sql:update>
</c:catch>
ok