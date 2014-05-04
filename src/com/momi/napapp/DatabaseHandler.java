package com.momi.napapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "tableSet";

	private static final String TABLE_VALUES = "values666";

	private static final String KEY_ID = "id";
	private static final String NAME_DATE = "date";
	private static final String NAME_TEMP = "temp";
	private static final String NAME_CTEMP = "rgbtemp";
	private static final String NAME_LINT = "intensity";

	private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
			+ TABLE_VALUES;

	// counts the amount of columns in the table
	private int count = 0;

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_VALUES_TABLE = "CREATE TABLE " + TABLE_VALUES + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + NAME_DATE + " TEXT,"
				+ NAME_TEMP + " TEXT," + NAME_CTEMP + " TEXT," + NAME_LINT
				+ " TEXT" + ")";

		db.execSQL(CREATE_VALUES_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		// This database is only a cache for online data, so its upgrade policy
		// is
		// to simply to discard the data and start over
		db.execSQL(SQL_DELETE_ENTRIES);
		onCreate(db);
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}

	// saves the current sensor info into the table
	public void saveReadings(String date, String temp, String rgbtemp,
			String intensity) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(NAME_DATE, date);
		values.put(NAME_TEMP, temp);
		values.put(NAME_CTEMP, rgbtemp);
		values.put(NAME_LINT, intensity);

		db.insert(TABLE_VALUES, null, values);
		count++;
		db.close();
	}

	// returns the ID of last column in the table. UNRELIABLE in the case of deleting/updating stuff
	public int getIndexOfLast() {
		return count;
	}

	//returns wanted reading from the table as an object
	public Reading getReading(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_VALUES, new String[] { KEY_ID,
				NAME_DATE, NAME_TEMP, NAME_CTEMP, NAME_LINT }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		Reading reading = new Reading(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2), cursor.getString(3),
				cursor.getString(4));

		return reading;

	}
}
