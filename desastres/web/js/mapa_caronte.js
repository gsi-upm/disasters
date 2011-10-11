function initialize2(){
	if(nivelMsg > 1){
		if(document.getElementById('opcionesMapa').verSanos.checked){
			verSanos = true;
		}
	}
}

function mapInit(){
	center = new GLatLng(38.232272, -1.698925); // Calasparra, Murcia (geriatrico)
	map.setCenter(center, 21);
}

function buildingInit(){
	showHospitals();
	showFiremenStations();
	showPoliceStations();
	showGeriatricCenters();
}

function cargarMenuAcciones(puntero){
	var menu = '';
	// igual que $.getJSON(url,data,success) pero forzamos async=false para que cargue bien el pop-up
	$.ajax({
		url: 'getActividades.jsp',
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
		url: 'getActividades.jsp',
		type: 'GET',
		dataType: 'json',
		data: {'marcador':puntero.marcador, 'tipo':puntero.tipo, 'estado':puntero.estado},
		success: function(data){
			$.each(data, function(entryIndex, entry){
				if(entryIndex == 0){
					menu += '<br/><table class="tabla_menu">' +
						'<tr><th><label for="accion">Acciones a realizar</label></th></tr>' +
						'<tr style="display:none"><td><input type="radio" name="accion" value="" checked="checked"/></td></tr>'; // Sin esto no funciona!!
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
	/*
	var menu = '<div id="acciones"><form id="form_acciones" name="form_acciones" action="#"><table class="tabla_menu">';
	var titulo = '<tr><th><label for="accion">Acciones a realizar</label></th></tr>';
	var oculto = '<tr style="display:none"><td><input type="radio" name="accion" value="" checked="checked"/></td></tr>'; // Sin esto no funciona!!
	var apagar = '<tr id="apagar"><td><input type="radio" name="accion" value="apagar"/>Atender emergencia</td></tr>';
	var atender = '<tr id="atender"><td><input type="radio" name="accion" value="atender"/>Atender herido</td></tr>';
	var evacuar = '<tr id="evacuar"><td><input type="radio" name="accion" value="evacuar"/>Evacuar residentes</td></tr>';
	var rescatar = '<tr id="rescatar"><td><input type="radio" name="accion" value="rescatar"/>Rescatar atrapado</td></tr>';
	var ayudar = '<tr id="ayudar"><td><input type="radio" name="accion" value="ayudar"/>Ayudar (0)</td></tr>';
	var trasladar = '<tr id="trasladar"><td><input type="radio" name="accion" value="trasladar"/>Trasladar herido</td></tr>';
	var evacuado = '<tr id="evacuado"><td><input type="radio" name="accion" value="evacuado"/>Fin evacuación</td></tr>';
	var volver = '<tr id="volver"><td><input type="radio" name="accion" value="volver"/>Volver a la residencia</td></tr>';
	var dejar = '<tr id="dejar"><td><input type="radio" name="accion" value="dejar"/>Dejar de atender</td></tr>';
	var apagado = '<tr id="apagado"><td><input type="radio" name="accion" value="apagado"/>Fuego pagado</td></tr>';
	var curado = '<tr id="curado"><td><input type="radio" name="accion" value="curado"/>Herido curado</td></tr>';
	var vuelto = '<tr id="vuelto"><td><input type="radio" name="accion" value="vuelto"/>Todos de vuelta</td></tr>';
	var rescatado = '<tr id="rescatado"><td><input type="radio" name="accion" value="rescatado"/>Atrapado rescatado</td></tr>';
	var cierre = '</table><br/><input type="hidden" id="iden2" name="iden2" value="' + puntero.id + '"/>';
	var boton = '<input id="aceptarAccion" type="button" value="Aceptar" onclick="actuar(iden2.value,\'' + userName + '\',accion);map.closeInfoWindow();"/>';
	if(puntero.marcador == 'event'){
		menu += titulo + oculto;
		if(puntero.estado == 'active') menu += apagar;
		else if(puntero.estado == 'controlled') menu += ayudar + dejar + apagado;
		menu += cierre + boton;
	}else if(puntero.marcador == 'people'){
		menu += titulo + oculto;
		if(puntero.tipo == 'healthy'){
			if(puntero.estado == 'active') menu += evacuar;
			else if(puntero.estado == 'controlled') menu += ayudar + dejar + evacuado + volver + vuelto;
		}else if(puntero.tipo == 'trapped'){
			if(puntero.estado == 'active') menu += rescatar;
			else if(puntero.estado == 'controlled') menu += ayudar + dejar + rescatado;
		}else{
			if(puntero.estado == 'active') menu += atender;
			else if(puntero.estado == 'controlled') menu += ayudar + dejar + trasladar + curado;
		}
		menu += cierre + boton;
	}else{
		menu += '</table>';
	}
	menu += '</form></div>';
	return menu;
	*/
}

function cargarListaActividades(evento){
	var menu = '';
	// igual que $.getJSON(url,data,success) pero async=false
	$.ajax({
		url: 'getActividades.jsp',
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
		document.getElementById('sintomas').style.display = 'block';

		document.getElementById('checkboxAsoc').innerHTML = '';
		emergenciasAsociadas = [new Array(), new Array()];
		$.getJSON('getAsociaciones.jsp', {
			'tipo':'asociadas',
			'iden': evento.id
		}, function(data) {
			$.each(data, function(entryIndex, entry) {
				document.getElementById('checkboxAsoc').innerHTML += '<li><input type="checkbox" name="assigned' + entry['id'] +
					'" onclick="asociarEmergencia(' + entry['id'] + ', assigned' + entry['id'] + '.checked)" checked="checked"/>' +
					entry['id'] +' - ' + entry['nombre'] + '</li>';
				emergenciasAsociadas[0].push([entry['id'], true]);
				emergenciasAsociadas[1].push([entry['id'], true]);
			});
		});
		$.getJSON('getAsociaciones.jsp', {
			'tipo':'emergencias',
			'iden': evento.id
		}, function(data) {
			$.each(data, function(entryIndex, entry){
				document.getElementById('checkboxAsoc').innerHTML += '<li><input type="checkbox" name="assigned' + entry['id'] +
						'" onclick="asociarEmergencia(' + entry['id'] + ', assigned' + entry['id'] + '.checked)"/>' +
						entry['id'] +' - ' + entry['nombre'] + '</li>';
				emergenciasAsociadas[0].push([entry['id'], false]);
				emergenciasAsociadas[1].push([entry['id'], false]);
			});
			if(document.getElementById('checkboxAsoc').innerHTML == ''){
				document.getElementById('checkboxAsoc').innerHTML = 'No hay emergencias para asociar<input type="hidden" name="idAssigned" value="0"/>';
			}
		});
	}else if(evento.marcador == 'resource'){
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
			centroAux = [map.getCenter(),map.getZoom()];
			localizacion = false;
		}
		// AQUI!!!
		/*$.getJSON('getAsociaciones.jsp', {
			'tipo':'asociadas',
			'iden': evento.id
		}, function(data) {
			$.each(data, function(entryIndex, entry) {
				document.getElementById('checkboxAsoc').innerHTML += '<li><input type="checkbox" name="assigned' + entryIndex +
					'" onclick="asociarEmergencia(' + entry['id'] + ', assigned' + entryIndex + '.checked)" checked="checked"/>' +
					entry['id'] +' - ' + entry['nombre'] + '</li>';
				emergenciasAsociadas[0].push([entry['id'], true]);
				emergenciasAsociadas[1].push([entry['id'], true]);
			});
		});*/
	}
			
	if(lateral != null){
		for(i=0; i<5; i++){
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
		for(i=0; i<5; i++){
			if(lateral.planta[i].value == evento.planta){
				lateral.planta[i].selected = 'selected';
			}
		}
		if(evento.marcador == 'event'){
			for(i=0; i<4; i++){
				if(lateral.tamanno[i].value == evento.size){
					lateral.tamanno[i].selected = 'selected';
				}
			}
			for(i=0; i<3; i++){
				if(lateral.trafico[i].value == evento.traffic){
					lateral.trafico[i].selected = 'selected';
				}
			}
		}else if(evento.marcador == 'people'){
			for(i=0; i<4; i++){
				if(lateral.peso[i].value == evento.size){
					lateral.peso[i].selected = 'selected';
				}
			}
			for(i=0; i<5; i++){
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

function limpiarLateral(evento){
	var lateral;
	if(evento.marcador == 'event'){
		lateral = document.getElementById('catastrofes');
		document.getElementById('submit10').style.display = 'none';
		document.getElementById('eliminar1').style.display = 'none';
	}else if(evento.marcador == 'people'){
		lateral = document.getElementById('heridos');
		document.getElementById('submit20').style.display = 'none';
		document.getElementById('eliminar2').style.display = 'none';
		document.getElementById('sintomas').style.display = 'none';

		document.getElementById('checkboxAsoc').innerHTML = '';
		emergenciasAsociadas = [new Array(), new Array()];
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
				document.getElementById('checkboxAsoc').innerHTML = 'No hay emergencias para asociar<input type="hidden" name="idAssigned" value="0"/>';
			}
		});
	}else if(evento.marcador == 'resource'){
		localizacion = document.getElementById('form-posicion').localizacion.checked;
		document.getElementById('datos').style.display = 'none';
		document.getElementById('listaRecursos').style.display = 'block';
		if(evento.nombre == userName){
			document.getElementById('form-posicion').style.display = 'none';
			map.setCenter(centroAux[0], centroAux[1]);
			if(puntoAux != null){
				map.removeOverlay(puntoAux);
				puntoAux = null;
			}
		}
	}

	if(lateral != null){
		if(limpiar){
			lateral.tipo[0].checked = 'checked';
			if(evento.marcador == 'event'){
				cambiaIcono('event', 'fire', 1);
				lateral.nombre.value = 'Incendio';
			}else if(evento.marcador == 'people'){
				cambiaIcono('people', 'healthy', 1);
				lateral.nombre.value = 'Sano';
			}
			lateral.info.value = '';
			lateral.descripcion.value = '';
			lateral.direccion.value = '';
			lateral.iden.value = '';
			lateral.planta[0].selected = 'selected';
			if(evento.marcador == 'event'){
				lateral.tamanno[0].selected = 'selected';
				lateral.trafico[0].selected = 'selected';
			}else if(evento.marcador == 'people'){
				lateral.peso[0].selected = 'selected';
				lateral.movilidad[0].selected = 'selected';
			}
		}
		limpiar = true;
	}
}