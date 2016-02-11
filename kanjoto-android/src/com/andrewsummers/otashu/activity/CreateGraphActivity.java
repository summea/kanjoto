
package com.andrewsummers.otashu.activity;

import java.util.ArrayList;
import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.GraphsDataSource;
import com.andrewsummers.otashu.data.LabelsDataSource;
import com.andrewsummers.otashu.model.Graph;

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
 * CreateGraphActivity is an Activity which provides users the ability to create new graphs.
 * <p>
 * This activity provides a form for creating a new Graph. User may want to create separate
 * graphs for different purposes. For example, there might be a graph for
 * "general note relationships" (i.e. how music notes typically fit together in sequence for certain
 * keys) or a graph for "emotions" (i.e. how notes fit together in sequence in relation to a
 * specific emotion).
 * </p>
 */
public class CreateGraphActivity extends Activity implements OnClickListener {
    private Button buttonSave = null;
    private Graph newlyInsertedGraph = new Graph();

    /**
     * onCreate override that provides graph creation view to user .
     * 
     * @param savedInstanceState Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_create_graph);

        // add listeners to buttons
        buttonSave = (Button) findViewById(R.id.button_save);
        buttonSave.setOnClickListener(this);
        
        LabelsDataSource lds = new LabelsDataSource(this);
        List<String> allLabels = new ArrayList<String>();
        allLabels = lds.getAllLabelListPreviews();
        lds.close();

        // locate next spinner in layout
        Spinner spinner = (Spinner) findViewById(R.id.spinner_label);

        // create array adapter for list of labels
        ArrayAdapter<String> labelsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item);
        labelsAdapter.addAll(allLabels);

        // specify the default layout when list of choices appears
        labelsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // apply this adapter to the spinner
        spinner.setAdapter(labelsAdapter);
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
                
                Graph graphToInsert = new Graph();

                String graphName = ((EditText) findViewById(R.id.edittext_graph_name)).getText()
                        .toString();
                Spinner notevalueLabel = (Spinner) findViewById(R.id.spinner_label);

                graphToInsert.setName(graphName.toString());
                graphToInsert.setLabelId(allLabelIds.get(notevalueLabel
                        .getSelectedItemPosition()));

                // first insert new graph (parent of all related notes)
                saveGraph(v, graphToInsert);

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
    private void saveGraph(View v, Graph graph) {
        // save graph in database
        GraphsDataSource gds = new GraphsDataSource(this);
        setNewlyInsertedGraph(gds.createGraph(graph));
        gds.close();

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context,
                context.getResources().getString(R.string.graph_saved),
                duration);
        toast.show();
    }

    public Graph getNewlyInsertedGraph() {
        return newlyInsertedGraph;
    }

    public void setNewlyInsertedGraph(Graph newlyInsertedGraph) {
        this.newlyInsertedGraph = newlyInsertedGraph;
    }
}
