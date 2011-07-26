// FECHA
function MostrarFechaActual() {
	var nombre_dia = new Array(fmt('domingo',idioma), fmt('lunes',idioma), fmt('martes',idioma),
		fmt('miercoles',idioma), fmt('jueves',idioma), fmt('viernes',idioma), fmt('sabado',idioma));
	var nombre_mes = new Array(fmt('enero',idioma), fmt('febrero',idioma), fmt('marzo',idioma),
		fmt('abril',idioma), fmt('mayo',idioma), fmt('junio',idioma), fmt('julio',idioma), fmt('agosto',idioma),
		fmt('septiembre',idioma), fmt('octubre',idioma), fmt('noviembre',idioma), fmt('diciembre',idioma));

	var hoy_es = new Date();
	var dia_mes = hoy_es.getDate();
	var dia_semana = hoy_es.getDay();
	var mes = hoy_es.getMonth() + 1;
	var anyo = hoy_es.getYear();

	if (anyo < 100) {
		anyo = '19' + anyo;
	} else if ( ( anyo > 100 ) && ( anyo < 999 ) ) {
		var cadena_anyo = new String(anyo);
		anyo = '20' + cadena_anyo.substring(1,3);
	}
	
	if(idioma == 'es'){
		document.write(nombre_dia[dia_semana] + ', ' + dia_mes + ' de ' + nombre_mes[mes - 1] + ' de ' + anyo);
	}else if(idioma == 'en' || idioma == 'en_GB'){
		var th = 'th';
		if(dia_mes==1 || dia_mes==21 || dia_mes==31){
			th = 'st';
		}else if(dia_mes==2 || dia_mes==22){
			th = 'nd';
		}else if(dia_mes==3 || dia_mes==23){
			th = 'rd';
		}
		document.write(nombre_dia[dia_semana] + ', ' + nombre_mes[mes - 1] + ', ' + dia_mes + th + ' ' + anyo);
	}else if(idioma == 'fr'){
		if(dia_mes == 1){
			dia_mes = 'premier de';
		}
		document.write(nombre_dia[dia_semana] + ', le ' + dia_mes + ' ' + nombre_mes[mes - 1] + ' ' + anyo);
	}else if(idioma == 'de'){
		document.write(nombre_dia[dia_semana] + ', ' + dia_mes + ' ' + nombre_mes[mes - 1] + ' ' + anyo);
	}
}

function MostrarFechaActualEs() {
	var nombre_dia = new Array('domingo', 'lunes', 'martes', 'miercoles', 'jueves', 'viernes', 'sabado');
	var nombre_mes = new Array('enero', 'febrero', 'marzo', 'abril', 'mayo', 'junio', 'julio', 'agosto',
		'septiembre', 'octubre', 'noviembre', 'diciembre');

	var hoy_es = new Date();
	var dia_mes = hoy_es.getDate();
	var dia_semana = hoy_es.getDay();
	var mes = hoy_es.getMonth() + 1;
	var anyo = hoy_es.getYear();

	if (anyo < 100) {
		anyo = '19' + anyo;
	} else if ( ( anyo > 100 ) && ( anyo < 999 ) ) {
		var cadena_anyo = new String(anyo);
		anyo = '20' + cadena_anyo.substring(1,3);
	}
	document.write(nombre_dia[dia_semana] + ', ' + dia_mes + ' de ' + nombre_mes[mes - 1] + ' de ' + anyo);
}

function MostrarFechaActualEn() {
	var nombre_dia = new Array('Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday');
	var nombre_mes = new Array('January', 'February', 'March', 'April', 'May', 'June', 'July', 'August',
		'September', 'October', 'November', 'December');

	var hoy_es = new Date();
	var dia_mes = hoy_es.getDate();
	var dia_semana = hoy_es.getDay();
	var mes = hoy_es.getMonth() + 1;
	var anyo = hoy_es.getYear();

	if (anyo < 100) {
		anyo = '19' + anyo;
	} else if ((anyo > 100) && (anyo < 999)) {
		var cadena_anyo = new String(anyo);
		anyo = '20' + cadena_anyo.substring(1,3);
	}

	var th = 'th';
	if(dia_mes==1 || dia_mes==21 || dia_mes==31){
		th = 'st';
	}else if(dia_mes==2 || dia_mes==22){
		th = 'nd';
	}else if(dia_mes==3 || dia_mes==23){
		th = 'rd';
	}

	document.write(nombre_dia[dia_semana] + ', ' + nombre_mes[mes - 1] + ', ' + dia_mes + th + ' ' + anyo);
}

function MostrarFechaActualFr() {
	var nombre_dia = new Array('Dimanche', 'Lundi', 'Mardi', 'Mercredi', 'Jeudi', 'Vendredi', 'Samedi');
	var nombre_mes = new Array('Janvier', 'Février', 'Mars', 'Avril', 'Mai', 'Juin', 'Juillet ','Août',
		'Septembre','Octobre','Novembre', 'Décembre');

	var hoy_es = new Date();
	var dia_mes = hoy_es.getDate();
	var dia_semana = hoy_es.getDay();
	var mes = hoy_es.getMonth() + 1;
	var anyo = hoy_es.getYear();

	if (anyo < 100) {
		anyo = '19' + anyo;
	} else if ( ( anyo > 100 ) && ( anyo < 999 ) ) {
		var cadena_anyo = new String(anyo);
		anyo = '20' + cadena_anyo.substring(1,3);
	}

	if(dia_mes == 1){
		dia_mes = 'premier de';
	}

	document.write(nombre_dia[dia_semana] + ', le ' + dia_mes + ' ' + nombre_mes[mes - 1] + ' ' + anyo);
}

function MostrarFechaActualDe() {
	var nombre_dia = new Array('Sonntag', 'Montag', 'Dienstag', 'Mittwoch', 'Donnerstag', 'Freitag', 'Samstag');
	var nombre_mes = new Array('Januar', 'Februar', 'März', 'April', 'Mai', 'Juni', 'Juli', 'August',
		'September', 'Oktober','November', 'Dezember');

	var hoy_es = new Date();
	var dia_mes = hoy_es.getDate();
	var dia_semana = hoy_es.getDay();
	var mes = hoy_es.getMonth() + 1;
	var anyo = hoy_es.getYear();

	if (anyo < 100) {
		anyo = '19' + anyo;
	} else if ( ( anyo > 100 ) && ( anyo < 999 ) ) {
		var cadena_anyo = new String(anyo);
		anyo = '20' + cadena_anyo.substring(1,3);
	}

	/*if(dia_mes < 10){
		dia_mes = '0' + dia_mes;
	}
	if(mes < 10){
		mes = '0' + mes;
	}*/

	document.write(nombre_dia[dia_semana] + ', ' + dia_mes + ' ' + nombre_mes[mes - 1] + ' ' + anyo);
}

//  RELOJ 24 HORAS
var Reloj24H = null;
var RelojEnMarcha = false;

function DetenerReloj24() {
	if(RelojEnMarcha) {
		clearTimeout(Reloj24H);
	}
	RelojEnMarcha = false;
}

function MostrarHoraActual() {
	var ahora = new Date();
	var hora = ahora.getHours();
	var minuto = ahora.getMinutes();
	var segundo = ahora.getSeconds();
	var HHMMSS
       
	if (hora < 10) {
		HHMMSS = '0' + hora;
	} else {
		HHMMSS = ' ' + hora;
	}
	if (minuto < 10) {
		HHMMSS += ':0' + minuto;
	} else {
		HHMMSS += ':' + minuto;
	}
	if (segundo < 10) {
		HHMMSS += ':0' + segundo;
	} else {
		HHMMSS += ':' + segundo;
	}
 
	document.getElementById('Reloj24H').innerHTML = HHMMSS;
	Reloj24H = setTimeout('MostrarHoraActual()',1000);
	RelojEnMarcha = true;
}

function IniciarReloj24() {
	DetenerReloj24();
	MostrarHoraActual();
}
