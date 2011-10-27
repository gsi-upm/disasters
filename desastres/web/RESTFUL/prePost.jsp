<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML>
<html>   
	<head>
		<script type="text/javascript" src="../js/jquery.js"></script>
		<script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAh7S32QoZL_osuspiqG6JHhShGMulApA1qGrWH2FWs8V2HjpzbhR6R94HFuEi6_iz-WDuB-XPDkJ2rA" type="text/javascript"></script>
		<script type="text/javascript">
			//Conversion of the possible null value of quantity and idAssigned to integer
			var quantity;
			<c:choose>
				<c:when test="${param.quantity == null}">
					quantity = 1;
				</c:when>
				<c:otherwise>
					quantity = ${param.quantity};
				</c:otherwise>
			</c:choose>

			var number = parseInt(quantity);
			if(number == NaN || number == 0){
				number=1;
			}

			var idAssigned;
			<c:choose>
				<c:when test="${param.idAssigned == null}">
					idAssigned = 0;
				</c:when>
				<c:otherwise>
					idAssigned = ${param.idAssigned};
				</c:otherwise>
			</c:choose>

			var assignment = parseInt(idAssigned);
			if(assignment == NaN){
				assignment=0;
			}

			var item;
			var type;
			//type can't be null
			<c:choose>
				<c:when test="${param.type == null}">
					type = 0;
				</c:when>
				<c:otherwise>
					type="${param.type}";
				</c:otherwise>
			</c:choose>

			var valid = false;
			if(type=="fire"||type=="flood"||type=="collapse"||type=="lostPerson"||type=="injuredPerson"){
				item = "event";
				valid=true;
			}else if(type=="police"||type=="firemen"||type=="ambulance"||type=="ambulancia"||
					type=="nurse"||type=="gerocultor"||type=="assistant"||type=="otherStaff"||type=="citizen"){
				item="resource";
				valid =true;
			}else if(type=="slight"||type=="serious"||type=="dead"||type=="healthy"){
				item="people";
				valid = true;
			}

			<c:choose>
				<c:when test="${param.latitud != null && param.longitud != null}">
					var address;
					<c:choose>
						<c:when test="${param.address==null}">
							address = "";
						</c:when>
						<c:otherwise>
							address ="${param.address}";
						</c:otherwise>
					</c:choose>

					$.post('post.jsp',{
						'item':item,
						'type':type,
						'quantity':number,
						'name':"${param.name}",
						'description':"${param.description}",
						'info':"${param.info}",
						'latitud':${param.latitud},
						'longitud':${param.longitud},
						'address':address,
						'state':"${param.state}",
						'size':"${param.size}",
						'traffic':"${param.traffic}",
						'idAssigned':assignment,
						'date':"${param.date}",
						'user':"${param.user}"
					});
				</c:when>
				<c:otherwise>
					var direccion = "${param.address}";
					//alert (direccion);
					var localizador = new GClientGeocoder();
					localizador.getLatLng(direccion,function(point){
						if (!point) {
							$.get('error.jsp',{
								'action':"address",
								'address':direccion
							});
						}else{
							$.post('post.jsp',{
								'item':item,
								'type':type,
								'quantity':number,
								'name':"${param.name}",
								'description':"${param.description}",
								'info':"${param.info}",
								'latitud':point.lat(),
								'longitud':point.lng(),
								'address':"${param.address}",
								'state':"${param.state}",
								'size':"${param.size}",
								'traffic':"${param.traffic}",
								'idAssigned':assignment,
								'date':"${param.date}",
								'user':"${param.user}"
							}, function(data){
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