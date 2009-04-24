function ObjDirectionsInfo(dirn, step, poly, eol, marker, stepnum, d, start, end)
{
this.dirn = dirn;
this.step = step;
this.poly = poly;
this.eol = eol;
this.icon = new GIcon(G_DEFAULT_ICON);
this.icon.infoWindowAnchor = new GPoint(5, 1);
this.marker=marker;
this.stepnum=stepnum;
this.d=d;
this.start=start;
this.end=end;
}
