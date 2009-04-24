var roadsInfo= new Array();
var starts = new Array();
var ends = new Array();
var resourcesList;

function moveAgents () {
    getResources();
    for (i in resourcesList){
        roadsInfo[resourcesList[i]] = new ObjDirectionsInfo(new GDirections(), 5, null, null, null, 0, 0, null, null);
        myAddListener(resourcesList[i]);
        if (marcadores_definitivos[indices[i]].tipo=="police"){
            roadsInfo[resourcesList[i]].icon.image="markers/policia"+marcadores_definitivos[indices[i]].cantidad+".png";
        } else if (marcadores_definitivos[indices[i]].tipo=="firemen"){
            roadsInfo[resourcesList[i]].icon.image="markers/bombero"+marcadores_definitivos[indices[i]].cantidad+".png";
        } else if (marcadores_definitivos[indices[i]].tipo=="ambulance"){
            roadsInfo[resourcesList[i]].icon.image="markers/ambulancia"+marcadores_definitivos[indices[i]].cantidad+".png";
        }
    }
    DirectionsBean.sendDirections();
    setTimeout("startRoute()",4000);
    setTimeout("update()",6000);
}


function startRoute() {
    for (i in resourcesList){
        starts[resourcesList[i]]= document.getElementById("start"+resourcesList[i]).value;
        ends[resourcesList[i]]= document.getElementById("end"+resourcesList[i]).value;
        roadsInfo[resourcesList[i]].dirn.loadFromWaypoints([starts[resourcesList[i]],ends[resourcesList[i]]],{getPolyline:true,getSteps:true});
    }
}

function update () {
    for (i in resourcesList){
        var newstart = document.getElementById("start"+resourcesList[i]).value;
        var newend = document.getElementById("end"+resourcesList[i]).value;
        if (starts[resourcesList[i]]!= newstart && ends[resourcesList[i]]!= newend){
            roadsInfo[resourcesList[i]].d=0;
            map.removeOverlay(roadsInfo[resourcesList[i]].marker);
            roadsInfo[resourcesList[i]].dirn.loadFromWaypoints([starts[resourcesList[i]],newstart,newend],{getPolyline:true,getSteps:true});
            starts[resourcesList[i]]= newstart;
            ends[resourcesList[i]]= newend;
            continue;
        }
        if (starts[resourcesList[i]]!= newstart){
            roadsInfo[resourcesList[i]].d=0;
            map.removeOverlay(roadsInfo[resourcesList[i]].marker);
            roadsInfo[resourcesList[i]].dirn.loadFromWaypoints([starts[resourcesList[i]],newstart],{getPolyline:true,getSteps:true});
            starts[resourcesList[i]]= newstart;
        }
        if (ends[resourcesList[i]]!= newend){
            roadsInfo[resourcesList[i]].d=0;
            map.removeOverlay(roadsInfo[resourcesList[i]].marker);
            roadsInfo[resourcesList[i]].dirn.loadFromWaypoints([starts[resourcesList[i]],newend],{getPolyline:true,getSteps:true});
            ends[resourcesList[i]]= newend;
        }
    }
    for (i in resourcesList){
        animate(roadsInfo[resourcesList[i]].d,resourcesList[i]);
    }
    DirectionsBean.sendDirections();
    setTimeout("update()",2500);
}

/*
 * From the list of elements, "indices", it makes a new list including only the
 * resources.
 */
function getResources() {
    resourcesList = new Array();
    for (n in indices){
        if (marcadores_definitivos[indices[n]].tipo=="police" ||
            marcadores_definitivos[indices[n]].tipo=="firemen" ||
            marcadores_definitivos[indices[n]].tipo=="ambulance"){
            resourcesList[n]=indices[n];
        }
    }
}

function myAddListener (id) {
    GEvent.addListener(roadsInfo[id].dirn,"load", function() {
        roadsInfo[id].poly=roadsInfo[id].dirn.getPolyline();
        roadsInfo[id].eol=roadsInfo[id].poly.Distance();
        //map.addOverlay(new GMarker(roadsInfo[id].poly.getVertex(0),G_START_ICON));
        //map.addOverlay(new GMarker(roadsInfo[id].poly.getVertex(roadsInfo[id].poly.getVertexCount()-1),G_END_ICON));
        roadsInfo[id].marker = new GMarker(roadsInfo[id].poly.getVertex(0),{icon:roadsInfo[id].icon});
        map.addOverlay(roadsInfo[id].marker);
    });

    GEvent.addListener(roadsInfo[id].dirn,"error", function() {
        alert("Location(s) not recognised for id "+id+" "+starts[resourcesList[id]]+" "+ends[resourcesList[id]]+"\nCode: "+roadsInfo[id].dirn.getStatus().code);
    });    
}

/**
 * Moves the icon through the route.
 */
function animate(dist, id) {
    if (dist>roadsInfo[id].eol) {
        return;
    }
    var p = roadsInfo[id].poly.GetPointAtDistance(dist);
    roadsInfo[id].marker.setPoint(p);
    if (roadsInfo[id].stepnum+1 < roadsInfo[id].dirn.getRoute(0).getNumSteps()) {
        if (roadsInfo[id].dirn.getRoute(0).getStep(roadsInfo[id].stepnum).getPolylineIndex() < roadsInfo[id].poly.GetIndexAtDistance(dist)) {
            roadsInfo[id].stepnum++;
            var stepdist = roadsInfo[id].dirn.getRoute(0).getStep(roadsInfo[id].stepnum-1).getDistance().meters;
            var steptime = roadsInfo[id].dirn.getRoute(0).getStep(roadsInfo[id].stepnum-1).getDuration().seconds;
            var stepspeed = ((stepdist/steptime) * 200.24).toFixed(0);
            roadsInfo[id].step = stepspeed/2.5;
        }
    } else {
        if (roadsInfo[id].dirn.getRoute(0).getStep(roadsInfo[id].stepnum).getPolylineIndex() < roadsInfo[id].poly.GetIndexAtDistance(dist)) {
        }
    }
    roadsInfo[id].d+=roadsInfo[id].step;
}