<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean class="gsi.proyect.ProyectBean" id="proyecto" scope="session"/>

<%@ include file="info_caronte.jsp" %>
<fmt:bundle basename="fmt.eji8n">
	<form action="#" id="opcionesMapa" style="float:right">
		<input type="checkbox" name="verSanos" onclick="mostrarSanos(verSanos.checked)"/> Ver personas sanas //
		<span id="planta-2" onclick="cambiarPlanta(-2)" style="cursor:pointer">Visi&oacute;n general</span> -
		<span id="planta-1" onclick="cambiarPlanta(-1)" style="cursor:pointer">Exterior</span> -
		<span id="planta0" onclick="cambiarPlanta(0)" style="cursor:pointer; font-weight:bold; text-decoration:underline"><fmt:message key="planta"/> 0</span> -
		<span id="planta1" onclick="cambiarPlanta(1)" style="cursor:pointer"><fmt:message key="planta"/> 1</span> -
		<span id="planta2" onclick="cambiarPlanta(2)" style="cursor:pointer"><fmt:message key="planta"/> 2</span>
	</form>
	<div id="dhtmlgoodies_tabView2">
		<div class="dhtmlgoodies_aTab">
			<div id="map_canvas"></div>
		</div>
		<div class="dhtmlgoodies_aTab">
			<!-- Tabla de ejemplo hasta introducir imagenes -->
			<script type="text/javascript">
			</script>
			<table class="tabla_menu">
				<tr>
					<th rowspan="5">
						<img id="planoResidencia" src="images/residencia/planta0.jpg" alt="Residencia" style="width:611px;height:487px"/>
						<br/>
						<fmt:message key="planta"/> <span id="plantaPlano">0</span>
					</th>
					<td><img alt="" src="markers/fuego.png"/></td>
					<td>(${firesResi.rowCount})</td>
					<td><img alt="" src="markers/leve1.png"/></td>
					<td>(${slightResi.rowCount})</td>
				</tr>
				<tr>
					<td><img alt="" src="markers/agua.png"/></td>
					<td>(${floodsResi.rowCount})</td>
					<td><img alt="" src="markers/grave1.png"/></td>
					<td>(${seriousResi.rowCount})</td>
				</tr>
				<tr>
					<td><img alt="" src="markers/casa.png"/></td>
					<td>(${collapsesResi.rowCount})</td>
					<td><img alt="" src="markers/muerto1.png"/></td>
					<td>(${deadResi.rowCount})</td>
				</tr>
				<tr>
					<td><img alt="" src="markers/personaHerida.png"/></td>
					<td>(${injuredPeopleResi.rowCount})</td>
					<td><img alt="" src="markers/trapped1.png"/></td>
					<td>(${trappedResi.rowCount})</td>
				</tr>
				<tr>
					<td><img alt="" src="markers/personaPerdida.png"/></td>
					<td>(${lostPeopleResi.rowCount})</td>
					<td><img alt="" src="markers/sano1.png"/></td>
					<td>(${healthyResi.rowCount})</td>
				</tr>
			</table>
		</div>
	</div>
	<script type="text/javascript">
		initTabs('dhtmlgoodies_tabView2',Array('<fmt:message key="mapapueblo"/>','<fmt:message key="planoresidencia"/>'),0,null,518);
	</script>
</fmt:bundle>