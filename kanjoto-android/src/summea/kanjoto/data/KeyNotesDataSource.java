
package summea.kanjoto.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import summea.kanjoto.model.KeyNote;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class KeyNotesDataSource {
    private SQLiteDatabase database;
    private KanjotoDatabaseHelper dbHelper;

    // database table columns
    private String[] allColumns = {
            KanjotoDatabaseHelper.COLUMN_ID,
            KanjotoDatabaseHelper.COLUMN_KEY_SIGNATURE_ID,
            KanjotoDatabaseHelper.COLUMN_NOTEVALUE,
            KanjotoDatabaseHelper.COLUMN_WEIGHT,
            KanjotoDatabaseHelper.COLUMN_APPRENTICE_ID,
    };

    /**
     * KeyNotesDataSource constructor.
     * 
     * @param context Current state.
     */
    public KeyNotesDataSource(Context context) {
        dbHelper = new KanjotoDatabaseHelper(context);
    }

    /**
     * KeyNotesDataSource constructor.
     * 
     * @param context Current state.
     * @param databaseName Database to use.
     */
    public KeyNotesDataSource(Context context, String databaseName) {
        dbHelper = new KanjotoDatabaseHelper(context, databaseName);
    }

    /**
     * Open database.
     * 
     * @throws SQLException
     */
    public void open() throws SQLException {
        setDatabase(dbHelper.getWritableDatabase());
    }

    /**
     * Close database.
     */
    public void close() {
        dbHelper.close();
    }

    /**
     * Create keyNote row in database.
     * 
     * @param keyNotevalues String of keyNote values to insert.
     * @return KeyNote of newly-created keyNote data.
     */
    public KeyNote createKeyNote(KeyNote keyNote) {
        ContentValues contentValues = new ContentValues();
        contentValues
                .put(KanjotoDatabaseHelper.COLUMN_KEY_SIGNATURE_ID, keyNote.getKeySignatureId());
        contentValues.put(KanjotoDatabaseHelper.COLUMN_NOTEVALUE, keyNote.getNotevalue());
        contentValues.put(KanjotoDatabaseHelper.COLUMN_WEIGHT, keyNote.getWeight());
        contentValues.put(KanjotoDatabaseHelper.COLUMN_APPRENTICE_ID, keyNote.getApprenticeId());

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long insertId = db
                .insert(KanjotoDatabaseHelper.TABLE_KEY_NOTES, null,
                        contentValues);

        Cursor cursor = db.query(
                KanjotoDatabaseHelper.TABLE_KEY_NOTES, allColumns,
                KanjotoDatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();

        KeyNote newKeyNote = cursorToKeyNote(cursor);
        cursor.close();
        db.close();

        return newKeyNote;
    }

    /**
     * Delete keyNote row from database.
     * 
     * @param keyNote KeyNote to delete.
     */
    public void deleteKeyNote(KeyNote keyNote) {
        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // delete noteset
        db.delete(KanjotoDatabaseHelper.TABLE_KEY_NOTES,
                KanjotoDatabaseHelper.COLUMN_ID + " = " + keyNote.getId(), null);

        db.close();
    }

    /**
     * Get all keyNotes from database table.
     * 
     * @return List of KeyNotes.
     */
    public List<KeyNote> getAllKeyNotes(long apprenticeId) {
        List<KeyNote> keyNotes = new ArrayList<KeyNote>();

        String query = "SELECT * FROM " + KanjotoDatabaseHelper.TABLE_KEY_NOTES
                + " WHERE " + KanjotoDatabaseHelper.COLUMN_APPRENTICE_ID + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(apprenticeId),
        });

        KeyNote keyNote = null;
        if (cursor.moveToFirst()) {
            do {
                // create note objects based on note data from database
                keyNote = new KeyNote();
                keyNote.setId(cursor.getLong(0));
                keyNote.setKeySignatureId(cursor.getLong(1));
                keyNote.setNotevalue(cursor.getInt(2));
                keyNote.setWeight(cursor.getFloat(3));
                keyNote.setApprenticeId(cursor.getLong(4));

                // add note string to list of strings
                keyNotes.add(keyNote);
            } while (cursor.moveToNext());
        }

        db.close();

        return keyNotes;
    }

    /**
     * Access column data at current position of result.
     * 
     * @param cursor Current cursor location.
     * @return KeyNote
     */
    private KeyNote cursorToKeyNote(Cursor cursor) {
        KeyNote keyNote = new KeyNote();
        keyNote.setId(cursor.getLong(0));
        keyNote.setKeySignatureId(cursor.getLong(1));
        keyNote.setNotevalue(cursor.getInt(2));
        keyNote.setWeight(cursor.getFloat(3));
        keyNote.setApprenticeId(cursor.getLong(4));
        return keyNote;
    }

    public KeyNote getKeyNote(long keyNoteId) {
        KeyNote keyNote = new KeyNote();

        String query = "SELECT * FROM " + KanjotoDatabaseHelper.TABLE_KEY_NOTES
                + " WHERE " + KanjotoDatabaseHelper.COLUMN_ID + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all keyNotes from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(keyNoteId),
        });

        if (cursor.moveToFirst()) {
            do {
                // create keyNote objects based on keyNote data from database
                keyNote = new KeyNote();
                keyNote.setId(cursor.getLong(0));
                keyNote.setKeySignatureId(cursor.getLong(1));
                keyNote.setNotevalue(cursor.getInt(2));
                keyNote.setWeight(cursor.getFloat(3));
                keyNote.setApprenticeId(cursor.getLong(4));
            } while (cursor.moveToNext());
        }

        db.close();

        return keyNote;
    }

    public List<KeyNote> getKeyNotesByKeySignature(long keySignatureId) {
        List<KeyNote> keyNotes = new ArrayList<KeyNote>();

        String query = "SELECT * FROM " + KanjotoDatabaseHelper.TABLE_KEY_NOTES
                + " WHERE " + KanjotoDatabaseHelper.COLUMN_KEY_SIGNATURE_ID + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all keyNotes from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(keySignatureId),
        });

        if (cursor.moveToFirst()) {
            do {
                // create keyNote objects based on keyNote data from database
                KeyNote keyNote = new KeyNote();
                keyNote.setId(cursor.getLong(0));
                keyNote.setKeySignatureId(cursor.getLong(1));
                keyNote.setNotevalue(cursor.getInt(2));
                keyNote.setWeight(cursor.getFloat(3));
                keyNote.setApprenticeId(cursor.getLong(4));
                keyNotes.add(keyNote);
            } while (cursor.moveToNext());
        }

        db.close();

        return keyNotes;
    }

    public List<Integer> getKeyNoteNotevaluesByKeySignature(long keySignatureId) {
        List<Integer> keyNotes = new ArrayList<Integer>();

        String query = "SELECT * FROM " + KanjotoDatabaseHelper.TABLE_KEY_NOTES
                + " WHERE " + KanjotoDatabaseHelper.COLUMN_KEY_SIGNATURE_ID + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all keyNotes from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(keySignatureId),
        });

        if (cursor.moveToFirst()) {
            do {
                keyNotes.add(cursor.getInt(2));
            } while (cursor.moveToNext());
        }

        db.close();

        return keyNotes;
    }

    public KeyNote updateKeyNote(KeyNote keyNote) {

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KanjotoDatabaseHelper.COLUMN_ID, keyNote.getId());
        contentValues
                .put(KanjotoDatabaseHelper.COLUMN_KEY_SIGNATURE_ID, keyNote.getKeySignatureId());
        contentValues.put(KanjotoDatabaseHelper.COLUMN_NOTEVALUE, keyNote.getNotevalue());
        contentValues.put(KanjotoDatabaseHelper.COLUMN_WEIGHT, keyNote.getWeight());
        contentValues.put(KanjotoDatabaseHelper.COLUMN_APPRENTICE_ID, keyNote.getApprenticeId());

        db.update(KanjotoDatabaseHelper.TABLE_KEY_NOTES, contentValues,
                KanjotoDatabaseHelper.COLUMN_ID + "=" + keyNote.getId(), null);

        db.close();

        return keyNote;
    }

    public List<Long> keySignatureIdsThatContain(long apprenticeId, int notevalue) {
        List<Long> keySignatureIds = new ArrayList<Long>();

        String query = "SELECT " + KanjotoDatabaseHelper.COLUMN_ID + " FROM "
                + KanjotoDatabaseHelper.TABLE_KEY_NOTES
                + " WHERE " + KanjotoDatabaseHelper.COLUMN_APPRENTICE_ID + "=?"
                + " AND " + KanjotoDatabaseHelper.COLUMN_NOTEVALUE + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all keyNotes from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(apprenticeId),
                String.valueOf(notevalue),
        });

        if (cursor.moveToFirst()) {
            do {
                // add keyNote to keyNotes list
                keySignatureIds.add(cursor.getLong(0));
            } while (cursor.moveToNext());
        }

        db.close();

        return keySignatureIds;
    }

    public long getKeySignatureByNotes(long apprenticeId, List<Integer> notevaluesInKeySignature) {
        long keySignatureId = 1;
        HashMap<Long, Integer> foundKeySignatureIds = new HashMap<Long, Integer>();

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // loop through each notevalue from input
        // and check to see if we've found a key signature from database
        for (int i = 0; i < notevaluesInKeySignature.size(); i++) {
            String query = "SELECT " + KanjotoDatabaseHelper.COLUMN_ID + ", "
                    + KanjotoDatabaseHelper.COLUMN_KEY_SIGNATURE_ID + " FROM "
                    + KanjotoDatabaseHelper.TABLE_KEY_NOTES
                    + " WHERE " + KanjotoDatabaseHelper.COLUMN_APPRENTICE_ID + "=?"
                    + " AND " + KanjotoDatabaseHelper.COLUMN_NOTEVALUE + "=?";

            // select all keyNotes from database
            Cursor cursor = db.rawQuery(query, new String[] {
                    String.valueOf(apprenticeId),
                    String.valueOf(notevaluesInKeySignature.get(i)),
            });

            if (cursor.moveToFirst()) {
                do {
                    // add keyNote to keyNotes list
                    long knid = cursor.getLong(1);
                    // check if key already exists
                    if (foundKeySignatureIds.containsKey(knid)) {
                        int value = foundKeySignatureIds.get(knid);
                        value++;
                        foundKeySignatureIds.put(knid, value);
                    } else {
                        // add new key
                        foundKeySignatureIds.put(knid, 1);
                    }
                } while (cursor.moveToNext());
            }
        }

        boolean foundBestMatch = false;

        if (foundKeySignatureIds.size() > 0) {
            for (int i = 4; i > 0; i--) {
                Iterator<Map.Entry<Long, Integer>> itr = foundKeySignatureIds.entrySet().iterator();
                while (itr.hasNext() && !foundBestMatch) {
                    Map.Entry<Long, Integer> kvpair = itr.next();
                    if (kvpair.getValue() == i) {
                        foundBestMatch = true;
                        keySignatureId = kvpair.getKey();
                    }
                }
            }
        }

        db.close();

        return keySignatureId;
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }
}
