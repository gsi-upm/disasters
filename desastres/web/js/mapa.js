const tiempoActualizacion = 2000;
const tiempoInicial = 2000;
var contador = 0;

var localizador;
var map;

var ultimamodif = '1992-01-01 00:00:01.000'; // una fecha antigua para empezar
var desfase = 0; // positivo: hora de local posterior a servidor
var marcadores_definitivos = new Array();
var marcadores_temporales = new Array();
var indices = new Array();
var pos_def = 0;
var pos_temp = 0;
var pos_indices = 0;

var puntero_temp;
var caracter_temp;
const temporal = 0;
const definitivo = 1;

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
var emergenciasAsociadas = new Array(new Array(), new Array());
var sintomas = new Array(new Array(), new Array());
var verSanos = false;
var limpiar = true;
var noActualizar = 0;
var centroAux = new Array();
var puntoAux;
var markerId;

const roadmap = google.maps.MapTypeId.ROADMAP;

var infoWindow;
var infoWinMarker = '';

/**
 * Funcion que se ejecuta para iniciar el mapa cuando se abre el navegador 
 */
function initialize(){
	localizador = new google.maps.Geocoder();
	infoWindow = new google.maps.InfoWindow();
	var centro = mapInit(); // en mapa_xxx.js
	var myOptions = {
		center: centro.center,
		zoom: centro.zoom,
		mapTypeId: roadmap,
		mapTypeControlOptions: {
			style: google.maps.MapTypeControlStyle.DROPDOWN_MENU
		},
		panControl: false,
		overviewMapControl: true,
		scaleControl: true
	};
	map = new google.maps.Map(document.getElementById('map_canvas'), myOptions);
	
	google.maps.event.addListener(map, 'click', function(){
		infoWindow.close();
		if(infoWinMarker != 'building'){
			limpiarLateral(infoWinMarker);
		}
	});

	buildingInit(); // en mapa_xxx.js

	// hacemos la peticion inicial del json (baja todo menos los borrados)
	$.getJSON('getpost/leeEventos.jsp', {
		'fecha': ultimamodif,
		'action':'firstTime',
		'nivel':nivelMsg
	}, function(data){
		$.each(data, function(entryIndex, entry){
			var nuevomarcador = new ObjMarcador(entry.id, entry.item, entry.type,
				entry.quantity, entry.name, entry.description, entry.info, entry.latitud,
				entry.longitud, entry.address, entry.size, entry.traffic, entry.floor,
				entry.state, entry.idAssigned, entry.date, entry.modified, entry.user, null);
			nuevomarcador.marker = generaMarcador(nuevomarcador, definitivo);
			marcadores_definitivos[nuevomarcador.id] = nuevomarcador;
			indices[pos_indices] = nuevomarcador.id;
			pos_indices++;
			if(entry.name == userName){
				markerId = entry.id;
			}
		});
	});
	
	initialize2(); // en mapa_xxx.js
	
	if(userName != ''){
		cambiaIcono('event', seleccionRadio(document.getElementById('catastrofes'), 0));
		cambiaIcono('people', seleccionRadio(document.getElementById('heridos'), 2), 1);
	}

	ultimamodif = obtiene_fecha(true);
	// setTimeout('moveAgents()', 2000);
	setTimeout('actualizar()', tiempoInicial);
}

/**
 * Actualiza el mapa
 */
function actualizar(){
	// cada 2 segundos hacemos la peticion actualizadora de json
	$.getJSON('getpost/leeEventos.jsp', {
		'fecha': ultimamodif,
		'action':'notFirst',
		'nivel':nivelMsg
	}, function(data){
		$.each(data, function(entryIndex, entry){
			if(entry.id != noActualizar){
				// el id lo asigna la base de datos
				var nuevomarcador = new ObjMarcador(entry.id, entry.item, entry.type,
					entry.quantity, entry.name, entry.description, entry.info, entry.latitud,
					entry.longitud, entry.address, entry.size, entry.traffic, entry.floor,
					entry.state, entry.idAssigned, entry.date, entry.modified, entry.user, null);

				// pintamos los nuevos, para lo que comprobamos que no existian
				if(marcadores_definitivos[nuevomarcador.id] == null){
					if(nuevomarcador.estado != 'erased'){
						nuevomarcador.marker = generaMarcador(nuevomarcador, definitivo);
						marcadores_definitivos[nuevomarcador.id] = nuevomarcador;
						indices[pos_indices] = nuevomarcador.id;
						pos_indices++;
					}
				}else{ // actualizamos los que han sido modificados
					// si se ha modificado algun dato se actualiza
					if(nuevomarcador.estado != 'erased'){
						marcadores_definitivos[nuevomarcador.id].marker.setMap(null);
						nuevomarcador.marker = generaMarcador(nuevomarcador, definitivo);
						marcadores_definitivos[nuevomarcador.id] = nuevomarcador;
					}else{ // si se ha eliminado un marcador
						marcadores_definitivos[nuevomarcador.id].marker.setMap(map);
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
							indices[index] = indices[index + 1];
							index++;
						}
						indices[index] = null;
						
						if(entry.id == markerId){
							document.location.href = 'logout.jsp';
						}
					}
				}
			}
		});
	});

	actualizar2(); // en mapa_xxx.js
	
	ultimamodif = obtiene_fecha(false);
	setTimeout('actualizar()', tiempoActualizacion);
}

/**
 * Marca en el mapa un edificio
 *
 * @param type Tipo de edificio
 * @param mensaje Mensaje que muestra el bocadillo
 * @param latitud Latitud del edificio
 * @param longitud Longitud del edificio
 * @return Marcador
 */
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

	var marker = new google.maps.Marker({
		position: new google.maps.LatLng(latitud, longitud),
		icon: new google.maps.MarkerImage('markers/buildings/' + imagen)
	});
	
	google.maps.event.addListener(marker, 'click', function(){
		infoWindow.close();	
		infoWindow = new google.maps.InfoWindow({content:'<div id="bocadillo">' + mensaje + '</div>'});
		infoWinMarker = 'building';
		infoWindow.open(map, marker);
		
		if(type == 'geriatricCenter'){
			var esquina1 = new google.maps.LatLng(38.232440, -1.699160); // desplazado (-0.000095, 0.000120)
			var esquina2 = new google.maps.LatLng(38.232380, -1.698640);
			var esquina3 = new google.maps.LatLng(38.232105, -1.698690);
			var esquina4 = new google.maps.LatLng(38.232165, -1.699210);
			var opcRect = {paths:[esquina1, esquina2, esquina3, esquina4], strokeColor:'#00FF00', strokeWeight:1, strokeOpacity:0.5, fillOpacity:0};
			var rectangulo = new google.maps.Polygon(opcRect);
			rectangulo.setMap(map);
			
			google.maps.event.addListener(infoWindow, 'closeclick', function(){
				rectangulo.setMap(null);
			});
		}
	});
	
	marker.setMap(map);
	matrix[matrixIndex] = marker;
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

/**
 * Obtiene los datos de un marcador
 * 
 * @param evento Indica si es emergencia (event), herido (people) o usuario (resource)
 * @param caracter Indica si es temporal o definitivo
 * @return Marcador
 */
function generaMarcador(evento, caracter){
	var opciones = definirOpciones(evento); // en mapa_xxx.js
	var marker = comportamientoMarcador(evento, caracter, opciones); // en mapa_xxx.js
	return marker;
}

/**
 * Crea una nueva catastrofe
 * 
 * @param marcador
 * @param tipo
 * @param cantidad
 * @param nombre
 * @param info
 * @param descripcion
 * @param direccion
 * @param longitud
 * @param latitud
 * @param estado
 * @param size
 * @param traffic
 * @param idAssigned
 * @param planta
 */
function crearCatastrofe(marcador, tipo, cantidad, nombre, info, descripcion, direccion, longitud, latitud, estado, size,traffic, idAssigned, planta){
	var fecha = obtiene_fecha();
	var nuevomarcador = new ObjMarcador(pos_temp, marcador, tipo, cantidad, nombre, descripcion, info, latitud,
		longitud, direccion, size, traffic, planta, estado, idAssigned, fecha, fecha, usuario_actual, null);

	if(marcador == 'event' && tipo == 'injuredPerson'){
		nuevomarcador.marcador = 'people';
		nuevomarcador.tipo = 'serious';
	}
	
	pos_temp++;
	nuevomarcador.marker = generaMarcador(nuevomarcador, temporal);
    marcadores_temporales[nuevomarcador.id] = nuevomarcador;
	
	if(proyecto == 'caronte'){
		guardar(nuevomarcador);
	}
}

/**
 * Guarda en la base de datos el marcador pasado como parametro
 * 
 * @param puntero Marcador que se va a guardar
 */
function guardar(puntero){
	eliminar(puntero, temporal);
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
			'size':puntero.size,
			'traffic':puntero.traffic,
			'planta':puntero.planta,
			'estado':puntero.estado,
			'idAssigned':puntero.idAssigned,
			'fecha':puntero.fecha,
			'usuario':puntero.usuario
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
					'accion':'asociar',
					'fecha':puntero.fecha,
					'id_emergencia':emergenciasAsociadas[0][i][0]
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
					'accion':'annadirSintoma',
					'fecha':puntero.fecha,
					'tipo_sintoma':sintomas[0][i][0]
				},
				async: false
			});
		}
	}*/

	if(proyecto == 'caronte'){
		escribirMensaje(puntero, 'crear', 1);
		registrarHistorial(userName, puntero.marcador, puntero.tipo, puntero.fecha, 'crear');
		limpiar = true;
		limpiarLateral(puntero.marcador);
	}

	// 2.Borrar el elemento del mapa y la matriz temporal
	marcadores_temporales[puntero.id] = null;

	// 3.Recargar el mapa para que aparezca el elemento nuevo
	// actualizar(); // esto adelanta el timeOut a ahora mismo
}

/**
 * Modifica los datos de un marcador (funcion usada en 'desastres')
 * 
 * @param id
 * @param cantidad
 * @param nombre
 * @param descripcion
 * @param info
 * @param latitud
 * @param longitud
 * @param direccion
 * @param size
 * @param traffic
 * @param estado
 * @param idAssigned
 */
function modificar(id, cantidad, nombre, descripcion, info, latitud, longitud, direccion, size, traffic, estado, idAssigned){
	// utiliza la variable puntero_temp accesible por todos y cargada por cargarModificar
	if(caracter_temp == temporal){
		// Actualizar la matriz temporal
		eliminar(marcadores_temporales[id], temporal);
		crearCatastrofe(puntero_temp.marcador, puntero_temp.tipo, cantidad, nombre, info, descripcion,
			direccion, longitud, latitud, estado, size, traffic, idAssigned, -2);
	}else if(caracter_temp == definitivo){
		// hay que hacer un update a la base de datos
		$.post('getpost/update.jsp',{
			'accion':'modificar',
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
			'size':size,
			'traffic':traffic,
			'planta':puntero_temp.planta,
			'estado':estado,
			'idAssigned':idAssigned,
			'fecha':puntero_temp.fecha,
			'usuario':usuario_actual
		});
	}
}

/**
 * Modifica los datos de un marcador (funcion usada en 'caronte')
 * 
 * @param id
 * @param tipo
 * @param cantidad
 * @param nombre
 * @param descripcion
 * @param info
 * @param direccion
 * @param tamanno
 * @param trafico
 * @param planta
 * @param idAssigned
 */
function modificar2(id, tipo, cantidad, nombre, descripcion, info, direccion, tamanno, trafico, planta, idAssigned){
	var puntero = marcadores_definitivos[id];
	var idA;
	var accion;
	if(idAssigned != null){
		idA = idAssigned;
	}else{
		idA = puntero.idAssigned;
	}

	if(tipo != puntero.tipo && ((tipo == 'slight' && puntero.tipo == 'serious') || (tipo == 'serious' && puntero.tipo == 'slight')) == false){
		accion = 'cambioTipo';
	}else{
		accion = 'modificar';
	}

	$.post('getpost/update.jsp',{
		'accion':accion,
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
		'size':tamanno,
		'traffic':trafico,
		'planta':planta,
		'estado':puntero.estado,
		'idAssigned':idA,
		'fecha':puntero.fecha,
		'usuario':usuario_actual
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
				'accion':accion2,
				'id_herido':id,
				'id_emergencia':emergenciasAsociadas[0][i][0]
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
				'accion':accion3,
				'id_herido':id,
				'tipo_sintoma':sintomas[0][i][0]
			});
		}
	}*/

	if(puntero.cantidad-cantidad > 0 && tipo != 'healthy'){
		for(i = 0; i < emergenciasAsociadas[0].length; i++){
			emergenciasAsociadas[0][i][1] = emergenciasAsociadas[1][i][1];
		}
		/*for(i = 0; i < sintomas[0].length; i++){
			sintomas[0][i][1] = sintomas[1][i][1];
		}*/
		crearCatastrofe(puntero.marcador, puntero.tipo, puntero.cantidad-1, puntero.nombre, puntero.info, puntero.descripcion, puntero.direccion,
			puntero.longitud+0.00001, puntero.latitud-0.000005, puntero.estado, puntero.size, puntero.traffic, puntero.idAssigned, puntero.planta);
	}

	var evento = {'id':id, 'marcador':puntero.marcador, 'tipo':tipo, 'cantidad':cantidad, 'nombre':nombre,
		'info':info, 'descripcion':descripcion, 'size':tamanno, 'traffic':trafico, 'planta':planta}
	if(proyecto == 'caronte'){
		escribirMensaje(evento, 'modificar', 2);
		registrarHistorial(userName, puntero.marcador, tipo, id, 'modificar');
		limpiarLateral(puntero.marcador);
	}
}

/**
 * Elimina un marcador de la base de datos
 * 
 * @param puntero
 * @param caracter
 */
function eliminar(puntero, caracter){
	if(caracter == temporal){
		puntero.marker.setMap(null);
	}else if(caracter == definitivo){
		// hay que hacer un update
		if(puntero.marcador == 'people' && puntero.tipo != 'healthy'){
			$.post('getpost/update.jsp',{
				'accion':'eliminar',
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
				'size':puntero.size,
				'traffic':puntero.traffic,
				'planta':puntero.planta,
				'estado':'active',
				'idAssigned':puntero.idAssigned,
				'fecha':puntero.fecha,
				'usuario':usuario_actual
			});
			if(proyecto == 'caronte'){
				escribirMensaje(puntero, 'eliminar', 1);
				registrarHistorial(userName, puntero.marcador, puntero.tipo, puntero.id, 'modificar');
				limpiarLateral(puntero.marcador);
			}
		}else{
			$.post('getpost/update.jsp',{
				'accion':'eliminar',
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
				'size':puntero.size,
				'traffic':puntero.traffic,
				'planta':puntero.planta,
				'estado':'erased',
				'idAssigned':puntero.idAssigned,
				'fecha':puntero.fecha,
				'usuario':usuario_actual
			});
			if(proyecto == 'caronte'){
				escribirMensaje(puntero, 'eliminar', 1);
				registrarHistorial(userName, puntero.marcador, puntero.tipo, puntero.id, 'eliminar');
				limpiarLateral(puntero.marcador);
			}
		}
	}
}

/**
 * Cancela la asociacion entre un herido y una emergencia
 * 
 * @param id Identificador del marcador
 */
function cancelar_asignacion(id){
	// borro la posicion actual y lo dibujo en la antigua
	marcadores_definitivos[id].marker.setMap(null);
	marcadores_definitivos[id].marker = generaMarcador(marcadores_definitivos[id], definitivo);
	noActualizar = 0;
}

/**
 * Obtiene la hora
 */
function obtiene_fecha(primera) {
	// La hora se obtiene en local
	var fecha_actual = new Date(new Date().getTime() - desfase);
	
	if(primera){
		// La hora se obtiene del servidor
		var hora_server;
		$.ajax({
			type: 'GET',
			dataType: 'json',
			url: 'getpost/getHora.jsp',
			success: function(data){
				hora_server = data.hora;
			},
			async: false
		});
		desfase = fecha_actual.getTime() - hora_server;
		fecha_actual = new Date(fecha_actual.getTime() - desfase);
	}

	var dia = fecha_actual.getDate();
	var mes = fecha_actual.getMonth() + 1;
	var anno = fecha_actual.getFullYear();
	var horas = fecha_actual.getHours();
	var minutos = fecha_actual.getMinutes();
	var segundos = fecha_actual.getSeconds();
	var milisegundos = fecha_actual.getMilliseconds();

	if(mes < 10){
		mes = '0' + mes;
	}
	if(dia < 10){
		dia = '0' + dia;
	}
	if(horas < 10){
		horas = '0' + horas;
	}
	if(minutos < 10){
		minutos = '0' + minutos;
	}
	if(segundos < 10){
		segundos = '0' + segundos;
	}
	if(milisegundos < 10){
		milisegundos = '00' + milisegundos;
	}else if(milisegundos < 100){
		milisegundos = '0' + milisegundos;
	}
	
	// llega a algo como esto: 1992-01-01 00:00:01 , necesario para MySql a partir de la fecha actual
	var hora = anno + '-' + mes + '-' + dia + ' ' + horas + ':' + minutos + ':' + segundos + '.' + milisegundos;
	return hora;
}