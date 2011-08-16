<%@ page import="org.securityfilter.example.Constants" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<fmt:bundle basename="fmt.eji8n">
	<h2><fmt:message key="iniciarsesion"/></h2>
	<div id="login">
		<form action="<%=response.encodeURL(Constants.LOGIN_FORM_ACTION)%>" method="post" id="loginform">
			<table>
				<tr><td id="regMsg" colspan="2" style="color:lime"></td></tr>
				<tr>
					<td><fmt:message key="usuario"/>:</td>
					<td><input type="text" name="<%=Constants.LOGIN_USERNAME_FIELD%>" id="username" size="26"/></td>
				</tr>
				<tr>
					<td><fmt:message key="contrasenna"/>:</td>
					<td><input type="password" name="<%=Constants.LOGIN_PASSWORD_FIELD%>" id="pwd" size="26"/></td>
				</tr>
				<tr>
					<td colspan="2"><input type="submit" name="Submit" id="submit_butt" value="<fmt:message key="aceptar"/>"/></td>
				</tr>
			</table>
			<p>
				&iquest;No est&aacute; registrado?
				<input type="button" value="Registrarse" onclick="registro()"/>
			</p>
		</form>
		<form action="#" id="registro" style="display:none">
			<table>
				<tr><td id="userError" colspan="2" style="color:red"></td></tr>
				<tr id="user">
					<td>Nombre de usuario</td>
					<td><input type="text" name="user"size="20"/></td>
				</tr>
				<tr><td id="passError" colspan="2" style="color:red"></td></tr>
				<tr id="pass1">
					<td>Contrase&ntilde;a</td>
					<td><input type="password" name="pass1"size="20"/></td>
				</tr>
				<tr id="pass2">
					<td>Repetir contrase&ntilde;a</td>
					<td><input type="password" name="pass2"size="20"/></td>
				</tr>
				<tr id="nombre">
					<td>Nombre real</td>
					<td><input type="text" name="nombre"size="20"/></td>
				</tr>
				<tr id="email">
					<td>Correo electr&oacute;nico</td>
					<td><input type="email" name="email"size="20"/></td>
				</tr>
				<tr>
					<td colspan="2">
						<input type="button" value="Aceptar" onclick="registrar(user.value, pass1.value, pass2.value, nombre.value, email.value)"/>
						<input type="button" value="Cancelar" onclick="cancelarRegistro()"/>
					</td>
				</tr>
			</table>
		</form>
	</div>
</fmt:bundle>