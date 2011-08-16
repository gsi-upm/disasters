$().ready(function() {
	$('#dialog1').jqm();
	$('#dialog2').jqm();
	$('#dialog3').jqm();
	$('#modificar').jqm();
});

$(document).ready(function() {
	$('#screen').hide();
	$('#close_screen').hide();
	$('#open_messages').hide();
	$('#console').hide();
	$('#visualize').hide();
	$('#showSimOptions').hide();

	$('#minitab3').toggle(
		function() {
			$('#showSimOptions').slideDown();
			$('#hideSimOptions').click(function(){
				$('#showSimOptions').slideUp();
				return false;
			});
			$('#submit_simulador').mouseup(function() {
				$('#showSimOptions').slideUp();
				return false;
			})
		}
		,function(){
			$('#showSimOptions').slideUp();
		}
	);

	$('#minitab2').toggle(
		function() {
			$('#visualize').slideDown();
			$('#hideVisualize').click(function(){
				$('#visualize').slideUp();
				return false;
			});
		}
		,function(){
			$('#visualize').slideUp();
		}
	);

	$('#minitab1').toggle(
		function() {
			$.get('info_disasters.jsp', {}, function(data) {
				$('#console').html(data);
			});
			$('#console').slideDown();
			$('#console').click(function(){
				$(this).slideUp();
			});
			return false;
		},function(){
			$('#console').slideUp();
		}
	);

	$('#runSim').click(function() {
		$('#options2').show();
		$('#runSim').val('run');
	});

	$('#pauseSim, #restartSim').click(function() {
		$('#options2').hide();
	});
});