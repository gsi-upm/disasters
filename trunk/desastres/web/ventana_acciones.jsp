<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:bundle basename="fmt.eji8n">
	<!-- Ventana de acciones -->
	<div class="jqmWindow" id="acciones">
		<form id="form-modifica" action="#">
			<table class="tabla_menu">
				<tr><th>Acciones a realizar</th></tr>
				<!-- acciones iniciales -->
				<tr id="apagar"><td><input type="radio" name="accion" value="apagar">Apagar fuego</td></tr>
				<tr id="atender"><td><input type="radio" name="accion" value="atender">Atender herido</td></tr>
				<tr id="evacuar"><td><input type="radio" name="accion" value="evacuar">Evacuar residentes</td></tr>
				<tr id="ayudar"><td><input type="radio" name="accion" value="ayudar">Ayudar (0)</td></tr>
				<tr><td><br></td></tr>
				<!-- acciones intermedias -->
				<tr id="trasladar"><td><input type="radio" name="accion" value="trasladar">Trasladar herido</td></tr>
				<tr id="evacuado"><td><input type="radio" name="accion" value="evacuado">Fin evacuación</td></tr>
				<tr id="volver"><td><input type="radio" name="accion" value="volver">Volver a la residencia</td></tr>
				<tr><td><br></td></tr>
				<!-- acciones finales -->
				<tr id="apagado"><td><input type="radio" name="accion" value="apagado">Fuego pagado</td></tr>
				<tr id="curado"><td><input type="radio" name="accion" value="curado">Herido curado</td></tr>
				<tr id="vuelto"><td><input type="radio" name="accion" value="vuelto">Todos de vuelta</td></tr>
			</table>
			<br>
			<input id="aceptarAccion" type="button" value="Aceptar">
		</form>
	</div>
</fmt:bundle>
