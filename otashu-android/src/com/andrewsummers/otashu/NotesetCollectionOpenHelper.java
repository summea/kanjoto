package com.andrewsummers.otashu;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * NotesetCollectionOpenHelper is an SQLiteOpenHelper that simplifies
 * connection access to application database.
 * 
 * Note: Data source based on tutorial by vogella
 * http://www.vogella.com/tutorials/AndroidSQLite/article.html
 * Licensed under:
 * CC BY-NC-SA 3.0 DE: http://creativecommons.org/licenses/by-nc-sa/3.0/de/deed.en
 * Eclipse Public License: https://www.eclipse.org/legal/epl-v10.html
 */
public class NotesetCollectionOpenHelper extends SQLiteOpenHelper {
	
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "noteset_collection.db";
	
	public static final String TABLE_NOTESETS = "notesets";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NOTEVALUES =  "notevalues";
	
	private static final String DATABASE_CREATE =
			"CREATE TABLE " + TABLE_NOTESETS + " (" +
			COLUMN_ID + " integer primary key autoincrement, " +
			COLUMN_NOTEVALUES + " text);";
	
	/**
	 * NotesetCollectionOpenHelper constructor.
	 * 
	 * @param context	Current state.
	 */
	NotesetCollectionOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * onCreate override that creates application database.
	 * 
	 * @param db	<code>SQLiteDatabase</code> database instance.
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);	
	}

	/**
	 * onUpgrade override that handles database changes.
	 * 
	 * @param db	<code>SQLiteDatabase</code> database instance.
	 * @param oldVersion	<code>int</code> value of old version number
	 * @param newVersion	<code>int</code> value of new version number
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}
	
	/**
	 * getAllNotesets gets all notesets.
	 * 
	 * @return List of Notesets.
	 */
	public List<Noteset> getAllNotesets() {
		
		List<Noteset> notesets = new LinkedList<Noteset>();
		
		String query = "SELECT * FROM " + TABLE_NOTESETS;
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		
		Noteset noteset = null;
		if (cursor.moveToFirst()) {
			do {
				noteset = new Noteset();
				noteset.setId(Integer.parseInt(cursor.getString(0)));
				noteset.setNotevalues(cursor.getString(1));
				
				notesets.add(noteset);
			} while (cursor.moveToNext());
		}
		
		Log.d("MYLOG", notesets.toString());
		
		return notesets;
	}
	
	/**
	 * getAllNotesets gets a preview list of all notesets.
	 * 
	 * @return List of Noteset preview strings.
	 */
	public List<String> getAllNotesetListPreviews() {
		
		List<String> notesets = new LinkedList<String>();
		
		String query = "SELECT * FROM " + TABLE_NOTESETS;
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		
		Noteset noteset = null;
		if (cursor.moveToFirst()) {
			do {
				noteset = new Noteset();
				noteset.setId(Integer.parseInt(cursor.getString(0)));
				noteset.setNotevalues(cursor.getString(1));
				
				notesets.add(noteset.toString());
			} while (cursor.moveToNext());
		}
		
		Log.d("MYLOG", notesets.toString());
		
		return notesets;
	}
}