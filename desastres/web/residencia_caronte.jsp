<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean class="jadex.desastres.ProyectBean" id="proyecto" scope="session"/>

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
						<img id="planoResidencia" src="images/residencia/planta0.jpg" alt="Residencia" style="width:611px;height:487px">
						<br>
						<span onclick="cambiarPlanta(0)" style="cursor:pointer"><fmt:message key="planta"/> 0</span> -
						<span onclick="cambiarPlanta(1)" style="cursor:pointer"><fmt:message key="planta"/> 1</span> -
						<span onclick="cambiarPlanta(2)" style="cursor:pointer"><fmt:message key="planta"/> 2</span>
					</th>
					<td><fmt:message key="incendio"/> (${firesResi.rowCount})</td>
					<td><img alt="" src="markers/fuego.png" class="rayas"></td>
					<td><fmt:message key="leves"/> (${slightResi.rowCount})</td>
					<td><img alt="" src="markers/leve1.png" class="rayas"></td>
				</tr>
				<tr>
					<td><fmt:message key="inundacion"/> (${floodsResi.rowCount})</td>
					<td><img alt="" src="markers/agua.png" class="rayas"></td>
					<td><fmt:message key="graves"/> (${seriousResi.rowCount})</td>
					<td><img alt="" src="markers/grave1.png" class="rayas"></td>
				</tr>
				<tr>
					<td><fmt:message key="derrumbamiento"/> (${collapsesResi.rowCount})</td>
					<td><img alt="" src="markers/casa.png" class="rayas"></td>
					<td><fmt:message key="muertos"/> (${deadResi.rowCount})</td>
					<td><img alt=""src="markers/muerto1.png" class="rayas"></td>
				</tr>
				<tr>
					<td><fmt:message key="personaherida"/> (${injuredPeopleResi.rowCount})</td>
					<td><img alt="" src="markers/personaHerida.png" class="rayas"></td>
					<td><fmt:message key="atrapados"/> (${trappedResi.rowCount})</td>
					<td><img alt="" src="markers/trapped1.png" class="rayas"></td>
				</tr>
				<tr>
					<td><fmt:message key="personaperdida"/> (${lostPeopleResi.rowCount})</td>
					<td><img alt="" src="markers/personaPerdida.png" class="rayas"></td>
					<td><fmt:message key="sanos"/> (${healthyResi.rowCount})</td>
					<td><img alt="" src="markers/sano1.png" class="rayas"></td>
				</tr>
			</table>
		</div>
	</div>
	<script type="text/javascript">
		initTabs('dhtmlgoodies_tabView2',Array('<fmt:message key="mapapueblo"/>','<fmt:message key="planoresidencia"/>'),0,null,518);
	</script>
</fmt:bundle>