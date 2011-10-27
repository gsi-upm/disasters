<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page isELIgnored = "false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ include file="database.jspf" %>

<sql:query var="eventos" dataSource="${CatastrofesServer}">
	SELECT * FROM catastrofes WHERE marcador = 'event' AND estado != (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased')
</sql:query>
<sql:query var="controlled" dataSource="${CatastrofesServer}">
	SELECT * FROM catastrofes WHERE marcador = 'event' AND estado = (SELECT id FROM tipos_estados WHERE tipo_estado = 'controlled')
</sql:query>
<sql:query var="fires" dataSource="${CatastrofesServer}">
	SELECT * FROM catastrofes WHERE tipo = 'fire' AND estado != (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased')
</sql:query>
<sql:query var="floods" dataSource="${CatastrofesServer}">
	SELECT * FROM catastrofes WHERE tipo = 'flood' AND estado != (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased')
</sql:query>
<sql:query var="collapses" dataSource="${CatastrofesServer}">
	SELECT * FROM catastrofes WHERE tipo = 'collapse' AND estado != (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased')
</sql:query>
<sql:query var="resources" dataSource="${CatastrofesServer}">
	SELECT * FROM catastrofes WHERE marcador = 'resource' AND estado != (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased')
</sql:query>
<sql:query var="policemen" dataSource="${CatastrofesServer}">
	SELECT * FROM catastrofes WHERE tipo = 'police' AND estado != (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased')
</sql:query>
<sql:query var="firemen" dataSource="${CatastrofesServer}">
	SELECT * FROM catastrofes WHERE tipo = 'firemen' AND estado != (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased')
</sql:query>
<sql:query var="ambulance" dataSource="${CatastrofesServer}">
	SELECT * FROM catastrofes WHERE tipo = 'ambulance' AND estado != (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased')
</sql:query>
<sql:query var="heridos" dataSource="${CatastrofesServer}">
	SELECT * FROM catastrofes WHERE marcador = 'people' AND tipo != 'healthy' AND estado != (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased')
</sql:query>
<sql:query var="slight" dataSource="${CatastrofesServer}">
	SELECT * FROM catastrofes WHERE tipo = 'slight' AND estado != (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased')
</sql:query>
<sql:query var="serious" dataSource="${CatastrofesServer}">
	SELECT * FROM catastrofes WHERE tipo = 'serious' AND estado != (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased')
</sql:query>
<sql:query var="dead" dataSource="${CatastrofesServer}">
	SELECT * FROM catastrofes WHERE tipo = 'dead' AND estado != (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased')
</sql:query>
<sql:query var="trapped" dataSource="${CatastrofesServer}">
	SELECT * FROM catastrofes WHERE tipo = 'trapped' AND estado != (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased')
</sql:query>
<fmt:bundle basename="fmt.eji8n">
	<p><fmt:message key="actualmentehay"/> ${eventos.rowCount} <fmt:message key="desastres"/>. (${controlled.rowCount} <fmt:message key="controlados"/>):</p>
	<ul>
		<li><img src="markers/fuego.png" height="25px" alt=""/> ${fires.rowCount} <fmt:message key="incendios"/></li>
		<li><img src="markers/agua.png" height="25px" alt=""/> ${floods.rowCount} <fmt:message key="inundaciones"/></li>
		<li><img src="markers/casa.png" height="30px" alt=""/> ${collapses.rowCount} <fmt:message key="derrumbamientos"/></li>
	</ul>
	<br/>
	<p><fmt:message key="hay"/> ${resources.rowCount} <fmt:message key="recursostrabajando"/></p>
	<ul>
		<li><img src="markers/policia${policemen.rowCount}.png" height="25px" alt=""/> ${policemen.rowCount} <fmt:message key="policias"/></li>
		<li><img src="markers/bombero${firemen.rowCount}.png" height="25px" alt=""/> ${firemen.rowCount} <fmt:message key="bomberos"/></li>
		<li><img src="markers/ambulancia${ambulance.rowCount}.png" height="25px" alt=""/> ${ambulance.rowCount} <fmt:message key="ambulancias"/></li>
	</ul>
	<br/>
	<p><fmt:message key="hay"/> ${heridos.rowCount} <fmt:message key="victimas"/></p>
	<ul>
		<li><img src="markers/leve${slight.rowCount}.png" height="25px" alt=""/> ${slight.rowCount} <fmt:message key="leves"/></li>
		<li><img src="markers/grave${serious.rowCount}.png" height="25px" alt=""/> ${serious.rowCount} <fmt:message key="graves"/></li>
		<li><img src="markers/muerto${dead.rowCount}.png" height="25px" alt=""/> ${dead.rowCount} <fmt:message key="muertos"/></li>
		<li><img src="markers/trapped${trapped.rowCount}.png" height="25px" alt=""/> ${trapped.rowCount} <fmt:message key="atrapados"/></li>
	</ul>
	<br/>
	<br/>
	<span class="pulsable"><fmt:message key="ocultar"/></span>
</fmt:bundle>