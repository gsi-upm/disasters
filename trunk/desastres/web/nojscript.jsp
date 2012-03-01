<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<% String proyecto = getServletContext().getInitParameter("proyecto"); %>

<!DOCTYPE HTML>
<html>
	<fmt:bundle basename="fmt.eji8n">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
			<title>No Javascript</title>
			<link type="image/vnd.microsoft.icon" rel="icon" href="images/favicon_<%=proyecto%>.ico"/>
			<link type="text/css" rel="stylesheet" href="css/improvisa_style.css"/>
			<script type="application/javascript" src="js/i18n.js"></script>
			<script type="application/javascript">
				var idioma = '<fmt:message key="idioma"/>';
			</script>
			<script type="application/javascript" src="js/hora_fecha.js"></script>
		</head>
		<body>
			<div>
				<c:import url="jspf/cabecera.jsp"/>
			</div>
			<p class="error">Esta p&aacute;gina necesita usar Javascript.</p>
			<p class="error">Por favor, active su funcionamiento o utilice un navegador web que lo soporte</p>
		</body>
	</fmt:bundle>
</html>