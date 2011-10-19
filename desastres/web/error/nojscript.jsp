<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean class="gsi.proyect.ProyectBean" id="proyecto" scope="session"/>

<!DOCTYPE HTML>
<html>
	<fmt:bundle basename="fmt.eji8n">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
			<title>No Javascript</title>
			<link type="image/vnd.microsoft.icon" rel="icon" href="images/favicon_${proyecto.proyect}.ico"/>
			<link type="text/css" rel="stylesheet" href="css/improvisa_style.css"/>
			<script type="text/javascript" src="js/i18n.js"></script>
			<script type="text/javascript">var idioma = '<fmt:message key="idioma"/>'</script>
			<script type="text/javascript" src="js/hora_fecha.js"></script>
		</head>
		<body>
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
			<p class="error">Esta p&aacute;gina necesita usar Javascript.</p>
			<p class="error">Por favor, active su funcionamiento o utilice un navegador web que lo soporte</p>
		</body>
	</fmt:bundle>
</html>