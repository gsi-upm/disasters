function seleccionRadio (form, valor){
	var types = ["fire","flood","collapse","police","firemen","ambulance","slight","serious","dead","trapped"];
	for (Count = 0; Count < 4; Count++) {
		if (form.tipo[Count].checked)
			break;
	}
	return types[Count+3*valor];
}