<%@page contentType="applicacion/json" pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@taglib prefix="json" uri="http://www.atg.com/taglibs/json"%>

<%@include file="../jspf/database.jspf"%>

<c:choose>
	<c:when test="${param.action == 'all'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT c.id, c.cantidad, c.nombre, c.descripcion, c.info, c.latitud, c.longitud,
				c.direccion, c.size, c.traffic, c.planta, c.idAssigned, c.fecha, c.modificado,
				c.usuario, m.tipo_marcador, t.tipo_catastrofe, e.tipo_estado
			FROM catastrofes c, tipos_marcadores m, tipos_catastrofes t, tipos_estados e
			WHERE c.modificado > ?
			AND c.marcador = m.id
			AND c.tipo = t.id
			AND c.estado = e.id
			AND e.tipo_estado != 'erased'
			<sql:param value="${param.fecha}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'events'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT c.id, c.cantidad, c.nombre, c.descripcion, c.info, c.latitud, c.longitud,
				c.direccion, c.size, c.traffic, c.planta, c.idAssigned, c.fecha, c.modificado,
				c.usuario, m.tipo_marcador, t.tipo_catastrofe, e.tipo_estado
			FROM catastrofes c, tipos_marcadores m, tipos_catastrofes t, tipos_estados e
			WHERE c.modificado > ?
			AND c.marcador = m.id
			AND m.tipo_marcador = 'event'
			AND c.tipo = t.id
			AND c.estado = e.id
			AND e.tipo_estado != 'erased'
			<sql:param value="${param.fecha}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'eventsModified'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT c.id, c.cantidad, c.nombre, c.descripcion, c.info, c.latitud, c.longitud,
				c.direccion, c.size, c.traffic, c.planta, c.idAssigned, c.fecha, c.modificado,
				c.usuario, m.tipo_marcador, t.tipo_catastrofe, e.tipo_estado
			FROM catastrofes c, tipos_marcadores m, tipos_catastrofes t, tipos_estados e
			WHERE c.modificado > ?
			AND c.marcador = m.id
			AND m.tipo_marcador = 'event'
			AND c.tipo = t.id
			AND c.estado = e.id
			<sql:param value="${param.fecha}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'resources'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT c.id, c.cantidad, c.nombre, c.descripcion, c.info, c.latitud, c.longitud,
				c.direccion, c.size, c.traffic, c.planta, c.idAssigned, c.fecha, c.modificado,
				c.usuario, m.tipo_marcador, t.tipo_catastrofe, e.tipo_estado
			FROM catastrofes c, tipos_marcadores m, tipos_catastrofes t, tipos_estados e
			WHERE c.modificado > ?
			AND c.marcador = m.id
			AND m.tipo_marcador = 'resource'
			AND c.tipo = t.id
			AND c.estado = e.id
			AND e.tipo_estado != 'erased'
			<sql:param value="${param.fecha}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'resourcesModified'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT c.id, c.cantidad, c.nombre, c.descripcion, c.info, c.latitud, c.longitud,
				c.direccion, c.size, c.traffic, c.planta, c.idAssigned, c.fecha, c.modificado,
				c.usuario, m.tipo_marcador, t.tipo_catastrofe, e.tipo_estado
			FROM catastrofes c, tipos_marcadores m, tipos_catastrofes t, tipos_estados e
			WHERE c.modificado > ?
			AND c.marcador = m.id
			AND m.tipo_marcador = 'resource'
			AND c.tipo = t.id
			AND c.estado = e.id
			<sql:param value="${param.fecha}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'people'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT c.id, c.cantidad, c.nombre, c.descripcion, c.info, c.latitud, c.longitud,
				c.direccion, c.size, c.traffic, c.planta, c.idAssigned, c.fecha, c.modificado,
				c.usuario, m.tipo_marcador, t.tipo_catastrofe, e.tipo_estado
			FROM catastrofes c, tipos_marcadores m, tipos_catastrofes t, tipos_estados e
			WHERE c.modificado > ?
			AND c.marcador = m.id
			AND m.tipo_marcador = 'people'
			AND c.tipo = t.id
			AND c.estado = e.id
			AND e.tipo_estado != 'erased'
			<sql:param value="${param.fecha}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'peopleModified'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT c.id, c.cantidad, c.nombre, c.descripcion, c.info, c.latitud, c.longitud,
				c.direccion, c.size, c.traffic, c.planta, c.idAssigned, c.fecha, c.modificado,
				c.usuario, m.tipo_marcador, t.tipo_catastrofe, e.tipo_estado
			FROM catastrofes c, tipos_marcadores m, tipos_catastrofes t, tipos_estados e
			WHERE c.modificado > ?
			AND c.marcador = m.id
			AND m.tipo_marcador = 'people'
			AND c.tipo = t.id
			AND c.estado = e.id
			<sql:param value="${param.fecha}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'id'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT c.id, c.cantidad, c.nombre, c.descripcion, c.info, c.latitud, c.longitud,
				c.direccion, c.size, c.traffic, c.planta, c.idAssigned, c.fecha, c.modificado,
				c.usuario, m.tipo_marcador, t.tipo_catastrofe, e.tipo_estado
			FROM catastrofes c, tipos_marcadores m, tipos_catastrofes t, tipos_estados e
			WHERE c.id = ?
			AND c.marcador = m.id
			AND c.tipo = t.id
			AND c.estado = e.id
			AND e.tipo_estado != 'erased'
			<sql:param value="${param.id}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'item'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT c.id, c.cantidad, c.nombre, c.descripcion, c.info, c.latitud, c.longitud,
				c.direccion, c.size, c.traffic, c.planta, c.idAssigned, c.fecha, c.modificado,
				c.usuario, m.tipo_marcador, t.tipo_catastrofe, e.tipo_estado
			FROM catastrofes c, tipos_marcadores m, tipos_catastrofes t, tipos_estados e
			WHERE c.marcador = m.id
			AND m.tipo_marcador = ?
			AND c.tipo = t.id
			AND c.estado = e.id
			AND e.tipo_estado != 'erased'
			<sql:param value="${param.item}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'type'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT c.id, c.cantidad, c.nombre, c.descripcion, c.info, c.latitud, c.longitud,
				c.direccion, c.size, c.traffic, c.planta, c.idAssigned, c.fecha, c.modificado,
				c.usuario, m.tipo_marcador, t.tipo_catastrofe, e.tipo_estado
			FROM catastrofes c, tipos_marcadores m, tipos_catastrofes t, tipos_estados e
			WHERE c.marcador = m.id
			AND c.tipo = t.id
			AND t.tipo_catastrofe = ?
			AND c.estado = e.id
			AND e.tipo_estado != 'erased'
			<sql:param value="${param.type}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'year'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT c.id, c.cantidad, c.nombre, c.descripcion, c.info, c.latitud, c.longitud,
				c.direccion, c.size, c.traffic, c.planta, c.idAssigned, c.fecha, c.modificado,
				c.usuario, m.tipo_marcador, t.tipo_catastrofe, e.tipo_estado
			FROM catastrofes c, tipos_marcadores m, tipos_catastrofes t, tipos_estados e
			WHERE c.fecha > ?
			AND c.modificado < ?
			AND c.marcador = m.id
			AND c.tipo = t.id
			AND c.estado = e.id
			AND e.tipo_estado != 'erased'
			<sql:param value="${param.year1}"/>
			<sql:param value="${param.year2}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'eventsByYear'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT c.id, c.cantidad, c.nombre, c.descripcion, c.info, c.latitud, c.longitud,
				c.direccion, c.size, c.traffic, c.planta, c.idAssigned, c.fecha, c.modificado,
				c.usuario, m.tipo_marcador, t.tipo_catastrofe, e.tipo_estado
			FROM catastrofes c, tipos_marcadores m, tipos_catastrofes t, tipos_estados e
			WHERE c.modificado > ?
			AND c.fecha < ?
			AND c.marcador = m.id
			AND m.tipo_marcador = 'event'
			AND c.tipo = t.id
			AND c.estado = e.id
			AND e.tipo_estado != 'erased'
			<sql:param value="${param.year1}"/>
			<sql:param value="${param.year2}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'peopleByYear'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT c.id, c.cantidad, c.nombre, c.descripcion, c.info, c.latitud, c.longitud,
				c.direccion, c.size, c.traffic, c.planta, c.idAssigned, c.fecha, c.modificado,
				c.usuario, m.tipo_marcador, t.tipo_catastrofe, e.tipo_estado
			FROM catastrofes c, tipos_marcadores m, tipos_catastrofes t, tipos_estados e
			WHERE c.modificado > ?
			AND c.fecha < ?
			AND c.marcador = m.id
			AND m.tipo_marcador = 'people'
			AND c.tipo = t.id
			AND c.estado = e.id
			AND e.tipo_estado != 'erased'
			<sql:param value="${param.year1}"/>
			<sql:param value="${param.year2}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'resourcesByYear'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT c.id, c.cantidad, c.nombre, c.descripcion, c.info, c.latitud, c.longitud,
				c.direccion, c.size, c.traffic, c.planta, c.idAssigned, c.fecha, c.modificado,
				c.usuario, m.tipo_marcador, t.tipo_catastrofe, e.tipo_estado
			FROM catastrofes c, tipos_marcadores m, tipos_catastrofes t, tipos_estados e
			WHERE modificado > ?
			AND c.fecha < ?
			AND c.marcador = m.id
			AND m.tipo_marcador = 'resource'
			AND c.tipo = t.id
			AND c.estado = e.id
			AND e.tipo_estado != 'erased'
			<sql:param value="${param.year1}"/>
			<sql:param value="${param.year2}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'associated'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT c.id, c.cantidad, c.nombre, c.descripcion, c.info, c.latitud, c.longitud,
				c.direccion, c.size, c.traffic, c.planta, c.idAssigned, c.fecha, c.modificado,
				c.usuario, m.tipo_marcador, t.tipo_catastrofe, e.tipo_estado
			FROM catastrofes c, tipos_marcadores m, tipos_catastrofes t, tipos_estados e
			WHERE c.marcador = m.id
			AND c.tipo = t.id
			AND t.tipo_catastrofe = ?
			AND c.idAssigned = ?
			AND c.estado = e.id
			AND e.tipo_estado != 'erased'
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
	<c:when test="${param.action == 'healthy'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT c.id, c.cantidad, c.nombre, c.descripcion, c.info, c.latitud, c.longitud,
				c.direccion, c.size, c.traffic, c.planta, c.idAssigned, c.fecha, c.modificado,
				c.usuario, m.tipo_marcador, t.tipo_catastrofe, e.tipo_estado
			FROM catastrofes c, tipos_marcadores m, tipos_catastrofes t, tipos_estados e
			WHERE c.marcador = m.id
			AND m.tipo_marcador = 'people'
			AND c.tipo = t.id
			AND t.tipo_catastrofe = 'healthy'
			AND c.estado = e.id
			AND e.tipo_estado != 'erased'
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'unhealthy'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT c.id, c.cantidad, c.nombre, c.descripcion, c.info, c.latitud, c.longitud,
				c.direccion, c.size, c.traffic, c.planta, c.idAssigned, c.fecha, c.modificado,
				c.usuario, m.tipo_marcador, t.tipo_catastrofe, e.tipo_estado
			FROM catastrofes c, tipos_marcadores m, tipos_catastrofes t, tipos_estados e
			WHERE c.marcador = m.id
			AND m.tipo_marcador = 'people'
			AND c.tipo = t.id
			AND t.tipo_catastrofe != 'healthy'
			AND c.estado = e.id
			AND e.tipo_estado != 'erased'
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'slight'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT c.id, c.cantidad, c.nombre, c.descripcion, c.info, c.latitud, c.longitud,
				c.direccion, c.size, c.traffic, c.planta, c.idAssigned, c.fecha, c.modificado,
				c.usuario, m.tipo_marcador, t.tipo_catastrofe, e.tipo_estado
			FROM catastrofes c, tipos_marcadores m, tipos_catastrofes t, tipos_estados e
			WHERE c.marcador = m.id
			AND m.tipo_marcador = 'people'
			AND c.tipo = t.id
			AND t.tipo_catastrofe = 'slight'
			AND c.estado = e.id
			AND e.tipo_estado != 'erased'
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'person'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT c.id, c.cantidad, c.nombre, c.descripcion, c.info, c.latitud, c.longitud,
				c.direccion, c.size, c.traffic, c.planta, c.idAssigned, c.fecha, c.modificado,
				c.usuario, m.tipo_marcador, t.tipo_catastrofe, e.tipo_estado
			FROM catastrofes c, tipos_marcadores m, tipos_catastrofes t, tipos_estados e
			WHERE c.id = ?
			AND c.marcador = m.id
			AND m.tipo_marcador = 'people'
			AND c.tipo = t.id
			AND c.estado = e.id
			AND e.tipo_estado != 'erased'
			<sql:param value="${param.id}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'associations'}">
		<sql:query var="asociaciones" dataSource="${CatastrofesServer}">
			SELECT a.id, a.id_herido, a.id_emergencia, e.tipo_estado
			FROM asociaciones_heridos_emergencias a, tipos_estados e
			WHERE a.estado = e.id
			AND e.tipo_estado != 'erased'
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'associationsModified'}">
		<sql:query var="asociaciones" dataSource="${CatastrofesServer}">
			SELECT a.id, a.id_herido, a.id_emergencia, e.tipo_estado
			FROM asociaciones_heridos_emergencias a, tipos_estados e
			WHERE a.estado = e.id
			AND a.fecha > ?
			<sql:param value="${param.fecha}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'activities'}">
		<sql:query var="actividades" dataSource="${CatastrofesServer}">
			SELECT a.id, a.id_usuario, a.id_emergencia, t.tipo, e.tipo_estado
			FROM actividades a, tipos_actividades t, tipos_estados e
			WHERE a.id_tipo_actividad = t.id 
			AND a.estado = e.id
			AND e.tipo_estado != 'erased'
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'activitiesModified'}">
		<sql:query var="actividades" dataSource="${CatastrofesServer}">
			SELECT a.id, a.id_usuario, a.id_emergencia, t.tipo, e.tipo_estado
			FROM actividades a, tipos_actividades t, tipos_estados e
			WHERE a.id_tipo_actividad = t.id 
			AND a.estado = e.id
			AND a.fecha > ?
			<sql:param value="${param.fecha}"/>
		</sql:query>
	</c:when>
</c:choose>
<c:choose> 
	<c:when test="${param.mode == 'xml'}">
		<disasters>
			<c:forEach var="evento" items="${eventos.rows}">
				<disaster>
					<id>${evento.id}</id>
					<item>${evento.tipo_marcador}</item>
					<type>${evento.tipo_catastrofe}</type>
					<quantity>${evento.cantidad}</quantity>
					<name>${evento.nombre}</name>
					<description>${evento.descripcion}</description>
					<info>${evento.info}</info>
					<address>${evento.direccion}</address>
					<latitud>${evento.latitud}</latitud>
					<longitud>${evento.longitud}</longitud>
					<size>${evento.size}</size>
					<traffic>${evento.traffic}</traffic>
					<floor>${evento.planta}</floor>
					<state>${evento.tipo_estado}</state>
					<idAssigned>${evento.idAssigned}</idAssigned>
					<date>${evento.fecha}</date>
					<modified>${evento.modificado}</modified>
					<user>${evento.usuario}</user>
				</disaster>
			</c:forEach>
		</disasters>
	</c:when>
	<c:otherwise>
		<json:array>
			<c:forEach var="evento" items="${eventos.rows}">
				<json:object>
					<json:property name="id" value="${evento.id}"/>
					<json:property name="item" value="${evento.tipo_marcador}"/>
					<json:property name="type" value="${evento.tipo_catastrofe}"/>
					<json:property name="quantity" value="${evento.cantidad}"/>
					<json:property name="name" value="${evento.nombre}"/>
					<json:property name="description" value="${evento.descripcion}"/>
					<json:property name="info" value="${evento.info}"/>
					<json:property name="latitud" value="${evento.latitud}"/>
					<json:property name="longitud" value="${evento.longitud}"/>
					<json:property name="address" value="${evento.direccion}"/>
					<json:property name="size" value="${evento.size}"/>
					<json:property name="traffic" value="${evento.traffic}"/>
					<json:property name="floor" value="${evento.planta}"/>
					<json:property name="state" value="${evento.tipo_estado}"/>
					<json:property name="idAssigned" value="${evento.idAssigned}"/>
					<json:property name="date" value="${evento.fecha}"/>
					<json:property name="modified" value="${evento.modificado}"/>
					<json:property name="user" value="${evento.usuario}"/>
				</json:object>
			</c:forEach>
			<c:forEach var="asociacion" items="${asociaciones.rows}">
				<json:object>
					<json:property name="id" value="${asociacion.id}"/>
					<json:property name="idInjured" value="${asociacion.id_herido}"/>
					<json:property name="idDisaster" value="${asociacion.id_emergencia}"/>
					<json:property name="state" value="${asociacion.tipo_estado}"/>
				</json:object>
			</c:forEach>
			<c:forEach var="actividad" items="${actividades.rows}">
				<json:object>
					<json:property name="id" value="${actividad.id}"/>
					<json:property name="idUser" value="${actividad.id_usuario}"/>
					<json:property name="idDisaster" value="${actividad.id_emergencia}"/>
					<json:property name="type" value="${actividad.tipo}"/>
					<json:property name="state" value="${actividad.tipo_estado}"/>
				</json:object>
			</c:forEach>
		</json:array>
	</c:otherwise>
</c:choose>