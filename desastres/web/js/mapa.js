var map;
var center;
var localizador;
var marcadores_definitivos;
var marcadores_temporales;
var indices;
var ultimamodif;
var timeout;

var hospitals;
var hospIndex = 0;
var firemenStations;
var fireIndex = 0;
var policeStations;
var policeIndex = 0;
var geriatricCenters;
var geriatricIndex = 0;

var pos_def;
var pos_temp;
var pos_indices;
var puntero_temp;
var caracter_temp;

var TEMPORAL = 0;
var DEFINITIVO = 1;

var ANONYMOUS = 1;
var usuario_actual;
var usuario_actual_tipo;

var estado_Experto = "off";

var tiempoActualizacion = 1500;
var tiempoInicial = 5000;

var userName = null;
var latitudUser = 0;
var longitudUser = 0;

var url_base = "/desastres/Xpert" ;

function initialize() {
	if (GBrowserIsCompatible()) {
		/* GOOGLE MAPS v3
		center = new google.maps.LatLng(38.232276, -1.699829); // Calasparra, Murcia (geriatrico)
		var myOptions = {
			center: center,
			zoom: 18,
			mapTypeId: google.maps.MapTypeId.ROADMAP
		};
		map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
		 */

		map = new GMap2(document.getElementById("map_canvas"));
		localizador = new GClientGeocoder();
		mapInit();

		//empezamos con un usuario anonimo, permitir registrarse mas tarde
		usuario_actual = ANONYMOUS;
		usuario_actual_tipo = "citizen";
		map.addControl(new GLargeMapControl()); //controles completos
		map.addControl(new GScaleControl ()); //Escala
		map.addControl(new GMapTypeControl()); //mapa, foto, hibrido

		//inicializamos los arrays
		marcadores_definitivos = new Array;
		pos_def = 0;
		marcadores_temporales = new Array;
		pos_temp = 0;
		hospitals = new Array;
		policeStations = new Array;
		firemenStations = new Array;
		geriatricCenters = new Array;

		indices = new Array;
		pos_indices = 0;

		buildingInit();

		ultimamodif = '1992-01-01 00:00:01'; //(una fecha antigua para empezar)
		//hacemos la peticion inicial del json (baja todo menos los borrados)
		$.getJSON('leeEventos.jsp', {
			'fecha': ultimamodif,
			'action':"firstTime",
			'nivel':nivelMsg
		}, function(data) {
			$.each(data, function(entryIndex, entry) {
				var nuevomarcador = new ObjMarcador(entry['id'],entry['item'],entry['type'],
					entry['quantity'],entry['name'],entry['description'],entry['info'],
					entry['latitud'],entry['longitud'],entry['address'],entry['state'],
					entry['date'], entry['modified'],entry['user_name'],entry['user_type'],
					null,entry['size'],entry['traffic'],entry['idAssigned'],null);

				nuevomarcador.marker = generaMarcador(nuevomarcador, DEFINITIVO);
				marcadores_definitivos[nuevomarcador.id] = nuevomarcador;
				indices[pos_indices] = nuevomarcador.id;
				pos_indices++;
			});
		});
		ultimamodif = obtiene_fecha();

		setTimeout("moveAgents()",2000);
		setTimeout("actualizar()",tiempoInicial);

		if(document.getElementById("signeduser") != null){
			userName = document.getElementById("signeduser").innerHTML;
		}

		if(navigator.geolocation){
			navigator.geolocation.getCurrentPosition(coordenadasUsuario);
		}
	}
}

function coordenadasUsuario(pos){
	latitudUser = pos.coords.latitude;
	longitudUser = pos.coords.longitude;

	var icono = new GIcon();
	icono.image = "markers/user.png";
	icono.iconSize = new GSize(28, 43);
	icono.iconAnchor = new GPoint(28, 43);
	opciones = {
		icon:icono
	};
	
	if(nivelMsg==null || nivelMsg==0){
		var marker = new GMarker (new GLatLng(latitudUser,longitudUser), opciones);
		map.addOverlay(marker);
	}

	if(userName != null){
		/*$.post('updateLatLong.jsp',{
			'nombre':userName,
			'latitud':latitudUser,
			'longitud':longitudUser
		});*/
		//*************
		// PRUEBAAA!!!
		//*************
		$.post('updateLatLong.jsp',{
			'nombre':userName,
			'latitud':38.232062 + (2*Math.random()-1)*0.0001,
			'longitud':-1.698394 + (2*Math.random()-1)*0.0001
		});
	}
}

function actualizar(){
	//cada 10 segundos hacemos la peticion actualizadora de json
	$.getJSON('leeEventos.jsp', {
		'fecha': ultimamodif,
		'action':"notFirst",
		'nivel':nivelMsg
	}, function(data) {
		$.each(data, function(entryIndex, entry) {
			//el id lo asigna la base de datos
			var nuevomarcador = new ObjMarcador(entry['id'],entry['item'],entry['type'],
				entry['quantity'],entry['name'],entry['description'],entry['info'],
				entry['latitud'],entry['longitud'],entry['address'],entry['state'],
				entry['date'], entry['modified'],entry['user_name'],entry['user_type'],
				null,entry['size'],entry['traffic'],entry['idAssigned'],null);

			//pintamos los nuevos, para lo que comprobamos que no existian
			if(marcadores_definitivos[nuevomarcador.id]==null){
				//alert("Vamos a pintar un marcador nuevo");
				if(nuevomarcador.estado!='erased'){
					nuevomarcador.marker=generaMarcador(nuevomarcador, DEFINITIVO);
					marcadores_definitivos[nuevomarcador.id]=nuevomarcador;
					indices[pos_indices]=nuevomarcador.id;
					pos_indices++;
				}
			}
			//actualizamos los que han sido modificados
			else{
				//alert("Vamos a actualizar el marcador "+nuevomarcador.id);
				//si se ha modificado algun dato se actualiza
				if(nuevomarcador.estado!="erased"){
					//alert("Actualizacion de un parametro cualquiera");
					map.removeOverlay(marcadores_definitivos[nuevomarcador.id].marker);
					nuevomarcador.marker=generaMarcador(nuevomarcador, DEFINITIVO);
					marcadores_definitivos[nuevomarcador.id]=nuevomarcador;
				}
				//si se ha eliminado un marcador
				else{
					//alert("Se ha borrado el marcador");
					map.removeOverlay(marcadores_definitivos[nuevomarcador.id].marker);
					marcadores_definitivos[nuevomarcador.id]=null;
					pos_indices--;

					//eliminamos el indice de la matriz de indices sin dejar huecos
					var id = nuevomarcador.id;
					var index;

					for (i=0;i<indices.length;i++){
						index = i;
						if (indices[i]== id) break;
					}

					while(indices[index+1]!=null){
						indices[index]=indices[index+1];
						index++;
					}
					indices[index]=null;
				}
			}
		});
	});

	ultimamodif = obtiene_fecha();
	timeout = setTimeout("actualizar()", tiempoActualizacion);
}

function showHospitals(){
	generateBuilding("hospital","Hospital Gregorio Marañon", 40.418702, -3.670573);
	generateBuilding("hospital","Centro de salud", 38.228138, -1.706449); // Calasparra, Murcia
}
function showFiremenStations(){
	generateBuilding("firemenStation","Parque de Bomberos", 40.414691, -3.706996);
	generateBuilding("firemenStation","Parque de bomberos", 38.111020,-1.865018); // Caravaca de la Cruz, Murcia
	generateBuilding("firemenStation","Parque de bomberos TEMPORAL", 38.21602,-1.72306); // TEMPORAL
}
function showPoliceStations(){
	generateBuilding("policeStation","Comisar&iacute;a central", 40.421565, -3.710095);
	generateBuilding("policeStation","Ayuntamiento y Polic&iacute;a municipal", 38.231125, -1.697560); // Calasparra, Murcia
}
function showGeriatricCenters(){
	generateBuilding("geriatricCenter","Residencia Virgen de la Esperanza", 38.232237, -1.699016); // Calasparra, Murcia
}

function generateBuilding(type, mensaje, latitud, longitud){
	var imagen;
	var matrix;
	var matrixIndex;
	if(type=='hospital'){
		imagen = "ambulanceStation.png";
		matrix = hospitals;
		matrixIndex = hospIndex;
	}else if(type=='policeStation'){
		imagen = "policeStation.png";
		matrix = policeStations;
		matrixIndex = policeIndex;
	}else if(type=='firemenStation'){
		imagen = "firemenStation.png";
		matrix = firemenStations;
		matrixIndex = fireIndex;
	}else if(type=='geriatricCenter'){
		imagen = "geriatricCenter.png";
		matrix = geriatricCenters;
		matrixIndex = geriatricIndex;
	}

	var icono = new GIcon();
	icono.image = "markers/"+imagen;

	//icono.shadow = "markers/shadow50.png";
	icono.iconSize = new GSize(50, 49);
	//icono.shadowSize = new GSize(43, 43);
	icono.iconAnchor = new GPoint(25, 40);
	icono.infoWindowAnchor = new GPoint(5, 1);
	opciones = {
		icon:icono
	}; //se pueden arrastrar para asociarlo

	var marker = new GMarker (new GLatLng(latitud,longitud), opciones);
	if(type=='geriatricCenter'){
		var linea1 = new GPolyline([new GLatLng(38.232634, -1.699622), new GLatLng(38.232634, -1.698201)], "#00ff00", 1, 0.5);
		var linea2 = new GPolyline([new GLatLng(38.232634, -1.698201), new GLatLng(38.231943, -1.698201)], "#00ff00", 1, 0.5);
		var linea3 = new GPolyline([new GLatLng(38.231943, -1.698201), new GLatLng(38.231943, -1.699622)], "#00ff00", 1, 0.5);
		var linea4 = new GPolyline([new GLatLng(38.231943, -1.699622), new GLatLng(38.232634, -1.699622)], "#00ff00", 1, 0.5);
		GEvent.addListener(marker, "click", function() {
			marker.openInfoWindowHtml("<div id='bocadillo'>"+mensaje+"</div>");
			map.addOverlay(linea1);
			map.addOverlay(linea2);
			map.addOverlay(linea3);
			map.addOverlay(linea4);
		});
		GEvent.addListener(marker, "infowindowclose", function() {
			map.removeOverlay(linea1);
			map.removeOverlay(linea2);
			map.removeOverlay(linea3);
			map.removeOverlay(linea4);
		});
	}else{
		GEvent.addListener(marker, "click", function() {
			marker.openInfoWindowHtml("<div id='bocadillo'>"+mensaje+"</div>");
		});
	}

	map.addOverlay(marker);
	matrix[matrixIndex]=marker;
	matrixIndex++;

	if(type=='hospital'){
		hospitals = matrix;
		hospIndex = matrixIndex;
	}else if(type=='policeStation'){
		policeStations = matrix;
		policeIndex = matrixIndex;
	}else if(type=='firemenStation'){
		firemenStations = matrix;
		fireIndex = matrixIndex;
	}else if(type=='geriatricCenter'){
		geriatricCenters = matrix;
		geriatricIndex = matrixIndex;
	}

	return marker;
}

function hideBuilding(type){
	var matrix;
	if(type=='hospital'){
		matrix=hospitals;
		hospitals = new Array;
		hospIndex = 0;
	}else if(type=='policeStation'){
		matrix=policeStations;
		policeStations = new Array;
		policeIndex = 0;
	}else if(type=='firemenStation'){
		matrix=firemenStations;
		firemenStations = new Array;
		fireIndex = 0;
	}else if(type=='geriatricCenter'){
		matrix=geriatricCenters;
		geriatricCenters = new Array;
		geriatricIndex = 0;
	}
	for(i=0; i<matrix.length; i++){
		map.removeOverlay(matrix[i]);
	}
}

function visualize(selected, type){
	if(type=='hospital'){
		if(selected){
			showHospitals();
		}else{
			hideBuilding (type);
		}
	}
	if(type=='policeStation'){
		if(selected){
			showPoliceStations();
		}else{
			hideBuilding (type);
		}
	}
	if(type=='firemenStation'){
		if(selected){
			showFiremenStations();
		}else{
			hideBuilding (type);
		}
	}
	if(type=='geriatricCenter'){
		if(selected){
			showGeriatricCenters();
		}else{
			hideBuilding (type);
		}
	}
}

function generaMarcador(evento, caracter){
	var opciones;
	var icono;

	//Es un evento
	if (evento.marcador=="event"){
		//Incendio
		if (evento.tipo=="fire"){
			icono = new GIcon(G_DEFAULT_ICON);
			icono.image = "markers/fuego.png";
			if(evento.estado=="controlled"){
				icono.image ="markers/fuego_control.png"
			}
		}
		//Inundacion
		if (evento.tipo=="flood"){
			icono = new GIcon(G_DEFAULT_ICON);
			icono.image = "markers/agua.png";
			if(evento.estado=="controlled"){
				icono.image ="markers/agua_control.png";
			}
		}
		//derrumbamiento
		if (evento.tipo=="collapse"){
			icono = new GIcon(G_DEFAULT_ICON);
			icono.image = "markers/casa.png";
			if(evento.estado=="controlled"){
				icono.image ="markers/casa_control.png";
			}
		}
		//anciano perdido
		if (evento.tipo=="lostPerson"){
			icono = new GIcon(G_DEFAULT_ICON);
			icono.image = "markers/personaPerdida.png";
			if(evento.estado=="controlled"){
				icono.image ="markers/personaPerdida_control.png";
			}
		}//anciano herido
		if (evento.tipo=="injuredPerson"){
			icono = new GIcon(G_DEFAULT_ICON);
			icono.image = "markers/personaHerida.png";
			if(evento.estado=="controlled"){
				icono.image ="markers/personaHerida_control.png";
			}
		}

		opciones = {
			icon:icono,
			draggable: true //Para que se pueda arrastrar
		}; 
	}
	//es un recurso
	if (evento.marcador=="resource"){
		//MAXIMO DE RECURSOS POR MARCADOR ES 10
		if (evento.cantidad > 10) {
			evento.cantidad = 10;
		}
		var active;
		if(evento.estado == 'active'){
			active = '_no';
		}else{
			active = evento.cantidad;
		}

		icono = new GIcon(G_DEFAULT_ICON);
		//es un policia
		if (evento.tipo=="police"){
			icono.image = "markers/policia"+active+".png";
		}
		//es un bombero
		if (evento.tipo=="firemen"){
			icono.image = "markers/bombero"+active+".png";
		}
		//es una ambulancia
		if (evento.tipo=="ambulance" || evento.tipo=="ambulancia"){
			icono.image = "markers/ambulancia"+active+".png";
		}
		//es un enfermero
		if (evento.tipo=="nurse"){
			icono.image = "markers/enfermero"+active+".png";
		}
		//es un gerocultor
		if (evento.tipo=="gerocultor"){
			icono.image = "markers/gerocultor"+active+".png";
		}
		//es un auxiliar
		if (evento.tipo=="assistant"){
			icono.image = "markers/auxiliar"+active+".png";
		}
		//otro
		if (evento.tipo=="otherStaff"){
			icono.image = "markers/otro"+active+".png";
		}

		opciones = {
			icon:icono,
			draggable: false //No se puede arrastrar
		}; 
	}
	//es una victima
	if (evento.marcador=="people"){
		icono = new GIcon();

		//MAXIMO DE VICTIMAS POR MARCADOR ES 10
		if (evento.cantidad>10){
			evento.cantidad=10;
		}

		//personas atrapadas
		if (evento.tipo=="trapped"){
			icono.image = "markers/trapped"+evento.cantidad+".png";
			//falta hacer este icono
			if(evento.estado=="controlled"){
				icono.image = "markers/trapped_control.png";
			}
		}
		//heridos leves
		if (evento.tipo=="healthy"){
			icono.image = "markers/sano"+evento.cantidad+".png";
		//if(evento.estado=="controlled"){
		//	icono.image = "markers/sano_control.png";
		//}
		}
		//heridos leves
		if (evento.tipo=="slight"){
			icono.image = "markers/leve"+evento.cantidad+".png";
			if(evento.estado=="controlled"){
				icono.image = "markers/slight_control.png";
			}
		}
		//heridos graves
		if (evento.tipo=="serious"){
			icono.image = "markers/grave"+evento.cantidad+".png";
			if(evento.estado=="controlled"){
				icono.image = "markers/serious_control.png";
			}
		}
		//muertos
		if (evento.tipo=="dead"){
			icono.image = "markers/muerto"+evento.cantidad+".png";
			if(evento.estado=="controlled"){
				icono.image = "markers/dead_control.png";
			}
		}
		icono.shadow = "markers/shadow50.png";
		icono.iconSize = new GSize(28, 43);
		icono.shadowSize = new GSize(43, 43);
		icono.iconAnchor = new GPoint(10, 40);
		icono.infoWindowAnchor = new GPoint(5, 1);
		opciones = {
			icon:icono,
			draggable: true //Se pueden arrastrar para asociarlo
		}; 
	}

	var marker = new GMarker (new GLatLng(evento.latitud, evento.longitud), opciones);

	//Annadimos el comportamiento

	if(caracter == TEMPORAL){ //aqui hay que guardar los datos
		var content = evento.nombre +"<br>" + evento.info + "<br>" +evento.descripcion + "<br>"
		+ "<a id=\"guardar\" href=\"#\" onclick=\"guardar(marcadores_temporales["+evento.id+"]); return false;\" > Guardar </a>"
		+" - "+ "<a id=\"modificar\" href=\"#\" onclick=\"cargarModificar(marcadores_temporales["+evento.id+"],TEMPORAL); return false;\" > Modificar </a>"
		+" - "+ "<a id=\"eliminar\" href=\"#\" onclick=\"eliminar(marcadores_temporales["+evento.id+"],TEMPORAL); return false;\" > Eliminar </a>";
		//marker.openInfoWindowHtml("<div id=\"bocadillo\">"+content+"</div>");//el globito se abre solo pero esto no lo consigo

		GEvent.addListener(marker, "click", function() {
			// content = evento.nombre + "<a href=\"#\" onclick=\"guardar(marcadores_temporales["+evento.id+"]); return false;\" > Guardar </a>";
			marker.openInfoWindowHtml("<div id=\"bocadillo\">"+content+"</div>");
		});

		GEvent.addListener(marker, "dragstart", function() {
			map.closeInfoWindow();
		});
		GEvent.addListener(marker, "dragend", function() {
			var asociada = asociar(evento.id, evento.marker);
			marker.openInfoWindowHtml("<div id=\"bocadillo\">"+"Es necesario guardar para poder asociar recursos. <br>"+marcadores_definitivos[asociada].nombre
				+ "<br><a id=\"guardar_asociacion\" href=\"#\" onclick=\"guardar(marcadores_temporales["+evento.id+"]); return false;\" > Guardar </a>"
				+" - "+ "<a id=\"cancelar\" href=\"#\" onclick=\"map.closeInfoWindow();\" > Cancelar </a>"+"</div>"  );
		});
	}

	if(caracter == DEFINITIVO){ //aqui podemos realizar modificaciones a los ya existentes
		GEvent.addListener(marker, "click", function() {
			var small = evento.nombre +"<br>" + evento.descripcion;
			var menuAcciones = cargarMenuAcciones(marcadores_definitivos[evento.id]);
			var links1 = '';
			if(nivelMsg > 1){
				if(evento.marcador!='resource'){
					links1 = "<br>" + menuAcciones;
				}else{
					if(evento.idAssigned == 0){
						links1 = "<br>" + "Sin actuar";
					}else{
						links1 = "<br>" + "Actuando sobre la emergencia " + evento.idAssigned;
					}
				}
				
				/*"<a id=\"modificar\" href=\"#\" onclick=\"cargarModificar(marcadores_definitivos["+evento.id+"],DEFINITIVO); return false;\" > Modificar </a>"
				+" - "+"<a id=\"acciones\" href='#'onclick=\"cargarAcciones(marcadores_definitivos["+evento.id+"])\"  > Acciones </a>"
				+" - "+ "<a id=\"eliminar\" href=\"#\" onclick=\"eliminar(marcadores_definitivos["+evento.id+"],DEFINITIVO); return false;\" > Eliminar </a>"
				+" - "+ "<a id='ver_mas1' href='#'onclick=\"verMas("+evento.id+");return false;\"  > Ver m&aacute;s </a>"*/
			}
			/*else{
				links1 = "<a id='ver_mas1' href='#'onclick=\"verMas("+evento.id+");return false;\"  > Ver m&aacute;s </a><br>";
			}*/
				
			marker.openInfoWindowHtml("<div id='bocadillo'>"+small+"<div id='bocadillo_links'>"+links1+"</div>"+
				"<div id='bocadillo_links2'>"+"</div>"+"</div>");

			var lateral;
			var boton;
			if(evento.marcador == 'event'){
				lateral = document.getElementById('catastrofes');
				boton = 'submit1';
				document.getElementById('eliminar1').style.display = 'inline';
			}else if(evento.marcador == 'people'){
				lateral = document.getElementById('heridos');
				boton = 'submit2';
				document.getElementById('eliminar2').style.display = 'inline';
				var tipo;
				if(evento.tipo == 'healthy'){tipo = 'sano';}
				else if(evento.tipo == 'slight'){tipo = 'leve';}
				else if(evento.tipo == 'serious'){tipo = 'grave';}
				else if(evento.tipo == 'dead'){tipo = 'muerto';}
				else if(evento.tipo == 'trapped'){tipo = 'trapped';}
				document.getElementById('icono_heridos').src = 'markers/' + tipo + '1.png';
				document.getElementById('radioNo').style.display = 'none';
				document.getElementById('radioMod').style.display = 'block';
				document.getElementById('sintomas').style.display = 'block';
			}

			for(i=0; i<5; i++){
				if(lateral.tipo[i].value == evento.tipo){
					lateral.tipo[i].checked = 'checked';
					if(evento.marcador == 'event'){
						cambiaIcono('event',evento.tipo);
					}else if(evento.marcador == 'people'){
						cambiaIcono('people',evento.tipo,1);
					}
				}
			}
			lateral.nombre.value = evento.nombre;
			lateral.info.value = evento.info;
			lateral.descripcion.value = evento.descripcion;
			lateral.direccion.value = evento.direccion;
			lateral.iden.value = evento.id;
			lateral.longitud.value = evento.longitud;
			lateral.latitud.value = evento.latitud;
			if(evento.marcador == 'event'){
				for(i=0; i<4; i++){
					if(lateral.size[i].value == evento.size){
						lateral.size[i].selected = 'selected';
					}
				}
				for(i=0; i<3; i++){
					if(lateral.traffic[i].value == evento.traffic){
						lateral.traffic[i].selected = 'selected';
					}
				}
			}
			document.getElementById(boton+'1').style.display = 'none';
			document.getElementById(boton+'0').style.display = 'inline';

			if(evento.marcador == 'event'){
				showTab('dhtmlgoodies_tabView1_1',0);
			}else if(evento.marcador == 'people'){
				showTab('dhtmlgoodies_tabView1_1',1);
			}
		});

		GEvent.addListener(marker, "dragstart", function() {
			map.closeInfoWindow();
		});

		GEvent.addListener(marker, "dragend", function() {
			//var asociada = asociar(evento.id, evento.marker);
			/*marker.openInfoWindowHtml("<div id=\"bocadillo\">"+"Asociado a cat&aacute;strofe: <br>"+marcadores_definitivos[asociada].nombre
				+ "<br><a id=\"guardar_asociacion\" href=\"#\" onclick=\"guardar_asociacion("+asociada+","+evento.id+"); return false;\" > Guardar </a>"
				+" - "+ "<a id=\"cancelar\" href=\"#\" onclick=\"cancelar_asignacion("+evento.id+"); return false;\" > Cancelar </a>"+"</div>" );*/
			marker.openInfoWindowHtml("<div id=\"bocadillo\">"+"¿Confirmar cambio de posición?<br>"
				+ "<a id=\"confirmar\" href=\"#\" onclick=\"return false;\" > Confirmar </a>"
				+" - "+ "<a id=\"cancelar\" href=\"#\" onclick=\"return false;\" > Cancelar </a>"+"</div>" );
		});

		GEvent.addListener(marker, "infowindowclose", function() {
			var lateral;
			var boton;
			if(evento.marcador == 'event'){
				lateral = document.getElementById('catastrofes');
				boton = 'submit1';
				document.getElementById('eliminar1').style.display = 'none';
			}else if(evento.marcador == 'people'){
				lateral = document.getElementById('heridos');
				boton = 'submit2';
				document.getElementById('eliminar2').style.display = 'none';
				document.getElementById('radioNo').style.display = 'block';
				document.getElementById('radioMod').style.display = 'none';
				document.getElementById('sintomas').style.display = 'none';
			}

			if(evento.marcador == 'event'){
				lateral.tipo[0].checked = 'checked';
				cambiaIcono('event','fire');
			}
			lateral.nombre.value = "";
			lateral.info.value = "";
			lateral.descripcion.value = "";
			lateral.direccion.value = "";
			lateral.iden.value = "";
			lateral.longitud.value = "";
			lateral.latitud.value = "";
			if(evento.marcador == 'event'){
				lateral.size[0].selected = "selected";
				lateral.traffic[0].selected = "selected";
			}
			document.getElementById(boton+'1').style.display = 'block';
			document.getElementById(boton+'0').style.display = 'none';
		});
	}

	//if (!(evento.marcador=="resource" && caracter==1)){
	map.addOverlay(marker);
	//}
	return marker;
}

function crearCatastrofe(marcador,tipo,cantidad,nombre,info,descripcion,direccion,longitud,latitud,estado,size,traffic,idAssigned,
	fatigue,fever,dyspnea,nausea,headache,vomiting,abdominal_pain,weight_loss,blurred_vision,muscle_weakness){

	var sintomas = '';
	if(fatigue) sintomas += 'fatigue,';
	if(fever) sintomas += 'fever,';
	if(dyspnea) sintomas += 'dyspnea,';
	if(nausea) sintomas += 'nausea,';
	if(headache) sintomas += 'headache,';
	if(vomiting) sintomas += 'vomiting,';
	if(abdominal_pain) sintomas += 'abdominal_pain,';
	if(weight_loss) sintomas += 'weight_loss,';
	if(blurred_vision) sintomas += 'blurred_vision,';
	if(muscle_weakness) sintomas += 'muscle_weakness,';

	var nuevomarcador = new ObjMarcador(pos_temp,marcador,tipo,cantidad,nombre,
		descripcion,info,latitud,longitud,direccion,estado,
		obtiene_fecha(), obtiene_fecha(),usuario_actual, usuario_actual_tipo,null,size,traffic,idAssigned,sintomas);

	if(marcador=="event"){
		nuevomarcador.size=size;
		nuevomarcador.traffic=traffic;
	}
	if(marcador=="resource"||marcador=="people"){
		nuevomarcador.idAssigned=idAssigned;
	}

	pos_temp = pos_temp +1;
	nuevomarcador.marker=generaMarcador(nuevomarcador, TEMPORAL);

	guardar(nuevomarcador);
}

function modificar(id,cantidad,nombre,info,descripcion,direccion,longitud,latitud,estado,size,traffic,idAssigned,
	fatigue,fever,dyspnea,nausea,headache,vomiting,abdominal_pain,weight_loss,blurred_vision,muscle_weakness){
	//utiliza la variable puntero_temp accesible por todos y cargada por cargarModificar
	if (caracter_temp==TEMPORAL){
		//Actualizar la matriz temporal

		eliminar(marcadores_temporales[id],TEMPORAL);
		crearCatastrofe(puntero_temp.marcador,puntero_temp.tipo,cantidad,nombre,info,descripcion,direccion,longitud,latitud,estado,size,traffic,idAssigned,
			fatigue,fever,dyspnea,nausea,headache,vomiting,abdominal_pain,weight_loss,blurred_vision,muscle_weakness);

	//"marcadores_temporales[puntero.id].nombre=nombre.value;marcadores_temporales[puntero.id].cantidad=cantidad.value;marcadores_temporales[puntero.id].info=info.value;marcadores_temporales[puntero.id].descripcion=descripcion.value; marcadores_temporales[puntero.id].direccon=direccion.value;marcadores_temporales[puntero.id].latitud=latitud.value;marcadores_temporales[puntero.id].longitud=longitud.value;marcadores_temporales[puntero.id].estado=estado.value;eliminar(marcadores_temporales[puntero.id],TEMPORAL);marcadores_temporales[puntero.id].marker=generaMarcador(marcadores_temporales[puntero.id],TEMPORAL);borrarFormulario(this.form,0);$('#modificar').jqm().jqmHide();return false;";
	}
	
	if (caracter_temp==DEFINITIVO){
		var sintomas = '';
		if(fatigue) sintomas += 'fatigue,';
		if(fever) sintomas += 'fever,';
		if(dyspnea) sintomas += 'dyspnea,';
		if(nausea) sintomas += 'nausea,';
		if(headache) sintomas += 'headache,';
		if(vomiting) sintomas += 'vomiting,';
		if(abdominal_pain) sintomas += 'abdominal_pain,';
		if(weight_loss) sintomas += 'weight_loss,';
		if(blurred_vision) sintomas += 'blurred_vision,';
		if(muscle_weakness) sintomas += 'muscle_weakness,';

		//hay que hacer un update a la base de datos
		$.post('update.jsp',{
			'id':id,
			'marcador':puntero_temp.marcador,
			'tipo':puntero_temp.tipo,
			'cantidad':cantidad,
			'nombre':nombre,
			'descripcion':descripcion,
			'info':info,
			'latitud':latitud,
			'longitud':longitud,
			'direccion':direccion,
			'estado':estado,
			'size':size,
			'traffic':traffic,
			'idAssigned':idAssigned,
			'fecha':puntero_temp.fecha,
			'usuario':usuario_actual,
			'sintomas':sintomas
		});
	//actualizar();
	}
}

function modificar2(id,tipo,cantidad,nombre,info,descripcion,direccion,longitud,latitud,estado,size,traffic){
	var puntero = marcadores_definitivos[id];
	$.post('update.jsp',{
		'id':id,
		'marcador':puntero.marcador,
		'tipo':tipo,
		'cantidad':cantidad,
		'nombre':nombre,
		'descripcion':descripcion,
		'info':info,
		'latitud':puntero.latitud,
		'longitud':puntero.longitud,
		'direccion':direccion,
		'estado':estado,
		'size':size,
		'traffic':traffic,
		'idAssigned':puntero.idAssigned,
		'fecha':puntero.fecha,
		'usuario':usuario_actual,
		'sintomas':puntero.sintomas
	});
}

function modificar3(id,tipo,fatigue,fever,dyspnea,nausea,headache,vomiting,abdominal_pain,weight_loss,blurred_vision,muscle_weakness){
	var puntero = marcadores_definitivos[id];

	var sintomas = '';
	if(fatigue) sintomas += 'fatigue,';
	if(fever) sintomas += 'fever,';
	if(dyspnea) sintomas += 'dyspnea,';
	if(nausea) sintomas += 'nausea,';
	if(headache) sintomas += 'headache,';
	if(vomiting) sintomas += 'vomiting,';
	if(abdominal_pain) sintomas += 'abdominal_pain,';
	if(weight_loss) sintomas += 'weight_loss,';
	if(blurred_vision) sintomas += 'blurred_vision,';
	if(muscle_weakness) sintomas += 'muscle_weakness,';

	//hay que hacer un update a la base de datos
	$.post('update.jsp',{
		'id':id,
		'marcador':puntero.marcador,
		'tipo':tipo,
		'cantidad':puntero.cantidad,
		'nombre':puntero.nombre,
		'descripcion':puntero.descripcion,
		'info':puntero.info,
		'latitud':puntero.latitud,
		'longitud':puntero.longitud,
		'direccion':puntero.direccion,
		'estado':puntero.estado,
		'size':puntero.size,
		'traffic':puntero.traffic,
		'idAssigned':puntero.idAssigned,
		'fecha':puntero.fecha,
		'usuario':usuario_actual,
		'sintomas':sintomas
	});
}

function actuar(idEvento,nombreUsuario,accionAux){
	var accion;
	for(i=0;i<accionAux.length;i++){
		if(accionAux[i].checked){
			accion = accionAux[i].value;
		}
	}
	
	var estadoEvento;
	var estadoUsuario;
	if(accion=='apagar' || accion=='atender' || accion=='evacuar'){
		estadoEvento = 'controlled';
		estadoUsuario = 'acting';
	}else if(accion=='ayudar' || accion=='trasladar' || accion=='evacuado' || accion=='volver'){
		estadoEvento = 'controlled2';
		estadoUsuario = 'acting';
	}else if(accion=='apagado' || accion=='curado'){
		estadoEvento = 'erased';
		estadoUsuario = 'active';
	}else if(accion=='vuelto' || accion=='dejar'){
		estadoEvento = 'active';
		estadoUsuario = 'active';
	}

	$.post('updateEstado.jsp',{
		'idEvento':idEvento,
		'nombreUsuario':nombreUsuario,
		'estadoEvento':estadoEvento,
		'estadoUsuario':estadoUsuario,
		'accion':accion
	});
}

function cargarMenuAcciones(puntero){
	var menu = "<div id=\"acciones\"><form id=\"form_acciones\" name=\"form_acciones\" action=\"#\"><table class=\"tabla_menu\">";
	var titulo = "<tr><th><label for=\"accion\">Acciones a realizar</label></th></tr>";
	var oculto = "<tr style=\"display:none\"><td><input type=\"radio\" name=\"accion\" value=\"\"></td></tr>"; // Sin esto no funciona!!
	var apagar = "<tr id=\"apagar\"><td><input type=\"radio\" name=\"accion\" value=\"apagar\">Atender emergencia</td></tr>";
	var atender = "<tr id=\"atender\"><td><input type=\"radio\" name=\"accion\" value=\"atender\">Atender herido</td></tr>";
	var evacuar = "<tr id=\"evacuar\"><td><input type=\"radio\" name=\"accion\" value=\"evacuar\">Evacuar residentes</td></tr>";
	var ayudar = "<tr id=\"ayudar\"><td><input type=\"radio\" name=\"accion\" value=\"ayudar\">Ayudar (0)</td></tr>";
	var trasladar = "<tr id=\"trasladar\"><td><input type=\"radio\" name=\"accion\" value=\"trasladar\">Trasladar herido</td></tr>";
	var evacuado = "<tr id=\"evacuado\"><td><input type=\"radio\" name=\"accion\" value=\"evacuado\">Fin evacuación</td></tr>";
	var volver = "<tr id=\"volver\"><td><input type=\"radio\" name=\"accion\" value=\"volver\">Volver a la residencia</td></tr>";
	var dejar = "<tr id=\"dejar\"><td><input type=\"radio\" name=\"accion\" value=\"dejar\">Dejar de atender</td></tr>";
	var apagado = "<tr id=\"apagado\"><td><input type=\"radio\" name=\"accion\" value=\"apagado\">Fuego pagado</td></tr>";
	var curado = "<tr id=\"curado\"><td><input type=\"radio\" name=\"accion\" value=\"curado\">Herido curado</td></tr>";
	var vuelto = "<tr id=\"vuelto\"><td><input type=\"radio\" name=\"accion\" value=\"vuelto\">Todos de vuelta</td></tr>";
	var cierre = "</table><br><input type=\"hidden\" id=\"iden2\" name=\"iden2\" value=\"" + puntero.id + "\">";
	var boton = "<input id=\"aceptarAccion\" type=\"button\" value=\"Aceptar\" onclick=\"actuar(iden2.value,'" + nombreUsuario + "',accion)\">";
	
	if(puntero.marcador == 'event'){
		menu += titulo + oculto;
		if(puntero.estado == 'active'){
			menu += apagar;
		}else if(puntero.estado == 'controlled'){
			menu += ayudar + dejar + apagado;
		}
		menu += cierre + boton;
	}else if(puntero.marcador == 'people'){
		menu += titulo;
		if(puntero.tipo == 'healthy'){
			if(puntero.estado == 'active'){
				menu += evacuar;
			}else if(puntero.estado == 'controlled'){
				menu += ayudar + dejar + evacuado + volver + vuelto;
			}
		}else{
			if(puntero.estado == 'active'){
				menu += atender;
			}else if(puntero.estado == 'controlled'){
				menu += ayudar + dejar + trasladar + curado;
			}
		}
		menu += cierre + boton;
	}else{
		menu += "</table>";
	}
	menu += "</form></div>";

	return menu;
}

function guardar(puntero){

	//1. Guardar el elemento en la base de datos
	$.post('guardaEvento.jsp',{
		'marcador':puntero.marcador,
		'tipo':puntero.tipo,
		'cantidad':puntero.cantidad,
		'nombre':puntero.nombre,
		'descripcion':puntero.descripcion,
		'info':puntero.info,
		'latitud':puntero.latitud,
		'longitud':puntero.longitud,
		'direccion':puntero.direccion,
		'estado':puntero.estado,
		'size':puntero.size,
		'traffic':puntero.traffic,
		'idAssigned':puntero.idAssigned,
		'fecha':puntero.fecha,
		'usuario':puntero.nombre_usuario,
		'sintomas':puntero.sintomas
	},
	function(data){
		$('#guardar').innerHTML=data;
	}
	);

	//2. Borrar el elemento del mapa y la matriz temporal
	eliminar(puntero,TEMPORAL);
	marcadores_temporales[puntero.id]=null;

//3. Recargar el mapa para que aparezca el elemento nuevo
//actualizar();//esto adelanta el timeOut a ahora mismo
}

function asociar (id, marker){
	//1. hallar el punto del marcador pasado

	latitud1 = marker.getLatLng().lat();
	longitud1 = marker.getLatLng().lng();

	//latitud1=marcadores_definitivos[id].marker.lat();
	//longitud1=marcadores_definitivos[id].marker.lng();
	//2. hallar la distancia a cada catastrofe de la matriz definitiva

	var i;
	var diferencia=999999999999;
	var id_mas_cercano;

	for (i=0;indices[i]!=null;i++) {

		idDefinitivo=indices[i];
		//nos quedamos solo con los eventos (catastrofes)
		if(marcadores_definitivos[idDefinitivo].marcador!="event"){
			continue;
		}

		latitud2=marcadores_definitivos[idDefinitivo].latitud;
		longitud2=marcadores_definitivos[idDefinitivo].longitud;

		dif_lat=Math.pow(latitud1-latitud2,2);
		dif_long=Math.pow(longitud1-longitud2,2);
		distancia=Math.sqrt(dif_lat+dif_long);
		if (distancia<diferencia){
			diferencia=distancia;
			id_mas_cercano=idDefinitivo;
		}
	}

	//3. Devolver el id de la catastrofe mas cercana o hacer el post directamente
	return id_mas_cercano;
}

function guardar_asociacion (idEvento, idRecurso){
	//alert(idEvento+"-"+idRecurso);
	//Hallar la nueva posicion del recurso en funcion del tipo
	var evento = marcadores_definitivos[idEvento];
	var recurso = marcadores_definitivos[idRecurso];

	latitud = evento.latitud;
	longitud = evento.longitud;

	var nueva_latitud = recurso.latitud;
	var nueva_longitud = recurso.longitud;

	//Hallar las nuevas distancias
	/*
	if(recurso.tipo=="police"){nueva_latitud=latitud+0.00005;nueva_longitud=longitud+0.000025;}
	if(recurso.tipo=="nurse"){nueva_latitud=latitud+0.00005;nueva_longitud=longitud-0.000025;}
	if(recurso.tipo=="gerocultor"){nueva_latitud=latitud+0.00005;nueva_longitud=longitud-0.000025;}
	if(recurso.tipo=="assistant"){nueva_latitud=latitud+0.00005;nueva_longitud=longitud-0.000025;}
	if(recurso.tipo=="firemen"){nueva_latitud=latitud+0.000025;nueva_longitud=longitud-0.00005;}
	if(recurso.tipo=="ambulance" || recurso.tipo=="ambulancia"){nueva_latitud=latitud+0.000025;nueva_longitud=longitud+0.00005;}
	if(recurso.tipo=="trapped"){nueva_latitud=latitud+0.00005;nueva_longitud=longitud-0.0001;}
	if(recurso.tipo=="healthy"){nueva_latitud=latitud+0.00005;nueva_longitud=longitud+0.0001;}
	if(recurso.tipo=="slight"){nueva_latitud=latitud+0.0001;nueva_longitud=longitud-0.00005;}
	if(recurso.tipo=="serious"){nueva_latitud=latitud+0.0001;nueva_longitud=longitud+0.00005;}
	if(recurso.tipo=="dead"){nueva_latitud=latitud+0.00005;nueva_longitud=longitud+0.0001;}
	*/

	//actualizar las modificaciones con el metodo modificar
	caracter_temp=DEFINITIVO;
	puntero_temp=recurso;
	modificar(idRecurso,recurso.cantidad,recurso.nombre,"Asociado a "+evento.nombre+". "+recurso.info,recurso.descripcion,evento.direccion,nueva_longitud,nueva_latitud,recurso.estado,recurso.size,recurso.traffic,idEvento);
}

function cancelar_asignacion(id){
	//borro la posicion actual y lo dibujo en la antigua
	map.removeOverlay(marcadores_definitivos[id].marker);
	generaMarcador(marcadores_definitivos[id], DEFINITIVO);
}

function eliminar(puntero,caracter){
	if (caracter == TEMPORAL){
		map.removeOverlay(puntero.marker);
	}
	if (caracter == DEFINITIVO){
		//hay que hacer un update
		if(puntero.marcador == 'people' && puntero.tipo != 'healthy'){
			$.post('update.jsp',{
				'id':puntero.id,
				'marcador':puntero.marcador,
				'tipo':'healthy',
				'cantidad':puntero.cantidad,
				'nombre':puntero.nombre,
				'descripcion':puntero.descripcion,
				'info':puntero.info,
				'latitud':puntero.latitud,
				'longitud':puntero.longitud,
				'direccion':puntero.direccion,
				'estado':puntero.estado,
				'size':puntero.size,
				'traffic':puntero.traffic,
				'idAssigned':puntero.idAssigned,
				'fecha':puntero.fecha,
				'usuario':usuario_actual
			});
		}else{
			$.post('update.jsp',{
				'id':puntero.id,
				'marcador':puntero.marcador,
				'tipo':puntero.tipo,
				'cantidad':puntero.cantidad,
				'nombre':puntero.nombre,
				'descripcion':puntero.descripcion,
				'info':puntero.info,
				'latitud':puntero.latitud,
				'longitud':puntero.longitud,
				'direccion':puntero.direccion,
				'estado':"erased",
				'size':puntero.size,
				'traffic':puntero.traffic,
				'idAssigned':puntero.idAssigned,
				'fecha':puntero.fecha,
				'usuario':usuario_actual
			});
		}
	//actualizar();
	}
}

function obtiene_fecha() {
	//llega a algo como esto: 1992-01-01 00:00:01 , necesario para MySql a partir de la fecha actual

	var fecha_actual = new Date()  ;

	var dia = fecha_actual.getDate()  ;
	var mes = fecha_actual.getMonth() + 1  ;
	var anio = fecha_actual.getFullYear()  ;
	var horas = fecha_actual.getHours();
	var minutos = fecha_actual.getMinutes();
	var segundos = fecha_actual.getSeconds();

	if (mes < 10)  mes = '0' + mes;
	if (dia < 10)  dia = '0' + dia ;
	if (horas < 10)  horas = '0' + horas ;
	if (minutos < 10)  minutos = '0' + minutos ;
	if (segundos < 10)  segundos = '0' + segundos ;

	return (anio + "-" + mes + "-" + dia + " " + horas+":"+minutos+":"+segundos)  ;
}