<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<% String proyecto = getServletContext().getInitParameter("proyect"); %>

<!DOCTYPE HTML>
<html>
	<fmt:bundle basename="fmt.eji8n">
		<head>
			<meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
			<title>Login error - Acceso denegado</title>
			<link type="image/x-icon" rel="shotcut icon" href="images/favicon_<%=proyecto%>.ico"/>
			<link type="text/css" rel="stylesheet" href="css/improvisa_style.css"/>
			<script type="text/javascript" src="js/i18n.js"></script>
			<script type="text/javascript">
				var idioma = '<fmt:message key="idioma"/>';
			</script>
			<script type="text/javascript" src="js/hora_fecha.js"></script>
		</head>
		<body onload="IniciarReloj24()">
			<div>
				<c:import url="jspf/cabecera.jsp"/>
			</div>
			<div>
				<h2>Acceso denegado</h2>
				<p>Nombre de usuario y/o contrase&ntilde;a incorrecta</p>
				<p>Vuelve a intentarlo</p>
				<p>Regresa a la <a href="index.jsp">p&aacute;gina de inicio</a></p>
			</div>
		</body>
	</fmt:bundle>
</html>