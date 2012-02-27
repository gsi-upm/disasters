<%@page contentType="application/json" pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@page import="java.util.Date"%>
<%@taglib prefix="json" uri="http://www.atg.com/taglibs/json"%>

<json:object name="temp">
	<json:property name="hora" value="<%= new Date().getTime() %>"/>
</json:object>