
package summea.kanjoto.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import summea.kanjoto.model.Note;
import summea.kanjoto.model.NotesetAndRelated;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.SparseArray;

public class NotesDataSource {
    private KanjotoDatabaseHelper dbHelper;
    private Context mContext;

    // database table columns
    private String[] allColumns = {
            KanjotoDatabaseHelper.COLUMN_ID,
            KanjotoDatabaseHelper.COLUMN_NOTESET_ID,
            KanjotoDatabaseHelper.COLUMN_NOTEVALUE,
            KanjotoDatabaseHelper.COLUMN_VELOCITY,
            KanjotoDatabaseHelper.COLUMN_LENGTH,
            KanjotoDatabaseHelper.COLUMN_POSITION
    };

    /**
     * NotesDataSource constructor.
     * 
     * @param context Current state.
     */
    public NotesDataSource(Context context) {
        dbHelper = new KanjotoDatabaseHelper(context);
        mContext = context;
    }

    /**
     * NotesDataSource constructor.
     * 
     * @param context Current state.
     * @param databaseName Database to use.
     */
    public NotesDataSource(Context context, String databaseName) {
        dbHelper = new KanjotoDatabaseHelper(context, databaseName);
    }

    /**
     * Open database.
     * 
     * @throws SQLException
     */
    public void open() throws SQLException {
        dbHelper.getWritableDatabase();
    }

    /**
     * Close database.
     */
    public void close() {
        dbHelper.close();
    }

    /**
     * Create note row in database.
     * 
     * @param notevalues String of note values to insert.
     * @return Note of newly-created note data.
     */
    public Note createNote(Note note) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KanjotoDatabaseHelper.COLUMN_NOTESET_ID, note.getNotesetId());
        contentValues.put(KanjotoDatabaseHelper.COLUMN_NOTEVALUE, note.getNotevalue());
        contentValues.put(KanjotoDatabaseHelper.COLUMN_VELOCITY, note.getVelocity());
        contentValues.put(KanjotoDatabaseHelper.COLUMN_LENGTH, note.getLength());
        contentValues.put(KanjotoDatabaseHelper.COLUMN_POSITION, note.getPosition());

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long insertId = db.insert(KanjotoDatabaseHelper.TABLE_NOTES, null,
                contentValues);

        Cursor cursor = db.query(
                KanjotoDatabaseHelper.TABLE_NOTES, allColumns,
                KanjotoDatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        Note newNote = cursorToNote(cursor);
        cursor.close();
        db.close();

        return newNote;
    }

    /**
     * Delete note row from database.
     * 
     * @param note Note to delete.
     */
    public void deleteNote(Note note) {
        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // delete noteset
        db.delete(KanjotoDatabaseHelper.TABLE_NOTES,
                KanjotoDatabaseHelper.COLUMN_ID + " = " + note.getId(), null);

        db.close();
    }

    /**
     * Get note from database table.
     * 
     * @return Note
     */
    public Note getNote(long id) {
        Note note = new Note();

        String query = "SELECT * FROM " + KanjotoDatabaseHelper.TABLE_NOTES
                + " WHERE " + KanjotoDatabaseHelper.COLUMN_ID + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(id)
        });

        if (cursor.moveToFirst()) {
            do {
                // create note objects based on note data from database
                note.setId(Integer.parseInt(cursor.getString(0)));
                note.setNotesetId(cursor.getLong(1));
                note.setNotevalue(cursor.getInt(2));
                note.setVelocity(cursor.getInt(3));
                note.setLength(cursor.getFloat(4));
                note.setPosition(cursor.getInt(5));
            } while (cursor.moveToNext());
        }

        db.close();

        return note;
    }

    /**
     * Get all notes from database table.
     * 
     * @return List of Notes.
     */
    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<Note>();

        String query = "SELECT * FROM " + KanjotoDatabaseHelper.TABLE_NOTES;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, null);

        Note note = null;
        if (cursor.moveToFirst()) {
            do {
                // create note objects based on note data from database
                note = new Note();
                note.setId(Integer.parseInt(cursor.getString(0)));
                note.setNotesetId(cursor.getLong(1));
                note.setNotevalue(cursor.getInt(2));
                note.setVelocity(cursor.getInt(3));
                note.setLength(cursor.getFloat(4));
                note.setPosition(cursor.getInt(5));

                // add note string to list of strings
                notes.add(note);
            } while (cursor.moveToNext());
        }

        db.close();

        return notes;
    }

    /**
     * Get all notes with specific noteset_id from database table.
     * 
     * @return List of Notes.
     */
    public List<Note> getAllNotesByNotesetId(long notesetId) {
        List<Note> notes = new ArrayList<Note>();

        String query = "SELECT * FROM " + KanjotoDatabaseHelper.TABLE_NOTES
                + " WHERE " + KanjotoDatabaseHelper.COLUMN_NOTESET_ID + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(notesetId)
        });

        Note note = null;
        if (cursor.moveToFirst()) {
            do {
                // create note objects based on note data from database
                note = new Note();
                note.setId(Integer.parseInt(cursor.getString(0)));
                note.setNotesetId(cursor.getLong(1));
                note.setNotevalue(cursor.getInt(2));
                note.setVelocity(cursor.getInt(3));
                note.setLength(cursor.getFloat(4));
                note.setPosition(cursor.getInt(5));

                // add note string to list of strings
                notes.add(note);
            } while (cursor.moveToNext());
        }

        db.close();

        return notes;
    }

    public List<Note> getAllNotesByPosition(int position) {
        List<Note> notes = new ArrayList<Note>();

        String query = "SELECT * FROM " + KanjotoDatabaseHelper.TABLE_NOTES
                + " WHERE " + KanjotoDatabaseHelper.COLUMN_POSITION + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(position)
        });

        Note note = null;
        if (cursor.moveToFirst()) {
            do {
                // create note objects based on note data from database
                note = new Note();
                note.setId(Integer.parseInt(cursor.getString(0)));
                note.setNotesetId(cursor.getLong(1));
                note.setNotevalue(cursor.getInt(2));
                note.setVelocity(cursor.getInt(3));
                note.setLength(cursor.getFloat(4));
                note.setPosition(cursor.getInt(5));

                // add note string to list of strings
                notes.add(note);
            } while (cursor.moveToNext());
        }

        db.close();

        return notes;
    }

    /**
     * Does a noteset with the given list of notes already exist?
     * 
     * @param notesetAndRelatedToCheck Noteset information to check
     * @return boolean of noteset existence status
     */
    public boolean doesNotesetExist(NotesetAndRelated notesetAndRelatedToCheck) {
        boolean notesetExists = false;
        SparseArray<Set<Long>> foundNotes = new SparseArray<Set<Long>>();

        long parentId = 0;

        if (notesetAndRelatedToCheck.getNotes().get(0) != null) {
            parentId = notesetAndRelatedToCheck.getNotes().get(0).getNotesetId();
        }

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        for (int i = 0; i < notesetAndRelatedToCheck.getNotes().size(); i++) {
            // TODO: check all possible note sequences
            // and narrow down as we move through the note positions
            String query = "SELECT " + KanjotoDatabaseHelper.COLUMN_NOTESET_ID + ", "
                    + KanjotoDatabaseHelper.COLUMN_NOTEVALUE
                    + " FROM " + KanjotoDatabaseHelper.TABLE_NOTES
                    + " WHERE " + KanjotoDatabaseHelper.COLUMN_NOTEVALUE + "=?"
                    + " AND " + KanjotoDatabaseHelper.COLUMN_POSITION + "=?";

            // select all notes from database
            Cursor cursor = db.rawQuery(query, new String[] {
                    String.valueOf(notesetAndRelatedToCheck.getNotes().get(i)),
                    String.valueOf(i + 1),
            });

            if (cursor.moveToFirst()) {
                do {
                    if (cursor.getLong(0) > 0) {
                        Set<Long> notesetIds = new HashSet<Long>();
                        if (foundNotes.get(i + 1) != null) {
                            notesetIds = foundNotes.get(i + 1);
                        }

                        notesetIds.add(cursor.getLong(0));

                        foundNotes.put(i + 1, notesetIds);
                    }
                } while (cursor.moveToNext());
            } else {
                foundNotes.put(i + 1, new HashSet<Long>());
            }
        }

        Set<Long> foundSet = new HashSet<Long>();
        foundSet.addAll(foundNotes.get(1));

        for (int i = 0; i < foundNotes.size(); i++) {
            int key = foundNotes.keyAt(i);
            foundSet.retainAll(foundNotes.get(key));
        }

        foundSet.remove(parentId);

        if (foundSet.size() > 0) {

            for (Long id : foundSet) {
                String query = "SELECT * FROM " + KanjotoDatabaseHelper.TABLE_NOTESETS
                        + " WHERE " + KanjotoDatabaseHelper.COLUMN_ID + "=?";

                // select all notes from database
                Cursor cursor = db.rawQuery(query, new String[] {
                        String.valueOf(id)
                });

                if (cursor.moveToFirst()) {
                    do {
                        if (cursor.getLong(0) > 0) {
                            if (cursor.getLong(2) == notesetAndRelatedToCheck.getNoteset()
                                    .getEmotion()) {
                                notesetExists = true;
                                break;
                            }
                        }
                    } while (cursor.moveToNext());
                }
            }

        }

        db.close();

        return notesetExists;
    }

    /**
     * Access column data at current position of result.
     * 
     * @param cursor Current cursor location.
     * @return Note
     */
    private Note cursorToNote(Cursor cursor) {
        Note note = new Note();
        note.setId(cursor.getLong(0));
        note.setNotevalue(cursor.getInt(1));
        note.setVelocity(cursor.getInt(2));
        note.setLength(cursor.getFloat(3));
        note.setPosition(cursor.getInt(4));
        return note;
    }

    public Note updateNote(Note note) {
        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KanjotoDatabaseHelper.COLUMN_NOTESET_ID, note.getNotesetId());
        contentValues.put(KanjotoDatabaseHelper.COLUMN_NOTEVALUE, note.getNotevalue());
        contentValues.put(KanjotoDatabaseHelper.COLUMN_VELOCITY, note.getVelocity());
        contentValues.put(KanjotoDatabaseHelper.COLUMN_LENGTH, note.getLength());
        contentValues.put(KanjotoDatabaseHelper.COLUMN_POSITION, note.getPosition());

        db.update(KanjotoDatabaseHelper.TABLE_NOTES, contentValues, KanjotoDatabaseHelper.COLUMN_ID
                + "=" + note.getId(), null);

        db.close();

        return note;
    }

    public HashMap<String, String> getEmotionFromNotes(List<Integer> notes) {
        HashMap<String, String> result = new HashMap<String, String>();
        long emotionId = 0;
        long notesetId = 0;
        float certainty = 0.0f;

        // select an edge position
        int position = 1;
        List<Note> p1Notes = getAllNotesByPosition(position);

        position = 2;
        List<Note> p2Notes = getAllNotesByPosition(position);

        position = 3;
        List<Note> p3Notes = getAllNotesByPosition(position);

        position = 4;
        List<Note> p4Notes = getAllNotesByPosition(position);

        int i = 0;
        // loop through all position 1-2 notes
        for (Note note1 : p1Notes) {
            if (notes.get(i) == note1.getNotevalue()) {

                if (certainty < 25.0) {
                    notesetId = note1.getNotesetId();
                    certainty = 25.0f;
                }

                // loop through all position 2-3 notes and compare with first
                for (Note note2 : p2Notes) {
                    if (notes.get(i + 1) == note2.getNotevalue()) {

                        if (certainty < 50.0) {
                            if (note1.getNotesetId() == note2.getNotesetId()) {
                                notesetId = note2.getNotesetId();
                            } else {
                                notesetId = note1.getNotesetId();
                            }
                            certainty = 50.0f;
                        }

                        if (note1.getNotesetId() != note2.getNotesetId()) {
                            break;
                        }
                        // loop through all position 3-4 notes and compare with first
                        for (Note note3 : p3Notes) {
                            if (notes.get(i + 2) == note3.getNotevalue()) {

                                if (certainty < 75.0) {
                                    if (note2.getNotesetId() == note3.getNotesetId()) {
                                        notesetId = note3.getNotesetId();
                                    } else {
                                        notesetId = note2.getNotesetId();
                                    }
                                    certainty = 75.0f;
                                }

                                if (note2.getNotesetId() != note3.getNotesetId()) {
                                    break;
                                }

                                // loop through all position 3-4 notes and compare with first
                                for (Note note4 : p4Notes) {
                                    if (notes.get(i + 3) == note4.getNotevalue()) {
                                        // complete noteset found!
                                        notesetId = note4.getNotesetId();
                                        certainty = 100.0f;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        NotesetsDataSource nsds = new NotesetsDataSource(mContext);
        emotionId = nsds.getNoteset(notesetId).getEmotion();

        result.put("emotionId", String.valueOf(emotionId));
        result.put("certainty", String.valueOf(certainty));

        return result;
    }
}
