const proyecto = 'caronte';

/**
 * Devuelve los valores iniciales del centro del mapa y su zoom
 * 
 * @return Centro y zoom
 */
function mapInit(){
	var center = new google.maps.LatLng(38.5238697889187, -0.1703082025051117); //Villajoyosa, Alicante, Valencia, España
	var zoom = 21;
	return {'center':center, 'zoom':zoom};
}

/**
 * Ejecucion adicional a initialize()
 */
function initialize2(){
	mostrarMensajes(); // en mensajes.js

	if(nivelMsg > 1){
		if(document.getElementById('opcionesMapa').verSanos.checked){
			verSanos = true;
		}
	}

	if(userName != '' && nivelMsg > 1){
		plantaResidencia = 0;
		var url = 'markers/residencia/planta'+ plantaResidencia + '.png';
                var bounds = new google.maps.LatLngBounds(new google.maps.LatLng(38.5231, -0.170724), new google.maps.LatLng(38.5239796, -0.16964));//Suroeste, Noreste
                residencia = new google.maps.GroundOverlay(url, bounds, {clickable: false});
                
		google.maps.event.addListener(map, 'zoom_changed', function(){
			residencia.setMap(null);
			var tipoMapa = map.getMapTypeId();
			var newZoom = map.getZoom();
			if(newZoom >= 19 && tipoMapa == roadmap && plantaResidencia >= 0){
				residencia.setMap(map);
			}else if(newZoom < 19 && plantaResidencia != -2){
				cambiarPlanta(-2);
			}
		});

		google.maps.event.addListener(map, 'maptypeid_changed', function(){
			residencia.setMap(null);
			var tipoMapa = map.getMapTypeId();
			if(map.getZoom() >= 19 && tipoMapa == roadmap && plantaResidencia >= 0){
				residencia.setMap(map);
			}
		});
		
		google.maps.event.addListener(residencia, 'click', function(){
			if(infoWindow != null){
				infoWindow.close();
				infoWindow = null;
				if(infoWinMarker != 'building'){
					limpiarLateral(infoWinMarker);
				}
			}
		});

		residencia.setMap(map);

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
			'tipo':'todasEmergencias',
			'nivel':nivelMsg
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

	/*if(localizacion == true){
		navigator.geolocation.getCurrentPosition(coordenadasUsuario); // coordenadasUsuario();
	}*/
	
	if(userName != '' && usuario_actual_tipo != 'citizen'){
		numeroMarcadores(0, true);
	}
}

/**
 * Ejecucion adicional a actualizar()
 */
function actualizar2(){
	mostrarMensajes2(); // en mensajes.js

	if(userName != ''){
		numeroMarcadores(plantaResidencia);
	}

	/*contador++;
	if(localizacion == true && nivelMsg > 0 && contador >= 3){
		contador = 0;
		navigator.geolocation.getCurrentPosition(coordenadasUsuario); // coordenadasUsuario();
	}*/
}

/**
 * Genera los edificios que se muestran en el mapa al inicio
 */
function buildingInit(){
	showBuilding('hospital');
	showBuilding('firemenStation');
	showBuilding('policeStation');
	showBuilding('geriatricCenter');
}

/**
 * Muestra en el mapa un tipo de edificio
 * 
 * @type Tipo de edificio a mostrar
 */
function showBuilding(type){
    // Villajoyosa, Alicante, Valencia, España
	if(type == 'hospital'){
		generateBuilding('hospital', 'Centro de salud', 38.506902, -0.231121);
                generateBuilding('hospital', 'Cruz Roja', 38.505762, -0.229287);
	}else if(type == 'firemenStation'){
		generateBuilding('firemenStation', 'Parque de bomberos', 38.505609, -0.233229);
	}else if(type == 'policeStation'){
		generateBuilding('policeStation', 'Ayuntamiento y Polic&iacute;a municipal', 38.510397,-0.231851); 
                generateBuilding('policeStation', 'Guardia Civil', 38.504637, -0.2373557);
	}else if(type == 'geriatricCenter'){
		generateBuilding('geriatricCenter', 'Residencia Ballesol Costa Blanca Senior Resort', 38.5238697889187, -0.1703082025051117); 
	}
}

/**
 * Define las opciones de un tipo de marcador segun sea el evento
 * 
 * @param evento Objeto marcador del que se quieren las opciones
 * @return Opciones del marcador
 */
function definirOpciones(evento){
	var opciones;
	var icono = new google.maps.MarkerImage(null);

	//MAXIMO DE RECURSOS POR MARCADOR ES 10
	var cantidad;
	if(evento.cantidad > 10){
		cantidad = 10;
	}else{
		cantidad = evento.cantidad;
	}

	if(evento.marcador == 'event'){ // Es un evento
		if(evento.tipo == 'fire'){ // Incendio
			icono.url = 'markers/events/fuego.png';
			if(evento.estado == 'controlled'){
				icono.url ='markers/events/fuego_control.png';
			}
		}else if(evento.tipo == 'flood'){// Inundacion
			icono.url = 'markers/events/agua.png';
			if(evento.estado == 'controlled'){
				icono.url ='markers/events/agua_control.png';
			}
		}else if(evento.tipo == 'collapse'){ // derrumbamiento
			icono.url = 'markers/events/casa.png';
			if(evento.estado == 'controlled'){
				icono.url ='markers/events/casa_control.png';
			}
		}else if(evento.tipo == 'lostPerson'){ // anciano perdido
			icono.url = 'markers/events/personaPerdida.png';
			if(evento.estado == 'controlled'){
				icono.url ='markers/events/personaPerdida_control.png';
			}
		}else if(evento.tipo == 'injuredPerson'){ // anciano herido
			icono.url = 'markers/events/personaHerida.png';
			if(evento.estado == 'controlled'){
				icono.url ='markers/events/personaHerida_control.png';
			}
		}
		opciones = {
			icon: icono,
			draggable: true // Para que se pueda arrastrar
		};
	}else if(evento.marcador == 'resource'){ // es un recurso
		var actuando = '';
		if(evento.estado == 'active'){
			actuando = '_no';
		}

		if(evento.nombre == userName){
			icono.url = 'markers/resources/user' + actuando + '.png';
		}else if(evento.tipo == 'police'){ // es un policia
			icono.url = 'markers/resources/policia' + actuando + '.png';
		}else if(evento.tipo == 'firemen'){ // es un bombero
			icono.url = 'markers/resources/bombero' + actuando + '.png';
		}else if(evento.tipo == 'ambulance' || evento.tipo == 'ambulancia'){ // es una ambulancia
			icono.url = 'markers/resources/ambulancia' + actuando + '.png';
		}else if(evento.tipo == 'nurse'){ // es un enfermero
			icono.url = 'markers/resources/enfermero' + actuando + '.png';
		}else if(evento.tipo == 'gerocultor'){ // es un gerocultor
			icono.url = 'markers/resources/gerocultor' + actuando + '.png';
		}else if(evento.tipo == 'assistant'){ // es un auxiliar
			icono.url = 'markers/resources/auxiliar' + actuando + '.png';
		}else if(evento.tipo == 'otherStaff'){ // otro
			icono.url = 'markers/resources/otroPersonal' + actuando + '.png';
		}
		opciones = {
			icon: icono,
			draggable: (evento.nombre == userName) // Arrastrar si soy yo
		};
	}else if(evento.marcador == 'people'){ // es una victima
		if(evento.tipo == 'trapped'){ // personas atrapadas
			icono.url = 'markers/people/trapped' + cantidad + '.png';
			if(evento.estado == 'controlled'){
				icono.url = 'markers/people/trapped_control.png';
			}
		}else if(evento.tipo=='healthy'){ // sanos
			icono.url = 'markers/people/sano' + cantidad + '.png';
			if(evento.estado=='controlled'){
				icono.url = 'markers/people/sano_control.png';
			}
		}else if(evento.tipo == 'slight'){ // heridos leves
			icono.url = 'markers/people/leve' + cantidad + '.png';
			if(evento.estado == 'controlled'){
				icono.url = 'markers/people/leve_control.png';
			}
		}else if(evento.tipo == 'serious'){ // heridos graves
			icono.url = 'markers/people/grave' + cantidad + '.png';
			if(evento.estado == 'controlled'){
				icono.url = 'markers/people/grave_control.png';
			}
		}else if(evento.tipo == 'dead'){ // muertos
			icono.url = 'markers/people/muerto' + cantidad + '.png';
			if(evento.estado == 'controlled'){
				icono.url = 'markers/people/muerto_control.png';
			}
		}
		icono.size = new google.maps.Size(28, 43);
		icono.anchor = new google.maps.Point(13, 43);
		opciones = {
			icon: icono,
			draggable: true // Se pueden arrastrar para asociarlo
		};
	}

	return opciones;
}

/**
 * Crea un marcador y le dota de diferentes eventos
 * 
 * @param evento Objeto marcador con el que se generara el marcador
 * @param caracter Si el marcador es definitivo o temporal
 * @param opciones Opcones del marcador
 * @retunr Marcador
 */
function comportamientoMarcador(evento, caracter, opciones){
	var opts = {
		position: new google.maps.LatLng(evento.latitud, evento.longitud),
		icon: opciones.icon,
		draggable: opciones.draggable
	};
	var marker = new google.maps.Marker(opts);
	// Annadimos el comportamiento
	google.maps.event.addListener(marker, 'click', function(){
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
		
		if(infoWindow != null){
			infoWindow.close();
			if(infoWinMarker != 'building' && infoWinMarker != evento.marcador){
				limpiarLateral(infoWinMarker, true);
			}
		}
		infoWindow = new google.maps.InfoWindow({content:'<div id="bocadillo">' + small + '<div id="bocadillo_links">' +
			links1 + '</div><div id="bocadillo_links2"></div></div>'});
		infoWinMarker = evento.marcador;
		infoWindow.open(map, marker);
		
		google.maps.event.addListener(infoWindow, 'closeclick', function(){
			limpiarLateral(evento.marcador);
		});
		
		cargarLateral(evento); // en mapa_caronte2.js
	});

	google.maps.event.addListener(marker, 'dragstart', function(){
		noActualizar = evento.id;
		if(infoWindow != null){
			infoWindow.close();
		}
	});

	google.maps.event.addListener(marker, 'dragend', function(punto){
		var nuevaLat = punto.latLng.lat().toFixed(6);
		var nuevaLong = punto.latLng.lng().toFixed(6);
		
		infoWindow = new google.maps.InfoWindow({content:'<div id="bocadillo">&iquest;Confirmar cambio de posici&oacute;n?<br/><br/>' +
			'<span id="confirmar" class="pulsable azul" onclick="infoWindow.close(); guardar_posicion(' + evento.id +
			',' + nuevaLat + ',' + nuevaLong + ')" >Confirmar</span>'+ ' - ' +
			'<span id="cancelar" class="pulsable azul" onclick="infoWindow.close(); cancelar_asignacion(' + evento.id + ');">Cancelar</span></div>'});
		infoWindow.open(map, marker);
		
		google.maps.event.addListener(infoWindow, 'closeclick', function(){
			cancelar_asignacion(evento.id);
			limpiarLateral(evento.marcador);
		});
	});

	if((evento.planta == plantaResidencia || evento.planta == -2 || plantaResidencia == -2) && (evento.tipo != 'healthy' || verSanos == true)){
		marker.setMap(map);
	}

	return marker;
}