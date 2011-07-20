function registro(){
	document.getElementById('loginform').style.display = 'none';
	document.getElementById('registro').style.display = 'block';
}

function registrar(user1, user2, pass1, pass2){
	if((user1 == user2) && (pass1 == pass2)){}
}

/*

<input type="button" value="Registrarse" onclick="registro()"/>

<form action="#" id="registro" style="display:none">
										<table>
											<tr>
												<td>Nombre usuario</td>
												<td><input type="text" name="user1"size="26"/></td>
											</tr>
											<tr>
												<td>Repetir nombre</td>
												<td><input type="text" name="user2"size="26"/></td>
											</tr>
											<tr>
												<td>Contraseña</td>
												<td><input type="password" name="pass1"size="26"/></td>
											</tr>
											<tr>
												<td>Repetir contraseña</td>
												<td><input type="password" name="pass2"size="26"/></td>
											</tr>
											<tr><td><input type="button" value="Aceptar" onclick="registrar(user1.value, user2.value, pass1.value, pass2.value)"/></td></tr>
										</table>
									</form>
*/