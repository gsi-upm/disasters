<%-- 
    Document   : DBJsonFromOCP
    Created on : 26-jul-2012, 17:20:57
    Author     : lara
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>

<%@include file="../jspf/database.jspf"%>

<!DOCTYPE HTML>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSON from OCP (FirePlan)</title>
        <script type="application/javascript" src="../js/jquery-1.7.1.min.js"></script>
        <script type="application/javascript" src="json_OCP.js"></script>
        <style type="text/css">
            table, th, td{
                border: 1px solid black;
            }
            #tabla_edit, #tabla_crear{
                display: none;
            }
        </style>
    </head>
    <body>
        <sql:query var="json" dataSource="${CatastrofesServer}">
            SELECT * FROM JSON
        </sql:query>

        <p>FirePlan Received from OCP</p>
        <div id="tabla_info">
            <table>
                <tr>
                    <th>ID</th><th>JSON</th><th>FECHA</th>
                </tr>
                <c:forEach var="jsonOCP" items="${json.rows}">
                    <tr>
                        <td id="id">${jsonOCP.id}</td>
                        <td id="jsonstring">${jsonOCP.jsonstring}</td>
                        <td id="date">${jsonOCP.fecha}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>
        
        <sql:query var="fireEventReceived" dataSource="${CatastrofesServer}">
            SELECT * FROM FIREEVENT_FROM_OCP
        </sql:query>

        <p>
        <p>FireEvent Received from OCP</p>
        <div id="tabla_info">
            <table>
                <tr>
                    <th>ID</th><th>FireEvent from OCP</th>
                </tr>
                <c:forEach var="fireEventOCP" items="${fireEventReceived.rows}">
                    <tr>
                        <td id="id">${fireEventOCP.id}</td>
                        <td id="fireEvent">${fireEventOCP.fireeventstring}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>
        
        
        <sql:query var="fireEvent" dataSource="${CatastrofesServer}">
            SELECT * FROM FIREEVENT_TO_OCP
        </sql:query>
        <p>
        <p>FireEvent sent to OCP</p>
        <div id="tabla_info">
            <table>
                <tr>
                    <th>ID</th><th>FireEvent to OCP</th>
                </tr>
                <c:forEach var="fireEventToOCP" items="${fireEvent.rows}">
                    <tr>
                        <td id="id">${fireEventToOCP.id}</td>
                        <td id="fireEvents">${fireEventToOCP.fire}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>
        
        <sql:query var="firePlan" dataSource="${CatastrofesServer}">
            SELECT * FROM FIREPLAN
        </sql:query>
        <p>
        <p>Tabla FIREPLAN</p>
        <div id="tabla_info">
            <table>
                <tr>
                    <th>ID</th><th>ID_FIREPLAN</th><th>ID_TASKS</th><th>FECHA</th><th>FIREEVENT</th>
                </tr>
                <c:forEach var="firePlanTable" items="${firePlan.rows}">
                    <tr>
                        <td id="id">${firePlanTable.id}</td>
                        <td id="id_firePlan">${firePlanTable.id_fireplan}</td>
                        <td id="id_tasks">${firePlanTable.id_tasks}</td>
                        <td id="fecha">${firePlanTable.fecha}</td>
                        <td id="fireevent">${firePlanTable.fireevent}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>
        
        <sql:query var="task_firePlan" dataSource="${CatastrofesServer}">
            SELECT * FROM TASK_FIREPLAN
        </sql:query>
        <p>
        <p>Tabla TASK_FIREPLAN</p>
        <div id="tabla_info">
            <table>
                <tr>
                    <th>ID</th><th>ID_TASK</th><th>NUMPEOPLE</th><th>WAITING FOR ANSWER</th><th>DESCRIPTION</th>
                </tr>
                <c:forEach var="task_firePlanTable" items="${task_firePlan.rows}">
                    <tr>
                        <td id="id">${task_firePlanTable.id}</td>
                        <td id="id_task">${task_firePlanTable.id_task}</td>
                        <td id="numpeople">${task_firePlanTable.numpeople}</td>
                        <td id="waitanswer">${task_firePlanTable.waitingforanswer}</td>
                        <td id="description">${task_firePlanTable.description}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>
        
        <sql:query var="resources_firePlan" dataSource="${CatastrofesServer}">
            SELECT * FROM RESOURCES_FIREPLAN
        </sql:query>
        <p>
        <p>Tabla RESOURCES_FIREPLAN</p>
        <div id="tabla_info">
            <table>
                <tr>
                    <th>ID</th><th>ID_T</th><th>ID_P</th>
                </tr>
                <c:forEach var="resources_firePlanTable" items="${resources_firePlan.rows}">
                    <tr>
                        <td id="id">${resources_firePlanTable.id}</td>
                        <td id="id_t">${resources_firePlanTable.id_t}</td>
                        <td id="id_p">${resources_firePlanTable.id_p}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>
        
        <sql:query var="person_firePlan" dataSource="${CatastrofesServer}">
            SELECT * FROM PERSON_FIREPLAN
        </sql:query>
        <p>
        <p>Tabla PERSON_FIREPLAN</p>
        <div id="tabla_info">
            <table>
                <tr>
                    <th>ID</th><th>ID_PERSON</th><th>NAME</th>
                </tr>
                <c:forEach var="person_firePlanTable" items="${person_firePlan.rows}">
                    <tr>
                        <td id="id">${person_firePlanTable.id}</td>
                        <td id="id_person">${person_firePlanTable.id_person}</td>
                        <td id="name">${person_firePlanTable.name}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>
        
        <sql:query var="phone_firePlan" dataSource="${CatastrofesServer}">
            SELECT * FROM PHONE_FIREPLAN
        </sql:query>
        <p>
        <p>Tabla PHONE_FIREPLAN</p>
        <div id="tabla_info">
            <table>
                <tr>
                    <th>ID</th><th>ID_PERSON</th><th>PHONE</th>
                </tr>
                <c:forEach var="phone_firePlanTable" items="${phone_firePlan.rows}">
                    <tr>
                        <td id="id">${phone_firePlanTable.id}</td>
                        <td id="id_person">${phone_firePlanTable.id_person}</td>
                        <td id="phone">${phone_firePlanTable.phone}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>
        
        <sql:query var="email_firePlan" dataSource="${CatastrofesServer}">
            SELECT * FROM EMAIL_FIREPLAN
        </sql:query>
        <p>
        <p>Tabla EMAIL_FIREPLAN</p>
        <div id="tabla_info">
            <table>
                <tr>
                    <th>ID</th><th>ID_PERSON</th><th>EMAIL</th>
                </tr>
                <c:forEach var="email_firePlanTable" items="${email_firePlan.rows}">
                    <tr>
                        <td id="id">${email_firePlanTable.id}</td>
                        <td id="id_person">${email_firePlanTable.id_person}</td>
                        <td id="email">${email_firePlanTable.email}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>
        
    </body>

</html>