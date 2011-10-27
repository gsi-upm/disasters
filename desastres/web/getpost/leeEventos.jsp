<%@ page contentType="application/json" pageEncoding="UTF-8"%>
<%@ page isELIgnored = "false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>

<%@ include file="database.jspf" %>

<c:choose>
	<c:when test="${param.action == 'firstTime' && param.nivel gt 0}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT c.id,c.marcador,c.tipo,c.cantidad,c.nombre,c.descripcion,c.info,
				c.latitud,c.longitud,c.direccion,c.size,c.traffic,c.idAssigned,c.planta,
				c.fecha,c.modificado,t.tipo_estado FROM catastrofes c, tipos_estados t
			WHERE c.modificado > ?
			AND c.estado = t.id
			AND t.tipo_estado != 'erased'
			<sql:param value="${param.fecha}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'firstTime' && param.nivel == 0}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT c.id,c.marcador,c.tipo,c.cantidad,c.nombre,c.descripcion,c.info,
				c.latitud,c.longitud,c.direccion,c.size,c.traffic,c.idAssigned,c.planta,
				c.fecha,c.modificado,t.tipo_estado FROM catastrofes c, tipos_estados t
			WHERE c.modificado > ?
			AND c.marcador != 'people'
			AND c.estado = t.id
			AND t.tipo_estado != 'erased'
			<sql:param value="${param.fecha}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'notFirst' && param.nivel gt 0}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT c.id,c.marcador,c.tipo,c.cantidad,c.nombre,c.descripcion,c.info,
				c.latitud,c.longitud,c.direccion,c.size,c.traffic,c.idAssigned,c.planta,
				c.fecha,c.modificado,t.tipo_estado FROM catastrofes c, tipos_estados t
			WHERE c.modificado > ?
			AND c.estado = t.id
			<sql:param value="${param.fecha}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.action == 'notFirst' && param.nivel == 0}">
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT c.id,c.marcador,c.tipo,c.cantidad,c.nombre,c.descripcion,c.info,
				c.latitud,c.longitud,c.direccion,c.size,c.traffic,c.idAssigned,c.usuario,c.planta,
				c.fecha,c.modificado,t.tipo_estado FROM catastrofes c, tipos_estados t
			WHERE c.modificado > ?
			AND c.marcador != 'people'
			AND c.estado = t.id
			<sql:param value="${param.fecha}"/>
		</sql:query>
	</c:when>
	<c:otherwise>
		<p class="textoError">Invalid action:${param.action}</p>
	</c:otherwise>
</c:choose> 
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
		<json:property name="user" value="${evento.usuario}"/>
		<json:property name="floor" value="${evento.planta}"/>
		<json:property name="date" value="${evento.fecha}"/>
		<json:property name="modified" value="${evento.modificado}"/>
	</json:object> ,
</c:forEach>
]