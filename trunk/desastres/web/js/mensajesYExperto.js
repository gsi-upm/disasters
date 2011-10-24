var estado_Experto = 'off';
var url_base = '/desastres/Xpert';

function mostrarMensajes(){
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

	var msgs = document.getElementById('messages');
	var id = 0;
	$.getJSON('getpost/getMensajes.jsp',{
		'nivel':nivelMsg,
		'action':'firstTime'
	}, function(data){
		var tamanno = data.length;
		$.each(data, function(entryIndex, entry){
			msgs.innerHTML += '<p>(' + entry['fecha'].split(' ')[1].split('.')[0] + ') ' + entry['mensaje'] + '</p>';
			if(entryIndex == tamanno-1){
				id = entry['id'];
				msgs.innerHTML += '<hr/>';
			}
		});
		if(tamanno > 0){
			msgs.scrollTop = msgs.scrollHeight + msgs.offsetHeight;
			document.getElementById('audio').play();
		}
		setTimeout('mostrarMensajes2(' + id + ')',2000);
	});
}

function mostrarMensajes2(index){
	var msgs = document.getElementById('messages');
	var id = index;
	$.getJSON('getpost/getMensajes.jsp',{
		'nivel':nivelMsg,
		'id':id,
		'action':'notFirst'
	}, function(data){
		var tamanno = data.length;
		id += tamanno;
		$.each(data, function(entryIndex, entry){
			msgs.innerHTML += '<p>(' + entry['fecha'].split(' ')[1].split('.')[0] + ') ' + entry['mensaje'] + '</p>';
		});
		if(tamanno > 0){
			msgs.scrollTop = msgs.scrollHeight + msgs.offsetHeight;
			document.getElementById('audio').play();
		}
		setTimeout('mostrarMensajes2(' + id + ')',2000);
	});
}

function lanzaExperto(){
	$.get(url_base,{
		'action':'start'
	}, function(data) {
		$('#screen').html(data);
	});

	$('#close_screen').click(function(){
		$('#screen').slideUp();
		$('#close_screen').slideUp();
		return false;
	});

	$('#screen').slideDown();
	$('#close_screen').slideDown();

	estado_Experto = 'on';
}

function avisaExperto(){
	if(estado_Experto == 'on'){
		$.get(url_base,{
			'action':'call'
		}, function(data) {
			alert(data);
			$('#screen').append(data);
		});
	}
}

function stopExperto(){
	$.get(url_base,{
		'action':'stop'
	},function(data){
		estado_Experto = 'off';
		$('#screen').append(data);
	});
}