<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%--<jsp:useBean class="roads.DirectionsBean" id="recursos" scope="session"/>--%>
<jsp:useBean class="gsi.proyect.ProyectBean" id="proyecto" scope="session"/>
<jsp:setProperty name="proyecto" property="nombreUsuario" value="<%= request.getRemoteUser() %>"/>

<c:if test="${param.lang != null}">
	<fmt:setLocale value="${param.lang}" scope="session"/>
</c:if>

<!DOCTYPE HTML>
<html>
	<fmt:bundle basename="fmt.eji8n">
		<head>
			<!--[if lt IE 9]><meta http-equiv="refresh" content="0; URL=mensajeIE.jsp"/><![endif]-->
			<noscript><meta http-equiv="refresh" content="0; URL=nojscript.jsp"/></noscript>
			<meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
			<title><fmt:message key="title"/></title>
			<link type="image/vnd.microsoft.icon" rel="icon" href="images/favicon_desastres.ico"/>
			<link type="text/css" rel="stylesheet" href="css/improvisa_style.css"/>
			<link type="text/css" rel="stylesheet" href="css/improvisa_style_desastres.css"/>
			<link type="text/css" rel="stylesheet" href="css/tab-view.css" media="screen"/>
			<script type="application/javascript" src="js/i18n.js"></script>
			<script type="application/javascript" src="js/jquery.js"></script>
			<script type="application/javascript" src="js/directionsInfo.js"></script> <!-- Object directionsInfo for agents on roads -->
			<script type="application/javascript"
				src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAh7S32QoZL_osuspiqG6JHhQel9MQybMe66MKP3JO4yeJZZpmpBRzTAem65aDN5SbzD0WHx5UGyq72Q">
			</script>
			<script type="application/javascript">
				var proyecto = 'desastres';
				var userName = '${proyecto.nombreUsuario}';
				var usuario_actual = ${proyecto.id};
				var usuario_actual_tipo = '${proyecto.rol}';
				var nivelMsg = ${proyecto.nivelMsg};
				var idioma = '<fmt:message key="idioma"/>';
			</script>
			<script type="application/javascript" src="js/mapa.js"></script>
			<script type="application/javascript" src="js/mapa_desastres.js"></script>
			<script type="application/javascript" src="js/mapa_desastres2.js"></script>
			<script type="application/javascript" src="js/experto.js"></script>
			<script type="application/javascript" src="js/registro.js"></script>
			<!-- Objeto Marcador -->
			<script type="application/javascript" src="js/marcador.js"></script>
			<!--Hora y Fecha -->
			<script type="application/javascript" src="js/hora_fecha.js"></script>
			<!-- Formularios, parte grafica y logica -->
			<script type="application/javascript" src="js/tab-view.js"></script>
			<script type="application/javascript" src="js/forms.js"></script>
			<!-- jqModal Dependencies -->
			<script type="application/javascript" src="js/jqModal.js"></script>
			<!-- Optional Javascript for Drag'n'Resize -->
			<script type="application/javascript" src="js/jqDnR.js"></script>
			<script type="application/javascript" src="js/dimensions.js"></script>
			<!-- Necesario para los pop-ups -->
			<script type="application/javascript" src="js/popUps.js"></script>
			<!-- jQuery -->
			<script type="application/javascript" src="js/jquery-fieldselection.js"></script>
			<script type="application/javascript" src="js/jquery-ui-personalized-1.5.2.min.js"></script>
			<!-- DWR. These files are created in the runtime -->
			<script type="application/javascript" src="/desastres/dwr/util.js"></script>
			<script type="application/javascript" src="/desastres/dwr/interface/DirectionsBean.js"></script>
			<script type="application/javascript" src="/desastres/dwr/engine.js"></script>
			<!-- Adds various Methods to GPolygon and GPolyline -->
			<script type="application/javascript" src="js/epoly.js"></script>
			<!-- Agents movement through roads -->
			<script type="application/javascript" src="js/resourcesOnRoads.js"></script>
			<!-- Areas around fires -->
			<script type="application/javascript" src="js/einsert.js"></script>
		</head>
		<body onload="IniciarReloj24(); initialize(); dwr.engine.setActiveReverseAjax(true);" onunload="GUnload()">
			<c:import url="jspf/ventana_modificacion.jsp"/>
			<table class="tabla_body">
				<!-- Cabecera con imagen y hora -->
				<tr>
					<td colspan="2"><c:import url="jspf/cabecera.jsp"/></td>
				</tr>
				<!-- Cuerpo de la pagina -->
				<tr>
					<td id="left">
						<!-- If the user isn't autenticated, we show the login form -->
						<c:if test="${proyecto.nombreUsuario == null}">
							<c:import url="jspf/formInicio.jsp"/>
						</c:if>
						<c:if test="${proyecto.nombreUsuario != null}">
							<!-- and if the user is autenticated, we show the username and logout button -->
							<fmt:message key="eres"/> <span id="signeduser">${proyecto.nombreUsuario}</span>
							<br/>
							<c:url var="logout" value="logout.jsp"/>
							<a href="${logout}" onclick="$.post('getpost/update.jsp',{'accion':'cerrarSesion','nombre':'${proyecto.nombreUsuario}'})">
								<fmt:message key="cerrarsesion"/>
							</a>
							<c:import url="jspf/menu_desastres.jsp"/>
						</c:if>
					</td>
					<td id="fila_mapa">
						<!-- minitabs top-right -->
						<div id="minitabs">
							<c:if test="${proyecto.nombreUsuario != null}">
								<div id="minitab3" class="minitab">
									<img alt="ver" src="images/tab_simulator.png"/>
								</div>
							</c:if>
							<div id="minitab2" class="minitab">
								<img alt="ver" src="images/tab_building.png"/>
							</div>
							<div id="minitab1" class="minitab">
								<img alt="m&aacute;s info" src="images/tab_eye.png"/>
							</div>
						</div>
						<div id="map_canvas"></div>
						<c:import url="jspf/minitabs.jsp"/>
					</td>
				</tr>
				<!-- Screen for the servlet information -->
				<tr>
					<td colspan="2">
						<div id="close_screen"><fmt:message key="ocultar"/></div>
						<div id="screen"></div>
					</td>
				</tr>
			</table>
			<%--
				int[] rscs = recursos.getResourcesList();
				for (int i = 0; i < rscs.length; i++) {
					String st = "<input type=\"hidden\" id=\"start" + rscs[i] + "\"/>";
					out.println(st);
					String ed = "<input type=\"hidden\" id=\"end" + rscs[i] + "\"/>";
					out.println(ed);
				}
			--%>
			
			<c:if test="${param.alert == true}">
				<script type="application/javascript">
					window.alert('Fin de la simulación');
				</script>
			</c:if>
		</body>
	</fmt:bundle>
</html>