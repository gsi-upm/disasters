var map;
var localizador;

var tiempoActualizacion = 2000;
var tiempoInicial = tiempoActualizacion;
var contador = 0;

var ultimamodif = '1992-01-01 00:00:01'; // una fecha antigua para empezar
var marcadores_definitivos = new Array();
var marcadores_temporales = new Array();
var indices = new Array();
var pos_def = 0;
var pos_temp = 0;
var pos_indices = 0;

var puntero_temp;
var caracter_temp;
var TEMPORAL = 0;
var DEFINITIVO = 1;

var hospitals = new Array();
var policeStations = new Array();
var firemenStations = new Array();
var geriatricCenters = new Array();
var hospIndex = 0;
var policeIndex = 0;
var fireIndex = 0;
var geriatricIndex = 0;

var localizacion = (navigator.geolocation != null);
var residencia; // marcador de la imagen de la residencia
var plantaResidencia = -1;
var emergenciasAsociadas = [new Array(), new Array()];
var sintomas = [new Array(), new Array()];
var verSanos = false;
var limpiar = true;
var noActualizar = 0;
var centroAux = new Array();
var puntoAux;

function initialize(){
	if(GBrowserIsCompatible()){
		map = new GMap2(document.getElementById('map_canvas'));
		localizador = new GClientGeocoder();
		mapInit(); // en mapa_xxx.js
		
		map.addControl(new GLargeMapControl()); // controles completos
		map.addControl(new GScaleControl ());   // escala
		map.addControl(new GMapTypeControl());  // mapa, foto, hibrido

		buildingInit(); // en mapa_xxx.js

		// hacemos la peticion inicial del json (baja todo menos los borrados)
		$.getJSON('getpost/leeEventos.jsp', {
			'fecha': ultimamodif,
			'action':'firstTime',
			'nivel':nivelMsg
		}, function(data) {
			$.each(data, function(entryIndex, entry){
				var nuevomarcador = new ObjMarcador(entry['id'],entry['item'],entry['type'],
					entry['quantity'],entry['name'],entry['description'],entry['info'],entry['latitud'],
					entry['longitud'],entry['address'],entry['state'],entry['date'], entry['modified'],
					null,entry['size'],entry['traffic'],entry['idAssigned'],entry['user'],entry['floor']);
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
			var icono = new GIcon();
			icono.image = 'images/residencia/planta'+ plantaResidencia + '.png';
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
					$.each(data, function(entryIndex, entry){
							localizacion = entry['localizacion']; // entonces valor por defecto del usuario
							document.getElementById('form-posicion').localizacion.checked = localizacion;
					});
				});
			}else{
				document.getElementById('form-posicion').localizacion.style.display = 'none';
			}

			$.getJSON('getpost/getAsociaciones.jsp', {
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

			numeroMarcadores(plantaResidencia);
		}

		if(localizacion == true){
			navigator.geolocation.getCurrentPosition(coordenadasUsuario);
		}
		
		// setTimeout('moveAgents()',2000);
		setTimeout('actualizar()',tiempoInicial);
	}
}

function coordenadasUsuario(pos){
	// PRUEBAAA!!! ***************************************************************
	var latitud = 38.232272 + (2*Math.random()-1)*0.0001 // pos.coords.latitude;
	var longitud = -1.698925 + (2*Math.random()-1)*0.0001 // pos.coords.longitude;
	//****************************************************************************
	
	if(nivelMsg == null || nivelMsg == 0){
		var icono = new GIcon();
		icono.image = 'markers/user.png';
		icono.iconSize = new GSize(28, 43);
		icono.iconAnchor = new GPoint(14, 43);
		var opciones = {
			icon:icono
		};
		var marker = new GMarker(new GLatLng(latitud,longitud), opciones);
		map.addOverlay(marker);
	}
	
	if(userName != '' && nivelMsg > 0 && localizacion == true){
		$.post('getpost/updateLatLong.jsp',{
			'nombre':userName,
			'latitud':latitud,
			'longitud':longitud
		});
	}
}

function numeroMarcadores(planta){
	if(activeTabIndex['dhtmlgoodies_tabView1'] == 2){
		$.getJSON('getpost/info_caronte.jsp',{
			'marcadores':'lateral'
		}, function(data){
			document.getElementById('numEnfermeros').innerHTML = '(' + data[0].nurse + ')';
			document.getElementById('numGerocultores').innerHTML = '(' + data[0].gerocultor + ')';
			document.getElementById('numAuxiliares').innerHTML = '(' + data[0].assistant + ')';
			document.getElementById('numOtros').innerHTML = '(' + data[0].otherStaff + ')';
			document.getElementById('numPolicias').innerHTML = '(' + data[0].police + ')';
			document.getElementById('numBomberos').innerHTML = '(' + data[0].firemen + ')';
			document.getElementById('numAmbulancias').innerHTML = '(' + data[0].ambulance + ')';
		});
	}
	if(activeTabIndex['dhtmlgoodies_tabView2'] == 1){
		$.getJSON('getpost/info_caronte.jsp',{
			'marcadores':'plano',
			'planta':planta
		}, function(data){
			document.getElementById('numFuegos').innerHTML = '(' + data[0].fire + ')';
			document.getElementById('numInundaciones').innerHTML = '(' + data[0].flood + ')';
			document.getElementById('numDerrumbamientos').innerHTML = '(' + data[0].collapse + ')';
			document.getElementById('numHeridos').innerHTML = '(' + data[0].injuredPerson + ')';
			document.getElementById('numPerdidos').innerHTML = '(' + data[0].lostPerson + ')';
			document.getElementById('numSanos').innerHTML = '(' + data[0].healthy + ')';
			document.getElementById('numLeves').innerHTML = '(' + data[0].slight + ')';
			document.getElementById('numGraves').innerHTML = '(' + data[0].serious + ')';
			document.getElementById('numMuertos').innerHTML = '(' + data[0].dead + ')';
			document.getElementById('numAtrapados').innerHTML = '(' + data[0].trapped + ')';
		});
	}
}

function actualizar(){
	// cada 2 segundos hacemos la peticion actualizadora de json
	$.getJSON('getpost/leeEventos.jsp', {
		'fecha': ultimamodif,
		'action':'notFirst',
		'nivel':nivelMsg
	}, function(data) {
		$.each(data, function(entryIndex, entry){
			if(entry['id'] != noActualizar){
				// el id lo asigna la base de datos
				var nuevomarcador = new ObjMarcador(entry['id'],entry['item'],entry['type'],
					entry['quantity'],entry['name'],entry['description'],entry['info'],entry['latitud'],
					entry['longitud'],entry['address'],entry['state'],entry['date'], entry['modified'],
					null,entry['size'],entry['traffic'],entry['idAssigned'],entry['user'],entry['floor']);

				// pintamos los nuevos, para lo que comprobamos que no existian
				if(marcadores_definitivos[nuevomarcador.id] == null){
					if(nuevomarcador.estado != 'erased'){
						nuevomarcador.marker = generaMarcador(nuevomarcador, DEFINITIVO);
						marcadores_definitivos[nuevomarcador.id] = nuevomarcador;
						indices[pos_indices] = nuevomarcador.id;
						pos_indices++;
					}
				}else{ // actualizamos los que han sido modificados
					// si se ha modificado algun dato se actualiza
					if(nuevomarcador.estado != 'erased'){
						map.removeOverlay(marcadores_definitivos[nuevomarcador.id].marker);
						nuevomarcador.marker = generaMarcador(nuevomarcador, DEFINITIVO);
						marcadores_definitivos[nuevomarcador.id] = nuevomarcador;
					}else{ // si se ha eliminado un marcador
						map.removeOverlay(marcadores_definitivos[nuevomarcador.id].marker);
						marcadores_definitivos[nuevomarcador.id] = null;
						pos_indices--;

						// eliminamos el indice de la matriz de indices sin dejar huecos
						var id = nuevomarcador.id;
						var index;

						for(var i = 0; i < indices.length; i++){
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
			}
		});
	});
	
	ultimamodif = obtiene_fecha();

	if(userName != ''){
		numeroMarcadores(plantaResidencia);
	}

	contador++;
	if(localizacion == true && nivelMsg > 0 && contador >= 3){
		contador = 0;
		navigator.geolocation.getCurrentPosition(coordenadasUsuario);
	}

	setTimeout('actualizar()', tiempoActualizacion);
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
		icon: icono,
		zIndexProcess: orden
	}; // se pueden arrastrar para asociarlo

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
		GEvent.addListener(marker, 'click', function(){
			marker.openInfoWindowHtml('<div id="bocadillo">' + mensaje + '</div>');
			for(var i = 0; i < 4; i++){
				map.addOverlay(lineas[i]);
			}
		});
		GEvent.addListener(marker, 'infowindowclose', function(){
			for(var i = 0; i < 4; i++){
				map.removeOverlay(lineas[i]);
			}
		});
	}else{
		GEvent.addListener(marker, 'click', function(){
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
	for(var i = 0; i < matrix.length; i++){
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
		}else if(evento.tipo == 'ambulance' || evento.tipo == 'ambulancia'){ // es una ambulancia
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
		// draggable: true // Se pueden arrastrar para asociarlo
		}; 
	}

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
			limpiarLateral(evento.marcador); // en mapa_xxx.js
		});
	}

	if((evento.planta == plantaResidencia || evento.planta == -2 || plantaResidencia == -2) && (evento.tipo != 'healthy' || verSanos == true)){
		// (!(evento.marcador=='resource' && caracter==1)){
		map.addOverlay(marker);
	}
	return marker;
}

function crearCatastrofe(marcador,tipo,cantidad,nombre,info,descripcion,direccion,longitud,latitud,estado,size,traffic,idAssigned,planta){
	var fecha = obtiene_fecha();
	var nuevomarcador = new ObjMarcador(pos_temp,marcador,tipo,cantidad,nombre,descripcion,info,
		latitud,longitud,direccion,estado,fecha,fecha,null,size,traffic,idAssigned,usuario_actual,planta);

	if(marcador == 'event'){
		nuevomarcador.size = size;
		nuevomarcador.traffic = traffic;
	}else if(marcador == 'resource' || marcador == 'people'){
		nuevomarcador.idAssigned = idAssigned;
	}

	if(marcador == 'event' && tipo == 'injuredPerson'){
		nuevomarcador.marcador = 'people';
		nuevomarcador.tipo = 'serious';
	}

	pos_temp++;
	nuevomarcador.marker = generaMarcador(nuevomarcador, TEMPORAL);

	guardar(nuevomarcador);
	escribirMensaje(nombre, planta, 'crear', 1);
	registrarHistorial(userName, marcador, tipo, fecha, 'crear');
}

function guardar(puntero){
	eliminar(puntero, TEMPORAL);

	// 1.Guardar el elemento en la base de datos
	$.ajax({
		type: 'POST',
		url: 'getpost/guardaEvento.jsp',
		data: {
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
			'usuario':puntero.usuario,
			'planta':puntero.planta
		},
		success: function(data){
			$('#guardar').innerHTML = data;
		},
		async: false
	});

	for(var i = 0; i < emergenciasAsociadas[0].length; i++){
		if(emergenciasAsociadas[0][i][1] == true){
			$.ajax({
				type: 'POST',
				url: 'getpost/update.jsp',
				data: {
					'fecha':puntero.fecha,
					'id_emergencia':emergenciasAsociadas[0][i][0],
					'accion':'asociar'
				},
				async: false
			});
		}
	}
	/*for(i = 0; i < sintomas[0].length; i++){
		if(sintomas[0][i][1] == true){
			$.ajax({
				type: 'POST',
				url: 'getpost/update.jsp',
				data: {
					'fecha':puntero.fecha,
					'tipo_sintoma':sintomas[0][i][0],
					'accion':'annadirSintoma'
				},
				async: false
			});
		}
	}*/

	// 2.Borrar el elemento del mapa y la matriz temporal
	marcadores_temporales[puntero.id] = null;

	// 3.Recargar el mapa para que aparezca el elemento nuevo
	// actualizar(); // esto adelanta el timeOut a ahora mismo
}

function modificar(id, cantidad, nombre, info, descripcion, direccion, longitud, latitud,
	estado, size, traffic, idAssigned, planta){
	// utiliza la variable puntero_temp accesible por todos y cargada por cargarModificar
	if(caracter_temp == TEMPORAL){
		// Actualizar la matriz temporal
		eliminar(marcadores_temporales[id],TEMPORAL);
		crearCatastrofe(puntero_temp.marcador,puntero_temp.tipo,cantidad,nombre,info,descripcion,
			direccion,longitud,latitud,estado,size,traffic,idAssigned,planta);
	}else if(caracter_temp == DEFINITIVO){
		// hay que hacer un update a la base de datos
		$.post('getpost/update.jsp',{
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
			'accion':'modificar'
		});
	}
}

function modificar2(id, tipo, cantidad, nombre, info, descripcion, direccion, tamanno, trafico, idAssigned, planta){
	var puntero = marcadores_definitivos[id];
	var idA;
	var accion;
	if(idAssigned != null){
		idA = idAssigned;
	}else{
		idA = puntero.idAssigned;
	}

	if(tipo != puntero.tipo &&
			((tipo == 'slight' && puntero.tipo == 'serious') ||
			(tipo == 'serious' && puntero.tipo == 'slight')) == false){
		accion = 'cambioTipo';
	}else{
		accion = 'modificar';
	}

	$.post('getpost/update.jsp',{
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
		'accion':accion
	});

	for(var i = 0; i < emergenciasAsociadas[0].length; i++){
		if(emergenciasAsociadas[0][i][1] != emergenciasAsociadas[1][i][1]){
			var accion2;
			if(emergenciasAsociadas[0][i][1] == true){
				accion2 = 'asociar';
			}else{
				accion2 = 'eliminarAsociacion';
			}
			$.post('getpost/update.jsp',{
				'id_herido':id,
				'id_emergencia':emergenciasAsociadas[0][i][0],
				'accion':accion2
			});
		}
	}
	/*for(i = 0; i < sintomas[0].length; i++){
		document.getElementById('prueba').innerHTML += sintomas[0][i][1];
		if(sintomas[0][i][1] != sintomas[1][i][1]){
			var accion3;
			if(sintomas[0][i][1] == true){
				accion3 = 'annadirSintoma';
			}else{
				accion3 = 'eliminarSintoma';
			}
			$.post('getpost/update.jsp',{
				'id_herido':id,
				'tipo_sintoma':sintomas[0][i][0],
				'accion':accion3
			});
		}
	}*/
	escribirMensaje(puntero.nombre, puntero.planta, 'modificar', 1);
	registrarHistorial(userName, puntero.marcador, tipo, id, 'modificar');
}

function eliminar(puntero,caracter){
	if(caracter == TEMPORAL){
		map.removeOverlay(puntero.marker);
	}else if(caracter == DEFINITIVO){
		// hay que hacer un update
		if(puntero.marcador == 'people' && puntero.tipo != 'healthy'){
			$.post('getpost/update.jsp',{
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
			escribirMensaje(puntero.nombre, puntero.planta, 'eliminar', 1);
			registrarHistorial(userName, puntero.marcador, puntero.tipo, puntero.id, 'modificar');
		}else{
			$.post('getpost/update.jsp',{
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
			escribirMensaje(puntero.nombre, puntero.planta, 'eliminar', 1);
			registrarHistorial(userName, puntero.marcador, puntero.tipo, puntero.id, 'eliminar');
		}
	}
}

function asociarEmergencia(id, valor){
	for(var i = 0; i < emergenciasAsociadas[0].length; i++){
		if(emergenciasAsociadas[0][i][0] == id){
			emergenciasAsociadas[0][i][1] = valor;
		}
	}
}

function annadirSintoma(tipo, valor){
	for(var i = 0; i < sintomas[0].length; i++){
		if(sintomas[0][i][0] == tipo){
			sintomas[0][i][1] = valor;
		}
	}
}

function guardar_posicion(id, lat, lng){
	var marcador = marcadores_definitivos[id];
	marcador.marker.setLatLng(new GLatLng(lat, lng));
	$.post('getpost/updateLatLong.jsp',{
		'id':id,
		'latitud':lat,
		'longitud':lng
	});
	registrarHistorial(userName, marcador.marcador, marcador.tipo, id, 'mover');
}

function actuar(idEvento,nombreUsuario,accionAux){
	var accion;
	var estadoEvento;
	var estadoUsuario;
	
	for(var i = 0;i < accionAux.length; i++){
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
		$.post('getpost/updateEstado.jsp',{
			'idEvento':idEvento,
			'nombreUsuario':nombreUsuario,
			'estadoEvento':estadoEvento,
			'estadoUsuario':estadoUsuario,
			'accion':accion
		});
	}
	var marcador = marcadores_definitivos[idEvento];
	registrarHistorial(userName, marcador.marcador, marcador.tipo, idEvento, accion);
}

function detener(idEvento,idEmergencia,nombreUsuario){
	$.post('getpost/updateEstado.jsp',{
		'idEvento':idEvento,
		'idEmergencia':idEmergencia,
		'nombreUsuario':nombreUsuario,
		'accion':'detener'
	});
}

function cambiarPlanta(num){
	map.removeOverlay(residencia);
	document.getElementById('planta' + plantaResidencia).style.fontWeight = 'normal';
	document.getElementById('planta' + plantaResidencia).style.textDecoration = 'none';

	plantaResidencia = num;

	document.getElementById('planta' + num).style.fontWeight = 'bold';
	document.getElementById('planta' + num).style.textDecoration = 'underline';
	if(num >= 0){
		document.getElementById('planoResidencia').src = 'images/residencia/planta' + num + '.jpg';
		document.getElementById('plantaPlano').innerHTML = num;
		residencia.getIcon().image = 'images/residencia/planta' + num + '.png';
		if(map.getZoom() >= 20 && map.getCurrentMapType().getName() == 'Mapa'){
			map.addOverlay(residencia);
		}
	}

	for(var i in marcadores_definitivos){
		map.removeOverlay(marcadores_definitivos[i].marker);
		if((num == -2 || marcadores_definitivos[i].planta == num || marcadores_definitivos[i].planta == -2) &&
				(marcadores_definitivos[i].tipo != 'healthy' || verSanos == true)){
			map.addOverlay(marcadores_definitivos[i].marker);
		}
	}

	numeroMarcadores(num);
}

function cambiarGeolocalizacion(valor){
	localizacion = valor;
	$.post('getpost/updateLatLong.jsp',{
		'nombre':userName,
		'localizacion':valor
	});
}

function findPos(lat,lng,dir){
	limpiar = false;
	if(puntoAux != null){
		map.removeOverlay(puntoAux);
	}
	if(dir == ''){
		localizar(new GLatLng(lat,lng));
	}else{
		localizador.getLatLng(dir,function(point){
			localizar(point)
		});
	}
}

function localizar(punto){
	localizador.getLocations(punto,function(response){
		if(response.Status.code == 200){
			document.getElementById('form-posicion').direccion.value = response.Placemark[0].address;
			document.getElementById('form-posicion').longitud.value = response.Placemark[0].Point.coordinates[0];
			document.getElementById('form-posicion').latitud.value = response.Placemark[0].Point.coordinates[1];
			var coorAux = new GLatLng(response.Placemark[0].Point.coordinates[1],response.Placemark[0].Point.coordinates[0]);
			puntoAux = new GMarker(coorAux);
			map.setCenter(coorAux,14);
			map.addOverlay(puntoAux);
			var event = GEvent.addListener(map, 'click', function(){
				map.removeOverlay(puntoAux);
				puntoAux = null;
				map.setCenter(centroAux[0], centroAux[1]);
				limpiarLateral('resource');
				GEvent.removeListener(event);
			});
		}
	});
}

function newPos(lat,lng,porDefecto){
	if(porDefecto == true){
		document.getElementById('form-posicion').porDefecto.checked = false;
		$.post('getpost/updateLatLong.jsp',{
			'nombre':userName,
			'latitud':lat,
			'longitud':lng,
			'porDefecto':true
		});
	}else{
		$.post('getpost/updateLatLong.jsp',{
			'nombre':userName,
			'latitud':lat,
			'longitud':lng
		});
	}	
}

function mostrarSanos(mostrar){
	verSanos = mostrar;
	for(var i in marcadores_definitivos){
		if(marcadores_definitivos[i].tipo == 'healthy'){
			if(mostrar == true && (marcadores_definitivos[i].planta == plantaResidencia || marcadores_definitivos[i].planta == -2 || plantaResidencia == -2)){
				map.addOverlay(marcadores_definitivos[i].marker);
			}else{
				map.removeOverlay(marcadores_definitivos[i].marker);
			}
		}
	}
}

function escribirMensaje(nombre, planta, accion, nivel){
	var mensaje;
	if(accion == 'crear'){
		mensaje = 'Nueva emergencia ' + nombre;
		if(planta >= 0){
			mensaje += ' en la planta ' + planta;
		}else if(planta == -1){
			mensaje += ' en el exterior';
		}
	}else if(accion == 'modificar'){
		mensaje = 'Emergencia ' + nombre + ' modificada';
	}else if(accion == 'eliminar'){
		mensaje = 'Emergencia ' + nombre + ' eliminada';
	}
	$.post('getpost/escribirMensaje.jsp', {
		'creador':usuario_actual,
		'mensaje':mensaje,
		'nivel':nivel
	});
}

function registrarHistorial(usuario, marcador, tipo, idEmergencia, accion){
	var evento;
	if(accion == 'crear'){
		evento = 'Evento creado por el usuario ' + usuario;
	}else if(accion == 'modificar'){
		evento = 'Emergencia ' + idEmergencia + ' modificada por ' + usuario;
	}else if(accion == 'eliminar'){
		evento = 'Emergencia ' + idEmergencia + ' eliminada por ' + usuario;
	}else if(accion == 'mover'){
		evento = 'Emergencia ' + idEmergencia + ' cambiada de sitio por ' + usuario;
	}else{
		evento = 'El usuario ' + usuario + ' ha actuado sobre ' + idEmergencia + ' - ' + accion;
	}

	$.post('getpost/registrarHistorial.jsp',{
		'usuario':usuario,
		'marcador':marcador,
		'tipo':tipo,
		'idEmergencia':idEmergencia,
		'evento':evento,
		'accion':accion
	});
}

function obtiene_fecha() {
	var hora;
	$.ajax({
		type: 'GET',
		dataType: 'json',
		url: 'getpost/getHora.jsp',
		success: function(data){
			hora = data.hora;
		},
		async: false
	});
	return hora;
	
	/*
	// llega a algo como esto: 1992-01-01 00:00:01 , necesario para MySql a partir de la fecha actual
	var fecha_actual = new Date();
	var dia = fecha_actual.getDate();
	var mes = fecha_actual.getMonth() + 1;
	var anno = fecha_actual.getFullYear();
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

	return (anno + '-' + mes + '-' + dia + ' ' + horas + ':' + minutos + ':' + segundos + '.' + milisegundos);
	*/
}

function fondo(marker, b){
	return -100000;
}

function orden(marker, b){
	return 1;
}