<%@page contentType="application/x-sql" pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>

<%@include file="../jspf/database.jspf"%>

<sql:update dataSource="${CatastrofesServer}">
	INSERT INTO MENSAJES(CREADOR, TIPO_RECEPTOR, RECEPTOR, MENSAJE)
	VALUES(?, ?, ?, ?)
	<sql:param value="${param.creador}"/>
	<sql:param value="${param.tipo_receptor}"/>
	<sql:param value="${param.receptor}"/>
	<sql:param value="${param.mensaje}"/>
</sql:update>