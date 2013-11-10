package com.angelhack.tripalert.route;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import android.util.Log;

public class JsonParser {

	static InputStream is = null;
	static JSONObject jObj = null;
	static String json = "";

	public JsonParser() {
		// TODO Auto-generated constructor stub
	}

	public String getJSONFromURL(String url) throws Exception {

		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();

			is = httpEntity.getContent();
		} catch (Exception e) {
			Log.d("angel", "error while calling url" + e);
			throw e;
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;

			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
				// Log.e("test: ", sb.toString());
			}

			json = sb.toString();
			is.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("buffer error", "Error converting result " + e.toString());
			throw e;
		}

		return json;
	}

	public List<LatLng> getLatLn(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		JSONArray routeArray = jsonObject.getJSONArray("routes");

		Log.d("angel", "number of routes = "+  routeArray.length());
		
		List<LatLng> latlngs = new ArrayList<LatLng>();

		for (int j = 0; j < routeArray.length(); j++) {
			JSONObject route = (JSONObject) routeArray.get(j);
			JSONArray legsArray = route.getJSONArray("legs");
			
			Log.d("angel", "number of legs = " + routeArray.length());
			

			for (int lg = 0; lg < legsArray.length(); lg++) {
				JSONObject leg = (JSONObject) legsArray.get(lg);
				JSONArray stepsArray = leg.getJSONArray("steps");
				
				Log.d("angel" , "number of steps = " + stepsArray.length());

				for (int sc = 0; sc < stepsArray.length(); sc++) {
					JSONObject step = stepsArray.getJSONObject(sc);
					JSONObject start = step.getJSONObject("start_location");
					JSONObject end = step.getJSONObject("end_location");

					LatLng ltlg_start = getLatLng(start);
					LatLng ltlng_end = getLatLng(end);
					latlngs.add(ltlg_start);
					latlngs.add(ltlng_end);
				}
			}
		}
		return latlngs;
	}

	private LatLng getLatLng(JSONObject latLongJson) throws JSONException {
		String lat = latLongJson.getString("lat");
		String lg = latLongJson.getString("lng");
		double d_lat = Double.parseDouble(lat);
		double d_long = Double.parseDouble(lg);
		
		return new LatLng(d_lat, d_long);

	}

}