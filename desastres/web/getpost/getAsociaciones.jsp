<%@page contentType="application/json" pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@taglib prefix="json" uri="http://www.atg.com/taglibs/json"%>

<%@include file="../jspf/database.jspf"%>

<c:choose>
	<c:when test="${param.tipo eq 'todasEmergencias'}">
		<c:choose>
			<c:when test="${param.nivel gt 1}">
				<sql:query var="emergencias" dataSource="${CatastrofesServer}">
					SELECT ID, NOMBRE FROM CATASTROFES
					WHERE MARCADOR = (SELECT ID FROM TIPOS_MARCADORES WHERE TIPO_MARCADOR = 'event')
					AND ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
				</sql:query>
			</c:when>
			<c:otherwise>
				<sql:query var="emergencias" dataSource="${CatastrofesServer}">
					SELECT ID, NOMBRE FROM CATASTROFES
					WHERE MARCADOR = (SELECT ID FROM TIPOS_MARCADORES WHERE TIPO_MARCADOR = 'event')
					AND ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
					AND PLANTA < 0
				</sql:query>
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:when test="${param.tipo eq 'emergencias'}">
		<c:choose>
			<c:when test="${param.nivel gt 1}">
				<sql:query var="emergencias" dataSource="${CatastrofesServer}">
					SELECT ID, NOMBRE FROM CATASTROFES
					WHERE MARCADOR = (SELECT ID FROM TIPOS_MARCADORES WHERE TIPO_MARCADOR = 'event')
					AND ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
					AND ID NOT IN (
						SELECT DISTINCT ID_EMERGENCIA
						FROM ASOCIACIONES_HERIDOS_EMERGENCIAS
						WHERE ID_HERIDO = ?
						AND ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased'))
					<sql:param value="${param.iden}"/>
				</sql:query>
			</c:when>
			<c:otherwise>
				<sql:query var="emergencias" dataSource="${CatastrofesServer}">
					SELECT ID, NOMBRE FROM CATASTROFES
					WHERE MARCADOR = (SELECT ID FROM TIPOS_MARCADORES WHERE TIPO_MARCADOR = 'event')
					AND ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
					AND ID NOT IN (
						SELECT DISTINCT ID_EMERGENCIA
						FROM ASOCIACIONES_HERIDOS_EMERGENCIAS
						WHERE ID_HERIDO = ?
						AND ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased'))
					AND PLANTA < 0
					<sql:param value="${param.iden}"/>
				</sql:query>
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:when test="${param.tipo eq 'asociadas'}">
		<c:choose>
			<c:when test="${param.nivel gt 1}">
				<sql:query var="emergencias" dataSource="${CatastrofesServer}">
					SELECT C.ID, NOMBRE FROM CATASTROFES C, ASOCIACIONES_HERIDOS_EMERGENCIAS A
					WHERE C.ID = ID_EMERGENCIA
					AND ID_HERIDO = ?
					AND C.ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
					AND A.ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
					<sql:param value="${param.iden}"/>
				</sql:query>
			</c:when>
			<c:otherwise>
				<sql:query var="emergencias" dataSource="${CatastrofesServer}">
					SELECT C.ID, NOMBRE FROM CATASTROFES c, ASOCIACIONES_HERIDOS_EMERGENCIAS A
					WHERE C.ID = ID_EMERGENCIA
					AND ID_HERIDO = ?
					AND C.ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
					AND A.ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
					AND PLANTA < 0
					<sql:param value="${param.iden}"/>
				</sql:query>
			</c:otherwise>
		</c:choose>
	</c:when>
</c:choose>

<json:array>
	<c:forEach var="emergencia" items="${emergencias.rows}">
		<json:object>
			<json:property name="id" value="${emergencia.id}"/>
			<json:property name="nombre" value="${emergencia.nombre}"/>
		</json:object>
	</c:forEach>
</json:array>