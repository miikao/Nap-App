package com.momi.napapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;


import com.momi.napapp.Table.FeedEntry;

import com.sensorcon.sensordrone.DroneEventHandler;
import com.sensorcon.sensordrone.DroneEventObject;
import com.sensorcon.sensordrone.android.Drone;
import com.sensorcon.sensordrone.android.tools.DroneConnectionHelper;

public class MainActivity extends Activity {

	Button btnConnect;
	Button btnDisconnect;
	Button btnMeasure;
	Button btnSave;
	TextView tvStatus;
	TextView tvTemperature;
	TextView rgbcTemperature;
	TextView lightIntensity;

	Drone myDrone;
	DroneEventHandler myDroneEventHandler;
	DroneConnectionHelper myHelper;
	FeedReaderDbHelper mDbHelper;
	Table table;

	// needed to transfer readings to table

	int id = 0;
	String intensity;
	String temp;
	String rgbtemp;
	// no idea if this works
	String date = java.text.DateFormat.getDateTimeInstance().format(
			Calendar.getInstance().getTime());

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		myDrone = new Drone();
		myHelper = new DroneConnectionHelper();
		mDbHelper = new FeedReaderDbHelper(getBaseContext());
		table = new Table();
		
		tvStatus = (TextView) findViewById(R.id.main_tv_connection_status);

		tvTemperature = (TextView) findViewById(R.id.main_tv_temperature);
		rgbcTemperature = (TextView) findViewById(R.id.main_tv_colortemp);
		lightIntensity = (TextView) findViewById(R.id.main_tv_intensity);

		btnConnect = (Button) findViewById(R.id.main_btn_connect);
		btnConnect.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (myDrone.isConnected) {
					genericDialog("Whoa!",
							"You are already connected to a Sensordrone.");
				} else {
					myHelper.connectFromPairedDevices(myDrone,
							MainActivity.this);
				}

			}
		});
		btnDisconnect = (Button) findViewById(R.id.main_btn_disconnect);
		btnDisconnect.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (myDrone.isConnected) {
					myDrone.disableTemperature();
					myDrone.disableRGBC();
					myDrone.setLEDs(0, 0, 0);
					myDrone.disconnect();
				} else {

					genericDialog("Whoa!",
							"You are not currently connected to a Sensordrone");
				}
			}
		});

		btnMeasure = (Button) findViewById(R.id.main_btn_measure);
		btnMeasure.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (myDrone.isConnected) {
					myDrone.measureTemperature();
					myDrone.measureRGBC();
				} else if (myDrone.isConnected && !myDrone.temperatureStatus) {

					genericDialog("Whoa!",
							"The temperature sensor hasn't been enabled!");
				} else if (myDrone.isConnected && !myDrone.rgbcStatus) {
					genericDialog("Whoa!",
							"The RGB-temperature sensor hasn't been enabled!");
				} else {

					genericDialog("Whoa!",
							"You are not currently connected to a Sensordrone");
				}
			}
		});

		btnSave = (Button) findViewById(R.id.main_btn_save);
		btnSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!myDrone.isConnected) {
					genericDialog("Whoa!", "Connect to a Sensordrone first!");
				} else {
					// TODO Auto-generated method stub
					++id;
					SQLiteDatabase db = mDbHelper.getWritableDatabase();

					ContentValues values = new ContentValues();
//					values.put(FeedEntry.COLUMN_NAME_ENTRY_ID, id);
					values.put(FeedEntry.COLUMN_NAME_DATE, date);
					values.put(FeedEntry.COLUMN_NAME_TEMP, temp);
					values.put(FeedEntry.COLUMN_NAME_CTEMP, rgbtemp);
					values.put(FeedEntry.COLUMN_NAME_LINT, intensity);

					long newRowId;
					newRowId = db.insert(FeedEntry.TABLE_NAME,
							null, values);
					db.close();
				}
				
				uiToast("Values saved!");
			}
		});

		myDroneEventHandler = new DroneEventHandler() {
			@Override
			public void parseEvent(DroneEventObject droneEventObject) {

				if (droneEventObject
						.matches(DroneEventObject.droneEventType.CONNECTED)) {
					myDrone.setLEDs(0, 0, 126);

					updateTextViewFromUI(tvStatus, "Connected");

					myDrone.enableTemperature();
					myDrone.enableRGBC();
				} else if (droneEventObject
						.matches(DroneEventObject.droneEventType.DISCONNECTED)) {
					updateTextViewFromUI(tvStatus, "Not connected");

				} else if (droneEventObject
						.matches(DroneEventObject.droneEventType.CONNECTION_LOST)) {
					updateTextViewFromUI(tvStatus, "Connection lost!");
					uiToast("Connection lost!");

				} else if (droneEventObject
						.matches(DroneEventObject.droneEventType.TEMPERATURE_ENABLED)) {
					myDrone.measureTemperature();

				} else if (droneEventObject
						.matches(DroneEventObject.droneEventType.RGBC_ENABLED)) {
					myDrone.measureRGBC();

				} else if (droneEventObject
						.matches(DroneEventObject.droneEventType.TEMPERATURE_MEASURED)) {

					temp = String.format("%.2f \u00B0C",
							myDrone.temperature_Celsius);
					updateTextViewFromUI(tvTemperature, temp);

					uiToast("Measurements updated!");

				} else if (droneEventObject
						.matches(DroneEventObject.droneEventType.RGBC_MEASURED)) {

					rgbtemp = String.format("%.1f K",
							myDrone.rgbcColorTemperature);
					updateTextViewFromUI(rgbcTemperature, rgbtemp);

					intensity = String.format("%.1f lux", myDrone.rgbcLux);
					updateTextViewFromUI(lightIntensity, intensity);

				} else if (droneEventObject
						.matches(DroneEventObject.droneEventType.TEMPERATURE_DISABLED)) {
				} else if (droneEventObject
						.matches(DroneEventObject.droneEventType.RGBC_DISABLED)) {

				}
			}
		};

	}

	@Override
	protected void onResume() {
		super.onResume();
		myDrone.registerDroneListener(myDroneEventHandler);
	}

	@Override
	protected void onPause() {
		super.onPause();
		myDrone.unregisterDroneListener(myDroneEventHandler);
	}

	public void genericDialog(String title, String msg) {
		Dialog dialog;
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setTitle(title);
		builder.setMessage(msg);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//
			}
		});
		dialog = builder.create();
		dialog.show();
	}

	public void updateTextViewFromUI(final TextView textView, final String text) {
		MainActivity.this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				textView.setText(text);
			}
		});
	}

	public void uiToast(final String msg) {
		MainActivity.this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT)
						.show();
			}
		});
	}

//	 @Override
//	 protected void onStart() {
//	 super.onStart();
//	 startActivity(new Intent(MainActivity.this, SoundActivity.class));
//	 finish();
//	 }
//	
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//	}

}
