var map;
var center;
var localizador;
var marcadores_definitivos;
var marcadores_temporales;
var indices;
var ultimamodif;
var timeout; 
        
var hospitals;var hospIndex = 0;
var firemenStations; var fireIndex = 0;
var policeStations; var policeIndex = 0;
         
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

var url_base = "/disasters/Xpert";

function initialize() {
    if (GBrowserIsCompatible()) {
			
        map = new GMap2(document.getElementById("map_canvas"));
        localizador = new GClientGeocoder();
	center = new GLatLng(40.416878,-3.703480); //La puerta del sol de Madrid
	map.setCenter(center, 11); 
        
        //empezamos con un usuario anonimo, permitir registrarse más tarde
        usuario_actual=ANONYMOUS;
        usuario_actual_tipo = "citizen";
	map.addControl(new GLargeMapControl()); //controles completos
	//map.addControl(new GSmallMapControl()); // version reducida de los controles
	//map.addControl(new GSmallZoomControl()); //Unicamente botones de +/- zoom	
	//map.addControl(new GScaleControl ()); //Escala
	map.addControl(new GMapTypeControl()); //mapa, foto, hibrido
		
	//inicializamos los arrays
	marcadores_definitivos=new Array;pos_def=0;
	marcadores_temporales=new Array;pos_temp=0;
        hospitals = new Array;
        policeStations = new Array;
        firemenStations = new Array;
         
        indices=new Array; pos_indices=0;
	
        var formBuild = document.getElementById('buildings');
        if(formBuild.hospital.checked)  showHospitals();//para mostrar los edificios
        if(formBuild.firemenStation.checked) showFiremenStations();
        if(formBuild.policeStation.checked) showPoliceStations();
        
        ultimamodif='1992-01-01 00:00:01'; //(una fecha antigua para empezar)
	//hacemos la peticion inicial del json (baja todo menos los borrados)
	$.getJSON('leeEventos.jsp', {'fecha': ultimamodif, 'action':"firstTime"}, function(data) {
            $.each(data, function(entryIndex, entry) {
                var nuevomarcador = new ObjMarcador(entry['id'],entry['item'],entry['type'],
                entry['quantity'],entry['name'],entry['description'],entry['info'],
                entry['latitud'],entry['longitud'],entry['address'],entry['state'],
                entry['date'], entry['modified'],entry['user_name'],entry['user_type'],null);
            
                if (nuevomarcador.item=="event"){
                    nuevomarcador.size = entry['size'];
                    nuevomarcador.traffic = entry['traffic'];
                    nuevomarcador.idAssigned = 0;
                }
                if (nuevomarcador.item=="resource"||nuevomarcador.item=="people"){
                    nuevomarcador.idAssigned = entry['idAssigned'];
                    nuevomarcador.traffic = 0;
                    nuevomarcador.size = 0;
                }

                nuevomarcador.marker=generaMarcador(nuevomarcador, DEFINITIVO);
                marcadores_definitivos[nuevomarcador.id]=nuevomarcador;
                indices[pos_indices]=nuevomarcador.id;
                pos_indices++;
            });
        });
	ultimamodif = obtiene_fecha();
        
        

        setTimeout("moveAgents()",2000);
        setTimeout("actualizar()",tiempoInicial);
    }
}



function actualizar(){
    //cada 10 segundos hacemos la peticion actualizadora de json
    $.getJSON('leeEventos.jsp', {'fecha': ultimamodif,'action':"notFirst"}, function(data) {
        $.each(data, function(entryIndex, entry) {
            //el id lo asigna la base de datos 
            var nuevomarcador = new ObjMarcador(entry['id'],entry['item'],entry['type'],
            entry['quantity'],entry['name'],entry['description'],entry['info'],
            entry['latitud'],entry['longitud'],entry['address'],entry['state'],
            entry['date'], entry['modified'],entry['user_name'],entry['user_type'],null);
            if (nuevomarcador.item=="event"){
                nuevomarcador.size = entry['size'];
                nuevomarcador.traffic = entry['traffic'];
                nuevomarcador.idAssigned = 0;
            }
            if (nuevomarcador.item=="resource"||nuevomarcador.item=="people"){
                nuevomarcador.idAssigned = entry['idAssigned'];
                nuevomarcador.traffic = 0;
                nuevomarcador.size=0;
            }   
                         
            //pintamos los nuevos, para lo que comprobamos que no existian
            if(marcadores_definitivos[nuevomarcador.id]==null){
                //alert("Vamos a pintar un marcador nuevo");
                if(nuevomarcador.estado=='erased'){
                    //alert("Entra por segunda vez");
                }
                else{
                    nuevomarcador.marker=generaMarcador(nuevomarcador, DEFINITIVO);
                    marcadores_definitivos[nuevomarcador.id]=nuevomarcador;
                    indices[pos_indices]=nuevomarcador.id;
                    pos_indices++;
                }
            }
            //actualizamos los que han sido modificados
            else{
                //alert("Vamos a actualizar el marcador "+nuevomarcador.id);
                //si se ha modificado algún dato se actualiza
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
    generateBuilding("hospital","Hospital Gregorio Marañon",40.418702, -3.670573);
    // generateBuilding("hospital","Hospital La Paz",40.480726, -3.685672);
    // generateBuilding("hospital","Hospital 12 de Octubre",40.375594, -3.695653);
         
         
}
        
function showFiremenStations(){
    generateBuilding("firemenStation","Parque de Bomberos",40.414691, -3.706996);
}
        
function showPoliceStations(){
    generateBuilding("policeStation","Comisaría central",40.421565, -3.710095);
}
         
function generateBuilding(type, mensaje, latitud, longitud){
    var imagen;
    var matrix;
    var matrixIndex;
    if(type=='hospital'){
        imagen = "ambulanceStation.png";
        matrix = hospitals;
        matrixIndex = hospIndex;
    }
    if(type=='policeStation'){
        imagen = "policeStation.png";
        matrix = policeStations;
        matrixIndex = policeIndex;
    }
    if(type=='firemenStation'){
        imagen = "firemenStation.png";
        matrix = firemenStations;
        matrixIndex = fireIndex;
    }
             
    var icono = new GIcon();
    icono.image = "markers/"+imagen;
           
    //icono.shadow = "markers/shadow50.png";
    icono.iconSize = new GSize(50, 49);
    //icono.shadowSize = new GSize(43, 43);
    icono.iconAnchor = new GPoint(25, 40);
    icono.infoWindowAnchor = new GPoint(5, 1);
    opciones = { icon:icono}; //se pueden arrastrar para asociarlo		
            
    var marker = new GMarker (new GLatLng(latitud,longitud), opciones);
    GEvent.addListener(marker, "click", function() {
        marker.openInfoWindowHtml("<div id='bocadillo'>"+mensaje+"</div>");	
    });
    map.addOverlay(marker);
    matrix[matrixIndex]=marker;
    matrixIndex++;
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
	}
    for(i=0; i<matrix.length; i++){
        map.removeOverlay(matrix[i]);
    }
	if(type=='hospital'){
		hospitals = matrix;
		hospIndex = matrixIndex;
	}else if(type=='policeStation'){
		policeStations = matrix;
		policeIndex = matrixIndex;
	}else if(type=='firemenStation'){
		firemenStations = matrix;
		fireIndex = matrixIndex;
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
            // map.addOverlay(new EInsert(new GLatLng(evento.latitud, evento.longitud), "images/anifire.gif", new GSize(800,800), 14));
            if(evento.estado=="controlled"){icono.image ="markers/fuego_control.png"}              
        }
        //Inundación
        if (evento.tipo=="flood"){
            icono = new GIcon(G_DEFAULT_ICON);
            icono.image = "markers/agua.png";
            if(evento.estado=="controlled")icono.image ="markers/agua_control.png";
        }
        //derrumbamiento
        if (evento.tipo=="collapse"){
            icono = new GIcon(G_DEFAULT_ICON);
            icono.image = "markers/casa.png";
            if(evento.estado=="controlled")icono.image ="markers/casa_control.png";
        }            
        opciones = { icon:icono }; //No se pueden arrastrar
    }
    //es un recurso
    if (evento.marcador=="resource"){
        //MAXIMO DE RECURSOS POR MARCADOR ES 10
        if (evento.cantidad>10)evento.cantidad=10;
        var icono = new GIcon(G_DEFAULT_ICON);
        //es un policia
        if (evento.tipo=="police"){
            icono.image = "markers/policia"+evento.cantidad+".png";
        }
        //es un bombero
        if (evento.tipo=="firemen"){
            icono.image = "markers/bombero"+evento.cantidad+".png";
        }
        //es una ambulancia
        if (evento.tipo=="ambulance" || evento.tipo=="ambulancia"){
            icono.image = "markers/ambulancia"+evento.cantidad+".png";
        }	
        opciones = { icon:icono, draggable: true }; //Para que se pueda arrastrar
    }
    //es una víctima
    if (evento.marcador=="people"){
        var icono = new GIcon();
			
        //MAXIMO DE VICTIMAS POR MARCADOR ES 10
        if (evento.cantidad>10)evento.cantidad=10;
                         
        //personas atrapadas
        if (evento.tipo=="trapped"){
            icono.image = "markers/trapped"+evento.cantidad+".png";
            //falta hacer este icono
            if(evento.estado=="controlled")icono.image = "markers/trapped1.png";
        }
        //heridos leves
        if (evento.tipo=="slight"){
            icono.image = "markers/leve"+evento.cantidad+".png";
            if(evento.estado=="controlled")icono.image = "markers/slight_control.png";
        }
        //heridos graves
        if (evento.tipo=="serious"){
            icono.image = "markers/grave"+evento.cantidad+".png";
            if(evento.estado=="controlled")icono.image = "markers/serious_control.png";
        }
        //muertos
        if (evento.tipo=="dead"){
            icono.image = "markers/muerto"+evento.cantidad+".png";
            if(evento.estado=="controlled")icono.image = "markers/dead_control.png";
        }
        icono.shadow = "markers/shadow50.png";
        icono.iconSize = new GSize(28, 43);
        icono.shadowSize = new GSize(43, 43);
        icono.iconAnchor = new GPoint(10, 40);
        icono.infoWindowAnchor = new GPoint(5, 1);
        opciones = { icon:icono, draggable: true }; //se pueden arrastrar para asociarlo		
    }
			
			
    var marker = new GMarker (new GLatLng(evento.latitud, evento.longitud), opciones);
		
    //Añadimos el comportamiento
		
    if( caracter==TEMPORAL){//aqui hay que guardar los datos
		
        var content = evento.nombre +"<br>" + evento.info + "<br>" +evento.descripcion + "<br>" 
            + "<a id=\"guardar\" href=\"#\" onclick=\"guardar(marcadores_temporales["+evento.id+"]); return false;\" > Guardar </a>"
            +" - "+ "<a id=\"modificar\" href=\"#\" onclick=\"cargarModificar(marcadores_temporales["+evento.id+"],TEMPORAL); return false;\" > Modificar </a>"
            +" - "+ "<a id=\"eliminar\" href=\"#\" onclick=\"eliminar(marcadores_temporales["+evento.id+"],TEMPORAL); return false;\" > Eliminar </a>";
        //marker.openInfoWindowHtml("<div id=\"bocadillo\">"+content+"</div>");//el globito se abre solo pero esto no lo consigo
		
        GEvent.addListener(marker, "click", function() {
            // content = evento.nombre + "<a href=\"#\" onclick=\"guardar(marcadores_temporales["+evento.id+"]); return false;\" > Guardar </a>";
			
			
			
            marker.openInfoWindowHtml("<div id=\"bocadillo\">"+content+"</div>");		});
		
        GEvent.addListener(marker, "dragstart", function() {map.closeInfoWindow();  });
        GEvent.addListener(marker, "dragend", function() {
            var asociada = asociar(evento.id, evento.marker);
            marker.openInfoWindowHtml("<div id=\"bocadillo\">"+"Es necesario guardar para poder asociar recursos. <br>"+marcadores_definitivos[asociada].nombre
                + "<br><a id=\"guardar_asociacion\" href=\"#\" onclick=\"guardar(marcadores_temporales["+evento.id+"]); return false;\" > Guardar </a>"
                +" - "+ "<a id=\"cancelar\" href=\"#\" onclick=\"map.closeInfoWindow();\" > Cancelar </a>"+"</div>"  ); 
        });
		
		
    }
		
    if( caracter==DEFINITIVO){ //aqui podemos realizar modificaciones a los ya existentes
        GEvent.addListener(marker, "click", function() {
                         
                         
            var small = evento.nombre +"<br>" + evento.descripcion ;
            //AQUI SE PUEDEN AUMENTAR LAS OPCIONES A MOSTRAR!!!
            var links1 = "<a id=\"modificar\" href=\"#\" onclick=\"cargarModificar(marcadores_definitivos["+evento.id+"],DEFINITIVO); return false;\" > Modificar </a>"
                +" - "+ "<a id=\"eliminar\" href=\"#\" onclick=\"eliminar(marcadores_definitivos["+evento.id+"],DEFINITIVO); return false;\" > Eliminar </a>"
                +" - "+ "<a id='ver_mas1' href='#'onclick=\"verMas("+evento.id+");return false;\"  > Ver m&aacute;s </a>";
                            
                         
                         
                         
			
            marker.openInfoWindowHtml("<div id='bocadillo'>"+small+"<div id='bocadillo_links'>"+links1+"</div>"+
                "<div id='bocadillo_links2'>"+"</div>"+"</div>");	
                        
                         
        });
                        
                        
        GEvent.addListener(marker, "dragstart", function() {map.closeInfoWindow();  });
        GEvent.addListener(marker, "dragend", function() {
            var asociada = asociar(evento.id, evento.marker);
            marker.openInfoWindowHtml("<div id=\"bocadillo\">"+"Asociado a catástrofe: <br>"+marcadores_definitivos[asociada].nombre
                + "<br><a id=\"guardar_asociacion\" href=\"#\" onclick=\"guardar_asociacion("+asociada+","+evento.id+"); return false;\" > Guardar </a>"
                +" - "+ "<a id=\"cancelar\" href=\"#\" onclick=\"cancelar_asignacion("+evento.id+"); return false;\" > Cancelar </a>"+"</div>" ); 
        });
		
    }
    
    //if (!(evento.marcador=="resource" && caracter==1)){
        map.addOverlay(marker);
    //}
    return marker;
}


function verMas(id){
    var evento = marcadores_definitivos[id];
    var complete = evento.nombre +"<br>" + evento.info + "<br>" +evento.descripcion + "<br>" + "Direccion: " + evento.direccion +"<br>";
    var links2 ="<a id=\"modificar\" href=\"#\" onclick=\"cargarModificar(marcadores_definitivos["+evento.id+"],DEFINITIVO); return false;\" > Modificar </a>"
        +" - "+ "<a id=\"eliminar\" href=\"#\" onclick=\"eliminar(marcadores_definitivos["+evento.id+"],DEFINITIVO); return false;\" > Eliminar </a>"
        +" - "+ "<a id='ver_mas2' href='#' onclick=\"verMenos("+evento.id+");return false;\" > Ver menos </a>";
                             
     
    marcadores_definitivos[id].marker.openInfoWindowHtml("<div id='bocadillo'>"+complete+"<div id='bocadillo_links'>"+links2+"</div>"+"</div>");

}

function verMenos(id){
    var evento = marcadores_definitivos[id];
    var small = evento.nombre +"<br>" + evento.descripcion ;
    var links1 ="<a id=\"modificar\" href=\"#\" onclick=\"cargarModificar(marcadores_definitivos["+evento.id+"],DEFINITIVO); return false;\" > Modificar </a>"
        +" - "+ "<a id=\"eliminar\" href=\"#\" onclick=\"eliminar(marcadores_definitivos["+evento.id+"],DEFINITIVO); return false;\" > Eliminar </a>"
        +" - "+ "<a id='ver_mas1' href='#' onclick=\"verMas("+evento.id+");return false;\" > Ver m&aacute;s </a>";
                             
     
    marcadores_definitivos[id].marker.openInfoWindowHtml("<div id='bocadillo'>"+small+"<div id='bocadillo_links'>"+links1+"</div>"+"</div>");

}

function crearCatastrofe(marcador,tipo,cantidad,nombre,info,descripcion,direccion,longitud,latitud,estado,size, traffic, idAssigned){
	
    var nuevomarcador = new ObjMarcador(pos_temp,marcador,tipo,cantidad,nombre,
    descripcion,info,latitud,longitud,direccion,estado,
    obtiene_fecha(), obtiene_fecha(),usuario_actual, usuario_actual_tipo,null);
        
    if(marcador=="event"){
        nuevomarcador.size=size;
        nuevomarcador.traffic=traffic;
    }
    if(marcador=="resource"||marcador=="people"){
        nuevomarcador.idAssigned=idAssigned;
            
    }
        
    pos_temp = pos_temp +1;
    nuevomarcador.marker=generaMarcador(nuevomarcador, TEMPORAL);
    marcadores_temporales[nuevomarcador.id]=nuevomarcador;
	
}

function modificar(id,cantidad,nombre,info,descripcion,direccion,longitud,latitud,estado,size,traffic,idAssigned){
    //utiliza la variable puntero_temp accesible por todos y cargada por cargarModificar
    if (caracter_temp==TEMPORAL){
        //Actualizar la matriz temporal
    
        eliminar(marcadores_temporales[id],TEMPORAL);
        crearCatastrofe(puntero_temp.marcador,puntero_temp.tipo,cantidad,nombre,info,descripcion,direccion,longitud,latitud,estado,size,traffic,idAssigned); 
             
        //"marcadores_temporales[puntero.id].nombre=nombre.value;marcadores_temporales[puntero.id].cantidad=cantidad.value;marcadores_temporales[puntero.id].info=info.value;marcadores_temporales[puntero.id].descripcion=descripcion.value; marcadores_temporales[puntero.id].direccon=direccion.value;marcadores_temporales[puntero.id].latitud=latitud.value;marcadores_temporales[puntero.id].longitud=longitud.value;marcadores_temporales[puntero.id].estado=estado.value;eliminar(marcadores_temporales[puntero.id],TEMPORAL);marcadores_temporales[puntero.id].marker=generaMarcador(marcadores_temporales[puntero.id],TEMPORAL);borrarFormulario(this.form,0);$('#modificar').jqm().jqmHide();return false;";
    
    }
    if (caracter_temp==DEFINITIVO){
        //hay que hacer un update a la base de datos
        $.post('update.jsp',{'id':id,'marcador':puntero_temp.marcador,'tipo':puntero_temp.tipo,
            'cantidad':cantidad,'nombre':nombre,'descripcion':descripcion,
            'info':info, 'latitud':latitud,'longitud':longitud,
            'direccion':direccion,'estado':estado,'size':size,'traffic':traffic,'idAssigned':idAssigned,'fecha':puntero_temp.fecha,
            'usuario':usuario_actual });
        //actualizar();
        
    

    }

}

function cargarModificar(puntero,caracter){
    //mostrar la ventanita
    //carga el evento a modificar en una variabla accesible por todos
    puntero_temp=puntero;
    caracter_temp=caracter;
    if(puntero.marcador=='event'){
        document.getElementById('quantity').style.display='none';
        document.getElementById('size-traffic').style.display='block';
    }
    //else{document.getElementById('quantity').style.visibility='visible';}
    else{
        document.getElementById('quantity').style.display='inline';
        document.getElementById('size-traffic').style.display='none';
       
        if(puntero.idAssigned!= 0){
            document.getElementById('asociacion').innerHTML="Asociado a "+marcadores_temporales[puntero.idAssigned].nombre;
       
        }
    }
    $('#modificar').jqm().jqmShow();
    //rellenamos con los campos recibidos
    
    document.getElementById('iden').value=puntero.id;
    document.getElementById('item_tipo').innerHTML=puntero.marcador+" - "+puntero.tipo;
    document.getElementById('iconoAdecuado').src=iconoAdecuado(puntero.marcador,puntero.tipo,puntero.cantidad);
    document.getElementById('cantidad').value=puntero.cantidad;
    document.getElementById('nombre').value=puntero.nombre;
    document.getElementById('info').value=puntero.info;
    document.getElementById('descripcion').value=puntero.descripcion;
    document.getElementById('direccion0').value=puntero.direccion;
    document.getElementById('latitud0').value=puntero.latitud;
    document.getElementById('longitud0').value=puntero.longitud;
    document.getElementById('estado').value=puntero.estado;
    document.getElementById('size').value=puntero.size;
    document.getElementById('traffic').value=puntero.traffic;
    document.getElementById('idAssigned').value=puntero.idAssigned;
    document.getElementById("validacion0").src="images/iconos/no.png";
    //añadir las que faltan....
    document.getElementById("pincha").innerHTML="Marcar posicion en el mapa";
    
}

function guardar(puntero){
	
    //1. Guardar el elemento en la base de datos
    $.post('guardaEvento.jsp',{'marcador':puntero.marcador,'tipo':puntero.tipo,
        'cantidad':puntero.cantidad,'nombre':puntero.nombre,'descripcion':puntero.descripcion,
        'info':puntero.info, 'latitud':puntero.latitud,'longitud':puntero.longitud,
        'direccion':puntero.direccion,'estado':puntero.estado,'size':puntero.size,'traffic':puntero.traffic,'idAssigned':puntero.idAssigned,
        'fecha':puntero.fecha,'usuario':puntero.nombre_usuario },
    function(data){ $('#guardar').innerHTML=data; }
);
        
    //2. Borrar el elemento del mapa y la matriz temporal
    eliminar(puntero,TEMPORAL);
    marcadores_temporales[puntero.id]=null;
	
    //3. Recargar el mapa para que aparezca el elemento nuevo
    //actualizar();//esto adelanta el timeOut a ahora mismo
            
}
	
function asociar (id, marker){
    //1. hallar el punto del marcador pasado
    
    latitud1=marker.getLatLng().lat();
    longitud1=marker.getLatLng().lng();
    
    //latitud1=marcadores_definitivos[id].marker.lat();
    //longitud1=marcadores_definitivos[id].marker.lng();
    //2. hallar la distancia a cada catastrofe de la matriz definitiva
    
    var i;
    var diferencia=999999999999;
    var id_mas_cercano;
    
    for (i=0;indices[i]!=null;i++) {
       
        idDefinitivo=indices[i];
        //nos quedamos solo con los eventos (catastrofes)
        if(marcadores_definitivos[idDefinitivo].marcador!="event"){continue;}
      
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
    
    latitud=evento.latitud;
    longitud=evento.longitud;
    // alert(latitud+" - "+longitud);
    var nueva_latitud;
    var nueva_longitud;
    
    //Hallar las nuevas distancias
    if(recurso.tipo=="police"){
        nueva_latitud=latitud+0.0005;
        nueva_longitud=longitud;
    }
    if(recurso.tipo=="firemen"){
        nueva_latitud=latitud+0.00025;
        nueva_longitud=longitud-0.0005;
    }
    if(recurso.tipo=="ambulance"){
        nueva_latitud=latitud+0.00025;
        nueva_longitud=longitud+0.0005;
    }
    if(recurso.tipo=="trapped"){
        nueva_latitud=latitud+0.0005;
        nueva_longitud=longitud-0.001;
    }
    if(recurso.tipo=="slight"){
        nueva_latitud=latitud+0.001;
        nueva_longitud=longitud-0.0005;
    }
    if(recurso.tipo=="serious"){
        nueva_latitud=latitud+0.001;
        nueva_longitud=longitud+0.0005;
    }
    if(recurso.tipo=="dead"){
        nueva_latitud=latitud+0.0005;
        nueva_longitud=longitud+0.001;
    }
    
   
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
    if (caracter==TEMPORAL){
        map.removeOverlay(puntero.marker);
    }
    if (caracter==DEFINITIVO){
        //hay que hacer un update
        $.post('update.jsp',{'id':puntero.id,'marcador':puntero.marcador,'tipo':puntero.tipo,
            'cantidad':puntero.cantidad,'nombre':puntero.nombre,'descripcion':puntero.descripcion,
            'info':puntero.info, 'latitud':puntero.latitud,'longitud':puntero.longitud,
            'direccion':puntero.direccion,'estado':"erased",'size':puntero.size,'traffic':puntero.traffic,'idAssigned':puntero.idAssigned,'fecha':puntero.fecha,
            'usuario':usuario_actual });
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
 
 
function lanzaExperto(){
    $.get(url_base,{'action':"start"}, function(data) {
        // alert(data);
        $('#screen').html(data);});
         
    $('#close_screen').click(function(){
        $('#screen').slideUp();
        $('#close_screen').slideUp();
        return false;
    });
         
                  
    $('#screen').slideDown();
    $('#close_screen').slideDown();

    estado_Experto = "on";
    //llama a avisaExperto en 1 segundo
    //   $.timer(1000, avisaExperto());

    

}

function avisaExperto(){
    if(estado_Experto == "on"){
          
        $.get(url_base,{'action':"call"}, function(data) {
            alert(data);
            $('#screen').append(data);});
          
          
          
        //en 4 segundos llamar a avisaExperto
        // $.timer(4000, avisaExperto());
    }
}

function stopExperto(){
    $.get(url_base,{'action':"stop"},function(data){
        
        estado_Experto = "off";
        
        
        //alert(data);
        $('#screen').append(data);

    });
}




