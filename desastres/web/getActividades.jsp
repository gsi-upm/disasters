<%@ page contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.Date" %>
<%@ page isELIgnored = "false" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %> 
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>

<%@ include file="database.jspf" %>

<c:if test="${param.id == null}">
	<c:choose>
		<c:when test="${param.marcador == 'event'}">
			<sql:query var="acciones" dataSource="${CatastrofesServer}">
				SELECT id, tipo, descripcion
				FROM tipos_actividades
				WHERE estado_emergencia=?
				AND (tipo_emergencia='' OR tipo_emergencia=? OR tipo_emergencia=?)
				<sql:param value="${param.estado}"/>
				<sql:param value="${param.tipo}"/>
				<sql:param value="${param.marcador}"/>
			</sql:query>
		</c:when>
		<c:when test="${param.marcador == 'people'}">
			<c:choose>
				<c:when test="${param.tipo == 'healthy' || param.tipo == 'trapped'}">
					<sql:query var="acciones" dataSource="${CatastrofesServer}">
						SELECT id, tipo, descripcion
						FROM tipos_actividades
						WHERE estado_emergencia=?
						AND (tipo_emergencia='' OR tipo_emergencia=?)
						<sql:param value="${param.estado}"/>
						<sql:param value="${param.tipo}"/>
					</sql:query>
				</c:when>
				<c:otherwise>
					<sql:query var="acciones" dataSource="${CatastrofesServer}">
						SELECT id, tipo, descripcion
						FROM tipos_actividades
						WHERE estado_emergencia=?
						AND (tipo_emergencia='' OR tipo_emergencia=?)
						<sql:param value="${param.estado}"/>
						<sql:param value="${param.marcador}"/>
					</sql:query>
				</c:otherwise>
			</c:choose>
		</c:when>
	</c:choose>
</c:if>

<c:if test="${param.id != null}">
	<c:choose>
		<c:when test="${param.marcador == 'event' || param.marcador == 'people'}">
			<sql:query var="acciones" dataSource="${CatastrofesServer}">
				SELECT a.id, u.nombre_usuario, t.tipo
				FROM actividades a, tipos_actividades t, usuarios u
				WHERE a.id_emergencia = ?
				AND a.id_tipo_actividad = t.id
				AND a.id_usuario = u.id
				AND a.estado = 'active'
				<sql:param value="${param.id}"/>
			</sql:query>
		</c:when>
		<c:when test="${param.marcador == 'resource'}">
			<sql:query var="acciones" dataSource="${CatastrofesServer}">
				SELECT a.id_emergencia, c.nombre, t.tipo
				FROM actividades a, tipos_actividades t, catastrofes c
				WHERE a.id_usuario = ?
				AND a.id_tipo_actividad = t.id
				AND a.id_emergencia = c.id
				AND a.estado = 'active'
				<sql:param value="${param.id}"/>
			</sql:query>
		</c:when>
	</c:choose>
</c:if>

<%--<sql:query var="acciones" dataSource="${CatastrofesServer}">
	SELECT id, tipo, descripcion
	FROM tipos_actividades
	WHERE estado_emergencia=?
	AND (tipo_emergencia='' OR tipo_emergencia=? OR tipo_emergencia=?)
	<sql:param value="${param.estado}"/>
	<sql:param value="${param.tipo}"/>
	<sql:param value="${param.marcador}"/>
</sql:query>--%>

[
<c:forEach var="accion" items="${acciones.rows}">
    <json:object name="temp">
        <json:property name="id" value="${accion.id}"/>
        <json:property name="tipo" value="${accion.tipo}"/>
        <json:property name="descripcion" value="${accion.descripcion}"/>
        <json:property name="nombre_usuario" value="${accion.nombre_usuario}"/>
        <json:property name="nombre" value="${accion.nombre}"/>
        <json:property name="id_emergencia" value="${accion.id_emergencia}"/>
	</json:object> ,
</c:forEach>
]
