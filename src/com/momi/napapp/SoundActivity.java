package com.momi.napapp;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class SoundActivity extends Activity {

	private Recorder mRecorder;
	private SoundPlay mSoundPlay;
	private Handler mHandler = new Handler();
	static TextView mSoundDB;

	private Runnable mUpdateTimer = new Runnable() { 
		@Override
		public void run() {
			mRecorder.SoundDB();
			mHandler.postDelayed(mUpdateTimer, 200L);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mRecorder = new Recorder(getApplicationContext());
		mSoundPlay = new SoundPlay(getApplicationContext());
		mSoundDB = (TextView) findViewById(R.id.text_sounddb);
	}

	protected void onDestroy() {
		super.onDestroy();

		if (Build.VERSION.SDK_INT < 14)
			System.exit(0);
	}

	protected void onResume() {
		super.onResume();
	}

	protected void onStart() {
		super.onStart();
		mRecorder.RecorderInit();
		//Causes the Runnable r to be added to the message queue, to be run after the specified amount of time elapses.
		mHandler.postDelayed(mUpdateTimer, 200L);
		mSoundPlay.a();
	}

	protected void onStop() {
		super.onStop();
		mRecorder.RecorderRel();
		//Remove any pending posts of Runnable r that are in the message queue.
		mHandler.removeCallbacks(mUpdateTimer);
	}

}
