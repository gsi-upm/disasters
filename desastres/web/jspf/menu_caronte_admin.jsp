<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:bundle basename="fmt.eji8n">
	<div id="dhtmlgoodies_tabView1">
		<div class="dhtmlgoodies_aTab">
			<form id="catastrofes" action="#">
				<div id="radio_catastrofes">
					<table class="tabla_menu">
						<tr>
							<td>
								<input type="radio" name="tipo" value="fire" checked="checked" onclick="cambiaIcono('event','fire');"/>
								<fmt:message key="incendio"/>
							</td>
							<td rowspan="5"><img id="icono_catastrofes" class="rayas" alt="" src="markers/events/fuego.png"/></td>
						</tr>
						<tr>
							<td>
								<input type="radio" name="tipo" value="flood" onclick="cambiaIcono('event','flood');"/>
								<fmt:message key="inundacion"/>
							</td>
						</tr>
						<tr>
							<td>
								<input type="radio" name="tipo" value="collapse" onclick="cambiaIcono('event','collapse');"/>
								<fmt:message key="derrumbamiento"/>
							</td>
						</tr>
						<tr>
							<td>
								<input type="radio" name="tipo" value="lostPerson" onclick="cambiaIcono('event','lostPerson');"/>
								<fmt:message key="personaperdida"/>
							</td>
						</tr>
						<tr>
							<td>
								<input type="radio" name="tipo" value="injuredPerson" onclick="cambiaIcono('event','injuredPerson');"/>
								<fmt:message key="personaherida"/>
							</td>
						</tr>
					</table>
				</div>
				<div id="radio_catastrofes_2" class="oculto">
					<table class="tabla_menu">
						<tr>
							<td id="tipo_catastrofes_2"></td>
							<td><img id="icono_catastrofes_2" class="rayas" src="markers/events/fuego.png" alt=""/></td>
						</tr>
					</table>
				</div>
				<table class="tabla_menu">
					<tr><td colspan="2"><input type="text" name="nombre" class="nombre" value="Incendio" placeholder="<fmt:message key="nombre"/>"/></td></tr>
					<tr><td colspan="2"><textarea name="info" class="info" rows="3" cols="1" placeholder="<fmt:message key="informacion"/>"></textarea></td></tr>
					<tr><td colspan="2"><textarea name="descripcion" class="descripcion" rows="3" cols="1" placeholder="<fmt:message key="descripcion"/>"></textarea></td></tr>
					<tr>
						<td rowspan="2">
							<textarea id="direccion1" class="direccion" name="direccion" rows="4" cols="20" placeholder="<fmt:message key="direccion"/>"></textarea>
						</td>
						<td class="img_menu">
							<img id="validardireccion1" class="botones" src="images/iconos/confirm2.png" alt="Validar direcci&oacute;n"
								onclick="validarDireccion(1)" onmouseover="cambiaFlecha(0,1)" onmouseout="cambiaFlecha(1,1)"/>
						</td>
					</tr>
					<tr><td class="img_menu"><img id="validacion1" class="botones" src="images/iconos/no.png" alt="Direcci&oacute;n no v&aacute;lida"/></td></tr>
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
						<td><label for="tamanno"><fmt:message key="tamanno"/></label></td>
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
						<td><label for="trafico"><fmt:message key="restriccion"/></label></td>
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
				<input type="button" id="submit11" class="btn" value="<fmt:message key="marcarenelmapa"/>" onclick="pinchaMapa(1);return false;"/>
				<br/>
				<input type="button" id="submit10" class="btn oculto" value="<fmt:message key="modificar"/>" onclick="
					modificar2(iden.value,seleccionRadio(this.form,0),1,nombre.value,descripcion.value,info.value,
						direccion.value,tamanno.value,trafico.value,planta.value,0); return false;"/>
				<input type="button" id="eliminar1" class="btn oculto" value="Eliminar" onclick="eliminar(marcadores_definitivos[iden.value],definitivo);"/>
				<div class="jqmWindow" id="dialog1">
					<p><fmt:message key="confirmarMarcador"/></p>
					<p class="centrado">
						<input type="button" value="<fmt:message key="annadir"/>" onclick="
							crearCatastrofe('event',seleccionRadio(this.form,0),1,nombre.value,info.value,descripcion.value,
								direccion.value,longitud.value,latitud.value,'active',tamanno.value,trafico.value,0,planta.value);
							$('#dialog1').jqm().jqmHide(); return false;"/>
						<input type="button" class="xxx jqmClose" value="<fmt:message key="cancelar"/>"/>
					</p>
				</div>
			</form>
		</div>
		<div class="dhtmlgoodies_aTab">
			<form id="heridos" action="#">
				<table class="tabla_menu">
					<tr>
						<td>
							<input type="radio" name="tipo" value="slight" checked="checked" onclick="cambiaIcono('people','slight',1);"/>
							<fmt:message key="leves"/>
						</td>
						<td rowspan="5"><img alt="" id="icono_heridos" src="markers/people/leve1.png" class="rayas"/></td>
					</tr>
					<tr>
						<td>
							<input type="radio" name="tipo" value="serious" onclick="cambiaIcono('people','serious',1);"/>
							<fmt:message key="graves"/>
						</td>
					</tr>
					<tr>
						<td>
							<input type="radio" name="tipo" value="dead" onclick="cambiaIcono('people','dead',1);"/>
							<fmt:message key="muertos"/>
						</td>
					</tr>
					<tr>
						<td>
							<input type="radio" name="tipo" value="trapped" onclick="cambiaIcono('people','trapped',1);"/>
							<fmt:message key="atrapados"/>
						</td>
					</tr>
					<tr>
						<td>
							<input type="radio" name="tipo" value="healthy" onclick="cambiaIcono('people','healthy',cantidad.value);"/>
							<fmt:message key="sanos"/>
						</td>
					</tr>
				</table>
				<div id="asociacionesEmergencias">
					<span><fmt:message key="asociado"/>:</span>
					<ul>
						<li id="textoAsoc"></li>
						<li>
							<ul id="checkboxAsoc"></ul>
						</li>
					</ul>
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
				<table id="cantidad" class="tabla_menu">
					<tr>
						<td><fmt:message key="numeropersonas"/>&thinsp;</td>
						<td>
							<select name="cantidad" onchange="cambiaIcono('people',seleccionRadio(this.form,2),cantidad.value);">
								<option value="1" selected="selected">1</option>
								<c:forEach var="item" begin="2" end="30" step="1">
									<option value="${item}">${item}</option>
								</c:forEach>
							</select>
						</td>
					</tr>
				</table>
				<!--<div id="sintomas">
					<span>S&iacute;ntomas:</span>
					<ul>
						<li id="textoSintomas">&thinsp;</li>
						<li>
							<ul id="listaSintomas"></ul>
						</li>
					</ul>
				</div>-->
				<table class="tabla_menu">
					<tr><td colspan="2"><input type="text" name="nombre" class="nombre" value="Sano" placeholder="<fmt:message key="nombre"/>"/></td></tr>
					<tr><td colspan="2"><textarea name="info" class="info" rows="3" cols="1" placeholder="<fmt:message key="informacion"/>"></textarea></td></tr>
					<tr><td colspan="2"><textarea name="descripcion" class="descripcion" rows="3" cols="1" placeholder="<fmt:message key="descripcion"/>"></textarea></td></tr>
					<tr>
						<td rowspan="2">
							<textarea id="direccion2" name="direccion" class="direccion" rows="4" cols="20" placeholder="<fmt:message key="direccion"/>"></textarea>
						</td>
						<td class="img_menu">
							<img id="validardireccion2" class="botones" src="images/iconos/confirm2.png" alt="Validar direcci&oacute;n"
								  onclick="validarDireccion(2)" onmouseover="cambiaFlecha(0,2)" onmouseout="cambiaFlecha(1,2)"/>
						</td>
					</tr>
					<tr>
						<td class="img_menu">
							<img id="validacion2" class="botones" src="images/iconos/no.png" alt="Direcci&oacute;n no v&aacute;lida"/>
						</td>
					</tr>
				</table>
				<input type="hidden" name="iden" value=""/>
				<input type="hidden" name="latitud" id="latitud2" value=""/>
				<input type="hidden" name="longitud" id="longitud2" value=""/>
				<input type="button" id="submit21" class="btn" value="<fmt:message key="marcarenelmapa"/>" onclick="pinchaMapa(2); return false;"/>
				<br/>
				<input type="button" id="submit20" class="btn oculto" value="<fmt:message key="modificar"/>" onclick="
						modificar2(iden.value,seleccionRadio(this.form,2),cantidad.value,nombre.value,descripcion.value,
						info.value,direccion.value,peso.value,movilidad.value,planta.value,0); return false;"/>
				<input type="button" id="eliminar2" value="Eliminar" class="btn oculto" onclick="eliminar(marcadores_definitivos[iden.value],definitivo);"/>
				<div class="jqmWindow" id="dialog2">
					<p><fmt:message key="confirmarMarcador"/></p>
					<p class="centrado">
						<input type="button" value="<fmt:message key="annadir"/>" onclick="
							crearCatastrofe('people',seleccionRadio(this.form,2),cantidad.value,nombre.value,info.value,descripcion.value,
								direccion.value,longitud.value,latitud.value,'active',peso.value,movilidad.value,0,planta.value);
							$('#dialog2').jqm().jqmHide(); return false;"/>
						<input type="button" class="xxx jqmClose" value="<fmt:message key="cancelar"/>"/>
					</p>
				</div>
			</form>
		</div>
		<div class="dhtmlgoodies_aTab">
			<div id="listaRecursos">
				<table class="tabla_menu">
					<tr>
						<td><fmt:message key="enfermero"/> <span id="numEnfermeros"></span></td>
						<td><img alt="" src="markers/resources/enfermero.png" class="rayas"/></td>
					</tr>
					<tr>
						<td><fmt:message key="gerocultor"/> <span id="numGerocultores"></span></td>
						<td><img alt="" src="markers/resources/gerocultor.png" class="rayas"/></td>
					</tr>
					<tr>
						<td><fmt:message key="auxiliar"/> <span id="numAuxiliares"></span></td>
						<td><img alt="" src="markers/resources/auxiliar.png" class="rayas"/></td>
					</tr>
					<tr>
						<td><fmt:message key="otroPersonal"/> <span id="numOtros"></span></td>
						<td><img alt="" src="markers/resources/otroPersonal.png" class="rayas"/></td>
					</tr>
					<tr><td colspan="2"><hr/></td></tr>
					<tr>
						<td><fmt:message key="policia"/> <span id="numPolicias"></span></td>
						<td><img alt="" src="markers/resources/policia.png" class="rayas"/></td>
					</tr>
					<tr>
						<td><fmt:message key="bomberos"/> <span id="numBomberos"></span></td>
						<td><img alt="" src="markers/resources/bombero.png" class="rayas"/></td>
					</tr>
					<tr>
						<td><fmt:message key="ambulancia"/> <span id="numAmbulancias"></span></td>
						<td><img alt="" src="markers/resources/ambulancia.png" class="rayas"/></td>
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
			<form id="form-posicion" action="#" class="oculto">
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
						<td><input type="number" class="coordenadas" name="latitud" value="0" max="90" min="-90" step="0.000001"/></td>
					</tr>
					<tr>
						<td><fmt:message key="longitud"/></td>
						<td><input type="number" class="coordenadas" name="longitud" value="0" max="180" min="-179.999999" step="0.000001"/></td>
					</tr>
					<tr>
						<td><fmt:message key="direccion"/></td>
						<td><textarea class="dirCoordenadas" name="direccion" rows="2" cols="1"></textarea></td>
					</tr>
				</table>
				<p>
					<input type="checkbox" name="porDefecto"><fmt:message key="guardarPosicion"/>
				</p>
				<p>
					<input type="button" id="submit01" class="btn" value="<fmt:message key="comprobar"/>" onclick="findPos(latitud.value, longitud.value, direccion.value)"/>
					<input type="button" id="submit02" class="btn" value="<fmt:message key="aceptar"/>" onclick="newPos(latitud.value, longitud.value, porDefecto.checked)"/>
				</p>
				<table class="tabla_menu">
					<tr>
						<td><fmt:message key="planta"/></td>
						<td>
							<select name="planta" id="select-planta3">
								<option value="-2" selected="selected"><fmt:message key="visionGeneral"/></option>
								<option value="-1"><fmt:message key="exterior"/></option>
								<option value="0"><fmt:message key="planta"/> 0</option>
								<option value="1"><fmt:message key="planta"/> 1</option>
								<option value="2"><fmt:message key="planta"/> 2</option>
							</select>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<input type="checkbox" name="plantaPorDefecto"><fmt:message key="porDefecto"/>
						</td>
					</tr>
				</table>
				<p>
					<input type="button" id="submit03" class="btn" value="<fmt:message key="aceptar"/>" onclick="editPlanta(planta.value, plantaPorDefecto.checked)"/>				
				</p>
			</form>
		</div>
	</div>
	<!-- aqui se cambia el tamanno y titulo de las tabs -->
	<script type="application/javascript">
		initTabs('dhtmlgoodies_tabView1',Array('<fmt:message key="emergencias"/>','<fmt:message key="heridos"/>','<fmt:message key="agentes"/>'),0,235,490);
	</script>
</fmt:bundle>