
package com.summea.kanjoto.activity;

import java.util.ArrayList;
import java.util.List;

import com.summea.kanjoto.R;
import com.summea.kanjoto.data.GraphsDataSource;
import com.summea.kanjoto.data.LabelsDataSource;
import com.summea.kanjoto.model.Graph;
import com.summea.kanjoto.model.Label;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
 * simply provides a form to edit metadata about graphs used in the Kanjoto project.
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

        long graphId = getIntent().getExtras().getLong("list_id");

        // open data source handle
        GraphsDataSource gds = new GraphsDataSource(this);
        editGraph = gds.getGraph(graphId);
        gds.close();

        // fill in existing form data
        EditText graphNameText = (EditText) findViewById(R.id.edittext_graph_name);
        graphNameText.setText(editGraph.getName());

        LabelsDataSource lds = new LabelsDataSource(this);
        List<String> allLabels = new ArrayList<String>();
        allLabels = lds.getAllLabelListPreviews();

        Label selectedLabel = lds.getLabel(editGraph.getLabelId());
        lds.close();

        // locate next spinner in layout
        Spinner spinner = (Spinner) findViewById(R.id.spinner_label);

        // create array adapter for list of notevalues
        ArrayAdapter<String> labelsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item);
        labelsAdapter.addAll(allLabels);

        // specify the default layout when list of choices appears
        labelsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // apply this adapter to the spinner
        spinner.setAdapter(labelsAdapter);
        spinner.setSelection(labelsAdapter.getPosition(selectedLabel.getName()));
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
                LabelsDataSource lds = new LabelsDataSource(this);
                List<Long> allLabelIds = lds.getAllLabelListDBTableIds();
                lds.close();

                Graph graphToUpdate = new Graph();
                graphToUpdate.setId(editGraph.getId());

                String graphName = ((EditText) findViewById(R.id.edittext_graph_name)).getText()
                        .toString();
                Spinner graphLabel = (Spinner) findViewById(R.id.spinner_label);

                graphToUpdate.setName(graphName.toString());
                graphToUpdate.setLabelId(allLabelIds.get(graphLabel
                        .getSelectedItemPosition()));

                // update graph
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
        GraphsDataSource gds = new GraphsDataSource(this);
        gds.updateGraph(graph);
        gds.close();

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context,
                context.getResources().getString(R.string.graph_saved),
                duration);
        toast.show();
    }
}
