<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@page import="java.sql.Timestamp, java.util.Date"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@taglib prefix="json" uri="http://www.atg.com/taglibs/json"%>

<%@include file="../jspf/database.jspf"%>

<c:choose>
	<c:when test="${param.action != 'insertar' && param.action != 'registrar'}">
		<c:choose>
			<c:when test="${param.action == 'user'}">
				<sql:query var="eventos" dataSource="${CatastrofesServer}">
					SELECT u.id, u.nombre_usuario, u.nombre_real, u.correo, u.latitud, u.longitud, u.planta, t.tipo
					FROM usuarios u, tipos_usuarios t
					WHERE u.nombre_usuario = ?
					AND u.password = ?
					AND u.tipo_usuario = t.id
					<sql:param value="${param.nombre_usuario}"/>
					<sql:param value="${param.password}"/>
				</sql:query>
			</c:when>
			<c:when test="${param.action == 'userProyect'}">
				<sql:query var="proyectos" dataSource="${CatastrofesServer}">
					SELECT u.id, t.tipo, t.nivel
					FROM usuarios u, tipos_usuarios t
					WHERE u.nombre_usuario = ?
					AND u.tipo_usuario = t.id
					<sql:param value="${param.nombre_usuario}"/>
				</sql:query>
			</c:when>
			<c:when test="${param.action == 'userRole'}">
				<sql:query var="eventos" dataSource="${CatastrofesServer}">
					SELECT u.id, u.nombre_usuario, u.nombre_real, u.correo, u.latitud, u.longitud, t.tipo
					FROM usuarios u, tipos_usuarios t
					WHERE u.nombre_usuario = ?
					AND u.tipo_usuario = t.id
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
			<c:forEach var="proyect" items="${proyectos.rows}">
				<json:object>
					<json:property name="id" value="${proyect.id}"/>
					<json:property name="rol" value="${proyect.tipo}"/>
					<json:property name="level" value="${proyect.nivel}"/>
				</json:object>
			</c:forEach>
		</json:array>
	</c:when>
	<c:when test="${param.action == 'insertar'}">
		<% String modif = "'" + new Timestamp(new Date().getTime()).toString() + "'"; %>
		<sql:update dataSource="${CatastrofesServer}">
			INSERT INTO catastrofes(marcador, tipo, cantidad, nombre, descripcion, info, latitud,
				longitud, direccion, estado, size, traffic, idAssigned, fecha, usuario, planta)
			VALUES(3, (SELECT id FROM tipos_catastrofes WHERE tipo_catastrofe = ?),
				1, ?, ?, ?, ?, ?, '', 1, '', '', 0, <%=modif%>, 1, ?)
			<sql:param value="${param.type}"/>
			<sql:param value="${param.name}"/>
			<sql:param value="${param.description}"/>
			<sql:param value="${param.info}"/>
			<sql:param value="${param.latitud}"/>
			<sql:param value="${param.longitud}"/>
			<sql:param value="${param.planta}"/>
		</sql:update>
	</c:when>
	<c:when test="${param.action == 'registrar'}">
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
	</c:when>
</c:choose>