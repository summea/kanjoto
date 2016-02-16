
package summea.kanjoto.activity;

import java.util.List;

import summea.kanjoto.data.KanjotoDatabaseHelper;
import summea.kanjoto.data.NotesDataSource;
import summea.kanjoto.model.Note;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import summea.kanjoto.R;

public class DatabaseDumperNotesActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_database_dumper);

        NotesDataSource nds = new NotesDataSource(this);
        List<Note> allNotes = nds.getAllNotes();
        nds.close();

        TextView debugText = (TextView) findViewById(R.id.debug_text);

        debugText.setText(debugText.getText().toString() + "Table: Notes\n"
                + KanjotoDatabaseHelper.COLUMN_ID + "|" + KanjotoDatabaseHelper.COLUMN_NOTESET_ID
                + "|" + KanjotoDatabaseHelper.COLUMN_NOTEVALUE + "|"
                + KanjotoDatabaseHelper.COLUMN_VELOCITY + "|" + KanjotoDatabaseHelper.COLUMN_LENGTH
                + "|" + KanjotoDatabaseHelper.COLUMN_POSITION + "\n");

        for (Note note : allNotes) {

            String newText = debugText.getText().toString();
            newText += note.getId() + "|" + note.getNotesetId() + "|" + note.getNotevalue() + "|"
                    + note.getVelocity() + "|" + note.getLength() + "|" + note.getPosition() + "\n";

            debugText.setText(newText);
        }
    }
}
