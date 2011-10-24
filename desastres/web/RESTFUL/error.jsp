<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:choose>
	<c:when test="${param.action == 'address'}">
		${param.address} Not found with Google Geolocator
	</c:when>
	<c:when test="${param.action == 'parameter'}">
		${param.parameter} value not valid: ${param.value}
	</c:when>
</c:choose>