<%@page contentType="application/json" pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@taglib prefix="json" uri="http://www.atg.com/taglibs/json"%>

<%@include file="../jspf/database.jspf"%>

<c:if test="${param.ID == null}">
	<c:choose>
		<c:when test="${param.marcador == 'event'}">
			<sql:query var="acciones" dataSource="${CatastrofesServer}">
				SELECT ID, TIPO, DESCRIPCION
				FROM TIPOS_ACTIVIDADES
				WHERE ESTADO_EMERGENCIA = (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = ?)
				AND (TIPO_EMERGENCIA = (SELECT ID FROM TIPOS_CATASTROFES WHERE TIPO_CATASTROFE = ?)
					OR TIPO_MARCADOR IS NULL)
				<sql:param value="${param.estado}"/>
				<sql:param value="${param.tipo}"/>
			</sql:query>
		</c:when>
		<c:when test="${param.marcador == 'people'}">
			<c:choose>
				<c:when test="${param.TIPO == 'healthy' || param.TIPO == 'trapped'}">
					<sql:query var="acciones" dataSource="${CatastrofesServer}">
						SELECT ID, TIPO, DESCRIPCION
						FROM TIPOS_ACTIVIDADES
						WHERE ESTADO_EMERGENCIA = (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = ?)
						AND (TIPO_EMERGENCIA = (SELECT ID FROM TIPOS_CATASTROFES WHERE TIPO_CATASTROFE = ?)
							OR TIPO_MARCADOR IS NULL)
						<sql:param value="${param.estado}"/>
						<sql:param value="${param.tipo}"/>
					</sql:query>
				</c:when>
				<c:otherwise>
					<sql:query var="acciones" dataSource="${CatastrofesServer}">
						SELECT ID, TIPO, DESCRIPCION
						FROM TIPOS_ACTIVIDADES
						WHERE ESTADO_EMERGENCIA = (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = ?)
						AND ((TIPO_MARCADOR = (SELECT ID FROM TIPOS_MARCADORES WHERE TIPO_MARCADOR = 'people')
								AND TIPO_EMERGENCIA IS NULL)
							OR TIPO_MARCADOR IS NULL)
						<sql:param value="${param.estado}"/>
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
				SELECT A.ID, NOMBRE_USUARIO, TIPO
				FROM ACTIVIDADES A, TIPOS_ACTIVIDADES T, USUARIOS U
				WHERE A.ID_EMERGENCIA = ?
				AND ID_TIPO_ACTIVIDAD = T.ID
				AND ID_USUARIO = U.ID
				AND ESTADO = (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'active')
				<sql:param value="${param.id}"/>
			</sql:query>
		</c:when>
		<c:when test="${param.marcador == 'resource'}">
			<sql:query var="acciones" dataSource="${CatastrofesServer}">
				SELECT A.ID_EMERGENCIA, C.NOMBRE, T.TIPO
				FROM ACTIVIDADES A, TIPOS_ACTIVIDADES T, CATASTROFES C
				WHERE ID_USUARIO = (SELECT ID FROM USUARIOS WHERE NOMBRE_USUARIO = (SELECT NOMBRE FROM CATASTROFES WHERE ID = ?))
				AND ID_TIPO_ACTIVIDAD = T.ID
				AND ID_EMERGENCIA = C.ID
				AND A.ESTADO = (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'active')
				<sql:param value="${param.id}"/>
			</sql:query>
		</c:when>
	</c:choose>
</c:if>

<json:array>
	<c:forEach var="accion" items="${acciones.rows}">
		<json:object>
			<json:property name="id" value="${accion.id}"/>
			<json:property name="tipo" value="${accion.tipo}"/>
			<json:property name="descripcion" value="${accion.descripcion}"/>
			<json:property name="nombre_usuario" value="${accion.nombre_usuario}"/>
			<json:property name="nombre" value="${accion.nombre}"/>
			<json:property name="id_emergencia" value="${accion.id_emergencia}"/>
		</json:object>
	</c:forEach>
</json:array>