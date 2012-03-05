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
			SELECT id, estado FROM catastrofes
			WHERE nombre = ? AND estado != (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased')
			<sql:param value="${param.name}"/>
		</sql:query>
		<c:forEach var="conect" items="${conectados.rows}">
			<c:set var="estado" value="${conect.estado}"/>
			<sql:update dataSource="${CatastrofesServer}">
				UPDATE catastrofes
				SET estado = (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased'), modificado = <%=modif%>
				WHERE id = ?
				<sql:param value="${conect.id}"/>
			</sql:update>
		</c:forEach>
		<sql:update dataSource="${CatastrofesServer}">
			INSERT INTO catastrofes(marcador, tipo, cantidad, nombre, descripcion, info, latitud,
				longitud, direccion, estado, size, traffic, idAssigned, fecha, usuario, planta)
			VALUES((SELECT id FROM tipos_marcadores WHERE tipo_marcador = 'resource'),
				(SELECT id FROM tipos_catastrofes WHERE tipo_catastrofe = ?),
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
	<%--<c:when test="${param.action == 'registrar'}">
		<c:catch var="errorInsert">
			<sql:update dataSource="${CatastrofesServer}">
				INSERT INTO usuarios(nombre_usuario, password, tipo_usuario, nombre_real,
					correo, latitud, longitud, localizacion, proyecto)
				VALUES(?, ?, 11, ?, ?, 0.0, 0.0, FALSE, 'caronte')
				<sql:param value="${param.user}"/>
				<sql:param value="${param.pass}"/>
				<sql:param value="${param.nombre}"/>
				<sql:param value="${param.email}"/>
			</sql:update>
		</c:catch>
	</c:when>--%>
	<c:otherwise>
		<c:choose>
			<c:when test="${param.action == 'user'}">
				<sql:query var="eventos" dataSource="${CatastrofesServer}">
					SELECT u.id, tipo nombre_usuario, nombre_real, correo, latitud, longitud, planta
					FROM usuarios u, tipos_usuarios t
					WHERE nombre_usuario = ?
					AND password = ?
					AND tipo_usuario = t.id
					<sql:param value="${param.nombre_usuario}"/>
					<sql:param value="${param.password}"/>
				</sql:query>
			</c:when>
			<c:when test="${param.action == 'userProject'}">
				<sql:query var="proyectos" dataSource="${CatastrofesServer}">
					SELECT u.id, tipo, nivel
					FROM usuarios u, tipos_usuarios t
					WHERE nombre_usuario = ?
					AND tipo_usuario = t.id
					<sql:param value="${param.nombre_usuario}"/>
				</sql:query>
			</c:when>
			<c:when test="${param.action == 'userRole'}">
				<sql:query var="eventos" dataSource="${CatastrofesServer}">
					SELECT u.id, tipo, nombre_usuario, nombre_real, correo, latitud, longitud
					FROM usuarios u, tipos_usuarios t
					WHERE nombre_usuario = ?
					AND tipo_usuario = t.id
					<sql:param value="${param.nombre_usuario}"/>
				</sql:query>
			</c:when>
		</c:choose>
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
			<c:forEach var="proyecto" items="${proyectos.rows}">
				<json:object>
					<json:property name="id" value="${proyecto.id}"/>
					<json:property name="rol" value="${proyecto.tipo}"/>
					<json:property name="level" value="${proyecto.nivel}"/>
				</json:object>
			</c:forEach>
		</json:array>
	</c:otherwise>
</c:choose>