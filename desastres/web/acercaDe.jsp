<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE HTML>
<html>
	<fmt:bundle basename="fmt.eji8n">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
			<title>Acerca de</title>
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
							<div id="fecha">
								<script type="text/javascript">MostrarFechaActual()</script>
							</div>
							<form id="Reloj24H" action="">
								<div>
									<input type="text" size="8" name="digitos" value="" style="background:transparent; border:0px #999999; color:#999999; text-align:center; font-size:18pt; font-weight:bold">
								</div>
							</form>
						</div>
					</td>
				</tr>
			</table>
			<h1>Informaci&oacute;n</h1>
			<table>
				<tr>
					<td>Director de emergencias:</td>
					<td>Nombre Apellido1 Apellido2 - 968723080</td>
				</tr>
				<tr>
					<td>Altura del edificio:</td>
					<td>xxx metros</td>
				</tr>
				<tr>
					<td>Direcci&oacute;n:</td>
					<td>Calle Molinico 7</td>
				</tr>
			</table>
			<p>
				<c:url value="index.jsp" var="volver"/>
				<a href="${volver}">Volver</a>
			</p>
		</body>
	</fmt:bundle>
</html>