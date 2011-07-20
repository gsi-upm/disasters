var estado_Experto = 'off';
var url_base = '/desastres/Xpert';

function mostrarMensajes(){
	$('#close_messages').click(function(){
		$('#messages').slideUp();
		$('#close_messages').slideUp();
		$('#open_messages').slideDown();
		return false;
	});

	$('#open_messages').click(function(){
		$('#messages').slideDown();
		$('#close_messages').slideDown();
		$('#open_messages').slideUp();
		return false;
	});

	mostrarMensajes2(-1);
}

function mostrarMensajes2(index){
	var msgs = document.getElementById('messages');
	var id = index;
	$.get('/desastres/messages',{
		'nivel':nivelMsg,
		'index':index
	}, function(data) {
		//$('#messages').html(data);
		var data2 = data.split('<span>');
		if(data2[1] != null){
			id = data2[1].split('</span>',1);
			msgs.innerHTML += data2[0];
			if(id > 0) document.getElementById('audio').play();
		}

		msgs.scrollTop = msgs.scrollHeight + msgs.offsetHeight;
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