<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page isELIgnored = "false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>

<%@ include file="database.jspf" %>

<c:if test="${param.accion != 'crear'}">
	<c:if test="${param.marcador == 'event'}">
		<sql:update dataSource="${CatastrofesServer}">
			INSERT INTO historial(id_usuario,tipo,id_tipo,id_emergencia,evento)
			VALUES((SELECT id FROM usuarios WHERE nombre_usuario = ?), 'event',
			       (SELECT id FROM tipos_emergencias WHERE tipo = ?), ?, ?)
			<sql:param value="${param.usuario}"/>
			<sql:param value="${param.tipo}"/>
			<sql:param value="${param.idEmergencia}"/>
			<sql:param value="${param.evento}"/>
		</sql:update>
	</c:if>
	<c:if test="${param.marcador == 'people'}">
		<sql:update dataSource="${CatastrofesServer}">
			INSERT INTO historial(id_usuario,tipo,id_tipo,id_emergencia,evento)
			VALUES((SELECT id FROM usuarios WHERE nombre_usuario = ?), 'people',
			       (SELECT id FROM tipos_heridos WHERE tipo = ?), ?, ?)
			<sql:param value="${param.usuario}"/>
			<sql:param value="${param.tipo}"/>
			<sql:param value="${param.idEmergencia}"/>
			<sql:param value="${param.evento}"/>
		</sql:update>
	</c:if>
</c:if>
<c:if test="${param.accion == 'crear'}">
	<c:if test="${param.marcador == 'event'}">
		<sql:update dataSource="${CatastrofesServer}">
			INSERT INTO historial(id_usuario,tipo,id_tipo,id_emergencia,evento)
			VALUES((SELECT id FROM usuarios WHERE nombre_usuario = ?), 'event',
			       (SELECT id FROM tipos_emergencias WHERE tipo = ?),
				   (SELECT id FROM catastrofes WHERE fecha = ?), ?)
			<sql:param value="${param.usuario}"/>
			<sql:param value="${param.tipo}"/>
			<sql:param value="${param.idEmergencia}"/>
			<sql:param value="${param.evento}"/>
		</sql:update>
	</c:if>
	<c:if test="${param.marcador == 'people'}">
		<sql:update dataSource="${CatastrofesServer}">
			INSERT INTO historial(id_usuario,tipo,id_tipo,id_emergencia,evento)
			VALUES((SELECT id FROM usuarios WHERE nombre_usuario = ?), 'people',
			       (SELECT id FROM tipos_heridos WHERE tipo = ?),
				   (SELECT id FROM catastrofes WHERE fecha = ?), ?)
			<sql:param value="${param.usuario}"/>
			<sql:param value="${param.tipo}"/>
			<sql:param value="${param.idEmergencia}"/>
			<sql:param value="${param.evento}"/>
		</sql:update>
	</c:if>
</c:if>