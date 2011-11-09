<%@page contentType="application/x-sql" pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>

<%@include file="../jspf/database.jspf"%>

<c:if test="${param.accion != 'crear'}">
	<sql:update dataSource="${CatastrofesServer}">
		INSERT INTO historial(id_usuario,tipo,id_tipo,id_emergencia,evento)
		VALUES((SELECT id FROM usuarios WHERE nombre_usuario = ?),
			(SELECT id FROM tipos_marcadores WHERE tipo_marcador = ?),
			(SELECT id FROM tipos_catastrofes WHERE tipo_catastrofe = ?), ?, ?)
		<sql:param value="${param.usuario}"/>
		<sql:param value="${param.marcador}"/>
		<sql:param value="${param.tipo}"/>
		<sql:param value="${param.emergencia}"/>
		<sql:param value="${param.evento}"/>
	</sql:update>
</c:if>
<c:if test="${param.accion == 'crear'}">
	<sql:update dataSource="${CatastrofesServer}">
		INSERT INTO historial(id_usuario,tipo,id_tipo,id_emergencia,evento)
		VALUES((SELECT id FROM usuarios WHERE nombre_usuario = ?),
			(SELECT id FROM tipos_marcadores WHERE tipo_marcador = ?),
			(SELECT id FROM tipos_catastrofes WHERE tipo_catastrofe = ?),
			(SELECT id FROM catastrofes WHERE fecha = ?), ?)
		<sql:param value="${param.usuario}"/>
		<sql:param value="${param.marcador}"/>
		<sql:param value="${param.tipo}"/>
		<sql:param value="${param.emergencia}"/>
		<sql:param value="${param.evento}"/>
	</sql:update>
</c:if>