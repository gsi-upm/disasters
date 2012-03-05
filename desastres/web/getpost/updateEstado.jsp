<%@page contentType="application/x-sql" pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@page import="java.sql.Timestamp, java.util.Date"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>

<%@include file="../jspf/database.jspf"%>

<% String modif = "'" + new Timestamp(new Date().getTime()).toString() + "'"; %>

<c:if test="${param.accion != 'detener'}">
	<%-- Pone como borrado la asociacion anterior... --%>
	<sql:update dataSource="${CatastrofesServer}">
		UPDATE ACTIVIDADES
		SET ESTADO = (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
		WHERE ID_USUARIO = (SELECT ID FROM USUARIOS WHERE NOMBRE_USUARIO = ?)
		AND ID_EMERGENCIA = ?
		<sql:param value="${param.nombreUsuario}"/>
		<sql:param value="${param.idEvento}"/>
	</sql:update>

	<%-- ... e introduce la nueva --%>
	<c:choose>
		<c:when test="${param.estadoEvento == 'erased' || param.accion == 'dejar'}">
			<sql:update dataSource="${CatastrofesServer}">
				INSERT INTO ACTIVIDADES(ID_USUARIO, ID_EMERGENCIA, ID_TIPO_ACTIVIDAD, ESTADO)
				VALUES((SELECT ID FROM USUARIOS WHERE NOMBRE_USUARIO = ?), ?,
					(SELECT ID FROM TIPOS_ACTIVIDADES WHERE TIPO = ?),
					(SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased'))
				<sql:param value="${param.nombreUsuario}"/>
				<sql:param value="${param.idEvento}"/>
				<sql:param value="${param.accion}"/>
			</sql:update>
		</c:when>
		<c:otherwise>
			<sql:update dataSource="${CatastrofesServer}">
				INSERT INTO ACTIVIDADES(ID_USUARIO, ID_EMERGENCIA, ID_TIPO_ACTIVIDAD, ESTADO)
				VALUES((SELECT ID FROM USUARIOS WHERE NOMBRE_USUARIO = ?), ?,
					(SELECT ID FROM TIPOS_ACTIVIDADES WHERE TIPO = ?),
					(SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'active'))
				<sql:param value="${param.nombreUsuario}"/>
				<sql:param value="${param.idEvento}"/>
				<sql:param value="${param.accion}"/>
			</sql:update>
		</c:otherwise>
	</c:choose>
	<c:choose>
		<c:when test="${param.accion == 'apagado' || param.accion == 'controlado' ||
				param.accion == 'acordonado' || param.accion == 'curado' || param.accion == 'rescatado'}">
			<sql:update dataSource="${CatastrofesServer}">
				UPDATE CATASTROFES
				SET ESTADO = (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = ?), MODIFICADO = <%=modif%>
				WHERE ID = ?
				<sql:param value="${param.estadoEvento}"/>
				<sql:param value="${param.idEvento}"/>
			</sql:update>
			<sql:update dataSource="${CatastrofesServer}">
				UPDATE CATASTROFES
				SET ESTADO = (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'active'), MODIFICADO = <%=modif%>
				WHERE NOMBRE = ? AND ESTADO = (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'acting')
				AND NOMBRE NOT IN (SELECT DISTINCT NOMBRE_USUARIO FROM USUARIOS U, ACTIVIDADES A
					WHERE U.ID = ID_USUARIO
					AND ESTADO = (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'active'))
				<sql:param value="${param.nombreUsuario}"/>
			</sql:update>
		</c:when>
		<c:when test="${param.accion == 'dejar'}">
			<sql:update dataSource="${CatastrofesServer}">
				UPDATE CATASTROFES
				SET ESTADO = (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'active'), MODIFICADO = <%=modif%>
				WHERE ID = ?
				AND ID NOT IN (SELECT DISTINCT ID_EMERGENCIA FROM ACTIVIDADES
					WHERE ESTADO = (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'active'))
				<sql:param value="${param.idEvento}"/>
			</sql:update>
			<sql:update dataSource="${CatastrofesServer}">
				UPDATE CATASTROFES
				SET ESTADO = (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'active'), MODIFICADO = <%=modif%>
				WHERE NOMBRE = ? AND ESTADO = (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'acting')
				AND NOMBRE NOT IN (SELECT DISTINCT NOMBRE_USUARIO FROM USUARIOS U, ACTIVIDADES A
					WHERE U.ID = ID_USUARIO
					AND ESTADO = (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'active'))
				<sql:param value="${param.nombreUsuario}"/>
			</sql:update>
		</c:when>
		<c:otherwise>
			<sql:update dataSource="${CatastrofesServer}">
				UPDATE CATASTROFES
				SET ESTADO = (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = ?), MODIFICADO = <%=modif%>
				WHERE ID = ?
				<sql:param value="${param.estadoEvento}"/>
				<sql:param value="${param.idEvento}"/>
			</sql:update>
			<sql:update dataSource="${CatastrofesServer}">
				UPDATE CATASTROFES
				SET ESTADO = (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = ?), MODIFICADO = <%=modif%>
				WHERE NOMBRE = ? AND ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
				<sql:param value="${param.estadoUsuario}"/>
				<sql:param value="${param.nombreUsuario}"/>
			</sql:update>
		</c:otherwise>
	</c:choose>
	<c:if test="${param.accion == 'curado' || param.accion == 'rescatado'}">
		<sql:update dataSource="${CatastrofesServer}">
			UPDATE CATASTROFES
			SET ESTADO = (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'active'),
				TIPO = (SELECT ID FROM TIPOS_CATASTROFES WHERE TIPO_CATASTROFE = 'healthy'),
				MODIFICADO = <%=modif%>
			WHERE ID = ?
			<sql:param value="${param.idEvento}"/>
		</sql:update>
	</c:if>
</c:if>
<c:if test="${param.accion == 'detener'}">
	<sql:update dataSource="${CatastrofesServer}">
		UPDATE ACTIVIDADES
		SET ESTADO = (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
		WHERE ID_EMERGENCIA = ?
		AND ID_USUARIO = (SELECT ID FROM USUARIOS WHERE NOMBRE_USUARIO = ?)
		<sql:param value="${param.idEmergencia}"/>
		<sql:param value="${param.nombreUsuario}"/>
	</sql:update>
	<sql:update dataSource="${CatastrofesServer}">
		UPDATE CATASTROFES
		SET ESTADO = (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'active'), MODIFICADO = <%=modif%>
		WHERE ID = ?
		AND ID NOT IN (SELECT DISTINCT ID_EMERGENCIA FROM ACTIVIDADES
			WHERE ESTADO = (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'active'))
		<sql:param value="${param.idEmergencia}"/>
	</sql:update>
	<sql:update dataSource="${CatastrofesServer}">
		UPDATE CATASTROFES
		SET ESTADO = (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'active'), MODIFICADO = <%=modif%>
		WHERE ID = ?
		AND (SELECT ID FROM USUARIOS WHERE NOMBRE_USUARIO = ?)
		NOT IN (SELECT DISTINCT ID_USUARIO FROM ACTIVIDADES
			WHERE ESTADO = (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'active'))
		<sql:param value="${param.idEvento}"/>
		<sql:param value="${param.nombreUsuario}"/>
	</sql:update>
</c:if>