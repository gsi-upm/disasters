<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%
	String proyecto = getServletContext().getInitParameter("proyecto");
	boolean proyectoCaronte = proyecto.equals("caronte");
%>

<fmt:bundle basename="fmt.eji8n">
	<img id="cabecera" src="/<%=proyecto%>/images/<fmt:message key="header"/>_<%=proyecto%>.gif" alt=""/>
	<div class="derecha">
		<!-- reloj -->
		<div id="reloj">
			<div id="fecha"></div>
			<div id="Reloj24H"></div>
		</div>
		<img id="langInit" class="pulsable" src="/<%=proyecto%>/images/flags/<fmt:message key="idioma"/>.png"
			 alt="lang:<fmt:message key="idioma"/>" onclick="menuIdiomas('abrir')"/>
		<div id="langSelect" class="oculto">
			<img class="pulsable" src="/<%=proyecto%>/images/close.gif" alt="<fmt:message key="cerrar"/>" onclick="menuIdiomas('cerrar')"/>
			<c:url var="index_es" value="/index.jsp"><c:param name="lang" value="es"/></c:url>
			<a href="${index_es}"><img src="/<%=proyecto%>/images/flags/es.png" alt="Espa&ntilde;ol"/></a>
			<c:url var="index_en" value="/index.jsp"><c:param name="lang" value="en"/></c:url>
			<a href="${index_en}"><img src="/<%=proyecto%>/images/flags/en.png" alt="English"/></a>
			<c:url var="index_en_GB" value="/index.jsp"><c:param name="lang" value="en_GB"/></c:url>
			<a href="${index_en_GB}"><img src="/<%=proyecto%>/images/flags/en_GB.png" alt="British English"/></a>
			<c:url var="index_fr" value="/index.jsp"><c:param name="lang" value="fr"/></c:url>
			<a href="${index_fr}"><img src="/<%=proyecto%>/images/flags/fr.png" alt="Fran&ccedil;ais"/></a>
			<c:url var="index_de" value="/index.jsp"><c:param name="lang" value="de"/></c:url>
			<a href="${index_de}"><img src="/<%=proyecto%>/images/flags/de.png" alt="Deutsch"/></a>
		</div>
		<c:if test="<%=proyectoCaronte%>">
			<div>
				<c:url value="/acercaDe.jsp" var="acercaDe"/>
				<a href="${acercaDe}"><fmt:message key="acerca"/></a>
			</div>
		</c:if>
	</div>
</fmt:bundle>