<%@ page contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<%@ page isELIgnored = "false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>

<%@ include file="database.jspf" %>

<sql:update dataSource="${CatastrofesServer}">
	INSERT INTO mensajes(mensaje, nivel)
	VALUES(?,?)
	<sql:param value="${param.mensaje}"/>
	<sql:param value="${param.nivel}"/>
</sql:update>