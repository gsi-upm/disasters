<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page language = "java"  %>
<%@ page import="java.sql.*" %>
<%--@ page import="catastrofes.*" --%>


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %> 
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>

<c:set var="databaseEmpotrado">
    <jsp:scriptlet>
		out.print(application.getRealPath("/WEB-INF/db/improvise"));
	</jsp:scriptlet>
</c:set>

<sql:setDataSource var="CatastrofesServer" driver="org.hsqldb.jdbcDriver" 
	url="jdbc:hsqldb:file:${databaseEmpotrado}" user="sa" password=""/>

<c:choose>
	<c:when test="${param.action eq 'all'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}" sql="
				   SELECT id, marcador, tipo, cantidad, nombre, descripcion, info, latitud, longitud,
				   direccion, estado,size, traffic, idAssigned, fecha, modificado, nombre_usuario, tipo_usuario FROM catastrofes, usuarios
				   WHERE modificado > ?
				   AND estado != 'erased'
				   AND catastrofes.usuario=usuarios.id_usuarios;">
			<sql:param value="${param.fecha}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action eq 'events'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}" sql="
				   SELECT id, marcador, tipo, cantidad, nombre, descripcion, info, latitud, longitud,
				   direccion, estado,size, traffic, idAssigned, fecha, modificado, nombre_usuario, tipo_usuario FROM catastrofes, usuarios
				   WHERE modificado > ?
				   AND marcador = 'event'
				   AND estado != 'erased'
				   AND catastrofes.usuario=usuarios.id_usuarios;">
			<sql:param value="${param.fecha}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action eq 'eventsModified'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}" sql="
				   SELECT id, marcador, tipo, cantidad, nombre, descripcion, info, latitud, longitud,
				   direccion, estado,size, traffic, idAssigned, fecha, modificado, nombre_usuario, tipo_usuario FROM catastrofes, usuarios
				   WHERE modificado > ?
				   AND marcador = 'event'
				   AND catastrofes.usuario=usuarios.id_usuarios;">
			<sql:param value="${param.fecha}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action eq 'resources'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}" sql="
				   SELECT id, marcador, tipo, cantidad, nombre, descripcion, info, latitud, longitud,
				   direccion, estado,size, traffic, idAssigned, fecha, modificado, nombre_usuario, tipo_usuario FROM catastrofes, usuarios
				   WHERE modificado > ?
				   AND marcador = 'resource'
				   AND estado != 'erased'
				   AND catastrofes.usuario=usuarios.id_usuarios;">
			<sql:param value="${param.fecha}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action eq 'resourcesModified'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}" sql="
				   SELECT id, marcador, tipo, cantidad, nombre, descripcion, info, latitud, longitud,
				   direccion, estado,size, traffic, idAssigned, fecha, modificado, nombre_usuario, tipo_usuario FROM catastrofes, usuarios
				   WHERE modificado > ?
				   AND marcador = 'resource'
				   AND catastrofes.usuario=usuarios.id_usuarios;">
			<sql:param value="${param.fecha}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action eq 'people'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}" sql="
				   SELECT id, marcador, tipo, cantidad, nombre, descripcion, info, latitud, longitud,
				   direccion, estado,size, traffic, idAssigned, fecha, modificado, nombre_usuario, tipo_usuario FROM catastrofes, usuarios
				   WHERE modificado > ?
				   AND marcador = 'people'
				   AND estado != 'erased'
				   AND catastrofes.usuario=usuarios.id_usuarios;">
			<sql:param value="${param.fecha}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action eq 'peopleModified'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}" sql="
				   SELECT id, marcador, tipo, cantidad, nombre, descripcion, info, latitud, longitud,
				   direccion, estado,size, traffic, idAssigned, fecha, modificado, nombre_usuario, tipo_usuario FROM catastrofes, usuarios
				   WHERE modificado > ?
				   AND marcador = 'people'
				   AND catastrofes.usuario=usuarios.id_usuarios;">
			<sql:param value="${param.fecha}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action eq 'id'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}" sql="
				   SELECT id, marcador, tipo, cantidad, nombre, descripcion, info, latitud, longitud,
				   direccion, estado, size, traffic, idAssigned, fecha, modificado, nombre_usuario, tipo_usuario FROM catastrofes, usuarios
				   WHERE id = ?
				   AND estado != 'erased'
				   AND catastrofes.usuario=usuarios.id_usuarios;">
			<sql:param value="${param.id}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action eq 'item'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}" sql="
				   SELECT id, marcador, tipo, cantidad, nombre, descripcion, info, latitud, longitud,
				   direccion, estado,size, traffic, idAssigned, fecha, modificado, nombre_usuario, tipo_usuario FROM catastrofes, usuarios
				   WHERE marcador =  ?
				   AND estado != 'erased'
				   AND catastrofes.usuario=usuarios.id_usuarios;">
			<sql:param value="${param.item}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action eq 'type'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}" sql="
				   SELECT id, marcador, tipo, cantidad, nombre, descripcion, info, latitud, longitud,
				   direccion, estado,size, traffic, idAssigned, fecha, modificado, nombre_usuario, tipo_usuario FROM catastrofes, usuarios
				   WHERE tipo =  ?
				   AND estado != 'erased'
				   AND catastrofes.usuario=usuarios.id_usuarios;">
			<sql:param value="${param.type}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action eq 'year'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}" sql="
				   SELECT id, marcador, tipo, cantidad, nombre, descripcion, info, latitud, longitud,
				   direccion, estado,size, traffic, idAssigned, fecha, modificado, nombre_usuario, tipo_usuario FROM catastrofes, usuarios
				   WHERE fecha > ?
				   AND estado != 'erased'
				   AND modificado < ?
				   AND catastrofes.usuario=usuarios.id_usuarios;">
			<sql:param value="${param.year1}"/>
			<sql:param value="${param.year2}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action eq 'eventsByYear'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}" sql="
				   SELECT id, marcador, tipo, cantidad, nombre, descripcion, info, latitud, longitud,
				   direccion, estado,size, traffic, idAssigned, fecha, modificado, nombre_usuario, tipo_usuario FROM catastrofes, usuarios
				   WHERE modificado > ?
				   AND fecha < ?
				   AND marcador = 'event'
				   AND estado != 'erased'
				   AND catastrofes.usuario=usuarios.id_usuarios;">
			<sql:param value="${param.year1}"/>
			<sql:param value="${param.year2}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action eq 'peopleByYear'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}" sql="
				   SELECT id, marcador, tipo, cantidad, nombre, descripcion, info, latitud, longitud,
				   direccion, estado,size, traffic, idAssigned, fecha, modificado, nombre_usuario, tipo_usuario FROM catastrofes, usuarios
				   WHERE modificado > ?
				   AND fecha < ?
				   AND marcador = 'people'
				   AND estado != 'erased'
				   AND catastrofes.usuario=usuarios.id_usuarios;">
			<sql:param value="${param.year1}"/>
			<sql:param value="${param.year2}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action eq 'resourcesByYear'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}" sql="
				   SELECT id, marcador, tipo, cantidad, nombre, descripcion, info, latitud, longitud,
				   direccion, estado,size, traffic, idAssigned, fecha, modificado, nombre_usuario, tipo_usuario FROM catastrofes, usuarios
				   WHERE modificado > ?
				   AND fecha < ?
				   AND marcador = 'resource'
				   AND estado != 'erased'
				   AND catastrofes.usuario=usuarios.id_usuarios;">
			<sql:param value="${param.year1}"/>
			<sql:param value="${param.year2}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action eq 'associated'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}" sql="
				   SELECT id, marcador, tipo, cantidad, nombre, descripcion, info, latitud, longitud,
				   direccion, estado,size, traffic, idAssigned, fecha, modificado, nombre_usuario, tipo_usuario FROM catastrofes, usuarios
				   WHERE tipo = ?
				   AND estado != 'erased'
				   AND idAssigned = ?
				   AND catastrofes.usuario=usuarios.id_usuarios;">
			<sql:param value="${param.type}"/>
			<sql:param value="${param.id}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action eq 'free'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}" sql="
				   SELECT id, tipo, nombre, descripcion, info, latitud, longitud,
				   direccion, estado, idAssigned  FROM resources
				   WHERE estado != 'erased';">
		</sql:query>
	</c:when>

	<c:when test="${param.action eq 'users'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}" sql="
				   SELECT id, marcador, tipo, cantidad, nombre, descripcion, info, latitud, longitud,
				   direccion, estado, size, traffic, idAssigned, fecha, modificado
				   FROM catastrofes
				   WHERE marcador = 'resource'
				   AND estado != 'erased';">
		</sql:query>
	</c:when>

	<c:when test="${param.action eq 'usersModified'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}" sql="
				   SELECT id, marcador, tipo, cantidad, nombre, descripcion, info, latitud, longitud,
				   direccion, estado, size, traffic, idAssigned, fecha, modificado
				   FROM catastrofes
				   WHERE marcador = 'resource'
				   AND modificado > ?;">
			<sql:param value="${param.fecha}"/>
		</sql:query>
	</c:when>

	<c:when test="${param.action eq 'healthy'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}" sql="
				   SELECT id, marcador, tipo, cantidad, nombre, descripcion, info, latitud, longitud,
				   direccion, estado, size, traffic, idAssigned, fecha, modificado
				   FROM catastrofes
				   WHERE marcador = 'people'
				   AND tipo = 'healthy'
				   AND estado != 'erased';">
		</sql:query>
	</c:when>

	<c:when test="${param.action eq 'unhealthy'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}" sql="
				   SELECT id, marcador, tipo, cantidad, nombre, descripcion, info, latitud, longitud,
				   direccion, estado, size, traffic, idAssigned, fecha, modificado
				   FROM catastrofes
				   WHERE marcador = 'people'
				   AND tipo != 'healthy'
				   AND estado != 'erased';">
		</sql:query>
	</c:when>

	<c:when test="${param.action eq 'slight'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}" sql="
				   SELECT id, marcador, tipo, cantidad, nombre, descripcion, info, latitud, longitud,
				   direccion, estado, size, traffic, idAssigned, fecha, modificado
				   FROM catastrofes
				   WHERE marcador = 'people'
				   AND tipo = 'slight'
				   AND estado != 'erased';">
		</sql:query>
	</c:when>

	<c:when test="${param.action eq 'person'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}" sql="
				   SELECT id, marcador, tipo, cantidad, nombre, descripcion, info, latitud, longitud,
				   direccion, estado, size, traffic, idAssigned, fecha, modificado, sintomas
				   FROM catastrofes
				   WHERE id = ?
				   AND marcador = 'people'
				   AND estado != 'erased';">
			<sql:param value="${param.id}"/>
		</sql:query>
	</c:when>

	<c:when test="${param.action eq 'proyects'}">
		<sql:query var="proyectos" dataSource="${CatastrofesServer}" sql="
				   SELECT id, proyecto
				   FROM proyectos;">
		</sql:query>
	</c:when>

	<c:when test="${param.action eq 'user'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}" sql="
				   SELECT * FROM usuarios
				   WHERE nombre_usuario = ?
				   AND password = ?;">
			<sql:param value="${param.nombre_usuario}"/>
			<sql:param value="${param.password}"/>
		</sql:query>
	</c:when>

	<c:when test="${param.action eq 'userRole'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}" sql="
				   SELECT id_usuarios, nombre_usuario, tipo_usuario
				   FROM usuarios
				   WHERE nombre_usuario = ?;">
			<sql:param value="${param.nombre_usuario}"/>
		</sql:query>
	</c:when>

	<c:when test="${param.action eq 'userProyect'}">
		<sql:query var="proyectos" dataSource="${CatastrofesServer}" sql="
				   SELECT id_usuarios, tipo_usuario, proyectos.id, proyecto,
				   relaciones.id, id_usuario, id_proyecto
				   FROM usuarios, proyectos, relaciones
				   WHERE nombre_usuario = ?
				   AND id_usuario = id_usuarios
				   AND id_proyecto = proyectos.id;">
			<sql:param value="${param.nombre_usuario}"/>
		</sql:query>
	</c:when>

	<c:when test="${param.action eq 'messages'}">
		<sql:query var="mensajes" dataSource="${CatastrofesServer}" sql="
				   SELECT id, mensaje, nivel, fecha
				   FROM mensajes
				   WHERE nivel <= ?;">
			<sql:param value="${param.nivel}"/>
		</sql:query>
	</c:when>

	<c:when test="${param.action eq 'messagesDate'}">
		<sql:query var="mensajes" dataSource="${CatastrofesServer}" sql="
				   SELECT id, mensaje, nivel, fecha
				   FROM mensajes
				   WHERE nivel <= ?
				   AND fecha > ?;">
			<sql:param value="${param.nivel}"/>
			<sql:param value="${param.fecha}"/>
		</sql:query>
	</c:when>

	<c:when test="${(param.action eq 'messagesId') and (param.index ne -1)}">
		<sql:query var="mensajes" dataSource="${CatastrofesServer}" sql="
				   SELECT id, mensaje, nivel, fecha
				   FROM mensajes
				   WHERE nivel <= ?
				   AND id > ?;">
			<sql:param value="${param.nivel}"/>
			<sql:param value="${param.index}"/>
		</sql:query>
	</c:when>

	<c:when test="${(param.action eq 'messagesId') and (param.index eq -1)}">
		<sql:query var="mensajes" dataSource="${CatastrofesServer}" sql="
				   SELECT TOP 1 id, mensaje, nivel, fecha
				   FROM mensajes
				   WHERE nivel <= ?
				   ORDER BY id DESC;">
			<sql:param value="${param.nivel}"/>
		</sql:query>
	</c:when>
</c:choose>

<c:choose>     
    <c:when test="${param.mode eq 'xml'}">     
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
				<json:property name="sintomas" value="${evento.sintomas}"/>
				<json:property name="user_name" value="${evento.nombre_usuario}"/>
				<json:property name="user_type" value="${evento.tipo_usuario}"/>
				<json:property name="real_name" value="${evento.nombre_real}"/>
				<json:property name="email" value="${evento.correo}"/>
			</json:object> ,
		</c:forEach>
		<c:forEach var="proyecto" items="${proyectos.rows}">
			<json:object name="temporal">
				<json:property name="proyect" value="${proyecto.proyecto}"/>
				<json:property name="rol" value="${proyecto.tipo_usuario}"/>
			</json:object> ,
		</c:forEach>
		<c:forEach var="mensaje" items="${mensajes.rows}">
			<json:object name="temporal">
				<json:property name="id" value="${mensaje.id}"/>
				<json:property name="mensaje" value="${mensaje.mensaje}"/>
				<json:property name="nivel" value="${mensaje.nivel}"/>
				<json:property name="fecha" value="${mensaje.fecha}"/>
			</json:object> ,
		</c:forEach>
		]
    </c:otherwise>
</c:choose>
