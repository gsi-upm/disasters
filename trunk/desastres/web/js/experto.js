var estado_Experto = 'off';
var url_base = '/desastres/Xpert';

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