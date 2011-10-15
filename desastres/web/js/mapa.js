var map;
var center;
var localizador;
var marcadores_definitivos;
var marcadores_temporales;
var indices;
var ultimamodif;
var timeout;

var hospitals;
var hospIndex = 0;
var firemenStations;
var fireIndex = 0;
var policeStations;
var policeIndex = 0;
var geriatricCenters;
var geriatricIndex = 0;

var pos_def;
var pos_temp;
var pos_indices;
var puntero_temp;
var caracter_temp;

var TEMPORAL = 0;
var DEFINITIVO = 1;

var ANONYMOUS = 1;
var usuario_actual;
var usuario_actual_tipo;

var tiempoActualizacion = 1500;
var tiempoInicial = 5000;
var contador = 0;

//var userName = null;
var latitudUser = 0;
var longitudUser = 0;

var verSanos = false;
var limpiar = true;
var localizacion = navigator.geolocation;
var resi; // marcador de la imagen de la residencia
var centroAux = new Array();
var puntoAux;

var plantaResidencia = -1;
var emergenciasAsociadas = [new Array(), new Array()];

function initialize(){
	if(GBrowserIsCompatible()){
		map = new GMap2(document.getElementById('map_canvas'));
		localizador = new GClientGeocoder();
		mapInit(); // en mapa_xxx.js

		// empezamos con un usuario anonimo, permitir registrarse mas tarde
		usuario_actual = ANONYMOUS;
		usuario_actual_tipo = 'citizen';
		map.addControl(new GLargeMapControl()); // controles completos
		map.addControl(new GScaleControl ());   // escala
		map.addControl(new GMapTypeControl());  // mapa, foto, hibrido

		GEvent.addListener(map, 'zoomend', function(oldZoom, newZoom){
			map.removeOverlay(resi);
			var tipoMapa = map.getCurrentMapType().getName();
			switch(newZoom){
				case 21:
					resi.getIcon().iconSize = new GSize(733, 585);
					resi.getIcon().iconAnchor = new GPoint(367, 305);
					break;
				case 20:
					resi.getIcon().iconSize = new GSize(367, 293);
					resi.getIcon().iconAnchor = new GPoint(183, 155);
					break;
				default:
					cambiarPlanta(-2);
			}
			if(newZoom >= 20 && tipoMapa == 'Mapa' && plantaResidencia >= 0){
				map.addOverlay(resi);
			}
		});

		GEvent.addListener(map, 'maptypechanged', function(){
			map.removeOverlay(resi);
			var tipoMapa = map.getCurrentMapType().getName();
			if(map.getZoom() >= 20 && tipoMapa == 'Mapa' && plantaResidencia >= 0){
				map.addOverlay(resi);
			}
		});

		var icono = new GIcon();
		icono.image = 'images/residencia/planta0.png';
		icono.iconSize = new GSize(733, 585);
		icono.iconAnchor = new GPoint(367, 305);
		var opciones = {
			icon:icono,
			clickable:false,
			zIndexProcess:fondo
		};
		resi = new GMarker(new GLatLng(38.232272,-1.698925), opciones);

		//inicializamos los arrays
		marcadores_definitivos = new Array();
		pos_def = 0;
		marcadores_temporales = new Array();
		pos_temp = 0;
		hospitals = new Array();
		policeStations = new Array();
		firemenStations = new Array();
		geriatricCenters = new Array();

		indices = new Array();
		pos_indices = 0;

		buildingInit(); // en mapa_xxx.js

		ultimamodif = '1992-01-01 00:00:01'; //(una fecha antigua para empezar)
		//hacemos la peticion inicial del json (baja todo menos los borrados)
		$.getJSON('leeEventos.jsp', {
			'fecha': ultimamodif,
			'action':'firstTime',
			'nivel':nivelMsg
		}, function(data) {
			$.each(data, function(entryIndex, entry) {
				var nuevomarcador = new ObjMarcador(entry['id'],entry['item'],entry['type'],
					entry['quantity'],entry['name'],entry['description'],entry['info'],
					entry['latitud'],entry['longitud'],entry['address'],entry['state'],
					entry['date'], entry['modified'],entry['user_name'],entry['user_type'],
					null,entry['size'],entry['traffic'],entry['idAssigned'],entry['floor'],null);

				nuevomarcador.marker = generaMarcador(nuevomarcador, DEFINITIVO);
				marcadores_definitivos[nuevomarcador.id] = nuevomarcador;
				indices[pos_indices] = nuevomarcador.id;
				pos_indices++;
			});
		});
		ultimamodif = obtiene_fecha();
		initialize2(); // en mapa_xxx.js
		
		if(userName != ''){
			plantaResidencia = 0;
			map.addOverlay(resi);

			if(localizacion == null){
				document.getElementById('form-posicion').localizacion.style.display = 'none';
			}

			$.getJSON('getLatLong.jsp',{
				'nombre':userName
			}, function(data){
				$.each(data, function(entryIndex, entry){
					latitudUser = entry['latitud'];
					longitudUser = entry['longitud'];
					if(localizacion){ // si el navegador soporta geolocalizacion (valor inicial)
						localizacion = entry['localizacion']; // entonces valor por defecto del usuario
						document.getElementById('form-posicion').localizacion.checked = localizacion;
					}
				});
			});
			
			$.getJSON('getAsociaciones.jsp', {
				'tipo':'todasEmergencias'
			}, function(data) {
				$.each(data, function(entryIndex, entry) {
					document.getElementById('checkboxAsoc').innerHTML += '<li><input type="checkbox" name="assigned' + entry['id'] +
						'" onclick="asociarEmergencia(' + entry['id'] + ', assigned' + entry['id'] + '.checked)"/>' +
						entry['id'] +' - ' + entry['nombre'] + '</li>';
					emergenciasAsociadas[0].push([entry['id'], false]);
					emergenciasAsociadas[1].push([entry['id'], false]);
				});
				if(document.getElementById('checkboxAsoc').innerHTML == ''){
					document.getElementById('checkboxAsoc').innerHTML = 'No hay emergencias para asociar';
				}
			});
		}

		if(localizacion){
			navigator.geolocation.getCurrentPosition(coordenadasUsuario);
		}

		setTimeout('moveAgents()',2000);
		setTimeout('actualizar()',tiempoInicial);
	}
}

function coordenadasUsuario(pos){
	// PRUEBAAA!!! ***************************************************************
	latitudUser = 38.232272 + (2*Math.random()-1)*0.0001 // pos.coords.latitude;
	longitudUser = -1.698925 + (2*Math.random()-1)*0.0001 // pos.coords.longitude;
	//****************************************************************************
	
	if(nivelMsg==null || nivelMsg==0){
		var icono = new GIcon();
		icono.image = 'markers/user.png';
		icono.iconSize = new GSize(28, 43);
		icono.iconAnchor = new GPoint(14, 43);
		var opciones = {
			icon:icono
		};
		var marker = new GMarker(new GLatLng(latitudUser,longitudUser), opciones);
		map.addOverlay(marker);
	}
	
	if(userName != '' && nivelMsg > 0 && localizacion){
		$.post('updateLatLong.jsp',{
			'nombre':userName,
			'latitud':latitudUser,
			'longitud':longitudUser
		});
	}
}

function findPos(lat, lng, dir){
	if(dir != ''){
		localizador.getLatLng(dir, function(point){
			localizador.getLocations(point,function(response){
				if(response.Status.code == 200){
					document.getElementById('form-posicion').direccion.value = response.Placemark[0].address;
					document.getElementById('form-posicion').longitud.value = response.Placemark[0].Point.coordinates[0];
					document.getElementById('form-posicion').latitud.value = response.Placemark[0].Point.coordinates[1];
					var coorAux = new GLatLng(response.Placemark[0].Point.coordinates[1],response.Placemark[0].Point.coordinates[0]);
					puntoAux = new GMarker(coorAux);
					map.setCenter(coorAux,14);
					map.addOverlay(puntoAux);
				}
			});
		});
	}else{
		localizador.getLocations(new GLatLng(lat,lng),function(response){
			if(response.Status.code == 200){
				document.getElementById('form-posicion').direccion.value = response.Placemark[0].address;
				document.getElementById('form-posicion').longitud.value = response.Placemark[0].Point.coordinates[0];
				document.getElementById('form-posicion').latitud.value = response.Placemark[0].Point.coordinates[1];
				var coorAux = new GLatLng(response.Placemark[0].Point.coordinates[1],response.Placemark[0].Point.coordinates[0]);
				puntoAux = new GMarker(coorAux);
				map.setCenter(coorAux,14);
				map.addOverlay(puntoAux);
			}
		});
	}
}

function newPos(lat, lng, porDefecto){
	if(porDefecto){
		document.getElementById('form-posicion').porDefecto.checked = false;
		$.post('updateLatLong.jsp',{
			'nombre':userName,
			'latitud':lat,
			'longitud':lng,
			'porDefecto':true
		});
	}
	$.post('updateLatLong.jsp',{
		'nombre':userName,
		'latitud':lat,
		'longitud':lng
	});
}

function fondo(marker, b){
	return -100000;
}

function orden(marker, b){
	return 1;
}

function actualizar(){
	//cada 5 segundos hacemos la peticion actualizadora de json
	$.getJSON('leeEventos.jsp', {
		'fecha': ultimamodif,
		'action':'notFirst',
		'nivel':nivelMsg
	}, function(data) {
		$.each(data, function(entryIndex, entry) {
			//el id lo asigna la base de datos
			var nuevomarcador = new ObjMarcador(entry['id'],entry['item'],entry['type'],
				entry['quantity'],entry['name'],entry['description'],entry['info'],
				entry['latitud'],entry['longitud'],entry['address'],entry['state'],
				entry['date'], entry['modified'],entry['user_name'],entry['user_type'],
				null,entry['size'],entry['traffic'],entry['idAssigned'],entry['floor'],null);

			//pintamos los nuevos, para lo que comprobamos que no existian
			if(marcadores_definitivos[nuevomarcador.id] == null){
				if(nuevomarcador.estado != 'erased'){
					nuevomarcador.marker = generaMarcador(nuevomarcador, DEFINITIVO);
					marcadores_definitivos[nuevomarcador.id] = nuevomarcador;
					indices[pos_indices] = nuevomarcador.id;
					pos_indices++;
				}
			}
			//actualizamos los que han sido modificados
			else{
				//si se ha modificado algun dato se actualiza
				if(nuevomarcador.estado != 'erased'){
					map.removeOverlay(marcadores_definitivos[nuevomarcador.id].marker);
					nuevomarcador.marker = generaMarcador(nuevomarcador, DEFINITIVO);
					marcadores_definitivos[nuevomarcador.id] = nuevomarcador;
					cambiarPlanta(plantaResidencia);
				}
				//si se ha eliminado un marcador
				else{
					map.removeOverlay(marcadores_definitivos[nuevomarcador.id].marker);
					marcadores_definitivos[nuevomarcador.id] = null;
					pos_indices--;

					//eliminamos el indice de la matriz de indices sin dejar huecos
					var id = nuevomarcador.id;
					var index;

					for(i=0; i<indices.length; i++){
						index = i;
						if(indices[i] == id){
							break;
						}
					}
					while(indices[index+1] != null){
						indices[index] = indices[index+1];
						index++;
					}
					indices[index] = null;
				}
			}
		});
	});

	contador++;
	if(localizacion && nivelMsg > 0 && contador >= 4){
		contador = 0;
		navigator.geolocation.getCurrentPosition(coordenadasUsuario);
	}

	ultimamodif = obtiene_fecha();
	timeout = setTimeout('actualizar()', tiempoActualizacion);
}

function generateBuilding(type, mensaje, latitud, longitud){
	var imagen;
	var matrix;
	var matrixIndex;
	if(type == 'hospital'){
		imagen = 'ambulanceStation.png';
		matrix = hospitals;
		matrixIndex = hospIndex;
	}else if(type == 'policeStation'){
		imagen = 'policeStation.png';
		matrix = policeStations;
		matrixIndex = policeIndex;
	}else if(type == 'firemenStation'){
		imagen = 'firemenStation.png';
		matrix = firemenStations;
		matrixIndex = fireIndex;
	}else if(type == 'geriatricCenter'){
		imagen = 'geriatricCenter.png';
		matrix = geriatricCenters;
		matrixIndex = geriatricIndex;
	}

	var icono = new GIcon();
	icono.image = 'markers/' + imagen;
	icono.iconSize = new GSize(50, 49);
	icono.iconAnchor = new GPoint(25, 49);
	icono.infoWindowAnchor = new GPoint(5, 1);
	var opciones = {
		icon:icono,
		zIndexProcess:orden
	}; //se pueden arrastrar para asociarlo

	var marker = new GMarker (new GLatLng(latitud,longitud), opciones);
	if(type == 'geriatricCenter'){
		var lineas = new Array();
		var esquina1 = [38.232440, -1.699160]; // desplazado (-0.000095, 0.000120)
		var esquina2 = [38.232380, -1.698640];
		var esquina3 = [38.232105, -1.698690];
		var esquina4 = [38.232165, -1.699210];
		var color = '#00ff00';
		var anchura = 1;
		var opacidad = 0.5;

		lineas[0] = new GPolyline([new GLatLng(esquina1[0],esquina1[1]), new GLatLng(esquina2[0],esquina2[1])], color, anchura, opacidad);
		lineas[1] = new GPolyline([new GLatLng(esquina2[0],esquina2[1]), new GLatLng(esquina3[0],esquina3[1])], color, anchura, opacidad);
		lineas[2] = new GPolyline([new GLatLng(esquina3[0],esquina3[1]), new GLatLng(esquina4[0],esquina4[1])], color, anchura, opacidad);
		lineas[3] = new GPolyline([new GLatLng(esquina4[0],esquina4[1]), new GLatLng(esquina1[0],esquina1[1])], color, anchura, opacidad);
		GEvent.addListener(marker, 'click', function() {
			marker.openInfoWindowHtml('<div id="bocadillo">' + mensaje + '</div>');
			for(i=0; i<4; i++){
				map.addOverlay(lineas[i]);
			}
		});
		GEvent.addListener(marker, 'infowindowclose', function() {
			for(i=0; i<4; i++){
				map.removeOverlay(lineas[i]);
			}
		});
	}else{
		GEvent.addListener(marker, 'click', function() {
			marker.openInfoWindowHtml('<div id="bocadillo">' + mensaje + '</div>');
		});
	}

	map.addOverlay(marker);
	matrix[matrixIndex]=marker;
	matrixIndex++;

	if(type == 'hospital'){
		hospitals = matrix;
		hospIndex = matrixIndex;
	}else if(type == 'policeStation'){
		policeStations = matrix;
		policeIndex = matrixIndex;
	}else if(type == 'firemenStation'){
		firemenStations = matrix;
		fireIndex = matrixIndex;
	}else if(type == 'geriatricCenter'){
		geriatricCenters = matrix;
		geriatricIndex = matrixIndex;
	}

	return marker;
}

function hideBuilding(type){
	var matrix;
	if(type == 'hospital'){
		matrix = hospitals;
		hospitals = new Array;
		hospIndex = 0;
	}else if(type == 'policeStation'){
		matrix = policeStations;
		policeStations = new Array;
		policeIndex = 0;
	}else if(type == 'firemenStation'){
		matrix = firemenStations;
		firemenStations = new Array;
		fireIndex = 0;
	}else if(type == 'geriatricCenter'){
		matrix = geriatricCenters;
		geriatricCenters = new Array;
		geriatricIndex = 0;
	}
	for(i=0; i<matrix.length; i++){
		map.removeOverlay(matrix[i]);
	}
}

function visualize(selected, type){
	if(type == 'hospital'){
		if(selected){
			showHospitals();
		}else{
			hideBuilding(type);
		}
	}
	if(type == 'policeStation'){
		if(selected){
			showPoliceStations();
		}else{
			hideBuilding(type);
		}
	}
	if(type == 'firemenStation'){
		if(selected){
			showFiremenStations();
		}else{
			hideBuilding(type);
		}
	}
	if(type == 'geriatricCenter'){
		if(selected){
			showGeriatricCenters();
		}else{
			hideBuilding(type);
		}
	}
}

function generaMarcador(evento, caracter){
	var opciones;
	var icono = new GIcon(G_DEFAULT_ICON);

	if(evento.marcador == 'event'){ // Es un evento
		if(evento.tipo == 'fire'){ // Incendio
			icono.image = 'markers/fuego.png';
			if(evento.estado == 'controlled'){
				icono.image ='markers/fuego_control.png';
			}
		}else if(evento.tipo == 'flood'){// Inundacion
			icono.image = 'markers/agua.png';
			if(evento.estado == 'controlled'){
				icono.image ='markers/agua_control.png';
			}
		}else if(evento.tipo == 'collapse'){ // derrumbamiento
			icono.image = 'markers/casa.png';
			if(evento.estado == 'controlled'){
				icono.image ='markers/casa_control.png';
			}
		}else if(evento.tipo == 'lostPerson'){ // anciano perdido
			icono.image = 'markers/personaPerdida.png';
			if(evento.estado == 'controlled'){
				icono.image ='markers/personaPerdida_control.png';
			}
		}else if(evento.tipo == 'injuredPerson'){ // anciano herido
			icono.image = 'markers/personaHerida.png';
			if(evento.estado == 'controlled'){
				icono.image ='markers/personaHerida_control.png';
			}
		}
		opciones = {
			icon: icono,
			zIndexProcess: orden,
			draggable: true // Para que se pueda arrastrar
		}; 
	}else if (evento.marcador == 'resource'){ // es un recurso
		//MAXIMO DE RECURSOS POR MARCADOR ES 10
		if(evento.cantidad > 10){
			evento.cantidad = 10;
		}
		
		var active;
		if(evento.estado == 'active'){
			active = '_no';
		}else{
			active = evento.cantidad;
		}
		
		if(evento.tipo == 'police'){ // es un policia
			icono.image = 'markers/policia' + active + '.png';
		}else if(evento.tipo == 'firemen'){ // es un bombero
			icono.image = 'markers/bombero' + active + '.png';
		}else if(evento.tipo == 'ambulance' || evento.tipo=='ambulancia'){ //es una ambulancia
			icono.image = 'markers/ambulancia' + active + '.png';
		}else if(evento.tipo == 'nurse'){ // es un enfermero
			icono.image = 'markers/enfermero' + active + '.png';
		}else if(evento.tipo == 'gerocultor'){ // es un gerocultor
			icono.image = 'markers/gerocultor' + active + '.png';
		}else if(evento.tipo == 'assistant'){ // es un auxiliar
			icono.image = 'markers/auxiliar' + active + '.png';
		}else if(evento.tipo == 'otherStaff'){ // otro
			icono.image = 'markers/otro' + active + '.png';
		}
		opciones = {
			icon: icono,
			zIndexProcess: orden
			// draggable: false //No se puede arrastrar
		}; 
	}else if(evento.marcador == 'people'){ // es una victima
		//MAXIMO DE VICTIMAS POR MARCADOR ES 10
		if(evento.cantidad > 10){
			evento.cantidad = 10;
		}

		if(evento.tipo == 'trapped'){ // personas atrapadas
			icono.image = 'markers/trapped' + evento.cantidad + '.png';
			if(evento.estado == 'controlled'){
				icono.image = 'markers/trapped_control.png';
			}
		}else if(evento.tipo=='healthy'){ // sanos
			icono.image = 'markers/sano' + evento.cantidad + '.png';
			if(evento.estado=='controlled'){
				icono.image = 'markers/sano_control.png';
			}
		}else if(evento.tipo == 'slight'){ // heridos leves
			icono.image = 'markers/leve' + evento.cantidad + '.png';
			if(evento.estado == 'controlled'){
				icono.image = 'markers/slight_control.png';
			}
		}else if(evento.tipo == 'serious'){ // heridos graves
			icono.image = 'markers/grave' + evento.cantidad + '.png';
			if(evento.estado == 'controlled'){
				icono.image = 'markers/serious_control.png';
			}
		}else if(evento.tipo == 'dead'){ // muertos
			icono.image = 'markers/muerto' + evento.cantidad + '.png';
			if(evento.estado == 'controlled'){
				icono.image = 'markers/dead_control.png';
			}
		}
		icono.shadow = 'markers/shadow50.png';
		icono.iconSize = new GSize(28, 43);
		icono.shadowSize = new GSize(43, 43);
		icono.iconAnchor = new GPoint(14, 43);
		icono.infoWindowAnchor = new GPoint(5, 1);
		opciones = {
			icon: icono,
			zIndexProcess: orden
			// draggable: true //Se pueden arrastrar para asociarlo
		}; 
	}

	var marker = new GMarker (new GLatLng(evento.latitud, evento.longitud), opciones);

	// Annadimos el comportamiento
	if(caracter == TEMPORAL){ //aqui hay que guardar los datos
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
	}else if(caracter == DEFINITIVO){ //aqui podemos realizar modificaciones a los ya existentes
		GEvent.addListener(marker, 'click', function(){
			var small = evento.nombre + '<br/>' + evento.descripcion;
			var links1 = '<form id="form_acciones" name="form_acciones" action="#">';
			if(nivelMsg > 1){
				if(evento.marcador != 'resource'){
					links1 += cargarMenuAcciones(marcadores_definitivos[evento.id]); // en mapa_xxx.js
				}else{
					links1 += cargarListaActividades(marcadores_definitivos[evento.id]); // en mapa_xxx.js
				}
			}
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
			cargarLateral(evento); // en mapa_xxx.js
		});

		GEvent.addListener(marker, 'dragstart', function(){
			map.closeInfoWindow();
		});

		GEvent.addListener(marker, 'dragend', function(){
			//var asociada = asociar(evento.id, evento.marker);
			/*marker.openInfoWindowHtml('<div id="bocadillo">Asociado a cat&aacute;strofe:<br/>'+
				marcadores_definitivos[asociada].nombre+'<br/>'+
				'<a id="guardar_asociacion" href="#" onclick="guardar_asociacion('+asociada+','+evento.id+');return false;">Guardar</a>'+' - '+
				'<a id="cancelar" href="#" onclick="cancelar_asignacion('+evento.id+');return false;">Cancelar</a></div>');*/
			
			// Temporal*****************************************************************************************
			cancelar_asignacion(evento.id);
			//**************************************************************************************************
			
			/*marker.openInfoWindowHtml('<div id="bocadillo">¿Confirmar cambio de posición?<br/>'+
				'<a id="confirmar" href="#" onclick="map.closeInfoWindow();guardar_posicion('+evento.id+
				','+marker.getLatLng().lat()+','+marker.getLatLng().lng()+')">Confirmar</a>'+ ' - '+
				'<a id="cancelar" href="#" onclick="map.closeInfoWindow();">Cancelar</a></div>');*/
		});

		if(evento.marcador == 'event'){
			GEvent.addListener(marker, 'dblclick', function(){
				marker.openInfoWindowHtml('¿Donde desea moverlo?');
				var event = GEvent.addListener(map, 'click', function(overlay, latlng){
					var nuevaLat = latlng.lat();
					var nuevaLong = latlng.lng();
					var nuevaPos = new GLatLng(nuevaLat, nuevaLong);
					map.removeOverlay(marker);
					marker.setLatLng(nuevaPos);
					map.addOverlay(marker);
					marker.openInfoWindowHtml('<div id="bocadillo">&iquest;Confirmar cambio de posici&oacute;n?<br/>' +
						'<a id="confirmar" href="#" onclick="map.closeInfoWindow(); guardar_posicion(' + evento.id +
						',' + nuevaLat + ',' + nuevaLong + ')" >Confirmar</a>'+ ' - ' +
						'<a id="cancelar" href="#" onclick="map.closeInfoWindow(); cancelar_asignacion(' + evento.id + ');">Cancelar</a></div>');
					GEvent.removeListener(event);
				});
			});
		}

		GEvent.addListener(marker, 'infowindowclose', function() {
			limpiarLateral(evento); // en mapa_xxx.js
		});
	}

	if((evento.planta == -2 || evento.planta == plantaResidencia) && ((evento.tipo != 'healthy') || verSanos)){
		// (!(evento.marcador=='resource' && caracter==1)){
		map.addOverlay(marker);
	}
	return marker;
}

function crearCatastrofe(marcador,tipo,cantidad,nombre,info,descripcion,direccion,longitud,latitud,estado,size,traffic,idAssigned,
	planta,fatigue,fever,dyspnea,nausea,headache,vomiting,abdominal_pain,weight_loss,blurred_vision,muscle_weakness){

	var sintomas = '';
	if(fatigue) sintomas += 'fatigue,';
	if(fever) sintomas += 'fever,';
	if(dyspnea) sintomas += 'dyspnea,';
	if(nausea) sintomas += 'nausea,';
	if(headache) sintomas += 'headache,';
	if(vomiting) sintomas += 'vomiting,';
	if(abdominal_pain) sintomas += 'abdominal_pain,';
	if(weight_loss) sintomas += 'weight_loss,';
	if(blurred_vision) sintomas += 'blurred_vision,';
	if(muscle_weakness) sintomas += 'muscle_weakness,';

	var nuevomarcador = new ObjMarcador(pos_temp,marcador,tipo,cantidad,nombre,
		descripcion,info,latitud,longitud,direccion,estado,obtiene_fecha(),obtiene_fecha(),
		usuario_actual,usuario_actual_tipo,null,size,traffic,idAssigned,planta,sintomas);

	if(marcador == 'event'){
		nuevomarcador.size = size;
		nuevomarcador.traffic = traffic;
	}else if(marcador == 'resource' || marcador == 'people'){
		nuevomarcador.idAssigned = idAssigned;
	}

	pos_temp++;
	nuevomarcador.marker = generaMarcador(nuevomarcador, TEMPORAL);

	guardar(nuevomarcador);
	registrarHistorial(userName, 0, 'creada');
}

function guardar(puntero){
	// 1.Guardar el elemento en la base de datos
	$.post('guardaEvento.jsp',{
		'marcador':puntero.marcador,
		'tipo':puntero.tipo,
		'cantidad':puntero.cantidad,
		'nombre':puntero.nombre,
		'descripcion':puntero.descripcion,
		'info':puntero.info,
		'latitud':puntero.latitud,
		'longitud':puntero.longitud,
		'direccion':puntero.direccion,
		'estado':puntero.estado,
		'size':puntero.size,
		'traffic':puntero.traffic,
		'idAssigned':puntero.idAssigned,
		'fecha':puntero.fecha,
		'usuario':puntero.nombre_usuario,
		'planta':puntero.planta,
		'sintomas':puntero.sintomas
	}, function(data){
		$('#guardar').innerHTML = data;
	});

	for(i=0; i<emergenciasAsociadas[0].length; i++){
		if(emergenciasAsociadas[0][i][1] == true){
			$.post('update.jsp',{
				'fecha':puntero.fecha,
				'id_emergencia':emergenciasAsociadas[0][i][0],
				'accion':'asociar'
			});
		}
	}

	// 2.Borrar el elemento del mapa y la matriz temporal
	eliminar(puntero, TEMPORAL);
	marcadores_temporales[puntero.id] = null;

	// 3.Recargar el mapa para que aparezca el elemento nuevo
	// actualizar(); // esto adelanta el timeOut a ahora mismo
}

function modificar(id,cantidad,nombre,info,descripcion,direccion,longitud,latitud,
	estado,size,traffic,idAssigned,planta,
	fatigue,fever,dyspnea,nausea,headache,vomiting,abdominal_pain,weight_loss,blurred_vision,muscle_weakness){
	// utiliza la variable puntero_temp accesible por todos y cargada por cargarModificar
	if (caracter_temp == TEMPORAL){
		// Actualizar la matriz temporal
		eliminar(marcadores_temporales[id],TEMPORAL);
		crearCatastrofe(puntero_temp.marcador,puntero_temp.tipo,cantidad,nombre,info,descripcion,
			direccion,longitud,latitud,estado,size,traffic,idAssigned,planta,
			fatigue,fever,dyspnea,nausea,headache,vomiting,abdominal_pain,weight_loss,blurred_vision,muscle_weakness);
	}else if(caracter_temp == DEFINITIVO){
		var sintomas = '';
		if(fatigue) sintomas += 'fatigue,';
		if(fever) sintomas += 'fever,';
		if(dyspnea) sintomas += 'dyspnea,';
		if(nausea) sintomas += 'nausea,';
		if(headache) sintomas += 'headache,';
		if(vomiting) sintomas += 'vomiting,';
		if(abdominal_pain) sintomas += 'abdominal_pain,';
		if(weight_loss) sintomas += 'weight_loss,';
		if(blurred_vision) sintomas += 'blurred_vision,';
		if(muscle_weakness) sintomas += 'muscle_weakness,';

		// hay que hacer un update a la base de datos
		$.post('update.jsp',{
			'id':id,
			'marcador':puntero_temp.marcador,
			'tipo':puntero_temp.tipo,
			'cantidad':cantidad,
			'nombre':nombre,
			'descripcion':descripcion,
			'info':info,
			'latitud':latitud,
			'longitud':longitud,
			'direccion':direccion,
			'estado':estado,
			'size':size,
			'traffic':traffic,
			'idAssigned':idAssigned,
			'fecha':puntero_temp.fecha,
			'usuario':usuario_actual,
			'planta':planta,
			'sintomas':sintomas,
			'accion':'modificar'
		});
	}
}

function modificar2(id,tipo,cantidad,nombre,info,descripcion,direccion,tamanno,trafico,idAssigned,planta){
	var puntero = marcadores_definitivos[id];
	var idA;
	var accion;
	if(idAssigned != null){
		idA = idAssigned;
	}else{
		idA = puntero.idAssigned;
	}

	if(tipo != puntero.tipo &&
		!((tipo=='slight'&&puntero.tipo=='serious') || (tipo=='serious'&&puntero.tipo=='slight'))){
		accion = 'cambioTipo';
	}else{
		accion = 'modificar';
	}

	$.post('update.jsp',{
		'id':id,
		'marcador':puntero.marcador,
		'tipo':tipo,
		'cantidad':cantidad,
		'nombre':nombre,
		'descripcion':descripcion,
		'info':info,
		'latitud':puntero.latitud,
		'longitud':puntero.longitud,
		'direccion':direccion,
		'estado':puntero.estado,
		'size':tamanno,
		'traffic':trafico,
		'idAssigned':idA,
		'fecha':puntero.fecha,
		'usuario':usuario_actual,
		'planta':planta,
		'sintomas':puntero.sintomas,
		'accion':accion
	});

	for(i=0; i<emergenciasAsociadas[0].length; i++){
		if(emergenciasAsociadas[0][i][1] != emergenciasAsociadas[1][i][1]){
			var accion2;
			if(emergenciasAsociadas[0][i][1] == true){
				accion2 = 'asociar';
			}else{
				accion2 = 'eliminarAsociacion';
			}
			$.post('update.jsp',{
				'id_herido':id,
				'id_emergencia':emergenciasAsociadas[0][i][0],
				'accion':accion2
			});
		}
	}
	registrarHistorial(userName, id, 'modif');
}

function modificar3(id,tipo,fatigue,fever,dyspnea,nausea,headache,vomiting,abdominal_pain,weight_loss,blurred_vision,muscle_weakness){
	var puntero = marcadores_definitivos[id];

	var sintomas = '';
	if(fatigue) sintomas += 'fatigue,';
	if(fever) sintomas += 'fever,';
	if(dyspnea) sintomas += 'dyspnea,';
	if(nausea) sintomas += 'nausea,';
	if(headache) sintomas += 'headache,';
	if(vomiting) sintomas += 'vomiting,';
	if(abdominal_pain) sintomas += 'abdominal_pain,';
	if(weight_loss) sintomas += 'weight_loss,';
	if(blurred_vision) sintomas += 'blurred_vision,';
	if(muscle_weakness) sintomas += 'muscle_weakness,';

	// hay que hacer un update a la base de datos
	$.post('update.jsp',{
		'id':id,
		'marcador':puntero.marcador,
		'tipo':tipo,
		'cantidad':puntero.cantidad,
		'nombre':puntero.nombre,
		'descripcion':puntero.descripcion,
		'info':puntero.info,
		'latitud':puntero.latitud,
		'longitud':puntero.longitud,
		'direccion':puntero.direccion,
		'estado':puntero.estado,
		'size':puntero.size,
		'traffic':puntero.traffic,
		'idAssigned':puntero.idAssigned,
		'fecha':puntero.fecha,
		'usuario':usuario_actual,
		'planta':puntero.planta,
		'sintomas':sintomas,
		'accion':'modificar'
	});
}

function eliminar(puntero,caracter){
	if(caracter == TEMPORAL){
		map.removeOverlay(puntero.marker);
	}else if(caracter == DEFINITIVO){
		// hay que hacer un update
		if(puntero.marcador == 'people' && puntero.tipo != 'healthy'){
			$.post('update.jsp',{
				'id':puntero.id,
				'marcador':puntero.marcador,
				'tipo':'healthy',
				'cantidad':puntero.cantidad,
				'nombre':puntero.nombre,
				'descripcion':puntero.descripcion,
				'info':puntero.info,
				'latitud':puntero.latitud,
				'longitud':puntero.longitud,
				'direccion':puntero.direccion,
				'estado':'active',
				'size':puntero.size,
				'traffic':puntero.traffic,
				'idAssigned':puntero.idAssigned,
				'fecha':puntero.fecha,
				'usuario':usuario_actual,
				'planta':puntero.planta,
				'accion':'eliminar'
			});
			registrarHistorial(userName, puntero.id, 'modif');
		}else{
			$.post('update.jsp',{
				'id':puntero.id,
				'marcador':puntero.marcador,
				'tipo':puntero.tipo,
				'cantidad':puntero.cantidad,
				'nombre':puntero.nombre,
				'descripcion':puntero.descripcion,
				'info':puntero.info,
				'latitud':puntero.latitud,
				'longitud':puntero.longitud,
				'direccion':puntero.direccion,
				'estado':'erased',
				'size':puntero.size,
				'traffic':puntero.traffic,
				'idAssigned':puntero.idAssigned,
				'fecha':puntero.fecha,
				'usuario':usuario_actual,
				'planta':puntero.planta,
				'accion':'eliminar'
			});
			registrarHistorial(userName, puntero.id, 'eliminar');
		}
	}
}

function asociarEmergencia(id, valor){
	for(i=0; i<emergenciasAsociadas[0].length; i++){
		if(emergenciasAsociadas[0][i][0] == id){
			emergenciasAsociadas[0][i][1] = valor;
		}
	}
}

function guardar_posicion(id, lat, lng){
	marcadores_definitivos[id].marker.setLatLng(new GLatLng(lat, lng));
	$.post('updateLatLong.jsp',{
		'id':id,
		'latitud':lat,
		'longitud':lng
	});
	registrarHistorial(userName, id, 'mover');
}

function actuar(idEvento,nombreUsuario,accionAux){
	var accion;
	var estadoEvento;
	var estadoUsuario;
	
	for(i=0;i<accionAux.length;i++){
		if(accionAux[i].checked){
			accion = accionAux[i].value;
		}
	}
	if(accion != ''){
		if(accion=='apagar' || accion=='controlar' || accion=='acordonar' ||
				accion=='atender' || accion=='evacuar' || accion=='rescatar'){
			estadoEvento = 'controlled';
			estadoUsuario = 'acting';
		}else if(accion=='ayudar' || accion=='trasladar' || accion=='evacuado' || accion=='volver'){
			estadoEvento = 'controlled2';
			estadoUsuario = 'acting';
		}else if(accion=='apagado' || accion=='controlado' || accion=='acordonado' ||
				accion=='curado' || accion=='rescatado'){
			estadoEvento = 'erased';
			estadoUsuario = 'active';
		}else if(accion=='vuelto' || accion=='dejar'){
			estadoEvento = 'active';
			estadoUsuario = 'active';
		}
		$.post('updateEstado.jsp',{
			'idEvento':idEvento,
			'nombreUsuario':nombreUsuario,
			'estadoEvento':estadoEvento,
			'estadoUsuario':estadoUsuario,
			'accion':accion
		});
	}
	registrarHistorial(userName, idEvento, accion);
}

function detener(idEvento,idEmergencia,nombreUsuario){
	$.post('updateEstado.jsp',{
		'idEvento':idEvento,
		'idEmergencia':idEmergencia,
		'nombreUsuario':nombreUsuario,
		'accion':'detener'
	});
}

function cambiarPlanta(num){
	map.removeOverlay(resi);
	document.getElementById('planta' + plantaResidencia).style.fontWeight = 'normal';
	document.getElementById('planta' + plantaResidencia).style.textDecoration = 'none';

	plantaResidencia = num;

	document.getElementById('planta' + num).style.fontWeight = 'bold';
	document.getElementById('planta' + num).style.textDecoration = 'underline';
	if(num >= 0){
		document.getElementById('planoResidencia').src = 'images/residencia/planta' + num + '.jpg';
		document.getElementById('plantaPlano').innerHTML = num;
		resi.getIcon().image = 'images/residencia/planta' + num + '.png';
		if(map.getZoom() >= 20 && map.getCurrentMapType().getName() == 'Mapa'){
			map.addOverlay(resi);
		}
	}

	for(i in marcadores_definitivos){
		map.removeOverlay(marcadores_definitivos[i].marker);
		if(num != -2){
			if(marcadores_definitivos[i].planta == num || marcadores_definitivos[i].planta == -2){
				if(marcadores_definitivos[i].tipo != 'healthy' || verSanos){
					map.addOverlay(marcadores_definitivos[i].marker);
				}
			}
		}else{
			if(marcadores_definitivos[i].tipo != 'healthy' || verSanos){
				map.addOverlay(marcadores_definitivos[i].marker);
			}
		}
	}
}

function cambiarGeolocalizacion(valor){
	$.post('updateLatLong.jsp',{
		'nombre':userName,
		'localizacion':valor
	});
}

function mostrarSanos(mostrar){
	verSanos = mostrar;
	for(i in marcadores_definitivos){
		if(marcadores_definitivos[i].tipo == 'healthy'){
			if(mostrar){
				map.addOverlay(marcadores_definitivos[i].marker);
			}else{
				map.removeOverlay(marcadores_definitivos[i].marker);
			}
		}
	}
}

function registrarHistorial(usuario, idEvento, accion){
	var evento;
	if(idEvento == 0){
		evento = 'Evento creado por el usuario ' + usuario;
	}else if(accion == 'modif'){
		evento = 'Emergencia ' + idEvento + ' modificada por ' + usuario;
	}else if(accion == 'eliminar'){
		evento = 'Emergencia ' + idEvento + ' eliminada por ' + usuario;
	}else if(accion == 'mover'){
		evento = 'Emergencia ' + idEvento + ' cambiada de sitio por ' + usuario;
	}else{
		evento = 'El usuario ' + usuario + ' ha actuado sobre ' + idEvento + ' - ' + accion;
	}

	$.post('registrarHistorial.jsp',{
		'usuario':usuario,
		'evento':evento
	});
}

function obtiene_fecha() {
	// llega a algo como esto: 1992-01-01 00:00:01 , necesario para MySql a partir de la fecha actual
	var fecha_actual = new Date();
	var dia = fecha_actual.getDate();
	var mes = fecha_actual.getMonth() + 1;
	var anio = fecha_actual.getFullYear();
	var horas = fecha_actual.getHours();
	var minutos = fecha_actual.getMinutes();
	var segundos = fecha_actual.getSeconds();
	var milisegundos = fecha_actual.getMilliseconds();

	if(mes < 10) mes = '0' + mes;
	if(dia < 10) dia = '0' + dia;
	if(horas < 10) horas = '0' + horas;
	if(minutos < 10) minutos = '0' + minutos;
	if(segundos < 10) segundos = '0' + segundos;
	if(milisegundos < 10) milisegundos = '00' + milisegundos;
	else if(milisegundos < 100) milisegundos = '0' + milisegundos;

	return (anio + '-' + mes + '-' + dia + ' ' + horas + ':' + minutos + ':' + segundos + '.' + milisegundos);
}

function asociar(id, marker){
	// 1.hallar el punto del marcador pasado
	var latitud1 = marker.getLatLng().lat(); // = marcadores_definitivos[id].marker.lat();
	var longitud1 = marker.getLatLng().lng(); // = marcadores_definitivos[id].marker.lng();

	// 2.hallar la distancia a cada catastrofe de la matriz definitiva
	var diferencia = 999999999999;
	var id_mas_cercano;
	var idDefinitivo;

	for(i=0; indices[i]!=null; i++){
		idDefinitivo = indices[i];
		// nos quedamos solo con los eventos (catastrofes)
		if(marcadores_definitivos[idDefinitivo].marcador != 'event'){
			continue;
		}
		var latitud2 = marcadores_definitivos[idDefinitivo].latitud;
		var longitud2 = marcadores_definitivos[idDefinitivo].longitud;
		var dif_lat = Math.pow(latitud1 - latitud2, 2);
		var dif_long = Math.pow(longitud1 - longitud2, 2);
		var distancia = Math.sqrt(dif_lat + dif_long);
		if(distancia < diferencia){
			diferencia = distancia;
			id_mas_cercano = idDefinitivo;
		}
	}

	// 3.Devolver el id de la catastrofe mas cercana o hacer el post directamente
	return id_mas_cercano;
}

function guardar_asociacion(idEvento, idRecurso){
	// Hallar la nueva posicion del recurso en funcion del tipo
	var evento = marcadores_definitivos[idEvento];
	var recurso = marcadores_definitivos[idRecurso];
	var latitud = evento.latitud;
	var longitud = evento.longitud;
	var nueva_latitud = recurso.latitud;
	var nueva_longitud = recurso.longitud;

	// Hallar las nuevas distancias
	/*
	if(recurso.tipo=='police'){nueva_latitud=latitud+0.00005;nueva_longitud=longitud+0.000025;}
	if(recurso.tipo=='nurse'){nueva_latitud=latitud+0.00005;nueva_longitud=longitud-0.000025;}
	if(recurso.tipo=='gerocultor'){nueva_latitud=latitud+0.00005;nueva_longitud=longitud-0.000025;}
	if(recurso.tipo=='assistant'){nueva_latitud=latitud+0.00005;nueva_longitud=longitud-0.000025;}
	if(recurso.tipo=='firemen'){nueva_latitud=latitud+0.000025;nueva_longitud=longitud-0.00005;}
	if(recurso.tipo=='ambulance'||recurso.tipo=='ambulancia'){nueva_latitud=latitud+0.000025;nueva_longitud=longitud+0.00005;}
	if(recurso.tipo=='trapped'){nueva_latitud=latitud+0.00005;nueva_longitud=longitud-0.0001;}
	if(recurso.tipo=='healthy'){nueva_latitud=latitud+0.00005;nueva_longitud=longitud+0.0001;}
	if(recurso.tipo=='slight'){nueva_latitud=latitud+0.0001;nueva_longitud=longitud-0.00005;}
	if(recurso.tipo=='serious'){nueva_latitud=latitud+0.0001;nueva_longitud=longitud+0.00005;}
	if(recurso.tipo=='dead'){nueva_latitud=latitud+0.00005;nueva_longitud=longitud+0.0001;}
	*/

	// actualizar las modificaciones con el metodo modificar
	caracter_temp = DEFINITIVO;
	puntero_temp = recurso;
	modificar(idRecurso, recurso.cantidad, recurso.nombre, 'Asociado a ' + evento.nombre + '. ' + recurso.info,
		recurso.descripcion, evento.direccion, nueva_longitud, nueva_latitud, recurso.estado, recurso.size, recurso.traffic, idEvento);
}

function cancelar_asignacion(id){
	// borro la posicion actual y lo dibujo en la antigua
	map.removeOverlay(marcadores_definitivos[id].marker);
	marcadores_definitivos[id].marker = generaMarcador(marcadores_definitivos[id], DEFINITIVO);
}