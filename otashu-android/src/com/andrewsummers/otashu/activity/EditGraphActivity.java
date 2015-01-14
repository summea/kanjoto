
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
public class EditGraphActivity extends Activity implements OnClickListener {
    private Button buttonSave = null;
    private Graph editGraph;

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

        // open data source handle
        GraphsDataSource lds = new GraphsDataSource(this);
        int graphId = (int) getIntent().getExtras().getLong("list_id");

        editGraph = lds.getGraph(graphId);
        lds.close();

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
                String graphName;

                Graph graphToUpdate = new Graph();

                graphToUpdate.setId(editGraph.getId());

                graphName = ((EditText) findViewById(R.id.edittext_graph_name)).getText()
                        .toString();

                graphToUpdate.setName(graphName.toString());

                // first insert new graph (parent of all related notes)
                saveGraphUpdates(v, graphToUpdate);

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
