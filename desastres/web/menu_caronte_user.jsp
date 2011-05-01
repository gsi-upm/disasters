<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean class="jadex.desastres.ProyectBean" id="proyecto" scope="session"/>

<%@ include file="info_caronte.jsp" %>
<fmt:bundle basename="fmt.eji8n">
	<div id="dhtmlgoodies_tabView1_2">
		<div class="dhtmlgoodies_aTab">
			<input type="hidden" name="marcador" value="event">
			<table class="tabla_menu">
				<tr>
					<th colspan="2">Internos</th>
				</tr>
				<tr>
					<td><fmt:message key="incendio"/> (${fires.rowCount})</td>
					<td><img alt="" src="markers/fuego.png" class="rayas"></td>
				</tr>
				<tr>
					<td><fmt:message key="inundacion"/> (${floods.rowCount})</td>
					<td><img alt="" src="markers/agua.png" class="rayas"></td>
				</tr>
				<tr>
					<td><fmt:message key="derrumbamiento"/> (${collapses.rowCount})</td>
					<td><img alt="" src="markers/casa.png" class="rayas"></td>
				</tr>
				<tr>
					<td><fmt:message key="personaherida"/> (${injuredPeople.rowCount})</td>
					<td><img alt="" src="markers/personaHerida.png" class="rayas"></td>
				</tr>
				<tr>
					<th colspan="2">Externos</th>
				</tr>
				<tr>
					<td><fmt:message key="personaperdida"/> (${lostPeople.rowCount})</td>
					<td><img alt="" src="markers/personaPerdida.png" class="rayas"></td>
				</tr>
			</table>
		</div>
		<div class="dhtmlgoodies_aTab">
			<input type="hidden" name="marcador" value="resource">
			<table class="tabla_menu">
				<tr>
					<td><fmt:message key="policia"/> (${policemen.rowCount})</td>
					<td><img alt="" src="markers/policia1.png" class="rayas"></td>
				</tr>
				<tr>
					<td><fmt:message key="bomberos"/> (${firemen.rowCount})</td>
					<td><img alt="" src="markers/bombero1.png" class="rayas"></td>
				</tr>
				<tr>
					<td><fmt:message key="ambulancia"/> (${ambulance.rowCount})</td>
					<td><img alt="" src="markers/ambulancia1.png" class="rayas"></td>
				</tr>
				<tr>
					<td><fmt:message key="enfermero"/> (${nurse.rowCount})</td>
					<td><img alt="" src="markers/enfermero1.png" class="rayas"></td>
				</tr>
			</table>
		</div>
		<div class="dhtmlgoodies_aTab">
			<table class="tabla_menu">
				<tr>
					<td><fmt:message key="leves"/> (${slight.rowCount})</td>
					<td><img alt="" src="markers/leve1.png" class="rayas"></td>
				</tr>
				<tr>
					<td><fmt:message key="graves"/> (${serious.rowCount})</td>
					<td><img alt="" src="markers/grave1.png" class="rayas"></td>
				</tr>
				<tr>
					<td><fmt:message key="muertos"/> (${dead.rowCount})</td>
					<td><img alt=""src="markers/muerto1.png" class="rayas"></td>
				</tr>
				<tr>
					<td><fmt:message key="atrapados"/> (${trapped.rowCount})</td>
					<td><img alt="" src="markers/trapped1.png" class="rayas"></td>
				</tr>
				<tr>
					<td><fmt:message key="sanos"/> (${healthy.rowCount})</td>
					<td><img alt="" src="markers/sano1.png" class="rayas"></td>
				</tr>
			</table>
		</div>
	</div>
	<!--aqui se cambia el tamanno y titulo de las tabs -->
	<script type="text/javascript">
		initTabs('dhtmlgoodies_tabView1_2',Array('<fmt:message key="eventos"/>','<fmt:message key="recursos"/>','<fmt:message key="victimas"/>'),0,228,490);
	</script>
</fmt:bundle>
