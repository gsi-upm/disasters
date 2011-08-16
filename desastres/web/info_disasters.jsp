<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ include file="database.jspf" %>
	<sql:query var="eventos" dataSource="${CatastrofesServer}" sql="
				SELECT * FROM CATASTROFES WHERE marcador = 'event' AND estado != 'erased';">
	</sql:query>

	<sql:query var="controlled" dataSource="${CatastrofesServer}" sql="
		SELECT * FROM CATASTROFES WHERE marcador = 'event' AND estado = 'controlled';">
	</sql:query>

	<sql:query var="fires" dataSource="${CatastrofesServer}" sql="
		SELECT * FROM CATASTROFES WHERE tipo = 'fire' AND estado != 'erased';">
	</sql:query>

	<sql:query var="floods" dataSource="${CatastrofesServer}" sql="
		SELECT * FROM CATASTROFES WHERE tipo = 'flood' AND estado != 'erased';">
	</sql:query>

	<sql:query var="collapses" dataSource="${CatastrofesServer}" sql="
		SELECT * FROM CATASTROFES WHERE tipo = 'collapse' AND estado != 'erased';">
	</sql:query>

	<sql:query var="resources" dataSource="${CatastrofesServer}" sql="
		SELECT * FROM CATASTROFES WHERE marcador = 'resource' AND estado != 'erased';">
	</sql:query>

	<sql:query var="policemen" dataSource="${CatastrofesServer}" sql="
		SELECT * FROM CATASTROFES WHERE tipo = 'police' AND estado != 'erased';">
	</sql:query>

	<sql:query var="firemen" dataSource="${CatastrofesServer}" sql="
		SELECT * FROM CATASTROFES WHERE tipo = 'firemen' AND estado != 'erased';">
	</sql:query>

	<sql:query var="ambulance" dataSource="${CatastrofesServer}" sql="
		SELECT * FROM CATASTROFES WHERE tipo = 'ambulance' AND estado != 'erased';">
	</sql:query>

	<sql:query var="heridos" dataSource="${CatastrofesServer}" sql="
		SELECT * FROM CATASTROFES WHERE marcador = 'people' AND tipo != 'healthy' AND estado != 'erased';">
	</sql:query>

	<sql:query var="slight" dataSource="${CatastrofesServer}" sql="
		SELECT * FROM CATASTROFES WHERE tipo = 'slight' AND estado != 'erased';">
	</sql:query>

	<sql:query var="serious" dataSource="${CatastrofesServer}" sql="
		SELECT * FROM CATASTROFES WHERE tipo = 'serious' AND estado != 'erased';">
	</sql:query>

	<sql:query var="dead" dataSource="${CatastrofesServer}" sql="
		SELECT * FROM CATASTROFES WHERE tipo = 'dead' AND estado != 'erased';">
	</sql:query>

	<sql:query var="trapped" dataSource="${CatastrofesServer}" sql="
		SELECT * FROM CATASTROFES WHERE tipo = 'trapped' AND estado != 'erased';">
	</sql:query>

<fmt:bundle basename="fmt.eji8n">
	<p><fmt:message key="actualmentehay"/> ${eventos.rowCount} <fmt:message key="desastres"/>. (${controlled.rowCount} <fmt:message key="controlados"/>):</p>
	<ul>
		<li><img src="markers/fuego.png" height="25px" alt=""/> ${fires.rowCount} <fmt:message key="incendios"/> </li>
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
		<li><img src="markers/leve${slight.rowCount}.png" height="25px" alt=""/> ${slight.rowCount} Leves</li>
		<li><img src="markers/grave${serious.rowCount}.png" height="25px" alt=""/> ${serious.rowCount} Graves</li>
		<li><img src="markers/muerto${dead.rowCount}.png" height="25px" alt=""/> ${dead.rowCount} Muertos</li>
		<li><img src="markers/trapped${trapped.rowCount}.png" height="25px" alt=""/> ${dead.rowCount} Atrapados</li>
	</ul>
	<br/>
	<br/>
	<span class="pulsable"><fmt:message key="ocultar"/></span>
</fmt:bundle>