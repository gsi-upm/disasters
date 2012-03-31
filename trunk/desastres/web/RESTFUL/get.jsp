<%@page contentType="applicacion/json" pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@taglib prefix="json" uri="http://www.atg.com/taglibs/json"%>

<%@include file="../jspf/database.jspf"%>

<c:choose>
	<c:when test="${param.action == 'all'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT C.ID, CANTIDAD, NOMBRE, C.DESCRIPCION, INFO, LATITUD, LONGITUD,
				DIRECCION, SIZE, TRAFFIC, PLANTA, IDASSIGNED, FECHA, MODIFICADO,
				USUARIO, TIPO_MARCADOR, TIPO_CATASTROFE, TIPO_ESTADO
			FROM CATASTROFES C, TIPOS_MARCADORES M, TIPOS_CATASTROFES T, TIPOS_ESTADOS E
			WHERE MODIFICADO > ?
			AND MARCADOR = M.ID
			AND TIPO = T.ID
			AND ESTADO = E.ID
			AND TIPO_ESTADO != 'erased'
			<sql:param value="${param.fecha}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'events'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT C.ID, CANTIDAD, NOMBRE, C.DESCRIPCION, INFO, LATITUD, LONGITUD,
				DIRECCION, SIZE, TRAFFIC, PLANTA, IDASSIGNED, FECHA, MODIFICADO,
				USUARIO, TIPO_MARCADOR, TIPO_CATASTROFE, TIPO_ESTADO
			FROM CATASTROFES C, TIPOS_MARCADORES M, TIPOS_CATASTROFES T, TIPOS_ESTADOS E
			WHERE MARCADOR = M.ID
			AND TIPO_MARCADOR = 'event'
			AND TIPO = T.ID
			AND ESTADO = E.ID
			AND TIPO_ESTADO != 'erased'
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'eventsModified'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT C.ID, CANTIDAD, NOMBRE, C.DESCRIPCION, INFO, LATITUD, LONGITUD,
				DIRECCION, SIZE, TRAFFIC, PLANTA, IDASSIGNED, FECHA, MODIFICADO,
				USUARIO, TIPO_MARCADOR, TIPO_CATASTROFE, TIPO_ESTADO
			FROM CATASTROFES C, TIPOS_MARCADORES M, TIPOS_CATASTROFES T, TIPOS_ESTADOS E
			WHERE MODIFICADO > ?
			AND MARCADOR = M.ID
			AND TIPO_MARCADOR = 'event'
			AND TIPO = T.ID
			AND ESTADO = E.ID
			<sql:param value="${param.fecha}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'resources'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT U.ID, CANTIDAD, NOMBRE, C.DESCRIPCION, INFO, C.LATITUD, C.LONGITUD,
				DIRECCION, SIZE, TRAFFIC, C.PLANTA, IDASSIGNED, FECHA, MODIFICADO,
				USUARIO, TIPO_MARCADOR, TIPO_CATASTROFE, TIPO_ESTADO
			FROM CATASTROFES C, TIPOS_MARCADORES M, TIPOS_CATASTROFES T, USUARIOS U, TIPOS_ESTADOS E
			WHERE MARCADOR = M.ID
			AND TIPO_MARCADOR = 'resource'
			AND TIPO = T.ID
			AND NOMBRE = NOMBRE_USUARIO
			AND ESTADO = E.ID
			AND TIPO_ESTADO != 'erased'
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'resourcesModified'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT U.ID, CANTIDAD, NOMBRE, C.DESCRIPCION, INFO, C.LATITUD, C.LONGITUD,
				DIRECCION, SIZE, TRAFFIC, C.PLANTA, IDASSIGNED, FECHA, MODIFICADO,
				USUARIO, TIPO_MARCADOR, TIPO_CATASTROFE, TIPO_ESTADO
			FROM CATASTROFES C, TIPOS_MARCADORES M, TIPOS_CATASTROFES T, USUARIOS U, TIPOS_ESTADOS E
			WHERE MODIFICADO > ?
			AND MARCADOR = M.ID
			AND TIPO_MARCADOR = 'resource'
			AND TIPO = T.ID
			AND NOMBRE = NOMBRE_USUARIO
			AND ESTADO = E.ID
			<sql:param value="${param.fecha}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'people'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT C.ID, CANTIDAD, NOMBRE, C.DESCRIPCION, INFO, LATITUD, LONGITUD,
				DIRECCION, SIZE, TRAFFIC, PLANTA, IDASSIGNED, FECHA, MODIFICADO,
				USUARIO, TIPO_MARCADOR, TIPO_CATASTROFE, TIPO_ESTADO
			FROM CATASTROFES C, TIPOS_MARCADORES M, TIPOS_CATASTROFES T, TIPOS_ESTADOS E
			WHERE MARCADOR = M.ID
			AND TIPO_MARCADOR = 'people'
			AND TIPO = T.ID
			AND ESTADO = E.ID
			AND TIPO_ESTADO != 'erased'
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'peopleModified'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT C.ID, CANTIDAD, NOMBRE, C.DESCRIPCION, INFO, LATITUD, LONGITUD,
				DIRECCION, SIZE, TRAFFIC, PLANTA, IDASSIGNED, FECHA, MODIFICADO,
				USUARIO, TIPO_MARCADOR, TIPO_CATASTROFE, TIPO_ESTADO
			FROM CATASTROFES C, TIPOS_MARCADORES M, TIPOS_CATASTROFES T, TIPOS_ESTADOS E
			WHERE MODIFICADO > ?
			AND MARCADOR = M.ID
			AND TIPO_MARCADOR = 'people'
			AND TIPO = T.ID
			AND ESTADO = E.ID
			<sql:param value="${param.fecha}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'id'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT C.ID, CANTIDAD, NOMBRE, C.DESCRIPCION, INFO, LATITUD, LONGITUD,
				DIRECCION, SIZE, TRAFFIC, PLANTA, IDASSIGNED, FECHA, MODIFICADO,
				USUARIO, TIPO_MARCADOR, TIPO_CATASTROFE, TIPO_ESTADO
			FROM CATASTROFES C, TIPOS_MARCADORES M, TIPOS_CATASTROFES T, TIPOS_ESTADOS E
			WHERE C.ID = ?
			AND MARCADOR = M.ID
			AND TIPO = T.ID
			AND ESTADO = E.ID
			AND TIPO_ESTADO != 'erased'
			<sql:param value="${param.id}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'item'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT C.ID, CANTIDAD, NOMBRE, C.DESCRIPCION, INFO, LATITUD, LONGITUD,
				DIRECCION, SIZE, TRAFFIC, PLANTA, IDASSIGNED, FECHA, MODIFICADO,
				USUARIO, TIPO_MARCADOR, TIPO_CATASTROFE, TIPO_ESTADO
			FROM CATASTROFES C, TIPOS_MARCADORES M, TIPOS_CATASTROFES T, TIPOS_ESTADOS E
			WHERE MARCADOR = M.ID
			AND TIPO_MARCADOR = ?
			AND TIPO = T.ID
			AND ESTADO = E.ID
			AND TIPO_ESTADO != 'erased'
			<sql:param value="${param.item}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'type'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT C.ID, CANTIDAD, NOMBRE, C.DESCRIPCION, INFO, LATITUD, LONGITUD,
				DIRECCION, SIZE, TRAFFIC, PLANTA, IDASSIGNED, FECHA, MODIFICADO,
				USUARIO, TIPO_MARCADOR, TIPO_CATASTROFE, TIPO_ESTADO
			FROM CATASTROFES C, TIPOS_MARCADORES M, TIPOS_CATASTROFES T, TIPOS_ESTADOS E
			WHERE MARCADOR = M.ID
			AND TIPO = T.ID
			AND TIPO_CATASTROFE = ?
			AND ESTADO = E.ID
			AND TIPO_ESTADO != 'erased'
			<sql:param value="${param.type}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'year'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT C.ID, CANTIDAD, NOMBRE, C.DESCRIPCION, INFO, LATITUD, LONGITUD,
				DIRECCION, SIZE, TRAFFIC, PLANTA, IDASSIGNED, FECHA, MODIFICADO,
				USUARIO, TIPO_MARCADOR, TIPO_CATASTROFE, TIPO_ESTADO
			FROM CATASTROFES C, TIPOS_MARCADORES M, TIPOS_CATASTROFES T, TIPOS_ESTADOS E
			WHERE FECHA > ?
			AND MODIFICADO < ?
			AND MARCADOR = M.ID
			AND TIPO = T.ID
			AND ESTADO = E.ID
			AND TIPO_ESTADO != 'erased'
			<sql:param value="${param.year1}"/>
			<sql:param value="${param.year2}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'eventsByYear'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT C.ID, CANTIDAD, NOMBRE, C.DESCRIPCION, INFO, LATITUD, LONGITUD,
				DIRECCION, SIZE, TRAFFIC, PLANTA, IDASSIGNED, FECHA, MODIFICADO,
				USUARIO, TIPO_MARCADOR, TIPO_CATASTROFE, TIPO_ESTADO
			FROM CATASTROFES C, TIPOS_MARCADORES M, TIPOS_CATASTROFES T, TIPOS_ESTADOS E
			WHERE MODIFICADO > ?
			AND FECHA < ?
			AND MARCADOR = M.ID
			AND TIPOS_MARCADOR = 'event'
			AND TIPO = T.ID
			AND ESTADO = E.ID
			AND TIPO_ESTADO != 'erased'
			<sql:param value="${param.year1}"/>
			<sql:param value="${param.year2}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'peopleByYear'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT C.ID, CANTIDAD, NOMBRE, C.DESCRIPCION, INFO, LATITUD, LONGITUD,
				DIRECCION, SIZE, TRAFFIC, PLANTA, IDASSIGNED, FECHA, MODIFICADO,
				USUARIO, TIPO_MARCADOR, TIPO_CATASTROFE, TIPO_ESTADO
			FROM CATASTROFES C, TIPOS_MARCADORES M, TIPOS_CATASTROFES T, TIPOS_ESTADOS E
			WHERE MODIFICADO > ?
			AND FECHA < ?
			AND MARCADOR = M.ID
			AND TIPO_MARCADOR = 'people'
			AND TIPO = T.ID
			AND ESTADO = E.ID
			AND TIPO_ESTADO != 'erased'
			<sql:param value="${param.year1}"/>
			<sql:param value="${param.year2}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'resourcesByYear'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT C.ID, CANTIDAD, NOMBRE, C.DESCRIPCION, INFO, LATITUD, LONGITUD,
				DIRECCION, SIZE, TRAFFIC, PLANTA, IDASSIGNED, FECHA, MODIFICADO,
				USUARIO, TIPO_MARCADOR, TIPO_CATASTROFE, TIPO_ESTADO
			FROM CATASTROFES C, TIPOS_MARCADORES M, TIPOS_CATASTROFES T, TIPOS_ESTADOS E
			WHERE MODIFICADO > ?
			AND FECHA < ?
			AND MARCADOR = M.ID
			AND TIPO_MARCADOR = 'resource'
			AND TIPO = T.ID
			AND ESTADO = E.ID
			AND TIPO_ESTADO != 'erased'
			<sql:param value="${param.year1}"/>
			<sql:param value="${param.year2}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'associated'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT C.ID, CANTIDAD, NOMBRE, C.DESCRIPCION, INFO, LATITUD, LONGITUD,
				DIRECCION, SIZE, TRAFFIC, PLANTA, IDASSIGNED, FECHA, MODIFICADO,
				USUARIO, TIPO_MARCADOR, TIPO_CATASTROFE, TIPO_ESTADO
			FROM CATASTROFES C, TIPOS_MARCADORES M, TIPOS_CATASTROFES T, TIPOS_ESTADOS E
			WHERE MARCADOR = M.ID
			AND TIPO = T.ID
			AND TIPO_CATASTROFE = ?
			AND IDASSIGNED = ?
			AND ESTADO = E.ID
			AND TIPO_ESTADO != 'erased'
			<sql:param value="${param.type}"/>
			<sql:param value="${param.id}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'free'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT * FROM RESOURCES WHERE ESTADO != 'erased'
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'healthy'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT C.ID, CANTIDAD, NOMBRE, C.DESCRIPCION, INFO, LATITUD, LONGITUD,
				DIRECCION, SIZE, TRAFFIC, PLANTA, IDASSIGNED, FECHA, MODIFICADO,
				USUARIO, TIPO_MARCADOR, TIPO_CATASTROFE, TIPO_ESTADO
			FROM CATASTROFES C, TIPOS_MARCADORES M, TIPOS_CATASTROFES T, TIPOS_ESTADOS E
			WHERE MARCADOR = M.ID
			AND TIPO_MARCADOR = 'people'
			AND TIPO = T.ID
			AND TIPO_CATASTROFE = 'healthy'
			AND ESTADO = E.ID
			AND TIPO_ESTADO != 'erased'
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'unhealthy'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT C.ID, CANTIDAD, NOMBRE, C.DESCRIPCION, INFO, LATITUD, LONGITUD,
				DIRECCION, SIZE, TRAFFIC, PLANTA, IDASSIGNED, FECHA, MODIFICADO,
				USUARIO, TIPO_MARCADOR, TIPO_CATASTROFE, TIPO_ESTADO
			FROM CATASTROFES C, TIPOS_MARCADORES M, TIPOS_CATASTROFES T, TIPOS_ESTADOS E
			WHERE MARCADOR = M.ID
			AND TIPO_MARCADOR = 'people'
			AND TIPO = T.ID
			AND TIPO_CATASTROFE != 'healthy'
			AND ESTADO = E.ID
			AND TIPO_ESTADO != 'erased'
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'slight'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT C.ID, CANTIDAD, NOMBRE, C.DESCRIPCION, INFO, LATITUD, LONGITUD,
				DIRECCION, SIZE, TRAFFIC, PLANTA, IDASSIGNED, FECHA, MODIFICADO,
				USUARIO, TIPO_MARCADOR, TIPO_CATASTROFE, TIPO_ESTADO
			FROM CATASTROFES C, TIPOS_MARCADORES M, TIPOS_CATASTROFES T, TIPOS_ESTADOS E
			WHERE MARCADOR = M.ID
			AND TIPO_MARCADOR = 'people'
			AND TIPO = T.ID
			AND TIPO_CATASTROFE = 'slight'
			AND ESTADO = E.ID
			AND TIPO_ESTADO != 'erased'
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'person'}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT C.ID, CANTIDAD, NOMBRE, C.DESCRIPCION, INFO, LATITUD, LONGITUD,
				DIRECCION, SIZE, TRAFFIC, PLANTA, IDASSIGNED, FECHA, MODIFICADO,
				USUARIO, TIPO_MARCADOR, TIPO_CATASTROFE, TIPO_ESTADO
			FROM CATASTROFES C, TIPOS_MARCADORES M, TIPOS_CATASTROFES T, TIPOS_ESTADOS E
			WHERE c.id = ?
			AND MARCADOR = M.ID
			AND TIPO_MARCADOR = 'people'
			AND TIPO = T.ID
			AND ESTADO = E.ID
			AND TIPO_ESTADO != 'erased'
			<sql:param value="${param.id}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'associations'}">
		<sql:query var="asociaciones" dataSource="${CatastrofesServer}">
			SELECT A.ID, ID_HERIDO, ID_EMERGENCIA, TIPO_ESTADO
			FROM ASOCIACIONES_HERIDOS_EMERGENCIAS A, TIPOS_ESTADOS E
			WHERE ESTADO = E.ID
			AND TIPO_ESTADO != 'erased'
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'associationsModified'}">
		<sql:query var="asociaciones" dataSource="${CatastrofesServer}">
			SELECT A.ID, ID_HERIDO, ID_EMERGENCIA, TIPO_ESTADO
			FROM ASOCIACIONES_HERIDOS_EMERGENCIAS A, TIPOS_ESTADOS E
			WHERE ESTADO = E.ID
			AND FECHA > ?
			<sql:param value="${param.fecha}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'activities'}">
		<sql:query var="actividades" dataSource="${CatastrofesServer}">
			SELECT A.ID, ID_USUARIO, ID_EMERGENCIA, TIPO, TIPO_ESTADO
			FROM ACTIVIDADES A, TIPOS_ACTIVIDADES T, TIPOS_ESTADOS E
			WHERE ID_TIPO_ACTIVIDAD = T.ID
			AND ESTADO = E.ID
			AND TIPO_ESTADO != 'erased'
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'activitiesModified'}">
		<sql:query var="actividades" dataSource="${CatastrofesServer}">
			SELECT A.ID, ID_USUARIO, ID_EMERGENCIA, TIPO, TIPO_ESTADO
			FROM ACTIVIDADES A, TIPOS_ACTIVIDADES T, TIPOS_ESTADOS E
			WHERE ID_TIPO_ACTIVIDAD = T.ID 
			AND ESTADO = E.ID
			AND FECHA > ?
			<sql:param value="${param.fecha}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'messages'}">
		<sql:query var="mensajes" dataSource="${CatastrofesServer}">
			SELECT ID, CREADOR, TIPO_RECEPTOR, RECEPTOR, MENSAJE, FECHA
			FROM MENSAJES
			WHERE CREADOR = ?
			AND TIPO_RECEPTOR = 0
			AND RECEPTOR = 0
			AND FECHA > ?
			<sql:param value="${param.creador}"/>
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
					<idAssigned>${evento.idassigned}</idAssigned>
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
					<json:property name="idAssigned" value="${evento.idassigned}"/>
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
			<c:forEach var="mensaje" items="${mensajes.rows}">
				<json:object>
					<json:property name="id" value="${mensaje.id}"/>
					<json:property name="creador" value="${mensaje.creador}"/>
					<json:property name="tipo_receptor" value="${mensaje.tipo_receptor}"/>
					<json:property name="receptor" value="${mensaje.receptor}"/>
					<json:property name="mensaje" value="${mensaje.mensaje}"/>
					<json:property name="fecha" value="${mensaje.fecha}"/>
				</json:object>
			</c:forEach>
		</json:array>
	</c:otherwise>
</c:choose>