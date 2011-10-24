<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page language = "java" %>
<%@ page import="java.sql.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %> 
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>

<%@ include file="database.jspf" %>

<c:choose>
	<c:when test="${param.action == 'all'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT id, marcador, tipo, cantidad, nombre, descripcion, info, latitud, longitud,
				direccion, estado,size, traffic, idAssigned, fecha, modificado, nombre_usuario, tipo_usuario
			FROM catastrofes, usuarios
			WHERE modificado > ?
			AND estado != 'erased'
			AND catastrofes.usuario = usuarios.id
			<sql:param value="${param.fecha}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'events'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT id, marcador, tipo, cantidad, nombre, descripcion, info, latitud, longitud,
				direccion, estado,size, traffic, idAssigned, fecha, modificado, nombre_usuario, tipo_usuario
			FROM catastrofes, usuarios
			WHERE modificado > ?
			AND marcador = 'event'
			AND estado != 'erased'
			AND catastrofes.usuario = usuarios.id
			<sql:param value="${param.fecha}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'eventsModified'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT id, marcador, tipo, cantidad, nombre, descripcion, info, latitud, longitud,
				direccion, estado,size, traffic, idAssigned, fecha, modificado, nombre_usuario, tipo_usuario
			FROM catastrofes, usuarios
			WHERE modificado > ?
			AND marcador = 'event'
			AND catastrofes.usuario = usuarios.id
			<sql:param value="${param.fecha}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'resources'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT id, marcador, tipo, cantidad, nombre, descripcion, info, latitud, longitud,
				direccion, estado,size, traffic, idAssigned, fecha, modificado, nombre_usuario, tipo_usuario
			FROM catastrofes, usuarios
			WHERE modificado > ?
			AND marcador = 'resource'
			AND estado != 'erased'
			AND catastrofes.usuario = usuarios.id
			<sql:param value="${param.fecha}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'resourcesModified'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT id, marcador, tipo, cantidad, nombre, descripcion, info, latitud, longitud,
				direccion, estado,size, traffic, idAssigned, fecha, modificado, nombre_usuario, tipo_usuario
			FROM catastrofes, usuarios
			WHERE modificado > ?
			AND marcador = 'resource'
			AND catastrofes.usuario = usuarios.id
			<sql:param value="${param.fecha}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'people'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT id, marcador, tipo, cantidad, nombre, descripcion, info, latitud, longitud,
				direccion, estado,size, traffic, idAssigned, fecha, modificado, nombre_usuario, tipo_usuario
			FROM catastrofes, usuarios
			WHERE modificado > ?
			AND marcador = 'people'
			AND estado != 'erased'
			AND catastrofes.usuario = usuarios.id
			<sql:param value="${param.fecha}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'peopleModified'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT id, marcador, tipo, cantidad, nombre, descripcion, info, latitud, longitud,
				direccion, estado,size, traffic, idAssigned, fecha, modificado, nombre_usuario, tipo_usuario
			FROM catastrofes, usuarios
			WHERE modificado > ?
			AND marcador = 'people'
			AND catastrofes.usuario = usuarios.id
			<sql:param value="${param.fecha}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'id'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT id, marcador, tipo, cantidad, nombre, descripcion, info, latitud, longitud,
				direccion, estado, size, traffic, idAssigned, fecha, modificado, nombre_usuario, tipo_usuario
			FROM catastrofes, usuarios
			WHERE id = ?
			AND estado != 'erased'
			AND catastrofes.usuario = usuarios.id
			<sql:param value="${param.id}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'item'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT id, marcador, tipo, cantidad, nombre, descripcion, info, latitud, longitud,
				direccion, estado,size, traffic, idAssigned, fecha, modificado, nombre_usuario, tipo_usuario
			FROM catastrofes, usuarios
			WHERE marcador = ?
			AND estado != 'erased'
			AND catastrofes.usuario = usuarios.id
			<sql:param value="${param.item}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'type'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT id, marcador, tipo, cantidad, nombre, descripcion, info, latitud, longitud,
				direccion, estado,size, traffic, idAssigned, fecha, modificado, nombre_usuario, tipo_usuario
			FROM catastrofes, usuarios
			WHERE tipo = ?
			AND estado != 'erased'
			AND catastrofes.usuario = usuarios.id
			<sql:param value="${param.type}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'year'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT id, marcador, tipo, cantidad, nombre, descripcion, info, latitud, longitud,
				direccion, estado,size, traffic, idAssigned, fecha, modificado, nombre_usuario, tipo_usuario
			FROM catastrofes, usuarios
			WHERE fecha > ?
			AND estado != 'erased'
			AND modificado < ?
			AND catastrofes.usuario = usuarios.id
			<sql:param value="${param.year1}"/>
			<sql:param value="${param.year2}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'eventsByYear'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT id, marcador, tipo, cantidad, nombre, descripcion, info, latitud, longitud,
				direccion, estado,size, traffic, idAssigned, fecha, modificado, nombre_usuario, tipo_usuario
			FROM catastrofes, usuarios
			WHERE modificado > ?
			AND fecha < ?
			AND marcador = 'event'
			AND estado != 'erased'
			AND catastrofes.usuario = usuarios.id
			<sql:param value="${param.year1}"/>
			<sql:param value="${param.year2}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'peopleByYear'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT id, marcador, tipo, cantidad, nombre, descripcion, info, latitud, longitud,
				direccion, estado,size, traffic, idAssigned, fecha, modificado, nombre_usuario, tipo_usuario
			FROM catastrofes, usuarios
			WHERE modificado > ?
			AND fecha < ?
			AND marcador = 'people'
			AND estado != 'erased'
			AND catastrofes.usuario = usuarios.id
			<sql:param value="${param.year1}"/>
			<sql:param value="${param.year2}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'resourcesByYear'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT id, marcador, tipo, cantidad, nombre, descripcion, info, latitud, longitud,
				direccion, estado,size, traffic, idAssigned, fecha, modificado, nombre_usuario, tipo_usuario
			FROM catastrofes, usuarios
			WHERE modificado > ?
			AND fecha < ?
			AND marcador = 'resource'
			AND estado != 'erased'
			AND catastrofes.usuario = usuarios.id
			<sql:param value="${param.year1}"/>
			<sql:param value="${param.year2}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'associated'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT id, marcador, tipo, cantidad, nombre, descripcion, info, latitud, longitud,
				direccion, estado,size, traffic, idAssigned, fecha, modificado, nombre_usuario, tipo_usuario
			FROM catastrofes, usuarios
			WHERE tipo = ?
			AND estado != 'erased'
			AND idAssigned = ?
			AND catastrofes.usuario = usuarios.id
			<sql:param value="${param.type}"/>
			<sql:param value="${param.id}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'free'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT * FROM resources
			WHERE estado != 'erased'
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'users'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT * FROM catastrofes
			WHERE marcador = 'resource' AND estado != 'erased'
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'usersModified'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT * FROM catastrofes WHERE marcador = 'resource' AND modificado > ?
			<sql:param value="${param.fecha}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'healthy'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT * FROM catastrofes
			WHERE marcador = 'people' AND tipo = 'healthy' AND estado != 'erased'
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'unhealthy'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT * FROM catastrofes
			WHERE marcador = 'people' AND tipo != 'healthy' AND estado != 'erased'
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'slight'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT * FROM catastrofes
			WHERE marcador = 'people' AND tipo = 'slight' AND estado != 'erased'
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'person'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT * FROM catastrofes
			WHERE id = ? AND marcador = 'people' AND estado != 'erased'
			<sql:param value="${param.id}"/>
		</sql:query>
	</c:when>
	
	<c:when test="${param.action == 'user'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT * FROM usuarios u, tipos_usuarios t
			WHERE u.nombre_usuario = ?
			AND u.password = ?
			AND u.tipo_usuario = t.id
			<sql:param value="${param.nombre_usuario}"/>
			<sql:param value="${param.password}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'userRole'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT * FROM usuarios
			WHERE nombre_usuario = ?
			<sql:param value="${param.nombre_usuario}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'proyects'}">
		<sql:query var="proyectos" dataSource="${CatastrofesServer}">
			SELECT * FROM proyectos
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'userProyect'}">
		<sql:query var="proyectos" dataSource="${CatastrofesServer}">
			SELECT * FROM usuarios u, tipos_usuarios t
			WHERE u.nombre_usuario = ?
			AND u.tipo_usuario = t.id
			<sql:param value="${param.nombre_usuario}"/>
		</sql:query>
	</c:when>
</c:choose>
<c:choose>     
    <c:when test="${param.mode == 'xml'}">
		<disasters>
			<c:forEach var="evento" items="${eventos.rows}">
				<disaster>
					<id>${evento.id}</id>
					<item>${evento.marcador}</item>
					<type>${evento.tipo}</type>
					<quantity>${evento.cantidad}</quantity>
					<name>${evento.nombre}</name>
					<description>${evento.descripcion}</description>
					<info>${evento.info}</info>
					<address>${evento.direccion}</address>
					<latitud>${evento.latitud}</latitud>
					<longitud>${evento.longitud}</longitud>
					<state>${evento.estado}</state>
					<size>${evento.size}</size>
					<traffic>${evento.traffic}</traffic>
					<idAssigned>${evento.idAssigned}</idAssigned>
					<date>${evento.fecha}</date>
					<modified>${evento.modificado}</modified>
					<username>${evento.nombre_usuario}</username>
					<usertype>${evento.tipo_usuario}</usertype>
				</disaster>
			</c:forEach>
		</disasters>
	</c:when>
	<c:otherwise>
		[
		<c:forEach var="evento" items="${eventos.rows}">
			<json:object name="temporal">
				<json:property name="id" value="${evento.id}"/>
				<json:property name="item" value="${evento.marcador}"/>
				<json:property name="type" value="${evento.tipo}"/>
				<json:property name="quantity" value="${evento.cantidad}"/>
				<json:property name="name" value="${evento.nombre}"/>
				<json:property name="description" value="${evento.descripcion}"/>
				<json:property name="info" value="${evento.info}"/>
				<json:property name="latitud" value="${evento.latitud}"/>
				<json:property name="longitud" value="${evento.longitud}"/>
				<json:property name="address" value="${evento.direccion}"/>
				<json:property name="state" value="${evento.estado}"/>
				<json:property name="size" value="${evento.size}"/>
				<json:property name="traffic" value="${evento.traffic}"/>
				<json:property name="idAssigned" value="${evento.idAssigned}"/>
				<json:property name="date" value="${evento.fecha}"/>
				<json:property name="modified" value="${evento.modificado}"/>
				<json:property name="user_name" value="${evento.nombre_usuario}"/>
				<json:property name="user_type" value="${evento.tipo_usuario}"/>
				<json:property name="real_name" value="${evento.nombre_real}"/>
				<json:property name="email" value="${evento.correo}"/>
			</json:object> ,
		</c:forEach>
		<c:forEach var="proyecto" items="${proyectos.rows}">
			<json:object name="temporal">
				<json:property name="proyect" value="${proyecto.proyecto}"/>
				<json:property name="rol" value="${proyecto.tipo}"/>
			</json:object> ,
		</c:forEach>
		]
    </c:otherwise>
</c:choose>