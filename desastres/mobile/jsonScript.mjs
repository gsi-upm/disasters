//here starts the moblet
var Events;
var Resources;
var People;
var eventosLoaded = false;
var resourcesLoaded = false;
var initialDate = "1991-01-01+01:01:01";

function loadData() {
		
		Events = new Array();
		loadEvents();
		Resources = new Array();
		loadResources();
		People = new Array();
		loadPeople();
		
		}

function actualize(){
	Events = new Array();
		loadEvents();
		Resources = new Array();
		loadResources();
		People = new Array();
		loadPeople();

}
		
function loadEvents(){		
		var eventosRequest = new HttpRequest();
		eventosRequest.onOpen=function() {
			show(loading, 1);
		};
		eventosRequest.onLoaded = function() {
			var xmlObj = new Xml(this.response);
			saveXML(xmlObj,Events);			
		};
		
		eventosRequest.onError = function() {
			errorMsg = this.statusText;
			show(error);
		};
		eventosRequest.open("GET", URL_base+"xml/events", false);
		eventosRequest.overrideMimeType("text/xml");
		eventosRequest.send();
	
}



function loadResources(){		
		var resourcesRequest = new HttpRequest();
		resourcesRequest.onOpen=function() {
			};
		resourcesRequest.onLoaded = function() {
			
			
			var xmlObj = new Xml(this.response);
			saveXML(xmlObj,Resources);			
			
			
		};
		
		resourcesRequest.onError = function() {
			errorMsg = this.statusText;
			show(error);
		};
		
		resourcesRequest.open("GET", URL_base+"xml/resources", false);
		resourcesRequest.overrideMimeType("text/xml");
		resourcesRequest.send();
	
}

function loadPeople(){		
		var victimsRequest = new HttpRequest();
		victimsRequest.onOpen=function() {
			
		};
		victimsRequest.onLoaded = function() {
			var xmlObj = new Xml(this.response);
			hide(loading);
			saveXML(xmlObj,People);
		};
		
		victimsRequest.onError = function() {
			errorMsg = this.statusText;
			show(error);
			
		};
		
			
		
		victimsRequest.open("GET", URL_base+"xml/people", false);
		victimsRequest.overrideMimeType("text/xml");
		victimsRequest.send();
	
}



function saveXML(xmlObj,matrix){

					
			if (xmlObj.disaster==Undefined){
			//debug("Empty XML");
			}
			
			else if (xmlObj.disaster.length==Undefined){
				//debug("1 item in XML:");
				//debug(xmlObj.disaster.name.text());
			
				var temporal = new Disaster(xmlObj.disaster.id.text(),
    		 							xmlObj.disaster.item.text(),
    		 							xmlObj.disaster.type.text(),
    		 							xmlObj.disaster.quantity.text(),
    		 							xmlObj.disaster.name.text(),
    		 							xmlObj.disaster.description.text(),
    		 							xmlObj.disaster.info.text(),
    		 							xmlObj.disaster.latitud.text(),
    		 							xmlObj.disaster.longitud.text(),
    		 							xmlObj.disaster.address.text(),
    		 							xmlObj.disaster.state.text(),
    		 							xmlObj.disaster.date.text(), 
    		 							xmlObj.disaster.modified.text(),
    		 							xmlObj.disaster.username.text(), 
    		 							xmlObj.disaster.usertype.text(), 
    		 							xmlObj.disaster.size.text(), 
    		 							xmlObj.disaster.traffic.text(), 
    		 							xmlObj.disaster.idAssigned.text()  );
    		 							
    		 debug("desastre creado ##############");
    		 matrix[0]=temporal;	
				
				
			
			}
			
			else{
				debug("array of disasters");
				
				 for(i=0;i<xmlObj.disaster.length;i++){
			    		/*debug(i);
			    		debug(xmlObj.disaster[i].name.text());
						debug(xmlObj.disaster[i].id.text());
						debug(xmlObj.disaster[i].description.text());
						debug(xmlObj.disaster[i].info.text());
						debug(xmlObj.disaster[i].item.text());
						debug(xmlObj.disaster[i].type.text());
						debug(xmlObj.disaster[i].address.text());
						debug(xmlObj.disaster[i].quantity.text());
						debug(xmlObj.disaster[i].longitud.text());
						debug(xmlObj.disaster[i].latitud.text());
						debug(xmlObj.disaster[i].state.text());
						debug(xmlObj.disaster[i].size.text());
						debug(xmlObj.disaster[i].traffic.text());
						debug(xmlObj.disaster[i].idAssigned.text());
						debug(xmlObj.disaster[i].username.text());
						debug(xmlObj.disaster[i].usertype.text());
						debug(xmlObj.disaster[i].date.text());
						debug(xmlObj.disaster[i].modified.text());*/
			    		 var temporal = new Disaster(xmlObj.disaster[i].id.text(),
			    		 							xmlObj.disaster[i].item.text(),
			    		 							xmlObj.disaster[i].type.text(),
			    		 							xmlObj.disaster[i].quantity.text(),
			    		 							xmlObj.disaster[i].name.text(),
			    		 							xmlObj.disaster[i].description.text(),
			    		 							xmlObj.disaster[i].info.text(),
			    		 							xmlObj.disaster[i].latitud.text(),
			    		 							xmlObj.disaster[i].longitud.text(),
			    		 							xmlObj.disaster[i].address.text(),
			    		 							xmlObj.disaster[i].state.text(),
			    		 							xmlObj.disaster[i].date.text(), 
			    		 							xmlObj.disaster[i].modified.text(),
			    		 							xmlObj.disaster[i].username.text(), 
			    		 							xmlObj.disaster[i].usertype.text(), 
			    		 							xmlObj.disaster[i].size.text(), 
			    		 							xmlObj.disaster[i].traffic.text(), 
			    		 							xmlObj.disaster[i].idAssigned.text()  );
			    		 							
			    		 debug("desastre creado ##############");
			    		 matrix[i]=temporal;
			
			}
			
    		
	 }
	
	//debug(matrix);

}



