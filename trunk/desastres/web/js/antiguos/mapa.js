	var map;
	var center;
	var localizador;
	var marcadores_definitivos;
	var marcadores_temporales;
	
	var pos_def;
	var pos_temp;
	
    function initialize() {
      if (GBrowserIsCompatible()) {
        map = new GMap2(document.getElementById("map_canvas"));
        localizador = new GClientGeocoder();
		center = new GLatLng(40.416878,-3.703480); //La puerta del sol de Madrid
		map.setCenter(center, 11); 


		//map.addControl(new GLargeMapControl()); //controles completos
		map.addControl(new GSmallMapControl()); // version reducida de los controles
		//map.addControl(new GSmallZoomControl()); //Unicamente botones de +/- zoom	
		//map.addControl(new GScaleControl ()); //Escala
		//map.addControl(new GMapTypeControl()); //mapa, foto, hibrido
		
		//inicializamos los arrays
		marcadores_definitivos=new Array;
		pos_def=0;
		marcadores_temporales=new Array;
		pos_temp=0;
		var ultimamodif='1992-01-01 00:00:01'; //(una fecha antigua para empezar)
		//hacemos la peticion inicial del json (baja todo menos los borrados)
		
		$.getJSON('leeEventos.jsp', {'fecha': ultimamodif}, function(data) { 
                                
					                             
					$.each(data, function(entryIndex, entry) {
                    	//el id lo asigna la base de datos 
						var nuevomarcador = new ObjMarcador(entry['id'],entry['marcador'],entry['tipo'],entry['cantidad'],entry[			'nombre'],entry['descripcion'],entry['info'],entry['latitud'],entry['longitud'],entry['direccion'],entry['estado'],entry['fecha'], entry['modificado'],null);
						
						nuevomarcador.marker=generaMarcador(nuevomarcador);
						marcadores_definitivos[nuevomarcador.id]=nuevomarcador;
						//alert(entry['id'] + entry['nombre'] + entry['latitud'] + entry['longitud']); //esto funciona
                                                
						});
                                        
				});
		
		
		//cada 10 segundos hacemos la peticion actualizadora de json
		
		}
	}
	
	
	 
	 
	 
	//Recursos:   0 -> Policia   1 -> Ambulancia  2-> Bombero
	function addRecurso(tipo, numero){
		
		//alert("imprimo:"+tipo+", "+numero);//Traza para ver como funciona el paso de parametros
		
		
		
		
		var icono = new GIcon(G_DEFAULT_ICON);
		//Policia
		if (tipo==0){
			icono.image = "markers/policia"+numero+".png";
		
		}
		//Ambulancia
		if (tipo==1){
			icono.image = "markers/ambulancia"+numero+".png";
		}
		//Bombero
		if (tipo==2){
			icono.image = "markers/bombero"+numero+".png";
		
		}
		var point = map.getCenter();
		var opciones = { icon:icono, draggable: true }; //Para que se pueda arrastrar
		//esto es lo que funcionaba!!
		var marker = new GMarker (point, opciones);
		
		 
		
		//el id lo asignara la base de datos cuando se guarde...)
		var nuevomarcador = new ObjMarcador(pos_temp,1,tipo,numero,"Unidad numero..."+numero,"descripcion","info",0,0,"direccion",0,'2001-01-01 00:00:01', '2001-01-01 00:00:01',marker);
		
		var i=nuevomarcador.id; //por comodidad, se usa muchas veces
		
		marcadores_temporales[i]=nuevomarcador; //metemos el marcador en el array
		pos_temp+=1;
		
		//marcadores_temporales[i].marker.referencia=marcadores_temporales[i];
		// asi cada marker sabe a que objeto marcador pertenece
		
		GEvent.addListener(marcadores_temporales[i].marker, "click", function() {
			
			var content = marcadores_temporales[i].nombre;
			         	
			//enlace.onclick = function() { map.removeOverlay(marcador); map.closeInfoWindow(); return false; }; 
			
			//content.style.visibility = 'visible';
			
			//var tabs = ["one","two","three","Four"];
		//marcadores_temporales[i].marker.openInfoWindowTabsHtml(tabs);
			
			marcadores_temporales[i].marker.openInfoWindowHtml("<div id=\"bocadillo\">"+content+"</div>");
			
			});// Aqui poner foto de policia y un nombre o tarea y posibilidad de eliminar o guardar
			
				
		GEvent.addListener(marcadores_temporales[i].marker, "dragstart", function() {map.closeInfoWindow();  });
		//al arrastrarlo se cierra el bocadillo
		GEvent.addListener(marcadores_temporales[i].marker, "dragend", function() {marcadores_temporales[i].marker.openInfoWindowHtml("<div id=\"bocadillo\">"+"Nuevo destino..."+"</div>"); }); //al soltarlo bota y se abre un nuevo bocadillo donde permite guardarlo. Cuando se guarda hay que: Mandarlo a la base de datos. Pedir los actualizados (solo el que acabamos de hacer), eliminar el recien creado y crear el de la base de datos (de otra forma aparecería 2 veces).
			map.addOverlay(marcadores_temporales[i].marker);
	}
	
 	//function generarBotonBorrar(marcador){
		//map.closeInfoWindow();
		//map.removeOverlay(marcador); 
		
	//}
	
	
	
	
	
	function addCatastrofe(tipo, info){
		//alert ("Recibo: "+tipo+", "+info); //log para ver que los parametros llegan bien
		var icono;
		
		//Incendio
		if (tipo==0){
			icono = new GIcon(G_DEFAULT_ICON);
			icono.image = "markers/fuego.png";
		
		}
		//Inundacion
		if (tipo==1){
			icono = new GIcon(G_DEFAULT_ICON);
			icono.image = "markers/agua.png";
		}
		//Derrumbamiento
		if (tipo==2){
			icono = new GIcon();
			icono.image = "markers/casa.png";
			icono.shadow = "markers/shadow50.png";
			icono.iconSize = new GSize(34, 34);
			icono.shadowSize = new GSize(37, 34);
			icono.iconAnchor = new GPoint(6, 20);
			icono.infoWindowAnchor = new GPoint(5, 1);
		
		}
		var opciones = {icon:icono};
		var point = map.getCenter();
		var marcador = new GMarker (point, opciones);
		GEvent.addListener(marcador, "click", function() { 
			marcador.openInfoWindowHtml("hola");
		} );
		map.addOverlay(marcador);	

	}
	
	
	//Personas:   0 -> Heridos leves   1 -> Heridos graves  2-> Muertos
	function addPersona(tipo, numero, info){
		
		//alert("imprimo:"+tipo+", "+numero);//Traza para ver como funciona el paso de parametros
		
		var icono = new GIcon();
		
		
		
		
		//Heridos leves
		if (tipo==0){
			icono.image = "markers/leve"+numero+".png";
		
		}
		//Heridos graves
		if (tipo==1){
			icono.image = "markers/grave"+numero+".png";
		}
		//Muertos
		if (tipo==2){
			icono.image = "markers/muerto"+numero+".png";
		
		}
		
		icono.shadow = "markers/shadow50.png";
		icono.iconSize = new GSize(28, 43);
		icono.shadowSize = new GSize(43, 43);
		icono.iconAnchor = new GPoint(6, 20);
		icono.infoWindowAnchor = new GPoint(5, 1);
		
		
		var point = map.getCenter();
		var opciones = { icon:icono }; //Para que se pueda arrastrar
		var marcador = new GMarker (point, opciones);
		GEvent.addListener(marcador, "click", function() {
			
			var content = info;
         	
			//enlace.onclick = function() { map.removeOverlay(marcador); map.closeInfoWindow(); return false; }; 
			//content.style.display = 'block';
			
			
			
			
			marcador.openInfoWindowHtml(content);
			
			});// Aqui poner foto de policia y un nombre o tarea y posibilidad de eliminar o guardar
			
		
		
					map.addOverlay(marcador);
	}
	
 	//function generarBotonBorrar(marcador){
		//map.closeInfoWindow();
		//map.removeOverlay(marcador); 
		
	//}
	
	
	function generaMarcador(evento){
		
		var opciones;
		
		
		//Es un evento
		if (evento.marcador==0){
			//Incendio
			if (evento.tipo==0){
				icono = new GIcon(G_DEFAULT_ICON);
				icono.image = "markers/fuego.png";
			}
			//Inundación
			if (evento.tipo==1){
				icono = new GIcon(G_DEFAULT_ICON);
				icono.image = "markers/agua.png";
			}
			//derrumbamiento
			if (evento.tipo==2){
				icono = new GIcon();
				icono.image = "markers/casa.png";
				icono.shadow = "markers/shadow50.png";
				icono.iconSize = new GSize(34, 34);
				icono.shadowSize = new GSize(37, 34);
				icono.iconAnchor = new GPoint(6, 20);
				icono.infoWindowAnchor = new GPoint(5, 1);
				}
			opciones = { icon:icono }; //No se pueden arrastrar
			
			}
		//es un recurso
		if (evento.marcador==1){
			var icono = new GIcon(G_DEFAULT_ICON);
			//es un policia
			if (evento.tipo==0){
				icono.image = "markers/policia"+evento.cantidad+".png";
				}
			//es una ambulancia
			if (evento.tipo==1){
				icono.image = "markers/ambulancia"+evento.cantidad+".png";
				}
			//es un bombero
			if (evento.tipo==2){
				icono.image = "markers/bombero"+evento.cantidad+".png";
				}
				
			opciones = { icon:icono, draggable: true }; //Para que se pueda arrastrar
		
			
				
			}
		//es una víctima
		if (evento.marcador==2){
			var icono = new GIcon();
			//heridos leves
			if (evento.tipo==0){
				icono.image = "markers/leve"+evento.cantidad+".png";
				}
			//heridos graves
			if (evento.tipo==1){
				icono.image = "markers/grave"+evento.cantidad+".png";
				}
			//muertos
			if (evento.tipo==2){
				icono.image = "markers/muerto"+evento.cantidad+".png";
				}
			icono.shadow = "markers/shadow50.png";
			icono.iconSize = new GSize(28, 43);
			icono.shadowSize = new GSize(43, 43);
			icono.iconAnchor = new GPoint(6, 20);
			icono.infoWindowAnchor = new GPoint(5, 1);
			opciones = { icon:icono }; //No se pueden arrastrar		
			}
			
			
		var marker = new GMarker (new GLatLng(evento.latitud, evento.longitud), opciones);
		
		//Añadimos el comportamiento
		GEvent.addListener(marker, "click", function() {
			var content = evento.nombre;
			marker.openInfoWindowHtml("<div id=\"bocadillo\">"+content+"</div>");
		});
		GEvent.addListener(marker, "dragstart", function() {map.closeInfoWindow();  });
		GEvent.addListener(marker, "dragend", function() {marker.openInfoWindowHtml("<div id=\"bocadillo\">"+"Nuevo destino..."+"</div>"); });
		
		
		map.addOverlay(marker);				
		return marker;
		}
	
	function addMarcadores() {
        //Add 10 markers to the map at random locations
        
		// Creamos el icono del INCENDIO
		var iconoFuego = new GIcon(G_DEFAULT_ICON);
		iconoFuego.image = "markers/fuego.png";
		
		//Creamos el icono de INUNDACION
		var iconoAgua = new GIcon(G_DEFAULT_ICON);
		iconoAgua.image = "markers/agua.png";
		// Set up our GMarkerOptions object
		opcionesFuego = { icon:iconoFuego };
		opcionesAgua = { icon:iconoAgua };
		
		//Creamos el icono de TERREMOTO
		
		var iconoCasa = new GIcon();
		iconoCasa.image = "markers/casa.png";
		iconoCasa.shadow = "markers/shadow50.png";
		iconoCasa.iconSize = new GSize(34, 34);
		iconoCasa.shadowSize = new GSize(37, 34);
		iconoCasa.iconAnchor = new GPoint(6, 20);
		iconoCasa.infoWindowAnchor = new GPoint(5, 1);

		// Set up our GMarkerOptions object literal
		opcionesCasa = { icon:iconoCasa };

		
		
		
		
		
		
		var bounds = map.getBounds();
        var southWest = bounds.getSouthWest();
        var northEast = bounds.getNorthEast();
        var lngSpan = northEast.lng() - southWest.lng();
        var latSpan = northEast.lat() - southWest.lat();
        for (var i = 0; i < 5; i++) {
          var point = new GLatLng(southWest.lat() + latSpan * Math.random(),
                                  southWest.lng() + lngSpan * Math.random());
          map.addOverlay(new GMarker(point, opcionesFuego));
        }
		
		for (var i = 0; i < 5; i++) {
          var point = new GLatLng(southWest.lat() + latSpan * Math.random(),
                                  southWest.lng() + lngSpan * Math.random());
          //marcadores[puntero] = new GMarker(point, opcionesAgua);
		  var gota =  new GMarker(point, opcionesAgua);
		  GEvent.addListener(gota, "click", function() {gota.openInfoWindowHtml("Policia 321");  });
		  map.addOverlay(gota);
		  //map.addOverlay(new GMarker(point, opcionesAgua));
		  puntero=puntero+1;
		  
        }
		for (var i = 0; i < 5; i++) {
          var point = new GLatLng(southWest.lat() + latSpan * Math.random(),
                                  southWest.lng() + lngSpan * Math.random());
          map.addOverlay(new GMarker(point, opcionesCasa));
        }

      
    }



