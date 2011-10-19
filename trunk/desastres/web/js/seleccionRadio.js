function seleccionRadio(form, valor){
	var types = ['fire','flood','collapse','injuredPerson','lostPerson',
		'police','firemen','ambulance','nurse','gerocultor','assistant',
		'healthy','slight','serious','dead','trapped'];
	var despTypes = 0; // desplaza el valor del tipo
	for(var count = 0; count < 6; count++){
		if(form.tipo[count].checked){
			break;
		}
	}
	if(valor == 1){
		despTypes = 5;
	}else if(valor == 2){
		despTypes = 11;
	}
	return types[count+despTypes];
}