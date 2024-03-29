<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%--<jsp:useBean class="roads.DirectionsBean" id="recursos" scope="session"/>--%>
<jsp:useBean class="gsi.project.UserBean" id="usuario" scope="session"/>
<jsp:setProperty name="usuario" property="nombre" value="<%= request.getRemoteUser() %>"/>

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
			<title>Caronte</title>
			<link type="image/vnd.microsoft.icon" rel="icon" href="images/favicon_caronte.ico"/>
			<link type="text/css" rel="stylesheet" href="css/improvisa_style.css"/>
			<link type="text/css" rel="stylesheet" href="css/improvisa_style_caronte.css"/>
			<link type="text/css" rel="stylesheet" href="css/tab-view.css"/>
			<link type="text/css" rel="stylesheet" href="css/jqModal.css"/>
			<script type="application/javascript" src="js/i18n.js"></script>
			<script type="application/javascript" src="js/jquery-1.8.3.min.js"></script>
			<!--<script type="application/javascript" src="js/ajax-pushlet-client.js"></script>-->
			<script type="application/javascript" src="https://maps.google.com/maps/api/js?v=3.10&amp;sensor=true&amp;region=ES"></script>
			<script type="application/javascript">
				var userName = '${usuario.nombre}';
				var usuario_actual = ${usuario.id};
				var usuario_actual_tipo = '${usuario.rol}';
				var nivelMsg = ${usuario.nivelMsg};
				var idioma = '<fmt:message key="idioma"/>';
			</script>
			<script type="application/javascript" src="js/mapa.js"></script>
			<script type="application/javascript" src="js/mapa_caronte.js"></script>
			<script type="application/javascript" src="js/mapa_caronte2.js"></script>
			<script type="application/javascript" src="js/mensajes.js"></script>
			<script type="application/javascript" src="js/registro.js"></script>
			<!-- Objeto Marcador -->
			<script type="application/javascript" src="js/marcador.js"></script>
			<!--Hora y Fecha -->
			<script type="application/javascript" src="js/hora_fecha.js"></script>
			<!-- Formularios, parte grafica y logica -->
			<script type="application/javascript" src="js/tw-sack.js"></script>
			<script type="application/javascript" src="js/tab-view.js"></script>
			<script type="application/javascript" src="js/forms.js"></script>
			<!-- jqModal Dependencies -->
			<script type="application/javascript" src="js/jqModal.js"></script>
			<!-- Optional Javascript for Drag'n'Resize -->
			<!--<script type="application/javascript" src="js/jqDnR.js"></script>-->
			<!--<script type="application/javascript" src="js/dimensions.js"></script>-->
			<!-- jQuery -->
			<!--<script type="application/javascript" src="js/jquery-fieldselection.js"></script>-->
			<!--<script type="application/javascript" src="js/jquery-ui-personalized-1.5.2.min.js"></script>-->
			<!-- DWR. These files are created in the runtime -->
			<!-- <script type="application/javascript" src="/caronte/dwr/util.js"></script> -->
			<!-- <script type="application/javascript" src="/caronte/dwr/interface/DirectionsBean.js"></script> -->
			<!-- <script type="application/javascript" src="/caronte/dwr/engine.js"></script> -->
			<!-- Object directionsInfo for agents on roads -->
			<!-- <script type="application/javascript" src="js/directionsInfo.js"></script> -->
			<!-- Agents movement through roads -->
			<!-- <script type="application/javascript" src="js/resourcesOnRoads.js"></script> -->
		</head>
		<body onload="IniciarReloj24(); initialize();"> <!-- dwr.engine.setActiveReverseAjax(true); -->
			<table class="tabla_body">
				<!-- Cabecera con imagen y hora -->
				<tr>
					<td colspan="3">
						<c:import url="jspf/cabecera.jsp"/>
					</td>
				</tr>
				<!-- Cuerpo de la pagina -->
				<tr>
					<td id="left">
						<!-- If the user isn't autenticated, we show the login form -->
						<c:if test="${usuario.nombre == null}">
							<c:import url="jspf/formInicio.jsp"/>
						</c:if>
						<c:if test="${usuario.nombre != null}">
							<!-- and if the user is autenticated, we show the username and logout button -->
							<fmt:message key="eres"/> <span id="signeduser">${usuario.nombre}</span>
							<br/>
							<c:url var="logout" value="logout.jsp">
								<c:param name="nombre" value="${usuario.nombre}"/>
							</c:url>
							<a href="${logout}"><fmt:message key="cerrarsesion"/></a>
							<c:if test="${usuario.rol != 'citizen'}">
								<!-- if the user is in role 'administrator' or 'agent' -->
								<c:import url="jspf/menu_caronte_admin.jsp"/>
							</c:if>
							<c:if test="${usuario.rol == 'citizen'}">
								<!-- if the user is in role 'citizen' -->
								<c:import url="jspf/menu_caronte_user.jsp"/>
							</c:if>
						</c:if>
					</td>
					<td id="fila_mapa">
						<c:choose>
							<c:when test="${usuario.nombre != null && usuario.rol != 'citizen'}">
								<c:import url="jspf/residencia.jsp"/>
							</c:when>
							<c:otherwise>
								<div class="div_vacio"></div>
								<div id="contenedor_mapa">
									<div id="map_canvas"></div>
								</div>
							</c:otherwise>
						</c:choose>
					</td>
					<td id="right">
						<div id="accion">
							<p id="pregunta"></p>
							<p>
								<input type="button" name="pregOK" value="Aceptar" onclick="responder(1)"/>
								<input type="button" name="pregNO" value="Cancelar" onclick="responder(0)"/>
							</p>
						</div>
						<div id="open_messages" class="pulsable"><fmt:message key="mostrar"/></div>
						<div id="close_messages" class="pulsable"><fmt:message key="ocultar"/></div>
						<div id="messages">
							<p><fmt:message key="mensajes"/>:</p>
						</div>
						<audio id="audio" class="oculto" controls="controls">
							<source type="audio/ogg" src="sounds/bad.ogg"/>
							<source type="audio/mpeg" src="sounds/alarm.mp3"/>
						</audio>
					</td>
				</tr>
			</table>
			<%--
				int[] rscs = recursos.getResourcesList();
				for(int i = 0; i < rscs.length; i++){
					String st = "<input type=\"hidden\" id=\"start" + rscs[i] + "\"/>";
					out.println(st);
					String ed = "<input type=\"hidden\" id=\"end" + rscs[i] + "\"/>";
					out.println(ed);
				}
			--%>
		</body>
	</fmt:bundle>
</html>