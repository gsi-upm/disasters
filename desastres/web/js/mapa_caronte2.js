/**
 * Obtiene las coordenadas del usuario y actualiza su posicion
 * 
 * @param pos Posicion devuelta
 */
function coordenadasUsuario(pos){
	// PRUEBAAA!!! ****************************************************************************************
	var latitud = (38.232272 + (2*Math.random()-1)*0.0001).toFixed(6); // pos.coords.latitude.toFixed(6);
	var longitud = (-1.698925 + (2*Math.random()-1)*0.0001).toFixed(6); // pos.coords.longitude.toFixed(6);
	//*****************************************************************************************************

	if(nivelMsg == null || nivelMsg == 0){
		var icono = new google.maps.MarkerImage('markers/resources/user_no.png');
		var opciones = {
			position: new google.maps.LatLng(latitud, longitud),
			icon: icono
		};
		var marker = new google.maps.Marker(opciones);
		marker.setMap(map);
	}

	if(userName != '' && nivelMsg > 0 && localizacion == true){
		$.post('getpost/updateLatLong.jsp',{
			'nombre':userName,
			'latitud':latitud,
			'longitud':longitud
		});
	}
}

/**
 * Indica el numero de agentes, emergencias y heridos en la residencia
 * 
 * @param planta Planta de la residencia
 */
function numeroMarcadores(planta){
	if(activeTabIndex['dhtmlgoodies_tabView1'] == 2){
		$.getJSON('getpost/info_caronte.jsp',{
			'marcadores':'lateral'
		}, function(data){
			document.getElementById('numEnfermeros').innerHTML = '(' + data.nurse + ')';
			document.getElementById('numGerocultores').innerHTML = '(' + data.gerocultor + ')';
			document.getElementById('numAuxiliares').innerHTML = '(' + data.assistant + ')';
			document.getElementById('numOtros').innerHTML = '(' + data.otherStaff + ')';
			document.getElementById('numPolicias').innerHTML = '(' + data.police + ')';
			document.getElementById('numBomberos').innerHTML = '(' + data.firemen + ')';
			document.getElementById('numAmbulancias').innerHTML = '(' + data.ambulance + ')';
		});
	}
	if(activeTabIndex['dhtmlgoodies_tabView2'] == 1){
		$.getJSON('getpost/info_caronte.jsp',{
			'marcadores':'plano',
			'planta':planta
		}, function(data){
			document.getElementById('numFuegos').innerHTML = '(' + data.fire + ')';
			document.getElementById('numInundaciones').innerHTML = '(' + data.flood + ')';
			document.getElementById('numDerrumbamientos').innerHTML = '(' + data.collapse + ')';
			document.getElementById('numPerdidos').innerHTML = '(' + data.lostPerson + ')';
			document.getElementById('numSanos').innerHTML = '(' + data.healthy + ')';
			document.getElementById('numLeves').innerHTML = '(' + data.slight + ')';
			document.getElementById('numGraves').innerHTML = '(' + data.serious + ')';
			document.getElementById('numMuertos').innerHTML = '(' + data.dead + ')';
			document.getElementById('numAtrapados').innerHTML = '(' + data.trapped + ')';
		});
	}
}

/**
 * Obtiene las acciones que se pueden realizar o se estan llevando a cabo sobre una emergencia o herido y
 * las envia en forma de menu para mostrarlas en un bocadillo
 * 
 * @param puntero Marcador pulsado
 * @return Menu de acciones
 */
function cargarMenuAcciones(puntero){
	var menu = '';
	// igual que $.getJSON(url,data,success) pero forzamos async=false para que cargue bien el pop-up
	$.ajax({
		type: 'GET',
		dataType: 'json',
		url: 'getpost/getActividades.jsp',
		data: {
			'marcador':puntero.marcador,
			'id':puntero.id
		},
		success: function(data){
			$.each(data, function(entryIndex, entry){
				if(entryIndex == 0){
					menu += '<br/><table class="tabla_menu">';
				}
				menu += '<tr><td><b>' + entry.nombre_usuario + '</b> realiza la accion <b>' + entry.tipo + '</b></td></tr>';
				if(entryIndex == data.length-1){
					menu += '</table>';
				}
			});
		},
		async: false
	});
	$.ajax({
		type: 'GET',
		dataType: 'json',
		url: 'getpost/getActividades.jsp',
		data: {
			'marcador':puntero.marcador,
			'tipo':puntero.tipo,
			'estado':puntero.estado
		},
		success: function(data){
			$.each(data, function(entryIndex, entry){
				if(entryIndex == 0){
					menu += '<br/><table class="tabla_menu">' +
				'<tr><th><label for="accion">Acciones a realizar</label></th></tr>' +
				'<tr class="oculto"><td><input type="radio" name="accion" value="" checked="checked"/></td></tr>'; // Sin esto no funciona!!
				}
				menu += '<tr id="' + entry.tipo + '"><td>' +
				'<input type="radio" name="accion" value="' + entry.tipo + '"/>' + entry.descripcion +
				'</td></tr>';
				if(entryIndex == data.length-1){
					menu += '<tr><td>' +
					'<input id="aceptarAccion" type="button" value="Aceptar" onclick="actuar(' + puntero.id + ',\'' + userName + '\',accion); infoWindow.close();"/>' +
					'</td></tr></table>';
				}
			});
		},
		async: false
	});
	return menu;
}

/**
 * Obtiene las actividades que esta realizando un agente y
 * las devuelve en forma de menu para mostrarlas en un bocadillo
 * 
 * @param evento
 * @return Menu de actividades
 */
function cargarListaActividades(evento){
	var menu = '';
	// igual que $.getJSON(url,data,success) pero async=false
	$.ajax({
		type: 'GET',
		dataType: 'json',
		url: 'getpost/getActividades.jsp',
		data: {
			'marcador':evento.marcador,
			'id':evento.id
		},
		success: function(data) {
			$.each(data, function(entryIndex, entry) {
				if(entryIndex == 0){
					menu += '</br><table class="tabla_menu">';
				}
				menu += '<tr><td>Accion <b>' + entry.tipo + '</b> realizada sobre <b>' + entry.nombre + '</b></td>';
				if(evento.nombre == userName){
					menu += '<td><input type="button" value="Detener" onclick="detener(' + evento.id + ',' + entry.id_emergencia + ',\'' + evento.nombre + '\'); infoWindow.close()"></td>';
				}
				menu += '</tr>';
				if(entryIndex == data.length-1){
					menu += '</table>';
				}
			});
		},
		async: false
	});
	return menu;
}

/**
 * Edita los datos de las pestannas de la izquierda y la muestra
 * 
 * @param evento Marcador del que se va a cargar su pestanna
 */
function cargarLateral(evento){
	var lateral;
	if(evento.marcador == 'event'){
		lateral = document.getElementById('catastrofes');
		document.getElementById('submit10').style.display = 'inline';
		if(usuario_actual_tipo != 'citizen'){
			document.getElementById('eliminar1').style.display = 'inline';
		}
		document.getElementById('radio_catastrofes').style.display = 'none';
		document.getElementById('radio_catastrofes_2').style.display = 'block';
		var tipoImg;
		if(evento.tipo == 'fire'){
			tipoImg = 'fuego';
		}else if(evento.tipo == 'flood'){
			tipoImg = 'agua';
		}else if(evento.tipo == 'collapse'){
			tipoImg = 'casa';
		}else if(evento.tipo == 'lostPerson'){
			tipoImg = 'personaPerdida';
		}
		document.getElementById('icono_catastrofes_2').src = 'markers/events/' + tipoImg + '.png';
		document.getElementById('tipo_catastrofes_2').innerHTML = fmt(evento.tipo);
	}else if(evento.marcador == 'people'){
		lateral = document.getElementById('heridos');
		document.getElementById('submit20').style.display = 'inline';
		if(usuario_actual_tipo != 'citizen'){
			document.getElementById('eliminar2').style.display = 'inline';
		}
		//document.getElementById('sintomas').style.display = 'block';

		document.getElementById('checkboxAsoc').innerHTML = '';
		emergenciasAsociadas = [new Array(), new Array()];
		//document.getElementById('listaSintomas').innerHTML = '';
		//sintomas = [new Array(), new Array()];
		$.ajax({
			type: 'GET',
			dataType: 'json',
			url: 'getpost/getAsociaciones.jsp',
			data: {
				'tipo':'asociadas',
				'iden': evento.id,
				'nivel':nivelMsg
			},
			success: function(data){
				$.each(data, function(entryIndex, entry){
					document.getElementById('checkboxAsoc').innerHTML += '<li><input type="checkbox" name="assigned' + entry.id +
						'" onclick="asociarEmergencia(' + entry.id + ', assigned' + entry.id + '.checked)" checked="checked"/>' +
						entry.id + ' - ' + entry.nombre + '</li>';
					emergenciasAsociadas[0].push([entry.id, true]);
					emergenciasAsociadas[1].push([entry.id, true]);
				});
			},
			async: false
		});
		$.ajax({
			type: 'GET',
			dataType: 'json',
			url: 'getpost/getAsociaciones.jsp',
			data: {
				'tipo':'emergencias',
				'iden': evento.id,
				'nivel':nivelMsg
			},
			success: function(data){
				$.each(data, function(entryIndex, entry){
					document.getElementById('checkboxAsoc').innerHTML += '<li><input type="checkbox" name="assigned' + entry.id +
						'" onclick="asociarEmergencia(' + entry.id + ', assigned' + entry.id + '.checked)"/>' +
						entry.id + ' - ' + entry.nombre + '</li>';
					emergenciasAsociadas[0].push([entry.id, false]);
					emergenciasAsociadas[1].push([entry.id, false]);
				});
				if(document.getElementById('checkboxAsoc').innerHTML == ''){
					document.getElementById('textoAsoc').innerHTML = '&thinsp;No hay emergencias para asociar';
				}else{
					document.getElementById('textoAsoc').innerHTML = '&emsp;V';
				}
			},
			async: false
		});
		/*$.ajax({
			type: 'GET',
			dataType: 'json',
			url: 'getpost/getSintomas.jsp',
			data: {
				'tipo':'sintomasSI',
				'iden': evento.id
			},
			success: function(data){
				$.each(data, function(entryIndex, entry){
					document.getElementById('listaSintomas').innerHTML += '<li><input type="checkbox" name="' + entry.tipo +
						'" onclick="annadirSintoma(\'' + entry.tipo + '\', ' + entry.tipo + '.checked)" checked="checked"/>' +
						entry.descripcion + '</li>';
					sintomas[0].push([entry.tipo, true]);
					sintomas[1].push([entry.tipo, true]);
				});
			},
			async: false
		});
		$.ajax({
			type: 'GET',
			dataType: 'json',
			url: 'getpost/getSintomas.jsp',
			data: {
				'tipo':'sintomasNO',
				'iden': evento.id
			},
			success: function(data){
				$.each(data, function(entryIndex, entry){
					document.getElementById('listaSintomas').innerHTML += '<li><input type="checkbox" name="' + entry.tipo +
						'" onclick="annadirSintoma(\'' + entry.tipo + '\', ' + entry.tipo + '.checked)"/>' +
						entry.descripcion + '</li>';
					sintomas[0].push([entry.tipo, false]);
					sintomas[1].push([entry.tipo, false]);
				});
			},
			async: false
		});*/
	}else if(evento.marcador == 'resource'){
		noActualizar = evento.id;
		document.getElementById('listaRecursos').style.display = 'none';
		document.getElementById('datos').style.display = 'block';
		document.getElementById('datos-usuario').innerHTML = evento.nombre;
		document.getElementById('datos-nombre').innerHTML = evento.descripcion;
		document.getElementById('datos-correo').innerHTML = evento.info;
		if(evento.nombre == userName){
			document.getElementById('form-posicion').style.display = 'block';
			document.getElementById('form-posicion').localizacion.checked = localizacion;
			document.getElementById('form-posicion').latitud.value = evento.latitud;
			document.getElementById('form-posicion').longitud.value = evento.longitud;
			document.getElementById('form-posicion').direccion.value = '';
			centroAux = {
				'center':map.getCenter(),
				'zoom':map.getZoom()
			};
		}
	}
			
	if(lateral != null){
		for(var i = 0; i < 5; i++){
			if(lateral.tipo[i].value == evento.tipo){
				lateral.tipo[i].checked = 'checked';
				if(evento.marcador == 'event'){
					cambiaIcono('event',evento.tipo);
				}else if(evento.marcador == 'people'){
					cambiaIcono('people',evento.tipo,evento.cantidad);
				}
			}
		}
		lateral.nombre.value = evento.nombre;
		lateral.info.value = evento.info;
		lateral.descripcion.value = evento.descripcion;
		lateral.direccion.value = evento.direccion;
		lateral.iden.value = evento.id;
		if(usuario_actual_tipo != 'citizen'){
			for(i = 0; i < 5; i++){
				if(lateral.planta[i].value == evento.planta){
					lateral.planta[i].selected = 'selected';
				}
			}
		}
		if(evento.marcador == 'event'){
			for(i = 0; i < 4; i++){
				if(lateral.tamanno[i].value == evento.size){
					lateral.tamanno[i].selected = 'selected';
				}
			}
			for(i = 0; i < 3; i++){
				if(lateral.trafico[i].value == evento.traffic){
					lateral.trafico[i].selected = 'selected';
				}
			}
		}else if(evento.marcador == 'people'){
			lateral.cantidad.value = evento.cantidad;
			for(i = 0; i < 4; i++){
				if(lateral.peso[i].value == evento.size){
					lateral.peso[i].selected = 'selected';
				}
			}
			for(i = 0; i < 5; i++){
				if(lateral.movilidad[i].value == evento.traffic){
					lateral.movilidad[i].selected = 'selected';
				}
			}
		}
	}

	if(evento.marcador == 'event'){
		showTab('dhtmlgoodies_tabView1',0);
	}else if(evento.marcador == 'people'){
		showTab('dhtmlgoodies_tabView1',1);
	}else if(evento.marcador == 'resource'){
		showTab('dhtmlgoodies_tabView1',2);
	}
}

/**
 * Pone los valores por defecto en la pestanna indicada
 * 
 * @param marcador Tipo de marcador del que se va a limpiar su pestanna
 * @param async Indica si la limpieza es asincrona
 */
function limpiarLateral(marcador, async){
	noActualizar = 0;
	var lateral;
	if(marcador == 'event'){
		lateral = document.getElementById('catastrofes');
		document.getElementById('submit10').style.display = 'none';
		if(usuario_actual_tipo != 'citizen'){
			document.getElementById('eliminar1').style.display = 'none';
		}
		document.getElementById('radio_catastrofes').style.display = 'inline';
		document.getElementById('radio_catastrofes_2').style.display = 'none';
	}else if(marcador == 'people'){
		lateral = document.getElementById('heridos');
		document.getElementById('submit20').style.display = 'none';
		if(usuario_actual_tipo != 'citizen'){
			document.getElementById('eliminar2').style.display = 'none';
		}
	}else if(marcador == 'resource'){
		document.getElementById('form-posicion').direccion.innerHTML = '';
		document.getElementById('form-posicion').porDefecto.checked = false;
		document.getElementById('form-posicion').plantaPorDefecto.checked = false;
	}
	
	if(lateral != null){
		if(limpiar == true){
			lateral.tipo[0].checked = 'checked';
			if(marcador == 'event'){
				cambiaIcono('event', 'fire', 1);
				lateral.nombre.value = 'Incendio';
			}else if(marcador == 'people'){
				cambiaIcono('people', 'slight', 1);
				lateral.nombre.value = 'Leve';
			}
			lateral.info.value = '';
			lateral.descripcion.value = '';
			lateral.direccion.value = '';
			lateral.iden.value = '';
			if(usuario_actual_tipo != 'citizen'){
				lateral.planta[0].selected = 'selected';
			}
			if(marcador == 'event'){
				lateral.tamanno[0].selected = 'selected';
				lateral.trafico[0].selected = 'selected';
			}else if(marcador == 'people'){
				lateral.peso[0].selected = 'selected';
				lateral.movilidad[0].selected = 'selected';
				document.getElementById('checkboxAsoc').innerHTML = '';
				emergenciasAsociadas = [new Array(), new Array()];
				//document.getElementById('listaSintomas').innerHTML = '';
				//sintomas = [new Array(), new Array()];
				if(async == null){
					async = false;
				}
				$.ajax({
					type: 'GET',
					dataType: 'json',
					url: 'getpost/getAsociaciones.jsp',
					data: {
						'tipo':'todasEmergencias',
						'nivel':nivelMsg
					},
					success: function(data){
						$.each(data, function(entryIndex, entry){
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
					},
					async: async
				});
				/*$.ajax({
					type: 'GET',
					dataType: 'json',
					url: 'getpost/getSintomas.jsp',
					data: {
						'tipo':'todosSintomas'
					},
					success: function(data){
						$.each(data, function(entryIndex, entry){
							document.getElementById('listaSintomas').innerHTML += '<li><input type="checkbox" name="' + entry.tipo +
								'" onclick="annadirSintoma(\'' + entry.tipo + '\', ' + entry.tipo + '.checked)"/>' +
								entry.descripcion + '</li>';
							sintomas[0].push([entry.tipo, false]);
							sintomas[1].push([entry.tipo, false]);
						});
					},
					async: async
				});*/
			}
		}
	}else{
		if(limpiar == true){
			document.getElementById('datos').style.display = 'none';
			document.getElementById('form-posicion').style.display = 'none';
			document.getElementById('listaRecursos').style.display = 'block';
		}
	}
	limpiar = true;
}

/**
 * 
 * 
 * @param id 
 * @param valor 
 */
function asociarEmergencia(id, valor){
	for(var i = 0; i < emergenciasAsociadas[0].length; i++){
		if(emergenciasAsociadas[0][i][0] == id){
			emergenciasAsociadas[0][i][1] = valor;
		}
	}
}

/**
 * Annade un sintoma a un herido
 * 
 * @param tipo 
 * @param valor 
 */
function annadirSintoma(tipo, valor){
	for(var i = 0; i < sintomas[0].length; i++){
		if(sintomas[0][i][0] == tipo){
			sintomas[0][i][1] = valor;
		}
	}
}

/**
 * Guarda en la base de datos que un agente actua sobre una emergencia o herido
 * 
 * @param idEvento Identificador de la actividad
 * @param nombreUsuario nombre del usuario
 * @param accionAux Accion a realizar
 */
function actuar(idEvento, nombreUsuario, accionAux){
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
			escribirMensaje({'id':idEvento}, 'eliminar', 1);
		}else if(accion=='vuelto' || accion=='trasladado' || accion=='dejar'){
			estadoEvento = 'active';
			estadoUsuario = 'active';
		}
		$.post('getpost/updateEstado.jsp',{
			'accion':accion,
			'idEvento':idEvento,
			'nombreUsuario':nombreUsuario,
			'estadoEvento':estadoEvento,
			'estadoUsuario':estadoUsuario
		});
	}
	var marcador = marcadores_definitivos[idEvento];
	registrarHistorial(userName, marcador.marcador, marcador.tipo, idEvento, accion);
}

/**
 * Detiene la actividad que un agente sete realizando sobre una emergencia
 * 
 * @param idEvento Identificador de la actividad realiada
 * @param idEmergencia Identificador de la emergencia
 * @param nombreUsuario Nombre del usuario
 */
function detener(idEvento, idEmergencia, nombreUsuario){
	$.post('getpost/updateEstado.jsp',{
		'accion':'detener',
		'idEvento':idEvento,
		'idEmergencia':idEmergencia,
		'nombreUsuario':nombreUsuario
	});
}

/**
 * Actualiza la posicion de un marcador
 * 
 * @param id Identificador del marcador
 * @param lat Latitud
 * @param lng Longitud
 */
function guardar_posicion(id, lat, lng){
	var marcador = marcadores_definitivos[id];
	marcador.marker.setPosition(new google.maps.LatLng(lat, lng));
	$.post('getpost/updateLatLong.jsp',{
		'id':id,
		'latitud':lat,
		'longitud':lng
	});
	registrarHistorial(userName, marcador.marcador, marcador.tipo, id, 'mover');
	noActualizar = 0;
}

/**
 * Cambia la planta de la residencia en el mapa
 * 
 * @paran num Numero de planta
 */
function cambiarPlanta(num){
	residencia.setMap(null);
	if(infoWindow != null){
		infoWindow.close();
		if(infoWinMarker != 'building'){
			limpiarLateral(infoWinMarker);
		}
	}
	document.getElementById('planta' + plantaResidencia).style.fontWeight = 'normal';
	document.getElementById('planta' + plantaResidencia).style.textDecoration = 'none';

	plantaResidencia = num;

	document.getElementById('planta' + num).style.fontWeight = 'bold';
	document.getElementById('planta' + num).style.textDecoration = 'underline';
	if(num >= 0){
		document.getElementById('planoResidencia').src = 'markers/residencia/planta' + num + '.jpg';
		document.getElementById('plantaPlano').innerHTML = num;
		var url = 'markers/residencia/planta' + num + '.png';
		var limites = residencia.getBounds();
		residencia = new google.maps.GroundOverlay(url, limites, {clickable: false});
		if(map.getZoom() >= 19 && map.getMapTypeId() == roadmap){
			residencia.setMap(map);
		}
	}

	document.getElementById('prueba').innerHTML = '';
	for(var i in marcadores_definitivos){
		document.getElementById('prueba').innerHTML += i + ' ';
	}
	for(i in marcadores_definitivos){
		marcadores_definitivos[i].marker.setMap(null);
		if((num == -2 || marcadores_definitivos[i].planta == num || marcadores_definitivos[i].planta == -2) &&
				(marcadores_definitivos[i].tipo != 'healthy' || verSanos == true)){
			marcadores_definitivos[i].marker.setMap(map);
		}
	}

	numeroMarcadores(num);
}

/**
 * Muestra los marcadores asociados a personas sanas
 * 
 * @param mostrar True para mostrar, false para ocultar
 */
function mostrarSanos(mostrar){
	verSanos = mostrar;
	for(var i in marcadores_definitivos){
		if(marcadores_definitivos[i].tipo == 'healthy'){
			if(mostrar == true && (marcadores_definitivos[i].planta == plantaResidencia || marcadores_definitivos[i].planta == -2 || plantaResidencia == -2)){
				marcadores_definitivos[i].marker.setMap(map);
			}else{
				marcadores_definitivos[i].marker.setMap(null);
			}
		}
	}
}

/**
 * Modifica la opcion de ser localizado o no
 * 
 * @param valor Indica si el usuario quiere ser localizado
 */
function cambiarGeolocalizacion(valor){
	localizacion = valor;
	$.post('getpost/updateLatLong.jsp',{
		'nombre':userName,
		'localizacion':valor
	});
}

/**
 * Encuentra la posicion de una direccion o de su latitud y longitud
 * 
 * @param lat Latitud
 * @param lng Longitud
 * @param dir Direccion
 */
function findPos(lat, lng, dir){
	limpiar = false;
	if(puntoAux != null){
		puntoAux.setMap(null);
	}
	if(dir == ''){
		localizar(new google.maps.LatLng(lat,lng));
	}else{
		localizador.geocode({address:dir}, function(respuesta){
			localizar(respuesta[0].geometry.location);
		});
	}
}

/**
 * Muestra en el mapa el punto que se quiere localizar
 * 
 * @param punto Coordenadas del punto a localizar
 */
function localizar(punto){
	localizador.geocode({location:punto}, function(respuesta, estado){
		var latlng = respuesta[0].geometry.location;
		if(estado == google.maps.GeocoderStatus.OK){
			document.getElementById('form-posicion').direccion.value = respuesta[0].formatted_address;
			document.getElementById('form-posicion').latitud.value = latlng.lat();
			document.getElementById('form-posicion').longitud.value = latlng.lng();
			var coorAux = new google.maps.LatLng(latlng.lat(),latlng.lng());
			puntoAux = new google.maps.Marker({position:coorAux});
			map.setCenter(coorAux, 14);
			puntoAux.setMap(map);
			var event = google.maps.event.addListener(map, 'click', function(){
				puntoAux.setMap(null);
				puntoAux = null;
				map.setCenter(centroAux.center, centroAux.zoom);
				limpiarLateral('resource');
				google.maps.event.removeListener(event);
			});
		}
	});
}

/**
 * Guarda la nueva posicion del usuario
 * 
 * @param lat Latitud
 * @param lng Longitud
 * @param porDefecto Indica si se quiere guardar esa posicion como la de por defecto del usuario
 */
function newPos(lat, lng, porDefecto){
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
	infoWindow.close();
}

/**
 * Guarda la nueva planta donde esta situado el agente
 * 
 * @param planta Planta
 * @param porDefecto Indica si la planta sera la de por defecto del agente
 */
function editPlanta(planta, porDefecto){
	$.post('getpost/updateLatLong.jsp',{
		'nombre':userName,
		'planta':planta,
		'plantaPorDefecto':porDefecto
	});
	infoWindow.close();
}