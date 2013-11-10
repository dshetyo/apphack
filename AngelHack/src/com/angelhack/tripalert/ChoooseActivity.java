package com.angelhack.tripalert;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class ChoooseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chooose);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chooose, menu);
		return true;
	}

	public void createTrip(View view){
		Intent i = new Intent(getBaseContext(), MainActivity.class);                      
		startActivity(i);
	}
	public void createEvent(View view){
		Intent i = new Intent(getBaseContext(), UpdatorActivity.class);                      
		startActivity(i);
	}
}
