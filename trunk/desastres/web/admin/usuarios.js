function modificar(id){
	document.getElementById('id_edit').innerHTML = document.getElementById('id_' + id).innerHTML;
	document.getElementById('nombre_usuario_edit').innerHTML = document.getElementById('nombre_usuario_' + id).innerHTML;
	document.getElementById('password_edit').innerHTML = document.getElementById('password_' + id).innerHTML;
	document.getElementById('tipo_usuario_edit').value = document.getElementById('tipo_usuario_' + id).innerHTML;
	document.getElementById('nombre_real_edit').value = document.getElementById('nombre_real_' + id).innerHTML;
	document.getElementById('correo_edit').value = document.getElementById('correo_' + id).innerHTML;
	document.getElementById('latitud_edit').value = document.getElementById('latitud_' + id).innerHTML;
	document.getElementById('longitud_edit').value = document.getElementById('longitud_' + id).innerHTML;
	document.getElementById('localizacion_edit').value = document.getElementById('localizacion_' + id).innerHTML;
	document.getElementById('proyecto_edit').innerHTML = document.getElementById('proyecto_' + id).innerHTML;
	document.getElementById('tabla_info').style.display = 'none';
	document.getElementById('tabla_edit').style.display = 'block';
}

function eliminar(id){
	$.post('usuarios_edit.jsp',{
		'accion':'eliminar',
		'id':id
	}, function (){
		limpiar();
		window.location.reload();
	});
}

function nuevo(){
	document.getElementById('tabla_info').style.display = 'none';
	document.getElementById('tabla_crear').style.display = 'block';
}

function aceptar(accion){
	if(accion == 'modificar'){
		$.post('usuarios_edit.jsp',{
			'accion':'modificar',
			'id':document.getElementById('id_edit').innerHTML,
			'tipo_usuario':document.getElementById('tipo_usuario_edit').value,
			'nombre_real':document.getElementById('nombre_real_edit').value,
			'correo':document.getElementById('correo_edit').value,
			'latitud':document.getElementById('latitud_edit').value,
			'longitud':document.getElementById('longitud_edit').value,
			'localizacion':document.getElementById('localizacion_edit').value
		}, function (){
			limpiar();
			window.location.reload();
		});
	}else if(accion == 'crear'){
		$.post('usuarios_edit.jsp',{
			'accion':'crear',
			'nombre_usuario':document.getElementById('nombre_usuario_crear').value,
			'password':document.getElementById('password_crear').value,
			'tipo_usuario':document.getElementById('tipo_usuario_crear').value,
			'nombre_real':document.getElementById('nombre_real_crear').value,
			'correo':document.getElementById('correo_crear').value,
			'latitud':document.getElementById('latitud_crear').value,
			'longitud':document.getElementById('longitud_crear').value,
			'localizacion':document.getElementById('localizacion_crear').value
		}, function (){
			limpiar();
			window.location.reload();
		});
	}
}

function cancelar(){
	document.getElementById('tabla_info').style.display = 'block';
	document.getElementById('tabla_edit').style.display = 'none';
	document.getElementById('tabla_crear').style.display = 'none';
	limpiar();
}

function limpiar(){
	document.getElementById('id_edit').innerHTML = '';
	document.getElementById('nombre_usuario_edit').innerHTML = '';
	document.getElementById('password_edit').innerHTML = '';
	document.getElementById('tipo_usuario_edit').value = '';
	document.getElementById('nombre_real_edit').value = '';
	document.getElementById('correo_edit').value = '';
	document.getElementById('latitud_edit').value = '';
	document.getElementById('longitud_edit').value = '';
	document.getElementById('localizacion_edit').value = '';
	document.getElementById('proyecto_edit').innerHTML = '';

	document.getElementById('nombre_usuario_crear').value = '';
	document.getElementById('password_crear').value = '';
	document.getElementById('tipo_usuario_crear').value = '11';
	document.getElementById('nombre_real_crear').value = '';
	document.getElementById('correo_crear').value = '';
	document.getElementById('latitud_crear').value = '';
	document.getElementById('longitud_crear').value = '';
	document.getElementById('localizacion_crear').value = 'true';
}