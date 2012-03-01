<%@page contentType="application/json" pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@taglib prefix="json" uri="http://www.atg.com/taglibs/json"%>

<%@include file="../jspf/database.jspf"%>

<c:choose>
	<c:when test="${param.action == 'firstTime' && param.nivel gt 0}">
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
	<c:when test="${param.action == 'firstTime' && param.nivel == 0}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT c.id, c.cantidad, c.nombre, c.descripcion, c.info, c.latitud, c.longitud,
				c.direccion, c.size, c.traffic, c.planta, c.idAssigned, c.fecha, c.modificado,
				c.usuario, m.tipo_marcador, t.tipo_catastrofe, e.tipo_estado
			FROM catastrofes c, tipos_marcadores m, tipos_catastrofes t, tipos_estados e
			WHERE c.modificado > ?
			AND c.marcador = m.id
			AND m.tipo_marcador = 'event'
			AND c.tipo = t.id
			AND c.planta = -1
			AND c.estado = e.id
			AND e.tipo_estado != 'erased'
			<sql:param value="${param.fecha}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'notFirst' && param.nivel gt 0}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT c.id, c.cantidad, c.nombre, c.descripcion, c.info, c.latitud, c.longitud,
				c.direccion, c.size, c.traffic, c.planta, c.idAssigned, c.fecha, c.modificado,
				c.usuario, m.tipo_marcador, t.tipo_catastrofe, e.tipo_estado
			FROM catastrofes c, tipos_marcadores m, tipos_catastrofes t, tipos_estados e
			WHERE c.modificado > ?
			AND c.marcador = m.id
			AND c.tipo = t.id
			AND c.estado = e.id
			<sql:param value="${param.fecha}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'notFirst' && param.nivel == 0}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT c.id, c.cantidad, c.nombre, c.descripcion, c.info, c.latitud, c.longitud,
				c.direccion, c.size, c.traffic, c.planta, c.idAssigned, c.fecha, c.modificado,
				c.usuario, m.tipo_marcador, t.tipo_catastrofe, e.tipo_estado
			FROM catastrofes c, tipos_marcadores m, tipos_catastrofes t, tipos_estados e
			WHERE c.modificado > ?
			AND c.marcador = m.id
			AND m.tipo_marcador = 'event'
			AND c.tipo = t.id
			AND c.planta = -1
			AND c.estado = e.id
			<sql:param value="${param.fecha}"/>
		</sql:query>
	</c:when>
	<c:otherwise>
		<p class="textoError">Invalid action:${param.action}</p>
	</c:otherwise>
</c:choose>

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
</json:array>