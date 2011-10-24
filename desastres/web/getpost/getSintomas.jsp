<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page isELIgnored = "false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %> 
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>

<%@ include file="database.jspf" %> 
<c:choose>
	<c:when test="${param.tipo eq 'todosSintomas'}">
		<sql:query var="sintomas" dataSource="${CatastrofesServer}">
			SELECT * FROM tipos_sintomas
		</sql:query>
	</c:when>
	<c:when test="${param.tipo eq 'sintomasNO'}">
		<sql:query var="sintomas" dataSource="${CatastrofesServer}">
			SELECT * FROM tipos_sintomas
			WHERE id NOT IN (SELECT DISTINCT id_sintoma FROM sintomas
			                 WHERE id_herido = ?
							 AND estado != (SELECT id FROM tipos_estados WHERE tipo = 'erased'))
			<sql:param value="${param.iden}"/>
		</sql:query>
	</c:when>
	<c:when test="${param.tipo eq 'sintomasSI'}">
		<sql:query var="sintomas" dataSource="${CatastrofesServer}">
			SELECT t.id, t.tipo, t.descripcion
			FROM sintomas s, tipos_sintomas t
			WHERE t.id = s.id_sintoma
			AND s.id_herido = ?
			AND s.estado != (SELECT id FROM tipos_estados WHERE tipo = 'erased')
			<sql:param value="${param.iden}"/>
		</sql:query>
	</c:when>
</c:choose>
[
<c:forEach var="sintoma" items="${sintomas.rows}">
    <json:object name="temp">
        <json:property name="id" value="${sintoma.id}"/>
        <json:property name="tipo" value="${sintoma.tipo}"/>
        <json:property name="descripcion" value="${sintoma.descripcion}"/>
	</json:object> ,
</c:forEach>
]