<%@page contentType="application/x-sql" pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>

<%@include file="../jspf/database.jspf"%>

<c:catch var="errorUpdate">
	<sql:update dataSource="${CatastrofesServer}">
		INSERT INTO CATASTROFES(MARCADOR, TIPO, CANTIDAD, NOMBRE, DESCRIPCION, INFO, LATITUD,
			LONGITUD, DIRECCION, SIZE, TRAFFIC, PLANTA, ESTADO, IDASSIGNED, FECHA, USUARIO)
		VALUES((SELECT ID FROM TIPOS_MARCADORES WHERE TIPO_MARCADOR = ?),
			(SELECT ID FROM TIPOS_CATASTROFES WHERE TIPO_CATASTROFE = ?), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,
			(SELECT ID FROM TIPOS_ESTADOS WHERE TIPO_ESTADO = ?), ?, ?, ?)
		<sql:param value="${param.marcador}"/>
		<sql:param value="${param.tipo}"/>
		<sql:param value="${param.cantidad}"/>
		<sql:param value="${param.nombre}"/>
		<sql:param value="${param.descripcion}"/>
		<sql:param value="${param.info}"/>
		<sql:param value="${param.latitud}"/>
		<sql:param value="${param.longitud}"/>
		<sql:param value="${param.direccion}"/>
		<sql:param value="${param.size}"/>
		<sql:param value="${param.traffic}"/>
		<sql:param value="${param.planta}"/>
		<sql:param value="${param.estado}"/>
		<sql:param value="${param.idAssigned}"/>
		<sql:param value="${param.fecha}"/>
		<sql:param value="${param.usuario}"/>
	</sql:update>
	
	<%-- Uncomment this code in order to enable Twitter service for new disasters.
		DO NOT FORGET TO INSERT YOUR TWITTER LOGIN AND PASSWORD IN THE TWITTER CONSTRUCTOR --%>
	<%--
		Twitter twitt = new Twitter("your_username_here","your_password_here");
		StreetLocator st = new StreetLocator();
		double lat = Double.parseDouble(request.getParameter("latitud"));
		double lon = Double.parseDouble(request.getParameter("longitud"));
		String msg = "New ";
		msg += request.getParameter("tipo");
		msg += " at ";
		msg +=st.getAddress(lat, lon);
		msg += " on ";
		msg += new Date();
		msg += " Size: " + request.getParameter("size");
		if( msg.length() > 140){
			msg = msg.substring(0, 134) +"(...)";
		}
		twitt.updateStatus(msg);
	--%>
</c:catch>