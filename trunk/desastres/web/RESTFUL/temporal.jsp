<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@page import="java.sql.Timestamp, java.util.Date"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@taglib prefix="json" uri="http://www.atg.com/taglibs/json"%>

<%@include file="../jspf/database.jspf"%>

<c:choose>
	<c:when test="${param.action == 'insertar'}">
		<% String modif = "'" + new Timestamp(new Date().getTime()).toString() + "'"; %>
		<c:set var="estado" value="1"/>
		<sql:query var="conectados" dataSource="${CatastrofesServer}">
			SELECT ID, ESTADO FROM CATASTROFES
			WHERE NOMBRE = ? AND ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
			<sql:param value="${param.name}"/>
		</sql:query>
		<c:forEach var="conect" items="${conectados.rows}">
			<c:set var="estado" value="${conect.estado}"/>
			<sql:update dataSource="${CatastrofesServer}">
				UPDATE CATASTROFES
				SET ESTADO = (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased'), MODIFICADO = <%=modif%>
				WHERE ID = ?
				<sql:param value="${conect.id}"/>
			</sql:update>
		</c:forEach>
		<sql:update dataSource="${CatastrofesServer}">
			INSERT INTO CATASTROFES(MARCADOR, TIPO, CANTIDAD, NOMBRE, DESCRIPCION, INFO, LATITUD,
				LONGITUD, DIRECCION, ESTADO, SIZE, TRAFFIC, IDASSIGNED, FECHA, USUARIO, PLANTA)
			VALUES((SELECT id FROM TIPOS_MARCADORES WHERE TIPO_MARCADOR = 'resource'),
				(SELECT ID FROM TIPOS_CATASTROFES WHERE TIPO_CATASTROFE = ?),
				1, ?, ?, ?, ?, ?, '', ?, '', '', 0, <%=modif%>, 1, ?)
			<sql:param value="${param.type}"/>
			<sql:param value="${param.name}"/>
			<sql:param value="${param.description}"/>
			<sql:param value="${param.info}"/>
			<sql:param value="${param.latitud}"/>
			<sql:param value="${param.longitud}"/>
			<sql:param value="${estado}"/>
			<sql:param value="${param.planta}"/>
		</sql:update>
	</c:when>
	<c:when test="${param.action == 'user'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT U.ID, TIPO, NOMBRE_USUARIO, NOMBRE_REAL, CORREO, LATITUD, LONGITUD, PLANTA
			FROM USUARIOS U, TIPOS_USUARIOS T
			WHERE NOMBRE_USUARIO = ?
			AND PASSWORD = ?
			AND TIPO_USUARIO = T.ID
			<sql:param value="${param.nombre_usuario}"/>
			<sql:param value="${param.password}"/>
		</sql:query>
		<json:array>
			<c:forEach var="evento" items="${eventos.rows}">
				<json:object>
					<json:property name="id" value="${evento.id}"/>
					<json:property name="user_name" value="${evento.nombre_usuario}"/>
					<json:property name="user_type" value="${evento.tipo}"/>
					<json:property name="real_name" value="${evento.nombre_real}"/>
					<json:property name="email" value="${evento.correo}"/>
					<json:property name="latitud" value="${evento.latitud}"/>
					<json:property name="longitud" value="${evento.longitud}"/>
					<json:property name="planta" value="${evento.planta}"/>
				</json:object>
			</c:forEach>
		</json:array>
	</c:when>
	<c:when test="${param.action == 'userRole'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT U.ID, TIPO, NIVEL
			FROM USUARIOS U, TIPOS_USUARIOS T
			WHERE NOMBRE_USUARIO = ?
			AND TIPO_USUARIO = T.ID
			<sql:param value="${param.nombre_usuario}"/>
		</sql:query>
		<json:array>
			<c:forEach var="evento" items="${eventos.rows}">
				<json:object>
					<json:property name="id" value="${evento.id}"/>
					<json:property name="rol" value="${evento.tipo}"/>
					<json:property name="level" value="${evento.nivel}"/>
				</json:object>
			</c:forEach>
		</json:array>
	</c:when>
</c:choose>