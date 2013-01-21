// Javascripts para los formularios
function cambiaIcono(marcador, tipo, cantidad){
	var imagen;
	if(cantidad > 10){
		cantidad = 10;
	}
	if(marcador == 'event'){
		if(tipo == 'fire'){
			imagen = 'markers/events/fuego.png';
		}else if(tipo == 'flood'){
			imagen = 'markers/events/agua.png';
		}else if(tipo == 'collapse'){
			imagen = 'markers/events/casa.png';
		}else if(tipo == 'lostPerson'){
			imagen = 'markers/events/personaPerdida.png';
		}else if(tipo == 'injuredPerson'){
			imagen = 'markers/events/personaHerida.png';
		}
		document.getElementById('icono_catastrofes').src = imagen;
		document.getElementById('catastrofes').nombre.value = fmt(tipo); // en i18n.js
	}else if(marcador == 'resource'){
		if(tipo == 'police'){
			imagen = 'markers/resources/policia' + cantidad + '.png';
		}else if(tipo == 'firemen'){
			imagen = 'markers/resources/bombero' + cantidad + '.png';
		}else if(tipo == 'ambulance' || tipo == 'ambulancia'){
			imagen = 'markers/resources/ambulancia' + cantidad + '.png';
		}else if(tipo == 'nurse'){
			imagen = 'markers/resources/enfermero' + cantidad + '.png';
		}else if(tipo == 'gerocultor'){
			imagen = 'markers/resources/gerocultor' + cantidad + '.png';
		}else if(tipo == 'assistant'){
			imagen = 'markers/resources/auxiliar' + cantidad + '.png';
		}else if(tipo == 'otherStaff'){
			imagen = 'markers/resources/otroPersonal' + cantidad + '.png';
		}
		document.getElementById('icono_recursos').src = imagen;
	}else if(marcador == 'people'){
		if(tipo == 'healthy'){
			imagen = 'markers/people/sano' + cantidad + '.png';
		}else if(tipo == 'slight'){
			imagen = 'markers/people/leve' + cantidad + '.png';
		}else if(tipo == 'serious'){
			imagen = 'markers/people/grave' + cantidad + '.png';
		}else if(tipo == 'dead'){
			imagen = 'markers/people/muerto' + cantidad + '.png';
		}else if(tipo == 'trapped'){
			imagen = 'markers/people/trapped' + cantidad + '.png';
		}
		document.getElementById('icono_heridos').src = imagen;
		document.getElementById('heridos').nombre.value = fmt(tipo); // en i18n.js
		if(tipo == 'healthy'){
			document.getElementById('cantidad').style.display = 'block';
		}else{
			document.getElementById('cantidad').style.display = 'none';
			if(PROYECTO == 'caronte'){
				document.getElementById('heridos').cantidad.value = 1;
			}
		}
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
	localizador.geocode({address:direccion}, function(resultado, estado){
		if(estado == google.maps.GeocoderStatus.OK){
			var point = resultado[0].geometry.location;
			map.setCenter(point, 15);
			document.getElementById('validacion' + numero).src = 'images/iconos/yes.png';
			document.getElementById('validacion' + numero).alt = 'Direcci&oacute;n v&aacute;lida';
			document.getElementById('latitud' + numero).value = point.lat();
			document.getElementById('longitud' + numero).value = point.lng();
		}else{
			document.getElementById('error_texto').innerHTML = 'La siguiente direccion no ha podido ser encontrada: <i>' +
				direccion + '</i>';
			$('#error').jqm().jqmShow();
			document.getElementById('validacion' + numero).src = 'images/iconos/no.png';
		}
	});
}

function pinchaMapa(numero){
	limpiar = false;
	if(numero == 0){
		$('#modificar').jqm().jqmHide();
	} 
	var handler = google.maps.event.addListener(map, 'click', function(point) {
		document.getElementById('latitud' + numero).value = point.latLng.lat().toFixed(6);
		document.getElementById('longitud' + numero).value = point.latLng.lng().toFixed(6);
		if(numero != 0){
			$('#dialog'+numero).jqm().jqmShow();
                        map.setOptions({draggableCursor:'url(images/iconos/openhand.cur), auto'});
		}else{
			$('#modificar').jqm().jqmShow();
			document.getElementById('pincha').innerHTML = 'Posici&oacute;n guardada. &iquest;Otra vez?';
		}
		google.maps.event.removeListener(handler);
	});
}

function iconoAdecuado(marcador,tipo,cantidad){
	var imagen;
	if(marcador == 'event'){
		if(tipo == 'fire'){
			imagen = 'markers/events/fuego.png';
		}else if(tipo == 'flood'){
			imagen = 'markers/events/agua.png';
		}else if(tipo == 'collapse'){
			imagen = 'markers/events/casa.png';
		}else if(tipo == 'lostPerson'){
			imagen = 'markers/events/personaPerdida.png';
		}else if(tipo == 'injuredPerson'){
			imagen = 'markers/events/personaHerida.png';
		}
	}else if(marcador == 'resource'){
		if(tipo == 'police'){
			imagen = 'markers/resources/policia' + cantidad + '.png';
		}else if(tipo == 'firemen'){
			imagen = 'markers/resources/bombero' + cantidad + '.png';
		}else if(tipo == 'ambulance' || tipo == 'ambulancia'){
			imagen = 'markers/resources/ambulancia' + cantidad + '.png';
		}else if(tipo == 'nurse'){
			imagen = 'markers/resources/enfermero' + cantidad + '.png';
		}else if(tipo == 'gerocultor'){
			imagen = 'markers/resources/gerocultor' + cantidad + '.png';
		}else if(tipo == 'assistant'){
			imagen = 'markers/resources/auxiliar' + cantidad + '.png';
		}else if(tipo == 'otherStaff'){
			imagen = 'markers/resources/otroPersoal' + cantidad + '.png';
		}
	}else if(marcador == 'people'){
		if(tipo == 'healthy'){
			imagen = 'markers/people/sano' + cantidad + '.png';
		}else if(tipo == 'slight'){
			imagen = 'markers/people/leve' + cantidad + '.png';
		}else if(tipo == 'serious'){
			imagen = 'markers/people/grave' + cantidad + '.png';
		}else if(tipo == 'dead'){
			imagen = 'markers/people/muerto' + cantidad + '.png';
		}else if(tipo == 'trapped'){
			imagen = 'markers/people/trapped' + cantidad + '.png';
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

// Necesario para los radio en los formularios
function seleccionRadio(form, valor){
	var types = [['fire','flood','collapse','lostPerson','injuredPerson'],
		['police','firemen','ambulance','nurse','gerocultor','assistant'],
		['slight','serious','dead','trapped','healthy']];
	var count;
	for(var i = 0; i < form.tipo.length; i++){
		if(form.tipo[i].checked){
			count = i;
		}
	}
	return types[valor][count];
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