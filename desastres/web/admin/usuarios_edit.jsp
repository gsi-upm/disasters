<%@page contentType="applicacion/x-sql" pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@page import="security.HashAlgorithm"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>

<%@include file="../jspf/database.jspf"%>

<c:choose>
	<c:when test="${param.accion == 'modificar'}">
		<c:if test="${param.localizacion == true}">
			<sql:update dataSource="${CatastrofesServer}">
				UPDATE USUARIOS
				SET TIPO_USUARIO = ?, NOMBRE_REAL = ?, CORREO = ?, LATITUD = ?, LONGITUD = ?, LOCALIZACION IS TRUE
				WHERE ID = ?
				<sql:param value="${param.tipo_usuario}"/>
				<sql:param value="${param.nombre_real}"/>
				<sql:param value="${param.correo}"/>
				<sql:param value="${param.latitud}"/>
				<sql:param value="${param.longitud}"/>
				<sql:param value="${param.id}"/>
			</sql:update>
		</c:if>
		<c:if test="${param.localizacion == false}">
			<sql:update dataSource="${CatastrofesServer}">
				UPDATE USUARIOS
				SET TIPO_USUARIO = ?, NOMBRE_REAL = ?, CORREO = ?, LATITUD = ?, LONGITUD = ?, LOCALIZACION IS FALSE
				WHERE ID = ?
				<sql:param value="${param.tipo_usuario}"/>
				<sql:param value="${param.nombre_real}"/>
				<sql:param value="${param.correo}"/>
				<sql:param value="${param.latitud}"/>
				<sql:param value="${param.longitud}"/>
				<sql:param value="${param.id}"/>
			</sql:update>
		</c:if>
	</c:when>
	<c:when test="${param.accion == 'crear'}">
		<c:set var="password">
			<%
				String usuario = request.getParameter("nombre_usuario");
				String password = request.getParameter("password");
				out.print(HashAlgorithm.SHA256(password, usuario));
			%>
		</c:set>
		<sql:update dataSource="${CatastrofesServer}">
			INSERT INTO USUARIRIOS(NOMBRE_USUARIO, PASSWORD, TIPO_USUARIO, NOMBRE_REAL, CORREO, LATITUD, LONGITUD, LOCALIZACION, PROYECTO)
			VALUES(?, ?, ?, ?, ?, ?, ?, ?, 'caronte')
			<sql:param value="${param.nombre_usuario}"/>
			<sql:param value="${password}"/>
			<sql:param value="${param.tipo_usuario}"/>
			<sql:param value="${param.nombre_real}"/>
			<sql:param value="${param.correo}"/>
			<sql:param value="${param.latitud}"/>
			<sql:param value="${param.longitud}"/>
			<sql:param value="${param.localizacion}"/>
		</sql:update>
	</c:when>
	<c:when test="${param.accion == 'eliminar'}">
		<sql:update dataSource="${CatastrofesServer}">
			DELETE FROM USUARIOS WHERE ID = ?
			<sql:param value="${param.id}"/>
		</sql:update>		
	</c:when>
</c:choose>