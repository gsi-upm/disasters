// Javascripts para los formularios

function cambiaIcono(marcador,tipo,cantidad){
	
	if (marcador=="event"){
		//var imagen="markers/fuego.png"
		if (tipo=="fire"){
			imagen="markers/fuego.png";
		}
		if (tipo=="flood"){
			imagen="markers/agua.png";
		}
		if (tipo=="collapse"){
			imagen="markers/casa.png";
		}
		if (tipo=="lostPerson"){
			imagen="markers/personaPerdida.png";
		}
		if (tipo=="injuredPerson"){
			imagen="markers/personaHerida.png";
		}
		document.getElementById("icono_catastrofes").src=imagen;
	}
	if (marcador=="resource"){
		//var imagen="markers/policia1.png";
		if (tipo=="police"){
			imagen="markers/policia"+cantidad+".png";
		}
		if (tipo=="firemen"){
			imagen="markers/bombero"+cantidad+".png";
		}
		if (tipo=="ambulance" || tipo=="ambulancia"){
			imagen="markers/ambulancia"+cantidad+".png";
		}
		if (tipo=="nurse"){
			imagen="markers/enfermero"+cantidad+".png";
		}
		document.getElementById("icono_recursos").src=imagen;
	}
	if (marcador=="people"){
		//var imagen="markers/leve1.png";
		if (tipo=="trapped"){
			imagen="markers/trapped"+cantidad+".png";
		}
		if (tipo=="healthy"){
			imagen="markers/sano"+cantidad+".png";
		}
		if (tipo=="slight"){
			imagen="markers/leve"+cantidad+".png";
		}
		if (tipo=="serious"){
			imagen="markers/grave"+cantidad+".png";
		}
		if (tipo=="dead"){
			imagen="markers/muerto"+cantidad+".png";
		}
		document.getElementById("icono_heridos").src=imagen;
	}	
}
	
function cambiaFlecha(i,numero){
	if (i==0){
		document.getElementById("validardireccion"+numero).src="images/iconos/confirm.png";
	}
	if (i==1){
		document.getElementById("validardireccion"+numero).src="images/iconos/confirm2.png";
	}
} 	
	
function validarDireccion(numero){
	
	direccion = document.getElementById("direccion"+numero).value;
	//alert (direccion);
	
	localizador.getLatLng(direccion,function(point) {
		if (!point) {
			document.getElementById("error_texto").innerHTML="La siguiente direccion no ha podido ser encontrada: <i>"+direccion+"</i>";
			$('#error').jqm().jqmShow();
			//alert(direccion + " not found");
			document.getElementById("validacion"+numero).src="images/iconos/no.png";
		}
		else {
			map.setCenter(point, 15);
			//alert(point);
			document.getElementById("validacion"+numero).src="images/iconos/yes.png";
			document.getElementById("validacion"+numero).alt="Direcci&oacute;n v&aacute;lida";
			document.getElementById("latitud"+numero).value= point.lat();
			document.getElementById("longitud"+numero).value= point.lng();
			//Esto es el puntero provisional
			/*var marker = new GMarker(point);
       		map.addOverlay(marker);
       		marker.openInfoWindowHtml("<b>My house</b> <br> Calle embajadores, 181");*/		
		}
	});
}

function pinchaMapa(numero){
	//var lat;
	//var lng;
	if (numero==0)$('#modificar').jqm().jqmHide();
	var handler = GEvent.addListener(map, 'click', function(overlay,point) {
		document.getElementById("latitud"+numero).value= point.lat();
		document.getElementById("longitud"+numero).value= point.lng();
		if(numero!=0) $('#dialog'+numero).jqm().jqmShow();
		else{
			$('#modificar').jqm().jqmShow();
			document.getElementById("pincha").innerHTML="Posicion guardada. &iquest;Otra vez?";
		}
		//alert("Punto almacenado: "+point.lat()+", "+point.lng());
		GEvent.removeListener(handler);
	} );
}

function iconoAdecuado(marcador,tipo,cantidad){
	var imagen;
	if (marcador=="event"){
		imagen="markers/fuego.png"
		if (tipo=="fire"){
			imagen="markers/fuego.png";
		}
		if (tipo=="flood"){
			imagen="markers/agua.png";
		}
		if (tipo=="collapse"){
			imagen="markers/casa.png";
		}
		if (tipo=="lostPerson"){
			imagen="markers/personaPerdida.png";
		}
		if (tipo=="injuredPerson"){
			imagen="markers/personaHerida.png";
		}
	}
	if (marcador=="resource"){
		imagen="markers/policia1.png";
		if (tipo=="police"){
			imagen="markers/policia"+cantidad+".png";
		}
		if (tipo=="firemen"){
			imagen="markers/bombero"+cantidad+".png";
		}
		if (tipo=="ambulance" || tipo=="ambulancia"){
			imagen="markers/ambulancia"+cantidad+".png";
		}
		if (tipo=="nurse"){
			imagen="markers/enfermero"+cantidad+".png";
		}
	}
	if (marcador=="people"){
		imagen="markers/leve1.png";
		if (tipo=="trapped"){
			imagen="markers/trapped"+cantidad+".png";
		}
		if (tipo=="healthy"){
			imagen="markers/sano"+cantidad+".png";
		}
		if (tipo=="slight"){
			imagen="markers/leve"+cantidad+".png";
		}
		if (tipo=="serious"){
			imagen="markers/grave"+cantidad+".png";
		}
		if (tipo=="dead"){
			imagen="markers/muerto"+cantidad+".png";
		}
	}
	return imagen;
}

function borrarFormulario(form,numero){
	form.nombre.value="nombre";
	form.descripcion.value="descripcion";
	form.info.value="info";
	form.cantidad.value="1";
	form.direccion.value="direccion";
	form.latitud.value=0;
	form.longitud.value=0;
	document.getElementById("validacion"+numero).src="images/iconos/no.png"
}