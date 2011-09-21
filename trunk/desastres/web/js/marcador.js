function ObjMarcador(id,marcador,tipo,cantidad,nombre,descripcion,info,latitud,longitud,direccion,estado,
		fecha,modificado,nombre_usuario,tipo_usuario,marker,size,traffic,idAssigned,planta,sintomas){
	//variables privadas
	this.id=id;
	this.marcador=marcador;
	this.tipo=tipo;
	this.cantidad=cantidad;
	this.nombre=nombre;
	this.descripcion=descripcion;
	this.info=info;
	this.latitud=latitud;
	this.longitud=longitud;
	this.direccion=direccion;
	this.estado=estado;
	this.fecha=fecha;
	this.modificado=modificado;
	this.nombre_usuario=nombre_usuario;
	this.tipo_usuario=tipo_usuario;
	this.marker=marker;
	this.size=size;
	this.traffic=traffic;
	this.idAssigned=idAssigned;
	this.planta=planta
	this.sintomas=sintomas;
	/*marker.marker_num = 3;
	marker.tabs = ['One', 'Two', 'Three'];*/
	//metodos
}

//metodo guardar

/*
var gmarkers = [];
 // ==================================================
      // A function to create a tabbed marker and set up the event window
      // This version accepts a variable number of tabs, passed in the arrays htmls[] and labels[]
      function createTabbedMarker(point,label,tabs,opciones) {
        var marker = new GMarker(point,opciones);
        var marker_num = gmarkers.length;
        marker.marker_num = marker_num;
        marker.tabs = tabs;
        gmarkers[marker_num] = marker;
        
        GEvent.addListener(gmarkers[marker_num], 'click', function(){
          marker.openInfoWindowTabsHtml(gmarkers[marker_num].tabs);
        });
        // add a line to the sidebar html
        //sidebar_html += '<a href="javascript:myclick(' + marker_num + ')">' + label + '</a><br/>';
        //return marker;
      }
      // ==================================================*/