<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:bundle basename="fmt.eji8n">
	<!-- Ventana de error de direccion no encontrada -->
	<div class="jqmWindow" id="error">
		<p id="error_texto"></p>
		<p class="centrado">
			<button class="xxx jqmClose"><fmt:message key="aceptar"/></button>
		</p>
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
				<tr id="sintomas">
					<td colspan="2">
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
					</td>
				</tr>
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
				longitud.value,latitud.value,estado.value,magnitude.value,traffic.value,idAssigned.value,
						fatigue.checked,fever.checked,dyspnea.checked,nausea.checked,headache.checked,vomiting.checked,abdominal_pain.checked,weight_loss.checked,blurred_vision.checked,muscle_weakness.checked);
				borrarFormulario(this.form,1);$('#modificar').jqm().jqmHide();return false;">
			</p>
		</form>
	</div>
</fmt:bundle>
