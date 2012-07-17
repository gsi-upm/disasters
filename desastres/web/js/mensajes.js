$(document).ready(function(){
	$('#accion').hide();
	$('#open_messages').hide();

	$('#close_messages').click(function(){
		$('#messages').slideUp();
		$('#close_messages').slideUp();
		$('#open_messages').slideDown();
		setTimeout('document.getElementById(\'opcionesMapa\').style.marginRight = \'50px\'',400);
		return false;
	});

	$('#open_messages').click(function(){
		$('#messages').slideDown();
		$('#close_messages').slideDown();
		$('#open_messages').slideUp();
		document.getElementById('opcionesMapa').style.marginRight = '0px';
		return false;
	});
});

function mostrarMensajes(){
	var msgs = document.getElementById('messages');
	$.getJSON('getpost/getMensajes.jsp',{
		'action':'firstTime',
		'nivel':nivelMsg
	}, function(data){
		var tamanno = data.length;
		$.each(data, function(entryIndex, entry){
			var fecha = entry.fecha.split(' ')[1].split('.')[0]; // HH:MM:SS
			// var fecha = entry.fecha.split(' ')[1].split(':',2).join(':'); // HH:MM
			var mensaje = entry.mensaje.split('&lt;').join('<').split('&gt;').join('>');//.split('&quot;').join('"').split('&apos;').join('\'').split('&amp;').join('&');
			msgs.innerHTML += '<p>(' + fecha + ') ' + mensaje + '</p>';
		});
		if(tamanno > 0){
			msgs.innerHTML += '<hr/>';
			msgs.scrollTop = msgs.scrollHeight + msgs.offsetHeight;
			document.getElementById('audio').play();
		}
	});
}

function mostrarMensajes2(){
	var msgs = document.getElementById('messages');
	$.getJSON('getpost/getMensajes.jsp',{
		'action':'notFirst',
		'receptor':usuario_actual,
		'nivel':nivelMsg,
		'fecha':ultimamodif
	}, function(data){
		var tamanno = data.length;
		$.each(data, function(entryIndex, entry){
			var mensaje = entry.mensaje.split('&lt;').join('<').split('&gt;').join('>');//.split('&quot;').join('"').split('&apos;').join('\'').split('&amp;').join('&');
			var fecha = entry.fecha.split(' ')[1].split('.')[0]; // HH:MM:SS
			// var fecha = entry.fecha.split(' ')[1].split(':',2).join(':'); // HH:MM
			if(entry.tipo_receptor == '0'){ // Si el mensaje es tipo pregunta (tipo_receptor = 0)
				if(mensaje != 'CANCEL'){
					document.getElementById('pregunta').innerHTML = mensaje;
					msgs.style.height = '365px';
					$('#accion').slideDown();
				}else{
					$('#accion').slideUp();
					msgs.style.height = '508px';
					document.getElementById('pregunta').innerHTML = '';
				}
			}else{
				if(entry.tipo_receptor == '1'){
					msgs.innerHTML += '<p class="importante">(' + fecha + ') ' + mensaje + '</p>';
				}else{
					msgs.innerHTML += '<p>(' + fecha + ') ' + mensaje + '</p>';
				}
			}
		});
		if(tamanno > 0){
			msgs.scrollTop = msgs.scrollHeight + msgs.offsetHeight;
			document.getElementById('audio').play();
		}
	});
}

function escribirMensaje(evento, accion, nivel){
	var mensaje;
	if(evento.tipo == null){
		evento = marcadores_definitivos[evento.id];
	}

	if(evento.tipo == 'slight' || evento.tipo == 'serious'){
		mensaje = 'Herido ' + fmt(evento.tipo,'es').toLowerCase();
	}else if(evento.tipo == 'healthy'){
		mensaje = 'Sanos(' + evento.cantidad + ')';
	}else{
		mensaje = fmt(evento.tipo,'es');
	}

	mensaje += ' (' + evento.id + ')"' + evento.nombre + '"';

	if(evento.planta >= 0){
		mensaje += ' en la planta ' + evento.planta;
	}else if(evento.planta == -1){
		mensaje += ' en el exterior';
	}
	
	if(evento.marcador == 'event'){
		mensaje += ' de tamaño ' + fmt(evento.size,'es');
	}else{
		if(evento.size != 'indefinido' && evento.traffic != 'indefinida'){
			mensaje += ' de peso ' + evento.size + ' y movilidad ' + evento.traffic;
		}else if(evento.size != 'indefinido'){
			mensaje += ' de peso ' + evento.size;
		}else if(evento.traffic != 'indefinida'){
			mensaje += ' de movilidad ' + evento.traffic;
		}

		var primera = true;
		for(var i = 0; i < emergenciasAsociadas[0].length; i++){
			if(emergenciasAsociadas[0][i][1] == true){
				var asociada = marcadores_definitivos[emergenciasAsociadas[0][i][0]];
				if(primera == true){
					primera = false;
					mensaje += '<br/>-Asociado con: ' + asociada.id + '-' + asociada.nombre;
				}else{
					mensaje += ', ' + asociada.id + '-' + asociada.nombre;
				}
			}
		}
	}

	if(accion == 'crear'){
		mensaje += ' creado';
		if(evento.info != ''){
			if(evento.info.length <= 40){
				mensaje += '<br/>-Información: ' + evento.info;
			}else{
				mensaje += '<br/>-Información: ' + evento.info.substring(0,40) + '...';
			}
		}
		if(evento.descripcion != ''){
			if(evento.info.length <= 40){
				mensaje += '<br/>-Descripción: ' + evento.descripcion;
			}else{
				mensaje += '<br/>-Descripción: ' + evento.descripcion.substring(0,40) + '...';
			}
		}
	}else if(accion == 'modificar'){
		mensaje += ' modificado';
		var anterior = marcadores_definitivos[evento.id];
		if(evento.info != '' && evento.info != anterior.info){
			if(evento.info.length <= 40){
				mensaje += '<br/>-Información: ' + evento.info;
			}else{
				mensaje += '<br/>-Información: ' + evento.info.substring(0,40) + '...';
			}
		}
		if(evento.descripcion != '' && evento.descripcion != anterior.descripcion){
			if(evento.info.length <= 40){
				mensaje += '<br/>-Descripción: ' + evento.descripcion;
			}else{
				mensaje += '<br/>-Descripción: ' + evento.descripcion.substring(0,40) + '...';
			}
		}
	}else if(accion == 'eliminar'){
		mensaje += ' eliminado';
	}
	$.post('getpost/escribirMensaje.jsp', {
		'creador':usuario_actual,
		'tipo_receptor':2,
		'receptor':nivel,
		'mensaje':mensaje
	});
}

function registrarHistorial(usuario, marcador, tipo, emergencia, accion){
	var evento;
	if(accion == 'crear'){
		evento = 'Evento creado por el usuario ' + usuario;
	}else if(accion == 'modificar'){
		evento = 'Emergencia ' + emergencia + ' modificada por ' + usuario;
	}else if(accion == 'eliminar'){
		evento = 'Emergencia ' + emergencia + ' eliminada por ' + usuario;
	}else if(accion == 'mover'){
		evento = 'Emergencia ' + emergencia + ' cambiada de sitio por ' + usuario;
	}else{
		evento = 'El usuario ' + usuario + ' ha actuado sobre ' + emergencia + ' - ' + accion;
	}

	$.post('getpost/registrarHistorial.jsp',{
		'accion':accion,
		'usuario':usuario,
		'marcador':marcador,
		'tipo':tipo,
		'emergencia':emergencia,
		'evento':evento
	});
}

function responder(respuesta){
	$('#accion').slideUp();
	document.getElementById('messages').style.height = '508px';
	document.getElementById('pregunta').innerHTML = '';
	
	var mensaje = (respuesta == 1) ? 'OK' : 'NO';
	$.post('getpost/escribirMensaje.jsp', {
		'creador':usuario_actual,
		'tipo_receptor':0,
		'receptor':0,
		'mensaje':mensaje
	});
}