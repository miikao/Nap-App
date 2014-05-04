package com.momi.napapp;
import android.provider.BaseColumns;

public final class Table {

	public Table() {}
	
	public static abstract class FeedEntry implements BaseColumns {
		public static final String TABLE_NAME = "values";
		public static final String COLUMN_NAME_ENTRY_ID = "entryid";
		public static final String COLUMN_NAME_DATE = "date";
		public static final String COLUMN_NAME_TEMP = "temp";
		public static final String COLUMN_NAME_CTEMP = "rgbtemp";
		public static final String COLUMN_NAME_LINT = "intensity";
			
		}
	

	private static final String TEXT_TYPE = " TEXT";
	private static final String COMMA_SEP = ",";
	static final String SQL_CREATE_ENTRIES =
	    "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
	    FeedEntry._ID + " INTEGER PRIMARY KEY," +
	    FeedEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
	    FeedEntry.COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP +
	    FeedEntry.COLUMN_NAME_TEMP + TEXT_TYPE + COMMA_SEP +
	    FeedEntry.COLUMN_NAME_CTEMP + TEXT_TYPE + COMMA_SEP +
	    FeedEntry.COLUMN_NAME_LINT + TEXT_TYPE + COMMA_SEP +
	    " )";

	static final String SQL_DELETE_ENTRIES =
	    "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;
	
	
	

}
