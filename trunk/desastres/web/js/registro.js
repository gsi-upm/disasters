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

function registrar(user1, user2, pass1, pass2){
	limpiar();
	if((user1 == user2) && (pass1 == pass2)){
		$.get('/desastres/registro',{
				'user':user1,
				'pass':pass1
		}, function(data) {
			if(data == 'ok'){
				document.getElementById('regMsg').innerHTML = 'REGISTRO CORRECTO!';
				document.getElementById('loginform').style.display = 'block';
				document.getElementById('registro').style.display = 'none';
				document.getElementById('username').value = user1;
				document.getElementById('pwd').value = pass1;
			}else{
				document.getElementById('userError').innerHTML = 'EL USUARIO YA EXISTE!';
			}
		});
	}else{
		if(user1 != user2){
			document.getElementById('userError').innerHTML = 'NOMBRE DE USUARIO REPETIDO';
			document.getElementById('user1').style.color = 'red';
			document.getElementById('user2').style.color = 'red';
		}
		if(pass1 != pass2){
			document.getElementById('passError').innerHTML = 'CONTRASEÑA REPETIDA';
			document.getElementById('pass1').style.color = 'red';
			document.getElementById('pass2').style.color = 'red';
		}
	}
}

function borrar(){
	document.getElementById('username').value = '';
	document.getElementById('pwd').value = '';
	document.getElementById('registro').user1.value = '';
	document.getElementById('registro').user2.value = '';
	document.getElementById('registro').pass1.value = '';
	document.getElementById('registro').pass2.value = '';
	document.getElementById('regMsg').innerHTML = '';
	limpiar();
}

function limpiar(){
	document.getElementById('user1').style.color = 'black';
	document.getElementById('user2').style.color = 'black';
	document.getElementById('pass1').style.color = 'black';
	document.getElementById('pass2').style.color = 'black';
	document.getElementById('userError').innerHTML = '';
	document.getElementById('passError').innerHTML = '';
}