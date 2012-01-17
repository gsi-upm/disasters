<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:useBean class="gsi.proyect.ProyectBean" id="proyecto" scope="session"/>

<fmt:bundle basename="fmt.eji8n">
	<!-- Menus from minitabs -->
	<div id="console" class="slideMenu"></div>
	<div id="visualize" class="slideMenu">
		<form id="buildings" action="#">
			<table>
				<tr>
					<td colspan="2"><fmt:message key="edificios"/></td>
				</tr>
				<tr>
					<td><input type="checkbox" name="hospital" value="hospital" checked="checked" onchange="visualize(this.checked,'hospital');"/></td>
					<td><fmt:message key="hospitales"/></td>
				</tr>
				<tr>
					<td><input type="checkbox" name="policeStation" value="policeStation" checked="checked" onchange="visualize(this.checked,'policeStation');"/></td>
					<td><fmt:message key="comisarias"/></td>
				</tr>
				<tr>
					<td><input type="checkbox" name="firemenStation" value="firemenStation" checked="checked" onchange="visualize(this.checked,'firemenStation');"/></td>
					<td><fmt:message key="parquesbomberos"/></td>
				</tr>
				<tr>
					<td id="hideVisualize" class="pulsable" colspan="2"><fmt:message key="ocultar"/></td>
				</tr>
			</table>
		</form>
	</div>
	<c:if test="${proyecto.nombreUsuario != null}">
		<div id="showSimOptions" class="slideMenu">
			<form id="SimOptions" action="/desastres/RunSimulation" method="post">
				<table>
					<tr>
						<td><span class="bigger"><fmt:message key="opcionessimulador"/></span></td>
					</tr>
					<tr>
						<td><input id="runSim" type="radio" name="sim" value="run" checked="checked"/><fmt:message key="arrancasimulador"/></td>
					</tr>
					<tr id="options2">
						<td>
							<fmt:message key="victimas"/> <input type="text" name="victims0" size="3"/>
							<br/>
							<fmt:message key="incendios"/> <input type="text" name="fires0" size="3"/>
							<input type="hidden" name="proyectName" value="disasters"/>
						</td>
					</tr>
					<tr>
						<td><input id="restartSim" type="radio" name="sim" value="restart"/><fmt:message key="reiniciarsimulador"/></td>
					</tr>
					<tr>
						<td><input id="pauseSim" type="radio" name="sim" value="pause"/><fmt:message key="pause"/></td>
					</tr>
					<tr>
						<td><input id="submit_simulador" type="submit" name="aceptar" value="<fmt:message key="aceptar"/>"/></td>
					</tr>
					<tr>
						<td><span id="hideSimOptions" class="pulsable"><fmt:message key="ocultar"/></span></td>
					</tr>
				</table>
			</form>
		</div>
	</c:if>
</fmt:bundle>