

// FECHA
function MostrarFechaActual()    {
var nombre_dia = new Array("domingo", "lunes", "martes", "miércoles", "jueves", "viernes", "sábado")
var nombre_mes = new Array("enero", "febrero", "marzo", "abril", "mayo", "junio", "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre")

var hoy_es = new Date();
dia_mes = hoy_es.getDate();
dia_semana = hoy_es.getDay();
mes = hoy_es.getMonth() + 1;
anyo = hoy_es.getYear();

if (anyo < 100) anyo = '19' + anyo
    else if ( ( anyo > 100 ) && ( anyo < 999 ) ) { var cadena_anyo = new String(anyo) ; anyo = '20' + cadena_anyo.substring(1,3) }
document.write(nombre_dia[dia_semana] + ", " + dia_mes + " de " + nombre_mes[mes - 1] + " de " + anyo) }

function MostrarFechaActualEn()    {
var nombre_dia = new Array("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
var nombre_mes = new Array("January", "February", "March", "April", "May", "June", "July", "Agost", "September", "October", "November", "December")

var hoy_es = new Date();
dia_mes = hoy_es.getDate();
dia_semana = hoy_es.getDay();
mes = hoy_es.getMonth() + 1;
anyo = hoy_es.getYear();

if (anyo < 100) anyo = '19' + anyo
    else if ( ( anyo > 100 ) && ( anyo < 999 ) ) { var cadena_anyo = new String(anyo) ; anyo = '20' + cadena_anyo.substring(1,3) }

var th = "th";
if (dia_mes%10==1)th = "st";
if (dia_mes%10==2)th ="nd";
if (dia_mes&10==3)th = "rd";

document.write(nombre_dia[dia_semana] + ", " + dia_mes + th + " of " + nombre_mes[mes - 1] + " of " + anyo) }




//  RELOJ 24 HORAS
var Reloj24H = null
var RelojEnMarcha = false

function DetenerReloj24 () {if(RelojEnMarcha)  clearTimeout(Reloj24H); RelojEnMarcha = false }

function MostrarHoraActual() {
 var ahora = new Date()
 var hora = ahora.getHours()
 var minuto = ahora.getMinutes()
 var segundo = ahora.getSeconds()
 var HHMMSS
       
if (hora < 10) {HHMMSS = "0" + hora} else {HHMMSS = " " + hora};
if (minuto < 10) {HHMMSS += ":0" + minuto} else {HHMMSS += ":" + minuto};
if (segundo < 10) {HHMMSS += ":0" + segundo} else {HHMMSS += ":" + segundo};
 
document.getElementById("Reloj24H").digitos.value=HHMMSS;       
Reloj24H = setTimeout("MostrarHoraActual()",1000)
RelojEnMarcha = true }

function IniciarReloj24() {
 	DetenerReloj24();
 	MostrarHoraActual() }
	
	
	


