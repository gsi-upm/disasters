<%@page contentType="application/json"%>
<%@page pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%> 
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json"%>

<c:set var="number">
	<jsp:scriptlet>
		String quantity = request.getParameter("quantity");
		if (quantity == null || quantity.equals("0")) {
			quantity = "1";
		}
		Integer number;
		try {
			number = Integer.parseInt(quantity);
		} catch (Exception e) {
			number = 1;
		}
		out.print(number);
	</jsp:scriptlet>
</c:set>

<c:set var="idAssigned">
	<jsp:scriptlet>
		String assignment = request.getParameter("idAssigned");
		if (assignment == null) {
			assignment = "0";
		}
		Integer id;
		try {
			id = Integer.parseInt(assignment);
		} catch (Exception e) {
			id = 1;
		}
		out.print(id);
	</jsp:scriptlet>
</c:set>

<c:choose>
	<c:when test="${param.latitud == null || param.longitud == null}">
		<jsp:forward page="error.jsp">
			<jsp:param name="action" value="parameter"/>
			<jsp:param name="parameter" value="type"/>
			<jsp:param name="value" value="null latitude or longitude"/>
		</jsp:forward>
	</c:when>
</c:choose>

<c:choose>
	<c:when test="${param.type==null}">
		<jsp:forward page="error.jsp">
			<jsp:param name="action" value="parameter"/>
			<jsp:param name="parameter" value="type"/>
			<jsp:param name="value" value="Empty type!!"/>
		</jsp:forward>
	</c:when>
	<c:otherwise>
		<c:set var="item">
			<c:choose>
				<c:when test="${param.type=='fire'||param.type=='flood'||param.type=='collapse'||param.type=='lostPerson'||param.type=='injuredPerson'}">event</c:when>
				<c:when test="${param.type=='police'||param.type=='firemen'||param.type=='ambulance'||param.type=='ambulancia'||
								param.type=='nurse'||param.type=='gerocultor'||param.type=='assistant'||param.type=='otherStaff'||param.type=='citizen'}">resource</c:when>
				<c:when test="${param.type=='slight'||param.type=='serious'||param.type=='dead'||param.type=='trapped'||param.type=='healthy'}">people</c:when>
				<c:when test="${param.type=='user'}">user</c:when>
				<c:otherwise>
					<jsp:forward page="error.jsp">
						<jsp:param name="action" value="parameter"/>
						<jsp:param name="parameter" value="type"/>
						<jsp:param name="value" value="${param.type}"/>
					</jsp:forward>
				</c:otherwise>
			</c:choose>
		</c:set>
	</c:otherwise>
</c:choose>

<%@ include file="../database.jspf" %>

<c:catch var="errorUpdate">
    <sql:update dataSource="${CatastrofesServer}" sql="INSERT INTO CATASTROFES (
				marcador, tipo, cantidad, nombre, descripcion, info, latitud, longitud, direccion, estado, size, traffic, idAssigned, fecha, usuario, planta)
				VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)">
		<sql:param value="${item}"/>
		<sql:param value="${param.type}"/>
		<sql:param value="${number}"/>
		<sql:param value="${param.name}"/>
		<sql:param value="${param.description}"/>
		<sql:param value="${param.info}"/>
		<sql:param value="${param.latitud}"/>
		<sql:param value="${param.longitud}"/>
		<sql:param value="${param.address}"/>
		<sql:param value="${param.state}"/>
		<sql:param value="${param.size}"/>
		<sql:param value="${param.traffic}"/>
		<sql:param value="${idAssigned}"/>
		<sql:param value="${param.date}"/>
		<sql:param value="${param.user}"/>
		<sql:param value="${param.floor}"/>
	</sql:update>
</c:catch>

<sql:query var="eventos" dataSource="${CatastrofesServer}">
	SELECT id FROM catastrofes ORDER BY id DESC LIMIT 1
</sql:query>

<c:forEach var="evento" items="${eventos.rows}">
	<json:object name="evento">
		<json:property name="id" value="${evento.id}"/>
	</json:object>
</c:forEach>
