<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean class="gsi.proyect.ProyectBean" id="proyecto" scope="session"/>

<%@ include file="info_caronte.jsp" %>
<fmt:bundle basename="fmt.eji8n">
	<div id="dhtmlgoodies_tabView2">
		<div class="dhtmlgoodies_aTab">
			<div id="map_canvas"></div>
		</div>
		<div class="dhtmlgoodies_aTab">
			<!-- Tabla de ejemplo hasta introducir imagenes -->
			<script type="text/javascript">
				function cambiarPlanta(num){
					document.getElementById('planoResidencia').src = 'images/residencia/planta' + num + '.jpg'
				}
			</script>
			<table class="tabla_menu">
				<tr>
					<th rowspan="5">
						<img id="planoResidencia" src="images/residencia/planta0.jpg" alt="Residencia" style="width:611px;height:487px"/>
						<br/>
						<span onclick="cambiarPlanta(0)" style="cursor:pointer"><fmt:message key="planta"/> 0</span> -
						<span onclick="cambiarPlanta(1)" style="cursor:pointer"><fmt:message key="planta"/> 1</span> -
						<span onclick="cambiarPlanta(2)" style="cursor:pointer"><fmt:message key="planta"/> 2</span>
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