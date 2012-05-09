<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%--<jsp:useBean class="roads.DirectionsBean" id="recursos" scope="session"/>--%>
<jsp:useBean class="gsi.project.UserBean" id="usuario" scope="session"/>
<jsp:setProperty name="usuario" property="nombre" value="<%= request.getRemoteUser() %>"/>

<c:if test="${param.lang != null}">
	<fmt:setLocale value="${param.lang}" scope="session"/>
</c:if>

<!DOCTYPE HTML>
<html>
	<fmt:bundle basename="fmt.eji8n">
		<head>
			<meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
			<title>Caronte</title>
			<link type="image/vnd.microsoft.icon" rel="icon" href="images/favicon_caronte.ico"/>
			<link type="text/css" rel="stylesheet" href="css/improvisa_style.css"/>
			<link type="text/css" rel="stylesheet" href="css/improvisa_style_caronte.css"/>
			<link type="text/css" rel="stylesheet" href="css/tab-view.css"/>
			<link type="text/css" rel="stylesheet" href="css/jqModal.css"/>
			<script type="application/javascript" src="js/i18n.js"></script>
			<script type="application/javascript" src="js/jquery-1.7.1.min.js"></script>
			<script type="application/javascript" src="http://maps.google.com/maps/api/js?v=3.6&amp;sensor=true&amp;region=ES"></script>
			<script type="application/javascript">
				var userName = '${usuario.nombre}';
				var usuario_actual = ${usuario.id};
				var usuario_actual_tipo = '${usuario.rol}';
				var nivelMsg = ${usuario.nivelMsg};
				var idioma = '<fmt:message key="idioma"/>';
			</script>
			<script type="application/javascript" src="js/mapa.js"></script>
			<script type="application/javascript" src="js/mapa_caronte.js"></script>
			<script type="application/javascript" src="js/mapa_caronte2.js"></script>
			<script type="application/javascript" src="js/mensajes.js"></script>
			<script type="application/javascript" src="js/registro.js"></script>
			<!-- Objeto Marcador -->
			<script type="application/javascript" src="js/marcador.js"></script>
			<!--Hora y Fecha -->
			<script type="application/javascript" src="js/hora_fecha.js"></script>
			<!-- Formularios, parte grafica y logica -->
			<script type="application/javascript" src="js/ajax.js"></script>
			<script type="application/javascript" src="js/tab-view.js"></script>
			<script type="application/javascript" src="js/forms.js"></script>
			<!-- jqModal Dependencies -->
			<script type="application/javascript" src="js/jqModal.js"></script>
			<!-- Optional Javascript for Drag'n'Resize -->
			<script type="application/javascript" src="js/jqDnR.js"></script>
			<script type="application/javascript" src="js/dimensions.js"></script>
			<!-- jQuery -->
			<script type="application/javascript" src="js/jquery-fieldselection.js"></script>
			<script type="application/javascript" src="js/jquery-ui-personalized-1.5.2.min.js"></script>
		</head>
		<body onload="IniciarReloj24(); initialize(); if(document.getElementById('username')!=null){document.getElementById('username').focus();}"> <!-- dwr.engine.setActiveReverseAjax(true); -->
			<c:import url="jspf/cabecera.jsp"/>
			<!-- If the user isn't autenticated, we show the login form -->
			<c:if test="${usuario.nombre == null}">
				<c:import url="jspf/formInicio.jsp"/>
			</c:if>
			<c:if test="${usuario.nombre != null}">
				<!-- and if the user is autenticated, we show the username and logout button -->
				<fmt:message key="eres"/> <span id="signeduser">${usuario.nombre}</span>
				<br/>
				<c:url var="logout" value="logout.jsp"/>
				<a href="${logout}" onclick="$.post('getpost/update.jsp',{'accion':'cerrarSesion','nombre':'${usuario.nombre}'})">
					<fmt:message key="cerrarsesion"/>
				</a>
				<c:import url="jspf/menu_caronte_mobile.jsp"/>
			
				<div id="messages">
					<p><fmt:message key="mensajes"/>:</p>
				</div>
				<audio id="audio" class="oculto" controls="controls">
					<source type="audio/ogg" src="sounds/bad.ogg"/>
					<source type="audio/mpeg" src="sounds/alarm.mp3"/>
				</audio>
			</c:if>
		</body>
	</fmt:bundle>
</html>