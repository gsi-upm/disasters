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
	'fire':['Incendio','Fire','Feu','Feuer'],
	'flood':['Inundación','Flood','Inondation','Flut'],
	'collapse':['Derrumbamiento','Collapse','Effondrement','Einsturz'],
	'lostPerson':['Persona perdida','Lost person','Personne perdue','Vermisste'],
	'injuredPerson':['Persona herida','Injured person','Personne blessée','Verletzte'],
	'healthy':['Sano','Healthy','Santé','Gesundheit'],
	'slight':['Leve','Slight','Léger','Leicht'],
	'serious':['Grave','Serious','Grave','Schwer'],
	'dead':['Muerto','Dead','Mort','Toten'],
	'trapped':['Atrapado','Trapped','Coincé','Gefangener'],
	'small':['pequeño','small','petit','klein'],
	'medium':['mediano','medium','moyen','mittle'],
	'big':['grande','big','grand','groß'],
	'huge':['enorme','huge','énorme','enorm']
};

function fmt(key,lang){
	if(lang == null){
		lang = idioma;
	}

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
		palabra = '???' + key + '???';
	}
	return palabra;
}