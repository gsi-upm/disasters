<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@include file="../jspf/database.jspf"%>

<sql:query var="eventos" dataSource="${CatastrofesServer}">
	SELECT ID FROM CATASTROFES
	WHERE MARCADOR = (SELECT ID FROM TIPOS_MARCADORES WHERE TIPO_MARCADOR = 'event')
	AND ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
</sql:query>
<sql:query var="controlled" dataSource="${CatastrofesServer}">
	SELECT ID FROM CATASTROFES
	WHERE MARCADOR = (SELECT ID FROM TIPOS_MARCADORES WHERE TIPO_MARCADOR = 'event')
	AND ESTADO = (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'controlled')
</sql:query>
<sql:query var="fires" dataSource="${CatastrofesServer}">
	SELECT ID FROM CATASTROFES
	WHERE TIPO = (SELECT ID FROM TIPOS_CATASTROFES WHERE TIPO_CATASTROFE = 'fire')
	AND ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
</sql:query>
<sql:query var="floods" dataSource="${CatastrofesServer}">
	SELECT ID FROM CATASTROFES
	WHERE TIPO = (SELECT ID FROM TIPOS_CATASTROFES WHERE TIPO_CATASTROFE = 'flood')
	AND ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
</sql:query>
<sql:query var="collapses" dataSource="${CatastrofesServer}">
	SELECT ID FROM CATASTROFES
	WHERE TIPO = (SELECT ID FROM TIPOS_CATASTROFES WHERE TIPO_CATASTROFE = 'collapse')
	AND ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
</sql:query>
<sql:query var="resources" dataSource="${CatastrofesServer}">
	SELECT ID FROM CATASTROFES
	WHERE MARCADOR = (SELECT ID FROM TIPOS_MARCADORES WHERE TIPO_MARCADOR = 'resource')
	AND ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
</sql:query>
<sql:query var="policemen" dataSource="${CatastrofesServer}">
	SELECT ID FROM CATASTROFES
	WHERE TIPO = (SELECT ID FROM TIPOS_CATASTROFES WHERE TIPO_CATASTROFE = 'police')
	AND ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
</sql:query>
<sql:query var="firemen" dataSource="${CatastrofesServer}">
	SELECT ID FROM CATASTROFES
	WHERE TIPO = (SELECT ID FROM TIPOS_CATASTROFES WHERE TIPO_CATASTROFE = 'firemen')
	AND ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
</sql:query>
<sql:query var="ambulance" dataSource="${CatastrofesServer}">
	SELECT ID FROM CATASTROFES
	WHERE TIPO = (SELECT ID FROM TIPOS_CATASTROFES WHERE TIPO_CATASTROFE = 'ambulance')
	AND ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
</sql:query>
<sql:query var="slight" dataSource="${CatastrofesServer}">
	SELECT ID FROM CATASTROFES
	WHERE TIPO = (SELECT ID FROM TIPOS_CATASTROFES WHERE TIPO_CATASTROFE = 'slight')
	AND ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
</sql:query>
<sql:query var="serious" dataSource="${CatastrofesServer}">
	SELECT ID FROM CATASTROFES
	WHERE TIPO = (SELECT ID FROM TIPOS_CATASTROFES WHERE TIPO_CATASTROFE = 'serious')
	AND ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
</sql:query>
<sql:query var="dead" dataSource="${CatastrofesServer}">
	SELECT ID FROM CATASTROFES
	WHERE TIPO = (SELECT ID FROM TIPOS_CATASTROFES WHERE TIPO_CATASTROFE = 'dead')
	AND ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
</sql:query>
<sql:query var="trapped" dataSource="${CatastrofesServer}">
	SELECT ID FROM CATASTROFES
	WHERE TIPO = (SELECT ID FROM TIPOS_CATASTROFES WHERE TIPO_CATASTROFE = 'trapped')
	AND ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
</sql:query>
<fmt:bundle basename="fmt.eji8n">
	<p><fmt:message key="actualmentehay"/> ${eventos.rowCount} <fmt:message key="desastres"/>. (${controlled.rowCount} <fmt:message key="controlados"/>):</p>
	<ul>
		<li><img src="markers/events/fuego.png" height="25px" alt=""/> ${fires.rowCount} <fmt:message key="incendios"/></li>
		<li><img src="markers/events/agua.png" height="25px" alt=""/> ${floods.rowCount} <fmt:message key="inundaciones"/></li>
		<li><img src="markers/events/casa.png" height="30px" alt=""/> ${collapses.rowCount} <fmt:message key="derrumbamientos"/></li>
	</ul>
	<p><fmt:message key="hay"/> ${resources.rowCount} <fmt:message key="recursostrabajando"/></p>
	<ul>
		<li><img src="markers/resources/policia${policemen.rowCount}.png" height="25px" alt=""/> ${policemen.rowCount} <fmt:message key="policias"/></li>
		<li><img src="markers/resources/bombero${firemen.rowCount}.png" height="25px" alt=""/> ${firemen.rowCount} <fmt:message key="bomberos"/></li>
		<li><img src="markers/resources/ambulancia${ambulance.rowCount}.png" height="25px" alt=""/> ${ambulance.rowCount} <fmt:message key="ambulancias"/></li>
	</ul>
	<p><fmt:message key="hay"/> ${heridos.rowCount} <fmt:message key="victimas"/></p>
	<ul>
		<li><img src="markers/people/leve${slight.rowCount}.png" height="25px" alt=""/> ${slight.rowCount} <fmt:message key="leves"/></li>
		<li><img src="markers/people/grave${serious.rowCount}.png" height="25px" alt=""/> ${serious.rowCount} <fmt:message key="graves"/></li>
		<li><img src="markers/people/muerto${dead.rowCount}.png" height="25px" alt=""/> ${dead.rowCount} <fmt:message key="muertos"/></li>
		<li><img src="markers/people/trapped${trapped.rowCount}.png" height="25px" alt=""/> ${trapped.rowCount} <fmt:message key="atrapados"/></li>
	</ul>
	<span ID="hideConsole" class="pulsable" onclick="$('#console').slideUp()"><fmt:message key="ocultar"/></span>
</fmt:bundle>