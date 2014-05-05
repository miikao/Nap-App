package com.momi.napapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import java.util.Calendar;







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
	static TextView mSoundDB;
	
	private Recorder mRecorder;
	private SoundPlay mSoundPlay;
	private Handler mHandler = new Handler();
	
	private Runnable mUpdateTimer = new Runnable() { 
		@Override
		public void run() {
			mRecorder.SoundDB();
			mHandler.postDelayed(mUpdateTimer, 200L);
		}
	};
	
	Drone myDrone;
	DroneEventHandler myDroneEventHandler;
	DroneConnectionHelper myHelper;
	
	DatabaseHandler mDbHelper;

	// needed to transfer readings to table

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
		mDbHelper = new DatabaseHandler(this);

		
		tvStatus = (TextView) findViewById(R.id.main_tv_connection_status);

		tvTemperature = (TextView) findViewById(R.id.main_tv_temperature);
		rgbcTemperature = (TextView) findViewById(R.id.main_tv_colortemp);
		lightIntensity = (TextView) findViewById(R.id.main_tv_intensity);
		mRecorder = new Recorder(getApplicationContext());
		mSoundPlay = new SoundPlay(getApplicationContext());
		mSoundDB = (TextView) findViewById(R.id.text_sounddb);

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

					Log.d("Insert: ", "Inserting .."); 
					mDbHelper.saveReadings(date, temp, rgbtemp, intensity);
					Log.d("database output", mDbHelper.getReading(1).toString());
					
					uiToast("Values saved!");
				}
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

	public void gotoActivity(View v) {
		
		Intent intent = new Intent(this,DetailActivity.class);
		startActivity(intent);
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (Build.VERSION.SDK_INT < 14)
			System.exit(0);
	}

	@Override
	protected void onStart() {
		super.onStart();
		mRecorder.RecorderInit();
		//Causes the Runnable r to be added to the message queue, to be run after the specified amount of time elapses.
		mHandler.postDelayed(mUpdateTimer, 200L);
		mSoundPlay.a();
	}

	@Override
	protected void onStop() {
		super.onStop();
		mRecorder.RecorderRel();
		//Remove any pending posts of Runnable r that are in the message queue.
		mHandler.removeCallbacks(mUpdateTimer);
	}

}
