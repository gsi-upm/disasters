<%@page contentType="application/json" pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@taglib prefix="json" uri="http://www.atg.com/taglibs/json"%>

<%@include file="../jspf/database.jspf"%>

<c:set var="planta" value="${param.planta}"/>
<c:if test="${param.marcadores == 'plano'}">
	<c:choose>
		<c:when test="${planta != -2}">
			<sql:query var="fire" dataSource="${CatastrofesServer}">
				SELECT ID FROM CATASTROFES
				WHERE TIPO = (SELECT ID FROM TIPOS_CATASTROFES WHERE TIPO_CATASTROFE = 'fire')
				AND ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
				AND PLANTA = ?
				<sql:param value="${planta}"/>
			</sql:query>
			<sql:query var="flood" dataSource="${CatastrofesServer}">
				SELECT ID FROM CATASTROFES
				WHERE TIPO = (SELECT ID FROM TIPOS_CATASTROFES WHERE TIPO_CATASTROFE = 'flood')
				AND ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
				AND PLANTA = ?
				<sql:param value="${planta}"/>
			</sql:query>
			<sql:query var="collapse" dataSource="${CatastrofesServer}">
				SELECT ID FROM CATASTROFES
				WHERE TIPO = (SELECT ID FROM TIPOS_CATASTROFES WHERE TIPO_CATASTROFE = 'collapse')
				AND ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
				AND PLANTA = ?
				<sql:param value="${planta}"/>
			</sql:query>
			<sql:query var="lostPerson" dataSource="${CatastrofesServer}">
				SELECT ID FROM CATASTROFES
				WHERE TIPO = (SELECT ID FROM TIPOS_CATASTROFES WHERE TIPO_CATASTROFE = 'lostPerson')
				AND ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
				AND PLANTA = ?
				<sql:param value="${planta}"/>
			</sql:query>
			<sql:query var="healthy" dataSource="${CatastrofesServer}">
				SELECT ID FROM CATASTROFES
				WHERE TIPO = (SELECT ID FROM TIPOS_CATASTROFES WHERE TIPO_CATASTROFE = 'healthy')
				AND ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
				AND PLANTA = ?
				<sql:param value="${planta}"/>
			</sql:query>
			<sql:query var="slight" dataSource="${CatastrofesServer}">
				SELECT ID FROM CATASTROFES
				WHERE TIPO = (SELECT ID FROM TIPOS_CATASTROFES WHERE TIPO_CATASTROFE = 'slight')
				AND ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
				AND PLANTA = ?
				<sql:param value="${planta}"/>
			</sql:query>
			<sql:query var="serious" dataSource="${CatastrofesServer}">
				SELECT ID FROM CATASTROFES
				WHERE TIPO = (SELECT ID FROM TIPOS_CATASTROFES WHERE TIPO_CATASTROFE = 'serious')
				AND ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
				AND PLANTA = ?
				<sql:param value="${planta}"/>
			</sql:query>
			<sql:query var="dead" dataSource="${CatastrofesServer}">
				SELECT ID FROM CATASTROFES
				WHERE TIPO = (SELECT ID FROM TIPOS_CATASTROFES WHERE TIPO_CATASTROFE = 'dead')
				AND ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
				AND PLANTA = ?
				<sql:param value="${planta}"/>
			</sql:query>
			<sql:query var="trapped" dataSource="${CatastrofesServer}">
				SELECT ID FROM CATASTROFES
				WHERE TIPO = (SELECT ID FROM TIPOS_CATASTROFES WHERE TIPO_CATASTROFE = 'trapped')
				AND ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
				AND PLANTA = ?
				<sql:param value="${planta}"/>
			</sql:query>
		</c:when>
		<c:otherwise>
			<sql:query var="fire" dataSource="${CatastrofesServer}">
				SELECT ID FROM CATASTROFES
				WHERE TIPO = (SELECT ID FROM TIPOS_CATASTROFES WHERE TIPO_CATASTROFE = 'fire')
				AND ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
			</sql:query>
			<sql:query var="flood" dataSource="${CatastrofesServer}">
				SELECT ID FROM CATASTROFES
				WHERE TIPO = (SELECT ID FROM TIPOS_CATASTROFES WHERE TIPO_CATASTROFE = 'flood')
				AND ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
			</sql:query>
			<sql:query var="collapse" dataSource="${CatastrofesServer}">
				SELECT ID FROM CATASTROFES
				WHERE TIPO = (SELECT ID FROM TIPOS_CATASTROFES WHERE TIPO_CATASTROFE = 'collapse')
				AND ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
			</sql:query>
			<sql:query var="lostPerson" dataSource="${CatastrofesServer}">
				SELECT ID FROM CATASTROFES
				WHERE TIPO = (SELECT ID FROM TIPOS_CATASTROFES WHERE TIPO_CATASTROFE = 'lostPerson')
				AND ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
			</sql:query>
			<sql:query var="healthy" dataSource="${CatastrofesServer}">
				SELECT ID FROM CATASTROFES
				WHERE TIPO = (SELECT ID FROM TIPOS_CATASTROFES WHERE TIPO_CATASTROFE = 'healthy')
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
		</c:otherwise>
	</c:choose>
</c:if>
<c:if test="${param.marcadores == 'lateral'}">
	<sql:query var="nurse" dataSource="${CatastrofesServer}">
		SELECT ID FROM CATASTROFES
		WHERE TIPO = (SELECT ID FROM TIPOS_CATASTROFES WHERE TIPO_CATASTROFE = 'nurse')
		AND ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
	</sql:query>
	<sql:query var="gerocultor" dataSource="${CatastrofesServer}">
		SELECT ID FROM CATASTROFES
		WHERE TIPO = (SELECT ID FROM TIPOS_CATASTROFES WHERE TIPO_CATASTROFE = 'gerocultor')
		AND ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
	</sql:query>
	<sql:query var="assistant" dataSource="${CatastrofesServer}">
		SELECT ID FROM CATASTROFES
		WHERE TIPO = (SELECT ID FROM TIPOS_CATASTROFES WHERE TIPO_CATASTROFE = 'assistant')
		AND ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
	</sql:query>
	<sql:query var="otherStaff" dataSource="${CatastrofesServer}">
		SELECT ID FROM CATASTROFES
		WHERE TIPO = (SELECT ID FROM TIPOS_CATASTROFES WHERE TIPO_CATASTROFE = 'otherStaff')
		AND ESTADO != (SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = 'erased')
	</sql:query>
	<sql:query var="police" dataSource="${CatastrofesServer}">
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
</c:if>

<json:object name="info">
	<json:property name="fire" value="${fire.rowCount}"/>
	<json:property name="flood" value="${flood.rowCount}"/>
	<json:property name="collapse" value="${collapse.rowCount}"/>
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
</json:object>