var centroAux = new Array();
var puntoAux;

/**
 * Obtiene las coordenadas del usuario y actualiza su posicion.
 * 
 * @param pos posicion devuelta
 */
function coordenadasUsuario(pos){
	var latitud = pos.coords.latitude.toFixed(6); // (38.523387 + (2*Math.random()-1)*0.0001).toFixed(6);
	var longitud = pos.coords.longitude.toFixed(6); // (-0.170057 + (2*Math.random()-1)*0.0001).toFixed(6); 

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
 * Indica el numero de agentes, emergencias y heridos en la residencia.
 * 
 * @param planta planta de la residencia
 * @param inicio true si es la carga de inicio
 */
function numeroMarcadores(planta, inicio){
	if(inicio == null){
		inicio = false;
	}
	if(inicio){
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
	}else{	
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
}

/**
 * Obtiene las acciones que se pueden realizar o se estan llevando a cabo sobre una emergencia o
 * herido y las envia en forma de menu para mostrarlas en un bocadillo.
 * 
 * @param puntero marcador pulsado
 * @return menu de acciones
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
					'<input id="aceptarAccion" type="button" value="Aceptar" onclick="actuar(' + puntero.id + ',\''+ puntero.tipo +'\',\''+ puntero.info 
                                             +'\',\''+ puntero.descripcion +'\',\''+ puntero.nombre +'\',\''+ puntero.longitud +'\',\''+ puntero.planta 
                                             +'\',\''+ puntero.latitud +'\',\'' + userName + '\',accion); infoWindow.close();"/>' +
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
 * las devuelve en forma de menu para mostrarlas en un bocadillo.
 * 
 * @param evento evento
 * @return menu de actividades
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
 * Edita los datos de las pestannas de la izquierda y la muestra.
 * 
 * @param evento marcador del que se va a cargar su pestanna
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
		emergenciasAsociadas = new Array();
		//document.getElementById('listaSintomas').innerHTML = '';
		//sintomas = new Array();
		$.getJSON('getpost/getAsociaciones.jsp', {
			'tipo':'asociadas',
			'iden': evento.id,
			'nivel':nivelMsg
		}, function(data){
			$.each(data, function(entryIndex, entry){
				var checked = (entry.valor == 'true') ? ' checked="checked"' : '';
				document.getElementById('checkboxAsoc').innerHTML += '<li><input type="checkbox" name="assigned' + entry.id +
					'" onclick="asociarEmergencia(' + entry.id + ', assigned' + entry.id + '.checked)"' + checked + '/>' +
					entry.id + ' - ' + entry.nombre + '</li>';
				emergenciasAsociadas.push({'id':entry.id, 'valor':entry.valor, 'valorBD':entry.valor});
			});
			if(document.getElementById('checkboxAsoc').innerHTML == ''){
				document.getElementById('textoAsoc').innerHTML = '&thinsp;No hay emergencias para asociar';
			}else{
				document.getElementById('textoAsoc').innerHTML = '&emsp;V';
			}
		});
		/*$.getJSON('getpost/getSintomas.jsp', {
			'tipo':'sintomasSI',
			'iden': evento.id
		} function(data){
			var checked = (entry.valor == 'true') ? ' checked="checked"' : '';
			$.each(data, function(entryIndex, entry){
				document.getElementById('listaSintomas').innerHTML += '<li><input type="checkbox" name="' + entry.tipo +
					'" onclick="annadirSintoma(\'' + entry.tipo + '\', ' + entry.tipo + '.checked)"' + checked + '/>' +
					entry.descripcion + '</li>';
				sintomas.push({'tipo':entry.tipo, 'valor':entry.valor, 'valorBD':entry.valor});
			});
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
		lateral.nombre.value = String(evento.nombre);
		lateral.info.value = String(evento.info);
		lateral.descripcion.value = String(evento.descripcion);
		lateral.direccion.value = String(evento.direccion);
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
 * Pone los valores por defecto en la pestanna indicada.
 * 
 * @param marcador yipo de marcador del que se va a limpiar su pestanna
 * @param async indica si la limpieza es asincrona
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
				//lateral.planta[0].selected = 'selected';
                                //No lo borramos para que coincida con el ultimo plano mostrado.
			}
			if(marcador == 'event'){
				lateral.tamanno[0].selected = 'selected';
				lateral.trafico[0].selected = 'selected';
			}else if(marcador == 'people'){
				lateral.peso[0].selected = 'selected';
				lateral.movilidad[0].selected = 'selected';
				document.getElementById('checkboxAsoc').innerHTML = '';
				emergenciasAsociadas = new Array();
				//document.getElementById('listaSintomas').innerHTML = '';
				//sintomas = new Array();
				if(async == null){
					async = false;
				}
				$.getJSON('getpost/getAsociaciones.jsp', {
						'tipo':'emergencias',
						'nivel':nivelMsg
					}, function(data){
						$.each(data, function(entryIndex, entry){
							document.getElementById('checkboxAsoc').innerHTML += '<li><input type="checkbox" name="assigned' + entry.id +
								'" onclick="asociarEmergencia(' + entry.id + ', assigned' + entry.id + '.checked)"/>' +
								entry.id +' - ' + entry.nombre + '</li>';
							emergenciasAsociadas.push({'id':entry.id, 'valor':false, 'valorBD':false});
						});
						if(document.getElementById('checkboxAsoc').innerHTML == ''){
							document.getElementById('textoAsoc').innerHTML = '&thinsp;No hay emergencias para asociar';
						}else{
							document.getElementById('textoAsoc').innerHTML = '&emsp;V';
						}
				});
				/*$.getJSON('getpost/getSintomas.jsp', {
					'tipo':'sintomas'
				}, function(data){
					$.each(data, function(entryIndex, entry){
						document.getElementById('listaSintomas').innerHTML += '<li><input type="checkbox" name="' + entry.tipo +
							'" onclick="annadirSintoma(\'' + entry.tipo + '\', ' + entry.tipo + '.checked)"/>' +
							entry.descripcion + '</li>';
						sintomas.push({'tipo':entry.tipo, 'valor':false, 'valorBD':false});
					});
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
 * Asocia una emergencia.
 * 
 * @param id identificador
 * @param valor valor
 */
function asociarEmergencia(id, valor){
	for(var i = 0; i < emergenciasAsociadas.length; i++){
		if(emergenciasAsociadas[i].id == id){
			emergenciasAsociadas[i].valor = valor;
		}
	}
}

/**
 * Annade un sintoma a un herido.
 * 
 * @param tipo tipo
 * @param valor valor
 */
function annadirSintoma(tipo, valor){
	for(var i = 0; i < sintomas.length; i++){
		if(sintomas[i].tipo == tipo){
			sintomas[i].valor = valor;
		}
	}
}

/**
 * Guarda en la base de datos que un agente actua sobre una emergencia o herido.
 * 
 * @param idEvento identificador de la actividad
 * @param tipo Tipo actividad
 * @param info
 * @param descripcion
 * @param nombre
 * @param longitud
 * @param planta
 * @param latitud
 * @param nombreUsuario nombre del usuario
 * @param accionAux accion a realizar
 */
function actuar(idEvento, tipo, info, descripcion, nombre, longitud, planta, latitud, nombreUsuario, accionAux){
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
                        
                        if(accion == 'apagado'){
                            
                            var type = 'produce-modify'; //Informamos a OCP de la accion eliminar
                            var descTotal = info+" "+descripcion;
                            
                            $.ajax({
                                    url:'/caronte/ProcessEvent',
                                    type: 'POST',
                                    data: {
                                        'type':type,
                                        'event':tipo,
                                        'size':'-1',
                                        'description':descTotal,
                                        'name':nombre,
                                        'longitude':longitud,
                                        'floor':planta,
                                        'latitude':latitud   
                                    },
                                    success: function(data,status) { 
                                        console.log("Success!!");
                                        console.log("Datos devueltos a actuar() en mapa_caronte2.js por ProcessEvent: "+data);
                                        console.log("Mensaje de response code de HTTP enviado por ProcessEvent: "+status);
                                    },
                                    error: function(xhr, desc, err) {
                                    console.log(xhr);
                                    console.log("Desc: " + desc + "\nErr:" + err);
                                    }
                                });
                        }
                        
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
 * Detiene la actividad que un agente sete realizando sobre una emergencia.
 * 
 * @param idEvento identificador de la actividad realiada
 * @param idEmergencia identificador de la emergencia
 * @param nombreUsuario nombre del usuario
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
 * Actualiza la posicion de un marcador.
 * 
 * @param id identificador del marcador
 * @param lat latitud
 * @param lng longitud
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
        
        /** Hacer POST de las variables de la ontología hacia el servlet para enviar a OCP. En este caso modificar un FireEvent */
        if(marcador.marcador == 'event' && marcador.tipo == 'fire' && marcador.estado == 'active'){
            
            var type = 'produce-modify';
            var descTotal = marcador.info+" "+marcador.descripcion;  // La descripcion son ambos campos
            
            $.ajax({
                    url:'/caronte/ProcessEvent',
                    type: 'POST',
                    data: {
                        'type':type,
                        'event':marcador.tipo,
                        'size':marcador.size,
                        'description':descTotal,
                        'name':marcador.nombre,
                        'longitude':lng, //Esta es la que cambia
                        'floor':marcador.planta,
                        'latitude':lat, //Esta es la que cambia  
                        'date':marcador.fecha
                    },
                    success: function(data,status) { 
                        console.log("Success!!");
                        console.log("Datos devueltos por ProcessEvent a guardar_posicion en mapa_caronte2.js: "+data);
                        console.log("Mensaje de response code de HTTP enviado por ProcessEvent: "+status);
                    },
                    error: function(xhr, desc, err) {
                    console.log(xhr);
                    console.log("Desc: " + desc + "\nErr:" + err);
                    }
                });
        }
}

/**
 * Cambia la planta de la residencia en el mapa.
 * 
 * @paran num numero de planta
 */
function cambiarPlanta(num){
	residencia.setMap(null);
	if(infoWindow != null){
		infoWindow.close();
		infoWindow = null;
		if(infoWinMarker != 'building'){
			limpiarLateral(infoWinMarker);
		}
		infoWinMarker = null;
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
		if(map.getZoom() >= zoomMax && map.getMapTypeId() == ROADMAP){
			residencia.setMap(map);
		}
	}

	for(var i in marcadores_definitivos){
		marcadores_definitivos[i].marker.setMap(null);
		if((num == -2 || marcadores_definitivos[i].planta == num || marcadores_definitivos[i].planta == -2) &&
				(marcadores_definitivos[i].tipo != 'healthy' || verSanos == true)){
			marcadores_definitivos[i].marker.setMap(map);
		}
	}

	numeroMarcadores(num);
        selectFloorInJSP(num);
}

/**
 * Muestra los marcadores asociados a personas sanas.
 * 
 * @param mostrar true para mostrar, false para ocultar
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
 * Modifica la opcion de ser localizado o no.
 * 
 * @param valor indica si el usuario quiere ser localizado
 */
function cambiarGeolocalizacion(valor){
	localizacion = valor;
	$.post('getpost/updateLatLong.jsp',{
		'nombre':userName,
		'localizacion':valor
	});
}

/**
 * Encuentra la posicion de una direccion o de su latitud y longitud.
 * 
 * @param lat latitud
 * @param lng longitud
 * @param dir direccion
 */
function findPos(lat, lng, dir){
	limpiar = false;
	if(puntoAux != null){
		puntoAux.setMap(null);
	}
	if(dir == ''){
		localizar(new google.maps.LatLng(lat, lng));
	}else{
		localizador.geocode({address:dir}, function(respuesta, estado){
			if(estado == google.maps.GeocoderStatus.OK){
				localizar(respuesta[0].geometry.location);
			}
		});
	}
}

/**
 * Muestra en el mapa el punto que se quiere localizar.
 * 
 * @param punto coordenadas del punto a localizar
 */
function localizar(punto){
	localizador.geocode({location:punto}, function(respuesta, estado){
		var latlng = respuesta[0].geometry.location;
		if(estado == google.maps.GeocoderStatus.OK){
			document.getElementById('form-posicion').direccion.value = respuesta[0].formatted_address;
			document.getElementById('form-posicion').latitud.value = latlng.lat();
			document.getElementById('form-posicion').longitud.value = latlng.lng();
			var coorAux = new google.maps.LatLng(latlng.lat(), latlng.lng());
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
 * Guarda la nueva posicion del usuario.
 * 
 * @param lat latitud
 * @param lng longitud
 * @param porDefecto indica si se quiere guardar esa posicion como la de por defecto del usuario
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
 * Guarda la nueva planta donde esta situado el agente.
 * 
 * @param planta planta
 * @param porDefecto indica si la planta sera la de por defecto del agente
 */
function editPlanta(planta, porDefecto){
	$.post('getpost/updateLatLong.jsp',{
		'nombre':userName,
		'planta':planta,
		'plantaPorDefecto':porDefecto
	});
	infoWindow.close();
}

/**
 * Cambia la planta de la residencia en el mapa y ademas cambia la planta que se 
 * muestra en el dropdown menu al cambiar el mapa.
 * 
 * @paran num Numero de planta
 */
function cambiarPlanta2(num){
	residencia.setMap(null);
	if(infoWindow != null){
		infoWindow.close();
		infoWindow = null;
		if(infoWinMarker != 'building'){
			limpiarLateral(infoWinMarker);
		}
		infoWinMarker = null;
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
		if(map.getZoom() >= zoomMax && map.getMapTypeId() == roadmap){
			residencia.setMap(map);
		}
	}

	for(var i in marcadores_definitivos){
		marcadores_definitivos[i].marker.setMap(null);
		if((num == -2 || marcadores_definitivos[i].planta == num || marcadores_definitivos[i].planta == -2) &&
				(marcadores_definitivos[i].tipo != 'healthy' || verSanos == true)){
			marcadores_definitivos[i].marker.setMap(map);
		}
	}

	numeroMarcadores(num);
}

/**
 * Segun se seleccina en menu_caronte_admin.jsp(y variantes) una planta en el 
 * dropdown menu, se cambia el mapa que se muestra.
 * 
 */
function selectFloorFromJSP(){
    var selectBox = document.getElementById("select-planta1");
    var selectedValue = selectBox.options[selectBox.selectedIndex].value;
    cambiarPlanta2(selectedValue);
}

/**
 * Segun se seleccione la planta en residencia.jsp se cambia la planta mostrada
 * en el dropdown menu de menu_caronte_admin.jsp(y variantes) 
 * 
 * @param number Planta
 */
function selectFloorInJSP(number){
    document.getElementById("select-planta1").selectedIndex = number+1;
}