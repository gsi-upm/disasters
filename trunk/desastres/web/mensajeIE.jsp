<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<% String proyecto = getServletContext().getInitParameter("proyect"); %>

<!DOCTYPE HTML>
<html>
	<fmt:bundle basename="fmt.eji8n">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
			<title>Internet Explorer Error</title>
			<link type="image/vnd.microsoft.icon" rel="icon" href="images/favicon_<%=proyecto%>.ico"/>
			<link type="text/css" rel="stylesheet" href="css/improvisa_style.css"/>
			<script type="text/javascript" src="js/i18n.js"></script>
			<script type="text/javascript">var idioma = '<fmt:message key="idioma"/>'</script>
			<script type="text/javascript" src="js/hora_fecha.js"></script>
		</head>
		<body onload="IniciarReloj24()">
			<div>
				<c:import url="jspf/cabecera.jsp"/>
			</div>
			<p class="error">
				<fmt:message key="mensajeIE"/>
				<a href="http://www.mozilla-europe.org/firefox/">Firefox</a>,
				<a href="http://www.google.com/chrome">Chrome</a>,
				<a href="http://www.opera.com/browser">Opera</a>
				<fmt:message key="o"/>
				<a href="http://www.apple.com/safari/download">Safari</a>.
			</p>
		</body>
	</fmt:bundle>
</html>