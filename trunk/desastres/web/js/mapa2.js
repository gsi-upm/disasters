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
		links1 = '<a id="ver_mas1" href="#" onclick="verMas(' + evento.id + ');return false;"> Ver m&aacute;s </a>';
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