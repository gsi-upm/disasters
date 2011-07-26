<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:useBean class="gsi.proyect.ProyectBean" id="proyecto" scope="session"/>

<!DOCTYPE HTML>
<html>
	<fmt:bundle basename="fmt.eji8n">
		<head>
			<meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
			<title><fmt:message key="title_${proyecto.proyect}"/> - Acceso denegado</title>
			<link type="image/x-icon" rel="shotcut icon" href="images/favicon_${proyecto.proyect}.ico"/>
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
			<div>
				<h2>Acceso denegado</h2>
				<p>Nombre de usuario y/o contrase&ntilde;a incorrecta</p>
				<p>Vuelve a intentarlo</p>
				<p>Regresa a la <a href="index.jsp">p&aacute;gina de inicio</a></p>
			</div>
		</body>
	</fmt:bundle>
</html>