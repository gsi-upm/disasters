var TIEMPO_ACTUALIZACION = 2000; // const
var TIEMPO_INICIAL = 2000; // const
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
var TEMPORAL = 0; // const
var DEFINITIVO = 1; // const

var hospitals = new Array();
var policeStations = new Array();
var firemenStations = new Array();
var geriatricCenters = new Array();

var localizacion = (navigator.geolocation != null);
var residencia; // marcador de la imagen de la residencia
var plantaResidencia = -1;
var emergenciasAsociadas = new Array();
//var sintomas = new Array();
var verSanos = false;
var limpiar = true;
var noActualizar = 0;
var markerId;

var ROADMAP = google.maps.MapTypeId.ROADMAP; // const

var infoWindow;
var infoWinMarker;

var id = 0; // El id que le vamos a dar al evento que se envia a OCP a traves del paquete gsi.sendToOCP

// variables userName, usuario_actual, usuario_actual_tipo, nivelMsg e idioma definidas en index.jsp

/**
 * Funcion que se ejecuta para iniciar el mapa cuando se abre el navegador.
 */
function initialize(){
	if(document.getElementById('username') != null){
		document.getElementById('username').focus();
	}
	
	localizador = new google.maps.Geocoder();
	var centro = mapInit(); // en mapa_xxx.js
	var myOptions = {
		center: centro.center,
		zoom: centro.zoom,
		mapTypeId: ROADMAP,
		mapTypeControlOptions: {
			style: google.maps.MapTypeControlStyle.DROPDOWN_MENU
		},
		panControl: false,
		overviewMapControl: true,
		scaleControl: true
	};
	map = new google.maps.Map(document.getElementById('map_canvas'), myOptions);
	
	google.maps.event.addListener(map, 'click', function(){
		if(infoWindow != null){
			infoWindow.close();
			infoWindow = null;
			if(infoWinMarker != 'building'){
				limpiarLateral(infoWinMarker);
			}
			infoWinMarker = null;
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
			nuevomarcador.marker = generaMarcador(nuevomarcador, DEFINITIVO);
			marcadores_definitivos[nuevomarcador.id] = nuevomarcador;
			indices[pos_indices] = nuevomarcador.id;
			pos_indices++;
			if(entry.name == userName){
				markerId = entry.id;
			}
		});
	});
	/* PUSHLET ***************
	p_join_listen('/events');
	*************************/
	
	initialize2(); // en mapa_xxx.js
	
	if(userName != ''){
		cambiaIcono('event', seleccionRadio(document.getElementById('catastrofes'), 0));
		cambiaIcono('people', seleccionRadio(document.getElementById('heridos'), 2), 1);
	}
	
	ultimamodif = obtiene_fecha(true);
	// setTimeout('moveAgents()', 2000);
	setTimeout('actualizar()', TIEMPO_INICIAL);
}

/**
 * Actualiza el mapa.
 */
function actualizar(){ //function onData(pushletEvent){ // Event Callback: display all events
	/* PUSHLET ****************************************
	var tipo = pushletEvent.get('p_subject');
	ultimamodif = pushletEvent.get('fecha');
	var data = $.parseJSON(pushletEvent.get('datos'));
	**************************************************/
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
						nuevomarcador.marker = generaMarcador(nuevomarcador, DEFINITIVO);
						marcadores_definitivos[nuevomarcador.id] = nuevomarcador;
						indices[pos_indices] = nuevomarcador.id;
						pos_indices++;
					}
				}else{ // actualizamos los que han sido modificados
					// si se ha modificado algun dato se actualiza
					if(nuevomarcador.estado != 'erased'){
						marcadores_definitivos[nuevomarcador.id].marker.setMap(null);
						nuevomarcador.marker = generaMarcador(nuevomarcador, DEFINITIVO);
						marcadores_definitivos[nuevomarcador.id] = nuevomarcador;
					}else{ // si se ha eliminado un marcador
						marcadores_definitivos[nuevomarcador.id].marker.setMap(null);
						delete marcadores_definitivos[nuevomarcador.id];
						pos_indices--;

						// eliminamos el indice de la matriz de indices sin dejar huecos
						var index = 0;
						while(indices[index] != nuevomarcador.id){
							index++;
						}
						while(indices[index + 1] != null){
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
	setTimeout('actualizar()', TIEMPO_ACTUALIZACION);
}

/**
 * Marca en el mapa un edificio.
 *
 * @param type tipo de edificio
 * @param mensaje mensaje que muestra el bocadillo
 * @param latitud latitud del edificio
 * @param longitud longitud del edificio
 * @param inicio true si se muestra al inicio
 * @return marcador
 */
function generateBuilding(type, mensaje, latitud, longitud, inicio){
	var imagen;
	if(type == 'hospital'){
		imagen = 'ambulanceStation.png';
	}else if(type == 'policeStation'){
		imagen = 'policeStation.png';
	}else if(type == 'firemenStation'){
		imagen = 'firemenStation.png';
	}else if(type == 'geriatricCenter'){
		imagen = 'geriatricCenter.png';
	}
	
	var marker = new google.maps.Marker({
		position: new google.maps.LatLng(latitud, longitud),
		icon: new google.maps.MarkerImage('markers/buildings/' + imagen),
		map: (inicio) ? map : null
	});
	
	google.maps.event.addListener(marker, 'click', function(){
		if(infoWindow != null){
			infoWindow.close();
		}
		infoWindow = new google.maps.InfoWindow({content:'<div id="bocadillo">' + mensaje + '</div>'});
		infoWinMarker = 'building';
		infoWindow.open(map, marker);
		
		/*if(type == 'geriatricCenter'){
			var esquinas = esquinasResidencia(latitud, longitud); // en mapa_xxx.js
			var opcCerc = {
				paths:esquinas, strokeColor: '#00FF00', strokeWeight: 1, strokeOpacity: 0.5, fillOpacity: 0
			};
			var cercado = new google.maps.Polygon(opcCerc);
			cercado.setMap(map);
			
			google.maps.event.addListener(infoWindow, 'closeclick', function(){
				cercado.setMap(null);
			});
			
			google.maps.event.addListener(cercado, 'click', function(){
				if(infoWindow != null){
					infoWindow.close();
					infoWindow = null;
					if(infoWinMarker != 'building'){
						limpiarLateral(infoWinMarker);
					}
					infoWinMarker = null;
				}
				cercado.setMap(null);
			});
			
			google.maps.event.addListener(map, 'click', function(){
				cercado.setMap(null);
			});
		}*/
	});
	
	if(type == 'hospital'){
		hospitals.push(marker);
	}else if(type == 'policeStation'){
		policeStations.push(marker);
	}else if(type == 'firemenStation'){
		firemenStations.push(marker);
	}else if(type == 'geriatricCenter'){
		geriatricCenters.push(marker);
	}

	return marker;
}

/**
 * Obtiene los datos de un marcador.
 * 
 * @param evento indica si es emergencia (event), herido (people) o usuario (resource)
 * @param caracter indica si es temporal o definitivo
 * @return marcador
 */
function generaMarcador(evento, caracter){
	var opciones = definirOpciones(evento); // en mapa_xxx.js
	var marker = comportamientoMarcador(evento, caracter, opciones); // en mapa_xxx.js
	return marker;
}

/**
 * Crea una nueva catastrofe.
 * 
 * @param marcador marcador
 * @param tipo tipo
 * @param cantidad cantidad
 * @param nombre nombre
 * @param info informacion
 * @param descripcion descripcion
 * @param direccion direccion
 * @param longitud longitud
 * @param latitud latitud
 * @param estado estado
 * @param size tamanno
 * @param traffic trafico
 * @param idAssigned id asignada
 * @param planta planta
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
	nuevomarcador.marker = generaMarcador(nuevomarcador, TEMPORAL);
	marcadores_temporales[nuevomarcador.id] = nuevomarcador;
	
	if(PROYECTO == 'caronte'){
		guardar(nuevomarcador);
	}
	
	/* Hacer POST de las variables de la ontologia hacia el servlet para enviar a OCP. En este caso para un FireEvent */
	if(marcador == 'event' && tipo == 'fire' && estado == 'active'){
		var type = 'produce'; // solo toma el valor 'register' en el servlet que recibe este POST. Al menos por ahora.
		// Variable que establece si: 
		// 1) nos estamos registrando para el evento que viene marcado por el value de "tipo" (register) <-- valores q toma type
		// 2) estamos produciendo solo el evento (produce)

		var descTotal = info + ' ' + descripcion; // La descripcion son ambos campos
		
		id++; // El id empieza desde el numero 1 se va incrementando cada vez que se genera un evento.
		
		$.ajax({
			url: '/caronte/ProcessEvent',
			type: 'POST',
			data: {
				'id':id,
				'type':type,
				'event':tipo,
				'size':size,
				'description':descTotal,
				'name':nombre,
				'longitude':longitud,
				'floor':planta,
				'latitude':latitud,
				'date':fecha
			},
			success: function(data, status){
				console.log('Success!!');
				console.log('Datos devueltos por ProcessEvent: ' + data);
				console.log('Mensaje de response code de HTTP enviado por ProcessEvent: ' + status);
			},
			error: function(xhr, desc, err){
				console.log(xhr);
				console.log('Desc: ' + desc + '\nErr: ' + err);
			}
		});
	}
}

/**
 * Guarda en la base de datos el marcador pasado como parametro.
 * 
 * @param puntero marcador que se va a guardar
 */
function guardar(puntero){
	eliminar(puntero, TEMPORAL);
	// 1.Guardar el elemento en la base de datos
	$.post('getpost/guardaEvento.jsp', {
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
	}, function(data){
		puntero.id = $.parseJSON(data).id;
		for(var i = 0; i < emergenciasAsociadas.length; i++){
			if(emergenciasAsociadas[i].valor == true){
				$.post('getpost/update.jsp', {
					'accion':'asociar',
					'fecha':puntero.fecha,
					'id_emergencia':emergenciasAsociadas[i].id
				});
			}
		}
		/*for(i = 0; i < sintomas.length; i++){
			if(sintomas[i].valor == true){
				$.post('getpost/update.jsp', {
					'accion':'annadirSintoma',
					'fecha':puntero.fecha,
					'tipo_sintoma':sintomas[i].tipo
				});
			}
		}*/
		if(PROYECTO == 'caronte'){
			escribirMensaje(puntero, 'crear', 1);
			registrarHistorial(userName, puntero.marcador, puntero.tipo, puntero.fecha, 'crear');
			limpiar = true;
			limpiarLateral(puntero.marcador);
		}
	});

	// 2.Borrar el elemento del mapa y la matriz temporal
	delete marcadores_temporales[puntero.id];

	// 3.Recargar el mapa para que aparezca el elemento nuevo
	// actualizar(); // esto adelanta el timeOut a ahora mismo
}

/**
 * Modifica los datos de un marcador (funcion usada en 'desastres').
 * 
 * @param id identificador
 * @param cantidad cantidad
 * @param nombre nombre
 * @param descripcion descripcion
 * @param info informacion
 * @param latitud latitud
 * @param longitud longitud
 * @param direccion direccion
 * @param size tamanno
 * @param traffic trafico
 * @param estado estado
 * @param idAssigned id asignada
 */
function modificar(id, cantidad, nombre, descripcion, info, latitud, longitud, direccion, size, traffic, estado, idAssigned){
	// utiliza la variable puntero_temp accesible por todos y cargada por cargarModificar
	if(caracter_temp == TEMPORAL){
		// Actualizar la matriz temporal
		eliminar(marcadores_temporales[id], TEMPORAL);
		crearCatastrofe(puntero_temp.marcador, puntero_temp.tipo, cantidad, nombre, info, descripcion,
			direccion, longitud, latitud, estado, size, traffic, idAssigned, -2);
	}else if(caracter_temp == DEFINITIVO){
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
 * Modifica los datos de un marcador (funcion usada en 'caronte').
 * 
 * @param id identificador
 * @param tipo tipo
 * @param cantidad cantidad
 * @param nombre nombre
 * @param descripcion descripcion
 * @param info informacion
 * @param direccion direccion
 * @param tamanno tamanno
 * @param trafico trafico
 * @param planta planta
 * @param idAssigned id asignada
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
	
	for(var i = 0; i < emergenciasAsociadas.length; i++){
		if(emergenciasAsociadas[i].valor != emergenciasAsociadas[i].valorBD){
			var accion2;
			if(emergenciasAsociadas[i].valor == true){
				accion2 = 'asociar';
			}else{
				accion2 = 'eliminarAsociacion';
			}
			$.post('getpost/update.jsp',{
				'accion':accion2,
				'id_herido':id,
				'id_emergencia':emergenciasAsociadas[i].id
			});
		}
	}
	/*for(i = 0; i < sintomas.length; i++){
		if(sintomas[i].valor != sintomas[i].valorBD){
			var accion3;
			if(sintomas[i].valor == true){
				accion3 = 'annadirSintoma';
			}else{
				accion3 = 'eliminarSintoma';
			}
			$.post('getpost/update.jsp',{
				'accion':accion3,
				'id_herido':id,
				'tipo_sintoma':sintomas[i].tipo
			});
		}
	}*/

	if(puntero.cantidad - cantidad > 0 && tipo != 'healthy'){
		for(i = 0; i < emergenciasAsociadas.length; i++){
			emergenciasAsociadas[i].valor = emergenciasAsociadas[i].valorBD;
		}
		/*for(i = 0; i < sintomas.length; i++){
			sintomas[i].valor = sintomas[i].valorBD;
		}*/
		crearCatastrofe(puntero.marcador, puntero.tipo, puntero.cantidad-1, puntero.nombre, puntero.info, puntero.descripcion, puntero.direccion,
			puntero.longitud+0.00001, puntero.latitud-0.000005, puntero.estado, puntero.size, puntero.traffic, puntero.idAssigned, puntero.planta);
	}

	var evento = {'id':id, 'marcador':puntero.marcador, 'tipo':tipo, 'cantidad':cantidad, 'nombre':nombre,
		'info':info, 'descripcion':descripcion, 'size':tamanno, 'traffic':trafico, 'planta':planta}
	if(PROYECTO == 'caronte'){
		escribirMensaje(evento, 'modificar', 2);
		registrarHistorial(userName, puntero.marcador, tipo, id, 'modificar');
		limpiarLateral(puntero.marcador);
	}
}

/**
 * Elimina un marcador de la base de datos.
 * 
 * @param puntero puntero
 * @param caracter caracter
 */
function eliminar(puntero, caracter){
	if(caracter == TEMPORAL){
		puntero.marker.setMap(null);
	}else if(caracter == DEFINITIVO){
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
			if(PROYECTO == 'caronte'){
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
			if(PROYECTO == 'caronte'){
				escribirMensaje(puntero, 'eliminar', 1);
				registrarHistorial(userName, puntero.marcador, puntero.tipo, puntero.id, 'eliminar');
				limpiarLateral(puntero.marcador);
			}
		}
	}
}

/**
 * Cancela la asociacion entre un herido y una emergencia.
 * 
 * @param id identificador del marcador
 */
function cancelar_asignacion(id){
	// borro la posicion actual y lo dibujo en la antigua
	marcadores_definitivos[id].marker.setMap(null);
	marcadores_definitivos[id].marker = generaMarcador(marcadores_definitivos[id], DEFINITIVO);
	noActualizar = 0;
}

/**
 * Obtiene la hora.
 * 
 * @param sinc true si sincroniza la hora con el servidor
 * @return hora
 */
function obtiene_fecha(sinc) {
	// La hora se obtiene en local
	var fecha_actual = new Date(new Date().getTime() - desfase);
	
	if(sinc){
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