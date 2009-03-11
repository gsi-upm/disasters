<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
        <title>DWR StocksDemo</title>
        
        <%-- This files are created in the runtime --%>
        <script type='text/javascript' src='/Disasters/dwr/util.js'></script>        
        <script type='text/javascript' src='/Disasters/dwr/interface/Directions.js'></script>
        <script type='text/javascript' src='js/engine.js'></script>
        
        <%-- Maps--%>
        <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAPDUET0Qt7p2VcSk6JNU1sBSM5jMcmVqUpI7aqV44cW1cEECiThQYkcZUPRJn9vy_TWxWvuLoOfSFBw" type="text/javascript"></script>
        <script src="epoly.js" type="text/javascript"></script>        
        
        <script type="text/javascript">
            var id=0;
            function getDirections() {
                id=document.getElementById("AgentID").value;
                alert("getting directions, id: "+id);
                Directions.sendDirections(id);
            }
        </script>
        
        <link rel="stylesheet" type="text/css" href="generic.css" /> 
    </head>
    
    <body onload="dwr.engine.setActiveReverseAjax(true);" onunload="GUnload()">
        <h1>Tracing a route using Reverse Ajax</h1>        
        
        <input type="text" id="start"/><br />
        <input type="text" id="end"/><br />
        <input type="submit" value="fill form" onclick="getDirections();"/>
        <br>
        Agent id
        <input type="text" id="AgentID"/>
        <br>
        <input type="submit" value="start" onclick="startRoute();return false"/>
        
        <div id="map" style="width: 700px; height: 500px"></div>
        
        <script type="text/javascript">
            //<![CDATA[
            if (GBrowserIsCompatible()) {
 
                var map = new GMap2(document.getElementById("map"));
                map.addControl(new GMapTypeControl());
                map.setCenter(new GLatLng(0,0),2);
                var dirn = new GDirections();
                var step = 5; // metres
                var tick = 100; // milliseconds
                var poly;
                var eol;
                var car = new GIcon();
                car.image="caricon.png"
                car.iconSize=new GSize(32,18);
                car.iconAnchor=new GPoint(16,9);
                var marker;
                var k=0;
                var stepnum=0;

                function animate(d) {
                    if (d>eol) {
                        return;
                    }
                    var p = poly.GetPointAtDistance(d);
                    if (k++>=180/step) {
                        map.panTo(p);
                        k=0;
                    }
                    marker.setPoint(p);
                    if (stepnum+1 < dirn.getRoute(0).getNumSteps()) {
                        if (dirn.getRoute(0).getStep(stepnum).getPolylineIndex() < poly.GetIndexAtDistance(d)) {
                            stepnum++;
                            var stepdist = dirn.getRoute(0).getStep(stepnum-1).getDistance().meters;
                            var steptime = dirn.getRoute(0).getStep(stepnum-1).getDuration().seconds;
                            var stepspeed = ((stepdist/steptime) * 2.24).toFixed(0);
                            step = stepspeed/2.5;
                        }
                    } else {
                        if (dirn.getRoute(0).getStep(stepnum).getPolylineIndex() < poly.GetIndexAtDistance(d)) {
                        }
                    }
                    setTimeout("animate("+(d+step)+")", tick);
                }

                GEvent.addListener(dirn,"load", function() {
                    poly=dirn.getPolyline();
                    eol=poly.Distance();
                    map.setCenter(poly.getVertex(0),17);
                    map.addOverlay(new GMarker(poly.getVertex(0),G_START_ICON));
                    map.addOverlay(new GMarker(poly.getVertex(poly.getVertexCount()-1),G_END_ICON));
                    marker = new GMarker(poly.getVertex(0),{icon:car});
                    map.addOverlay(marker);
                    setTimeout("animate(0)",1000);  // Allow time for the initial map display
                });

                GEvent.addListener(dirn,"error", function() {
                    alert("Location(s) not recognised. Code: "+dirn.getStatus().code);
                });

                function startRoute() {
                    var startpoint = document.getElementById("start").value;
                    var endpoint = document.getElementById("end").value;
                    dirn.loadFromWaypoints([startpoint,endpoint],{getPolyline:true,getSteps:true});
                }

            }
            //]]>
        </script>
    </body>
</html> 