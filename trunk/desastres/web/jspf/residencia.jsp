<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:bundle basename="fmt.eji8n">
	<form id="opcionesMapa" action="#">
		<span><input type="checkbox" name="verSanos" onclick="mostrarSanos(verSanos.checked)"/> <fmt:message key="verSanos"/> //</span>
		<span id="planta-2" class="pulsable" onclick="cambiarPlanta(-2)"><fmt:message key="visionGeneral"/></span> -
		<span id="planta-1" class="pulsable" onclick="cambiarPlanta(-1)"><fmt:message key="exterior"/></span> -
		<span id="planta0" class="pulsable" onclick="cambiarPlanta(0)"><fmt:message key="planta"/> 0</span> -
		<span id="planta1" class="pulsable" onclick="cambiarPlanta(1)"><fmt:message key="planta"/> 1</span> -
		<span id="planta2" class="pulsable" onclick="cambiarPlanta(2)"><fmt:message key="planta"/> 2</span>
	</form>
	<div id="dhtmlgoodies_tabView2">
		<div class="dhtmlgoodies_aTab">
			<div id="map_canvas"></div>
		</div>
		<div class="dhtmlgoodies_aTab">
			<table class="tabla_menu">
				<tr>
					<th rowspan="5">
						<img id="planoResidencia" src="markers/residencia/planta0.jpg" alt="Residencia"/>
						<br/>
						<fmt:message key="planta"/> <span id="plantaPlano">0</span>
					</th>
					<td><img alt="" src="markers/events/fuego.png"/></td>
					<td id="numFuegos"></td>
					<td><img alt="" src="markers/people/sano1.png"/></td>
					<td id="numSanos"></td>
				</tr>
				<tr>
					<td><img alt="" src="markers/events/agua.png"/></td>
					<td id="numInundaciones"></td>
					<td><img alt="" src="markers/people/leve1.png"/></td>
					<td id="numLeves"></td>
				</tr>
				<tr>
					<td><img alt="" src="markers/events/casa.png"/></td>
					<td id="numDerrumbamientos"></td>
					<td><img alt="" src="markers/people/grave1.png"/></td>
					<td id="numGraves"></td>
				</tr>
				<tr>
					<td><img alt="" src="markers/events/personaPerdida.png"/></td>
					<td id="numPerdidos"></td>
					<td><img alt="" src="markers/people/muerto1.png"/></td>
					<td id="numMuertos"></td>
				</tr>
				<tr>
					<td colspan="2"></td>
					<td><img alt="" src="markers/people/trapped1.png"/></td>
					<td id="numAtrapados"></td>
				</tr>
			</table>
		</div>
	</div>
	<script type="application/javascript">
		initTabs('dhtmlgoodies_tabView2',Array('<fmt:message key="gestionEmergencias"/>','<fmt:message key="recursos"/>'),0,null,518);
	</script>
</fmt:bundle>