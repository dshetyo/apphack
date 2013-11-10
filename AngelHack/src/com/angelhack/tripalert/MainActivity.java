package com.angelhack.tripalert;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.angelhack.tripalert.model.Event;
import com.angelhack.tripalert.model.Events;
import com.angelhack.tripalert.model.Path;
import com.angelhack.tripalert.model.TravelPoint;
import com.angelhack.tripalert.route.JsonParser;
import com.angelhack.tripalert.route.RouteFinder;
import com.angelhack.tripalert.util.Constant;
import com.angelhack.tripalert.util.GetEvents;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.GeoPoint;
import com.google.gson.Gson;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;

public class MainActivity extends FragmentActivity implements LocationListener,
		OnInfoWindowClickListener, OnMapLongClickListener {

	static final LatLng HAMBURG = new LatLng(53.558, 9.927);
	static final LatLng KIEL = new LatLng(53.551, 9.993);
	static final LatLng MS_HYD = new LatLng(17.435943700000000000,
			78.341673099999980000);
	static final LatLng MIND_SPACE = new LatLng(17.440994100000000000,
			78.377730700000030000);

	LatLng srcLatLng;
	LatLng destLatLng;

	Marker srcMarker;
	Marker targetMarker;

	List<Marker> allMarkers = new ArrayList<Marker>();

	int marketCount = 0;

	LatLng currrentPositon;
	private GoogleMap map;

	private List<LatLng> pathLatLngs;
	private Date tripDate;
	
	Set<Integer> eventIdSet = new HashSet<Integer>();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		SupportMapFragment smf = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);

		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		Log.d("angel", "requesting location now ");

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, this);

		Log.d("angel", "requested  current postion ");

		map = smf.getMap();

		map.moveCamera(CameraUpdateFactory.newLatLngZoom(MIND_SPACE, 15));

		// Move the camera instantly to hamburg with a zoom of 15.
		// map.moveCamera(CameraUpdateFactory.newLatLngZoom(HAMBURG, 15));

		if (map != null) {

			/*
			 * Marker msMrk = map.addMarker(new MarkerOptions().position(MS_HYD)
			 * .title("ms")); Marker mindspaceMrk = map.addMarker(new
			 * MarkerOptions().position( MIND_SPACE).title("ms"));
			 */
			/*
			 * Marker hamburg = map.addMarker(new
			 * MarkerOptions().position(MS_HYD) .title("ms")); Marker kiel =
			 * map.addMarker(new MarkerOptions() .position(MS_HYD) .title("ms")
			 * .snippet("Kiel is cool") .icon(BitmapDescriptorFactory
			 * .fromResource(R.drawable.ic_launcher)));
			 */

			map.setOnMapLongClickListener(this);
		}
		
		AlertsTask alertTask = new AlertsTask();
		Void x = null;
		alertTask.execute(x);
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.d("angel", "location changed ");
		double latitude = location.getLatitude();
		double longitude = location.getLongitude();

		currrentPositon = new LatLng(latitude, longitude);

		map.moveCamera(CameraUpdateFactory.newLatLngZoom(currrentPositon, 15));

		Log.d("angel", "current latlong " + latitude + " " + longitude);
		Toast.makeText(this, latitude + " " + longitude, 150);
	}

	@Override
	public void onProviderDisabled(String provider) {
		Log.d("angel", "onProviderDisabled");
	}

	@Override
	public void onProviderEnabled(String provider) {
		Log.d("Latitude", "onProviderEnabled");
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.d("Latitude", "onStatusChanged");

	}

	public void onGetRoute(View view) {
		Log.d("angel", "on get route clicked");

		GetRouteTask getRouteTask = new GetRouteTask();
		getRouteTask.execute(new LatLng[] { srcLatLng, destLatLng });

	}

	@Override
	public void onMapLongClick(LatLng point) {
		Log.d("angel", "adding new marker");

		if (marketCount % 2 == 0) {
			srcMarker = map.addMarker(new MarkerOptions()
					.position(point)
					.title("src")
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_launcher)));

			srcLatLng = point;
			allMarkers.add(srcMarker);
		} else {
			targetMarker = map.addMarker(new MarkerOptions()
					.position(point)
					.title("dest")
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_launcher)));

			destLatLng = point;
			allMarkers.add(targetMarker);
		}

		marketCount++;

	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		// TODO Auto-generated method stub

	}

	class GetRouteTask extends AsyncTask<LatLng, Void, Void> {

		String op;

		@Override
		protected Void doInBackground(LatLng... latLngs) {
			Log.d("angel", "geting route now");
			RouteFinder rf = new RouteFinder();
			String url = rf.makeUrl(latLngs[0], latLngs[1]);

			JsonParser jsonParser = new JsonParser();
			String obj;
			try {
				obj = jsonParser.getJSONFromURL(url);
				this.op = obj;

				pathLatLngs = jsonParser.getLatLn(obj);
				for (LatLng ll : pathLatLngs) {
					Log.d("angel", "lat longs = " + ll.toString());
				}

			} catch (Exception e) {
				Log.e("angle", "error while calling getroute", e);
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void v) {
			Log.d("angel", "result from api" + op);

			Log.d("angel", "number of latlngs " + pathLatLngs.size());
			for (LatLng ltLng : pathLatLngs) {
				Marker m = map.addMarker(new MarkerOptions().position(ltLng)
						.title(""));
				allMarkers.add(m);
			}
		}

	}

	public void onClear() {
		clear();
		if (srcMarker != null) {
			srcMarker.remove();
			srcLatLng = null;
		}

		if (targetMarker != null) {
			targetMarker.remove();
			destLatLng = null;
		}
	}

	public void onSaveTrip(View view) {
		SaveTripTask saveTask = new SaveTripTask();
		Void x = null;
		saveTask.execute(x);
	}

	public void saveTrip() throws Exception {
		Path p = new Path();
		List<TravelPoint> tpList = new ArrayList<TravelPoint>();
		TravelPoint tp;
		com.angelhack.tripalert.model.Location loc;
		for (LatLng ll : pathLatLngs) {
			tp = new TravelPoint();
			loc = new com.angelhack.tripalert.model.Location();
			loc.setLatitude(ll.latitude);
			loc.setLongitude(ll.longitude);
			tp.setLocation(loc);
			tp.setTravelTime(tripDate);
			tpList.add(tp);

		}

		p.setTravelPointList(tpList);

		StringBuilder urlString = new StringBuilder();
		Gson gson = new Gson();
		String arg = gson.toJson(p);
		urlString.append(Constant.server + "savePath?arg="
				+ URLEncoder.encode(arg));

		String url = urlString.toString();
		
		Log.d("urll" ,url);
		
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);

		HttpResponse httpResponse = httpClient.execute(httpPost);
		HttpEntity httpEntity = httpResponse.getEntity();

		Log.d("angel", "successfuklly savedTrip ");

	}

	private void clear() {
		marketCount = 0;
		for (Marker m : allMarkers) {
			m.remove();
		}
	}

	class SaveTripTask extends AsyncTask<Void, Void, Void> {

		boolean success = false;

		@Override
		protected Void doInBackground(Void... latLngs) {
			Log.d("angel", "saving route now");
			try {
				saveTrip();
				success = true;
			} catch (Exception e) {
				Log.d("angel", "error while saving trip", e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {
			if (success == true) {
				clear();
				Toast.makeText(MainActivity.this, "saved Trip", 300).show();
			} else {
				Toast.makeText(MainActivity.this, "Error in saving Trip", 300)
						.show();
			}

		}

	}

	class AlertsTask extends AsyncTask<Void,Integer, Void> {

	    GetEvents getEvents = new GetEvents();
		private  final Void voidd = null;
		
		private Events events ;

		
		
		@Override
		protected Void doInBackground(Void... params) {
			while(true){
				try {
					Events events = getEvents.getEvents();
					this.events = events;
					java.util.Random r = new Random();
					int no = r.nextInt();
					publishProgress(no);
				} catch (Exception e) {
					Log.e("angel", "error in getEvents",e);
				}finally{
					try {
						Thread.sleep(1000 *5);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Log.e("angel", "error" ,e);
					}
				}
				
			}
			
		}

		
		protected void onProgressUpdate(Integer... progress) {
			Log.d("angel","updating progress");
			if(events!= null && events.getEvents()!= null){
				Log.d("angel", "no of events received = " + events.getEvents().size());
			
			  for(Event e: events.getEvents()){
				  if(!eventIdSet.contains(e.getEventId())){
					  com.angelhack.tripalert.model.Location l = e.getTravelPoint().getLocation();
					  LatLng ln = new LatLng(l.getLatitude(), l.getLongitude());
					  Marker msMrk = map.addMarker(new MarkerOptions().position(ln));
				  }
			  }
			}
			//Toast.makeText(MainActivity.this, "events", 100).show();
		}

		protected void onPostExecute(Long result) {
			
		}
	}

}
