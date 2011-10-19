// Javascripts para los formularios
function cambiaIcono(marcador, tipo, cantidad){
	var nombre;
	var imagen;
	if(marcador == 'event'){
		if(tipo == 'fire'){
			imagen = 'markers/fuego.png';
			nombre = 'Incencio';
		}else if(tipo == 'flood'){
			imagen = 'markers/agua.png';
			nombre = 'Inundación';
		}else if(tipo == 'collapse'){
			imagen = 'markers/casa.png';
			nombre = 'Derrumbamiento';
		}else if(tipo == 'lostPerson'){
			imagen = 'markers/personaPerdida.png';
			nombre = 'Persona perdida';
		}else if(tipo == 'injuredPerson'){
			imagen = 'markers/personaHerida.png';
			nombre = 'Persona herida';
		}
		document.getElementById('icono_catastrofes').src = imagen;
		document.getElementById('catastrofes').nombre.value = nombre;
	}else if(marcador == 'resource'){
		if(tipo == 'police'){
			imagen = 'markers/policia' + cantidad + '.png';
		}else if(tipo == 'firemen'){
			imagen = 'markers/bombero' + cantidad + '.png';
		}else if(tipo == 'ambulance' || tipo == 'ambulancia'){
			imagen = 'markers/ambulancia' + cantidad + '.png';
		}else if(tipo == 'nurse'){
			imagen = 'markers/enfermero' + cantidad + '.png';
		}else if(tipo == 'gerocultor'){
			imagen = 'markers/gerocultor' + cantidad + '.png';
		}else if(tipo == 'assistant'){
			imagen = 'markers/auxiliar' + cantidad + '.png';
		}else if(tipo == 'otherStaff'){
			imagen = 'markers/otro' + cantidad + '.png';
		}
		document.getElementById('icono_recursos').src = imagen;
	}else if(marcador == 'people'){
		if(tipo == 'healthy'){
			imagen = 'markers/sano' + cantidad + '.png';
			nombre = 'Sano';
		}else if(tipo == 'slight'){
			imagen = 'markers/leve' + cantidad + '.png';
			nombre = 'Leve';
		}else if(tipo == 'serious'){
			imagen = 'markers/grave' + cantidad + '.png';
			nombre = 'Grave';
		}else if(tipo == 'dead'){
			imagen = 'markers/muerto' + cantidad + '.png';
			nombre = 'Muerto';
		}else if(tipo == 'trapped'){
			imagen = 'markers/trapped' + cantidad + '.png';
			nombre = 'Atrapado';
		}
		document.getElementById('icono_heridos').src = imagen;
		document.getElementById('heridos').nombre.value = nombre;
	}	
}
	
function cambiaFlecha(i, numero){
	if(i == 0){
		document.getElementById('validardireccion'+numero).src = 'images/iconos/confirm.png';
	}else if(i == 1){
		document.getElementById('validardireccion'+numero).src = 'images/iconos/confirm2.png';
	}
} 	
	
function validarDireccion(numero){
	var direccion = document.getElementById('direccion' + numero).value;
	localizador.getLatLng(direccion, function(point){
		if(!point){
			document.getElementById('error_texto').innerHTML = 'La siguiente direccion no ha podido ser encontrada: <i>' +
				direccion + '</i>';
			$('#error').jqm().jqmShow();
			document.getElementById('validacion'+numero).src = 'images/iconos/no.png';
		}else{
			map.setCenter(point, 15);
			document.getElementById('validacion' + numero).src = 'images/iconos/yes.png';
			document.getElementById('validacion' + numero).alt = 'Direcci&oacute;n v&aacute;lida';
			document.getElementById('latitud' + numero).value = point.lat();
			document.getElementById('longitud' + numero).value = point.lng();
			//Esto es el puntero provisional
			/*var marker = new GMarker(point);
       		map.addOverlay(marker);
       		marker.openInfoWindowHtml('<b>My house</b> <br/> Calle embajadores, 181');*/
		}
	});
}

function pinchaMapa(numero){
	limpiar = false;
	if(numero == 0){
		$('#modificar').jqm().jqmHide();
	}
	var handler = GEvent.addListener(map, 'click', function(overlay,point) {
		document.getElementById('latitud' + numero).value = point.lat();
		document.getElementById('longitud' + numero).value = point.lng();
		if(numero != 0){
			$('#dialog'+numero).jqm().jqmShow();
		}else{
			$('#modificar').jqm().jqmShow();
			document.getElementById('pincha').innerHTML = 'Posici&oacute;n guardada. &iquest;Otra vez?';
		}
		GEvent.removeListener(handler);
	});
}

function iconoAdecuado(marcador,tipo,cantidad){
	var imagen;
	if(marcador == 'event'){
		imagen = 'markers/fuego.png'
		if(tipo == 'fire'){
			imagen = 'markers/fuego.png';
		}else if(tipo == 'flood'){
			imagen = 'markers/agua.png';
		}else if(tipo == 'collapse'){
			imagen = 'markers/casa.png';
		}else if(tipo == 'lostPerson'){
			imagen = 'markers/personaPerdida.png';
		}else if(tipo == 'injuredPerson'){
			imagen = 'markers/personaHerida.png';
		}
	}else if(marcador == 'resource'){
		imagen = 'markers/policia1.png';
		if(tipo == 'police'){
			imagen = 'markers/policia' + cantidad + '.png';
		}else if(tipo == 'firemen'){
			imagen = 'markers/bombero' + cantidad + '.png';
		}else if(tipo == 'ambulance' || tipo=='ambulancia'){
			imagen = 'markers/ambulancia' + cantidad + '.png';
		}else if(tipo == 'nurse'){
			imagen = 'markers/enfermero' + cantidad + '.png';
		}else if(tipo == 'gerocultor'){
			imagen = 'markers/gerocultor' + cantidad + '.png';
		}else if(tipo == 'assistant'){
			imagen = 'markers/auxiliar' + cantidad + '.png';
		}else if(tipo == 'otherStaff'){
			imagen = 'markers/otro' + cantidad + '.png';
		}
	}else if(marcador == 'people'){
		imagen = 'markers/leve1.png';
		if(tipo == 'trapped'){
			imagen = 'markers/trapped' + cantidad + '.png';
		}else if(tipo == 'healthy'){
			imagen = 'markers/sano' + cantidad + '.png';
		}else if(tipo == 'slight'){
			imagen = 'markers/leve' + cantidad + '.png';
		}else if(tipo == 'serious'){
			imagen = 'markers/grave' + cantidad + '.png';
		}else if(tipo == 'dead'){
			imagen = 'markers/muerto' + cantidad + '.png';
		}
	}
	return imagen;
}

function borrarFormulario(form, numero){
	form.nombre.value = 'nombre';
	form.descripcion.value = 'descripcion';
	form.info.value = 'info';
	form.cantidad.value = '1';
	form.direccion.value = 'direccion';
	form.latitud.value = 0;
	form.longitud.value = 0;
	document.getElementById('validacion' + numero).src = 'images/iconos/no.png'
}

function menuIdiomas(accion){
	if(accion == 'abrir'){
		document.getElementById('langSelect').style.display='inline';
		document.getElementById('langInit').style.display='none';
	}else if(accion == 'cerrar'){
		document.getElementById('langSelect').style.display='none';
		document.getElementById('langInit').style.display='inline';
	}
}