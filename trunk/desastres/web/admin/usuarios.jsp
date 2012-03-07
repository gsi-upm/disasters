<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>

<%@include file="../jspf/database.jspf"%>

<!DOCTYPE HTML>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>USUARIOS</title>
		<script type="application/javascript" src="../js/jquery-1.7.1.min.js"></script>
		<script type="application/javascript" src="usuarios.js"></script>
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
		<sql:query var="usuarios" dataSource="${CatastrofesServer}">
			SELECT * FROM USUARIOS
		</sql:query>
		<sql:query var="tipos_usuarios" dataSource="${CatastrofesServer}">
			SELECT * FROM TIPOS_USUARIOS
		</sql:query>
		<p>USUARIOS</p>
		<div id="tabla_info">
			<table>
				<tr>
					<th>id</th><th>nombre_usuario</th><th>password</th><th>tipo_usuario</th><th>nombre_real</th>
					<th>correo</th><th>latitud</th><th>longitud</th><th>localizacion</th><th>proyecto</th><th></th>
				</tr>
				<c:forEach var="usuario" items="${usuarios.rows}">
					<tr>
						<td id="id_${usuario.id}">${usuario.id}</td>
						<td id="nombre_usuario_${usuario.id}">${usuario.nombre_usuario}</td>
						<td id="password_${usuario.id}">${usuario.password}</td>
						<td id="tipo_usuario_${usuario.id}">${usuario.tipo_usuario}</td>
						<td id="nombre_real_${usuario.id}">${usuario.nombre_real}</td>
						<td id="correo_${usuario.id}">${usuario.correo}</td>
						<td id="latitud_${usuario.id}">${usuario.latitud}</td>
						<td id="longitud_${usuario.id}">${usuario.longitud}</td>
						<td id="localizacion_${usuario.id}">${usuario.localizacion}</td>
						<td id="proyecto_${usuario.id}">${usuario.proyecto}</td>
						<td>
							<input type="button" value="Modificar" onclick="modificar(${usuario.id})"/>
							<br/>
							<input type="button" value="Eliminar" onclick="eliminar(${usuario.id})"/>
						</td>
					</tr>
				</c:forEach>
			</table>
			<input type="button" value="Nuevo" onclick="nuevo()"/>
		</div>
		<div id="tabla_edit">
			<table>
				<tr>
					<th>id</th><th>nombre_usuario</th><th>password</th><th>tipo_usuario</th><th>nombre_real</th>
					<th>correo</th><th>latitud</th><th>longitud</th><th>localizacion</th><th>proyecto</th><th></th>
				</tr>
				<tr>
					<td id="id_edit"></td>
					<td id="nombre_usuario_edit"></td>
					<td id="password_edit"></td>
					<td>
						<select id="tipo_usuario_edit">
							<c:forEach var="tipo_usuario" items="${tipos_usuarios.rows}">
								<option value="${tipo_usuario.id}">${tipo_usuario.id} - ${tipo_usuario.tipo}</option>
							</c:forEach>
						</select>
					</td>
					<td><input id="nombre_real_edit" type="text"/></td>
					<td><input id="correo_edit" type="email"/></td>
					<td><input id="latitud_edit" type="number" step="0.000001"/></td>
					<td><input id="longitud_edit" type="number" step="0.000001"/></td>
					<td>
						<select id="localizacion_edit">
							<option value="true">true</option>
							<option value="false">false</option>
						</select>
					</td>
					<td id="proyecto_edit"></td>
					<td>
						<input type="button" value="Aceptar" onclick="aceptar('modificar')"/>
						<br/>
						<input type="button" value="Cancelar" onclick="cancelar()"/>
					</td>
				</tr>
			</table>
		</div>
		<div id="tabla_crear">
			<table>
				<tr>
					<th>nombre_usuario</th><th>password</th><th>tipo_usuario</th><th>nombre_real</th>
					<th>correo</th><th>latitud</th><th>longitud</th><th>localizacion</th><th></th>
				</tr>
				<tr>
					<td><input id="nombre_usuario_crear" type="text"/></td>
					<td><input id="password_crear" type="text"/></td>
					<td>
						<select id="tipo_usuario_crear">
							<c:forEach var="tipo_usuario" items="${tipos_usuarios.rows}">
								<option value="${tipo_usuario.id}">${tipo_usuario.id} - ${tipo_usuario.tipo}</option>
							</c:forEach>
						</select>
					</td>
					<td><input id="nombre_real_crear" type="text"/></td>
					<td><input id="correo_crear" type="email"/></td>
					<td><input id="latitud_crear" type="number" step="0.000001"/></td>
					<td><input id="longitud_crear" type="number" step="0.000001"/></td>
					<td>
						<select id="localizacion_crear">
							<option value="true" selected="selected">true</option>
							<option value="false">false</option>
						</select>
					</td>
					<td>
						<input type="button" value="Aceptar" onclick="aceptar('crear')"/>
						<br/>
						<input type="button" value="Cancelar" onclick="cancelar()"/>
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>