<%@page contentType="application/json" pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@taglib prefix="json" uri="http://www.atg.com/taglibs/json"%>

<%@include file="../jspf/database.jspf"%>

<c:choose>
	<c:when test="${param.tipo eq 'sintomas'}">
		<sql:query var="sintomas" dataSource="${CatastrofesServer}">
			SELECT * FROM TIPOS_SINTOMAS
		</sql:query>
	</c:when>
	<c:when test="${param.tipo eq 'sintomasSI'}">
		<sql:query var="sintomasSI" dataSource="${CatastrofesServer}">
			SELECT T.ID, TIPO, DESCRIPCION FROM SINTOMAS S, TIPOS_SINTOMAS T
			WHERE T.ID = ID_SINTOMA AND ID_HERIDO = ?
			AND ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
			<sql:param value="${param.iden}"/>
		</sql:query>
		<sql:query var="sintomas" dataSource="${CatastrofesServer}">
			SELECT * FROM TIPOS_SINTOMAS
			WHERE ID NOT IN (SELECT DISTINCT ID_SINTOMA FROM SINTOMAS
				WHERE ID_HERIDO = ?
				AND ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased'))
			<sql:param value="${param.iden}"/>
		</sql:query>
	</c:when>
</c:choose>

<json:array>
	<c:forEach var="sintomaSI" items="${sintomasSI.rows}">
		<json:object>
			<json:property name="id" value="${sintomaSI.id}"/>
			<json:property name="tipo" value="${sintomaSI.tipo}"/>
			<json:property name="descripcion" value="${sintomaSI.descripcion}"/>
			<json:property name="valor" value="true"/>
		</json:object>
	</c:forEach>
	<c:forEach var="sintoma" items="${sintomas.rows}">
		<json:object>
			<json:property name="id" value="${sintoma.id}"/>
			<json:property name="tipo" value="${sintoma.tipo}"/>
			<json:property name="descripcion" value="${sintoma.descripcion}"/>
			<json:property name="valor" value="false"/>
		</json:object>
	</c:forEach>
</json:array>