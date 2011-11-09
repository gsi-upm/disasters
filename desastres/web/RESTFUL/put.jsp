<%@page contentType="applicacion/x-sql" pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@page import="java.sql.Timestamp, java.util.Date"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>

<%@include file="../jspf/database.jspf"%>
<% String modif = "'" + new Timestamp(new Date().getTime()).toString() + "'"; %>

<c:choose>
	<c:when test="${param.action == 'info'}">
		<c:catch var="errorInsert">
			<sql:update dataSource="${CatastrofesServer}">
				UPDATE catastrofes
				SET info = ?, modificado = <%=modif%>
				WHERE id = ?
				<sql:param value="${param.value}"/>
				<sql:param value="${param.id}"/>
			</sql:update>
		</c:catch>
	</c:when>
	<c:when test="${param.action == 'state'}">
		<c:catch var="errorInsert">
			<sql:update dataSource="${CatastrofesServer}">
				UPDATE catastrofes
				SET estado = (SELECT id FROM tipos_estados WHERE tipo_estado = ?), modificado = <%=modif%>
				WHERE id = ?
				<sql:param value="${param.value}"/>
				<sql:param value="${param.id}"/>
			</sql:update>
		</c:catch>
	</c:when>
	<c:when test="${param.action == 'latitud'}">
		<c:catch var="errorInsert">
			<sql:update dataSource="${CatastrofesServer}">
				UPDATE catastrofes
				SET latitud = ?, modificado = <%=modif%>
				WHERE id = ?
				<sql:param value="${param.value}"/>
				<sql:param value="${param.id}"/>
			</sql:update>
		</c:catch>
	</c:when>
	<c:when test="${param.action == 'longitud'}">
		<c:catch var="errorInsert">
			<sql:update dataSource="${CatastrofesServer}">
				UPDATE catastrofes
				SET longitud = ?, modificado = <%=modif%>
				WHERE id = ?
				<sql:param value="${param.value}"/>
				<sql:param value="${param.id}"/>
			</sql:update>
		</c:catch>
	</c:when>
	<c:when test="${param.action == 'latlong'}">
		<c:catch var="errorInsert">
			<sql:update dataSource="${CatastrofesServer}">
				UPDATE catastrofes
				SET latitud = ?, longitud = ?, modificado = <%=modif%>
				WHERE id = ?
				<sql:param value="${param.latitud}"/>
				<sql:param value="${param.longitud}"/>
				<sql:param value="${param.id}"/>
			</sql:update>
		</c:catch>
	</c:when>
	<c:when test="${param.action == 'quantity'}">
		<c:catch var="errorInsert">
			<sql:update dataSource="${CatastrofesServer}">
				UPDATE catastrofes
				SET cantidad = ?, modificado = <%=modif%>
				WHERE id = ?
				<sql:param value="${param.value}"/>
				<sql:param value="${param.id}"/>
			</sql:update>
		</c:catch>
	</c:when>
	<c:when test="${param.action == 'traffic'}">
		<c:catch var="errorInsert">
			<sql:update dataSource="${CatastrofesServer}">
				UPDATE catastrofes
				SET traffic = ?, modificado = <%=modif%>
				WHERE id = ?
				<sql:param value="${param.value}"/>
				<sql:param value="${param.id}"/>
			</sql:update>
		</c:catch>
	</c:when>
	<c:when test="${param.action == 'size'}">
		<c:catch var="errorInsert">
			<sql:update dataSource="${CatastrofesServer}">
				UPDATE catastrofes
				SET size = ?, modificado = <%=modif%>
				WHERE id = ?
				<sql:param value="${param.value}"/>
				<sql:param value="${param.id}"/>
			</sql:update>
		</c:catch>
	</c:when>
	<c:when test="${param.action == 'idAssigned'}">
		<c:catch var="errorInsert">
			<sql:update dataSource="${CatastrofesServer}">
				UPDATE catastrofes
				SET idAssigned = ?, modificado = <%=modif%>
				WHERE id = ?
				<sql:param value="${param.value}"/>
				<sql:param value="${param.id}"/>
			</sql:update>
		</c:catch>
	</c:when>
	<c:when test="${param.action == 'add'}">
		<c:catch var="errorInsert">
			<sql:query var="eventos" dataSource="${CatastrofesServer}">
				SELECT cantidad FROM catastrofes WHERE id = ?
				<sql:param value="${param.id}"/>
			</sql:query>
			<c:forEach var="evento" items="${eventos.rows}">
				<sql:update dataSource="${CatastrofesServer}">
					UPDATE catastrofes
					SET cantidad = ?, modificado = <%=modif%>
					WHERE id = ?
					<sql:param value="${evento.cantidad+1}"/>
					<sql:param value="${param.id}"/>
				</sql:update>
			</c:forEach>
		</c:catch>
	</c:when>
	<c:when test="${param.action == 'remove'}">
		<c:catch var="errorInsert">
			<sql:query var="eventos" dataSource="${CatastrofesServer}">
				SELECT cantidad FROM catastrofes WHERE id = ?
				<sql:param value="${param.id}"/>
			</sql:query>
			<c:forEach var="evento" items="${eventos.rows}">
				<sql:update dataSource="${CatastrofesServer}">
					UPDATE catastrofes
					SET cantidad = ?, modificado = <%=modif%>
					WHERE id = ?
					<sql:param value="${evento.cantidad-1}"/>
					<sql:param value="${param.id}"/>
				</sql:update>
			</c:forEach>
		</c:catch>
	</c:when>
	<c:when test="${param.action == 'message'}">
		<c:catch var="errorInsert">
			<sql:update dataSource="${CatastrofesServer}">
				INSERT INTO mensajes(creador, mensaje, nivel, fecha)
				VALUES(1, ?, ?, <%=modif%>)
				<sql:param value="${param.mensaje}"/>
				<sql:param value="${param.nivel}"/>
			</sql:update>
		</c:catch>
	</c:when>
</c:choose>
<c:choose>
	<c:when test="${empty errorInsert}">
		OK
	</c:when>
	<c:otherwise>
		<p class="textoError">Error updating: ${errorInsert}</p>
		<p class="textoError">${eventos}, ${eventos.rows}</p>
	</c:otherwise>
</c:choose> 