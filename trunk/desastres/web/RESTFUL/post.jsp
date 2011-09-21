<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %> 
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>

<c:set var="databaseEmpotrado">
    <jsp:scriptlet>
		out.print(application.getRealPath("/WEB-INF/db/improvise"));
	</jsp:scriptlet>
</c:set>

<sql:setDataSource var="CatastrofesServer" driver="org.hsqldb.jdbcDriver" 
	url="jdbc:hsqldb:file:${databaseEmpotrado}" user="sa" password=""/>

<c:catch var="errorUpdate">
    <sql:update dataSource="${CatastrofesServer}" sql="INSERT INTO CATASTROFES (
				marcador, tipo, cantidad, nombre, descripcion, info, latitud, longitud, direccion, estado, size, traffic, idAssigned, fecha, usuario, planta)
				VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)">
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
