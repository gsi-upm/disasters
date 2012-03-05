<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>

<%@include file="../jspf/database.jspf"%>

<!DOCTYPE HTML>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>BASE DE DATOS</title>
	</head>
	<body>
		<sql:query var="eventos" dataSource="${CatastrofesServer}">
			SELECT c.id, c.tipo, c.marcador, c.cantidad, c.nombre, c.descripcion, c.info, c.latitud,
				c.longitud, c.direccion, c.size, c.traffic, c.planta, c.estado, c.idAssigned, c.fecha,
				c.modificado, c.usuario, m.tipo_marcador, t.tipo_catastrofe, e.tipo_estado, u.nombre_usuario
			FROM catastrofes c, tipos_marcadores m, tipos_catastrofes t, tipos_estados e, usuarios u
			WHERE c.marcador = m.id
			AND c.tipo = t.id
			AND c.estado = e.id
			AND c.usuario = u.id
		</sql:query>
		<sql:query var="asociaciones" dataSource="${CatastrofesServer}">
			SELECT * FROM ASOCIACIONES_HERIDOS_EMERGENCIAS
		</sql:query>
		<sql:query var="actividades" dataSource="${CatastrofesServer}">
			SELECT * FROM ACTIVIDADES
		</sql:query>
		<%--<sql:query var="sintomas" dataSource="${CatastrofesServer}">
			SELECT * FROM SINTOMAS
		</sql:query>--%>
		<sql:query var="mensajes" dataSource="${CatastrofesServer}">
			SELECT * FROM MENSAJES
		</sql:query>
		<sql:query var="historial" dataSource="${CatastrofesServer}">
			SELECT * FROM HISTORIAL
		</sql:query>
		<sql:query var="usuarios" dataSource="${CatastrofesServer}">
			SELECT * FROM USUARIOS
		</sql:query>
		<sql:query var="tipos_estados" dataSource="${CatastrofesServer}">
			SELECT * FROM TIPOS_ESTADOS
		</sql:query>
		<sql:query var="tipos_marcadores" dataSource="${CatastrofesServer}">
			SELECT * FROM TIPOS_MARCADORES
		</sql:query>
		<sql:query var="tipos_catastrofes" dataSource="${CatastrofesServer}">
			SELECT C.ID, ID_MARCADOR, TIPO_CATASTROFE, C.DESCRIPCION, TIPO_MARCADOR
			FROM TIPOS_CATASTROFES C, TIPOS_MARCADORES M
			WHERE ID_MARCADOR = M.ID
		</sql:query>
		<sql:query var="tipos_usuarios" dataSource="${CatastrofesServer}">
			SELECT * FROM TIPOS_USUARIOS
		</sql:query>
		<sql:query var="tipos_actividades" dataSource="${CatastrofesServer}">
			SELECT A.ID, A.TIPO, A.TIPO_EMERGENCIA, A.TIPO_MARCADOR, ESTADO_EMERGENCIA, A.DESCRIPCION,
				M.TIPO_MARCADOR tipomarcador, C.TIPO_CATASTROFE, E.TIPO_ESTADO
			FROM TIPOS_ACTIVIDADES A, TIPOS_MARCADORES M, TIPOS_CATASTROFES C, TIPOS_ESTADOS E
			WHERE (A.TIPO_MARCADOR = M.ID OR (A.TIPO_MARCADOR IS NULL AND M.ID = 1)) /* ID forzado solo muestre 1 */
			AND (A.TIPO_EMERGENCIA = C.ID OR (A.TIPO_EMERGENCIA IS NULL AND C.ID = 1)) /* ID forzado solo muestre 1 */
			AND ESTADO_EMERGENCIA = E.ID
		</sql:query>
		<sql:query var="tipos_eventos" dataSource="${CatastrofesServer}">
			SELECT * FROM TIPOS_EVENTOS
		</sql:query>
		<sql:query var="tipos_sintomas" dataSource="${CatastrofesServer}">
			SELECT * FROM TIPOS_SINTOMAS
		</sql:query>
		<sql:query var="asociaciones_emergencias" dataSource="${CatastrofesServer}">
			SELECT * FROM ASOCIACIONES_EMERGENCIAS_CON_TIPOS_USUARIOS
		</sql:query>
		<sql:query var="asociaciones_usuarios" dataSource="${CatastrofesServer}">
			SELECT * FROM ASOCIACIONES_USUARIOS_CON_TIPOS_USUARIOS
		</sql:query>
		<sql:query var="asociaciones_actividades" dataSource="${CatastrofesServer}">
			SELECT * FROM ASOCIACIONES_ACTIVIDADES_CON_TIPOS_USUARIOS
		</sql:query>

		<p>CATASTROFES</p>
		<table border="1">
			<tr>
				<th>ID</th><th>MARCADOR</th><th>TIPO</th><th>CANTIDAD</th><th>NOMBRE</th><th>DESCRIPCION</th>
				<th>INFO</th><th>LATITUD</th><th>LONGITUD</th><th>DIRECCION</th><th>SIZE</th><th>TRAFFIC</th>
				<th>PLANTA</th><th>ESTADO</th><th>IDASSIGNED</th><th>FECHA</th><th>MODIFICADO</th><th>USUARIO</th>
			</tr>
			<c:forEach var="evento" items="${eventos.rows}">
				<tr>
					<td>${evento.id}</td>
					<td>${evento.marcador} - [${evento.tipo_marcador}]</td>
					<td>${evento.tipo} - [${evento.tipo_catastrofe}]</td>
					<td>${evento.cantidad}</td>
					<td>${evento.nombre}</td>
					<td>${evento.descripcion}</td>
					<td>${evento.info}</td>
					<td>${evento.latitud}</td>
					<td>${evento.longitud}</td>
					<td>${evento.direccion}</td>
					<td>${evento.size}</td>
					<td>${evento.traffic}</td>
					<td>${evento.planta}</td>
					<td>${evento.estado} - [${evento.tipo_estado}]</td>
					<td>${evento.idassigned}</td>
					<td>${evento.fecha}</td>
					<td>${evento.modificado}</td>
					<td>${evento.usuario} - [${evento.nombre_usuario}]</td>
				</tr>
			</c:forEach>
		</table>
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
		<%--<p>SINTOMAS</p>
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
		</table>--%>
		<p>MENSAJES</p>
		<table border="1">
			<tr><th>ID</th><th>CREADOR</th><th>MENSAJE</th><th>NIVEL</th><th>FECHA</th></tr>
			<c:forEach var="mensaje" items="${mensajes.rows}">
				<tr>
					<td>${mensaje.id}</td>
					<td>${mensaje.creador}</td>
					<td>${mensaje.mensaje}</td>
					<td>${mensaje.nivel}</td>
					<td>${mensaje.fecha}</td>
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
		<p>USUARIOS</p>
		<table border="1">
			<tr>
				<th>ID</th><th>NOMBRE_USUARIO</th><th>PASSWORD</th><th>TIPO_USUARIO</th><th>NOMBRE_REAL</th>
				<th>CORREO</th><th>LATITUD</th><th>LONGITUD</th><th>LOCALIZACION</th><th>PLANTA</th><th>PROYECTO</th>
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
					<td>${usuario.planta}</td>
					<td>${usuario.proyecto}</td>
				</tr>
			</c:forEach>
		</table>
		<hr/>
		<p>TIPOS_ESTADOS</p>
		<table border="1">
			<tr><th>ID</th><th>TIPO_ESTADO</th><th>DESCRIPCION</th></tr>
			<c:forEach var="estado" items="${tipos_estados.rows}">
				<tr>
					<td>${estado.id}</td>
					<td>${estado.tipo_estado}</td>
					<td>${estado.descripcion}</td>
				</tr>
			</c:forEach>
		</table>
		<p>TIPOS_MARCADORES</p>
		<table border="1">
			<tr><th>ID</th><th>TIPO_MARCADOR</th><th>DESCRIPCION</th></tr>
			<c:forEach var="marcador" items="${tipos_marcadores.rows}">
				<tr>
					<td>${marcador.id}</td>
					<td>${marcador.tipo_marcador}</td>
					<td>${marcador.descripcion}</td>
				</tr>
			</c:forEach>
		</table>
		<p>TIPOS_CATASTROFES</p>
		<table border="1">
			<tr><th>ID</th><th>ID_MARCADOR</th><th>TIPO_CATASTROFE</th><th>DESCRIPCION</th></tr>
			<c:forEach var="catastrofe" items="${tipos_catastrofes.rows}">
				<tr>
					<td>${catastrofe.id}</td>
					<td>${catastrofe.id_marcador} - [${catastrofe.tipo_marcador}]</td>
					<td>${catastrofe.tipo_catastrofe}</td>
					<td>${catastrofe.descripcion}</td>
				</tr>
			</c:forEach>
		</table>
		<p>TIPOS_USUARIOS</p>
		<table border="1">
			<tr><th>ID</th><th>TIPO</th><th>ID_CATASTROFE</th><th>NIVEL</th><th>DESCRIPCION</th></tr>
			<c:forEach var="usuario" items="${tipos_usuarios.rows}">
				<tr>
					<td>${usuario.id}</td>
					<td>${usuario.tipo}</td>
					<td>${usuario.id_catastrofe}</td>
					<td>${usuario.nivel}</td>
					<td>${usuario.descripcion}</td>
				</tr>
			</c:forEach>
		</table>
		<p>TIPOS_ACTIVIDADES</p>
		<table border="1">
			<tr><th>ID</th><th>TIPO</th><th>TIPO_MARCADOR</th><th>TIPO_EMERGENCIA</th><th>ESTADO_EMERGENCIA</th><th>DESCRIPCION</th></tr>
			<c:forEach var="actividad" items="${tipos_actividades.rows}">
				<tr>
					<td>${actividad.id}</td>
					<td>${actividad.tipo}</td>
					<td>${actividad.tipo_marcador}<c:if test="${actividad.tipo_marcador != null}"> - [${actividad.tipomarcador}]</c:if></td>
					<td>${actividad.tipo_emergencia}<c:if test="${actividad.tipo_emergencia != null}"> - [${actividad.tipo_catastrofe}]</c:if></td>
					<td>${actividad.estado_emergencia} - [${actividad.tipo_estado}]</td>
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