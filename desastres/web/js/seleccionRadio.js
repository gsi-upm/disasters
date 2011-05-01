function seleccionRadio (form, valor){
	var types = ["fire","flood","collapse","lostPerson","injuredPerson","police","firemen","ambulance","nurse","slight","serious","dead","trapped","healthy"];
	var despTypes = 0; // desplaza el valor del tipo
	for (Count = 0; Count < 5; Count++) {
		if (form.tipo[Count].checked)
			break;
	}
	if(valor == 1){
		despTypes = 5;
	}else if(valor == 2){
		despTypes = 9;
	}
	return types[Count+despTypes];
}