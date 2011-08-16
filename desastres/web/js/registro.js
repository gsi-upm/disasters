function registro(){
	borrar();
	document.getElementById('loginform').style.display = 'none';
	document.getElementById('registro').style.display = 'block';
}

function cancelarRegistro(){
	borrar();
	document.getElementById('loginform').style.display = 'block';
	document.getElementById('registro').style.display = 'none';
}

function registrar(user, pass1, pass2, nombre, email){
	limpiar();
	if(user != '' && pass1 != '' && pass2 != '' && nombre != '' && email != ''){
		if(pass1 == pass2){
			$.get('/desastres/registro',{
				'user':user,
				'pass':pass1,
				'nombre':nombre,
				'email':email
			}, function(data) {
				if(data == 'ok'){
					document.getElementById('regMsg').innerHTML = 'REGISTRO CORRECTO!';
					document.getElementById('loginform').style.display = 'block';
					document.getElementById('registro').style.display = 'none';
					document.getElementById('username').value = user;
					document.getElementById('pwd').value = pass1;
				}else{
					document.getElementById('userError').innerHTML = 'EL USUARIO YA EXISTE!';
					document.getElementById('user').style.color = 'red';
				}
			});
		}else{
			document.getElementById('passError').innerHTML = 'CONTRASEÑAS DISTINTAS!';
			document.getElementById('pass1').style.color = 'red';
			document.getElementById('pass2').style.color = 'red';
		}
	}else{
		if(user == '') document.getElementById('user').style.color = 'red';
		if(pass1 == '') document.getElementById('pass1').style.color = 'red';
		if(pass2 == '') document.getElementById('pass2').style.color = 'red';
		if(nombre == '') document.getElementById('nombre').style.color = 'red';
		if(email == '') document.getElementById('email').style.color = 'red';
		if(pass1 != pass2){
			document.getElementById('passError').innerHTML = 'CONTRASEÑAS DISTINTAS!';
			document.getElementById('pass1').style.color = 'red';
			document.getElementById('pass2').style.color = 'red';
		}
	}
}

function borrar(){
	document.getElementById('username').value = '';
	document.getElementById('pwd').value = '';
	document.getElementById('registro').user.value = '';
	document.getElementById('registro').pass1.value = '';
	document.getElementById('registro').pass2.value = '';
	document.getElementById('registro').name.value = '';
	document.getElementById('registro').email.value = '';
	document.getElementById('regMsg').innerHTML = '';
	limpiar();
}

function limpiar(){
	document.getElementById('user').style.color = 'black';
	document.getElementById('pass1').style.color = 'black';
	document.getElementById('pass2').style.color = 'black';
	document.getElementById('nombre').style.color = 'black';
	document.getElementById('email').style.color = 'black';
	document.getElementById('userError').innerHTML = '';
	document.getElementById('passError').innerHTML = '';
}