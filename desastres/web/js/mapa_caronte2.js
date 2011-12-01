function coordenadasUsuario(pos){
	// PRUEBAAA!!! ****************************************************************************************
	var latitud = (38.232272 + (2*Math.random()-1)*0.0001).toFixed(6); // pos.coords.latitude.toFixed(6);
	var longitud = (-1.698925 + (2*Math.random()-1)*0.0001).toFixed(6); // pos.coords.longitude.toFixed(6);
	//*****************************************************************************************************

	if(nivelMsg == null || nivelMsg == 0){
		var icono = new GIcon(G_DEFAULT_ICON);
		icono.image = 'markers/user.png';
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
					'<input id="aceptarAccion" type="button" value="Aceptar" onclick="actuar(' + puntero.id + ',\'' + userName + '\',accion);map.closeInfoWindow();"/>' +
					'</td></tr></table>';
				}
			});
		},
		async: false
	});
	return menu;
}

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
					menu += '<td><input type="button" value="Detener" onclick="detener(' + evento.id + ',' + entry.id_emergencia + ',\'' + evento.nombre + '\');map.closeInfoWindow()"></td>';
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

function limpiarLateral(marcador){
	document.getElementById('prueba').innerHTML = marcador;
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
					async: false
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
					async: false
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

function detener(idEvento,idEmergencia,nombreUsuario){
	$.post('getpost/updateEstado.jsp',{
		'accion':'detener',
		'idEvento':idEvento,
		'idEmergencia':idEmergencia,
		'nombreUsuario':nombreUsuario
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
		document.getElementById('planoResidencia').src = 'markers/residencia/planta' + num + '.jpg';
		document.getElementById('plantaPlano').innerHTML = num;
		residencia.getIcon().image = 'markers/residencia/planta' + num + '.png';
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
				map.setCenter(centroAux.center, centroAux.zoom);
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