<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
	String proyecto = getServletContext().getInitParameter("proyect");
	String contexto = proyecto;
	if(proyecto.equals("caronte")){
		contexto = "desastres";
	}
%>

<!DOCTYPE HTML>
<html>
	<fmt:bundle basename="fmt.eji8n">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
			<title>P&aacute;gina no encontrada</title>
			<link type="image/vnd.microsoft.icon" rel="icon" href="/<%=contexto%>/images/favicon_<%=proyecto%>.ico"/>
			<link type="text/css" rel="stylesheet" href="/<%=contexto%>/css/improvisa_style.css"/>
			<script type="text/javascript" src="/<%=contexto%>/js/i18n.js"></script>
			<script type="text/javascript">
				var idioma = '<fmt:message key="idioma"/>';
			</script>
			<script type="text/javascript" src="/<%=contexto%>/js/hora_fecha.js"></script>
		</head>
		<body onload="IniciarReloj24()">
			<div>
				<c:import url="cabecera.jsp"/>
			</div>
			<br/>
			<h1 class="error">P&aacute;gina no encontrada</h1>
			<p class="error">
				<c:url value="/index.jsp" var="volver"/>
				<a href="${volver}"><fmt:message key="volver"/></a>
			</p>
		</body>
	</fmt:bundle>
</html>