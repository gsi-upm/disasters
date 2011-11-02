var map;
var localizador;

var tiempoActualizacion = 2000;
var tiempoInicial = tiempoActualizacion;
var contador = 0;

var ultimamodif = '1992-01-01 00:00:01.000'; // una fecha antigua para empezar
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
		localizador = new GClientGeocoder();
		map = new GMap2(document.getElementById('map_canvas'));
		var centro = mapInit(); // en mapa_xxx.js
		map.setCenter(centro['center'], centro['zoom']);
		map.addControl(new GLargeMapControl());    // controles completos
		map.addControl(new GScaleControl());       // escala
		map.addControl(new GMenuMapTypeControl()); // selector mapa
		// map.addControl(new GOverviewMapControl()); // minimapa
		var minimapa = new GOverviewMapControl(); // minimapa

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

		cambiaIcono('event',seleccionRadio(document.getElementById('catastrofes'),0));
		cambiaIcono('people',seleccionRadio(document.getElementById('heridos'),2),1);
		// setTimeout('moveAgents()',2000);
		setTimeout('actualizar()',tiempoInicial);
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

	actualizar2(); // en mapa_xxx.js

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
		icono.infoWindowAnchor = new GPoint(14, 0);
		opciones = {
			icon: icono,
			zIndexProcess: orden,
			draggable: true // Se pueden arrastrar para asociarlo
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
			marker.openInfoWindowHtml('<div id="bocadillo">&iquest;Confirmar cambio de posici&oacute;n?<br/>' +
				'<a id="confirmar" href="#" onclick="map.closeInfoWindow(); guardar_posicion(' + evento.id +
				',' + nuevaLat + ',' + nuevaLong + ')" >Confirmar</a>'+ ' - ' +
				'<a id="cancelar" href="#" onclick="map.closeInfoWindow(); cancelar_asignacion(' + evento.id + ');">Cancelar</a></div>');

			// Temporal*****************************************************************************************
			//cancelar_asignacion(evento.id);
			//**************************************************************************************************
			
			/*marker.openInfoWindowHtml('<div id="bocadillo">¿Confirmar cambio de posición?<br/>'+
				'<a id="confirmar" href="#" onclick="map.closeInfoWindow();guardar_posicion('+evento.id+
				','+marker.getLatLng().lat()+','+marker.getLatLng().lng()+')">Confirmar</a>'+ ' - '+
				'<a id="cancelar" href="#" onclick="map.closeInfoWindow();">Cancelar</a></div>');*/
		});

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
			'fecha':puntero.fecha,
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

	if(puntero.cantidad-cantidad > 0 && tipo != 'healthy'){
		crearCatastrofe(puntero.marcador, puntero.tipo, puntero.cantidad-1, puntero.nombre, puntero.info, puntero.descripcion, puntero.direccion,
			puntero.longitud+0.00001, puntero.latitud-0.000005, puntero.estado, puntero.tamanno, puntero.trafico, puntero.idAssigned, puntero.planta);
	}
	


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

function obtiene_fecha() {
	// La hora se obtiene del servidor
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

	// La hora se obtiene en local
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