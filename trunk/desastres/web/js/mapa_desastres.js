const proyecto = 'desastres';

function mapInit(){
	var center = new google.maps.LatLng(40.416878, -3.703480); // La puerta del sol de Madrid
	var zoom = 12;
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
		showBuilding('firemenStation');
	}
	if(formBuild.policeStation.checked){
		showBuilding('policeStation');
	}
}

function showBuilding(type){
	if(type == 'hospital'){
		generateBuilding('hospital','Hospital Gregorio Marañon', 40.418702, -3.670573);
	}else if(type == 'firemenStation'){
		generateBuilding('firemenStation','Parque de Bomberos', 40.414691, -3.706996);
	}else if(type == 'policeStation'){
		generateBuilding('policeStation','Comisar&iacute;a central', 40.421565, -3.710095);
	}
}

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
			draggable: false // Para que se pueda arrastrar
		};
	}else if (evento.marcador == 'resource'){ // es un recurso
		if(evento.tipo == 'police'){ // es un policia
			icono.url = 'markers/resources/policia' + cantidad + '.png';
		}else if(evento.tipo == 'firemen'){ // es un bombero
			icono.url = 'markers/resources/bombero' + cantidad + '.png';
		}else if(evento.tipo == 'ambulance' || evento.tipo == 'ambulancia'){ // es una ambulancia
			icono.url = 'markers/resources/ambulancia' + cantidad + '.png';
		}else if(evento.tipo == 'nurse'){ // es un enfermero
			icono.url = 'markers/resources/enfermero' + cantidad + '.png';
		}else if(evento.tipo == 'gerocultor'){ // es un gerocultor
			icono.url = 'markers/resources/gerocultor' + cantidad + '.png';
		}else if(evento.tipo == 'assistant'){ // es un auxiliar
			icono.url = 'markers/resources/auxiliar' + cantidad + '.png';
		}else if(evento.tipo == 'otherStaff'){ // otro
			icono.url = 'markers/resources/otroPersonal' + cantidad + '.png';
		}
		opciones = {
			icon: icono,
			draggable: false
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
		icono.anchor = new google.map.Point(13, 43);
		opciones = {
			icon: icono,
			draggable: true // Se pueden arrastrar para asociarlo
		};
	}

	return opciones;
}

function comportamientoMarcador(evento, caracter, opciones){
	var marker = new google.maps.Marker (new google.maps.LatLng(evento.latitud, evento.longitud), opciones);
	// Annadimos el comportamiento
	if(caracter == temporal){ // aqui hay que guardar los datos
		var content = evento.nombre + '<br/>' + evento.info + '<br/>' +evento.descripcion + '<br/>' +
			'<span id="guardar" class="pulsable azul" onclick="guardar(marcadores_temporales[' + evento.id + ']); return false;">Guardar</span>'+ ' - ' +
			'<span id="modificar" class="pulsable azul" onclick="cargarModificar(marcadores_temporales[' + evento.id + '], temporal); return false;">Modificar</span>'+ ' - ' +
			'<span id="eliminar" class="pulsable azul" onclick="eliminar(marcadores_temporales[' + evento.id + '], temporal); return false;">Eliminar</span>';
		
		google.maps.event.addListener(marker, 'click', function(){
			infoWindow.close();
			infoWindow = new google.maps.InfoWindow({content:'<div id="bocadillo">' + content + '</div>'});
			infoWindow.open(map, marker);
		});

		google.maps.event.addListener(marker, 'dragstart', function(){
			infoWindow.close();
		});

		google.maps.event.addListener(marker, 'dragend', function(){
			var asociada = asociar(evento.id, evento.marker);
			infoWindow = new google.maps.InfoWindow({content:'<div id="bocadillo">Es necesario guardar para poder asociar recursos.<br/>' +
				marcadores_definitivos[asociada].nombre + '<br/>' +
				'<span id="guardar_asociacion" class="pulsable azul" onclick="guardar(marcadores_temporales[' + evento.id + ']); return false;">Guardar</span>' + ' - ' +
				'<span id="cancelar" class="pulsable azul" onclick="infoWin2.close();">Cancelar</span>"+"</div>'});
			infoWindow.open(map, marker);
		});
	}else if(caracter == definitivo){ // aqui podemos realizar modificaciones a los ya existentes
		google.maps.event.addListener(marker, 'click', function(){
			var small = evento.nombre + '<br/>' + evento.descripcion;
			var links1;
			if(nivelMsg > 1){
				links1 = '<span id="modificar" class="pulsable azul" onclick="cargarModificar(marcadores_definitivos['+evento.id+'], definitivo); return false;">Modificar</span>'+' - '+
					'<span id="eliminar" class="pulsable azul" onclick="eliminar(marcadores_definitivos['+evento.id+'], definitivo); return false;">Eliminar</span>'+' - '+
					'<span id="ver_mas1" class="pulsable azul" onclick="verMas('+evento.id+');return false;">Ver m&aacute;s</span>';
			}else{
				links1 = '<span id="ver_mas1" class="pulsable azul" onclick="verMas('+evento.id+');return false;">Ver m&aacute;s</span>';
			}

			infoWindow.close();
			infoWindow = new google.maps.InfoWindow({content:'<div id="bocadillo">' + small + '<div id="bocadillo_links">' + links1 +
				'</div><div id="bocadillo_links2"></div></div>'});
			infoWindow.open(map, marker);
		});

		google.maps.event.addListener(marker, 'dragstart', function(){
			infoWindow.close();
		});

		google.maps.event.addListener(marker, 'dragend', function(){
			var asociada = asociar(evento.id, evento.marker);
			infoWindow = new google.maps.InfoWindow({content:'<div id="bocadillo">Asociado a cat&aacute;strofe:<br/>' +
				marcadores_definitivos[asociada].nombre+'<br/>'+
				'<span id="guardar_asociacion" class="pulsable azul" onclick="guardar_asociacion(' + asociada + ',' + evento.id + '); return false;">Guardar</span>' + ' - ' +
				'<span id="cancelar" class="pulsable azul" onclick="cancelar_asignacion(' + evento.id + '); return false;">Cancelar</span></div>'});
			infoWindow.open(map, marker);
		});
	}

	// (!(evento.marcador=='resource' && caracter==1)){
	marker.setMap(map);
	// }

	return marker;
}