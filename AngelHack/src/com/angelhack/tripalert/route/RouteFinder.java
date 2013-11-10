package com.angelhack.tripalert.route;

import com.google.android.gms.maps.model.LatLng;

public class RouteFinder {

	
	
	public String makeUrl(LatLng src ,LatLng dest){
	    StringBuilder urlString = new StringBuilder();
	    urlString.append("http://maps.googleapis.com/maps/api/directions/json");
	    urlString.append("?origin="); //start positie
	    urlString.append(Double.toString(src.latitude));
	    urlString.append(",");
	    urlString.append(Double.toString(src.longitude));
	    urlString.append("&destination="); //eind positie
	    urlString.append(Double.toString(dest.latitude));
	    urlString.append(",");
	    urlString.append(Double.toString(dest.longitude));
	    urlString.append("&sensor=false&mode=driving");

	    return urlString.toString();
	}
}
