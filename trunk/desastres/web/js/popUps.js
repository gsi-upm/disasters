$().ready(function(){
	$('#dialog1').jqm();
	$('#dialog2').jqm();
	$('#dialog3').jqm();
	$('#modificar').jqm();
});

$(document).ready(function(){
	$('#screen').hide();
	$('#close_screen').hide();
	$('#console').hide();
	$('#visualize').hide();
	$('#showSimOptions').hide();

	$('#minitab3').toggle(
		function(){
			$('#showSimOptions').slideDown();
			$('#hideSimOptions').click(function(){
				$('#showSimOptions').slideUp();
			});
			$('#submit_simulador').mouseup(function(){
				$('#showSimOptions').slideUp();
			})
		},
		function(){
			$('#showSimOptions').slideUp();
		}
	);

	$('#minitab2').toggle(
		function(){
			$('#visualize').slideDown();
			$('#hideVisualize').click(function(){
				$('#visualize').slideUp();
			});
		},
		function(){
			$('#visualize').slideUp();
		}
	);

	$('#minitab1').toggle(
		function(){
			$.get('getpost/info_desastres.jsp', {}, function(data){
				$('#console').html(data);
			});
			$('#console').slideDown();
		},
		function(){
			$('#console').slideUp();
		}
	);

	$('#runSim').click(function(){
		$('#options2').show();
		$('#runSim').val('run');
	});

	$('#pauseSim, #restartSim').click(function(){
		$('#options2').hide();
	});
});