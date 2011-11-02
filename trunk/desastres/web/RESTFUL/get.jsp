<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page language = "java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %> 
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>

<%@ include file="../jspf/database.jspf" %>

<c:choose>
	<c:when test="${param.action == 'all'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT c.id, c.marcador, c.tipo, c.cantidad, c.nombre, c.descripcion, c.info, c.latitud,
				c.longitud, c.direccion, c.size, c.traffic, c.idAssigned, c.fecha, c.modificado,
				u.nombre_usuario, u.tipo_usuario, t.tipo_estado
			FROM catastrofes c, usuarios u, tipos_estados t
			WHERE c.modificado > ?
			AND c.estado = t.id
			AND t.tipo_estado != 'erased'
			AND c.usuario = u.id
			<sql:param value="${param.fecha}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'events'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT c.id, c.marcador, c.tipo, c.cantidad, c.nombre, c.descripcion, c.info, c.latitud,
				c.longitud, c.direccion, c.size, c.traffic, c.idAssigned, c.fecha, c.modificado,
				u.nombre_usuario, u.tipo_usuario, t.tipo_estado
			FROM catastrofes c, usuarios u, tipos_estados t
			WHERE c.modificado > ?
			AND c.marcador = 'event'
			AND c.estado = t.id
			AND t.tipo_estado != 'erased'
			AND c.usuario = u.id
			<sql:param value="${param.fecha}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'eventsModified'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT c.id, c.marcador, c.tipo, c.cantidad, c.nombre, c.descripcion, c.info, c.latitud,
				c.longitud, c.direccion, c.size, c.traffic, c.idAssigned, c.fecha, c.modificado,
				u.nombre_usuario, u.tipo_usuario, t.tipo_estado
			FROM catastrofes c, usuarios u, tipos_estados t
			WHERE c.modificado > ?
			AND c.marcador = 'event'
			AND c.estado = t.id
			AND c.usuario = u.id
			<sql:param value="${param.fecha}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'resources'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT c.id, c.marcador, c.tipo, c.cantidad, c.nombre, c.descripcion, c.info, c.latitud,
				c.longitud, c.direccion, c.size, c.traffic, c.idAssigned, c.fecha, c.modificado,
				u.nombre_usuario, u.tipo_usuario, t.tipo_estado
			FROM catastrofes c, usuarios u, tipos_estados t
			WHERE c.modificado > ?
			AND c.marcador = 'resource'
			AND c.estado = t.id
			AND t.tipo_estado != 'erased'
			AND c.usuario = u.id
			<sql:param value="${param.fecha}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'resourcesModified'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT c.id, c.marcador, c.tipo, c.cantidad, c.nombre, c.descripcion, c.info, c.latitud,
				c.longitud, c.direccion, c.size, c.traffic, c.idAssigned, c.fecha, c.modificado,
				u.nombre_usuario, u.tipo_usuario, t.tipo_estado
			FROM catastrofes c, usuarios u, tipos_estados t
			WHERE c.modificado > ?
			AND c.marcador = 'resource'
			AND c.estado = t.id
			AND c.usuario = u.id
			<sql:param value="${param.fecha}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'people'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT c.id, c.marcador, c.tipo, c.cantidad, c.nombre, c.descripcion, c.info, c.latitud,
				c.longitud, c.direccion, c.size, c.traffic, c.idAssigned, c.fecha, c.modificado,
				u.nombre_usuario, u.tipo_usuario, t.tipo_estado
			FROM catastrofes c, usuarios u, tipos_estados t
			WHERE c.modificado > ?
			AND c.marcador = 'people'
			AND c.estado = t.id
			AND t.tipo_estado != 'erased'
			AND c.usuario = u.id
			<sql:param value="${param.fecha}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'peopleModified'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT c.id, c.marcador, c.tipo, c.cantidad, c.nombre, c.descripcion, c.info, c.latitud,
				c.longitud, c.direccion, c.size, c.traffic, c.idAssigned, c.fecha, c.modificado,
				u.nombre_usuario, u.tipo_usuario, t.tipo_estado
			FROM catastrofes c, usuarios u, tipos_estados t
			WHERE c.modificado > ?
			AND c.marcador = 'people'
			AND c.estado = t.id
			AND c.usuario = u.id
			<sql:param value="${param.fecha}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'id'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT c.id, c.marcador, c.tipo, c.cantidad, c.nombre, c.descripcion, c.info, c.latitud,
				c.longitud, c.direccion, c.size, c.traffic, c.idAssigned, c.fecha, c.modificado,
				u.nombre_usuario, u.tipo_usuario, t.tipo_estado
			FROM catastrofes c, usuarios u, tipos_estados t
			WHERE c.id = ?
			AND c.estado = t.id
			AND t.tipo_estado != 'erased'
			AND c.usuario = u.id
			<sql:param value="${param.id}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'item'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT c.id, c.marcador, c.tipo, c.cantidad, c.nombre, c.descripcion, c.info, c.latitud,
				c.longitud, c.direccion, c.size, c.traffic, c.idAssigned, c.fecha, c.modificado,
				u.nombre_usuario, u.tipo_usuario, t.tipo_estado
			FROM catastrofes c, usuarios u, tipos_estados t
			WHERE c.marcador = ?
			AND c.estado = t.id
			AND t.tipo_estado != 'erased'
			AND c.usuario = u.id
			<sql:param value="${param.item}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'type'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT c.id, c.marcador, c.tipo, c.cantidad, c.nombre, c.descripcion, c.info, c.latitud,
				c.longitud, c.direccion, c.size, c.traffic, c.idAssigned, c.fecha, c.modificado,
				u.nombre_usuario, u.tipo_usuario, t.tipo_estado
			FROM catastrofes c, usuarios u, tipos_estados t
			WHERE c.tipo = ?
			AND c.estado = t.id
			AND t.tipo_estado != 'erased'
			AND c.usuario = u.id
			<sql:param value="${param.type}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'year'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT c.id, c.marcador, c.tipo, c.cantidad, c.nombre, c.descripcion, c.info, c.latitud,
				c.longitud, c.direccion, c.size, c.traffic, c.idAssigned, c.fecha, c.modificado,
				u.nombre_usuario, u.tipo_usuario, t.tipo_estado
			FROM catastrofes c, usuarios u, tipos_estados t
			WHERE c.fecha > ?
			AND c.modificado < ?
			AND c.estado = t.id
			AND t.tipo_estado != 'erased'
			AND c.usuario = u.id
			<sql:param value="${param.year1}"/>
			<sql:param value="${param.year2}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'eventsByYear'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT c.id, c.marcador, c.tipo, c.cantidad, c.nombre, c.descripcion, c.info, c.latitud,
				c.longitud, c.direccion, c.size, c.traffic, c.idAssigned, c.fecha, c.modificado,
				u.nombre_usuario, u.tipo_usuario, t.tipo_estado
			FROM catastrofes c, usuarios u, tipos_estados t
			WHERE c.modificado > ?
			AND c.fecha < ?
			AND c.marcador = 'event'
			AND c.estado = t.id
			AND t.tipo_estado != 'erased'
			AND c.usuario = u.id
			<sql:param value="${param.year1}"/>
			<sql:param value="${param.year2}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'peopleByYear'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT c.id, c.marcador, c.tipo, c.cantidad, c.nombre, c.descripcion, c.info, c.latitud,
				c.longitud, c.direccion, c.size, c.traffic, c.idAssigned, c.fecha, c.modificado,
				u.nombre_usuario, u.tipo_usuario, t.tipo_estado
			FROM catastrofes c, usuarios u, tipos_estados t
			WHERE c.modificado > ?
			AND c.fecha < ?
			AND c.marcador = 'people'
			AND c.estado = t.id
			AND t.tipo_estado != 'erased'
			AND c.usuario = u.id
			<sql:param value="${param.year1}"/>
			<sql:param value="${param.year2}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'resourcesByYear'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT c.id, c.marcador, c.tipo, c.cantidad, c.nombre, c.descripcion, c.info, c.latitud,
				c.longitud, c.direccion, c.size, c.traffic, c.idAssigned, c.fecha, c.modificado,
				u.nombre_usuario, u.tipo_usuario, t.tipo_estado
			FROM catastrofes c, usuarios u, tipos_estados t
			WHERE modificado > ?
			AND c.fecha < ?
			AND c.marcador = 'resource'
			AND c.estado = t.id
			AND t.tipo_estado != 'erased'
			AND c.usuario = u.id
			<sql:param value="${param.year1}"/>
			<sql:param value="${param.year2}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'associated'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT c.id, c.marcador, c.tipo, c.cantidad, c.nombre, c.descripcion, c.info, c.latitud,
				c.longitud, c.direccion, c.size, c.traffic, c.idAssigned, c.fecha, c.modificado,
				u.nombre_usuario, u.tipo_usuario, t.tipo_estado
			FROM catastrofes c, usuarios u, tipos_estados t
			WHERE c.tipo = ?
			AND c.idAssigned = ?
			AND c.estado = t.id
			AND t.tipo_estado != 'erased'
			AND c.usuario = u.id
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
			SELECT c.id, c.marcador, c.tipo, c.cantidad, c.nombre, c.descripcion, c.info, c.latitud,
				c.longitud, c.direccion, c.size, c.traffic, c.idAssigned, c.fecha, c.modificado, t.tipo_estado
			FROM catastrofes c, tipos_estados t
			WHERE c.marcador = 'resource'
			AND c.estado = t.id
			AND t.tipo_estado != 'erased'
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'usersModified'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT c.id, c.marcador, c.tipo, c.cantidad, c.nombre, c.descripcion, c.info, c.latitud,
				c.longitud, c.direccion, c.size, c.traffic, c.idAssigned, c.fecha, c.modificado, t.tipo_estado
			FROM catastrofes c, tipos_estados t
			WHERE c.marcador = 'resource'
			AND c.modificado > ?
			AND c.estado = t.id
			<sql:param value="${param.fecha}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'healthy'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT c.id, c.marcador, c.tipo, c.cantidad, c.nombre, c.descripcion, c.info, c.latitud,
				c.longitud, c.direccion, c.size, c.traffic, c.idAssigned, c.fecha, c.modificado, t.tipo_estado
			FROM catastrofes c, tipos_estados t
			WHERE c.marcador = 'people'
			AND c.tipo = 'healthy'
			AND c.estado = t.id
			AND t.tipo_estado != 'erased'
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'unhealthy'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT c.id, c.marcador, c.tipo, c.cantidad, c.nombre, c.descripcion, c.info, c.latitud,
				c.longitud, c.direccion, c.size, c.traffic, c.idAssigned, c.fecha, c.modificado, t.tipo_estado
			FROM catastrofes c, tipos_estados t
			WHERE c.marcador = 'people'
			AND c.tipo != 'healthy'
			AND c.estado = t.id
			AND t.tipo_estado != 'erased'
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'slight'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT c.id, c.marcador, c.tipo, c.cantidad, c.nombre, c.descripcion, c.info, c.latitud,
				c.longitud, c.direccion, c.size, c.traffic, c.idAssigned, c.fecha, c.modificado, t.tipo_estado
			FROM catastrofes c, tipos_estados t
			WHERE c.marcador = 'people'
			AND c.tipo = 'slight'
			AND c.estado = t.id
			AND t.tipo_estado != 'erased'
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'person'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT c.id, c.marcador, c.tipo, c.cantidad, c.nombre, c.descripcion, c.info, c.latitud,
				c.longitud, c.direccion, c.size, c.traffic, c.idAssigned, c.fecha, c.modificado, t.tipo_estado
			FROM catastrofes c, tipos_estados t
			WHERE c.id = ?
			AND c.marcador = 'people'
			AND c.estado = t.id
			AND t.tipo_estado != 'erased'
			<sql:param value="${param.id}"/>
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
				<json:property name="state" value="${evento.tipo_estado}"/>
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
		]
    </c:otherwise>
</c:choose>