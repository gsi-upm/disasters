<%@page contentType="application/x-sql" pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@page import="java.sql.Timestamp, java.util.Date"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>

<%@include file="../jspf/database.jspf"%>

<% String modif = "'" + new Timestamp(new Date().getTime()).toString() + "'"; %>

<c:if test="${param.accion == 'modificar' || param.accion == 'cambioTipo' || param.accion == 'eliminar'}">
	<sql:update dataSource="${CatastrofesServer}">
		UPDATE CATASTROFES
		SET MARCADOR = (SELECT ID FROM TIPOS_MARCADORES WHERE TIPO_MARCADOR = ?),
			TIPO = (SELECT ID FROM TIPOS_CATASTROFES WHERE TIPO_CATASTROFE = ?),
			CANTIDAD = ?, NOMBRE = ?, DESCRIPCION = ?, INFO = ?, LATITUD = ?,
			LONGITUD = ?, DIRECCION = ?, SIZE = ?, TRAFFIC = ?, PLANTA = ?,
			ESTADO = (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = ?),
			IDASSIGNED = ?, FECHA = ?, MODIFICADO = <%=modif%>, USUARIO = ?
		WHERE ID = ?
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
		<sql:param value="${param.idAssigned}"/>
		<sql:param value="${param.fecha}"/>
		<sql:param value="${param.usuario}"/>
		<sql:param value="${param.id}"/>
	</sql:update>
</c:if>
<%--<c:if test="${param.idAssigned != 0 && param.accion != 'eliminarAsociacion'}">
	<sql:update dataSource="${CatastrofesServer}">
		INSERT INTO ASOCIACIONES_HERIDOS_EMERGENCIAS(ID_HERIDO, ID_EMERGENCIA, ESTADO)
		VALUES(?,?,(SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'active'))
		<sql:param value="${param.ID}"/>
		<sql:param value="${param.idAssigned}"/>
	</sql:update>
</c:if>--%>
<c:if test="${proyecto == 'caronte'}">
	<c:choose>
		<c:when test="${param.accion == 'eliminar'}">
			<c:if test="${param.marcador == 'people'}">
				<sql:update dataSource="${CatastrofesServer}">
					UPDATE ASOCIACIONES_HERIDOS_EMERGENCIAS
					SET ESTADO = (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
					WHERE ID_HERIDO = ?
					AND ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
					<sql:param value="${param.id}"/>
				</sql:update>
				<%--<sql:update dataSource="${CatastrofesServer}">
					UPDATE SINTOMAS
					SET ESTADO = (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
					WHERE ID_HERIDO = ?
					AND ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
					<sql:param value="${param.id}"/>
				</sql:update>--%>
			</c:if>
			<c:if test="${param.marcador == 'event'}">
				<sql:update dataSource="${CatastrofesServer}">
					UPDATE ASOCIACIONES_HERIDOS_EMERGENCIAS
					SET ESTADO = (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
					WHERE ID_EMERGENCIA = ?
					<sql:param value="${param.id}"/>
				</sql:update>
			</c:if>
			<sql:update dataSource="${CatastrofesServer}">
				UPDATE ACTIVIDADES
				SET ESTADO = (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
				WHERE ID_EMERGENCIA = ?
				<sql:param value="${param.id}"/>
			</sql:update>
			<sql:update dataSource="${CatastrofesServer}">
				UPDATE CATASTROFES
				SET ESTADO = (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'active')
				WHERE marcador = (SELECT ID FROM TIPOS_MARCADORES WHERE TIPO_MARCADOR = 'resource')
				AND nombre NOT IN (SELECT DISTINCT NOMBRE_USUARIO FROM USUARIOS U, ACTIVIDADES A
				WHERE U.ID = ID_USUARIO
					AND ESTADO = (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'active'))
			</sql:update>
		</c:when>
		<c:when test="${param.accion == 'eliminarAsociacion'}">
			<sql:update dataSource="${CatastrofesServer}">
				UPDATE ASOCIACIONES_HERIDOS_EMERGENCIAS
				SET ESTADO = (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
				WHERE ID_HERIDO = ?
				AND ID_EMERGENCIA = ?
				AND ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
				<sql:param value="${param.id_herido}"/>
				<sql:param value="${param.id_emergencia}"/>
			</sql:update>
		</c:when>
		<c:when test="${param.accion == 'asociar'}">
			<c:if test="${param.fecha == null}">
				<sql:update dataSource="${CatastrofesServer}">
					INSERT INTO ASOCIACIONES_HERIDOS_EMERGENCIAS(ID_HERIDO, ID_EMERGENCIA, ESTADO)
					VALUES(?,?,(SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'active'))
					<sql:param value="${param.id_herido}"/>
					<sql:param value="${param.id_emergencia}"/>
				</sql:update>
			</c:if>
			<c:if test="${param.fecha != null}">
				<sql:update dataSource="${CatastrofesServer}">
					INSERT INTO ASOCIACIONES_HERIDOS_EMERGENCIAS(ID_HERIDO, ID_EMERGENCIA, ESTADO)
					VALUES((SELECT ID FROM CATASTROFES WHERE FECHA = ?), ?,
						(SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'active'))
					<sql:param value="${param.fecha}"/>
					<sql:param value="${param.id_emergencia}"/>
				</sql:update>
			</c:if>
		</c:when>
		<c:when test="${param.accion == 'cambioTipo'}">
			<sql:update dataSource="${CatastrofesServer}">
				UPDATE ACTIVIDADES
				SET ESTADO = (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
				WHERE ID_EMERGENCIA = ?
				<sql:param value="${param.id}"/>
			</sql:update>
			<sql:update dataSource="${CatastrofesServer}">
				UPDATE CATASTROFES
				SET ESTADO = (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'active')
				WHERE ID = ?
				<sql:param value="${param.id}"/>
			</sql:update>
			<sql:update dataSource="${CatastrofesServer}">
				UPDATE CATASTROFES
				SET ESTADO = (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'active')
				WHERE MARCADOR = (SELECT ID FROM TIPOS_MARCADORES WHERE TIPO_MARCADOR = 'resource')
				AND NOMBRE NOT IN (SELECT DISTINCT NOMBRE_USUARIO FROM USUARIOS U, ACTIVIDADES A
					WHERE U.ID = ID_USUARIO
					AND ESTADO = (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'active'))
			</sql:update>
		</c:when>
		<%--<c:when test="${param.accion == 'annadirSintoma'}">
			<c:if test="${param.fecha == null}">
				<sql:update dataSource="${CatastrofesServer}">
					INSERT INTO SINTOMAS(ID_HERIDO,ID_SINTOMA,ESTADO)
					VALUES(?, (SELECT ID FROM TIPOS_SINTOMAS WHERE TIPO = ?),
						(SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'active'))
					<sql:param value="${param.id_herido}"/>
					<sql:param value="${param.tipo_sintoma}"/>
				</sql:update>
			</c:if>
			<c:if test="${param.fecha != null}">
				<sql:update dataSource="${CatastrofesServer}">
					INSERT INTO SINTOMAS(ID_HERIDO, ID_SINTOMA, ESTADO)
					VALUES((SELECT ID FROM CATASTROFES WHERE FECHA = ?),
						(SELECT ID FROM TIPOS_SINTOMAS WHERE TIPO = ?),
						(SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'active'))
					<sql:param value="${param.fecha}"/>
					<sql:param value="${param.tipo_sintoma}"/>
				</sql:update>
			</c:if>
		</c:when>
		<c:when	test="${param.accion == 'eliminarSintoma'}">
			<sql:update dataSource="${CatastrofesServer}">
				UPDATE SINTOMAS
				SET ESTADO = (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
				WHERE ID_HERIDO = ?
				AND ID_SINTOMA = (SELECT ID FROM tipos_sintomas WHERE TIPO = ?)
				AND ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
				<sql:param value="${param.id_herido}"/>
				<sql:param value="${param.tipo_sintoma}"/>
			</sql:update>
		</c:when>--%>
	</c:choose>
</c:if>