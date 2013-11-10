package com.angelhack.tripalert.util;

import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

import com.angelhack.tripalert.model.Events;
import com.angelhack.tripalert.route.JsonParser;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

public class GetEvents {
	
	public Events getEvents() throws Exception{
		StringBuilder urlString = new StringBuilder();
		urlString.append(Constant.server + "getEvents");
		String url = urlString.toString();
		
		/*
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		
		HttpResponse httpResponse = httpClient.execute(httpPost);
		HttpEntity httpEntity = httpResponse.getEntity();
         */
		
		JsonParser parser = new JsonParser();
		String content = parser.getJSONFromURL(url);
		Gson gson = new Gson();
		Events events = gson.fromJson(content, Events.class);
		
		Log.d("angel","successfuklly  got events "  );
		return events;
	}
}
