function seleccionRadio (form, valor){
	var types = ["fire","flood","collapse","injuredPerson","lostPerson",
		"police","firemen","ambulance","nurse","gerocultor","assistant",
		"healthy","slight","serious","dead","trapped"];
	var despTypes = 0; // desplaza el valor del tipo
	for (Count = 0; Count < 6; Count++) {
		if (form.tipo[Count].checked)
			break;
	}
	if(valor == 1){
		despTypes = 5;
	}else if(valor == 2){
		despTypes = 11;
	}
	return types[Count+despTypes];
}