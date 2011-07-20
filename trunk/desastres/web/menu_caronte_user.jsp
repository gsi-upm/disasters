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
					<input type="hidden" name="cantidad" value="1"/>
				</p>
				<p>
					<input type="button" id="submit11" value="<fmt:message key="marcarenelmapa"/>" class="btn" onclick="pinchaMapa(1);return false;"/>
					<input type="button" id="submit12" value="<fmt:message key="annadir"/>" class="btn" onclick="crearCatastrofe(
						'event',seleccionRadio(this.form,0),cantidad.value,nombre.value,info.value,
						descripcion.value,direccion.value,longitud.value,latitud.value,estado.value,size.value,traffic.value,0);
						borrarFormulario(this.form,1);return false;"/>
				</p>
				<div class="jqmWindow" id="dialog1">
					<p><fmt:message key="puntoalmacenado"/></p>
					<p class="centrado">
						<button onclick="crearCatastrofe('event',seleccionRadio(this.form,0),cantidad.value,nombre.value,info.value,
							descripcion.value,direccion.value,longitud.value,latitud.value,estado.value,size.value,traffic.value,0);
							$('#dialog1').jqm().jqmHide();borrarFormulario(this.form,1);return false;"><fmt:message key="annadir"/></button>
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
							<input type="radio" name="tipo" value="slight" checked="checked" onclick="cambiaIcono('people','slight',1);"/>
							<fmt:message key="leves"/> <%--(${slight.rowCount})--%>
						</td>
						<td rowspan="4"><img alt="" id="icono_heridos" src="markers/leve1.png" class="rayas"/></td>
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
				<table class="tabla_menu">
					<tr>
						<sql:query var="residentes" dataSource="${CatastrofesServer}" sql="
							SELECT * FROM catastrofes where tipo='healthy' and estado!='erased';">
						</sql:query>
						<c:if test="${residentes.rowCount > 0}">
							<td><label for="residente">Residentes:</label></td>
							<td>
								<select name="residente" id="residente">
									<c:forEach var="residente" items="${residentes.rows}">
										<option value="${residente.id}">${residente.id} - ${residente.nombre}</option>
									</c:forEach>
								</select>
							</td>
						</c:if>
						<c:if test="${residentes.rowCount == 0}">
							<td colspan="2"><b>Sin residentes que asociar</b></td>
						</c:if>
					</tr>
					<tr>
						<td colspan="2">
							<input type="hidden" name="fatigue"/>
							<input type="hidden" name="fever"/>
							<input type="hidden" name="dyspnea"/>
							<input type="hidden" name="nausea"/>
							<input type="hidden" name="headache"/>
							<input type="hidden" name="vomiting"/>
							<input type="hidden" name="abdominal_pain"/>
							<input type="hidden" name="weight_loss"/>
							<input type="hidden" name="blurred_vision"/>
							<input type="hidden" name="muscle_weakness"/>
						</td>
						<!--
						<td colspan="2">
							S&iacute;ntomas:<br/>
							<input type="checkbox" name="fatigue"/>Fatiga
							<input type="checkbox" name="fever"/>Fiebre
							<input type="checkbox" name="dyspnea"/>Disnea
							<input type="checkbox" name="nausea"/>Nauseas
							<input type="checkbox" name="headache"/>Dolor de cabeza
							<input type="checkbox" name="vomiting"/>V&oacute;mitos
							<input type="checkbox" name="abdominal_pain"/>Dolor abdominal
							<input type="checkbox" name="weight_loss"/>Perdida de peso
							<input type="checkbox" name="blurred_vision"/>Visi&oacute;n borrosa
							<input type="checkbox" name="muscle_weakness"/>Debilidad muscular
						</td>
						-->
					</tr>
				</table>
				<p>
					<input type="hidden" name="latitud" id="latitud3" value=""/>
					<input type="hidden" name="longitud" id="longitud3" value=""/>
					<input type="hidden" name="estado" value="active"/>
					<input type="hidden" name="size" id="magnitude3" value=""/>
					<input type="hidden" name="traffic" id="traffic3" value=""/>
				</p>
				<p>
					<input type="button" id="submit32" value="<fmt:message key="annadir"/>" class="btn" onclick="modificar3(
						residente.value,seleccionRadio(this.form,2),fatigue.checked,fever.checked,dyspnea.checked,nausea.checked,headache.checked,vomiting.checked,abdominal_pain.checked,weight_loss.checked,blurred_vision.checked,muscle_weakness.checked);
						return false;"/>
				</p>
				<!--a href="#" onclick="pinchaMapa(3)">Marcar en mapa</a-->
				<div class="jqmWindow" id="dialog3">
					<p><fmt:message key="puntoalmacenado"/></p>
					<p class="centrado">
						<button onclick="crearCatastrofe('people',seleccionRadio(this.form,2),cantidad.value,nombre.value,info.value,
							descripcion.value,direccion.value,longitud.value,latitud.value,estado.value,size.value,traffic.value,0,
							fatigue.checked,fever.checked,dyspnea.checked,nausea.checked,headache.checked,vomiting.checked,abdominal_pain.checked,weight_loss.checked,blurred_vision.checked,muscle_weakness.checked);
							$('#dialog3').jqm().jqmHide();borrarFormulario(this.form,3);return false;"><fmt:message key="annadir"/></button>
						<button class="xxx jqmClose"><fmt:message key="cancelar"/></button>
					</p>
				</div>
			</form>
		</div>
	</div>
	<!--aqui se cambia el tamanno y titulo de las tabs -->
	<script type="text/javascript">
		initTabs('dhtmlgoodies_tabView1',Array('<fmt:message key="eventos"/>','<fmt:message key="victimas"/>'),0,254,490);
	</script>
</fmt:bundle>
