<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>

<%@ include file="database.jspf" %>

<c:catch var="errorUpdate">
    <sql:update dataSource="${CatastrofesServer}">
		INSERT INTO catastrofes(marcador, tipo, cantidad, nombre, descripcion, info, latitud,
			longitud, direccion, estado, size, traffic, idAssigned, fecha, usuario, planta)
		VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?,
		       (SELECT id FROM tipos_estados WHERE tipo_estado = ?),
			   ?, ?, ?, ?, ?, ?)
		<sql:param value="${param.item}"/>
		<sql:param value="${param.type}"/>
		<sql:param value="${param.quantity}"/>
		<sql:param value="${param.name}"/>
		<sql:param value="${param.description}"/>
		<sql:param value="${param.info}"/>
		<sql:param value="${param.latitud}"/>
		<sql:param value="${param.longitud}"/>
		<sql:param value="${param.address}"/>
		<sql:param value="${param.state}"/>
		<sql:param value="${param.size}"/>
		<sql:param value="${param.traffic}"/>
		<sql:param value="${param.idAssigned}"/>
		<sql:param value="${param.date}"/>
		<sql:param value="${param.user}"/>
		<sql:param value="${param.floor}"/>
	</sql:update>
</c:catch>
<!-- Returns the ID of the las record inserted -->
<sql:query var="eventos" dataSource="${CatastrofesServer}">
	SELECT id FROM catastrofes ORDER BY id DESC LIMIT 1
</sql:query>
<c:forEach var="evento" items="${eventos.rows}">
	${evento.id}
</c:forEach>
