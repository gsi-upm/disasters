<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean class="gsi.proyect.ProyectBean" id="proyecto" scope="session"/>

<%@ include file="info_caronte.jsp" %>
<fmt:bundle basename="fmt.eji8n">
	<div id="dhtmlgoodies_tabView1">
		<div class="dhtmlgoodies_aTab">
			<form id="catastrofes" action="#">
				<table class="tabla_menu">
					<tr>
						<td>
							<input type="radio" name="tipo" value="fire" checked="checked" onclick="cambiaIcono('event','fire');"/>
							<fmt:message key="incendio"/> <%--(${fires.rowCount})--%>
						</td>
						<td rowspan="5"><img id="icono_catastrofes" class="rayas" alt="" src="markers/fuego.png"/></td>
					</tr>
					<tr>
						<td>
							<input type="radio" name="tipo" value="flood" onclick="cambiaIcono('event','flood');"/>
							<fmt:message key="inundacion"/> <%--(${floods.rowCount})--%>
						</td>
					</tr>
					<tr>
						<td>
							<input type="radio" name="tipo" value="collapse" onclick="cambiaIcono('event','collapse');"/>
							<fmt:message key="derrumbamiento"/> <%--(${collapses.rowCount})--%>
						</td>
					</tr>
					<tr>
						<td>
							<input type="radio" name="tipo" value="injuredPerson" onclick="cambiaIcono('event','injuredPerson');"/>
							<fmt:message key="personaherida"/> <%--(${injuredPeople.rowCount})--%>
						</td>
					</tr>
					<tr>
						<td>
							<input type="radio" name="tipo" value="lostPerson" onclick="cambiaIcono('event','lostPerson');"/>
							<fmt:message key="personaperdida"/> <%--(${lostPeople.rowCount})--%>
						</td>
					</tr>
				</table>
				<table class="tabla_menu">
					<tr><td colspan="2"><input type="text" name="nombre" class="nombre" value="Incendio" placeholder="<fmt:message key="nombre"/>"/></td></tr>
					<tr><td colspan="2"><textarea name="info" class="info" rows="3" cols="1" placeholder="<fmt:message key="informacion"/>"></textarea></td></tr>
					<tr><td colspan="2"><textarea name="descripcion" class="descripcion" rows="3" cols="1" placeholder="<fmt:message key="descripcion"/>"></textarea></td></tr>
					<tr>
						<td rowspan="2">
							<textarea id="direccion1" class="direccion" name="direccion" rows="4" cols="20" placeholder="<fmt:message key="direccion"/>"></textarea>
						</td>
						<td class="img_menu">
							<img class="botones" alt="Validar direcci&oacute;n" id="validardireccion1" onclick="validarDireccion(1)"
								 onmouseover="cambiaFlecha(0,1)" onmouseout="cambiaFlecha(1,1)" src="images/iconos/confirm2.png"/>
						</td>
					</tr>
					<tr><td class="img_menu"><img class="botones" alt="Direcci&oacute;n no v&aacute;lida" id="validacion1" src="images/iconos/no.png"/></td></tr>
				</table>
				<table class="tabla_menu">
					<tr>
						<td><fmt:message key="planta"/></td>
						<td>
							<select name="planta" id="select-planta1">
								<option value="-2" selected="selected"><fmt:message key="visionGeneral"/></option>
								<option value="-1"><fmt:message key="exterior"/></option>
								<option value="0"><fmt:message key="planta"/> 0</option>
								<option value="1"><fmt:message key="planta"/> 1</option>
								<option value="2"><fmt:message key="planta"/> 2</option>
							</select>
						</td>
					</tr>
					<tr>
						<td><label for="tamanno"><fmt:message key="tamanno"/>:</label></td>
						<td>
							<select name="tamanno" id="tamanno">
								<option value="small" selected="selected"><fmt:message key="pequenno"/></option>
								<option value="medium"><fmt:message key="mediano"/></option>
								<option value="big"><fmt:message key="grande"/></option>
								<option value="huge"><fmt:message key="enorme"/></option>
							</select>
						</td>
					</tr>
					<tr>
						<td><label for="trafico"><fmt:message key="densidadtrafico"/>:</label></td>
						<td>
							<select name="trafico" id="trafico">
								<option value="low" selected="selected"><fmt:message key="baja"/></option>
								<option value="medium"><fmt:message key="media"/></option>
								<option value="high"><fmt:message key="alta"/></option>
							</select>
						</td>
					</tr>
				</table>
				<input type="hidden" name="iden" id="iden1" value=""/>
				<input type="hidden" name="latitud" id="latitud1" value=""/>
				<input type="hidden" name="longitud" id="longitud1" value=""/>
				<input type="button" id="submit11" value="<fmt:message key="marcarenelmapa"/>" class="btn" onclick="pinchaMapa(1);return false;"/>
				<br/>
				<input type="button" id="submit10" value="<fmt:message key="modificar"/>" class="btn oculto" onclick="modificar2(
					iden.value,seleccionRadio(this.form,0),1,nombre.value,info.value,descripcion.value,
					direccion.value,tamanno.value,trafico.value,null,planta.value);return false;"/>
				<input type="button" id="eliminar1" value="Eliminar" class="btn oculto" onclick="eliminar(marcadores_definitivos[iden.value],DEFINITIVO);"/>
				<div class="jqmWindow" id="dialog1">
					<p><fmt:message key="confirmarMarcador"/><%--<fmt:message key="puntoalmacenado"/>--%></p>
					<p class="centrado">
						<button onclick="crearCatastrofe('event',seleccionRadio(this.form,0),1,nombre.value,info.value,
							descripcion.value,direccion.value,longitud.value,latitud.value,'active',tamanno.value,trafico.value,0,planta.value);
							$('#dialog1').jqm().jqmHide();return false;"><fmt:message key="annadir"/></button>
						<button class="xxx jqmClose"><fmt:message key="cancelar"/></button>
					</p>
				</div>
			</form>
		</div>
		<div class="dhtmlgoodies_aTab">
			<form id="heridos" action="#">
				<table class="tabla_menu">
					<tr>
						<td>
							<input type="radio" name="tipo" value="healthy" checked="checked" onclick="cambiaIcono('people','healthy',1);"/>
							<fmt:message key="sanos"/> <%--(${healthy.rowCount})--%>
						</td>
						<td rowspan="5"><img alt="" id="icono_heridos" src="markers/sano1.png" class="rayas"/></td>
					</tr>
					<tr>
						<td>
							<input type="radio" name="tipo" value="slight" onclick="cambiaIcono('people','slight',1);"/>
							<fmt:message key="leves"/> <%--(${slight.rowCount})--%>
						</td>
					</tr>
					<tr>
						<td>
							<input type="radio" name="tipo" value="serious" onclick="cambiaIcono('people','serious',1);"/>
							<fmt:message key="graves"/> <%--(${serious.rowCount})--%>
						</td>
					</tr>
					<tr>
						<td>
							<input type="radio" name="tipo" value="dead" onclick="cambiaIcono('people','dead',1);"/>
							<fmt:message key="muertos"/> <%--(${dead.rowCount})--%>
						</td>
					</tr>
					<tr>
						<td>
							<input type="radio" name="tipo" value="trapped" onclick="cambiaIcono('people','trapped',1);"/>
							<fmt:message key="atrapados"/> <%--(${trapped.rowCount})--%>
						</td>
					</tr>
				</table>
				<div id="asociacionesEmergencias">
					<span id="textoAsoc"></span>
					<ul>
						<fmt:message key="asociado"/>:
						<span id="checkboxAsoc"></span>
					</ul>
					<div id="selectAsoc"></div>
				</div>
				<table class="tabla_menu">
					<tr>
						<td><fmt:message key="planta"/></td>
						<td>
							<select name="planta" id="select-planta2">
								<option value="-2" selected="selected"><fmt:message key="visionGeneral"/></option>
								<option value="-1"><fmt:message key="exterior"/></option>
								<option value="0"><fmt:message key="planta"/> 0</option>
								<option value="1"><fmt:message key="planta"/> 1</option>
								<option value="2"><fmt:message key="planta"/> 2</option>
							</select>
						</td>
					</tr>
					<tr>
						<td><fmt:message key="peso"/></td>
						<td>
							<select name="peso" id="peso">
								<option value="indefinido"><fmt:message key="pesoIndefinido"/></option>
								<option value="bajo"><fmt:message key="pesoBajo"/></option>
								<option value="medio"><fmt:message key="pesoMedio"/></option>
								<option value="alto"><fmt:message key="pesoAlto"/></option>
							</select>
						</td>
					</tr>
					<tr>
						<td><fmt:message key="movilidad"/></td>
						<td>
							<select name="movilidad" id="movilidad">
								<option value="indefinida"><fmt:message key="movIndefinida"/></option>
								<option value="normal"><fmt:message key="movNormal"/></option>
								<option value="reducida"><fmt:message key="movReducida"/></option>
								<option value="asistida"><fmt:message key="movAsistida"/></option>
								<option value="inconsciente"><fmt:message key="movInconsciente"/></option>
							</select>
						</td>
					</tr>
				</table>
				<table class="tabla_menu">
					<tr id="sintomas">
						<td colspan="2">
							<!--S&iacute;ntomas:<br/>-->
							<input type="hidden" name="fatigue"/> <!--<input type="checkbox" name="fatigue"/>Fatiga-->
							<input type="hidden" name="fever"/> <!--<input type="checkbox" name="fever"/>Fiebre-->
							<input type="hidden" name="dyspnea"/> <!--<input type="checkbox" name="dyspnea"/>Disnea-->
							<input type="hidden" name="nausea"/> <!--<input type="checkbox" name="nausea"/>Nauseas-->
							<input type="hidden" name="headache"/> <!--<input type="checkbox" name="headache"/>Dolor de cabeza-->
							<input type="hidden" name="vomiting"/> <!--<input type="checkbox" name="vomiting"/>V&oacute;mitos-->
							<input type="hidden" name="abdominal_pain"/> <!--<input type="checkbox" name="abdominal_pain"/>Dolor abdominal-->
							<input type="hidden" name="weight_loss"/> <!--<input type="checkbox" name="weight_loss"/>Perdida de peso-->
							<input type="hidden" name="blurred_vision"/> <!--<input type="checkbox" name="blurred_vision"/>Visi&oacute;n borrosa-->
							<input type="hidden" name="muscle_weakness"/> <!--<input type="checkbox" name="muscle_weakness"/>Debilidad muscular-->
						</td>
					</tr>
					<tr><td colspan="2"><input type="text" name="nombre" class="nombre" value="Sano" placeholder="<fmt:message key="nombre"/>"/></td></tr>
					<tr><td colspan="2"><textarea name="info" class="info" rows="3" cols="1" placeholder="<fmt:message key="informacion"/>"></textarea></td></tr>
					<tr><td colspan="2"><textarea name="descripcion" class="descripcion" rows="3" cols="1" placeholder="<fmt:message key="descripcion"/>"></textarea></td></tr>
					<tr>
						<td rowspan="2">
							<textarea id="direccion2" name="direccion" class="direccion" rows="4" cols="20" placeholder="<fmt:message key="direccion"/>"></textarea>
						</td>
						<td class="img_menu">
							<img class="botones" alt="Validar direcci&oacute;n" id="validardireccion2" onclick="validarDireccion(2)"
								 onmouseover="cambiaFlecha(0,2)" onmouseout="cambiaFlecha(1,2)" src="images/iconos/confirm2.png"/>
						</td>
					</tr>
					<tr>
						<td class="img_menu">
							<img class="botones" alt="Direcci&oacute;n no v&aacute;lida" id="validacion2" src="images/iconos/no.png"/>
						</td>
					</tr>
				</table>
				<input type="hidden" name="iden" value=""/>
				<input type="hidden" name="latitud" id="latitud2" value=""/>
				<input type="hidden" name="longitud" id="longitud2" value=""/>
				<input type="button" id="submit21" value="<fmt:message key="marcarenelmapa"/>" class="btn" onclick="pinchaMapa(2); return false;"/>
				<br/>
				<input type="button" id="submit20" value="<fmt:message key="modificar"/>" class="btn oculto"
					onclick="modificar2(iden.value,seleccionRadio(this.form,2),1,nombre.value,info.value,descripcion.value,
					direccion.value,peso.value,movilidad.value,0,planta.value); return false;"/>
				<input type="button" id="eliminar2" value="Eliminar" class="btn oculto" onclick="eliminar(marcadores_definitivos[iden.value],DEFINITIVO);"/>
				<div class="jqmWindow" id="dialog2">
					<p><fmt:message key="confirmarMarcador"/><%--<fmt:message key="puntoalmacenado"/>--%></p>
					<p class="centrado">
						<button onclick="crearCatastrofe('people',seleccionRadio(this.form,2),1,nombre.value,info.value,
							descripcion.value,direccion.value,longitud.value,latitud.value,'active',peso.value,movilidad.value,0,planta.value);
							$('#dialog2').jqm().jqmHide();return false;"><fmt:message key="annadir"/></button>
						<button class="xxx jqmClose"><fmt:message key="cancelar"/></button>
					</p>
				</div>
			</form>
		</div>
		<div class="dhtmlgoodies_aTab">
			<div id="listaRecursos">
				<table class="tabla_menu">
					<tr>
						<td><fmt:message key="enfermero"/> <%--(${nurse.rowCount})--%></td>
						<td><img alt="" src="markers/enfermero1.png" class="rayas"/></td>
					</tr>
					<tr>
						<td><fmt:message key="gerocultor"/> <%--(${gerocultor.rowCount})--%></td>
						<td><img alt="" src="markers/gerocultor1.png" class="rayas"/></td>
					</tr>
					<tr>
						<td><fmt:message key="auxiliar"/> <%--(${assistant.rowCount})--%></td>
						<td><img alt="" src="markers/auxiliar1.png" class="rayas"/></td>
					</tr>
					<tr><td colspan="2"><hr/></td></tr>
					<tr>
						<td><fmt:message key="policia"/> <%--(${policemen.rowCount})--%></td>
						<td><img alt="" src="markers/policia1.png" class="rayas"/></td>
					</tr>
					<tr>
						<td><fmt:message key="bomberos"/> <%--(${firemen.rowCount})--%></td>
						<td><img alt="" src="markers/bombero1.png" class="rayas"/></td>
					</tr>
					<tr>
						<td><fmt:message key="ambulancia"/> <%--(${ambulance.rowCount})--%></td>
						<td><img alt="" src="markers/ambulancia1.png" class="rayas"/></td>
					</tr>
				</table>
			</div>
			<div id="datos" class="oculto">
				<table class="tabla_menu">
					<tr>
						<td><fmt:message key="usuario"/></td>
						<td id="datos-usuario"></td>
					</tr>
					<tr>
						<td><fmt:message key="nombre"/></td>
						<td id="datos-nombre"></td>
					</tr>
					<tr>
						<td><fmt:message key="correo"/></td>
						<td id="datos-correo"></td>
					</tr>
					<tr>
						<td id="datos-actividades-titulo"></td>
						<td id="datos-actividades"></td>
					</tr>
				</table>
			</div>
			<form id="form-posicion" class="oculto" action="#">
				<hr/>
				<p>
					<input type="checkbox" name="localizacion" onclick="cambiarGeolocalizacion(localizacion.checked)"/>Activar geolocalizaci&oacute;n
				</p>
				<table class="tabla_menu">
					<tr>
						<th colspan="2"><fmt:message key="posicion"/></th>
					</tr>
					<tr>
						<td><fmt:message key="latitud"/></td>
						<td><input type="number" name="latitud" size="29" value="0" max="90" min="-90" step="0.000001"/></td>
					</tr>
					<tr>
						<td><fmt:message key="longitud"/></td>
						<td><input type="number" name="longitud" size="29" value="0" max="180" min="-179.999999" step="0.000001"/></td>
					</tr>
					<tr>
						<td><fmt:message key="direccion"/></td>
						<td><textarea name="direccion" cols="28" rows="2"></textarea></td>
					</tr>
				</table>
				<p>
					<input type="checkbox" name="porDefecto"><fmt:message key="guardarPosicion"/>
				</p>
				<p>
					<input type="button" id="submit01" value="<fmt:message key="comprobar"/>" class="btn" onclick="findPos(latitud.value,longitud.value,direccion.value)"/>
					<input type="button" id="submit02" value="<fmt:message key="aceptar"/>" class="btn" onclick="newPos(latitud.value,longitud.value,porDefecto.checked)"/>
				</p>
			</form>
		</div>
	</div>
	<!-- aqui se cambia el tamanno y titulo de las tabs -->
	<script type="text/javascript">
		initTabs('dhtmlgoodies_tabView1',Array('<fmt:message key="emergencias"/>','<fmt:message key="heridos"/>','<fmt:message key="agentes"/>'),0,254,490);
	</script>
</fmt:bundle>