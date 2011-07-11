<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean class="gsi.proyect.ProyectBean" id="proyecto" scope="session"/>

<%@ include file="info_caronte.jsp" %>
<fmt:bundle basename="fmt.eji8n">
	<div id="dhtmlgoodies_tabView1_1">
		<div class="dhtmlgoodies_aTab">
			<form id="catastrofes" action="#">
				<table class="tabla_menu">
					<tr>
						<td><input type="hidden" name="marcador" value="event"></td>
						<td><input type="hidden" name="cantidad" value="1"></td>
					</tr>
					<tr>
						<th><fmt:message key="internos"/></th>
						<td rowspan="7"><img alt="" id="icono_catastrofes" src="markers/fuego.png" class="rayas"></td>
					</tr>
					<tr>
						<td>
							<input type="radio" name="tipo" value="fire" checked="checked" onclick="cambiaIcono(marcador.value,'fire');">
							<fmt:message key="incendio"/> <%--(${fires.rowCount})--%>
						</td>
					</tr>
					<tr>
						<td>
							<input type="radio" name="tipo" value="flood" onclick="cambiaIcono(marcador.value,'flood');">
							<fmt:message key="inundacion"/> <%--(${floods.rowCount})--%>
						</td>
					</tr>
					<tr>
						<td>
							<input type="radio" name="tipo" value="collapse" onclick="cambiaIcono(marcador.value,'collapse');">
							<fmt:message key="derrumbamiento"/> <%--(${collapses.rowCount})--%>
						</td>
					</tr>
					<tr>
						<td>
							<input type="radio" name="tipo" value="injuredPerson" onclick="cambiaIcono(marcador.value,'injuredPerson');">
							<fmt:message key="personaherida"/> <%--(${injuredPeople.rowCount})--%>
						</td>
					</tr>
					<tr>
						<th><fmt:message key="externos"/></th>
					</tr>
					<tr>
						<td>
							<input type="radio" name="tipo" value="lostPerson" onclick="cambiaIcono(marcador.value,'lostPerson');">
							<fmt:message key="personaperdida"/> <%--(${lostPeople.rowCount})--%>
						</td>
					</tr>
				</table>
				<table class="tabla_menu">
					<tr><td colspan="2"><input type="text" name="nombre" class="nombre" value="" placeholder="<fmt:message key="nombre"/>"></td></tr>
					<tr><td colspan="2"><textarea name="info" class="info" rows="3" cols="1" placeholder="<fmt:message key="informacion"/>"></textarea></td></tr>
					<tr><td colspan="2"><textarea name="descripcion" class="descripcion" rows="3" cols="1" placeholder="<fmt:message key="descripcion"/>"></textarea></td></tr>
					<tr>
						<td rowspan="2">
							<textarea id="direccion1" class="direccion" name="direccion" rows="4" cols="20" placeholder="<fmt:message key="direccion"/>"></textarea>
						</td>
						<td class="img_menu">
							<img class="botones" alt="Validar direcci&oacute;n" id="validardireccion1" onclick="validarDireccion(1)"
								 onmouseover="cambiaFlecha(0,1)" onmouseout="cambiaFlecha(1,1)" src="images/iconos/confirm2.png">
						</td>
					</tr>
					<tr><td class="img_menu"><img class="botones" alt="Direcci&oacute;n no v&aacute;lida" id="validacion1" src="images/iconos/no.png"></td></tr>
				</table>
				<!--a href="#" onclick="pinchaMapa(1);">Marcar en mapa</a-->
				<br>
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
					<input type="hidden" name="iden" id="iden1" value="">
					<input type="hidden" name="latitud" id="latitud1" value="">
					<input type="hidden" name="longitud" id="longitud1" value="">
					<input type="hidden" name="estado" value="active">
				</p>
				<p>
					<input type="button" id="submit11" value="<fmt:message key="marcarenelmapa"/>" class="btn" onclick="pinchaMapa(1);return false;">
					<!--<input type="button" id="submit12" value="<fmt:message key="annadir"/>" class="btn" onclick="crearCatastrofe(
						marcador.value,seleccionRadio(this.form,0),cantidad.value,nombre.value,info.value,
						descripcion.value,direccion.value,longitud.value,latitud.value,estado.value,size.value,traffic.value,0);
						borrarFormulario(this.form,1);return false;">-->
					<input type="button" id="submit10" value="<fmt:message key="modificar"/>" class="btn" style="display:none;" onclick="modificar2(
						iden.value,seleccionRadio(this.form,0),cantidad.value,nombre.value,info.value,descripcion.value,
						direccion.value,longitud.value,latitud.value,estado.value,size.value,traffic.value);return false;">
					<input type="button" id="eliminar1" value="Eliminar" class="btn" style="display:none;" onclick="eliminar(marcadores_definitivos[iden.value],DEFINITIVO);">
				</p>
				<div class="jqmWindow" id="dialog1">
					<p>¿Confirma añadir el marcador en el mapa?<!--<fmt:message key="puntoalmacenado"/>--></p>
					<p class="centrado">
						<button onclick="crearCatastrofe('event',seleccionRadio(this.form,0),cantidad.value,nombre.value,info.value,
							descripcion.value,direccion.value,longitud.value,latitud.value,estado.value,size.value,traffic.value,0);
							$('#dialog1').jqm().jqmHide();return false;"><fmt:message key="annadir"/></button>
						<button class="xxx jqmClose"><fmt:message key="cancelar"/></button>
					</p>
				</div>
			</form>
		</div>
		<div class="dhtmlgoodies_aTab">
			<form id="heridos" action="#">
				<table class="tabla_menu" id="radioNo">
					<tr><td colspan="2"><input type="hidden" name="marcador" value="people"></td></tr>
					<tr>
						<td>
							<fmt:message key="sanos"/> <%--(${healthy.rowCount})--%>
						</td>
						<td><img alt="" id="icono_residentes" src="markers/sano1.png" class="rayas"></td>
					</tr>
				</table>
				<table class="tabla_menu" id="radioMod" style="display:none">
					<tr><td colspan="2"><input type="hidden" name="marcador" value="people"></td></tr>
					<tr>
						<td>
							<input type="radio" name="tipo" value="healthy" checked="checked" onclick="cambiaIcono('people','healthy',1);">
							<fmt:message key="sanos"/> <%--(${healthy.rowCount})--%>
						</td>
						<td rowspan="5"><img alt="" id="icono_heridos" src="markers/sano1.png" class="rayas"></td>
					</tr>
					<tr>
						<td>
							<input type="radio" name="tipo" value="slight" onclick="cambiaIcono('people','slight',1);">
							<fmt:message key="leves"/> <%--(${slight.rowCount})--%>
						</td>
					</tr>
					<tr>
						<td>
							<input type="radio" name="tipo" value="serious" onclick="cambiaIcono('people','serious',1);">
							<fmt:message key="graves"/> <%--(${serious.rowCount})--%>
						</td>
					</tr>
					<tr>
						<td>
							<input type="radio" name="tipo" value="dead" onclick="cambiaIcono('people','dead',1);">
							<fmt:message key="muertos"/> <%--(${dead.rowCount})--%>
						</td>
					</tr>
					<tr>
						<td>
							<input type="radio" name="tipo" value="trapped" onclick="cambiaIcono('people','trapped',1);">
							<fmt:message key="atrapados"/> <%--(${trapped.rowCount})--%>
						</td>
					</tr>
				</table>
				<table class="tabla_menu">
					<tr id="sintomas" style="display:none">
						<!--<td colspan="2">
							S&iacute;ntomas:<br>
							<input type="checkbox" name="fatigue">Fatiga
							<input type="checkbox" name="fever">Fiebre
							<input type="checkbox" name="dyspnea">Disnea
							<input type="checkbox" name="nausea">Nauseas
							<input type="checkbox" name="headache">Dolor de cabeza
							<input type="checkbox" name="vomiting">V&oacute;mitos
							<input type="checkbox" name="abdominal_pain">Dolor abdominal
							<input type="checkbox" name="weight_loss">Perdida de peso
							<input type="checkbox" name="blurred_vision">Visi&oacute;n borrosa
							<input type="checkbox" name="muscle_weakness">Debilidad muscular
						</td>-->
						<td colspan="2">
							<input type="hidden" name="fatigue">
							<input type="hidden" name="fever">
							<input type="hidden" name="dyspnea">
							<input type="hidden" name="nausea">
							<input type="hidden" name="headache">
							<input type="hidden" name="vomiting">
							<input type="hidden" name="abdominal_pain">
							<input type="hidden" name="weight_loss">
							<input type="hidden" name="blurred_vision">
							<input type="hidden" name="muscle_weakness">
						</td>
					</tr>
					<tr><td colspan="2"><input type="text" name="nombre" class="nombre" value="" placeholder="<fmt:message key="nombre"/>"></td></tr>
					<tr><td colspan="2"><textarea name="info" class="info" rows="3" cols="1" placeholder="<fmt:message key="informacion"/>"></textarea></td></tr>
					<tr><td colspan="2"><textarea name="descripcion" class="descripcion" rows="3" cols="1" placeholder="<fmt:message key="descripcion"/>"></textarea></td></tr>
					<tr>
						<td rowspan="2">
							<textarea id="direccion2" name="direccion" class="direccion" rows="4" cols="20" placeholder="<fmt:message key="direccion"/>"></textarea>
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
					<input type="hidden" name="iden" value="">
					<input type="hidden" name="latitud" id="latitud2" value="">
					<input type="hidden" name="longitud" id="longitud2" value="">
					<input type="hidden" name="estado" value="active">
					<input type="hidden" name="size" id="magnitude2" value="">
					<input type="hidden" name="traffic" id="traffic2" value="">
					<input type="hidden" name="cantidad" value="1">
				</p>
				<p>
					<input type="button" id="submit21" value="<fmt:message key="marcarenelmapa"/>" class="btn" onclick="pinchaMapa(2);return false;">
					<input type="button" id="submit20" value="<fmt:message key="modificar"/>" class="btn" style="display:none;" onclick="modificar2(
						iden.value,seleccionRadio(this.form,2),cantidad.value,nombre.value,info.value,descripcion.value,
						direccion.value,longitud.value,latitud.value,estado.value,size.value,traffic.value);return false;">
					<input type="button" id="eliminar2" value="Eliminar" class="btn" style="display:none;" onclick="eliminar(marcadores_definitivos[iden.value],DEFINITIVO);">
				</p>

				<div class="jqmWindow" id="dialog2">
					<p>¿Confirma añadir el marcador en el mapa?<!--<fmt:message key="puntoalmacenado"/>--></p>
					<p class="centrado">
						<button onclick="crearCatastrofe('people',seleccionRadio(this.form,2),cantidad.value,nombre.value,info.value,
							descripcion.value,direccion.value,longitud.value,latitud.value,estado.value,size.value,traffic.value,0);
							$('#dialog2').jqm().jqmHide();return false;"><fmt:message key="annadir"/></button>
						<button class="xxx jqmClose"><fmt:message key="cancelar"/></button>
					</p>
				</div>
			</form>
		</div>
		<div class="dhtmlgoodies_aTab">
			<table class="tabla_menu">
				<tr>
					<td><fmt:message key="enfermero"/> <%--(${nurse.rowCount})--%></td>
					<td><img alt="" src="markers/enfermero1.png" class="rayas"></td>
				</tr>
				<tr>
					<td><fmt:message key="gerocultor"/> <%--(${gerocultor.rowCount})--%></td>
					<td><img alt="" src="markers/gerocultor1.png" class="rayas"></td>
				</tr>
				<tr>
					<td><fmt:message key="auxiliar"/> <%--(${assistant.rowCount})--%></td>
					<td><img alt="" src="markers/auxiliar1.png" class="rayas"></td>
				</tr>
				<tr><td colspan="2"><hr></td></tr>
				<tr>
					<td><fmt:message key="policia"/> <%--(${policemen.rowCount})--%></td>
					<td><img alt="" src="markers/policia1.png" class="rayas"></td>
				</tr>
				<tr>
					<td><fmt:message key="bomberos"/> <%--(${firemen.rowCount})--%></td>
					<td><img alt="" src="markers/bombero1.png" class="rayas"></td>
				</tr>
				<tr>
					<td><fmt:message key="ambulancia"/> <%--(${ambulance.rowCount})--%></td>
					<td><img alt="" src="markers/ambulancia1.png" class="rayas"></td>
				</tr>
			</table>
		</div>

		<%--<div class="dhtmlgoodies_aTab">
			<form id="experto" action="#">
				<p><fmt:message key="explicacionexperto"/></p>
				<p>
					<input type="button" id="Start" value="<fmt:message key="comenzar"/>" class="btn" onclick="lanzaExperto(); return false;">
					<input type="button" id="Stop" value="<fmt:message key="parar"/>" class="btn" onclick="stopExperto(); return false;">
				</p>
			</form>
			<hr>
			<form id="SimOptions" method="post" action="/desastres/RunSimulation">
				<p class="bigger"><fmt:message key="opcionessimulador"/></p>
				<p><input id="runSim" type="radio" name="sim" value="run" checked="checked"><fmt:message key="arrancasimulador"/></p>
				<div id="options2">
					<table>
						<tr>
							<td><fmt:message key="victimas"/></td>
							<td><input type="text" name="victims0" size="5"></td>
						</tr>
						<tr>
							<td><fmt:message key="incendios"/></td>
							<td><input type="text" name="fires0" size="5"></td>
						</tr>
					</table>
				</div>
				<input type="hidden" name="proyectName" value="caronte">
				<p><input id="restartSim" type="radio" name="sim" value="restart"><fmt:message key="reiniciarsimulador"/></p>
				<p><input id="pauseSim" type="radio" name="sim" value="pause"><fmt:message key="pause"/></p>
				<p><input id="submit_simulador" type="submit" name="aceptar" value="<fmt:message key="aceptar"/>"></p>
			</form>
		</div>--%>
	</div>
	<!--aqui se cambia el tamanno y titulo de las tabs -->
	<script type="text/javascript">
		initTabs('dhtmlgoodies_tabView1_1',Array('<fmt:message key="eventos"/>','Residentes','<fmt:message key="recursos"/>'),0,254,490);
	</script>
</fmt:bundle>