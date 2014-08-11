package com.andrewsummers.otashu;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * NotesetsDataSource is a data source that provides database functionality
 * for noteset-related data (e.g. CRUD) actions.
 * 
 * Note: Data source based on tutorial by vogella
 * http://www.vogella.com/tutorials/AndroidSQLite/article.html
 * Licensed under:
 * CC BY-NC-SA 3.0 DE: http://creativecommons.org/licenses/by-nc-sa/3.0/de/deed.en
 * Eclipse Public License: https://www.eclipse.org/legal/epl-v10.html
 */
public class NotesetsDataSource {

	private SQLiteDatabase database;
	private NotesetCollectionOpenHelper dbHelper;
	private String[] allColumns = { NotesetCollectionOpenHelper.COLUMN_ID, NotesetCollectionOpenHelper.COLUMN_NOTEVALUES };
	
	/**
	 * NotesetsDataSource constructor.
	 * 
	 * @param context	Current state.
	 */
	public NotesetsDataSource(Context context) {
		dbHelper = new NotesetCollectionOpenHelper(context);
	}
	
	/**
	 * Open database.
	 * @throws SQLException
	 */
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	/**
	 * Close database.
	 */
	public void close() {
		dbHelper.close();
	}
	
	/**
	 * Create noteset row in database.
	 * 
	 * @param notevalues	String of note values to insert.
	 * @return Noteset of newly-created noteset data.
	 */
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
	
	/**
	 * Delete noteset row from database.
	 * 
	 * @param noteset	Noteset to delete.
	 */
	public void deleteNoteset(Noteset noteset) {
		long id = noteset.getId();
		Log.d("OTASHULOG", "deleting noteset with id: " + id);
		database.delete(NotesetCollectionOpenHelper.TABLE_NOTESETS, NotesetCollectionOpenHelper.COLUMN_ID + " = " + id, null);
	}
	
	/**
	 * Get all notesets from database table.
	 * 
	 * @return List of Notesets.
	 */
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
	
	/**
	 * Access column data at current position of result.
	 *  
	 * @param cursor	Current cursor location.
	 * @return Noteset
	 */
	private Noteset cursorToNoteset(Cursor cursor) {
		Noteset noteset = new Noteset();
		noteset.setId(cursor.getLong(0));
		noteset.setNotevalues(cursor.getString(1));
		return noteset;
	}
}