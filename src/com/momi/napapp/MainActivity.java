package com.momi.napapp;

import com.sensorcon.sensordrone.DroneEventHandler;
import com.sensorcon.sensordrone.DroneEventObject;
import com.sensorcon.sensordrone.android.Drone;
import com.sensorcon.sensordrone.android.tools.DroneConnectionHelper;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends Activity {
	
	 Button btnConnect;
	    Button btnDisconnect;
	    Button btnMeasure;
	    TextView tvStatus;
	    TextView tvTemperature;
	
	Drone myDrone;
	DroneEventHandler myDroneEventHandler;
	DroneConnectionHelper myHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        myDrone = new Drone();
        myHelper = new DroneConnectionHelper();
        
        tvStatus = (TextView)findViewById(R.id.main_tv_connection_status);

        tvTemperature = (TextView)findViewById(R.id.main_tv_temperature);

        btnConnect = (Button)findViewById(R.id.main_btn_connect);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDrone.isConnected) {
                    genericDialog("Whoa!","You are already connected to a Sensordrone.");
                }
                else {
                    myHelper.connectFromPairedDevices(myDrone, MainActivity.this);
                }

            }
        });
        btnDisconnect = (Button)findViewById(R.id.main_btn_disconnect);
        btnDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDrone.isConnected) {
                    myDrone.disableTemperature();
                    myDrone.setLEDs(0,0,0);
                    myDrone.disconnect();
                }
                else {
        
                    genericDialog("Whoa!","You are not currently connected to a Sensordrone");
                }
            }
        });

        
        btnMeasure = (Button)findViewById(R.id.main_btn_measure);
        btnMeasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDrone.isConnected) {
                    myDrone.measureTemperature();
                }
                else if (myDrone.isConnected && !myDrone.temperatureStatus) {
                    
                    genericDialog("Whoa!","The temperature sensor hasn't been enabled!");
                }
                else {
                    
                    genericDialog("Whoa!","You are not currently connected to a Sensordrone");
                }
            }
        });

        	myDroneEventHandler = new DroneEventHandler() {
            @Override
            public void parseEvent(DroneEventObject droneEventObject) {

                if (droneEventObject.matches(DroneEventObject.droneEventType.CONNECTED)) {
                    myDrone.setLEDs(0,0,126);

                    updateTextViewFromUI(tvStatus, "Connected");

                    myDrone.enableTemperature();
                }
                else if (droneEventObject.matches(DroneEventObject.droneEventType.DISCONNECTED)) {
                    updateTextViewFromUI(tvStatus, "Not connected");
                }
                else if (droneEventObject.matches(DroneEventObject.droneEventType.CONNECTION_LOST)) {
                	updateTextViewFromUI(tvStatus, "Connection lost!");
                    uiToast("Connection lost!");
                }
                else if (droneEventObject.matches(DroneEventObject.droneEventType.TEMPERATURE_ENABLED)) {
                	myDrone.measureTemperature();
                }
                else if (droneEventObject.matches(DroneEventObject.droneEventType.TEMPERATURE_MEASURED)) {

                	String temp = String.format("%.2f \u00B0C",myDrone.temperature_Celsius);
                    updateTextViewFromUI(tvTemperature, temp);

                   
                    uiToast("Temperature updated!");

                }
                else if (droneEventObject.matches(DroneEventObject.droneEventType.TEMPERATURE_DISABLED)) {
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
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
