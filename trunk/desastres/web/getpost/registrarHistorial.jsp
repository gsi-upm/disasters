<%@page contentType="application/x-sql" pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>

<%@include file="../jspf/database.jspf"%>

<c:if test="${param.accion != 'crear'}">
	<sql:update dataSource="${CatastrofesServer}">
		INSERT INTO HISTORIAL(ID_USUARIO,TIPO,ID_TIPO,ID_EMERGENCIA,EVENTO)
		VALUES((SELECT ID FROM USUARIOS WHERE NOMBRE_USUARIO = ?),
			(SELECT ID FROM TIPOS_MARCADORES WHERE TIPO_MARCADOR = ?),
			(SELECT ID FROM TIPOS_CATASTROFES WHERE TIPO_CATASTROFE = ?), ?, ?)
		<sql:param value="${param.usuario}"/>
		<sql:param value="${param.marcador}"/>
		<sql:param value="${param.tipo}"/>
		<sql:param value="${param.emergencia}"/>
		<sql:param value="${param.evento}"/>
	</sql:update>
</c:if>
<c:if test="${param.accion == 'crear'}">
	<sql:update dataSource="${CatastrofesServer}">
		INSERT INTO HISTORIAL(ID_USUARIO,TIPO,ID_TIPO,ID_EMERGENCIA,EVENTO)
		VALUES((SELECT ID FROM USUARIOS WHERE NOMBRE_USUARIO = ?),
			(SELECT ID FROM TIPOS_MARCADORES WHERE TIPO_MARCADOR = ?),
			(SELECT ID FROM TIPOS_CATASTROFES WHERE TIPO_CATASTROFE = ?),
			(SELECT ID FROM CATASTROFES WHERE FECHA = ?), ?)
		<sql:param value="${param.usuario}"/>
		<sql:param value="${param.marcador}"/>
		<sql:param value="${param.tipo}"/>
		<sql:param value="${param.emergencia}"/>
		<sql:param value="${param.evento}"/>
	</sql:update>
</c:if>