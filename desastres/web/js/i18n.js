var dictionary = {
	// 'key': ['Español','English','Français','Deutsch'],
	'domingo': ['Domingo','Sunday','Dimanche','Sonntag'],
	'lunes': ['Lunes','Monday','Lundi','Montag'],
	'martes': ['Martes','Tuesday','Mardi','Dienstag'],
	'miercoles': ['Miércoles','Wednesday','Mercredi','Mittwoch'],
	'jueves': ['Jueves','Thursday','Jeudi','Donnerstag'],
	'viernes': ['Viernes','Friday','Vendredi','Freitag'],
	'sabado': ['Sábado','Saturday','Samedi','Samstag'],
	'enero': ['enero','January','Janvier','Januar'],
	'febrero': ['febrero','February','Février','Februar'],
	'marzo': ['marzo','March','Mars','März'],
	'abril': ['abril','April','Avril','April'],
	'mayo': ['mayo','May','Mai','Mai'],
	'junio': ['junio','June','Juin','Juni'],
	'julio': ['julio','July','Juillet','Juli'],
	'agosto': ['agosto','August','Août','August'],
	'septiembre': ['septiembre','September','Septembre','September'],
	'octubre': ['octubre','October','Octobre','Oktober'],
	'noviembre': ['noviembre','November','Novembre','November'],
	'diciembre': ['diciembre','December','Décembre','Dezember'],
	'incendio':['Incendio','Fire','Feu','Feuer'],
	'inundacion':['Inundación','Flood','Inondation','Flut'],
	'derrumbamiento':['Derrumbamiento','Collapse','Effondrement','Einsturz'],
	'personaPerdida':['Persona perdida','Lost person','Personne perdue','Vermisste']
};

function fmt(key){
	var lang;
	if(idioma == 'es'){
		lang = 0;
	}else if(idioma == 'en' || idioma == 'en_GB'){
		lang = 1;
	}else if(idioma == 'fr'){
		lang = 2;
	}else if(idioma == 'de'){
		lang = 3;
	}

	var palabra = dictionary[key][lang];
	if(palabra == null){
		palabra = '???' + key + '???';
	}
	return palabra;
}