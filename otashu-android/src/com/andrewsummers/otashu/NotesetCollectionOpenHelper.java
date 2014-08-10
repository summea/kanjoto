/* Data source based on tutorial by vogella
 * http://www.vogella.com/tutorials/AndroidSQLite/article.html
 * Licensed under:
 * CC BY-NC-SA 3.0 DE: http://creativecommons.org/licenses/by-nc-sa/3.0/de/deed.en
 * Eclipse Public License: https://www.eclipse.org/legal/epl-v10.html
 */

package com.andrewsummers.otashu;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
	
	NotesetCollectionOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	
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
