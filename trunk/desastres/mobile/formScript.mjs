//forms

//var URL_base="http://localhost:8080/Disasters/rest/"; //developers url
var URL_base="http://138.4.3.208:8080/Disasters/rest/";

var itemType="event";
var type="fire";
var quantity=1;

var address="madrid";
var name="";
var info="";
var description="";
var latitud;
var longitud;
var idAssigned=0;
var size="";
var Traffic="";

function saveType(answerItem, answerType){
	debug("#########Elementos extraidos: "+ answerType);
	itemType=answerItem;
	type=answerType;
}


function saveQuantity(answerQuantity){
	quantity=answerQuantity;
}

function saveInfo(answerAddress, answerName, answerInfo, answerDescription){
	name=answerName;
	info=answerInfo;
	address=answerAddress;
	description=answerDescription;
}


function saveSize(answerSize){
size=answerSize;
}

function saveTraffic(answerTraffic){
traffic=answerTraffic;
}


function delete(idNumber){
		
		var delHttpRequest = new HttpRequest();
		delHttpRequest.onOpen=function() {
			debug("##### del - Request OPEN");
			
		};
		delHttpRequest.onLoaded = function() {
			debug("##### del -Request LOADED");
			var delXML = this.responseXML;
			actualize();
			//debug("#######esta es la respuesta: "+delXML);
			show(main);
			
		};
		
		delHttpRequest.onError = function() {
			errorMsg = this.statusText;
			show(error);
		};
		
		var parameters="delete/id/"+idNumber;
		
		delHttpRequest.open("GET", URL_base + parameters, true);			
		delHttpRequest.overrideMimeType("text/xml");
		delHttpRequest.send();
}




function send(){
		
		var postHttpRequest = new HttpRequest();
		postHttpRequest.onOpen=function() {
			debug("##### post - Request OPEN");
			show(loading, 1);
		};
		postHttpRequest.onLoaded = function() {
			debug("##### post -Request LOADED");
			var postXML = this.responseXML;
			
			debug("#######esta es la respuesta: "+postXML);
			//show(main);
			actualize();
			getAddress(address,latitud,longitud);
		};
		
		postHttpRequest.onError = function() {
			errorMsg = this.statusText;
			show(error);
		};
		
		var parameters="post/type="+type+"&name="+escape(name)+"&info="+escape(info)+"&description="+escape(description)+"&address="+escape(address)+"&latitud="+latitud+"&longitud="+longitud+"&quantity="+quantity+"&idAssigned="+idAssigned;
		if(size!=""){parameters+="&size="+size;}
		if(traffic!=""){parameters+="&traffic="+traffic;}
		//reset values
		idAssigned=0;
		size="";
		traffic="";
		postHttpRequest.open("GET", URL_base + parameters, true);			
		postHttpRequest.overrideMimeType("text/xml");
		postHttpRequest.send();
}



function validateAddress(address){
		var geoHttpRequest = new HttpRequest();
		geoHttpRequest.onOpen=function() {
			debug("##### Geo- Request OPEN");
		};
		geoHttpRequest.onLoaded = function() {
			debug("##### Geo-Request LOADED");
			var geoXML = this.responseXML;
			
			//debug("###LENGTH: "+geoXML.Result.length);
			//if there are several answers we take only the first one.
			if(geoXML.Result.length==Undefined){
				latitud = geoXML.Result.Latitude.text();
				longitud = geoXML.Result.Longitude.text();
			
			}
			else{
				latitud = geoXML.Result[0].Latitude.text();
				longitud = geoXML.Result[0].Longitude.text();
			
			}
			//debug("##### latitud: "+latitud + " - longitud: "+longitud);
			validAddress.visible="true";
			invalidAddress.visible="false";
						
		};
		
		geoHttpRequest.onError = function() {
			debug("##### Address not found");
			errorMsg = this.statusText;
			//show(error);
			invalidAddress.visible="true";
			validAddress.visible="false";
		};
		
		
		var params = "appid=" + appId + "&location=" + escape(address);
		geoHttpRequest.open("GET", "http://local.yahooapis.com/MapsService/V1/geocode?" + params, true);
		geoHttpRequest.overrideMimeType("text/xml");
		geoHttpRequest.send();
//debug("##########SENDING...");

}


function increment(parameter, valueParameter, increment){
		var putHttpRequest = new HttpRequest();
		putHttpRequest.onOpen=function() {
			debug("##### put- Request OPEN");
		};
		putHttpRequest.onLoaded = function() {
			debug("##### put-Request LOADED");
			
						
		};
		
		putHttpRequest.onError = function() {
			debug("##### put not realized");
			errorMsg = this.statusText;
			//show(error);
			
		};
		
		var putParams = id+"/"+parameter + "/"+valueParameter+"/inc/"+increment;
		putHttpRequest.open("GET", URL_base+"put/" + params, true);
		putHttpRequest.overrideMimeType("text/xml");
		putHttpRequest.send();
//debug("##########SENDING...");

}


function associate(idEvent){
	//debug("########id a asociar:"+idEvent);
	idAssigned=idEvent;
	//we have to find the event chosen
	var eventAssociated;
	for(i=0;i<Events.length;i++){
		eventAssociated = Events[i];
		if(eventAssociated.id==idEvent)break;
	}
	address = eventAssociated.address;
	latitud = eventAssociated.latitud;
	longitud = eventAssociated.longitud;
	
	
	//Creating the new distances
    //No floating support to do this!!!
	
	/*
	actualize();
	//Encontrar el elemento y su id
	var element;
	
	for(int i=0;i<Resources.length;i++){
		if(Resources[i].latitud!=latitud||Resources[i].longitud!=longitud||Resources[i].address!=address||Resources[i].name!=name)continue;
		element=Resources[i];
	}
	for(int i=0;i<People.length;i++){
		if(People[i].latitud!=latitud||People[i].longitud!=longitud||People[i].address!=address||People[i].name!=name)continue;
		element=People[i];
	}
	
	var id=element.id;
	
	
	
	
	
    if(type=="police"){
        increment(id, 'latitud','0.0005');
        
    }
    if(type=="firemen"){
        increment(id, 'latitud','0.00025');
        increment(id, 'longitud','-0.0005');
        
    }
    if(type=="ambulance"){
    	increment(id, 'latitud','0.00025');
        increment(id, 'longitud','0.0005');
       
    }
    if(type=="trapped"){
        increment(id, 'latitud','0.0005');
        increment(id, 'longitud','-0.001');
      
    }
    if(type=="slight"){
       increment(id, 'latitud','0.001');
       increment(id, 'longitud','-0.0005');
       
    }
    if(type=="serious"){
        increment(id, 'latitud','0.001');
        increment(id, 'longitud','0.0005');
        
    }
    if(type=="dead"){
        increment(id, 'latitud','0.0005');
        increment(id, 'longitud','0.001');
     
    }*/
	
	
	

}





