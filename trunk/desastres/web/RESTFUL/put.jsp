<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.Date" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>

<%@ include file="database.jspf" %>
<% String modif = "'" + new Timestamp(new java.util.Date().getTime()).toString() + "'"; %>

<c:choose>
	<c:when test="${param.action eq 'info'}">
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
	<c:when test="${param.action eq 'state'}">
		<c:catch var="errorInsert">
			<sql:update dataSource="${CatastrofesServer}">
				UPDATE catastrofes
				SET estado = ?, modificado = <%=modif%>
				WHERE id = ?
				<sql:param value="${param.value}"/>
				<sql:param value="${param.id}"/>
			</sql:update>
		</c:catch>
	</c:when>
	<c:when test="${param.action eq 'latitud'}">
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
	<c:when test="${param.action eq 'longitud'}">
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
	<c:when test="${param.action eq 'latlong'}">
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
	<c:when test="${param.action eq 'quantity'}">
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
	<c:when test="${param.action eq 'traffic'}">
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
	<c:when test="${param.action eq 'size'}">
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
	<c:when test="${param.action eq 'idAssigned'}">
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
	<c:when test="${param.action eq 'add'}">
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
	<c:when test="${param.action eq 'remove'}">
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
	<c:when test="${param.action eq 'message'}">
		<c:catch var="errorInsert">
			<sql:update dataSource="${CatastrofesServer}">
				INSERT INTO mensajes(mensaje, nivel, fecha)
				VALUES(?,?,<%=modif%>)
				<sql:param value="${param.mensaje}"/>
				<sql:param value="${param.nivel}"/>
			</sql:update>
		</c:catch>
	</c:when>
	<c:when test="${param.action eq 'registrar'}">
		<c:catch var="errorInsert">
			<sql:update dataSource="${CatastrofesServer}">
				INSERT INTO usuarios(nombre_usuario, password, tipo_usuario, nombre_real,
					correo, latitud, longitud, localizacion, proyecto)
				VALUES(?, ?, 11, ?, ?, 0.0, 0.0, FALSE, 'caronte')
				<sql:param value="${param.user}"/>
				<sql:param value="${param.pass}"/>
				<sql:param value="${param.nombre}"/>
				<sql:param value="${param.email}"/>
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