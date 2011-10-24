<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>

<%@ include file="getpost/database.jspf" %>

<!DOCTYPE HTML>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>BASE DE DATOS</title>
    </head>
    <body>
		<sql:query var="asociaciones" dataSource="${CatastrofesServer}"
			sql="SELECT * FROM asociaciones_heridos_emergencias">
		</sql:query>
		<sql:query var="sintomas" dataSource="${CatastrofesServer}"
			sql="SELECT * FROM sintomas">
		</sql:query>
		<sql:query var="actividades" dataSource="${CatastrofesServer}"
			sql="SELECT * FROM actividades">
		</sql:query>
		<sql:query var="usuarios" dataSource="${CatastrofesServer}"
			sql="SELECT * FROM usuarios">
		</sql:query>
		<sql:query var="eventos" dataSource="${CatastrofesServer}"
			sql="SELECT * FROM catastrofes">
		</sql:query>
        <sql:query var="historial" dataSource="${CatastrofesServer}"
			sql="SELECT * FROM historial">
		</sql:query>
		<sql:query var="mensajes" dataSource="${CatastrofesServer}"
			sql="SELECT * FROM mensajes">
		</sql:query>
		<sql:query var="tipos_estados" dataSource="${CatastrofesServer}"
			sql="SELECT * FROM tipos_estados">
		</sql:query>
		<sql:query var="tipos_emergencias" dataSource="${CatastrofesServer}"
			sql="SELECT * FROM tipos_emergencias">
		</sql:query>
		<sql:query var="tipos_heridos" dataSource="${CatastrofesServer}"
			sql="SELECT * FROM tipos_heridos">
		</sql:query>
		<sql:query var="tipos_usuarios" dataSource="${CatastrofesServer}"
			sql="SELECT * FROM tipos_usuarios">
		</sql:query>
		<sql:query var="tipos_actividades" dataSource="${CatastrofesServer}"
			sql="SELECT * FROM tipos_actividades">
		</sql:query>
		<sql:query var="tipos_eventos" dataSource="${CatastrofesServer}"
			sql="SELECT * FROM tipos_eventos">
		</sql:query>
		<sql:query var="tipos_sintomas" dataSource="${CatastrofesServer}"
			sql="SELECT * FROM tipos_sintomas">
		</sql:query>
		<sql:query var="asociaciones_emergencias" dataSource="${CatastrofesServer}"
			sql="SELECT * FROM asociaciones_emergencias_con_tipos_usuarios">
		</sql:query>
		<sql:query var="asociaciones_usuarios" dataSource="${CatastrofesServer}"
			sql="SELECT * FROM asociaciones_usuarios_con_tipos_usuarios">
		</sql:query>
		<sql:query var="asociaciones_actividades" dataSource="${CatastrofesServer}"
			sql="SELECT * FROM asociaciones_actividades_con_tipos_usuarios">
		</sql:query>
		<p>ASOCIACIONES_HERIDOS_EMERGENCIAS</p>
		<table border="1">
			<tr><th>ID</th><th>ID_HERIDO</th><th>ID_EMERGENCIA</th><th>ESTADO</th><th>FECHA</th></tr>
			<c:forEach var="asociacion" items="${asociaciones.rows}">
				<tr>
					<td>${asociacion.id}</td>
					<td>${asociacion.id_herido}</td>
					<td>${asociacion.id_emergencia}</td>
					<td>${asociacion.estado}</td>
					<td>${asociacion.fecha}</td>
				</tr>
			</c:forEach>
		</table>
		<p>ACTIVIDADES</p>
		<table border="1">
			<tr><th>ID</th><th>ID_USUARIO</th><th>ID_EMERGENCIA</th><th>ID_TIPO_ACTIVIDAD</th><th>ESTADO</th><th>FECHA</th></tr>
			<c:forEach var="actividad" items="${actividades.rows}">
				<tr>
					<td>${actividad.id}</td>
					<td>${actividad.id_usuario}</td>
					<td>${actividad.id_emergencia}</td>
					<td>${actividad.id_tipo_actividad}</td>
					<td>${actividad.estado}</td>
					<td>${actividad.fecha}</td>
				</tr>
			</c:forEach>
		</table>
		<p>SINTOMAS</p>
		<table border="1">
			<tr><th>ID</th><th>ID_HERIDO</th><th>ID_SINTOMA</th><th>ESTADO</th><th>FECHA</th></tr>
			<c:forEach var="sintoma" items="${sintomas.rows}">
				<tr>
					<td>${sintoma.id}</td>
					<td>${sintoma.id_herido}</td>
					<td>${sintoma.id_sintoma}</td>
					<td>${sintoma.estado}</td>
					<td>${sintoma.fecha}</td>
				</tr>
			</c:forEach>
		</table>
		<p>USUARIOS</p>
		<table border="1">
			<tr>
				<th>ID</th><th>NOMBRE_USUARIO</th><th>PASSWORD</th><th>TIPO_USUARIO</th><th>NOMBRE_REAL</th>
				<th>CORREO</th><th>LATITUD</th><th>LONGITUD</th><th>LOCALIZACION</th><th>PROYECTO</th>
			</tr>
			<c:forEach var="usuario" items="${usuarios.rows}">
				<tr>
					<td>${usuario.id}</td>
					<td>${usuario.nombre_usuario}</td>
					<td>${usuario.password}</td>
					<td>${usuario.tipo_usuario}</td>
					<td>${usuario.nombre_real}</td>
					<td>${usuario.correo}</td>
					<td>${usuario.latitud}</td>
					<td>${usuario.longitud}</td>
					<td>${usuario.localizacion}</td>
					<td>${usuario.proyecto}</td>
				</tr>
			</c:forEach>
		</table>
		<p>CATASTROFES</p>
		<table border="1">
			<tr>
				<th>ID</th><th>MARCADOR</th><th>TIPO</th><th>CANTIDAD</th><th>NOMBRE</th><th>DESCRIPCION</th>
				<th>INFO</th><th>LATITUD</th><th>LONGITUD</th><th>DIRECCION</th><th>ESTADO</th><th>SIZE</th>
				<th>TRAFFIC</th><th>FECHA</th><th>MODIFICADO</th><th>USUARIO</th><th>PLANTA</th><th>IDASSIGNED</th>
			</tr>
			<c:forEach var="evento" items="${eventos.rows}">
				<tr>
					<td>${evento.id}</td>
					<td>${evento.marcador}</td>
					<td>${evento.tipo}</td>
					<td>${evento.cantidad}</td>
					<td>${evento.nombre}</td>
					<td>${evento.descripcion}</td>
					<td>${evento.info}</td>
					<td>${evento.latitud}</td>
					<td>${evento.longitud}</td>
					<td>${evento.direccion}</td>
					<td>${evento.estado}</td>
					<td>${evento.size}</td>
					<td>${evento.traffic}</td>
					<td>${evento.fecha}</td>
					<td>${evento.modificado}</td>
					<td>${evento.usuario}</td>
					<td>${evento.planta}</td>
					<td>${evento.idassigned}</td>
				</tr>
			</c:forEach>
		</table>
		<p>HISTORIAL</p>
		<table border="1">
			<tr><th>ID</th><th>ID_USUARIO</th><th>TIPO</th><th>ID_TIPO</th><th>ID_EMERGENCIA</th><th>EVENTO</th><th>FECHA</th></tr>
			<c:forEach var="fila" items="${historial.rows}">
				<tr>
					<td>${fila.id}</td>
					<td>${fila.id_usuario}</td>
					<td>${fila.tipo}</td>
					<td>${fila.id_tipo}</td>
					<td>${fila.id_emergencia}</td>
					<td>${fila.evento}</td>
					<td>${fila.fecha}</td>
				</tr>
			</c:forEach>
		</table>
		<p>MENSAJES</p>
		<table border="1">
			<tr><th>ID</th><th>MENSAJE</th><th>NIVEL</th><th>FECHA</th></tr>
			<c:forEach var="mensaje" items="${mensajes.rows}">
				<tr>
					<td>${mensaje.id}</td>
					<td>${mensaje.mensaje}</td>
					<td>${mensaje.nivel}</td>
					<td>${mensaje.fecha}</td>
				</tr>
			</c:forEach>
		</table>
		<hr/>
		<p>TIPOS_ESTADOS</p>
		<table border="1">
			<tr><th>ID</th><th>TIPO</th><th>DESCRIPCION</th></tr>
			<c:forEach var="estado" items="${tipos_estados.rows}">
				<tr>
					<td>${estado.id}</td>
					<td>${estado.tipo}</td>
					<td>${estado.descripcion}</td>
				</tr>
			</c:forEach>
		</table>
		<p>TIPOS_EMERGENCIAS</p>
		<table border="1">
			<tr><th>ID</th><th>TIPO</th><th>DESCRIPCION</th></tr>
			<c:forEach var="emergencia" items="${tipos_emergencias.rows}">
				<tr>
					<td>${emergencia.id}</td>
					<td>${emergencia.tipo}</td>
					<td>${emergencia.descripcion}</td>
				</tr>
			</c:forEach>
		</table>
		<p>TIPOS_HERIDOS</p>
		<table border="1">
			<tr><th>ID</th><th>TIPO</th><th>DESCRIPCION</th></tr>
			<c:forEach var="herido" items="${tipos_heridos.rows}">
				<tr>
					<td>${herido.id}</td>
					<td>${herido.tipo}</td>
					<td>${herido.descripcion}</td>
				</tr>
			</c:forEach>
		</table>
		<p>TIPOS_USUARIOS</p>
		<table border="1">
			<tr><th>ID</th><th>TIPO</th><th>NIVEL</th><th>DESCRIPCION</th></tr>
			<c:forEach var="usuario" items="${tipos_usuarios.rows}">
				<tr>
					<td>${usuario.id}</td>
					<td>${usuario.tipo}</td>
					<td>${usuario.nivel}</td>
					<td>${usuario.descripcion}</td>
				</tr>
			</c:forEach>
		</table>
		<p>TIPOS_ACTIVIDADES</p>
		<table border="1">
			<tr><th>ID</th><th>TIPO</th><th>TIPO_EMERGENCIA</th><th>ESTADO_EMERGENCIA</th><th>DESCRIPCION</th></tr>
			<c:forEach var="actividad" items="${tipos_actividades.rows}">
				<tr>
					<td>${actividad.id}</td>
					<td>${actividad.tipo}</td>
					<td>${actividad.tipo_emergencia}</td>
					<td>${actividad.estado_emergencia}</td>
					<td>${actividad.descripcion}</td>
				</tr>
			</c:forEach>
		</table>
		<p>TIPOS_EVENTOS</p>
		<table border="1">
			<tr><th>ID</th><th>TIPO</th><th>DESCRIPCION</th></tr>
			<c:forEach var="evento" items="${tipos_eventos.rows}">
				<tr>
					<td>${evento.id}</td>
					<td>${evento.tipo}</td>
					<td>${evento.descripcion}</td>
				</tr>
			</c:forEach>
		</table>
		<p>TIPOS_SINTOMAS</p>
		<table border="1">
			<tr><th>ID</th><th>TIPO</th><th>DESCRIPCION</th></tr>
			<c:forEach var="sintoma" items="${tipos_sintomas.rows}">
				<tr>
					<td>${sintoma.id}</td>
					<td>${sintoma.tipo}</td>
					<td>${sintoma.descripcion}</td>
				</tr>
			</c:forEach>
		</table>
		<p>ASOCIACIONES_EMERGENCIAS_CON_TIPO_USUARIOS</p>
		<table border="1">
			<tr><th>ID</th><th>ID_TIPO_USUARIO</th><th>ID_TIPO_EMERGENCIA</th><th>LOCALIZACION</th></tr>
			<c:forEach var="asociacion" items="${asociaciones_emergencias.rows}">
				<tr>
					<td>${asociacion.id}</td>
					<td>${asociacion.id_tipo_usuario}</td>
					<td>${asociacion.id_tipo_emergencia}</td>
					<td>${asociacion.localizacion}</td>
				</tr>
			</c:forEach>
		</table>
		<p>ASOCIACIONES_USUARIOS_CON_TIPO_USUARIOS</p>
		<table border="1">
			<tr><th>ID</th><th>ID_TIPO_USUARIO</th><th>ID_TIPO_USUARIO_VER</th><th>VISUALIZAR</th></tr>
			<c:forEach var="asociacion" items="${asociaciones_usuarios.rows}">
				<tr>
					<td>${asociacion.id}</td>
					<td>${asociacion.id_tipo_usuario}</td>
					<td>${asociacion.id_tipo_usuario_ver}</td>
					<td>${asociacion.visualizar}</td>
				</tr>
			</c:forEach>
		</table>
		<p>ASOCIACIONES_ACTIVIDADES_CON_TIPO_USUARIOS</p>
		<table border="1">
			<tr><th>ID</th><th>ID_TIPO_USUARIO</th><th>ID_TIPO_EMERGENCIA</th></tr>
			<c:forEach var="asociacion" items="${asociaciones_actividades.rows}">
				<tr>
					<td>${asociacion.id}</td>
					<td>${asociacion.id_tipo_usuario}</td>
					<td>${asociacion.id_tipo_actividad}</td>
				</tr>
			</c:forEach>
		</table>
    </body>
</html>