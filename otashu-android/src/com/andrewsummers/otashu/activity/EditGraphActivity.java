
package com.andrewsummers.otashu.activity;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.GraphsDataSource;
import com.andrewsummers.otashu.model.Graph;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * EditGraphActivity is an Activity which provides users the ability to edit graphs.
 * <p>
 * This activity provides a form for editing existing Graphs. Graph to edit is selected either via
 * the "view all graphs" activity or by the related "edit" context menu. The edit form fills in data
 * found (from the database) for specified Graph to edit and (if successful) any saved updates will
 * then be saved in the database.
 * </p>
 * <p>
 * This activity may sound more interesting than it is in reality: who wouldn't want to be able to
 * edit graphs stored in a database in one easy editing form? In reality, however, this activity
 * simply provides a form to edit metadata about graphs used in the Otashu project.
 * </p>
 */
public class EditGraphActivity extends Activity implements OnClickListener {
    private Button buttonSave = null;
    private Graph editGraph; // keep track of which Graph is currently being edited

    /**
     * onCreate override that provides graph creation view to user .
     * 
     * @param savedInstanceState Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_edit_graph);

        // add listeners to buttons
        buttonSave = (Button) findViewById(R.id.button_save);
        buttonSave.setOnClickListener(this);

        int graphId = (int) getIntent().getExtras().getLong("list_id");

        // open data source handle
        GraphsDataSource lds = new GraphsDataSource(this);
        editGraph = lds.getGraph(graphId);
        lds.close();

        // fill in existing form data
        EditText graphNameText = (EditText) findViewById(R.id.edittext_graph_name);
        graphNameText.setText(editGraph.getName());
    }

    /**
     * onClick override used to save graph data once user clicks save button.
     * 
     * @param view Incoming view.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_save:
                // gather graph data from form
                Graph graphToUpdate = new Graph();
                graphToUpdate.setId(editGraph.getId());

                String graphName = ((EditText) findViewById(R.id.edittext_graph_name)).getText()
                        .toString();

                graphToUpdate.setName(graphName.toString());

                // first insert new graph (parent of all related notes)
                saveGraphUpdates(v, graphToUpdate);

                // close activity
                finish();
                break;
        }
    }

    /**
     * onResume override used to open up data source when resuming activity.
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * onPause override used to close data source when activity paused.
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Save graph data.
     * 
     * @param v Incoming view.
     * @param data Incoming string of data to be saved.
     */
    private void saveGraphUpdates(View v, Graph graph) {
        // save graph in database
        GraphsDataSource lds = new GraphsDataSource(this);
        lds.updateGraph(graph);
        lds.close();

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context,
                context.getResources().getString(R.string.graph_saved),
                duration);
        toast.show();
    }
}
