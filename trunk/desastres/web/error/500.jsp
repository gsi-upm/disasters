<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<% String proyecto = getServletContext().getInitParameter("proyecto"); %>

<!DOCTYPE HTML>
<html>
	<fmt:bundle basename="fmt.eji8n">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
			<title>Error en el servidor</title>
			<link type="image/vnd.microsoft.icon" rel="icon" href="/<%=proyecto%>/images/favicon_<%=proyecto%>.ico"/>
			<link type="text/css" rel="stylesheet" href="/<%=proyecto%>/css/improvisa_style.css"/>
			<script type="text/javascript" src="/<%=proyecto%>/js/i18n.js"></script>
			<script type="text/javascript">
				var idioma = '<fmt:message key="idioma"/>';
			</script>
			<script type="text/javascript" src="/<%=proyecto%>/js/hora_fecha.js"></script>
		</head>
		<body onload="IniciarReloj24()">
			<div>
				<c:import url="/error/cabecera.jsp"/>
			</div>
			<br/>
			<h1 class="error">Error en el servidor</h1>
			<p class="error">
				<c:url value="/index.jsp" var="volver"/>
				<a href="${volver}"><fmt:message key="volver"/></a>
			</p>
		</body>
	</fmt:bundle>
</html>