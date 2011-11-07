<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page isELIgnored = "false" %>
<%@ page import="java.sql.Timestamp, java.util.Date" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>

<%@ include file="../jspf/database.jspf" %>

<% String modif = "'" + new Timestamp(new Date().getTime()).toString() + "'"; %>

<c:if test="${param.accion == 'modificar' || param.accion == 'cambioTipo' || param.accion == 'eliminar'}">
	<sql:update dataSource="${CatastrofesServer}">
		UPDATE catastrofes
		SET marcador = (SELECT id FROM tipos_marcadores WHERE tipo_marcador = ?),
			tipo = (SELECT id FROM tipos_catastrofes WHERE tipo_catastrofe = ?),
			cantidad = ?, nombre = ?, descripcion = ?, info = ?, latitud = ?,
			longitud = ?, direccion = ?, size = ?, traffic = ?, planta = ?,
			estado = (SELECT id FROM tipos_estados WHERE tipo_estado = ?),
			fecha = ?, modificado = <%=modif%>, usuario = ?
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
		<sql:param value="${param.size}"/>
		<sql:param value="${param.traffic}"/>
		<sql:param value="${param.planta}"/>
		<sql:param value="${param.estado}"/>
		<sql:param value="${param.fecha}"/>
		<sql:param value="${param.usuario}"/>
		<sql:param value="${param.id}"/>
	</sql:update>
</c:if>
<%--<c:if test="${param.idAssigned != 0 && param.accion != 'eliminarAsociacion'}">
	<sql:update dataSource="${CatastrofesServer}">
		INSERT INTO asociaciones_heridos_emergencias(id_herido, id_emergencia, estado)
		VALUES(?,?,(SELECT id FROM tipos_estados WHERE tipo_estado = 'active'))
		<sql:param value="${param.id}"/>
		<sql:param value="${param.idAssigned}"/>
	</sql:update>
</c:if>--%>
<c:choose>
	<c:when test="${param.accion == 'eliminar'}">
		<c:if test="${param.marcador == 'people'}">
			<sql:update dataSource="${CatastrofesServer}">
				UPDATE asociaciones_heridos_emergencias
				SET estado = (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased')
				WHERE id_herido = ?
				AND estado != (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased')
				<sql:param value="${param.id}"/>
			</sql:update>
			<%--<sql:update dataSource="${CatastrofesServer}">
				UPDATE sintomas
				SET estado = (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased')
				WHERE id_herido = ?
				AND estado != (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased')
				<sql:param value="${param.id}"/>
			</sql:update>--%>
		</c:if>
		<c:if test="${param.marcador == 'event'}">
			<sql:update dataSource="${CatastrofesServer}">
				UPDATE asociaciones_heridos_emergencias
				SET estado = (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased')
				WHERE id_emergencia = ?
				<sql:param value="${param.id}"/>
			</sql:update>
		</c:if>
		<sql:update dataSource="${CatastrofesServer}">
			UPDATE actividades
			SET estado = (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased')
			WHERE id_emergencia = ?
			<sql:param value="${param.id}"/>
		</sql:update>
		<sql:update dataSource="${CatastrofesServer}">
			UPDATE catastrofes
			SET estado = (SELECT id FROM tipos_estados WHERE tipo_estado = 'active')
			WHERE marcador = (SELECT id FROM tipos_marcadores WHERE tipo_marcador = 'resource')
			AND nombre NOT IN (SELECT DISTINCT u.nombre_usuario FROM usuarios u, actividades a
				WHERE u.id = a.id_usuario
				AND a.estado = (SELECT id FROM tipos_estados WHERE tipo_estado = 'active'))
		</sql:update>
	</c:when>
	<c:when test="${param.accion == 'eliminarAsociacion'}">
		<sql:update dataSource="${CatastrofesServer}">
			UPDATE asociaciones_heridos_emergencias
			SET estado = (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased')
			WHERE id_herido = ?
			AND id_emergencia = ?
			AND estado != (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased')
			<sql:param value="${param.id_herido}"/>
			<sql:param value="${param.id_emergencia}"/>
		</sql:update>
	</c:when>
	<c:when test="${param.accion == 'asociar'}">
		<c:if test="${param.fecha == null}">
			<sql:update dataSource="${CatastrofesServer}">
				INSERT INTO asociaciones_heridos_emergencias(id_herido,id_emergencia,estado)
				VALUES(?,?,(SELECT id FROM tipos_estados WHERE tipo_estado = 'active'))
				<sql:param value="${param.id_herido}"/>
				<sql:param value="${param.id_emergencia}"/>
			</sql:update>
		</c:if>
		<c:if test="${param.fecha != null}">
			<sql:update dataSource="${CatastrofesServer}">
				INSERT INTO asociaciones_heridos_emergencias(id_herido, id_emergencia, estado)
				VALUES((SELECT id FROM catastrofes WHERE fecha = ?), ?,
					(SELECT id FROM tipos_estados WHERE tipo_estado = 'active'))
				<sql:param value="${param.fecha}"/>
				<sql:param value="${param.id_emergencia}"/>
			</sql:update>
		</c:if>
	</c:when>
	<c:when test="${param.accion == 'cambioTipo'}">
		<sql:update dataSource="${CatastrofesServer}">
			UPDATE actividades
			SET estado = (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased')
			WHERE id_emergencia = ?
			<sql:param value="${param.id}"/>
		</sql:update>
		<sql:update dataSource="${CatastrofesServer}">
			UPDATE catastrofes
			SET estado = (SELECT id FROM tipos_estados WHERE tipo_estado = 'active')
			WHERE id = ?
			<sql:param value="${param.id}"/>
		</sql:update>
		<sql:update dataSource="${CatastrofesServer}">
			UPDATE catastrofes
			SET estado = (SELECT id FROM tipos_estados WHERE tipo_estado = 'active')
			WHERE marcador = (SELECT id FROM tipos_marcadores WHERE tipo_marcador = 'resource')
			AND nombre NOT IN (SELECT DISTINCT u.nombre_usuario FROM usuarios u, actividades a
				WHERE u.id = a.id_usuario
				AND estado = (SELECT id FROM tipos_estados WHERE tipo_estado = 'active'))
		</sql:update>
	</c:when>
	<%--<c:when test="${param.accion == 'annadirSintoma'}">
		<c:if test="${param.fecha == null}">
			<sql:update dataSource="${CatastrofesServer}">
				INSERT INTO sintomas(id_herido,id_sintoma,estado)
				VALUES(?, (SELECT id FROM tipos_sintomas WHERE tipo = ?),
					(SELECT id FROM tipos_estados WHERE tipo_estado = 'active'))
				<sql:param value="${param.id_herido}"/>
				<sql:param value="${param.tipo_sintoma}"/>
			</sql:update>
		</c:if>
		<c:if test="${param.fecha != null}">
			<sql:update dataSource="${CatastrofesServer}">
				INSERT INTO sintomas(id_herido, id_sintoma, estado)
				VALUES((SELECT id FROM catastrofes WHERE fecha = ?),
					(SELECT id FROM tipos_sintomas WHERE tipo = ?),
					(SELECT id FROM tipos_estados WHERE tipo_estado = 'active'))
				<sql:param value="${param.fecha}"/>
				<sql:param value="${param.tipo_sintoma}"/>
			</sql:update>
		</c:if>
	</c:when>
	<c:when test="${param.accion == 'eliminarSintoma'}">
		<sql:update dataSource="${CatastrofesServer}">
			UPDATE sintomas
			SET estado = (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased')
			WHERE id_herido = ?
			AND id_sintoma = (SELECT id FROM tipos_sintomas WHERE tipo = ?)
			AND estado != (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased')
			<sql:param value="${param.id_herido}"/>
			<sql:param value="${param.tipo_sintoma}"/>
		</sql:update>
	</c:when>--%>
</c:choose>
<c:if test="${param.accion == 'cerrarSesion'}">
	<sql:update dataSource="${CatastrofesServer}">
		UPDATE catastrofes
		SET estado = (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased'), modificado = <%=modif%>
		WHERE nombre = ?
		<sql:param value="${param.nombre}"/>
	</sql:update>
</c:if>