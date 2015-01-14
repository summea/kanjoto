
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
 * CreateGraphActivity is an Activity which provides users the ability to create new graphs.
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
                String graphName;

                Graph graphToInsert = new Graph();

                graphName = ((EditText) findViewById(R.id.edittext_graph_name)).getText()
                        .toString();

                graphToInsert.setName(graphName.toString());

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
