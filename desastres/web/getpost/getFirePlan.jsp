<%-- 
    Document   : getFirePlan
    Created on : 29-nov-2012, 17:19:10
    Author     : Lara Lorna Jimenez Jimenez
--%>

<%@page contentType="application/json" pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@page import="java.sql.Timestamp, java.util.Date"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@taglib prefix="json" uri="http://www.atg.com/taglibs/json"%>

<%@include file="../jspf/database.jspf"%>
<%
	long fecha = new Date().getTime();
	String hace5min = "'" + new Timestamp(fecha - 300000).toString() + "'"; // 300000 = 5m*60s*1000ms
	String hace30min = "'" + new Timestamp(fecha - 1800000).toString() + "'"; // 1800000 = 30m*60s*1000ms
	// String hace12horas = "'" + new Timestamp(fecha - 43200000).toString() + "'"; // 43200000 = 12h*60m*60s*1000ms
%>


<%--Para sacar exclusivamente el string del Fireplan recibido en ReceiveFirePlan.java --%>
<c:if test="${param.action == 'firstTime' && param.receptor > 0}">
    <sql:query var="plans" dataSource="${CatastrofesServer}">
            SELECT * FROM JSON WHERE FECHA >= <%=hace5min%>
    </sql:query>
</c:if>
                                
<c:if test="${param.action == 'notFirst' && param.receptor > 0}">
    <sql:query var="plans" dataSource="${CatastrofesServer}">
            SELECT * FROM JSON WHERE FECHA > ?
            <sql:param value="${param.fecha}"/>
    </sql:query>
</c:if>
            
<json:array>
    <c:forEach var="plan" items="${plans.rows}"> 
        <json:object>
            <json:property name="fireplan" value="${plan.jsonstring}"/> 
            <json:property name="date" value="${plan.fecha}"/>
        </json:object> 
        <h2><c:out value="${plan.jsonstring}"></c:out></h2>
    </c:forEach>
</json:array>

<%-- Para sacar la info del parseo de JSONparser.java
<c:if test="${param.action == 'firstTime' && param.receptor > 0}">
    <sql:query var="plans" dataSource="${CatastrofesServer}">
            SELECT * FROM FIREPLAN WHERE FECHA >= <%=hace5min%>
    </sql:query>
</c:if>
                                
<c:if test="${param.action == 'notFirst' && param.receptor > 0}">
    <sql:query var="plans" dataSource="${CatastrofesServer}">
            SELECT * FROM FIREPLAN WHERE FECHA > ?
            <sql:param value="${param.fecha}"/>
    </sql:query>
</c:if> 

<json:array>
    <c:forEach var="plan" items="${plans.rows}"> 
        <sql:query var="descrip" dataSource="${CatastrofesServer}">
            SELECT DESCRIPTION FROM TASK_FIREPLAN WHERE ID_TASK = ?
            <sql:param value="${plan.id_tasks}"/>
        </sql:query>
        <c:forEach var="descript" items="${descrip.rows}">
            <sql:query var="resources" dataSource="${CatastrofesServer}">
                SELECT ID_P FROM RESOURCES_FIREPLAN WHERE ID_T = ?
                <sql:param value="${plan.id_tasks}"/>
            </sql:query>
            <c:forEach var="person" items="${resources.rows}">
                <sql:query var="names" dataSource="${CatastrofesServer}">
                    SELECT NAME FROM PERSON_FIREPLAN WHERE ID_PERSON = ?
                    <sql:param value="${person.id_p}"/>
                </sql:query>
                <c:forEach var="name" items="${names.rows}">
                    <json:object>
                        <json:property name="id_fireplan" value="${plan.id_fireplan}"/> 
                        <json:property name="task" value="${plan.id_tasks}"/>
                        <json:property name="fecha" value="${plan.fecha}"/>
                        <json:property name="desc" value="${descript.description}"/>
                        <json:property name="idname" value="${name.name}"/>
                    </json:object>  
                </c:forEach>
            </c:forEach>
        </c:forEach>    
    </c:forEach>
</json:array>--%>

