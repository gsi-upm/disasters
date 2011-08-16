function initialize2(){}

function mapInit(){
	center = new GLatLng(40.416878,-3.703480); //La puerta del sol de Madrid
	map.setCenter(center, 11);
}

function buildingInit(){
	var formBuild = document.getElementById('buildings');
	if(formBuild.hospital.checked){
		showHospitals();//para mostrar los edificios
	}
	if(formBuild.firemenStation.checked){
		showFiremenStations();
	}
	if(formBuild.policeStation.checked){
		showPoliceStations();
	}
	if(formBuild.geriatricCenter.checked){
		showGeriatricCenters();
	}
}