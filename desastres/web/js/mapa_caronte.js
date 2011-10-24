function mapInit(){
	var center = new GLatLng(38.232272, -1.698925); // Calasparra, Murcia (geriatrico)
	map.setCenter(center, 21);
}

function buildingInit(){
	showHospitals();
	showFiremenStations();
	showPoliceStations();
	showGeriatricCenters();
}

function showHospitals(){
	generateBuilding('hospital','Centro de salud', 38.228138, -1.706449); // Calasparra, Murcia
}

function showFiremenStations(){
	generateBuilding('firemenStation','Parque de bomberos', 38.111020,-1.865018); // Caravaca de la Cruz, Murcia
	generateBuilding('firemenStation','Parque de bomberos TEMPORAL', 38.21602,-1.72306); // TEMPORAL
}

function showPoliceStations(){
	generateBuilding('policeStation','Ayuntamiento y Polic&iacute;a municipal', 38.231125, -1.697560); // Calasparra, Murcia
}

function showGeriatricCenters(){
	generateBuilding('geriatricCenter','Residencia Virgen de la Esperanza', 38.232272, -1.698925); // Calasparra, Murcia
}

function initialize2(){
	if(nivelMsg > 1){
		if(document.getElementById('opcionesMapa').verSanos.checked){
			verSanos = true;
		}
	}
}

function cargarMenuAcciones(puntero){
	var menu = '';
	// igual que $.getJSON(url,data,success) pero forzamos async=false para que cargue bien el pop-up
	$.ajax({
		url: 'getpost/getActividades.jsp',
		type: 'GET',
		dataType: 'json',
		data: {'marcador':puntero.marcador, 'id':puntero.id},
		success: function(data){
			$.each(data, function(entryIndex, entry){
				if(entryIndex == 0){
					menu += '<br/><table class="tabla_menu">';
				}
				menu += '<tr><td><b>' + entry['nombre_usuario'] + '</b> realiza la accion <b>' + entry['tipo'] + '</b></td></tr>';
				if(entryIndex == data.length-1){
					menu += '</table>';
				}
			});
		},
		async: false
	});
	$.ajax({
		url: 'getpost/getActividades.jsp',
		type: 'GET',
		dataType: 'json',
		data: {'marcador':puntero.marcador, 'tipo':puntero.tipo, 'estado':puntero.estado},
		success: function(data){
			$.each(data, function(entryIndex, entry){
				if(entryIndex == 0){
					menu += '<br/><table class="tabla_menu">' +
						'<tr><th><label for="accion">Acciones a realizar</label></th></tr>' +
						'<tr class="oculto"><td><input type="radio" name="accion" value="" checked="checked"/></td></tr>'; // Sin esto no funciona!!
				}
				menu += '<tr id="' + entry['tipo'] + '"><td>' +
					'<input type="radio" name="accion" value="' + entry['tipo'] + '"/>' + entry['descripcion'] +
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
		url: 'getpost/getActividades.jsp',
		type: 'GET',
		dataType: 'json',
		data: {'marcador':evento.marcador, 'id':evento.id},
		success: function(data) {
			$.each(data, function(entryIndex, entry) {
				if(entryIndex == 0){
					menu += '</br><table class="tabla_menu">';
				}
				menu += '<tr><td>Accion <b>' + entry['tipo'] + '</b> realizada sobre <b>' + entry['nombre'] + '</b></td>';
				if(evento.nombre == userName){
					menu += '<td><input type="button" value="Detener" onclick="detener(' + evento.id + ',' + entry['id_emergencia'] + ',\'' + evento.nombre + '\');map.closeInfoWindow()"></td>';
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
		document.getElementById('eliminar1').style.display = 'inline';
	}else if(evento.marcador == 'people'){
		lateral = document.getElementById('heridos');
		document.getElementById('submit20').style.display = 'inline';
		document.getElementById('eliminar2').style.display = 'inline';
		var tipo;
		if(evento.tipo == 'healthy'){
			tipo = 'sano';
		}else if(evento.tipo == 'slight'){
			tipo = 'leve';
		}else if(evento.tipo == 'serious'){
			tipo = 'grave';
		}else if(evento.tipo == 'dead'){
			tipo = 'muerto';
		}else if(evento.tipo == 'trapped'){
			tipo = 'trapped';
		}
		document.getElementById('icono_heridos').src = 'markers/' + tipo + '1.png';
		//document.getElementById('sintomas').style.display = 'block';

		document.getElementById('checkboxAsoc').innerHTML = '';
		emergenciasAsociadas = [new Array(), new Array()];
		//document.getElementById('listaSintomas').innerHTML = '';
		//sintomas = [new Array(), new Array()];
		$.ajax({
			url: 'getpost/getAsociaciones.jsp',
			type: 'GET',
			dataType: 'json',
			data: {'tipo':'asociadas', 'iden': evento.id},
			success: function(data){
				$.each(data, function(entryIndex, entry){
					document.getElementById('checkboxAsoc').innerHTML += '<li><input type="checkbox" name="assigned' + entry['id'] +
						'" onclick="asociarEmergencia(' + entry['id'] + ', assigned' + entry['id'] + '.checked)" checked="checked"/>' +
						entry['id'] +' - ' + entry['nombre'] + '</li>';
					emergenciasAsociadas[0].push([entry['id'], true]);
					emergenciasAsociadas[1].push([entry['id'], true]);
				});
			},
			async: false
		});
		$.ajax({
			url: 'getpost/getAsociaciones.jsp',
			type: 'GET',
			dataType: 'json',
			data: {'tipo':'emergencias', 'iden': evento.id},
			success: function(data){
				$.each(data, function(entryIndex, entry){
					document.getElementById('checkboxAsoc').innerHTML += '<li><input type="checkbox" name="assigned' + entry['id'] +
							'" onclick="asociarEmergencia(' + entry['id'] + ', assigned' + entry['id'] + '.checked)"/>' +
							entry['id'] +' - ' + entry['nombre'] + '</li>';
					emergenciasAsociadas[0].push([entry['id'], false]);
					emergenciasAsociadas[1].push([entry['id'], false]);
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
			url: 'getpost/getSintomas.jsp',
			type: 'GET',
			dataType: 'json',
			data: {'tipo':'sintomasSI', 'iden': evento.id},
			success: function(data){
				$.each(data, function(entryIndex, entry){
					document.getElementById('listaSintomas').innerHTML += '<li><input type="checkbox" name="' + entry['tipo'] +
							'" onclick="annadirSintoma(\'' + entry['tipo'] + '\', ' + entry['tipo'] + '.checked)" checked="checked"/>' +
							entry['descripcion'] + '</li>';
					sintomas[0].push([entry['tipo'], true]);
					sintomas[1].push([entry['tipo'], true]);
				});
			},
			async: false
		});
		$.ajax({
			url: 'getpost/getSintomas.jsp',
			type: 'GET',
			dataType: 'json',
			data: {'tipo':'sintomasNO', 'iden': evento.id},
			success: function(data){
				$.each(data, function(entryIndex, entry){
					document.getElementById('listaSintomas').innerHTML += '<li><input type="checkbox" name="' + entry['tipo'] +
							'" onclick="annadirSintoma(\'' + entry['tipo'] + '\', ' + entry['tipo'] + '.checked)"/>' +
							entry['descripcion'] + '</li>';
					sintomas[0].push([entry['tipo'], false]);
					sintomas[1].push([entry['tipo'], false]);
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
			centroAux = [map.getCenter(), map.getZoom()];
		}
	}
			
	if(lateral != null){
		for(var i = 0; i < 5; i++){
			if(lateral.tipo[i].value == evento.tipo){
				lateral.tipo[i].checked = 'checked';
				if(evento.marcador == 'event'){
					cambiaIcono('event',evento.tipo);
				}else if(evento.marcador == 'people'){
					cambiaIcono('people',evento.tipo,1);
				}
			}
		}
		lateral.nombre.value = evento.nombre;
		lateral.info.value = evento.info;
		lateral.descripcion.value = evento.descripcion;
		lateral.direccion.value = evento.direccion;
		lateral.iden.value = evento.id;
		for(i = 0; i < 5; i++){
			if(lateral.planta[i].value == evento.planta){
				lateral.planta[i].selected = 'selected';
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
	var lateral;
	if(marcador == 'event'){
		lateral = document.getElementById('catastrofes');
		document.getElementById('submit10').style.display = 'none';
		document.getElementById('eliminar1').style.display = 'none';
	}else if(marcador == 'people'){
		lateral = document.getElementById('heridos');
		document.getElementById('submit20').style.display = 'none';
		document.getElementById('eliminar2').style.display = 'none';
	}else if(marcador == 'resource'){
		noActualizar = 0;
	}
	
	if(lateral != null){
		if(limpiar == true){
			lateral.tipo[0].checked = 'checked';
			if(marcador == 'event'){
				cambiaIcono('event', 'fire', 1);
				lateral.nombre.value = 'Incendio';
			}else if(marcador == 'people'){
				cambiaIcono('people', 'healthy', 1);
				lateral.nombre.value = 'Sano';
			}
			lateral.info.value = '';
			lateral.descripcion.value = '';
			lateral.direccion.value = '';
			lateral.iden.value = '';
			lateral.planta[0].selected = 'selected';
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
					url: 'getpost/getAsociaciones.jsp',
					type: 'GET',
					dataType: 'json',
					data: {'tipo':'todasEmergencias'},
					success: function(data){
						$.each(data, function(entryIndex, entry){
							document.getElementById('checkboxAsoc').innerHTML += '<li><input type="checkbox" name="assigned' + entry['id'] +
								'" onclick="asociarEmergencia(' + entry['id'] + ', assigned' + entry['id'] + '.checked)"/>' +
								entry['id'] +' - ' + entry['nombre'] + '</li>';
							emergenciasAsociadas[0].push([entry['id'], false]);
							emergenciasAsociadas[1].push([entry['id'], false]);
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
					url: 'getpost/getSintomas.jsp',
					type: 'GET',
					dataType: 'json',
					data: {'tipo':'todosSintomas'},
					success: function(data){
						$.each(data, function(entryIndex, entry){
							document.getElementById('listaSintomas').innerHTML += '<li><input type="checkbox" name="' + entry['tipo'] +
									'" onclick="annadirSintoma(\'' + entry['tipo'] + '\', ' + entry['tipo'] + '.checked)"/>' +
									entry['descripcion'] + '</li>';
							sintomas[0].push([entry['tipo'], false]);
							sintomas[1].push([entry['tipo'], false]);
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