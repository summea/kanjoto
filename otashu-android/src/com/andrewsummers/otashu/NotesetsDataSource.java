/* Data source based on tutorial by vogella
 * http://www.vogella.com/tutorials/AndroidSQLite/article.html
 * Licensed under:
 * CC BY-NC-SA 3.0 DE: http://creativecommons.org/licenses/by-nc-sa/3.0/de/deed.en
 * Eclipse Public License: https://www.eclipse.org/legal/epl-v10.html
 */

package com.andrewsummers.otashu;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class NotesetsDataSource {

	private SQLiteDatabase database;
	private NotesetCollectionOpenHelper dbHelper;
	private String[] allColumns = { NotesetCollectionOpenHelper.COLUMN_ID, NotesetCollectionOpenHelper.COLUMN_NOTEVALUES };
	
	public NotesetsDataSource(Context context) {
		dbHelper = new NotesetCollectionOpenHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public Noteset createNoteset(String notevalues) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(NotesetCollectionOpenHelper.COLUMN_NOTEVALUES, notevalues);
		
		long insertId = database.insert(NotesetCollectionOpenHelper.TABLE_NOTESETS, null, contentValues);
		
		Cursor cursor = database.query(NotesetCollectionOpenHelper.TABLE_NOTESETS, allColumns, NotesetCollectionOpenHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
		
		cursor.moveToFirst();
		Noteset newNoteset = cursorToNoteset(cursor);
		cursor.close();
		return newNoteset;
	}
	
	public void deleteNoteset(Noteset noteset) {
		long id = noteset.getId();
		Log.d("OTASHULOG", "deleting noteset with id: " + id);
		database.delete(NotesetCollectionOpenHelper.TABLE_NOTESETS, NotesetCollectionOpenHelper.COLUMN_ID + " = " + id, null);
	}
	
	public List<Noteset> getAllNotesets() {
		List<Noteset> notesets = new ArrayList<Noteset>();
		
		Cursor cursor = database.query(NotesetCollectionOpenHelper.TABLE_NOTESETS, allColumns, null, null, null, null, null);
		
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Noteset noteset = cursorToNoteset(cursor);
			notesets.add(noteset);
			cursor.moveToNext();
		}
		
		cursor.close();
		return notesets;
	}
	
	private Noteset cursorToNoteset(Cursor cursor) {
		Noteset noteset = new Noteset();
		noteset.setId(cursor.getLong(0));
		noteset.setNotevalues(cursor.getString(1));
		return noteset;
	}
}