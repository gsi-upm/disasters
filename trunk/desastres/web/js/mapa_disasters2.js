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
	if(selected == true){
		showBuilding(type);
	}else{
		hideBuilding(type);
	}
}

function verMas(id){
	var evento = marcadores_definitivos[id];
	var complete = evento.nombre + '<br/>' + evento.info + '<br/>' +evento.descripcion + '<br/>Direccion: ' + evento.direccion + '<br/>';
	var links2;
	if(nivelMsg > 1){
		links2 = '<a id="modificar" href="#" onclick="cargarModificar(marcadores_definitivos[' + evento.id + '],DEFINITIVO);return false;">Modificar</a>' + ' - ' +
			'<a id="acciones" href="#" onclick="cargarAcciones(marcadores_definitivos[' + evento.id + '])">Acciones</a>' + ' - ' +
			'<a id="eliminar" href="#" onclick="eliminar(marcadores_definitivos[' + evento.id + '],DEFINITIVO);return false;">Eliminar</a>' + ' - ' +
			'<a id="ver_mas2" href="#" onclick="verMenos(' + evento.id + ');return false;">Ver menos</a>';
	}else{
		links2 = '<a id="ver_mas2" href="#" onclick="verMenos(' + evento.id + ');return false;">Ver menos</a>';
	}
	marcadores_definitivos[id].marker.openInfoWindowHtml('<div id="bocadillo">' + complete + '<div id="bocadillo_links">' + links2 + '</div></div>');
}

function verMenos(id){
	var evento = marcadores_definitivos[id];
	var small = evento.nombre + '<br/>' + evento.descripcion ;
	var links1;
	if(nivelMsg > 1){
		links1 = '<a id="modificar" href="#" onclick="cargarModificar(marcadores_definitivos[' + evento.id + '],DEFINITIVO);return false;">Modificar</a>' + ' - ' +
			'<a id="acciones" href="#"onclick="cargarAcciones(marcadores_definitivos[' + evento.id + '])"  >Acciones</a>' + ' - ' +
			'<a id="eliminar" href="#" onclick="eliminar(marcadores_definitivos[' + evento.id + '],DEFINITIVO); return false;" >Eliminar</a>' + ' - ' +
			'<a id="ver_mas1" href="#" onclick="verMas(' + evento.id + ');return false;">Ver m&aacute;s</a>';
	}else{
		links1 = '<a id="ver_mas1" href="#" onclick="verMas(' + evento.id + ');return false;">Ver m&aacute;s</a>';
	}
	marcadores_definitivos[id].marker.openInfoWindowHtml('<div id="bocadillo">' + small + '<div id="bocadillo_links">' + links1 + '</div></div>');
}

function cargarModificar(puntero,caracter){
	// mostrar la ventanita
	// carga el evento a modificar en una variabla accesible por todos
	puntero_temp = puntero;
	caracter_temp = caracter;
	if(puntero.marcador == 'event'){
		document.getElementById('quantity').style.visibility = 'hidden';
		document.getElementById('size-traffic').style.visibility = 'visible';
		document.getElementById('traffic-select').style.visibility = 'visible';
		document.getElementById('control').style.visibility = 'visible';
		document.getElementById('sintomas').style.visibility = 'hidden';
	}else{
		document.getElementById('quantity').style.visibility = 'visible';
		document.getElementById('size-traffic').style.visibility = 'hidden';
		document.getElementById('traffic-select').style.visibility = 'hidden';
		document.getElementById('sintomas').style.visibility = 'visible';
		if(puntero.marcador == 'resource'){
			document.getElementById('control').style.visibility = 'hidden';
			document.getElementById('sintomas').style.visibility = 'hidden';
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
	var latitud1 = marker.getLatLng().lat(); // = marcadores_definitivos[id].marker.lat();
	var longitud1 = marker.getLatLng().lng(); // = marcadores_definitivos[id].marker.lng();

	// 2.hallar la distancia a cada catastrofe de la matriz definitiva
	var diferencia = 999999999999;
	var id_mas_cercano;
	var idDefinitivo;

	for(var i = 0; indices[i] != null; i++){
		idDefinitivo = indices[i];
		// nos quedamos solo con los eventos (catastrofes)
		if(marcadores_definitivos[idDefinitivo].marcador != 'event'){
			continue;
		}
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

	// 3.Devolver el id de la catastrofe mas cercana o hacer el post directamente
	return id_mas_cercano;
}