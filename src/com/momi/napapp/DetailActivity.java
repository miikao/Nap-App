package com.momi.napapp;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
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
		
		//tv = (TextView) findViewById(R.id.rowsview);
		//Log.d("database", dbh.getReading(1).toString());
		//tv.setText(dbh.getReadings().toString());
		
		TableLayout stk = (TableLayout) findViewById(R.id.table_main);
        TableRow tbrow0 = new TableRow(this);
        
        TextView tv0 = new TextView(this);
        tv0.setText(" Date ");
        tv0.setTextColor(Color.WHITE);
        tbrow0.addView(tv0);
        
        TextView tv1 = new TextView(this);
        tv1.setText(" Temp ");
        tv1.setTextColor(Color.WHITE);
        tbrow0.addView(tv1);
        
        TextView tv2 = new TextView(this);
        tv2.setText("Color Temp ");
        tv2.setTextColor(Color.WHITE);
        tbrow0.addView(tv2);
        
        TextView tv3 = new TextView(this);
        tv3.setText(" Intensity ");
        tv3.setTextColor(Color.WHITE);
        tbrow0.addView(tv3);
        
        
        TextView tv4 = new TextView(this);
        tv4.setText(" Location ");
        tv4.setTextColor(Color.WHITE);
        tbrow0.addView(tv4);
        
        stk.addView(tbrow0);
        
        for (Reading r : dbh.getReadings() ) {
        	TableRow tbrow = new TableRow(this);
           
        	TextView t1v = new TextView(this);   
            t1v.setText(r._date);
            t1v.setTextColor(Color.WHITE);
            t1v.setGravity(Gravity.CENTER);
            tbrow.addView(t1v);
            
            TextView t2v = new TextView(this);
            t2v.setText(r.getTemp());
            t2v.setTextColor(Color.WHITE);
            t2v.setGravity(Gravity.CENTER);
            tbrow.addView(t2v);
            
            TextView t3v = new TextView(this);
            t3v.setText(r.getCTemp());
            t3v.setTextColor(Color.WHITE);
            t3v.setGravity(Gravity.CENTER);
            tbrow.addView(t3v);
            
            TextView t4v = new TextView(this);
            t4v.setText(r.getIntensity());
            t4v.setTextColor(Color.WHITE);
            t4v.setGravity(Gravity.CENTER);
            tbrow.addView(t4v);
            
            stk.addView(tbrow);

		}
        
	}

}
