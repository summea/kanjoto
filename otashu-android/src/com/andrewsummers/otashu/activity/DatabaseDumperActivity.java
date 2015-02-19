
package com.andrewsummers.otashu.activity;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.OtashuDatabaseHelper;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * DatabaseDumperActivity is an Activity which provides a dump of database data.
 * <p>
 * Directly working with database files on Android seems to be surprisingly difficult. Until a
 * better way is found, the Database Dumper allows for data from the database to be dumped to a page
 * for debug and informational purposes.
 * </p>
 */
public class DatabaseDumperActivity extends ListActivity {

    static final String[] database_tables = new String[] {
        OtashuDatabaseHelper.TABLE_BOOKMARKS,
        OtashuDatabaseHelper.TABLE_EDGES,
        OtashuDatabaseHelper.TABLE_NOTES,
        OtashuDatabaseHelper.TABLE_NOTESETS,
    };
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setListAdapter(new ArrayAdapter<String>(this, R.layout.list_database_table, database_tables));
        
        ListView listView = getListView();
        
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = null;
                switch (position) {
                    case 0:
                        intent = new Intent(DatabaseDumperActivity.this, DatabaseDumperBookmarksActivity.class);
                        break;
                    case 1:
                        intent = new Intent(DatabaseDumperActivity.this, DatabaseDumperEdgesActivity.class);
                        break;
                    case 2:
                        intent = new Intent(DatabaseDumperActivity.this, DatabaseDumperNotesActivity.class);
                        break;
                    case 3:
                        intent = new Intent(DatabaseDumperActivity.this, DatabaseDumperNotesetsActivity.class);
                        break;
                }

                if (intent != null) {
                    startActivity(intent);
                }
            }
        });
    }
    
    /*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_database_dumper);        

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

        GraphsDataSource gds = new GraphsDataSource(this);
        List<Graph> allGraphs = gds.getAllGraphs();
        gds.close();

        ApprenticeScorecardsDataSource ascds = new ApprenticeScorecardsDataSource(this);
        List<ApprenticeScorecard> allApprenticeScorecards = ascds.getAllApprenticeScorecards("");
        ascds.close();

        ApprenticeScoresDataSource asds = new ApprenticeScoresDataSource(this);
        List<ApprenticeScore> allApprenticeScores = asds.getAllApprenticeScores();
        asds.close();

        TextView debugText = (TextView) findViewById(R.id.debug_text);

        debugText.setText(debugText.getText().toString() + "Table: Emotions\n"
                + OtashuDatabaseHelper.COLUMN_ID + "|" + OtashuDatabaseHelper.COLUMN_NAME + "|"
                + OtashuDatabaseHelper.COLUMN_LABEL_ID + "\n");

        for (Emotion emotion : allEmotions) {

            String newText = debugText.getText().toString();
            newText += emotion.getId() + "|" + emotion.getName() + "|" + emotion.getLabelId()
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
                + OtashuDatabaseHelper.COLUMN_ID + "|" + OtashuDatabaseHelper.COLUMN_NAME + "|" + OtashuDatabaseHelper.COLUMN_LABEL_ID + "\n");

        for (Graph graph : allGraphs) {

            String newText = debugText.getText().toString();
            newText += graph.getId() + "|" + graph.getName() + "|" + graph.getLabelId() + "\n";

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

        debugText.setText(debugText.getText().toString() + "\nTable: Apprentice Scorecards\n"
                + OtashuDatabaseHelper.COLUMN_ID + "|" + OtashuDatabaseHelper.COLUMN_TAKEN_AT + "|"
                + OtashuDatabaseHelper.COLUMN_CORRECT + "|" + OtashuDatabaseHelper.COLUMN_TOTAL
                + "\n");

        for (ApprenticeScorecard aScorecard : allApprenticeScorecards) {

            String newText = debugText.getText().toString();
            newText += aScorecard.getId() + "|" + aScorecard.getTakenAt()
                    + aScorecard.getCorrect() + "|" + aScorecard.getTotal() + "\n";

            debugText.setText(newText);
        }

        debugText.setText(debugText.getText().toString() + "\nTable: Apprentice Scores\n"
                + OtashuDatabaseHelper.COLUMN_ID + "|" + OtashuDatabaseHelper.COLUMN_SCORECARD_ID
                + "|" + OtashuDatabaseHelper.COLUMN_QUESTION_NUMBER
                + "|" + OtashuDatabaseHelper.COLUMN_CORRECT + "|"
                + OtashuDatabaseHelper.COLUMN_EDGE_ID
                + "\n");

        for (ApprenticeScore aScore : allApprenticeScores) {

            String newText = debugText.getText().toString();
            newText += aScore.getId() + "|" + aScore.getScorecardId()
                    + "|" + aScore.getQuestionNumber()
                    + "|" + aScore.getCorrect()
                    + "|" + aScore.getEdgeId() + "\n";

            debugText.setText(newText);
        }
    }
    */
}
