<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page isELIgnored = "false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>

<%@ include file="database.jspf" %>

<c:set var="planta" value="${param.planta}"/>
<c:if test="${param.marcadores == 'plano'}">
	<c:choose>
		<c:when test="${planta != -2}">
			<sql:query var="fire" dataSource="${CatastrofesServer}">
				SELECT * FROM catastrofes WHERE tipo = 'fire' AND planta = ? AND estado != (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased')
				<sql:param value="${planta}"/>
			</sql:query>
			<sql:query var="flood" dataSource="${CatastrofesServer}">
				SELECT * FROM catastrofes WHERE tipo = 'flood' AND planta = ? AND estado != (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased')
				<sql:param value="${planta}"/>
			</sql:query>
			<sql:query var="collapse" dataSource="${CatastrofesServer}">
				SELECT * FROM catastrofes WHERE tipo = 'collapse' AND planta = ? AND estado != (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased')
				<sql:param value="${planta}"/>
			</sql:query>
			<sql:query var="lostPerson" dataSource="${CatastrofesServer}">
				SELECT * FROM catastrofes WHERE tipo = 'lostPerson' AND planta = ? AND estado != (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased')
				<sql:param value="${planta}"/>
			</sql:query>
			<sql:query var="injuredPerson" dataSource="${CatastrofesServer}">
				SELECT * FROM catastrofes WHERE tipo = 'injuredPerson' AND planta = ? AND estado != (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased')
				<sql:param value="${planta}"/>
			</sql:query>
			<sql:query var="healthy" dataSource="${CatastrofesServer}">
				SELECT * FROM catastrofes WHERE tipo = 'healthy' AND planta = ? AND estado != (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased')
				<sql:param value="${planta}"/>
			</sql:query>
			<sql:query var="slight" dataSource="${CatastrofesServer}">
				SELECT * FROM catastrofes WHERE tipo = 'slight' AND planta = ? AND estado != (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased')
				<sql:param value="${planta}"/>
			</sql:query>
			<sql:query var="serious" dataSource="${CatastrofesServer}">
				SELECT * FROM catastrofes WHERE tipo = 'serious' AND planta = ? AND estado != (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased')
				<sql:param value="${planta}"/>
			</sql:query>
			<sql:query var="dead" dataSource="${CatastrofesServer}">
				SELECT * FROM catastrofes WHERE tipo = 'dead' AND planta = ? AND estado != (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased')
				<sql:param value="${planta}"/>
			</sql:query>
			<sql:query var="trapped" dataSource="${CatastrofesServer}">
				SELECT * FROM catastrofes WHERE tipo = 'trapped' AND planta = ? AND estado != (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased')
				<sql:param value="${planta}"/>
			</sql:query>
		</c:when>
		<c:otherwise>
			<sql:query var="fire" dataSource="${CatastrofesServer}">
				SELECT * FROM catastrofes WHERE tipo = 'fire' AND estado != (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased')
			</sql:query>
			<sql:query var="flood" dataSource="${CatastrofesServer}">
				SELECT * FROM catastrofes WHERE tipo = 'flood' AND estado != (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased')
			</sql:query>
			<sql:query var="collapse" dataSource="${CatastrofesServer}">
				SELECT * FROM catastrofes WHERE tipo = 'collapse' AND estado != (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased')
			</sql:query>
			<sql:query var="lostPerson" dataSource="${CatastrofesServer}">
				SELECT * FROM catastrofes WHERE tipo = 'lostPerson' AND estado != (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased')
			</sql:query>
			<sql:query var="injuredPerson" dataSource="${CatastrofesServer}">
				SELECT * FROM catastrofes WHERE tipo = 'injuredPerson' AND estado != (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased')
			</sql:query>
			<sql:query var="healthy" dataSource="${CatastrofesServer}">
				SELECT * FROM catastrofes WHERE tipo = 'healthy' AND estado != (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased')
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
		</c:otherwise>
	</c:choose>
</c:if>
<c:if test="${param.marcadores == 'lateral'}">
	<sql:query var="nurse" dataSource="${CatastrofesServer}">
		SELECT * FROM catastrofes WHERE tipo = 'nurse' AND estado != (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased');
	</sql:query>
	<sql:query var="gerocultor" dataSource="${CatastrofesServer}">
		SELECT * FROM catastrofes WHERE tipo = 'gerocultor' AND estado != (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased');
	</sql:query>
	<sql:query var="assistant" dataSource="${CatastrofesServer}">
		SELECT * FROM catastrofes WHERE tipo = 'assistant' AND estado != (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased');
	</sql:query>
	<sql:query var="otherStaff" dataSource="${CatastrofesServer}">
		SELECT * FROM catastrofes WHERE tipo = 'otherStaff' AND estado != (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased');
	</sql:query>
	<sql:query var="police" dataSource="${CatastrofesServer}">
		SELECT * FROM catastrofes WHERE tipo = 'police' AND estado != (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased');
	</sql:query>
	<sql:query var="firemen" dataSource="${CatastrofesServer}">
		SELECT * FROM catastrofes WHERE tipo = 'firemen' AND estado != (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased');
	</sql:query>
	<sql:query var="ambulance" dataSource="${CatastrofesServer}">
		SELECT * FROM catastrofes WHERE tipo = 'ambulance' AND estado != (SELECT id FROM tipos_estados WHERE tipo_estado = 'erased');
	</sql:query>
</c:if>
[
<json:object name="info">
	<json:property name="fire" value="${fire.rowCount}"/>
	<json:property name="flood" value="${flood.rowCount}"/>
	<json:property name="collapse" value="${collapse.rowCount}"/>
	<json:property name="injuredPerson" value="${injuredPerson.rowCount}"/>
	<json:property name="lostPerson" value="${lostPerson.rowCount}"/>
	<json:property name="healthy" value="${healthy.rowCount}"/>
	<json:property name="slight" value="${slight.rowCount}"/>
	<json:property name="serious" value="${serious.rowCount}"/>
	<json:property name="dead" value="${dead.rowCount}"/>
	<json:property name="trapped" value="${trapped.rowCount}"/>
	<json:property name="nurse" value="${nurse.rowCount}"/>
	<json:property name="gerocultor" value="${gerocultor.rowCount}"/>
	<json:property name="assistant" value="${assistant.rowCount}"/>
	<json:property name="otherStaff" value="${otherStaff.rowCount}"/>
	<json:property name="police" value="${police.rowCount}"/>
	<json:property name="firemen" value="${firemen.rowCount}"/>
	<json:property name="ambulance" value="${ambulance.rowCount}"/>
</json:object> ,
]