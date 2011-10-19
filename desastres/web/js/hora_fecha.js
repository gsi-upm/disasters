//  RELOJ 24 HORAS
var Reloj24H = null;
var RelojEnMarcha = false;

function MostrarHoraActual(){
	var ahora = new Date();
	var hora = ahora.getHours();
	var minuto = ahora.getMinutes();
	var segundo = ahora.getSeconds();
	var HHMMSS;
	var fecha;
       
	if(hora < 10){
		hora = '0' + hora;
	}
	if(minuto < 10){
		minuto = '0' + minuto;
	}
	if(segundo < 10){
		segundo = '0' + segundo;
	}

	HHMMSS = hora + ':' + minuto + ':' + segundo;
	document.getElementById('Reloj24H').innerHTML = HHMMSS;
	if(fecha == null || (hora == '00' && minuto == '00' && segundo == '00')){
		fecha = MostrarFechaActual(ahora);
		document.getElementById('fecha').innerHTML = fecha;
	}
	
	Reloj24H = setTimeout('MostrarHoraActual()',1000);
	RelojEnMarcha = true;
}

// FECHA
function MostrarFechaActual(hoy_es){
	var nombre_dia = [fmt('domingo',idioma), fmt('lunes',idioma), fmt('martes',idioma),
		fmt('miercoles',idioma), fmt('jueves',idioma), fmt('viernes',idioma), fmt('sabado',idioma)];
	var nombre_mes = [fmt('enero',idioma), fmt('febrero',idioma), fmt('marzo',idioma),
		fmt('abril',idioma), fmt('mayo',idioma), fmt('junio',idioma), fmt('julio',idioma), fmt('agosto',idioma),
		fmt('septiembre',idioma), fmt('octubre',idioma), fmt('noviembre',idioma), fmt('diciembre',idioma)];
	var dia_mes = hoy_es.getDate();
	var dia_semana = hoy_es.getDay();
	var mes = hoy_es.getMonth();
	var anno = hoy_es.getFullYear();
	var fecha;

	if(idioma == 'es'){
		fecha = nombre_dia[dia_semana] + ', ' + dia_mes + ' de ' + nombre_mes[mes] + ' de ' + anno;
	}else if(idioma == 'en' || idioma == 'en_GB'){
		var th = 'th';
		if(dia_mes == 1 || dia_mes == 21 || dia_mes == 31){
			th = 'st';
		}else if(dia_mes == 2 || dia_mes == 22){
			th = 'nd';
		}else if(dia_mes == 3 || dia_mes == 23){
			th = 'rd';
		}
		fecha = nombre_dia[dia_semana] + ', ' + nombre_mes[mes] + ', ' + dia_mes + th + ' ' + anno;
	}else if(idioma == 'fr'){
		if(dia_mes == 1){
			dia_mes = 'premier de';
		}
		fecha = nombre_dia[dia_semana] + ', le ' + dia_mes + ' ' + nombre_mes[mes] + ' ' + anno;
	}else if(idioma == 'de'){
		fecha = nombre_dia[dia_semana] + ', ' + dia_mes + ' ' + nombre_mes[mes] + ' ' + anno;
	}
	return fecha;
}

function IniciarReloj24() {
	DetenerReloj24();
	MostrarHoraActual();
}

function DetenerReloj24(){
	if(RelojEnMarcha) {
		clearTimeout(Reloj24H);
	}
	RelojEnMarcha = false;
}