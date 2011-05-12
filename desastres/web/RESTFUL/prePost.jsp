<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %> 
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>   
	<head>
		<script type="text/javascript" src="../js/jquery.js"></script>
		<script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAA_pvzu2eEg9OhdvcrhLRkgRSNlkQ0Xctn_2Rk5IICoY4QgDEp3hTh3tolDUaPK2qFjuPWKczd1flPEw" type="text/javascript"></script>

		<script type="text/javascript">
			//Conversion of the possible null value of quantity and idAssigned to integer
         
			<c:choose>
				<c:when test="${param.quantity==null}">var quantity = 1;</c:when>
				<c:otherwise>var quantity=${param.quantity};</c:otherwise>
			</c:choose>

				number = parseInt (quantity);
				if (number == NaN || number == 0){number=1;}

			<c:choose>
				<c:when test="${param.idAssigned==null}">var idAssigned = 0;</c:when>
				<c:otherwise>var idAssigned=${param.idAssigned};</c:otherwise>
			</c:choose>

				var assignment = parseInt (idAssigned);
				if (assignment == NaN ){assignment=0;}

				var item;
				var type;

				//type can't be null

			<c:choose>
				<c:when test="${param.type==null}">type = 0;</c:when>
				<c:otherwise>type="${param.type}";</c:otherwise>
			</c:choose>

				var valid = false;
				if (type=="fire"||type=="flood"||type=="collapse"||type=="lostPerson"||type=="injuredPerson") {item = "event"; valid=true;}
				if (type=="police"||type=="firemen"||type=="ambulance"||type=="ambulancia"||type=="nurse"||type=="gerocultor"||type=="assistant"){item="resource";valid =true;}
				if(type=="slight"||type=="serious"||type=="dead"||type=="healthy"){item="people";valid = true;}

			<c:choose>
				<c:when test="${param.latitud!=null && param.longitud!=null}">
					<c:choose>
						<c:when test="${param.address==null}">var address = "";</c:when>
						<c:otherwise>var address ="${param.address}";</c:otherwise>
					</c:choose>

					$.post('post.jsp',{'item':item,'type':type,
						'quantity':number,'name':"${param.name}",'description':"${param.description}",
						'info':"${param.info}", 'latitud':${param.latitud},'longitud':${param.longitud},
						'address':address,'state':"${param.state}",'size':"${param.size}",'traffic':"${param.traffic}",'idAssigned':assignment,'date':"${param.date}",
						'user':"${param.user}" });
					
				</c:when>
				<c:otherwise>

					var direccion = "${param.address}";
					//alert (direccion);
					localizador = new GClientGeocoder();

					localizador.getLatLng(direccion,function(point) {
						if (!point) {
							$.get('error.jsp',{'action':"address",'address':direccion});
						}
						else{
							$.post('post.jsp',{'item':item,'type':type,
								'quantity':number,'name':"${param.name}",'description':"${param.description}",
								'info':"${param.info}", 'latitud':point.lat(),'longitud':point.lng(),
								'address':"${param.address}",'state':"${param.state}",'size':"${param.size}",'traffic':"${param.traffic}",'idAssigned':assignment,'date':"${param.date}",
								'user':"${param.user}" }, function(data){
									document.getElementById('numero').innerHTML = data;
							});
						}
					});

				</c:otherwise>
			</c:choose>
				
			// if(!valid)window.location = "error.jsp?action=parameter&parameter=type&value="+type;
		</script>
	</head>
	<body>
		<div id="numero"></div>
	</body>
</html>