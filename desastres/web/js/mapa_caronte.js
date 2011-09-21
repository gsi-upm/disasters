function initialize2(){
	if(nivelMsg > 1){
		if(document.getElementById('opcionesMapa').verSanos.checked){
			verSanos = true;
		}
	}
}

function mapInit(){
	center = new GLatLng(38.232272, -1.698925); // Calasparra, Murcia (geriatrico)
	map.setCenter(center, 21);
}

function buildingInit(){
	showHospitals();
	showFiremenStations();
	showPoliceStations();
	showGeriatricCenters();
}