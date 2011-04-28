<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page language = "java" %>
<%@ page isELIgnored = "false" %>
<%@ page import = "org.securityfilter.example.Constants"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
	<fmt:bundle basename="fmt.eji8n">
        <head>
            <meta http-equiv="content-type" content="text/html; charset=UTF-8">
            <title><fmt:message key="title"/></title>
			<link type="image/x-icon" rel="shotcut icon" href="images/favicon.ico">
            <link href="css/improvisa_style.css" rel="stylesheet" type="text/css">
            <link rel="stylesheet" href="css/tab-view.css" type="text/css" media="screen">
			<!--[if lt IE 9]>
				<script type="text/javascript">
					window.alert('Esta pagina no es compatible con Internet Explorer 8 y anteriores.\nUtilice IE 9 u otro navegador web.');
					document.write('<h2 class="error">Esta p&aacute;gina no es compatible con Internet Explorer 8 y anteriores.</h2>');
					document.write('<h2 class="error">Utilice IE 9 u otro navegador web, como <a href="http://www.mozilla-europe.org/firefox/">Firefox</a>, ' +
						'<a href="http://www.google.com/chrome">Chrome</a>, <a href="http://www.opera.com/browser">Opera</a> o <a href="http://www.apple.com/safari/download">Safari</a>.</h2>');
				</script>
			<![endif]-->
            <script src="js/jquery.js" type="text/javascript" ></script>
            <script src="js/directionsInfo.js" type="text/javascript"></script> <!-- Object directionsInfo for agents on roads -->            
            <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAA_pvzu2eEg9OhdvcrhLRkgRSXZ-vMQ48x4C6FPZ72aXwxrdjUDxSASm6YS5fgxM4XDiwIpFkrYCIdUQ" type="text/javascript"></script>
            <script src="js/mapa.js" type="text/javascript"></script>
            <!-- Objeto Marcador -->
            <script src="js/marcador.js" type="text/javascript"></script>            
            <!--Hora y Fecha -->
            <script src="js/hora_fecha.js" type="text/javascript"></script>
            <!-- Formularios, parte grafica y logica -->
            <script src="js/tab-view.js" type="text/javascript" ></script>
            <script src="js/forms.js" type="text/javascript" ></script>
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
            <%-- DWR.These files are created in the runtime --%>
            <script type='text/javascript' src='/disasters/dwr/util.js'></script>
            <script type='text/javascript' src='/disasters/dwr/interface/DirectionsBean.js'></script>
            <script type='text/javascript' src='/disasters/dwr/engine.js'></script>
            <%-- Adds various Methods to GPolygon and GPolyline --%>
            <script type="text/javascript" src="js/epoly.js"></script>
            <%-- Agents movement through roads --%>
            <script src="js/resourcesOnRoads.js" type="text/javascript"></script>
            <%-- Areas around fires --%>
            <script src="js/einsert.js" type="text/javascript"></script>
        </head>
        <body onload="IniciarReloj24(); initialize(); dwr.engine.setActiveReverseAjax(true);" onunload="GUnload()">
			<%--@ include file="info.jsp" --%>
			<!-- Ventana de error de direccion no encontrada -->
			<div class="jqmWindow" id="error">
				<p id="error_texto"></p>
				<p class="centrado"><button class="xxx jqmClose"><fmt:message key="aceptar"/></button></p>
			</div>
			<!-- Ventana de modificacion -->
			<div class="jqmWindow" id="modificar">
				<form id="form-modifica" action="#">
					<table class="tabla_menu">
						<tr>
							<td colspan="2"><fmt:message key="modificacion"/></td>
						</tr>
						<tr>
							<td id="item_tipo"></td>
							<td id="item_tipo_icon" rowspan="4" class="img_menu"><img alt="" src="#" id="iconoAdecuado" class="rayas"></td>
						</tr>
						<tr id="quantity">
							<td>
								<label for="cantidad"><fmt:message key="numero"/>:</label>
								<select name="cantidad" id="cantidad">
									<option id="s1" value="1">1</option>
									<option id="s2" value="2">2</option>
									<option id="s3" value="3">3</option>
									<option id="s4" value="4">4</option>
									<option id="s5" value="5">5</option>
									<option id="s6" value="6">6</option>
									<option id="s7" value="7">7</option>
									<option id="s8" value="8">8</option>
									<option id="s9" value="9">9</option>
									<option id="s10" value="10">10</option>
								</select>
							</td>
						</tr>
						<tr><td id="asociacion"></td></tr>
						<tr><td><input type="text" name="nombre" id="nombre" class="nombre"></td></tr>
					</table>
					<table class="tabla_menu">
						<tr><td colspan="2"><textarea name="info" id="info" class="info" rows="3" cols="1"><fmt:message key="informacion"/></textarea></td></tr>
						<tr><td colspan="2"><textarea name="descripcion" id="descripcion" class="descripcion" rows="3" cols="1"><fmt:message key="descripcion"/></textarea></td></tr>
						<tr>
							<td rowspan="2">
								<textarea id="direccion0" class="direccion" name="direccion" rows="4" cols="20"><fmt:message key="direccion"/></textarea>
							</td>
							<td class="img_menu">
								<img class="botones" alt="Validar direcci&oacute;n" id="validardireccion0" onclick="validarDireccion(0)"
									 onmouseover="cambiaFlecha(0,0)" onmouseout="cambiaFlecha(1,0)" src="images/iconos/confirm2.png">
							</td>
						</tr>
						<tr>
							<td class="img_menu">
								<img class="botones" alt="Direcci&oacute;n no v&aacute;lida" id="validacion0" src="images/iconos/no.png">
							</td>
						</tr>
					</table>
					<table class="tabla_menu">
						<tr>
							<td colspan="2">
								<input type="hidden" name="iden" id="iden" value="">
								<input type="hidden" name="latitud" id="latitud0" value="">
								<input type="hidden" name="longitud" id="longitud0" value="">
								<input type="hidden" name="idAssigned" id="idAssigned" value="">
							</td>
						</tr>
						<tr id="control">
							<td><label for="estado"><fmt:message key="estado"/>:</label></td>
							<td>
								<select name="estado" id="estado">
									<option id="active" value="active"><fmt:message key="activo"/></option>
									<option id="controlled" value="controlled"><fmt:message key="controlado"/></option>
								</select>
							</td>
						</tr>
						<tr id="size-traffic">
							<td><label for="magnitude"><fmt:message key="tamanno"/>:</label></td>
							<td>
								<select name="magnitude" id="magnitude">
									<option value="small"><fmt:message key="pequenno"/></option>
									<option value="medium"><fmt:message key="mediano"/></option>
									<option value="big"><fmt:message key="grande"/></option>
									<option value="huge"><fmt:message key="enorme"/></option>
								</select>
							</td>
						</tr>
						<tr id="traffic-select">
							<td><label for="traffic0"><fmt:message key="densidadtrafico"/>:</label></td>
							<td>
								<select name="traffic" id="traffic0">
									<option value="low"><fmt:message key="baja"/></option>
									<option value="medium"><fmt:message key="media"/></option>
									<option value="high"><fmt:message key="alta"/></option>
								</select>
							</td>
						</tr>
					</table>
					<p><a href="#" onclick="pinchaMapa(0);" id="pincha"><fmt:message key="marcarmapa"/></a></p>
					<p><input type="button" id="submit0" value="<fmt:message key="modificar"/>" class="btn" onclick="modificar(
							iden.value,cantidad.value,nombre.value,info.value,descripcion.value,direccion.value,
							longitud.value,latitud.value,estado.value,magnitude.value,traffic.value,idAssigned.value);
							borrarFormulario(this.form,1);$('#modificar').jqm().jqmHide();return false;">
					</p>
				</form>
			</div>
			<!-- Cabecera con imagen y hora -->
			<table class="tabla_menu">
				<tr>
					<td>
						<div id="cabecera"><img src="images/<fmt:message key="header"/>.gif" alt=""></div>
					</td>
					<td class="derecha">
						<!--reloj -->
						<div id="reloj">
							<div id="fecha">
								<script type="text/javascript"><fmt:message key="mostrarfecha"/></script>
							</div>
							<div id="Reloj24H"></div>
						</div>
						<!-- minitabs top-right -->
                        <div id="minitabs">
                            <div id="minitab3" class="minitab">
                                <img alt="ver" src="images/tab_simulator.png">
                            </div>
                            <div id="minitab2" class="minitab">
                                <img alt="ver" src="images/tab_building.png">
                            </div>
                            <div id="minitab1" class="minitab">
                                <img alt="más info" src="images/tab_eye.png">
                            </div>
                        </div>
					</td>
				</tr>
			</table>
			<!-- Cuerpo de la pagina -->
			<table class="tabla_menu">
				<tr style="vertical-align:top">
					<td>
						<div id="left">
							<!-- If the user isn't autenticated, we show the login form -->
							<% if (request.getRemoteUser() == null) {%>
							<h2>Sign in</h2>
							<div id="login">
								<form action="<%=response.encodeURL(Constants.LOGIN_FORM_ACTION)%>" method="post" id="loginform">
									<table>
										<tr>
											<td>Usuario:</td>
											<td><input type="text" name="<%=Constants.LOGIN_USERNAME_FIELD%>" id="username"></td>
										</tr>
										<tr>
											<td>Contrase&ntilde;a:</td>
											<td><input type="password" name="<%=Constants.LOGIN_PASSWORD_FIELD%>" id="pwd"></td>
										</tr>
										<tr>
											<td colspan="2"><input type="submit" name="Submit" id="submit_butt" value="Aceptar"></td>
										</tr>
									</table>
								</form>
							</div>
							<% } else { %>
							<!-- and if the user is autenticated, we show the username and logout button -->
							<fmt:message key="eres"/> <span id="signeduser"><%= request.getRemoteUser()%></span>
							<br>
							<a href="logout.jsp">Logout</a>
							<!-- if the user is in role 'administrador' -->
							<div id="dhtmlgoodies_tabView1">
								<div class="dhtmlgoodies_aTab">
									<form id="catastrofes" action="#">
										<table class="tabla_menu">
											<tr>
												<td><input type="hidden" name="marcador" value="event"></td>
												<td><input type="hidden" name="cantidad" value="1"></td>
											</tr>
											<tr>
												<td>
													<input type="radio" name="tipo" value="fire" checked="checked" onclick="cambiaIcono(marcador.value,'fire');">
													<fmt:message key="incendio"/>
												</td>
												<td rowspan="3"><img alt="" id="icono_catastrofes" src="markers/fuego.png" class="rayas"></td>
											</tr>
											<tr>
												<td>
													<input type="radio" name="tipo" value="flood" onclick="cambiaIcono(marcador.value,'flood');">
													<fmt:message key="inundacion"/>
												</td>
											</tr>
											<tr>
												<td>
													<input type="radio" name="tipo" value="collapse" onclick="cambiaIcono(marcador.value,'collapse');">
													<fmt:message key="derrumbamiento"/>
												</td>
											</tr>
										</table>
										<table class="tabla_menu">
											<tr><td colspan="2"><input type="text" name="nombre" class="nombre" value="<fmt:message key="nombre"/>"></td></tr>
											<tr><td colspan="2"><textarea name="info" class="info" rows="3" cols="1"><fmt:message key="informacion"/></textarea></td></tr>
											<tr><td colspan="2"><textarea name="descripcion" class="descripcion" rows="3" cols="1"><fmt:message key="descripcion"/></textarea></td></tr>
											<tr>
												<td rowspan="2">
													<textarea id="direccion1" class="direccion" name="direccion" rows="4" cols="20"><fmt:message key="direccion"/></textarea>
												</td>
												<td class="img_menu">
													<img class="botones" alt="Validar direcci&oacute;n" id="validardireccion1" onclick="validarDireccion(1)"
														 onmouseover="cambiaFlecha(0,1)" onmouseout="cambiaFlecha(1,1)" src="images/iconos/confirm2.png">
												</td>
											</tr>
											<tr><td class="img_menu"><img class="botones" alt="Direcci&oacute;n no v&aacute;lida" id="validacion1" src="images/iconos/no.png"></td></tr>
										</table>
										<!--a href="#" onclick="pinchaMapa(1);">Marcar en mapa</a-->
										<table class="tabla_menu">
											<tr>
												<td><label for="size"><fmt:message key="tamanno"/>:</label></td>
												<td>
													<select name="size" id="size">
														<option value="small" selected="selected"><fmt:message key="pequenno"/></option>
														<option value="medium"><fmt:message key="mediano"/></option>
														<option value="big"><fmt:message key="grande"/></option>
														<option value="huge"><fmt:message key="enorme"/></option>
													</select>
												</td>
											</tr>
											<tr>
												<td><label for="traffic"><fmt:message key="densidadtrafico"/>:</label></td>
												<td>
													<select name="traffic" id="traffic">
														<option value="low" selected="selected"><fmt:message key="baja"/></option>
														<option value="medium"><fmt:message key="media"/></option>
														<option value="high"><fmt:message key="alta"/></option>
													</select>
												</td>
											</tr>
										</table>
										<p>
											<input type="hidden" name="latitud" id="latitud1" value="">
											<input type="hidden" name="longitud" id="longitud1" value="">
											<input type="hidden" name="estado" value="active">
										</p>
										<p>
											<input type="button" id="submit11" value="<fmt:message key="marcarenelmapa"/>" class="btn" onclick="pinchaMapa(1);return false;">
											<input type="button" id="submit12" value="<fmt:message key="annadir"/>" class="btn" onclick="crearCatastrofe(
												marcador.value,seleccionRadio(this.form,0),cantidad.value,nombre.value,info.value,
												descripcion.value,direccion.value,longitud.value,latitud.value,estado.value,size.value,traffic.value,0);
												borrarFormulario(this.form,1);return false;">
										</p>

										<div class="jqmWindow" id="dialog1">
											<p><fmt:message key="puntoalmacenado"/></p>
											<p class="centrado">
												<button onclick="crearCatastrofe(marcador.value,seleccionRadio(this.form,0),cantidad.value,nombre.value,info.value,
													descripcion.value,direccion.value,longitud.value,latitud.value,estado.value,size.value,traffic.value,0);
													$('#dialog1').jqm().jqmHide();borrarFormulario(this.form,1);return false;"><fmt:message key="annadir"/></button>
												<button class="xxx jqmClose"><fmt:message key="cancelar"/></button>
											</p>
										</div>
									</form>
								</div>
								<div class="dhtmlgoodies_aTab">
									<form id="recursos" action="#">
										<table class="tabla_menu">
											<tr><td colspan="2"><input type="hidden" name="marcador" value="resource"></td></tr>
											<tr>
												<td>
													<input type="radio" name="tipo" value="police" checked="checked" onclick="cambiaIcono(marcador.value,'police', cantidad.value);">
													<fmt:message key="policia"/>
												</td>
												<td rowspan="3"><img alt="" id="icono_recursos" src="markers/policia1.png" class="rayas"></td>
											</tr>
											<tr>
												<td>
													<input type="radio" name="tipo" value="firemen" onclick="cambiaIcono(marcador.value,'firemen', cantidad.value);">
													<fmt:message key="bomberos"/>
												</td>
											</tr>
											<tr>
												<td>
													<input type="radio" name="tipo" value="ambulance" onclick="cambiaIcono(marcador.value,'ambulance',cantidad.value);">
													<fmt:message key="ambulancia"/>
												</td>
											</tr>
										</table>
										<table class="tabla_menu">
											<tr>
												<td colspan="2">
													<label for="cantidad"><fmt:message key="numerounidades"/>:</label>
													<select name="cantidad" onchange="cambiaIcono(marcador.value,seleccionRadio(this.form,1),cantidad.value);">
													<option value="1" selected="selected">1</option>
													<option value="2">2</option>
													<option value="3">3</option>
													<option value="4">4</option>
													<option value="5">5</option>
													<option value="6">6</option>
													<option value="7">7</option>
													<option value="8">8</option>
													<option value="9">9</option>
													<option value="10">10</option>
												</select>
												</td>
											</tr>
											<tr><td colspan="2"><input type="text" name="nombre" value="<fmt:message key="unidades"/>" class="nombre"></td></tr>
											<tr><td colspan="2"><textarea name="info" class="info" rows="3" cols="1"><fmt:message key="informacion"/></textarea></td></tr>
											<tr><td colspan="2"><textarea name="descripcion" class="descripcion" rows="3" cols="1"><fmt:message key="descripcion"/></textarea></td></tr>
											<tr>
												<td rowspan="2">
													<textarea id="direccion2" name="direccion" class="direccion" rows="4" cols="20"><fmt:message key="direccion"/></textarea>
												</td>
												<td class="img_menu">
													<img class="botones" alt="Validar direcci&oacute;n" id="validardireccion2" onclick="validarDireccion(2)"
														 onmouseover="cambiaFlecha(0,2)" onmouseout="cambiaFlecha(1,2)" src="images/iconos/confirm2.png">
												</td>
											</tr>
											<tr>
												<td class="img_menu">
													<img class="botones" alt="Direcci&oacute;n no v&aacute;lida" id="validacion2" src="images/iconos/no.png">
												</td>
											</tr>
										</table>
										<p>
											<input type="hidden" name="latitud" id="latitud2" value="">
											<input type="hidden" name="longitud" id="longitud2" value="">
											<input type="hidden" name="estado" value="active">
											<input type="hidden" name="magnitude" id="magnitude2" value="">
											<input type="hidden" name="traffic" id="traffic2" value="">
										</p>
										<p>
											<input type="button" id="submit21" value="<fmt:message key="marcarenelmapa"/>" class="btn" onclick="pinchaMapa(2);return false;">
											<input type="button" id="submit22" value="<fmt:message key="annadir"/>" class="btn" onclick="crearCatastrofe(
												marcador.value,seleccionRadio(this.form,1),cantidad.value,nombre.value,info.value,
												descripcion.value,direccion.value,longitud.value,latitud.value,estado.value,magnitude.value,traffic.value,0);
												borrarFormulario(this.form,2);return false;">
										</p>

										<div class="jqmWindow" id="dialog2">
											<p><fmt:message key="puntoalmacenado"/></p>
											<p class="centrado">
												<button onclick="crearCatastrofe(marcador.value,seleccionRadio(this.form,1),cantidad.value,nombre.value,info.value,
													descripcion.value,direccion.value,longitud.value,latitud.value,estado.value,magnitude.value,traffic.value,0);
													$('#dialog2').jqm().jqmHide();borrarFormulario(this.form,2);return false;"><fmt:message key="annadir"/></button>
												<button class="xxx jqmClose"><fmt:message key="cancelar"/></button>
											</p>
										</div>
									</form>
								</div>
								<div class="dhtmlgoodies_aTab">
									<form id="heridos" action="#">
										<table class="tabla_menu">
											<tr><td colspan="2"><input type="hidden" name="marcador" value="people"></td></tr>
											<tr>
												<td>
													<input type="radio" name="tipo" value="slight" checked="checked" onclick="cambiaIcono(marcador.value,'slight',cantidad.value);">
													<fmt:message key="leves"/>
												</td>
												<td rowspan="4"><img alt="" id="icono_heridos" src="markers/leve1.png" class="rayas"></td>
											</tr>
											<tr>
												<td>
													<input type="radio" name="tipo" value="serious" onclick="cambiaIcono(marcador.value,'serious',cantidad.value);">
													<fmt:message key="graves"/>
												</td>
											</tr>
											<tr>
												<td>
													<input type="radio" name="tipo" value="dead" onclick="cambiaIcono(marcador.value,'dead',cantidad.value);">
													<fmt:message key="muertos"/>
												</td>
											</tr>
											<tr>
												<td>
													<input type="radio" name="tipo" value="trapped" onclick="cambiaIcono(marcador.value,'trapped',cantidad.value);">
													<fmt:message key="atrapados"/>
												</td>
											</tr>
										</table>
										<table class="tabla_menu">
											<tr>
												<td colspan="2">
													<label for="cantidad"><fmt:message key="numeropersonas"/>:</label>
													<select name="cantidad" onchange="cambiaIcono(marcador.value,seleccionRadio(this.form,2),cantidad.value);">
														<option value="1" selected="selected">1</option>
														<option value="2">2</option>
														<option value="3">3</option>
														<option value="4">4</option>
														<option value="5">5</option>
														<option value="6">6</option>
														<option value="7">7</option>
														<option value="8">8</option>
														<option value="9">9</option>
														<option value="10">10</option>
													</select>
												</td>
											</tr>
											<tr><td colspan="2"><input type="text" name="nombre" class="nombre" value="<fmt:message key="personas"/>"></td></tr>
											<tr><td colspan="2"><textarea name="info" class="info" rows="3" cols="1"><fmt:message key="nombres"/></textarea></td></tr>
											<tr><td colspan="2"><textarea name="descripcion" class="descripcion" rows="3" cols="1"><fmt:message key="descripciongravedad"/></textarea></td></tr>
											<tr>
												<td rowspan="2">
													<textarea id="direccion3" name="direccion" class="direccion" rows="4" cols="20"><fmt:message key="direccion"/></textarea>
												</td>
												<td class="img_menu">
													<img class="botones" alt="Validar direcci&oacute;n" id="validardireccion3" onclick="validarDireccion(3)"
														 onmouseover="cambiaFlecha(0,3)" onmouseout="cambiaFlecha(1,3)" src="images/iconos/confirm2.png">
												</td>
											</tr>
											<tr>
												<td class="img_menu">
													<img class="botones" alt="Direcci&oacute;n no v&aacute;lida" id="validacion3" src="images/iconos/no.png">
												</td>
											</tr>
										</table>
										<p>
											<input type="hidden" name="latitud" id="latitud3" value="">
											<input type="hidden" name="longitud" id="longitud3" value="">
											<input type="hidden" name="estado" value="active">
											<input type="hidden" name="magnitude" id="magnitude3" value="">
											<input type="hidden" name="traffic" id="traffic3" value="">
										</p>
										<p>
											<input type="button" id="submit31" value="<fmt:message key="marcarenelmapa"/>" class="btn" onclick="pinchaMapa(3);return false;">
											<input type="button" id="submit32" value="<fmt:message key="annadir"/>" class="btn" onclick="crearCatastrofe(
												marcador.value,seleccionRadio(this.form,2),cantidad.value,nombre.value,info.value,
												descripcion.value,direccion.value,longitud.value,latitud.value,estado.value,magnitude.value,traffic.value,0);
												borrarFormulario(this.form,3);return false;">
										</p>
										<!--a href="#" onclick="pinchaMapa(3)">Marcar en mapa</a-->
										<div class="jqmWindow" id="dialog3">
											<p><fmt:message key="puntoalmacenado"/></p>
											<p class="centrado">
												<button onclick="crearCatastrofe(marcador.value,seleccionRadio(this.form,2),cantidad.value,nombre.value,info.value,
													descripcion.value,direccion.value,longitud.value,latitud.value,estado.value,magnitude.value,traffic.value,0);
													$('#dialog3').jqm().jqmHide();borrarFormulario(this.form,3);return false;"><fmt:message key="annadir"/></button>
												<button class="xxx jqmClose"><fmt:message key="cancelar"/></button>
											</p>
										</div>
									</form>
								</div>
								<div class="dhtmlgoodies_aTab">
									<form id="experto" action="#">
										<p><fmt:message key="explicacionexperto"/></p>
										<p>
											<input type="button" id="Start" value="<fmt:message key="comenzar"/>" class="btn" onclick="lanzaExperto(); return false;">
											<input type="button" id="Stop" value="<fmt:message key="parar"/>" class="btn" onclick="stopExperto(); return false;">
										</p>
									</form>
								</div>
							</div>
							<!--aqui se cambia el tamanno y titulo de las tabs -->
							<script type="text/javascript">
								initTabs('dhtmlgoodies_tabView1',Array('<fmt:message key="eventos"/>','<fmt:message key="recursos"/>','<fmt:message key="victimas"/>','<fmt:message key="experto"/>'),0,228,440);
							</script>
							<% } %>
						</div>
					</td>
					<td id="fila_mapa">
						<div id="right">
							<div id="map_canvas"></div>
						</div>
					</td>
				</tr>
			</table>
			<!-- Menus from minitabs -->
            <div id="console" class="slideMenu"></div>
            <div id="visualize" class="slideMenu">
                <form action="#" id="buildings">
					<table>
						<tr>
							<td colspan="2"><fmt:message key="edificios"/></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="hospital" value="hospital" checked="checked" onchange="visualize(this.checked,'hospital');"></td>
							<td><fmt:message key="hospitales"/></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="policeStation" value="policeStation" checked="checked" onchange="visualize(this.checked,'policeStation');"></td>
							<td><fmt:message key="comisarias"/></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="firemenStation" value="firemenStation" checked="checked" onchange="visualize(this.checked,'firemenStation');"></td>
							<td><fmt:message key="parquesbomberos"/></td>
						</tr>
						<tr>
							<td colspan="2"><a id="hideVisualize" href="#"><fmt:message key="ocultar"/></a></td>
						</tr>
					</table>
                </form>
            </div>
            <div id="showSimOptions" class="slideMenu">
                <form id="SimOptions" method="post" action="/disasters/RunSimulation">
					<table>
						<tr>
							<td><p class="bigger"><fmt:message key="opcionesSimulador"/></td>
						</tr>
						<tr>
							<td><input id="runSim" type="radio" name="sim" value="run" checked="checked"><fmt:message key="arrancaSimulador"/></td>
						</tr>
						<tr id="options2">
							<td>
								<fmt:message key="victimas"/> <input type="text" name="victims0" size="3">
								<br>
								<fmt:message key="incendios"/> <input type="text" name="fires0" size="3">
							</td>
						</tr>
						<tr>
							<td><input id="restartSim" type="radio" name="sim" value="restart"><fmt:message key="reiniciarSimulador"/></td>
						</tr>
						<tr>
							<td><input id="pauseSim" type="radio" name="sim" value="pause"><fmt:message key="pause"/></td>
						</tr>
						<tr>
							<td><input id="submit_simulador" type="submit" name="aceptar" value="<fmt:message key="aceptar"/>"></td>
						</tr><tr>
							<td><a id="hideSimOptions" href="#"><fmt:message key="ocultar"/></a></td>
						</tr>
					</table>
                </form>
            </div>
			<!-- Screen for the servlet information -->
			<div id="close_screen"><a href="#"><fmt:message key="ocultar"/></a></div>
			<div id="screen"></div>
			<p class="iconos_validacion">
				<a href="http://validator.w3.org/check?uri=referer">
					<img src="http://www.w3.org/Icons/valid-html401" alt="Valid HTML 4.01 Strict">
				</a>
				<a href="http://jigsaw.w3.org/css-validator/check/referer">
					<img src="http://jigsaw.w3.org/css-validator/images/vcss" alt="&iexcl;CSS V&aacute;lido!">
				</a>
			</p>
			<jsp:useBean class="roads.DirectionsBean" id="resources" scope="session"/>
			<%
				int[] rscs = resources.getResourcesList();
				for (int i = 0; i < rscs.length; i++) {
					String st = "<input type=\"hidden\" id=\"start" + rscs[i] + "\">";
					out.println(st);
					String ed = "<input type=\"hidden\" id=\"end" + rscs[i] + "\"><br>";
					out.println(ed);
				}
			%>
			<c:if test="${param.alert == true}">
				<script type="text/javascript">
					window.alert("Fin de la simulacion.")
				</script>
			</c:if>
		</body>
	</fmt:bundle>
</html>