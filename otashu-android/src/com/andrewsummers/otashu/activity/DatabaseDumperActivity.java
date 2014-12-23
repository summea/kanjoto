
package com.andrewsummers.otashu.activity;

import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.BookmarksDataSource;
import com.andrewsummers.otashu.data.EdgesDataSource;
import com.andrewsummers.otashu.data.EmotionsDataSource;
import com.andrewsummers.otashu.data.GraphsDataSource;
import com.andrewsummers.otashu.data.LabelsDataSource;
import com.andrewsummers.otashu.data.NotesDataSource;
import com.andrewsummers.otashu.data.NotesetsDataSource;
import com.andrewsummers.otashu.data.NotevaluesDataSource;
import com.andrewsummers.otashu.data.OtashuDatabaseHelper;
import com.andrewsummers.otashu.data.VerticesDataSource;
import com.andrewsummers.otashu.model.Bookmark;
import com.andrewsummers.otashu.model.Edge;
import com.andrewsummers.otashu.model.Emotion;
import com.andrewsummers.otashu.model.Graph;
import com.andrewsummers.otashu.model.Label;
import com.andrewsummers.otashu.model.Note;
import com.andrewsummers.otashu.model.Noteset;
import com.andrewsummers.otashu.model.Notevalue;
import com.andrewsummers.otashu.model.Vertex;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class DatabaseDumperActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_database_dumper);

        BookmarksDataSource bds = new BookmarksDataSource(this);
        List<Bookmark> allBookmarks = bds.getAllBookmarks();
        bds.close();

        NotesetsDataSource nsds = new NotesetsDataSource(this);
        List<Noteset> allNotesets = nsds.getAllNotesets();
        nsds.close();

        NotesDataSource nds = new NotesDataSource(this);
        List<Note> allNotes = nds.getAllNotes();
        nds.close();

        EmotionsDataSource eds = new EmotionsDataSource(this);
        List<Emotion> allEmotions = eds.getAllEmotions();
        eds.close();

        LabelsDataSource lds = new LabelsDataSource(this);
        List<Label> allLabels = lds.getAllLabels();
        lds.close();

        NotevaluesDataSource nvds = new NotevaluesDataSource(this);
        List<Notevalue> allNotevalues = nvds.getAllNotevalues();
        nvds.close();

        VerticesDataSource vds = new VerticesDataSource(this);
        List<Vertex> allVertices = vds.getAllVertices();
        vds.close();

        EdgesDataSource edds = new EdgesDataSource(this);
        List<Edge> allEdges = edds.getAllEdges();
        edds.close();

        GraphsDataSource gds = new GraphsDataSource(this);
        List<Graph> allGraphs = gds.getAllGraphs();
        gds.close();

        TextView debugText = (TextView) findViewById(R.id.debug_text);

        debugText.setText(debugText.getText().toString() + "Table: Bookmarks\n"
                + OtashuDatabaseHelper.COLUMN_ID + "|" + OtashuDatabaseHelper.COLUMN_NAME + "|"
                + OtashuDatabaseHelper.COLUMN_SERIALIZED_VALUE + "\n");

        for (Bookmark bookmark : allBookmarks) {

            String newText = debugText.getText().toString();
            newText += bookmark.getId() + "|" + bookmark.getName() + "|"
                    + bookmark.getSerializedValue() + "\n";

            debugText.setText(newText);
        }

        debugText.setText(debugText.getText().toString() + "Table: Emotions\n"
                + OtashuDatabaseHelper.COLUMN_ID + "|" + OtashuDatabaseHelper.COLUMN_NAME + "|"
                + OtashuDatabaseHelper.COLUMN_LABEL_ID + "\n");

        for (Emotion emotion : allEmotions) {

            String newText = debugText.getText().toString();
            newText += emotion.getId() + "|" + emotion.getName() + "|" + emotion.getLabelId()
                    + "\n";

            debugText.setText(newText);
        }

        debugText.setText(debugText.getText().toString() + "\nTable: Notes\n"
                + OtashuDatabaseHelper.COLUMN_ID + "|" + OtashuDatabaseHelper.COLUMN_NOTESET_ID
                + "|" + OtashuDatabaseHelper.COLUMN_NOTEVALUE + "|"
                + OtashuDatabaseHelper.COLUMN_VELOCITY + "|" + OtashuDatabaseHelper.COLUMN_LENGTH
                + "|" + OtashuDatabaseHelper.COLUMN_POSITION + "\n");

        for (Note note : allNotes) {

            String newText = debugText.getText().toString();
            newText += note.getId() + "|" + note.getNotesetId() + "|" + note.getNotevalue() + "|"
                    + note.getVelocity() + "|" + note.getLength() + "|" + note.getPosition() + "\n";

            debugText.setText(newText);
        }

        debugText.setText(debugText.getText().toString() + "\nTable: Notesets\n"
                + OtashuDatabaseHelper.COLUMN_ID + "|" + OtashuDatabaseHelper.COLUMN_NAME + "|"
                + OtashuDatabaseHelper.COLUMN_EMOTION_ID + "\n");

        for (Noteset noteset : allNotesets) {

            String newText = debugText.getText().toString();
            newText += noteset.getId() + "|" + noteset.getName() + "|" + noteset.getEmotion()
                    + "\n";

            debugText.setText(newText);
        }

        debugText.setText(debugText.getText().toString() + "\nTable: Labels\n"
                + OtashuDatabaseHelper.COLUMN_ID + "|" + OtashuDatabaseHelper.COLUMN_NAME + "|"
                + OtashuDatabaseHelper.COLUMN_COLOR + "\n");

        for (Label label : allLabels) {

            String newText = debugText.getText().toString();
            newText += label.getId() + "|" + label.getName() + "|" + label.getColor() + "\n";

            debugText.setText(newText);
        }

        debugText.setText(debugText.getText().toString() + "\nTable: Notevalues\n"
                + OtashuDatabaseHelper.COLUMN_ID + "|" + OtashuDatabaseHelper.COLUMN_NOTEVALUE
                + "|" + OtashuDatabaseHelper.COLUMN_NOTELABEL + "|"
                + OtashuDatabaseHelper.COLUMN_LABEL_ID + "\n");

        for (Notevalue notevalue : allNotevalues) {

            String newText = debugText.getText().toString();
            newText += notevalue.getId() + "|" + notevalue.getNotevalue() + "|"
                    + notevalue.getNotelabel() + "|" + notevalue.getLabelId() + "\n";

            debugText.setText(newText);
        }

        debugText.setText(debugText.getText().toString() + "\nTable: Graphs\n"
                + OtashuDatabaseHelper.COLUMN_ID + "|" + OtashuDatabaseHelper.COLUMN_NAME + "\n");

        for (Graph graph : allGraphs) {

            String newText = debugText.getText().toString();
            newText += graph.getId() + "|" + graph.getName() + "\n";

            debugText.setText(newText);
        }

        debugText.setText(debugText.getText().toString() + "\nTable: Vertices\n"
                + OtashuDatabaseHelper.COLUMN_ID + "|" + OtashuDatabaseHelper.COLUMN_GRAPH_ID + "|"
                + OtashuDatabaseHelper.COLUMN_NODE + "\n");

        for (Vertex vertex : allVertices) {

            String newText = debugText.getText().toString();
            newText += vertex.getId() + "|" + vertex.getGraphId() + "|" + vertex.getNode() + "\n";

            debugText.setText(newText);
        }

        debugText
                .setText(debugText.getText().toString() + "\nTable: Edges\n"
                        + OtashuDatabaseHelper.COLUMN_ID + "|"
                        + OtashuDatabaseHelper.COLUMN_GRAPH_ID + "|"
                        + OtashuDatabaseHelper.COLUMN_FROM_ID + "|"
                        + OtashuDatabaseHelper.COLUMN_TO_ID + "|"
                        + OtashuDatabaseHelper.COLUMN_WEIGHT + "\n");

        for (Edge edge : allEdges) {

            String newText = debugText.getText().toString();
            newText += edge.getId() + "|" + edge.getGraphId() + "|" + edge.getFromId() + "|"
                    + edge.getToId() + "|"
                    + edge.getWeight() + "\n";

            debugText.setText(newText);
        }
    }
}
