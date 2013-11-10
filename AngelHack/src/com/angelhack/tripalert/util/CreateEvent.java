package com.angelhack.tripalert.util;

import java.net.URLEncoder;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

import com.angelhack.tripalert.model.Event;
import com.angelhack.tripalert.model.Location;
import com.angelhack.tripalert.model.TravelPoint;
import com.google.gson.Gson;

public class CreateEvent {
	String msg = "test";
	int sev = 3;
	Date travelTime = new Date();
	double latitude = 0.0;
	double longitude = 0.0;
	
	public void createEvent(Event e) throws Exception {
		/*
		Location loc = new Location();
		loc.setLatitude(latitude);
		loc.setLongitude(longitude);
		
		TravelPoint tp = new TravelPoint();
		tp.setTravelTime(travelTime);
		tp.setLocation(loc);
		
		Event e = new Event();
		e.setTravelPoint(tp);
		e.setMsg(msg);
		e.setSev(sev);
		*/
		StringBuilder urlString = new StringBuilder();
		Gson gson = new Gson();
		String arg =  gson.toJson(e);
	    urlString.append(Constant.server + "createEvent?arg="+ URLEncoder.encode(arg));
	   
		
	    String url = urlString.toString();
	    DefaultHttpClient httpClient = new DefaultHttpClient();
	    
	    Log.d("urll" , url);
	    
		HttpPost httpPost = new HttpPost(url);

		HttpResponse httpResponse = httpClient.execute(httpPost);
		HttpEntity httpEntity = httpResponse.getEntity();

		Log.d("angel","successfuklly saved event "  );
	}
	
	
}
