<%@page contentType="application/json" pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@page import="java.sql.Timestamp, java.util.Date"%>
<%@taglib prefix="json" uri="http://www.atg.com/taglibs/json"%>

<json:object name="temp">
	<json:property name="hora" value="<%= new Timestamp(new Date().getTime()).toString() %>"/>
</json:object>