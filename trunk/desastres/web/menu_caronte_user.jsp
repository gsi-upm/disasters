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
					<input type="hidden" name="iden" id="iden1" value=""/>
					<input type="hidden" name="latitud" id="latitud1" value=""/>
					<input type="hidden" name="longitud" id="longitud1" value=""/>
					<input type="hidden" name="cantidad" value="1"/>
					<input type="button" id="submit11" value="<fmt:message key="marcarenelmapa"/>" class="btn" onclick="pinchaMapa(1);return false;"/>
					<input type="button" id="submit10" value="<fmt:message key="modificar"/>" class="btn" style="display:none;" onclick="modificar2(
						iden.value,seleccionRadio(this.form,0),cantidad.value,nombre.value,info.value,descripcion.value,
						direccion.value,size.value,traffic.value);return false;"/>
					<input type="hidden" id="eliminar1"/>
				</p>
				<div class="jqmWindow" id="dialog1">
					<p>¿Confirma añadir el marcador en el mapa?<!--<fmt:message key="puntoalmacenado"/>--></p>
					<p class="centrado">
						<button onclick="crearCatastrofe('event',seleccionRadio(this.form,0),cantidad.value,nombre.value,info.value,
							descripcion.value,direccion.value,longitud.value,latitud.value,'active',size.value,traffic.value,0);
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
				<p>
					<sql:query var="emergencias" dataSource="${CatastrofesServer}"
						sql="SELECT * FROM catastrofes WHERE marcador='event' AND estado!='erased'">
					</sql:query>
					<c:if test="${emergencias.rowCount > 0}">
						Asociado a:
						<select name="idAssigned" id="emergencia">
							<option value="0">0 - Sin asociar</option>
							<c:forEach var="emergencia" items="${emergencias.rows}">
								<option value="${emergencia.id}">${emergencia.id} - ${emergencia.nombre}</option>
							</c:forEach>
						</select>
					</c:if>
					<c:if test="${emergencias.rowCount == 0}">
						Sin emergencias para asociar
						<input type="hidden" name="idAssigned" value="0"/>
					</c:if>
				</p>
				<table class="tabla_menu">
					<tr>
						<td>Peso</td>
						<td>
							<select name="peso" id="peso">
								<option value="indefinido">Indefinido</option>
								<option value="bajo">Bajo (&lt;65kg)</option>
								<option value="medio">Medio (65-100kg)</option>
								<option value="alto">Alto (&gt;100kg)</option>
							</select>
						</td>
					</tr>
					<tr>
						<td>Movilidad</td>
						<td>
							<select name="movilidad" id="movilidad">
								<option value="normal">Normal</option>
								<option value="2">Tipo 2</option>
								<option value="3">Tipo 3</option>
								<option value="4">Tipo 4</option>
								<option value="5">Tipo 5</option>
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
				<p>
					<input type="hidden" name="iden" value=""/>
					<input type="hidden" name="latitud" id="latitud2" value=""/>
					<input type="hidden" name="longitud" id="longitud2" value=""/>
					<input type="hidden" name="size" id="magnitude2" value=""/>
					<input type="hidden" name="traffic" id="traffic2" value=""/>
					<input type="hidden" name="cantidad" value="1"/>
				</p>
				<p>
					<input type="button" id="submit21" value="<fmt:message key="marcarenelmapa"/>" class="btn" onclick="pinchaMapa(2);return false;"/>
					<input type="button" id="submit20" value="<fmt:message key="modificar"/>" class="btn" style="display:none;" onclick="modificar2(
						iden.value,seleccionRadio(this.form,2),cantidad.value,nombre.value,info.value,descripcion.value,
						direccion.value,size.value,traffic.value,idAssigned.value,peso.value,movilidad.value);return false;"/>
					<input type="hidden" id="eliminar2"/>
					<input type="hidden" name="verSanos"/>
				</p>
				<div class="jqmWindow" id="dialog2">
					<p>¿Confirma añadir el marcador en el mapa?<!--<fmt:message key="puntoalmacenado"/>--></p>
					<p class="centrado">
						<button onclick="crearCatastrofe('people',seleccionRadio(this.form,2),cantidad.value,nombre.value,info.value,
							descripcion.value,direccion.value,longitud.value,latitud.value,'active',size.value,traffic.value,0,peso.value,movilidad.value);
							$('#dialog2').jqm().jqmHide();return false;"><fmt:message key="annadir"/></button>
						<button class="xxx jqmClose"><fmt:message key="cancelar"/></button>
					</p>
				</div>
			</form>
		</div>
	</div>
	<!--aqui se cambia el tamanno y titulo de las tabs -->
	<script type="text/javascript">
		initTabs('dhtmlgoodies_tabView1',Array('<fmt:message key="eventos"/>','Residentes'),0,254,490);
	</script>
</fmt:bundle>