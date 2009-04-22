
// Some default settings
var zipcode = 55901;
var appId = "wK3p2_zV34Gy_9N.gow6.Ya7gxh.ZSi88fT4a6YF15iP5eegnp_2zeactjEhiaE6l6J2jzx.lWA-";
var zoom = 5;
var errorMsg;
var latitudMap="";
var longitudMap="";
var zoomLevels = new Array();

//
// Load up the requested map
//
function loadMap() {
	if (zoomLevels[zoom]) {
		mapviewer.reset();
	} else {
		if (mapviewer.start) {
			mapviewer.start();
		}
		var mapHttpRequest = new HttpRequest();
		mapHttpRequest.onOpen=function() {
			show(loading, 1);
		};
		mapHttpRequest.onLoaded = function() {
			var mapXML = this.responseXML;
			mapURL = mapXML.url;
			loadMapTile(mapURL);
		};
		
		mapHttpRequest.onError = function() {
			errorMsg = this.statusText;
			show(error);
		};
		
		var params = 
			"appid=" + appId + 
			"&location=" + escape(zipcode) + 
			"&zoom=" + zoom +
			"&latitude="+ latitudMap +
			"&longitude="+ longitudMap +
			"&image_height=400" + 
			"&image_width=500" ;
			
		mapHttpRequest.open("GET", "http://api.local.yahoo.com/MapsService/V1/mapImage?" + params, true);
		mapHttpRequest.applyFilter("mMaps.xsl");
		mapHttpRequest.overrideMimeType("text/xml");
		
		mapHttpRequest.send();
	}
}

function loadMapTile(url) {
	var request = new HttpRequest();
	request.onLoaded = function() {
		debug('loadMapTile ...');
		var image = this.responseImage;
		debug('loadMapTile ... 1');
		zoomLevels[zoom] = image;
		//debug('loadMapTile ... 2');
		//mapviewer.reset();
		debug('loadMapTile ... 3');
		show(mapviewer);
		debug('loadMapTile ... 4');
		mapviewer.stop();
	};
	
	request.onError = function() {
		errorMsg = this.statusText;
		show(error);
	};
	
	request.open("GET", url , true);
	request.overrideMimeType("image/png");
	
	request.send();
}

function zoomIn() {
	zoom -= 1;
	if (zoom < 0) zoom = 0;
	loadMap();
}

function zoomOut() {
	zoom += 1;
	if (zoom > 7) zoom = 7;
	loadMap();
}



function getAddress(address,latitudAnswer,longitudAnswer) {
	//debug("#####Latitud y longitud: "+latitudAnswer+longitudAnswer);
	latitudMap=latitudAnswer;
	longitudMap=longitudAnswer;
	zipcode = address;
	zoom = 5;
	zoomLevels.clear();
	loadMap();
}