<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>

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

<sql:query var="lostPeople" dataSource="${CatastrofesServer}" sql="
		SELECT * FROM CATASTROFES WHERE tipo = 'lostPerson' AND estado != 'erased';">
</sql:query>

<sql:query var="injuredPeople" dataSource="${CatastrofesServer}" sql="
		SELECT * FROM CATASTROFES WHERE tipo = 'injuredPerson' AND estado != 'erased';">
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

<sql:query var="nurse" dataSource="${CatastrofesServer}" sql="
		SELECT * FROM CATASTROFES WHERE tipo = 'nurse' AND estado != 'erased';">
</sql:query>

<sql:query var="gerocultor" dataSource="${CatastrofesServer}" sql="
		SELECT * FROM CATASTROFES WHERE tipo = 'gerocultor' AND estado != 'erased';">
</sql:query>

<sql:query var="assistant" dataSource="${CatastrofesServer}" sql="
		SELECT * FROM CATASTROFES WHERE tipo = 'assistant' AND estado != 'erased';">
</sql:query>

<sql:query var="heridos" dataSource="${CatastrofesServer}" sql="
		SELECT * FROM CATASTROFES WHERE marcador = 'people' AND tipo != 'healthy' AND estado != 'erased';">
</sql:query>

<sql:query var="healthy" dataSource="${CatastrofesServer}" sql="
		SELECT * FROM CATASTROFES WHERE tipo = 'healthy' AND estado != 'erased';">
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

<sql:query var="firesResi" dataSource="${CatastrofesServer}" sql="
		SELECT * FROM CATASTROFES WHERE tipo = 'fire' AND estado != 'erased' AND
		latitud > 38.231943 AND	latitud < 38.232634 AND longitud > -1.699622 AND longitud < -1.698201;">
</sql:query>

<sql:query var="floodsResi" dataSource="${CatastrofesServer}" sql="
		SELECT * FROM CATASTROFES WHERE tipo = 'flood' AND estado != 'erased' AND
		latitud > 38.231943 AND	latitud < 38.232634 AND longitud > -1.699622 AND longitud < -1.698201;">
</sql:query>

<sql:query var="collapsesResi" dataSource="${CatastrofesServer}" sql="
		SELECT * FROM CATASTROFES WHERE tipo = 'collapse' AND estado != 'erased' AND
		latitud > 38.231943 AND	latitud < 38.232634 AND longitud > -1.699622 AND longitud < -1.698201;">
</sql:query>

<sql:query var="lostPeopleResi" dataSource="${CatastrofesServer}" sql="
		SELECT * FROM CATASTROFES WHERE tipo = 'lostPerson' AND estado != 'erased' AND
		latitud > 38.231943 AND	latitud < 38.232634 AND longitud > -1.699622 AND longitud < -1.698201;">
</sql:query>

<sql:query var="injuredPeopleResi" dataSource="${CatastrofesServer}" sql="
		SELECT * FROM CATASTROFES WHERE tipo = 'injuredPerson' AND estado != 'erased' AND
		latitud > 38.231943 AND	latitud < 38.232634 AND longitud > -1.699622 AND longitud < -1.698201;">
</sql:query>

<sql:query var="healthyResi" dataSource="${CatastrofesServer}" sql="
		SELECT * FROM CATASTROFES WHERE tipo = 'healthy' AND estado != 'erased' AND
		latitud > 38.231943 AND	latitud < 38.232634 AND longitud > -1.699622 AND longitud < -1.698201;">
</sql:query>

<sql:query var="slightResi" dataSource="${CatastrofesServer}" sql="
		SELECT * FROM CATASTROFES WHERE tipo = 'slight' AND estado != 'erased' AND
		latitud > 38.231943 AND	latitud < 38.232634 AND longitud > -1.699622 AND longitud < -1.698201;">
</sql:query>

<sql:query var="seriousResi" dataSource="${CatastrofesServer}" sql="
		SELECT * FROM CATASTROFES WHERE tipo = 'serious' AND estado != 'erased' AND
		latitud > 38.231943 AND	latitud < 38.232634 AND longitud > -1.699622 AND longitud < -1.698201;">
</sql:query>

<sql:query var="deadResi" dataSource="${CatastrofesServer}" sql="
		SELECT * FROM CATASTROFES WHERE tipo = 'dead' AND estado != 'erased' AND
		latitud > 38.231943 AND	latitud < 38.232634 AND longitud > -1.699622 AND longitud < -1.698201;">
</sql:query>

<sql:query var="trappedResi" dataSource="${CatastrofesServer}" sql="
		SELECT * FROM CATASTROFES WHERE tipo = 'trapped' AND estado != 'erased' AND
		latitud > 38.231943 AND	latitud < 38.232634 AND longitud > -1.699622 AND longitud < -1.698201;">
</sql:query>