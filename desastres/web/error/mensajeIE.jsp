<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean class="gsi.proyect.ProyectBean" id="proyecto" scope="session"/>

<!DOCTYPE HTML>
<html>
	<fmt:bundle basename="fmt.eji8n">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
			<title>Error Internet Explorer</title>
			<link type="image/vnd.microsoft.icon" rel="icon" href="images/favicon_${proyecto.proyect}.ico"/>
			<link type="text/css" rel="stylesheet" href="css/improvisa_style.css"/>
			<script type="text/javascript" src="js/i18n.js"></script>
			<script type="text/javascript">var idioma = '<fmt:message key="idioma"/>'</script>
			<script type="text/javascript" src="js/hora_fecha.js"></script>
		</head>
		<body onload="IniciarReloj24()">
			<table class="tabla_menu">
				<tr>
					<td>
						<div id="cabecera"><img src="images/<fmt:message key="header"/>_${proyecto.proyect}.gif" alt=""></div>
					</td>
					<td align="right">
						<div id="reloj">
							<div id="fecha"></div>
							<div id="Reloj24H"></div>
						</div>
					</td>
				</tr>
			</table>
			<p class="error">
				<fmt:message key="mensajeIE"/>
				<a href="http://www.mozilla-europe.org/firefox/">Firefox</a>,
				<a href="http://www.google.com/chrome">Chrome</a>,
				<a href="http://www.opera.com/browser">Opera</a>
				<fmt:message key="o"/>
				<a href="http://www.apple.com/safari/download">Safari</a>.
			</p>
		</body>
	</fmt:bundle>
</html>