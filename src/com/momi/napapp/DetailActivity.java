package com.momi.napapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class DetailActivity extends Activity {
	
	TextView tv;
	DatabaseHandler dbh;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		dbh = new DatabaseHandler(this);
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		tv = (TextView) findViewById(R.id.rowsview);
		Log.d("database", dbh.getReading(1).toString());
		tv.setText(dbh.getReadings().toString());
	}

}
