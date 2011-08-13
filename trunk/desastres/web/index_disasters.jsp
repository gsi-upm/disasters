<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page language="java" %>
<%@ page isELIgnored="false" %>
<%@ page import="org.securityfilter.example.Constants" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%--<jsp:useBean class="roads.DirectionsBean" id="recursos" scope="session"/>--%>
<jsp:useBean class="gsi.proyect.ProyectBean" id="proyecto" scope="session"/>
<c:set var="nombreUsuario" value="<%= request.getRemoteUser()%>"/>

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
			<meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
			<title><fmt:message key="title_${proyecto.proyect}"/></title>
			<link type="image/vnd.microsoft.icon" rel="icon" href="images/favicon_${proyecto.proyect}.ico"/>
			<link type="text/css" rel="stylesheet" href="css/improvisa_style.css"/>
			<link type="text/css" rel="stylesheet" href="css/improvisa_style_${proyecto.proyect}.css"/>
			<link type="text/css" rel="stylesheet" href="css/tab-view.css" media="screen"/>
			<script type="text/javascript" src="js/i18n.js"></script>
			<!--[if lt IE 9]>
				<script type="text/javascript" src="js/mensajeIE.js"></script>
				<script type="text/javascript"><fmt:message key="mensajeIE"/></script>
			<![endif]-->
			<script type="text/javascript" src="js/jquery.js"></script>
			<script type="text/javascript" src="js/directionsInfo.js"></script> <!-- Object directionsInfo for agents on roads -->
			<script type="text/javascript" src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAA_pvzu2eEg9OhdvcrhLRkgRSXZ-vMQ48x4C6FPZ72aXwxrdjUDxSASm6YS5fgxM4XDiwIpFkrYCIdUQ"></script>
			<!-- GOOGLE MAPS v3
				<meta name="viewport" content="initial-scale=1.0, user-scalable=no">
				<script type="text/javascript" src="http://maps.google.com/maps/api/js?v=3&sensor=false&language=es&region=ES"></script>
			-->
			<script type="text/javascript">
				var nivelMsg = ${proyecto.nivelMsg};
				var nombreUsuario = '${nombreUsuario}';
				var idioma = '<fmt:message key="idioma"/>';
			</script>
			<script type="text/javascript" src="js/mapa_${proyecto.proyect}.js"></script>
			<script type="text/javascript" src="js/mapa.js"></script>  <!-- mapa, marcador, forms, resourcesOnRoads, ventana_modificacion y menu_caronte_admin -->
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
		<body onload="IniciarReloj24(); initialize(); dwr.engine.setActiveReverseAjax(true); mostrarMensajes();" onunload="GUnload()">
			<c:if test="${nombreUsuario != null}">
				<%--<c:import url="ventana_modificacion.jsp"/>
				<c:import url="ventana_acciones.jsp"/>--%>
			</c:if>
			<table class="tabla_menu">
				<!-- Cabecera con imagen y hora -->
				<tr>
					<td>
						<div id="cabecera"><img src="images/<fmt:message key="header"/>_${proyecto.proyect}.gif" alt=""/></div>
					</td>
					<td class="derecha">
						<!--reloj -->
						<div id="reloj">
							<div id="fecha">
								<script type="text/javascript">MostrarFechaActual()</script>
							</div>
							<div id="Reloj24H"></div>
						</div>
<!--SPAN DE PRUEBAS -->	v.73 <span id="prueba"></span>
					</td>
				</tr>
			</table>
			<table class="tabla_menu">
				<!-- Cuerpo de la pagina -->
				<tr style="vertical-align:top">
					<td>
						<div id="left">
							<!-- If the user isn't autenticated, we show the login form -->
							<c:if test="${nombreUsuario == null}">
								<h2><fmt:message key="iniciarsesion"/></h2>
								<div id="login">
									<form action="<%=response.encodeURL(Constants.LOGIN_FORM_ACTION)%>" method="post" id="loginform">
										<table>
											<tr><td id="regMsg" colspan="2" style="color:lime"></td></tr>
											<tr>
												<td><fmt:message key="usuario"/>:</td>
												<td><input type="text" name="<%=Constants.LOGIN_USERNAME_FIELD%>" id="username" size="26"/></td>
											</tr>
											<tr>
												<td><fmt:message key="contrasenna"/>:</td>
												<td><input type="password" name="<%=Constants.LOGIN_PASSWORD_FIELD%>" id="pwd" size="26"/></td>
											</tr>
											<tr>
												<td colspan="2"><input type="submit" name="Submit" id="submit_butt" value="<fmt:message key="aceptar"/>"/></td>
											</tr>
										</table>
										<p>
											&iquest;No est&aacute; registrado?
											<input type="button" value="Registrarse" onclick="registro()"/>
										</p>
									</form>
									<form action="#" id="registro" style="display:none">
										<table>
											<tr><td id="userError" colspan="2" style="color:red"></td></tr>
											<tr id="user1">
												<td>Nombre usuario</td>
												<td><input type="text" name="user1"size="26"/></td>
											</tr>
											<tr id="user2">
												<td>Repetir nombre</td>
												<td><input type="text" name="user2"size="26"/></td>
											</tr>
											<tr><td id="passError" colspan="2" style="color:red"></td></tr>
											<tr id="pass1">
												<td>Contraseña</td>
												<td><input type="password" name="pass1"size="26"/></td>
											</tr>
											<tr id="pass2">
												<td>Repetir contraseña</td>
												<td><input type="password" name="pass2"size="26"/></td>
											</tr>
											<tr>
												<td colspan="2">
													<input type="button" value="Aceptar" onclick="registrar(user1.value, user2.value, pass1.value, pass2.value)"/>
													<input type="button" value="Cancelar" onclick="cancelarRegistro()"/>
												</td>
											</tr>
										</table>
									</form>
								</div>
							</c:if>
							<c:if test="${nombreUsuario != null}">
								<!-- and if the user is autenticated, we show the username and logout button -->
								<fmt:message key="eres"/> <span id="signeduser">${nombreUsuario}</span>
								<br/>
								<a href="logout.jsp"><fmt:message key="cerrarsesion"/></a>
								<c:if test="${proyecto.proyect == 'disasters'}">
									<c:import url="menu_disasters.jsp"/>
								</c:if>

								<c:if test="${proyecto.proyect == 'caronte'}">
									<c:if test="${proyecto.rol == 'administrator' || proyecto.rol == 'agent' || proyecto.rol == 'nurse' || proyecto.rol == 'otherStaff'}">
										<!-- if the user is in role 'administrator' or 'agent' -->
										<c:import url="menu_caronte_admin.jsp"/>
									</c:if>
									<c:if test="${proyecto.rol == 'citizen'}">
										<!-- if the user is in role 'citizen' -->
										<c:import url="menu_caronte_user.jsp"/>
									</c:if>
								</c:if>
							</c:if>
						</div>
					</td>
					<td id="fila_mapa">
						<div id="right">
							<c:if test="${nombreUsuario == null}">
								<c:if test="${proyecto.proyect == 'disasters'}">
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
								</c:if>
								<c:if test="${proyecto.proyect == 'caronte'}">
									<div style="height:21px"></div>
								</c:if>
								<div id="map_canvas"></div>
							</c:if>
							<c:if test="${nombreUsuario != null}">
								<c:if test="${proyecto.proyect == 'disasters'}">
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
								<c:if test="${proyecto.proyect == 'caronte'}">
									<c:if test="${proyecto.rol != 'citizen'}">
										<c:import url="residencia_caronte.jsp"/>
									</c:if>
									<c:if test="${proyecto.rol == 'citizen'}">
										<div style="height:21px"></div>
										<div id="map_canvas"></div>
									</c:if>
								</c:if>
							</c:if>
						</div>
					</td>
					<td>
						<div id="open_messages"><a href="#"><fmt:message key="mostrar"/></a></div>
						<div id="close_messages"><a href="#"><fmt:message key="ocultar"/></a></div>
						<div id="messages">
							<p>MENSAJES:</p>
						</div>
						<audio id="audio" controls="controls" style="display:none">
							<source src="images/bad.ogg" type="audio/ogg"/>
							<source src="images/alarm.mp3" type="audio/mpeg"/>
						</audio>
					</td>
				</tr>
			</table>
			<c:if test="${proyecto.proyect == 'disasters'}">
				<c:import url="minitabs_disasters.jsp"/>
			</c:if>
			<!-- Screen for the servlet information -->
			<div id="close_screen"><a href="#"><fmt:message key="ocultar"/></a></div>
			<div id="screen"></div>
			<!--<p class="iconos_validacion">
				<a href="http://validator.w3.org/check?uri=referer"><img src="http://www.w3.org/Icons/valid-html401" alt="Valid HTML 4.01 Strict"/></a>
				<a href="http://jigsaw.w3.org/css-validator/check/referer"><img src="http://jigsaw.w3.org/css-validator/images/vcss" alt="Valid CSS!"/></a>
				<a href="http://www.w3.org/html/logo/"><img src="http://www.w3.org/html/logo/badge/html5-badge-h-css3.png" alt="HTML5 Powered with CSS3 / Styling"/></a>
			</p>-->
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