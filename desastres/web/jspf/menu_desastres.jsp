<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:bundle basename="fmt.eji8n">
	<div id="dhtmlgoodies_tabView1">
		<div class="dhtmlgoodies_aTab">
			<form id="catastrofes" action="#">
				<table class="tabla_menu">
					<tr>
						<td><input type="hidden" name="marcador" value="event"/></td>
						<td><input type="hidden" name="cantidad" value="1"/></td>
					</tr>
					<tr>
						<td>
							<input type="radio" name="tipo" value="fire" checked="checked" onclick="cambiaIcono(marcador.value,'fire');"/>
							<fmt:message key="incendio"/>
						</td>
						<td rowspan="3"><img alt="" id="icono_catastrofes" src="markers/events/fuego.png" class="rayas"/></td>
					</tr>
					<tr>
						<td>
							<input type="radio" name="tipo" value="flood" onclick="cambiaIcono(marcador.value,'flood');"/>
							<fmt:message key="inundacion"/>
						</td>
					</tr>
					<tr>
						<td>
							<input type="radio" name="tipo" value="collapse" onclick="cambiaIcono(marcador.value,'collapse');"/>
							<fmt:message key="derrumbamiento"/>
						</td>
					</tr>
				</table>
				<table class="tabla_menu">
					<tr><td colspan="2"><input type="text" name="nombre" class="nombre" value="<fmt:message key="nombre"/>"/></td></tr>
					<tr><td colspan="2"><textarea name="info" class="info" rows="3" cols="1"><fmt:message key="informacion"/></textarea></td></tr>
					<tr><td colspan="2"><textarea name="descripcion" class="descripcion" rows="3" cols="1"><fmt:message key="descripcion"/></textarea></td></tr>
					<tr>
						<td rowspan="2">
							<textarea id="direccion1" class="direccion" name="direccion" rows="4" cols="20"><fmt:message key="direccion"/></textarea>
						</td>
						<td class="img_menu">
							<img class="botones" alt="Validar direcci&oacute;n" id="validardireccion1" onclick="validarDireccion(1)"
								 onmouseover="cambiaFlecha(0,1)" onmouseout="cambiaFlecha(1,1)" src="images/iconos/confirm2.png"/>
						</td>
					</tr>
					<tr><td class="img_menu"><img class="botones" alt="Direcci&oacute;n no v&aacute;lida" id="validacion1" src="images/iconos/no.png"/></td></tr>
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
					<input type="hidden" name="latitud" id="latitud1" value=""/>
					<input type="hidden" name="longitud" id="longitud1" value=""/>
					<input type="hidden" name="estado" value="active"/>
				</p>
				<p>
					<input type="button" id="submit11" value="<fmt:message key="marcarenelmapa"/>" class="btn" onclick="pinchaMapa(1);return false;"/>
					<input type="button" id="submit12" value="<fmt:message key="annadir"/>" class="btn" onclick="crearCatastrofe(
						marcador.value,seleccionRadio(this.form,0),cantidad.value,nombre.value,info.value,descripcion.value,direccion.value,
						longitud.value,latitud.value,estado.value,size.value,traffic.value,0,-2); borrarFormulario(this.form,1); return false;"/>
				</p>
				<div class="jqmWindow" id="dialog1">
					<p><fmt:message key="puntoalmacenado"/></p>
					<p class="centrado">
						<button onclick="crearCatastrofe(marcador.value,seleccionRadio(this.form,0),cantidad.value,nombre.value,info.value,
							descripcion.value,direccion.value,longitud.value,latitud.value,estado.value,size.value,traffic.value,0,-2);
							$('#dialog1').jqm().jqmHide(); borrarFormulario(this.form,1); return false;"><fmt:message key="annadir"/></button>
						<button class="xxx jqmClose"><fmt:message key="cancelar"/></button>
					</p>
				</div>
			</form>
		</div>
		<div class="dhtmlgoodies_aTab">
			<form id="recursos" action="#">
				<table class="tabla_menu">
					<tr><td colspan="2"><input type="hidden" name="marcador" value="resource"/></td></tr>
					<tr>
						<td>
							<input type="radio" name="tipo" value="police" checked="checked" onclick="cambiaIcono(marcador.value,'police', cantidad.value);"/>
							<fmt:message key="policia"/>
						</td>
						<td rowspan="3"><img alt="" id="icono_recursos" src="markers/resources/policia1.png" class="rayas"/></td>
					</tr>
					<tr>
						<td>
							<input type="radio" name="tipo" value="firemen" onclick="cambiaIcono(marcador.value,'firemen', cantidad.value);"/>
							<fmt:message key="bomberos"/>
						</td>
					</tr>
					<tr>
						<td>
							<input type="radio" name="tipo" value="ambulance" onclick="cambiaIcono(marcador.value,'ambulance',cantidad.value);"/>
							<fmt:message key="ambulancia"/>
						</td>
					</tr>
				</table>
				<table class="tabla_menu">
					<tr>
						<td colspan="2">
							<label for="cantidad1"><fmt:message key="numerounidades"/>:</label>
							<select name="cantidad" id="cantidad1" onchange="cambiaIcono(marcador.value,seleccionRadio(this.form,1),cantidad.value);">
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
					<tr><td colspan="2"><input type="text" name="nombre" value="<fmt:message key="unidades"/>" class="nombre"/></td></tr>
					<tr><td colspan="2"><textarea name="info" class="info" rows="3" cols="1"><fmt:message key="informacion"/></textarea></td></tr>
					<tr><td colspan="2"><textarea name="descripcion" class="descripcion" rows="3" cols="1"><fmt:message key="descripcion"/></textarea></td></tr>
					<tr>
						<td rowspan="2">
							<textarea id="direccion2" name="direccion" class="direccion" rows="4" cols="20"><fmt:message key="direccion"/></textarea>
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
				<p>
					<input type="hidden" name="latitud" id="latitud2" value=""/>
					<input type="hidden" name="longitud" id="longitud2" value=""/>
					<input type="hidden" name="estado" value="active"/>
					<input type="hidden" name="magnitude" id="magnitude2" value=""/>
					<input type="hidden" name="traffic" id="traffic2" value=""/>
				</p>
				<p>
					<input type="button" id="submit21" value="<fmt:message key="marcarenelmapa"/>" class="btn" onclick="pinchaMapa(2);return false;"/>
					<input type="button" id="submit22" value="<fmt:message key="annadir"/>" class="btn" onclick="crearCatastrofe(
						marcador.value,seleccionRadio(this.form,1),cantidad.value,nombre.value,info.value,
						descripcion.value,direccion.value,longitud.value,latitud.value,estado.value,magnitude.value,traffic.value,0);
						borrarFormulario(this.form,2);return false;"/>
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
					<tr><td colspan="2"><input type="hidden" name="marcador" value="people"/></td></tr>
					<tr>
						<td>
							<input type="radio" name="tipo" value="slight" checked="checked" onclick="cambiaIcono(marcador.value,'slight',cantidad.value);"/>
							<fmt:message key="leves"/>
						</td>
						<td rowspan="4"><img alt="" id="icono_heridos" src="markers/people/leve1.png" class="rayas"/></td>
					</tr>
					<tr>
						<td>
							<input type="radio" name="tipo" value="serious" onclick="cambiaIcono(marcador.value,'serious',cantidad.value);"/>
							<fmt:message key="graves"/>
						</td>
					</tr>
					<tr>
						<td>
							<input type="radio" name="tipo" value="dead" onclick="cambiaIcono(marcador.value,'dead',cantidad.value);"/>
							<fmt:message key="muertos"/>
						</td>
					</tr>
					<tr>
						<td>
							<input type="radio" name="tipo" value="trapped" onclick="cambiaIcono(marcador.value,'trapped',cantidad.value);"/>
							<fmt:message key="atrapados"/>
						</td>
					</tr>
				</table>
				<table class="tabla_menu">
					<tr>
						<td colspan="2">
							<label for="cantidad2"><fmt:message key="numeropersonas"/>:</label>
							<select name="cantidad" id="cantidad2" onchange="cambiaIcono(marcador.value,seleccionRadio(this.form,2),cantidad.value);">
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
					<tr><td colspan="2"><input type="text" name="nombre" class="nombre" value="<fmt:message key="personas"/>"/></td></tr>
					<tr><td colspan="2"><textarea name="info" class="info" rows="3" cols="1"><fmt:message key="nombres"/></textarea></td></tr>
					<tr><td colspan="2"><textarea name="descripcion" class="descripcion" rows="3" cols="1"><fmt:message key="descripciongravedad"/></textarea></td></tr>
					<tr>
						<td rowspan="2">
							<textarea id="direccion3" name="direccion" class="direccion" rows="4" cols="20"><fmt:message key="direccion"/></textarea>
						</td>
						<td class="img_menu">
							<img class="botones" alt="Validar direcci&oacute;n" id="validardireccion3" onclick="validarDireccion(3)"
								 onmouseover="cambiaFlecha(0,3)" onmouseout="cambiaFlecha(1,3)" src="images/iconos/confirm2.png"/>
						</td>
					</tr>
					<tr>
						<td class="img_menu">
							<img class="botones" alt="Direcci&oacute;n no v&aacute;lida" id="validacion3" src="images/iconos/no.png"/>
						</td>
					</tr>
				</table>
				<p>
					<input type="hidden" name="latitud" id="latitud3" value=""/>
					<input type="hidden" name="longitud" id="longitud3" value=""/>
					<input type="hidden" name="estado" value="active"/>
					<input type="hidden" name="magnitude" id="magnitude3" value=""/>
					<input type="hidden" name="traffic" id="traffic3" value=""/>
				</p>
				<p>
					<input type="button" id="submit31" value="<fmt:message key="marcarenelmapa"/>" class="btn" onclick="pinchaMapa(3);return false;"/>
					<input type="button" id="submit32" value="<fmt:message key="annadir"/>" class="btn" onclick="crearCatastrofe(
						marcador.value,seleccionRadio(this.form,2),cantidad.value,nombre.value,info.value,
						descripcion.value,direccion.value,longitud.value,latitud.value,estado.value,magnitude.value,traffic.value,0);
						borrarFormulario(this.form,3);return false;"/>
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
					<input type="button" id="Start" value="<fmt:message key="comenzar"/>" class="btn" onclick="lanzaExperto(); return false;"/>
					<input type="button" id="Stop" value="<fmt:message key="parar"/>" class="btn" onclick="stopExperto(); return false;"/>
				</p>
			</form>
		</div>
	</div>
	<!--aqui se cambia el tamanno y titulo de las tabs -->
	<script type="text/javascript">
		initTabs('dhtmlgoodies_tabView1',Array('<fmt:message key="eventos"/>','<fmt:message key="recursos"/>','<fmt:message key="victimas"/>','<fmt:message key="experto"/>'),0,228,488);
	</script>
</fmt:bundle>