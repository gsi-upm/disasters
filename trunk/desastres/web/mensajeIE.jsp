<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<% String proyecto = getServletContext().getInitParameter("proyecto"); %>

<!DOCTYPE HTML>
<html>
	<fmt:bundle basename="fmt.eji8n">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
			<title>Internet Explorer Error</title>
			<link type="image/vnd.microsoft.icon" rel="icon" href="images/favicon_<%=proyecto%>.ico"/>
			<link type="text/css" rel="stylesheet" href="css/improvisa_style.css"/>
			<script type="application/javascript" src="js/i18n.js"></script>
			<script type="application/javascript">
				var idioma = '<fmt:message key="idioma"/>';
			</script>
			<script type="application/javascript" src="js/hora_fecha.js"></script>
		</head>
		<body onload="IniciarReloj24()">
			<table class="tabla_body">
				<tr >
					<td><c:import url="jspf/cabecera.jsp"/></td>
				</tr>
				<tr>
					<td class="error">
						<fmt:message key="mensajeIE"/>
						<a href="http://www.mozilla.org/firefox/">Firefox</a>,
						<a href="http://www.google.com/chrome">Chrome</a>
						<fmt:message key="o"/>
						<a href="http://www.opera.com/browser">Opera</a>.
					</td>
				</tr>
			</table>
		</body>
	</fmt:bundle>
</html>