<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%--<jsp:useBean class="roads.DirectionsBean" id="recursos" scope="session"/>--%>
<jsp:useBean class="gsi.proyect.ProyectBean" id="proyecto" scope="session"/>
<c:set var="nombreUsuario" value="<%= request.getRemoteUser() %>"/>

<c:if test="${param.lang != null}">
	<fmt:setLocale value="${param.lang}" scope="session"/>
</c:if>

<c:if test="${nombreUsuario != null}">
	<c:if test="${param.proyect == null}">
		<c:redirect url="/proyect?user=${nombreUsuario}"/>
	</c:if>
	<c:if test="${param.proyect != null}"> <%-- and param.alert == null --%>
		<jsp:setProperty name="proyecto" property="proyect" value="${param.proyect}"/>
	</c:if>
	<c:if test="${param.rol != null}">
		<jsp:setProperty name="proyecto" property="rol" value="${param.rol}"/>
	</c:if>
</c:if>

<!DOCTYPE HTML>
<html>
	<fmt:bundle basename="fmt.eji8n">
		<head>
			<!--[if lt IE 9]><meta http-equiv="refresh" content="0; URL=error/mensajeIE.jsp"/><![endif]-->
			<noscript><meta http-equiv="refresh" content="0; URL=error/nojscript.jsp"/></noscript>
			<meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
			<title><fmt:message key="title_disasters"/></title>
			<link type="image/vnd.microsoft.icon" rel="icon" href="images/favicon_disasters.ico"/>
			<link type="text/css" rel="stylesheet" href="css/improvisa_style.css"/>
			<link type="text/css" rel="stylesheet" href="css/improvisa_style_disasters.css"/>
			<link type="text/css" rel="stylesheet" href="css/tab-view.css" media="screen"/>
			<script type="text/javascript" src="js/i18n.js"></script>
			<script type="text/javascript" src="js/jquery.js"></script>
			<script type="text/javascript" src="js/directionsInfo.js"></script> <!-- Object directionsInfo for agents on roads -->
			<script type="text/javascript" src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAh7S32QoZL_osuspiqG6JHhShGMulApA1qGrWH2FWs8V2HjpzbhR6R94HFuEi6_iz-WDuB-XPDkJ2rA"></script>
			<script type="text/javascript">
				var nivelMsg = ${proyecto.nivelMsg};
				var userName = '${nombreUsuario}';
				var usuario_actual_tipo = '${proyecto.rol}';
				var idioma = '<fmt:message key="idioma"/>';
			</script>
			<script type="text/javascript" src="js/mapa_disasters.js"></script>
			<script type="text/javascript" src="js/mapa.js"></script>
			<script type="text/javascript" src="js/mapa2.js"></script>
			<script type="text/javascript" src="js/mensajesYExperto.js"></script>
			<script type="text/javascript" src="js/registro.js"></script>
			<!-- Objeto Marcador -->
			<script type="text/javascript" src="js/marcador.js"></script>
			<!--Hora y Fecha -->
			<script type="text/javascript" src="js/hora_fecha.js"></script>
			<!-- Formularios, parte grafica y logica -->
			<script type="text/javascript" src="js/tab-view.js"></script>
			<script type="text/javascript" src="js/forms.js"></script>
			<!-- jqModal Dependencies -->
			<script type="text/javascript" src="js/jquery.js"></script>
			<script type="text/javascript" src="js/jqModal.js"></script>
			<!-- Optional Javascript for Drag'n'Resize -->
			<script type="text/javascript" src="js/jqDnR.js"></script>
			<script type="text/javascript" src="js/dimensions.js"></script>
			<!-- Necesario para los radio en los formularios -->
			<script type="text/javascript" src="js/seleccionRadio.js"></script>
			<!-- Necesario para los pop-ups -->
			<script type="text/javascript" src="js/popUps.js"></script>
			<!-- jQuery -->
			<script type="text/javascript" src="js/jquery-fieldselection.js"></script>
			<script type="text/javascript" src="js/jquery-ui-personalized-1.5.2.min.js"></script>
			<!-- DWR. These files are created in the runtime -->
			<script type="text/javascript" src="/desastres/dwr/util.js"></script>
			<script type="text/javascript" src="/desastres/dwr/interface/DirectionsBean.js"></script>
			<script type="text/javascript" src="/desastres/dwr/engine.js"></script>
			<!-- Adds various Methods to GPolygon and GPolyline -->
			<script type="text/javascript" src="js/epoly.js"></script>
			<!-- Agents movement through roads -->
			<script type="text/javascript" src="js/resourcesOnRoads.js"></script>
			<!-- Areas around fires -->
			<script type="text/javascript" src="js/einsert.js"></script>
		</head>
		<body onload="IniciarReloj24(); initialize(); mostrarMensajes(); dwr.engine.setActiveReverseAjax(true);" onunload="GUnload()">
			<c:import url="ventana_modificacion.jsp"/>
			<!-- Cabecera con imagen y hora -->
			<div>
				<img id="cabecera" src="images/<fmt:message key="header"/>_disasters.gif" alt=""/>
				<div class="derecha">
					<!-- reloj -->
					<div id="reloj">
						<div id="fecha"></div>
						<div id="Reloj24H"></div>
					</div>
					v.85
					<img id="langInit" class="pulsable" src="images/flags/<fmt:message key="idioma"/>.png"
						alt="lang:<fmt:message key="idioma"/>" onclick="menuIdiomas('abrir')"/>
					<div id="langSelect" class="oculto">
						<img class="pulsable" src="images/close.gif" alt="<fmt:message key="cerrar"/>" onclick="menuIdiomas('cerrar')"/>
						<c:url var="index_es" value="index.jsp"><c:param name="lang" value="es"/></c:url>
						<a href="${index_es}"><img src="images/flags/es.png" alt="Espa&ntilde;ol"/></a>
						<c:url var="index_en" value="index.jsp"><c:param name="lang" value="en"/></c:url>
						<a href="${index_en}"><img src="images/flags/en.png" alt="English"/></a>
						<c:url var="index_en_GB" value="index.jsp"><c:param name="lang" value="en_GB"/></c:url>
						<a href="${index_en_GB}"><img src="images/flags/en_GB.png" alt="British English"/></a>
						<c:url var="index_fr" value="index.jsp"><c:param name="lang" value="fr"/></c:url>
						<a href="${index_fr}"><img src="images/flags/fr.png" alt="Fran&ccedil;ais"/></a>
						<c:url var="index_de" value="index.jsp"><c:param name="lang" value="de"/></c:url>
						<a href="${index_de}"><img src="images/flags/de.png" alt="Deutsch"/></a>
					</div>
					<span id="prueba"></span> <!-- SPAN DE PRUEBAS -->
					<div>
						<c:url value="acercaDe.jsp" var="acercaDe"/>
						<a href="${acercaDe}"><fmt:message key="acerca"/></a>
					</div>
				</div>
			</div>
			<!-- Cuerpo de la pagina -->
			<table class="tabla_body">
				<tr>
					<td id="left">
						<!-- If the user isn't autenticated, we show the login form -->
						<c:if test="${nombreUsuario == null}">
							<c:import url="formInicio.jsp"/>
						</c:if>
						<c:if test="${nombreUsuario != null}">
							<!-- and if the user is autenticated, we show the username and logout button -->
							<fmt:message key="eres"/> <span id="signeduser">${nombreUsuario}</span>
							<br/>
							<c:url var="logout" value="logout.jsp"/>
							<a href="${logout}" onclick="$.post('getpost/update.jsp',{'accion':'cerrarSesion','nombre':'${nombreUsuario}'})">
								<fmt:message key="cerrarsesion"/>
							</a>
							<c:import url="menu_disasters.jsp"/>
						</c:if>
					</td>
					<td id="fila_mapa">
						<c:if test="${nombreUsuario == null}">
							<!-- minitabs top-right -->
							<div id="minitabs">
								<c:if test="${nombreUsuario != null}">
									<div id="minitab3" class="minitab">
										<img alt="ver" src="images/tab_simulator.png"/>
									</div>
								</c:if>
								<div id="minitab2" class="minitab">
									<img alt="ver" src="images/tab_building.png"/>
								</div>
								<div id="minitab1" class="minitab">
									<img alt="más info" src="images/tab_eye.png"/>
								</div>
							</div>
							<div id="map_canvas"></div>
						</c:if>
						<c:if test="${nombreUsuario != null}">
							<!-- minitabs top-right -->
							<div id="minitabs">
								<c:if test="${nombreUsuario != null}">
									<div id="minitab3" class="minitab">
										<img alt="ver" src="images/tab_simulator.png"/>
									</div>
								</c:if>
								<div id="minitab2" class="minitab">
									<img alt="ver" src="images/tab_building.png"/>
								</div>
									<div id="minitab1" class="minitab">
									<img alt="más info" src="images/tab_eye.png"/>
								</div>
							</div>
							<div id="map_canvas"></div>
						</c:if>
					</td>
					<c:if test="${proyecto.proyect == 'caronte'}">
						<td id="right">
							<div id="open_messages" class="pulsable"><fmt:message key="mostrar"/></div>
							<div id="close_messages" class="pulsable"><fmt:message key="ocultar"/></div>
							<div id="messages">
								<p><fmt:message key="mensajes"/>:</p>
							</div>
							<audio id="audio" class="oculto" controls="controls">
								<source src="images/bad.ogg" type="audio/ogg"/>
								<source src="images/alarm.mp3" type="audio/mpeg"/>
							</audio>
						</td>
					</c:if>
				</tr>
			</table>
			<c:if test="${proyecto.proyect == 'disasters'}">
				<c:import url="minitabs_disasters.jsp"/>
				<!-- Screen for the servlet information -->
				<div id="close_screen"><fmt:message key="ocultar"/></div>
				<div id="screen"></div>
			</c:if>
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
				<script type="text/javascript">
					window.alert('Fin de la simulacion')
				</script>
			</c:if>
		</body>
	</fmt:bundle>
</html>