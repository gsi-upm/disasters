<%@page contentType="applicacion/x-sql" pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@taglib prefix="json" uri="http://www.atg.com/taglibs/json"%>

<%@include file="../jspf/database.jspf"%>

<c:set var="number">
	<%
		String quantity = request.getParameter("quantity");
		if(quantity == null || quantity.equals("0")){
			quantity = "1";
		}
		Integer number;
		try{
			number = Integer.parseInt(quantity);
		}catch(NumberFormatException ex){
			number = 1;
		}
		out.print(number);
	%>
</c:set>
<c:set var="idAssigned">
	<%
		String assignment = request.getParameter("idAssigned");
		if(assignment == null){
			assignment = "0";
		}
		Integer id;
		try{
			id = Integer.parseInt(assignment);
		}catch(NumberFormatException ex){
			id = 0;
		}
		out.print(id);
	%>
</c:set>
<c:set var="floor">
	<%
		String planta = request.getParameter("floor");
		if(planta == null){
			planta = "-2";
		}
		Integer floor;
		try{
			floor = Integer.parseInt(planta);
		}catch(NumberFormatException ex){
			floor = -2;
		}
		out.print(floor);
	%>
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
	<c:when test="${param.type == null}">
		<jsp:forward page="error.jsp">
			<jsp:param name="action" value="parameter"/>
			<jsp:param name="parameter" value="type"/>
			<jsp:param name="value" value="Empty type!!"/>
		</jsp:forward>
	</c:when>
	<c:otherwise>
		<c:choose>
			<c:when test="${param.type == 'fire' || param.type == 'flood' || param.type == 'collapse' ||
					param.type == 'lostPerson' || param.type == 'injuredPerson'}">
				<c:set var="item" value="event"/>
			</c:when>
			<c:when test="${param.type == 'police' || param.type == 'firemen' || param.type == 'ambulance' ||
					param.type=='ambulancia'|| param.type=='nurse' || param.type == 'gerocultor' ||
					param.type == 'assistant' || param.type == 'otherStaff' || param.type == 'citizen'}">
				<c:set var="item" value="resource"/>
			</c:when>
			<c:when test="${param.type == 'slight' || param.type == 'serious' || param.type == 'dead'||
					param.type == 'trapped' || param.type == 'healthy'}">
				<c:set var="item" value="people"/>
			</c:when>
			<c:when test="${param.type == 'user'}">
				<c:set var="item" value="user"/>
			</c:when>
			<c:otherwise>
				<jsp:forward page="error.jsp">
					<jsp:param name="action" value="parameter"/>
					<jsp:param name="parameter" value="type"/>
					<jsp:param name="value" value="${param.type}"/>
				</jsp:forward>
			</c:otherwise>
		</c:choose>
	</c:otherwise>
</c:choose>
<c:catch var="errorUpdate">
	<sql:update dataSource="${CatastrofesServer}">
		INSERT INTO CATASTROFES(MARCADOR, TIPO, CANTIDAD, NOMBRE, DESCRIPCION, INFO, LATITUD,
			LONGITUD, DIRECCION, SIZE, TRAFFIC, PLANTA, ESTADO, IDASSIGNED, FECHA, USUARIO)
		VALUES((SELECT ID FROM TIPOS_MARCADORES WHERE TIPO_MARCADOR = ?),
			(SELECT ID FROM TIPOS_CATASTROFES WHERE TIPO_CATASTROFE = ?), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,
			(SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = ?), ?, ?, ?)
		<sql:param value="${item}"/>
		<sql:param value="${param.type}"/>
		<sql:param value="${number}"/>
		<sql:param value="${param.name}"/>
		<sql:param value="${param.description}"/>
		<sql:param value="${param.info}"/>
		<sql:param value="${param.latitud}"/>
		<sql:param value="${param.longitud}"/>
		<sql:param value="${param.address}"/>
		<sql:param value="${param.size}"/>
		<sql:param value="${param.traffic}"/>
		<sql:param value="${floor}"/>
		<sql:param value="${param.state}"/>
		<sql:param value="${idAssigned}"/>
		<sql:param value="${param.date}"/>
		<sql:param value="${param.user}"/>
	</sql:update>
</c:catch>
<sql:query var="eventos" dataSource="${CatastrofesServer}">
	SELECT ID FROM CATASTROFES ORDER BY ID DESC LIMIT 1
</sql:query>
<c:forEach var="evento" items="${eventos.rows}">
	<json:object name="evento">
		<json:property name="id" value="${evento.id}"/>
	</json:object>
</c:forEach>