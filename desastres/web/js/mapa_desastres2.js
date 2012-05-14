function hideBuilding(type){
	var matrix;
	if(type == 'hospital'){
		matrix = hospitals;
		hospitals = new Array();
		hospIndex = 0;
	}else if(type == 'policeStation'){
		matrix = policeStations;
		policeStations = new Array();
		policeIndex = 0;
	}else if(type == 'firemenStation'){
		matrix = firemenStations;
		firemenStations = new Array();
		fireIndex = 0;
	}else if(type == 'geriatricCenter'){
		matrix = geriatricCenters;
		geriatricCenters = new Array();
		geriatricIndex = 0;
	}
	for(var i = 0; i < matrix.length; i++){
		map.removeOverlay(matrix[i]);
	}
}

function visualize(selected, type){
	if(selected == true){
		showBuilding(type);
	}else{
		hideBuilding(type);
	}
}

function verMas(id){
	var evento = marcadores_definitivos[id];
	var complete = evento.nombre + '<br/>' + evento.info + '<br/>' +evento.descripcion + '<br/>Direccion: ' + evento.direccion + '<br/>';
	var links;
	if(nivelMsg > 1){
		links = '<span id="modificar" class="pulsable azul" onclick="cargarModificar(marcadores_definitivos[' + evento.id + '], definitivo)">Modificar</span>' + ' - ' +
			'<span id="eliminar" class="pulsable azul" onclick="eliminar(marcadores_definitivos[' + evento.id + '], definitivo)">Eliminar</span>' + ' - ' +
			'<span id="ver_menos" class="pulsable azul" onclick="verMenos(' + evento.id + ')">Ver menos</span>';
	}else{
		links = '<span id="ver_menos" class="pulsable azul" onclick="verMenos(' + evento.id + ')">Ver menos</span>';
	}
	infoWindow.setContent('<div id="bocadillo">' + complete + '<div id="bocadillo_links">' + links + '</div></div>');
}

function verMenos(id){
	var evento = marcadores_definitivos[id];
	var small = evento.nombre + '<br/>' + evento.descripcion;
	var links;
	if(nivelMsg > 1){
		links = '<span id="modificar" class="pulsable azul" onclick="cargarModificar(marcadores_definitivos[' + evento.id + '], definitivo)">Modificar</span>' + ' - ' +
			'<span id="eliminar" class="pulsable azul" onclick="eliminar(marcadores_definitivos[' + evento.id + '], definitivo)">Eliminar</span>' + ' - ' +
			'<span id="ver_mas" class="pulsable azul" onclick="verMas(' + evento.id + ');return false;">Ver m&aacute;s</span>';
	}else{
		links = '<span id="ver_mas" class="pulsable azul" onclick="verMas(' + evento.id + ');return false;">Ver m&aacute;s</span>';
	}
	infoWindow.setContent('<div id="bocadillo">' + small + '<div id="bocadillo_links">' + links + '</div></div>');
}

function cargarModificar(puntero, caracter){
	// mostrar la ventanita
	// carga el evento a modificar en una variabla accesible por todos
	puntero_temp = puntero;
	caracter_temp = caracter;
	if(puntero.marcador == 'event'){
		document.getElementById('quantity').style.visibility = 'hidden';
		document.getElementById('size-traffic').style.visibility = 'visible';
		document.getElementById('traffic-select').style.visibility = 'visible';
		document.getElementById('control').style.visibility = 'visible';
	}else{
		document.getElementById('quantity').style.visibility = 'visible';
		document.getElementById('size-traffic').style.visibility = 'hidden';
		document.getElementById('traffic-select').style.visibility = 'hidden';
		if(puntero.marcador == 'resource'){
			document.getElementById('control').style.visibility = 'hidden';
		}else{
			document.getElementById('control').style.visibility = 'visible';
		}
		if(puntero.idAssigned != 0){
			document.getElementById('asociacion').innerHTML = 'Asociado a ' + marcadores_temporales[puntero.idAssigned].nombre;
		}
	}

	$('#modificar').jqm().jqmShow();
	// rellenamos con los campos recibidos
	document.getElementById('iden').value = puntero.id;
	document.getElementById('item_tipo').innerHTML = puntero.marcador + ' - ' + puntero.tipo;
	document.getElementById('iconoAdecuado').src = iconoAdecuado(puntero.marcador,puntero.tipo,puntero.cantidad);
	document.getElementById('cantidad').value = puntero.cantidad;
	document.getElementById('nombre').value = puntero.nombre;
	document.getElementById('info').value = puntero.info;
	document.getElementById('descripcion').value = puntero.descripcion;
	document.getElementById('direccion0').value = puntero.direccion;
	document.getElementById('latitud0').value = puntero.latitud;
	document.getElementById('longitud0').value = puntero.longitud;
	document.getElementById('estado').value = puntero.estado;
	document.getElementById('size').value = puntero.size;
	document.getElementById('traffic').value = puntero.traffic;
	document.getElementById('idAssigned').value = puntero.idAssigned;
	document.getElementById('validacion0').src = 'images/iconos/no.png';
	// annadir las que faltan....
	document.getElementById('pincha').innerHTML = 'Marcar posici&oacute;n en el mapa';
}

function asociar(id, marker){
	// 1.hallar el punto del marcador pasado
	var latitud1 = marker.getPosition().lat(); // marcadores_definitivos[id].marker.lat();
	var longitud1 = marker.getPosition().lng(); // marcadores_definitivos[id].marker.lng();

	// 2.hallar la distancia a cada catastrofe de la matriz definitiva
	var diferencia = 999999999999;
	var id_mas_cercano;

	for(var i = 0; indices[i] != null; i++){
		var idDefinitivo = indices[i];
		// nos quedamos solo con los eventos (catastrofes)
		if(marcadores_definitivos[idDefinitivo].marcador == 'event'){
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
	if(recurso.tipo == 'police'){nueva_latitud=latitud+0.00005; nueva_longitud=longitud+0.000025;}
	else if(recurso.tipo == 'nurse'){nueva_latitud=latitud+0.00005; nueva_longitud=longitud-0.000025;}
	else if(recurso.tipo == 'gerocultor'){nueva_latitud=latitud+0.00005; nueva_longitud=longitud-0.000025;}
	else if(recurso.tipo == 'assistant'){nueva_latitud=latitud+0.00005; nueva_longitud=longitud-0.000025;}
	else if(recurso.tipo == 'firemen'){nueva_latitud=latitud+0.000025; nueva_longitud=longitud-0.00005;}
	else if(recurso.tipo == 'ambulance' || recurso.tipo == 'ambulancia'){nueva_latitud=latitud+0.000025; nueva_longitud=longitud+0.00005;}
	else if(recurso.tipo == 'trapped'){nueva_latitud=latitud+0.00005; nueva_longitud=longitud-0.0001;}
	else if(recurso.tipo == 'healthy'){nueva_latitud=latitud+0.00005; nueva_longitud=longitud+0.0001;}
	else if(recurso.tipo == 'slight'){nueva_latitud=latitud+0.0001; nueva_longitud=longitud-0.00005;}
	else if(recurso.tipo == 'serious'){nueva_latitud=latitud+0.0001; nueva_longitud=longitud+0.00005;}
	else if(recurso.tipo == 'dead'){nueva_latitud=latitud+0.00005; nueva_longitud=longitud+0.0001;}

	// actualizar las modificaciones con el metodo modificar
	caracter_temp = definitivo;
	puntero_temp = recurso;
	modificar(idRecurso, recurso.cantidad, recurso.nombre, recurso.descripcion, 'Asociado a ' + evento.nombre + '. ' + recurso.info,
		nueva_latitud, nueva_longitud, evento.direccion, recurso.size, recurso.traffic, recurso.estado, idEvento);
}

function limpiarLateral(){}

function escribirMensaje(){}

function registrarHistorial(){}