<%@ page contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.Date" %>
<%@ page isELIgnored = "false" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %> 
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>

<%@ include file="database.jspf" %>

<% String modif = "'" + new Timestamp(new java.util.Date().getTime()).toString() + "'";%>  

<sql:update dataSource="${CatastrofesServer}">
	UPDATE catastrofes SET
	marcador = ?, tipo = ?, cantidad = ?, nombre = ?, descripcion = ?, info = ?,
	latitud = ?, longitud = ?, direccion = ?, estado = ?, size=?, traffic=?,
	fecha = ?, usuario = ?, planta = ?, sintomas = ?, modificado = <%=modif%>
	WHERE id = ?
	<sql:param value="${param.marcador}"/>
	<sql:param value="${param.tipo}"/>
    <sql:param value="${param.cantidad}"/>
    <sql:param value="${param.nombre}"/>
    <sql:param value="${param.descripcion}"/>
    <sql:param value="${param.info}"/>
    <sql:param value="${param.latitud}"/>
    <sql:param value="${param.longitud}"/>
    <sql:param value="${param.direccion}"/>
    <sql:param value="${param.estado}"/>
    <sql:param value="${param.size}"/>
    <sql:param value="${param.traffic}"/>
    <sql:param value="${param.fecha}"/>
    <sql:param value="${param.usuario}"/>
    <sql:param value="${param.planta}"/>
	<sql:param value="${param.sintomas}"/>
    <sql:param value="${param.id}"/>
</sql:update>
<c:if test="${param.idAssigned != 0 && param.accion != 'eliminarAsociacion'}">
	<sql:update dataSource="${CatastrofesServer}">
		INSERT INTO asociaciones_heridos_emergencias(id_herido, id_emergencia, estado)
		VALUES(?,?,'active')
		<sql:param value="${param.id}"/>
		<sql:param value="${param.idAssigned}"/>
	</sql:update>
</c:if>
<c:if test="${param.accion == 'eliminar'}">
	<sql:update dataSource="${CatastrofesServer}">
		UPDATE catastrofes
		SET estado = 'active', modificado = <%=modif%>, idAssigned = 0
		WHERE idAssigned = ?
		AND estado != 'erased';
		<sql:param value="${param.id}"/>
	</sql:update>
	<c:if test="${param.marcador == 'people'}">
		<sql:update dataSource="${CatastrofesServer}">
			UPDATE asociaciones_heridos_emergencias
			SET estado = 'erased'
			WHERE id_herido = ?
			AND estado != 'erased'
			<sql:param value="${param.id}"/>
		</sql:update>
	</c:if>
	<c:if test="${param.marcador == 'event'}">
		<sql:update dataSource="${CatastrofesServer}">
			UPDATE asociaciones_heridos_emergencias
			SET estado = 'erased'
			WHERE id_emergencia = ?
			AND estado != 'erased'
			<sql:param value="${param.id}"/>
		</sql:update>
	</c:if>
</c:if>
<c:if test="${param.accion == 'eliminarAsociacion'}">
	<sql:update dataSource="${CatastrofesServer}">
		UPDATE asociaciones_heridos_emergencias
		SET estado = 'erased'
		WHERE id_herido = ?
		AND id_emergencia = ?
		AND estado != 'erased'
		<sql:param value="${param.id_herido}"/>
		<sql:param value="${param.id_emergencia}"/>
	</sql:update>
</c:if>
ok