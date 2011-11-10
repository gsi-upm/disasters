function mapInit(){
	var center = new GLatLng(38.232272, -1.698925); // Calasparra, Murcia (geriatrico)
	var zoom = 21;
	return {'center':center, 'zoom':zoom};
}

function initialize2(){
	mostrarMensajes(); // en mensajes.js

	if(nivelMsg > 1){
		if(document.getElementById('opcionesMapa').verSanos.checked){
			verSanos = true;
		}
	}

	if(userName != '' && nivelMsg > 1){
		plantaResidencia = 0;
		var icono = new GIcon();
		icono.image = 'markers/residencia/planta'+ plantaResidencia + '.png';
		icono.iconSize = new GSize(733, 585);
		icono.iconAnchor = new GPoint(367, 305);
		var opciones = {
			icon:icono,
			clickable:false,
			zIndexProcess:fondo
		};
		residencia = new GMarker(new GLatLng(38.232272,-1.698925), opciones);

		GEvent.addListener(map, 'zoomend', function(oldZoom, newZoom){
			map.removeOverlay(residencia);
			var tipoMapa = map.getCurrentMapType().getName();
			switch(newZoom){
				case 21:
					residencia.getIcon().iconSize = new GSize(733, 585);
					residencia.getIcon().iconAnchor = new GPoint(367, 305);
					break;
				case 20:
					residencia.getIcon().iconSize = new GSize(367, 293);
					residencia.getIcon().iconAnchor = new GPoint(183, 155);
					break;
				default:
					cambiarPlanta(-2);
			}
			if(newZoom >= 20 && tipoMapa == 'Mapa' && plantaResidencia >= 0){
				map.addOverlay(residencia);
			}
		});

		GEvent.addListener(map, 'maptypechanged', function(){
			map.removeOverlay(residencia);
			var tipoMapa = map.getCurrentMapType().getName();
			if(map.getZoom() >= 20 && tipoMapa == 'Mapa' && plantaResidencia >= 0){
				map.addOverlay(residencia);
			}
		});

		map.addOverlay(residencia);

		if(localizacion == true){ // si el navegador soporta geolocalizacion (valor inicial)
			$.getJSON('getpost/getLatLong.jsp',{
				'nombre':userName
			}, function(data){
				localizacion = data[0].localizacion; // entonces valor por defecto del usuario
				document.getElementById('form-posicion').localizacion.checked = localizacion;
			});
		}else{
			document.getElementById('form-posicion').localizacion.style.display = 'none';
		}

		$.getJSON('getpost/getAsociaciones.jsp', {
			'tipo':'todasEmergencias'
		}, function(data){
			$.each(data, function(entryIndex, entry) {
				document.getElementById('checkboxAsoc').innerHTML += '<li><input type="checkbox" name="assigned' + entry.id +
					'" onclick="asociarEmergencia(' + entry.id + ', assigned' + entry.id + '.checked)"/>' +
					entry.id +' - ' + entry.nombre + '</li>';
				emergenciasAsociadas[0].push([entry.id, false]);
				emergenciasAsociadas[1].push([entry.id, false]);
			});
			if(document.getElementById('checkboxAsoc').innerHTML == ''){
				document.getElementById('textoAsoc').innerHTML = '&thinsp;No hay emergencias para asociar';
			}else{
				document.getElementById('textoAsoc').innerHTML = '&emsp;V';
			}
		});

		numeroMarcadores(plantaResidencia);
	}

	if(localizacion == true){
		// PRUEBAAA!!! ***********************************************************************
		coordenadasUsuario(); // navigator.geolocation.getCurrentPosition(coordenadasUsuario);
		//************************************************************************************
	}
}

function actualizar2(){
	mostrarMensajes2(); // en mensajes.js

	if(userName != ''){
		numeroMarcadores(plantaResidencia);
	}

	contador++;
	if(localizacion == true && nivelMsg > 0 && contador >= 3){
		contador = 0;
		// PRUEBAAA!!! ***********************************************************************
		coordenadasUsuario(); // navigator.geolocation.getCurrentPosition(coordenadasUsuario);
		//************************************************************************************
	}
}

function buildingInit(){
	showBuilding('hospital');
	showBuilding('fireStation');
	showBuilding('policeStation');
	showBuilding('geriatricCenter');
}

function showBuilding(type){
	if(type == 'hospital'){
		generateBuilding('hospital','Centro de salud',38.228138,-1.706449); // Calasparra, Murcia
	}else if(type == 'fireStation'){
		generateBuilding('firemenStation','Parque de bomberos',38.111020,-1.865018); // Caravaca de la Cruz, Murcia
		generateBuilding('firemenStation','Parque de bomberos TEMPORAL',38.21602,-1.72306); // TEMPORAL
	}else if(type == 'policeStation'){
		generateBuilding('policeStation','Ayuntamiento y Polic&iacute;a municipal',38.231125,-1.697560); // Calasparra, Murcia
	}else if(type == 'geriatricCenter'){
		generateBuilding('geriatricCenter','Residencia Virgen de la Esperanza',38.232272,-1.698925); // Calasparra, Murcia
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
		var actuando = '';
		if(evento.estado == 'active'){
			actuando = '_no';
		}

		if(evento.tipo == 'police'){ // es un policia
			icono.image = 'markers/resources/policia' + actuando + '.png';
		}else if(evento.tipo == 'firemen'){ // es un bombero
			icono.image = 'markers/resources/bombero' + actuando + '.png';
		}else if(evento.tipo == 'ambulance' || evento.tipo == 'ambulancia'){ // es una ambulancia
			icono.image = 'markers/resources/ambulancia' + actuando + '.png';
		}else if(evento.tipo == 'nurse'){ // es un enfermero
			icono.image = 'markers/resources/enfermero' + actuando + '.png';
		}else if(evento.tipo == 'gerocultor'){ // es un gerocultor
			icono.image = 'markers/resources/gerocultor' + actuando + '.png';
		}else if(evento.tipo == 'assistant'){ // es un auxiliar
			icono.image = 'markers/resources/auxiliar' + actuando + '.png';
		}else if(evento.tipo == 'otherStaff'){ // otro
			icono.image = 'markers/resources/otroPersonal' + actuando + '.png';
		}
		opciones = {
			icon: icono,
			zIndexProcess: orden,
			draggable: (evento.nombre == userName) // Arrastrar si soy yo
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
		icono.infoWindowAnchor = new GPoint(13, 2);
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
	GEvent.addListener(marker, 'click', function(){
		var small = evento.nombre + '<br/>' + evento.descripcion;
		var links1 = '<form id="form_acciones" name="form_acciones" action="#">';
		if(nivelMsg > 1){
			if(evento.marcador != 'resource'){
				links1 += cargarMenuAcciones(marcadores_definitivos[evento.id]); // en mapa_caronte2.js
			}else{
				links1 += cargarListaActividades(marcadores_definitivos[evento.id]); // en mapa_caronte2.js
			}
		}
		links1 += '</form>';

		marker.openInfoWindowHtml('<div id="bocadillo">' + small + '<div id="bocadillo_links">' + links1 +
			'</div><div id="bocadillo_links2"></div></div>');
		cargarLateral(evento); // en mapa_caronte2.js
	});

	GEvent.addListener(marker, 'dragstart', function(){
		noActualizar = evento.id;
		map.closeInfoWindow();
	});

	GEvent.addListener(marker, 'dragend', function(latlng){
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
	});

	GEvent.addListener(marker, 'infowindowclose', function() {
		limpiarLateral(evento.marcador); // en mapa_caronte2.js
	});

	if((evento.planta == plantaResidencia || evento.planta == -2 || plantaResidencia == -2) && (evento.tipo != 'healthy' || verSanos == true)){
		map.addOverlay(marker);
	}

	return marker;
}