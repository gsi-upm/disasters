function mapInit(){
	var center = new GLatLng(40.416878,-3.703480); // La puerta del sol de Madrid
	map.setCenter(center, 11);
}

function buildingInit(){
	var formBuild = document.getElementById('buildings');
	if(formBuild.hospital.checked){
		showHospitals(); // para mostrar los edificios
	}
	if(formBuild.firemenStation.checked){
		showFiremenStations();
	}
	if(formBuild.policeStation.checked){
		showPoliceStations();
	}
}

function showHospitals(){
	generateBuilding('hospital','Hospital Gregorio Marañon', 40.418702, -3.670573);
}

function showFiremenStations(){
	generateBuilding('firemenStation','Parque de Bomberos', 40.414691, -3.706996);
}

function showPoliceStations(){
	generateBuilding('policeStation','Comisar&iacute;a central', 40.421565, -3.710095);
}

function initialize2(){}

function cargarMenuAcciones(){}

function cargarListaActividades(){}

function cargarLateral(){}

function limpiarLateral(){}