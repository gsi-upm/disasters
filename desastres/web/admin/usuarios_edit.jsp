<%@page contentType="applicacion/x-sql" pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@page import="java.security.MessageDigest"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>

<%@include file="../jspf/database.jspf"%>

<c:choose>
	<c:when test="${param.accion == 'modificar'}">
		<sql:update dataSource="${CatastrofesServer}">
			UPDATE usuarios
			SET tipo_usuario = ?, nombre_real = ?, correo = ?, latitud = ?, longitud = ?, localizacion = ?
			WHERE id = ?
			<sql:param value="${param.tipo_usuario}"/>
			<sql:param value="${param.nombre_real}"/>
			<sql:param value="${param.correo}"/>
			<sql:param value="${param.latitud}"/>
			<sql:param value="${param.longitud}"/>
			<sql:param value="${param.localizacion}"/>
			<sql:param value="${param.id}"/>
		</sql:update>
	</c:when>
	<c:when test="${param.accion == 'crear'}">
		<c:set var="password">
			<%
				String password = request.getParameter("password");
				String hash = "";
				try{
					MessageDigest md5 = MessageDigest.getInstance("MD5");
					md5.update(password.getBytes("UTF-8"));
					byte[] valorHash = md5.digest();
					int[] valorHash2 = new int[16];
					for(int i = 0; i < valorHash.length; i++){
						valorHash2[i] = new Integer(valorHash[i]);
						if(valorHash2[i] < 0){
							valorHash2[i] += 256;
						}
						hash += (Integer.toHexString(valorHash2[i]));
					}
				}catch(Exception ex){}
				out.print(hash);
			%>
		</c:set>
		<sql:update dataSource="${CatastrofesServer}">
			INSERT INTO usuarios(nombre_usuario, password, tipo_usuario, nombre_real, correo, latitud, longitud, localizacion, proyecto)
			VALUES(?, ?, ?, ?, ?, ?, ?, ?, 'caronte')
			<sql:param value="${param.nombre_usuario}"/>
			<sql:param value="${password}"/>
			<sql:param value="${param.tipo_usuario}"/>
			<sql:param value="${param.nombre_real}"/>
			<sql:param value="${param.correo}"/>
			<sql:param value="${param.latitud}"/>
			<sql:param value="${param.longitud}"/>
			<sql:param value="${param.localizacion}"/>
		</sql:update>
	</c:when>
	<c:when test="${param.accion == 'eliminar'}">
		<sql:update dataSource="${CatastrofesServer}">
			DELETE FROM usuarios
			WHERE id = ?
			<sql:param value="${param.id}"/>
		</sql:update>		
	</c:when>
</c:choose>