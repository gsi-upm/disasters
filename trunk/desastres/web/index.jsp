<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="org.securityfilter.example.Constants"%>

<%@ page language = "java"  %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
    
    <fmt:bundle basename="fmt.eji8n">
    
        <head>
            <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
            <title><fmt:message key="title"/></title>
            
            <link href="css/improvisa_style.css" rel="stylesheet" type="text/css" />
            <link rel="stylesheet" href="css/tab-view.css" type="text/css" media="screen">
            <script src="js/jquery.js" type="text/javascript" ></script>
            <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAA_pvzu2eEg9OhdvcrhLRkgRSXZ-vMQ48x4C6FPZ72aXwxrdjUDxSASm6YS5fgxM4XDiwIpFkrYCIdUQ" type="text/javascript"></script>
            <script src="js/mapa.js" type="text/javascript"></script>
            <!-- Objeto Marcador -->
            <script src="js/marcador.js" type="text/javascript"></script>
            <!--Hora y Fecha -->
            <script src="js/hora_fecha.js" type="text/javascript"></script>
            
            <!-- Formularios, parte grafica y logica -->
            <script src="js/tab-view.js" type="text/javascript" ></script>
            <script src="js/forms.js" type="text/javascript" ></script>
            
            <!-- jqModal Dependencies -->
            <script type="text/javascript" src="js/jquery.js"></script>
            <script type="text/javascript" src="js/jqModal.js"></script>
            
            <!-- Optional Javascript for Drag'n'Resize -->
            <script type="text/javascript" src="js/jqDnR.js"></script>
            <script type="text/javascript" src="js/dimensions.js"></script>
            <!-- Necesario para los radio en los formularios -->
            <script>
                function seleccionRadio (form, valor){
                    var types = ["fire","flood","collapse","police","firemen","ambulance","slight","serious","dead","trapped"];
                    for (Count = 0; Count < 4; Count++) {
                        if (form.tipo[Count].checked)
                            break;
                    }
                    return types[Count+3*valor];
                }
            </script>
            <!-- Necesario para los pop-ups -->
            <script>
                $().ready(function() {
                    $('#dialog1').jqm();$('#dialog2').jqm();$('#dialog3').jqm();$('#modificar').jqm();
                });
            </script>
            <script>
     
                $(document).ready(function() {
                    $('#screen').hide();
                    $('#close_screen').hide();
                    $('#console').hide();
                    $('#visualize').hide();

        
                    $('#minitab2').toggle(
                    function() {
                        $('#visualize').slideDown();
                        $('#hideVisualize').click(function(){
                            $('#visualize').slideUp();
                            return false;
                        });
                        //$('#visualize').click(function() {
                        //$(this).slideUp();});
                        //  return false;
    
                    }
                    ,function(){
                        $('#visualize').slideUp();
                    }  );
        
                    $('#minitab1').toggle(function() {
                        $.get('info.jsp', {}, function(data) {$('#console').html(data);});
                        $('#console').slideDown();
                        $('#console').click(function() {
                            $(this).slideUp();});
                        return false;
                    },function(){
                        $('#console').slideUp();
                    });
                });
     
            </script>
            
            <!-- Style and javaScript for the virtual keyboard -->
            <style type='text/css'>
                @import 'css/keyboardstyle.css';
            </style>
            <%--
            <script
                type='text/javascript'
                src='js/jquery-1.2.6.min.js'>
            </script>
            --%>
            <script
                type='text/javascript'
                src='js/jquery-fieldselection.js'>
            </script>
            <script
                type='text/javascript'
                src='js/jquery-ui-personalized-1.5.2.min.js'>
            </script>
            <script
                type='text/javascript'
                src='js/vkeyboard.js'>
            </script>
            
            
        </head>
        
        <!-- Ventana de error de direccion no encontrada -->
        <div class="jqmWindow" id="error">
            <table><tr><td align="left"><p id="error_texto"></p></td></tr><tr><td align="center">
            <br><button class="xxx jqmClose"><fmt:message key="aceptar"/></button></td></tr></table>
        </div>
        
        <!-- Ventana de modificacion -->
        <div class="jqmWindow" id="modificar">
            <form id="form-modifica">
                
                <table border="0" width="100%" cellpadding="0" cellspacing="0">
                    <tr><td colspan="2"><h5><fmt:message key="modificacion"/></h5></td></tr>
                    <tr><td><p id="item_tipo"></p>
                            <div id="quantity">
                                <label for="cantidad"><fmt:message key="numero"/>:</label>
                                <select name="cantidad" id="cantidad" >
                                    <option id="s1" value="1">1</option>
                                    <option id="s2" value="2">2</option>
                                    <option id="s3" value="3">3</option>
                                    <option id="s4" value="4">4</option>
                                    <option id="s5" value="5">5</option>
                                    <option id="s6" value="6">6</option>
                                    <option id="s7" value="7">7</option>
                                    <option id="s8" value="8">8</option>
                                    <option id="s9" value="9">9</option>
                                    <option id="s10" value="10">10</option>
                                </select> 
                            </div>
                            
                            <p id="asociacion"></p>
                            <input type="text" name="nombre" id="nombre" class="nombre" />
                        </td><td align="center">
                            <img alt="" id="iconoAdecuado" class="rayas"/>
                            
                            
                </td></tr> </table>
                <textarea name="info" id="info" class="info"><fmt:message key="informacion"/></textarea>
                <textarea name="descripcion" id="descripcion" class="descripcion"><fmt:message key="descripcion"/></textarea>
                <table cellpadding="0" cellspacing="0" border="0" width="100%"><tr><td align="left" valign="top">
                        <textarea id="direccion0" class="direccion" name="direccion"><fmt:message key="direccion"/></textarea></td><td valign="top" align="right">
                            <img class="botones" alt="Validar direccion" id="validardireccion0" onclick="validarDireccion(0)" onmouseover="cambiaFlecha(0,0)"  onmouseout="cambiaFlecha(1,0)" src="images/iconos/confirm2.png"/><br>
                    <img class="botones" alt="Direccion no valida" id="validacion0"  src="images/iconos/no.png"/></td></tr>
                    <tr><td align="left" colspan="2" >
                            <input type="hidden" name="iden" id="iden" value=""/>
                            <input type="hidden" name="latitud" id="latitud0" value=""/>
                            <input type="hidden" name="longitud" id="longitud0" value=""/>
                            <input type="hidden" name="idAssigned" id="idAssigned" value=""/>
                            <table cellpadding="0" cellspacing="0" border="0" width="100%"><tr><td > 
                                    <label for="estado"><fmt:message key="estado"/>:</label> </td><td align="right"><select name="estado" id="estado" >
                                            <option id="active" value="active"><fmt:message key="activo"/></option>
                                            <option id="controlled" value="controlled"><fmt:message key="controlado"/></option>
                                            
                            </select>        </td></tr>   </table>
                            <div id="size-traffic">
                                <table cellpadding="0" cellspacing="0" border="0" width="100%"><tr><td>   
                                        <label for="magnitude"><fmt:message key="tamaño"/></label></td><td align="right">
                                            <select name="magnitude" id="magnitude">
                                                <option value="small"><fmt:message key="pequeño"/></option>
                                                <option value="medium"><fmt:message key="mediano"/></option>
                                                <option value="big"><fmt:message key="grande"/></option>
                                                <option value="huge"><fmt:message key="enorme"/></option>
                                    </select></td></tr>
                                    <tr><td>                            
                                        <label for="traffic"><fmt:message key="densidadtrafico"/></label></td><td align="right">
                                            <select name="traffic" id="traffic">
                                                <option value="low"><fmt:message key="baja"/></option>
                                                <option value="medium"><fmt:message key="media"/></option>
                                                <option value="high"><fmt:message key="alta"/></option>
                                            </select>
                                    </td></tr>
                                </table>
                            </div>
                            <br><br>
                            <a href="#" onclick="pinchaMapa(0);" id="pincha"><fmt:message key="marcarmapa"/></a><br>
                            <input type=button  id="submit0" value="<fmt:message key="modificar"/>" class="btn" onclick="modificar(iden.value,
                                cantidad.value,nombre.value,info.value,descripcion.value,direccion.value,longitud.value,latitud.value,estado.value,magnitude.value,traffic.value,idAssigned.value);
                                borrarFormulario(this.form,1);$('#modificar').jqm().jqmHide();return false;"/>
                            
                </td></tr></table>
                
                
                
                
                
            </form>
            
        </div>
        <!-- Ventana de modificacion -->


        <!-- Cabecera con imagen y hora -->
        <body onload="initialize();IniciarReloj24();" onunload="GUnload()">
            
            <table  cellpadding="0" cellspacing="0" width="100%" border="0"><tr><td>
                        <div id="cabecera"><img src="images/<fmt:message key="header"/>.gif" alt="" /></div>
                    </td><td align="right">
                        
                        
                        
                        
                        <!--reloj -->
                        <div id="reloj">
                            <div id="fecha">
                                <script type="text/javascript"><fmt:message key="mostrarfecha"/></script>
                            </div>
                            <form id="Reloj24H" action="">
                                <div><input style="
                                            background:transparent; border:0px #999999; color:#999999; text-align:center; font-size:18pt; font-weight:bold" type="text" size="8" name="digitos" value=" " /></div>
                            </form>
                        </div>
                        
                        <!-- minitabs top -right -->
                        <div id="minitabs">
                            <div id="minitab2" class="minitab">
                                <img alt="ver" src="images/tab_building.png">
                            </div>
                            <div id="minitab1" class="minitab">
                                <img alt="más info" src="images/tab_eye.png">
                            </div>
                            
                        </div>
            </td></tr></table>
            
            <!-- If the user isn't autenticated, we show the login form -->
            <% if (request.getRemoteUser()==null)%>
            <% {%>
            <h3>Sign in</h3>            
            <div id="login">                
                <form action="<%=response.encodeURL(Constants.LOGIN_FORM_ACTION)%>" method="POST" id="loginform">                    
                    Username:
                    <input type="text"
                           name="<%=Constants.LOGIN_USERNAME_FIELD%>"
                           id="username"
                           ><p>                    
                    Password:
                    <input type="password"
                           name="<%=Constants.LOGIN_PASSWORD_FIELD%>"
                           id="pwd"
                           ><p>
                    <a href="#" id="showkeyboard" title="Type in your password using a virtual keyboard.">Keyboard</a> <br />                    
                    <input type="submit" name="Submit" id="submit_butt" value="Submit" />
                </form>
            </div>
            <% }%>
            <!-- and if the user is autenticated, we show the username and logout button -->
            <% if (request.getRemoteUser()!=null)%>
            <% {%>
            <fmt:message key="eres"/> <%= request.getRemoteUser() %>
            <a href="logout.jsp">Logout</a>
            <% }%>
            
            <table cellpadding="0" cellspacing="0" border="0">
                <tr><td  valign="top" >
                        <div id="left">
                            
                            <!-- if the user is in role 'administrador' -->
                            <% if (request.isUserInRole("administrador")) {
                            %>
                            <div id="dhtmlgoodies_tabView1">
                                <div class="dhtmlgoodies_aTab">
                                    <form id="catastrofes">
                                        
                                        <input type="hidden" name="marcador" value="event"/><br>
                                        
                                        <table width="100%" border="0"><tr><td>
                                                    <input type="radio" name="tipo"  value="fire" checked="checked"  onclick="cambiaIcono(marcador.value,'fire');"/><fmt:message key="incendio"/><br>
                                                    <input type="radio" name="tipo"  value="flood" onclick="cambiaIcono(marcador.value,'flood');"/><fmt:message key="inundacion"/><br>
                                                <input type="radio" name="tipo"  value="collapse" onclick="cambiaIcono(marcador.value,'collapse');"/><fmt:message key="derrumbamiento"/><br><br></td>
                                            <td><img alt="" id="icono_catastrofes"  src="markers/fuego.png" class="rayas"></td></tr>
                                        </table>
                                        
                                        <input type="hidden" name="cantidad" value="1"/>                                                                                                                                        
                                        <input type="text" name="nombre" class="nombre" value="<fmt:message key="nombre"/>"/>
                                        <textarea name="info" class="info"><fmt:message key="informacion"/></textarea>
                                        <textarea name="descripcion" class="descripcion"><fmt:message key="descripcion"/></textarea>
                                        <table cellpadding="0" cellspacing="0" border="0" width="100%"><tr><td align="left" valign="top">
                                                <textarea id="direccion1" class="direccion" name="direccion"><fmt:message key="direccion"/></textarea></td><td valign="top" align="right">
                                                    <img class="botones" alt="Validar direccion" id="validardireccion1" onclick="validarDireccion(1)" onmouseover="cambiaFlecha(0,1)"  onmouseout="cambiaFlecha(1,1)" src="images/iconos/confirm2.png"/><br>
                                            <img class="botones" alt="Direccion no valida" id="validacion1"  src="images/iconos/no.png"/></td></tr>
                                        </table>
                                        
                                        <!--a href="#" onclick="pinchaMapa(1);">Marcar en mapa</a--><br>
                                        <table cellpadding="0" cellspacing="0" border="0" width="100%"><tr><td>   
                                                <label for="size"><fmt:message key="tamaño"/>: </label></td><td>
                                                    <select name="size" id="size">
                                                        <option value="small" selected><fmt:message key="pequeño"/></option>
                                                        <option value="medium"><fmt:message key="mediano"/></option>
                                                        <option value="big"><fmt:message key="grande"/></option>
                                                        <option value="huge"><fmt:message key="enorme"/></option>
                                            </select></td></tr>
                                            <tr><td>                            
                                                <label for="traffic"><fmt:message key="densidadtrafico"/>: </label></td><td>
                                                    <select name="traffic" id="traffic">
                                                        <option value="low" selected><fmt:message key="baja"/></option>
                                                        <option value="medium"><fmt:message key="media"/></option>
                                                        <option value="high"><fmt:message key="alta"/></option>
                                                    </select>
                                            </td></tr>
                                        </table>
                                        <div class="jqmWindow" id="dialog1">
                                            <fmt:message key="puntoalmacenado"/><br><br>
                                            <center> <button onclick="crearCatastrofe(
                                                marcador.value,seleccionRadio(this.form,0),cantidad.value,nombre.value,info.value,
                                                descripcion.value,direccion.value,longitud.value,latitud.value,estado.value,size.value,traffic.value,0);$('#dialog1').jqm().jqmHide();borrarFormulario(this.form,1);return false;"><fmt:message key="añadir"/></button>
                                                <button class="xxx jqmClose"><fmt:message key="cancelar"/></button>
                                            </center>
                                        </div>
                                        
                                        
                                        <input type="hidden" name="latitud" id="latitud1" value=""/>
                                        <input type="hidden" name="longitud" id="longitud1" value=""/>
                                        <input type="hidden" name="estado" value="active"/> 
                                        <br>
                                        <input type=button  id="submit" value="<fmt:message key="marcarenelmapa"/>" class="btn" onclick="pinchaMapa(1);return false;"/>
                                        
                                        <input type=button  id="submit" value="<fmt:message key="añadir"/>" class="btn" onclick="crearCatastrofe(
                                            marcador.value,seleccionRadio(this.form,0),cantidad.value,nombre.value,info.value,
                                            descripcion.value,direccion.value,longitud.value,latitud.value,estado.value,size.value,traffic.value,0);borrarFormulario(this.form,1);return false;"/>
                                        
                                    </form>
                                </div>
                                
                                <div class="dhtmlgoodies_aTab">
                                    <form id="recursos">
                                        
                                        <input type="hidden" name="marcador" value="resource"/>
                                        <br>
                                        
                                        <table width="100%" border="0"><tr><td>                                                
                                                    <input type="radio" name="tipo"  value="police" checked="checked"  onclick="cambiaIcono(marcador.value,'police', cantidad.value);"/> <fmt:message key="policia"/><br>
                                                    <input type="radio" name="tipo"  value="firemen" onclick="cambiaIcono(marcador.value,'firemen',cantidad.value);"/> <fmt:message key="bomberos"/><br>
                                                <input type="radio" name="tipo"  value="ambulance" onclick="cambiaIcono(marcador.value,'ambulance',cantidad.value);"/> <fmt:message key="ambulancia"/><br><br></td>
                                            <td><img alt="" id="icono_recursos"  src="markers/policia1.png" class="rayas"></td></tr>
                                        </table>
                                        <label for="cantidad"><fmt:message key="numerounidades"/>:</label>
                                        <select name="cantidad" onchange="cambiaIcono(marcador.value,seleccionRadio(this.form,1),cantidad.value);">
                                            <option value="1" selected>1</option>
                                            <option value="2">2</option>
                                            <option value="3">3</option>
                                            <option value="4">4</option>
                                            <option value="5">5</option>
                                            <option value="6">6</option>
                                            <option value="7">7</option>
                                            <option value="8">8</option>
                                            <option value="9">9</option>
                                            <option value="10">10</option>
                                        </select>
                                        
                                        
                                        <input type="text" name="nombre" value="<fmt:message key="unidades"/>" class="nombre"/>
                                        <textarea name="info" class="info"><fmt:message key="informacion"/></textarea>
                                        <textarea name="descripcion" class="descripcion"><fmt:message key="descripcion"/></textarea>
                                        <table cellpadding="0" cellspacing="0" border="0" width="100%"><tr><td align="left" valign="top">
                                                <textarea id="direccion2" name="direccion" class="direccion"><fmt:message key="direccion"/></textarea></td><td valign="top" align="right">
                                                    <img class="botones" alt="Validar direccion" id="validardireccion2" onclick="validarDireccion(2)" onmouseover="cambiaFlecha(0,2)"  onmouseout="cambiaFlecha(1,2)" src="images/iconos/confirm2.png"/><br>
                                        <img class="botones" alt="Direccion no valida" id="validacion2"  src="images/iconos/no.png" /></td></tr></table>
                                        
                                        
                                        <!--a href="#" onclick="pinchaMapa(2)">Marcar en mapa</a-->
                                        <div class="jqmWindow" id="dialog2">
                                            <fmt:message key="puntoalmacenado"/><br><br>
                                            <center> <button onclick="crearCatastrofe(
                                                marcador.value,seleccionRadio(this.form,1),cantidad.value,nombre.value,info.value,
                                                descripcion.value,direccion.value,longitud.value,latitud.value,estado.value,magnitude.value,traffic.value,0);$('#dialog2').jqm().jqmHide();borrarFormulario(this.form,2);return false;"><fmt:message key="añadir"/></button>
                                                <button class="xxx jqmClose"><fmt:message key="cancelar"/></button>
                                            </center>
                                        </div>
                                        
                                        
                                        <input type="hidden" name="latitud" id="latitud2" value=""/>
                                        <input type="hidden" name="longitud" id="longitud2" value=""/>
                                        <input type="hidden" name="estado" value="active"/><br><br>
                                        <input type="hidden" name="magnitude" id="magnitude2" value=""/>
                                        <input type="hidden" name="traffic" id="traffic2" value=""/>
                                        <input type=button  id="submit" value="<fmt:message key="marcarenelmapa"/>" class="btn" onclick="pinchaMapa(2);return false;"/>
                                        
                                        <input type=button  id="submit" value="<fmt:message key="añadir"/>" class="btn" onclick="crearCatastrofe(marcador.value,seleccionRadio(this.form,1),cantidad.value,nombre.value,info.value,descripcion.value,direccion.value,longitud.value,latitud.value,estado.value,magnitude.value,traffic.value,0);borrarFormulario(this.form,2);return false;"/>
                                        
                                        
                                    </form>
                                    
                                </div>
                                
                                <div class="dhtmlgoodies_aTab">
                                    <form id="heridos">
                                        
                                        <input type="hidden" name="marcador" value="people"/>
                                        <br>
                                        
                                        <table width="100%" border="0"><tr><td>
                                                    
                                                    <input type="radio" name="tipo"  value="slight" checked="checked"  onclick="cambiaIcono(marcador.value,'slight',cantidad.value);"/><fmt:message key="leves"/><br>
                                                    <input type="radio" name="tipo"  value="serious" onclick="cambiaIcono(marcador.value,'serious',cantidad.value);"/><fmt:message key="graves"/><br>
                                                    <input type="radio" name="tipo"  value="dead" onclick="cambiaIcono(marcador.value,'dead',cantidad.value);"/><fmt:message key="muertos"/><br>
                                                    <input type="radio" name="tipo"  value="trapped"   onclick="cambiaIcono(marcador.value,'trapped',cantidad.value);"/><fmt:message key="atrapados"/><br>    
                                                <br></td>
                                            <td><img alt="" id="icono_heridos"  src="markers/leve1.png" class="rayas"></td></tr>
                                        </table>
                                        <label for="cantidad"><fmt:message key="numeropersonas"/>:</label>
                                        <select name="cantidad" onchange="cambiaIcono(marcador.value,seleccionRadio(this.form,2),cantidad.value);">
                                            <option value="1" selected>1</option>
                                            <option value="2">2</option>
                                            <option value="3">3</option>
                                            <option value="4">4</option>
                                            <option value="5">5</option>
                                            <option value="6">6</option>
                                            <option value="7">7</option>
                                            <option value="8">8</option>
                                            <option value="9">9</option>
                                            <option value="10">10</option>
                                        </select>
                                        
                                        
                                        <input type="text" name="nombre" class="nombre" value="<fmt:message key="personas"/>"/>
                                        <textarea name="info" class="info"><fmt:message key="nombres"/></textarea>
                                        <textarea name="descripcion" class="descripcion"><fmt:message key="descripciongravedad"/></textarea>
                                        <table cellpadding="0" cellspacing="0" border="0" width="100%"><tr><td align="left" valign="top">
                                                <textarea id="direccion3" name="direccion" class="direccion"><fmt:message key="direccion"/></textarea></td><td valign="top" align="right">
                                                    <img class="botones" alt="Validar direccion" id="validardireccion3" onclick="validarDireccion(3)" onmouseover="cambiaFlecha(0,3)"  onmouseout="cambiaFlecha(1,3)" src="images/iconos/confirm2.png"/><br>
                                            <img class="botones" alt="Direccion no valida" id="validacion3"  src="images/iconos/no.png" /></td></tr>
                                        </table>
                                        
                                        
                                        
                                        <!--a href="#" onclick="pinchaMapa(3)">Marcar en mapa</a-->
                                        <div class="jqmWindow" id="dialog3">
                                            <fmt:message key="puntoalmacenado"/><br><br>
                                            <center> <button onclick="crearCatastrofe(
                                                marcador.value,seleccionRadio(this.form,2),cantidad.value,nombre.value,info.value,
                                                descripcion.value,direccion.value,longitud.value,latitud.value,estado.value,magnitude.value,traffic.value,0);$('#dialog3').jqm().jqmHide();borrarFormulario(this.form,3);return false;"><fmt:message key="añadir"/></button>
                                                <button class="xxx jqmClose"><fmt:message key="cancelar"/></button>
                                            </center>
                                        </div>
                                        
                                        <input type="hidden" name="latitud" id="latitud3" value=""/>
                                        <input type="hidden" name="longitud" id="longitud3" value=""/>
                                        <input type="hidden" name="estado" value="active"/><br>
                                        <input type="hidden" name="magnitude" id="magnitude3" value=""/>
                                        <input type="hidden" name="traffic" id="traffic3" value=""/>
                                        
                                        <input type=button  id="submit" value="<fmt:message key="marcarenelmapa"/>" class="btn" onclick="pinchaMapa(3);return false;"/>
                                        <input type=button  id="submit" value="<fmt:message key="añadir"/>" class="btn" onclick="crearCatastrofe(marcador.value,seleccionRadio(this.form,2),cantidad.value,nombre.value,info.value,descripcion.value,direccion.value,longitud.value,latitud.value,estado.value,magnitude.value,traffic.value,0);borrarFormulario(this.form,3);return false;"/>
                                        
                                        
                                    </form>
                                </div>
                                
                                <div class="dhtmlgoodies_aTab">
                                    <form id="experto">
                                        <fmt:message key="explicacionexperto"/>
                                        <br><br>
                                        <input type=button  id="Start" value="<fmt:message key="comenzar"/>" class="btn" onclick="lanzaExperto();return false;"/>
                                        <input type=button  id="Stop" value="<fmt:message key="parar"/>" class="btn" onclick="stopExperto();return false;"/>                               
                                    </form>                             
                                </div>
                            </div>                           
                            
                            <!--aqui se cambia el tamaño y titulo de las tabs -->
                            <script type="text/javascript">
                                initTabs('dhtmlgoodies_tabView1',Array('<fmt:message key="eventos"/>','<fmt:message key="recursos"/>','<fmt:message key="victimas"/>','<fmt:message key="experto"/>'),0,240,440);
                            </script>
                            <% }
                            %>
                            
                        </div>
                    </td><td width="100%" valign="top">
                        
                        <div id="right">
                            
                            <div id="map_canvas" style="border:#999999 solid 1px; width: 100%; height:471px;">
                                
                            </div>
                            
                        </div>
                        
            </td></tr></table>
            
            <!-- Menus from minitabs -->
            
            <div id="console" class="slideMenu"></div>
            <div id="visualize" class="slideMenu">
                <form name="buildings" id="buildings">
                    <p><fmt:message key="edificios"/></p>
                    <input type="checkbox" name="hospital" value="hospital" checked="checked" onchange="visualize(this.checked,'hospital');" ><fmt:message key="hospitales"/></input><br>
                    <input type="checkbox" name="policeStation" value="policeStation" checked="checked" onchange="visualize(this.checked,'policeStation');"><fmt:message key="comisarias"/></input><br>
                    <input type="checkbox" name="firemenStation" value="firemenStation" checked="checked" onchange="visualize(this.checked,'firemenStation');"><fmt:message key="parquesbomberos"/></input><br><br><br>
                    
                    <a id="hideVisualize" href="#"><fmt:message key="ocultar"/></a>
                </form>
                
            </div>
            
            <!-- Screen for the servlet information -->
            
            <div id="close_screen"><a href="#"><fmt:message key="ocultar"/></a></div>
            <div id="screen">
                
                
            </div>
            
        </body>
        
    </fmt:bundle>
    
</html>