<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE HTML>
<html>
	<fmt:bundle basename="fmt.eji8n">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
			<title><fmt:message key="acerca"/></title>
			<link type="image/vnd.microsoft.icon" rel="icon" href="images/favicon_caronte.ico"/>
			<link type="text/css" rel="stylesheet" href="css/improvisa_style.css"/>
			<script type="application/javascript" src="js/i18n.js"></script>
			<script type="application/javascript">
				var idioma = '<fmt:message key="idioma"/>';
			</script>
			<script type="application/javascript" src="js/hora_fecha.js"></script>
		</head>
		<body onload="IniciarReloj24()">
			<div>
				<c:import url="jspf/cabecera.jsp"/>
			</div>
			<h1><fmt:message key="informacion"/></h1>
			<table>
				<tr>
					<td><fmt:message key="director"/>:</td>
					<td>Nombre Apellido1 Apellido2 - 968723080</td>
				</tr>
				<tr>
					<td><fmt:message key="altura"/>:</td>
					<td>xxx metros</td>
				</tr>
				<tr>
					<td><fmt:message key="direccion"/>:</td>
					<td>Calle Molinico 7</td>
				</tr>
			</table>
			<p>
				<c:url value="index.jsp" var="volver"/>
				<a href="${volver}"><fmt:message key="volver"/></a>
			</p>
		</body>
	</fmt:bundle>
</html>