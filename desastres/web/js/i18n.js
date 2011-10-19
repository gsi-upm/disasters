var dictionary = {
	// 'key': ['Español','English','Français','Deutsch'],
	'domingo': ['Domingo','Sunday','Dimanche','Sonntag'],
	'lunes': ['Lunes','Monday','Lundi','Montag'],
	'martes': ['Martes','Tuesday','Mardi','Dienstag'],
	'miercoles': ['Miércoles','Wednesday','Mercredi','Mittwoch'],
	'jueves': ['Jueves','Thursday','Jeudi','Donnerstag'],
	'viernes': ['Viernes','Friday','Vendredi','Freitag'],
	'sabado': ['Sábado','Saturday','Samedi','Samstag'],
	'enero': ['Enero','January','Janvier','Januar'],
	'febrero': ['Febrero','February','Février','Februar'],
	'marzo': ['Marzo','March','Mars','März'],
	'abril': ['Abril','April','Avril','April'],
	'mayo': ['Mayo','May','Mai','Mai'],
	'junio': ['Junio','June','Juin','Juni'],
	'julio': ['Julio','July','Juillet','Juli'],
	'agosto': ['Agosto','August','Août','August'],
	'septiembre': ['Septiembre','September','Septembre','September'],
	'octubre': ['Octubre','October','Octobre','Oktober'],
	'noviembre': ['Noviembre','November','Novembre','November'],
	'diciembre': ['Diciembre','December','Décembre','Dezember']
};

function fmt(key, lang){
	var langAux;
	if(lang == 'es'){
		langAux = 0;
	}else if(lang == 'en' || lang == 'en_GB'){
		langAux = 1;
	}else if(lang == 'fr'){
		langAux = 2;
	}else if(lang == 'de'){
		langAux = 3;
	}

	var palabra = dictionary[key][langAux];
	if(palabra == null){
		palabra = '';
	}
	return palabra;
}