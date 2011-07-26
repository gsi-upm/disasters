var dictionary = new Array();

dictionary['domingo'] = ['Domingo','Sunday','Dimanche','Sonntag'];
dictionary['lunes'] = ['Lunes','Monday','Lundi','Montag'];
dictionary['martes'] = ['Martes','Tuesday','Mardi','Dienstag'];
dictionary['miercoles'] = ['Miércoles','Wednesday','Mercredi','Mittwoch'];
dictionary['jueves'] = ['Jueves','Thursday','Jeudi','Donnerstag'];
dictionary['viernes'] = ['Viernes','Friday','Vendredi','Freitag'];
dictionary['sabado'] = ['Sábado','Saturday','Samedi','Samstag'];
dictionary['enero'] = ['Enero','January','Janvier','Januar'];
dictionary['febrero'] = ['Febrero','February','Février','Februar'];
dictionary['marzo'] = ['Marzo','March','Mars','März'];
dictionary['abril'] = ['Abril','April','Avril','April'];
dictionary['mayo'] = ['Mayo','May','Mai','Mai'];
dictionary['junio'] = ['Junio','June','Juin','Juni'];
dictionary['julio'] = ['Julio','July','Juillet','Juli'];
dictionary['agosto'] = ['Agosto','August','Août','August'];
dictionary['septiembre'] = ['Septiembre','September','Septembre','September'];
dictionary['octubre'] = ['Octubre','October','Octobre','Oktober'];
dictionary['noviembre'] = ['Noviembre','November','Novembre','November'];
dictionary['diciembre'] = ['Diciembre','December','Décembre','Dezember'];

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