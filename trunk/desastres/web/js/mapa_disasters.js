function mapInit(){
	var center = new GLatLng(40.416878, -3.703480); // La puerta del sol de Madrid
	var zoom = 11;
	return {'center':center, 'zoom':zoom};
}

function initialize2(){}

function actualizar2(){}

function buildingInit(){
	var formBuild = document.getElementById('buildings');
	if(formBuild.hospital.checked){
		showBuilding('hospital'); // para mostrar los edificios
	}
	if(formBuild.firemenStation.checked){
		showBuilding('fireStation');
	}
	if(formBuild.policeStation.checked){
		showBuilding('policeStation');
	}
}

function showBuilding(type){
	if(type == 'hospital'){
		generateBuilding('hospital','Hospital Gregorio Marañon', 40.418702, -3.670573);
	}else if(type == 'fireStation'){
		generateBuilding('firemenStation','Parque de Bomberos', 40.414691, -3.706996);
	}else if(type == 'policeStation'){
		generateBuilding('policeStation','Comisar&iacute;a central', 40.421565, -3.710095);
	}
}

function definirOpciones(evento){
	var opciones;
	var icono = new GIcon(G_DEFAULT_ICON);

	//MAXIMO DE RECURSOS POR MARCADOR ES 10
	var cantidad;
	if(evento.cantidad > 10){
		cantidad = 10;
	}else{
		cantidad = evento.cantidad;
	}

	if(evento.marcador == 'event'){ // Es un evento
		if(evento.tipo == 'fire'){ // Incendio
			icono.image = 'markers/events/fuego.png';
			if(evento.estado == 'controlled'){
				icono.image ='markers/events/fuego_control.png';
			}
		}else if(evento.tipo == 'flood'){// Inundacion
			icono.image = 'markers/events/agua.png';
			if(evento.estado == 'controlled'){
				icono.image ='markers/events/agua_control.png';
			}
		}else if(evento.tipo == 'collapse'){ // derrumbamiento
			icono.image = 'markers/events/casa.png';
			if(evento.estado == 'controlled'){
				icono.image ='markers/events/casa_control.png';
			}
		}else if(evento.tipo == 'lostPerson'){ // anciano perdido
			icono.image = 'markers/events/personaPerdida.png';
			if(evento.estado == 'controlled'){
				icono.image ='markers/events/personaPerdida_control.png';
			}
		}else if(evento.tipo == 'injuredPerson'){ // anciano herido
			icono.image = 'markers/events/personaHerida.png';
			if(evento.estado == 'controlled'){
				icono.image ='markers/events/personaHerida_control.png';
			}
		}
		opciones = {
			icon: icono,
			zIndexProcess: orden,
			draggable: true // Para que se pueda arrastrar
		};
	}else if (evento.marcador == 'resource'){ // es un recurso
		if(evento.tipo == 'police'){ // es un policia
			icono.image = 'markers/resources/policia' + cantidad + '.png';
		}else if(evento.tipo == 'firemen'){ // es un bombero
			icono.image = 'markers/resources/bombero' + cantidad + '.png';
		}else if(evento.tipo == 'ambulance' || evento.tipo == 'ambulancia'){ // es una ambulancia
			icono.image = 'markers/resources/ambulancia' + cantidad + '.png';
		}else if(evento.tipo == 'nurse'){ // es un enfermero
			icono.image = 'markers/resources/enfermero' + cantidad + '.png';
		}else if(evento.tipo == 'gerocultor'){ // es un gerocultor
			icono.image = 'markers/resources/gerocultor' + cantidad + '.png';
		}else if(evento.tipo == 'assistant'){ // es un auxiliar
			icono.image = 'markers/resources/auxiliar' + cantidad + '.png';
		}else if(evento.tipo == 'otherStaff'){ // otro
			icono.image = 'markers/resources/otroPersonal' + cantidad + '.png';
		}
		opciones = {
			icon: icono,
			zIndexProcess: orden,
			draggable: false
		};
	}else if(evento.marcador == 'people'){ // es una victima
		if(evento.tipo == 'trapped'){ // personas atrapadas
			icono.image = 'markers/people/trapped' + cantidad + '.png';
			if(evento.estado == 'controlled'){
				icono.image = 'markers/people/trapped_control.png';
			}
		}else if(evento.tipo=='healthy'){ // sanos
			icono.image = 'markers/people/sano' + cantidad + '.png';
			if(evento.estado=='controlled'){
				icono.image = 'markers/people/sano_control.png';
			}
		}else if(evento.tipo == 'slight'){ // heridos leves
			icono.image = 'markers/people/leve' + cantidad + '.png';
			if(evento.estado == 'controlled'){
				icono.image = 'markers/people/leve_control.png';
			}
		}else if(evento.tipo == 'serious'){ // heridos graves
			icono.image = 'markers/people/grave' + cantidad + '.png';
			if(evento.estado == 'controlled'){
				icono.image = 'markers/people/grave_control.png';
			}
		}else if(evento.tipo == 'dead'){ // muertos
			icono.image = 'markers/people/muerto' + cantidad + '.png';
			if(evento.estado == 'controlled'){
				icono.image = 'markers/people/muerto_control.png';
			}
		}
		icono.iconSize = new GSize(28, 43);
		icono.iconAnchor = new GPoint(13, 43);
		icono.infoWindowAnchor = new GPoint(13, 0);
		opciones = {
			icon: icono,
			zIndexProcess: orden,
			draggable: true // Se pueden arrastrar para asociarlo
		};
	}

	return opciones;
}

function comportamientoMarcador(evento, caracter, opciones){
	var marker = new GMarker (new GLatLng(evento.latitud, evento.longitud), opciones);

	// Annadimos el comportamiento
	if(caracter == TEMPORAL){ // aqui hay que guardar los datos
		var content = evento.nombre + '<br/>' + evento.info + '<br/>' +evento.descripcion + '<br/>' +
		'<a id="guardar" href="#" onclick="guardar(marcadores_temporales[' + evento.id + ']); return false;">Guardar</a>'+ ' - ' +
		'<a id="modificar" href="#" onclick="cargarModificar(marcadores_temporales[' + evento.id + '],TEMPORAL); return false;">Modificar</a>'+ ' - ' +
		'<a id="eliminar" href="#" onclick="eliminar(marcadores_temporales[' + evento.id + '],TEMPORAL); return false;">Eliminar</a>';

		GEvent.addListener(marker, 'click', function(){
			marker.openInfoWindowHtml('<div id="bocadillo">' + content + '</div>');
		});

		GEvent.addListener(marker, 'dragstart', function(){
			map.closeInfoWindow();
		});

		GEvent.addListener(marker, 'dragend', function(){
			var asociada = asociar(evento.id, evento.marker);
			marker.openInfoWindowHtml('<div id="bocadillo">Es necesario guardar para poder asociar recursos.<br/>' +
				marcadores_definitivos[asociada].nombre + '<br/>' +
				'<a id="guardar_asociacion" href="#" onclick="guardar(marcadores_temporales[' + evento.id + ']); return false;">Guardar</a>' + ' - ' +
				'<a id="cancelar" href="#" onclick="map.closeInfoWindow();">Cancelar</a>"+"</div>');
		});
	}else if(caracter == DEFINITIVO){ // aqui podemos realizar modificaciones a los ya existentes
		GEvent.addListener(marker, 'click', function(){
			var small = evento.nombre + '<br/>' + evento.descripcion;
			var links1 = '<form id="form_acciones" name="form_acciones" action="#">';
			links1 += '</form>';

			/*	'<a id="modificar" href="#" onclick="cargarModificar(marcadores_definitivos['+evento.id+'],DEFINITIVO);return false;">Modificar</a>'+' - '+
				'<a id="acciones" href="#" onclick="cargarAcciones(marcadores_definitivos['+evento.id+'])">Acciones</a>'+' - '+
				'<a id="eliminar" href="#" onclick="eliminar(marcadores_definitivos['+evento.id+'],DEFINITIVO);return false;">Eliminar</a>'+' - '+
				'<a id="ver_mas1" href="#" onclick="verMas('+evento.id+');return false;">Ver m&aacute;s</a>'
			}else{
				links1 = '<a id="ver_mas1" href="#" onclick="verMas('+evento.id+');return false;">Ver m&aacute;s</a><br/>';
			}*/

			marker.openInfoWindowHtml('<div id="bocadillo">' + small + '<div id="bocadillo_links">' + links1 +
				'</div><div id="bocadillo_links2"></div></div>');
		});

		GEvent.addListener(marker, 'dragstart', function(){
			map.closeInfoWindow();
		});

		GEvent.addListener(marker, 'dragend', function(latlng){
			//var asociada = asociar(evento.id, evento.marker);
			/*marker.openInfoWindowHtml('<div id="bocadillo">Asociado a cat&aacute;strofe:<br/>'+
				marcadores_definitivos[asociada].nombre+'<br/>'+
				'<a id="guardar_asociacion" href="#" onclick="guardar_asociacion('+asociada+','+evento.id+');return false;">Guardar</a>'+' - '+
				'<a id="cancelar" href="#" onclick="cancelar_asignacion('+evento.id+');return false;">Cancelar</a></div>');*/

			var nuevaLat = latlng.lat();
			var nuevaLong = latlng.lng();
			var nuevaPos = new GLatLng(nuevaLat, nuevaLong);
			map.removeOverlay(marker);
			marker.setLatLng(nuevaPos);
			map.addOverlay(marker);
			marker.openInfoWindowHtml('<div id="bocadillo">&iquest;Confirmar cambio de posici&oacute;n?<br/><br/>' +
				'<span id="confirmar" class="pulsable azul" onclick="map.closeInfoWindow(); guardar_posicion(' + evento.id +
				',' + nuevaLat + ',' + nuevaLong + ')" >Confirmar</span>'+ ' - ' +
				'<span id="cancelar" class="pulsable azul" onclick="map.closeInfoWindow(); cancelar_asignacion(' + evento.id + ');">Cancelar</span></div>');
			// Temporal*****************************************************************************************
			//cancelar_asignacion(evento.id);
			//**************************************************************************************************

			/*marker.openInfoWindowHtml('<div id="bocadillo">¿Confirmar cambio de posición?<br/>'+
				'<a id="confirmar" href="#" onclick="map.closeInfoWindow();guardar_posicion('+evento.id+
				','+marker.getLatLng().lat()+','+marker.getLatLng().lng()+')">Confirmar</a>'+ ' - '+
				'<a id="cancelar" href="#" onclick="map.closeInfoWindow();">Cancelar</a></div>');*/
		});

		GEvent.addListener(marker, 'infowindowclose', function(){});
	}

	// (!(evento.marcador=='resource' && caracter==1)){
	map.addOverlay(marker);
	// }

	return marker;
}

function escribirMensaje(){}

function registrarHistorial(){}