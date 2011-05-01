function mapInit(){
	center = new GLatLng(38.232237, -1.699016); // Calasparra, Murcia (geriatrico)
	map.setCenter(center, 19);
}

function buildingInit(){
	showHospitals();
	showFiremenStations();
	showPoliceStations();
	showGeriatricCenters();
}