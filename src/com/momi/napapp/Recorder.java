package com.momi.napapp;

import android.content.Context;
import android.media.MediaRecorder;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;

public class Recorder {

	private MediaRecorder mRecorder = null;
	private Context mContext;

	public Recorder(Context applicationContext) {

		mContext = applicationContext;
	}

	private void RecorderErr() {
		mRecorder = null;
	}

	public void RecorderInit() {
		float bak = new CalAvg().Cal(3.0f);
		Log.d("MainActivity", String.valueOf(bak));

		if (mRecorder != null)
			return;

		try {
			mRecorder = new MediaRecorder();
			mRecorder.setAudioSource(1);
			mRecorder.setOutputFormat(1);
			mRecorder.setAudioEncoder(1);
			mRecorder.setOutputFile("/dev/null");
			mRecorder.prepare();
			mRecorder.start();

		} catch (IllegalStateException e) {
			e.printStackTrace();
			RecorderErr();
		} catch (IOException e) {
			e.printStackTrace();
			RecorderErr();
		} catch (Exception e) {
			e.printStackTrace();
			RecorderErr();
		}

		return;
	}

	public void RecorderRel() {

		if (mRecorder != null) {
			try {
				mRecorder.stop();
				mRecorder.release();
				mRecorder = null;
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void SoundDB() {
		float f1 = mRecorder.getMaxAmplitude();
		float f2 = 0;
		TextView localTextView;
		StringBuilder localStringBuilder;

		if (f1 > 0.0F) {
			f2 = (float) (20.0D * Math.log10(f1));
			Log.d("MainActivity", "SoundDB: " + f2);
		}

		localTextView = MainActivity.mSoundDB;
		localTextView.setText(Math.round(f2) + " DB");
	}

	class CalAvg {

		final int a = 4;
		float[] b = new float[a];
		int c = 0;

		private float Cal(float paramFloat) {
			float f3;
			if (paramFloat == 0.0F) {
				f3 = 0.0F;
				return f3;
			}
			c = (1 + c);
			if (c > -1 + b.length)
				c = 0;
			b[c] = paramFloat;
			float[] arrayOfFloat = b;
			int i = arrayOfFloat.length;
			int j = 0;
			float f1 = 0.0F;
			while (true) {
				if (j >= i) {
					f3 = f1 / b.length;
					break;
				}
				float f2 = arrayOfFloat[j];
				if (f2 == 0.0F)
					f2 = paramFloat;
				f1 += f2;
				j++;
			}

			return f3;
		}
	}

}
