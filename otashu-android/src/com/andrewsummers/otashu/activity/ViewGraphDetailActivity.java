
package com.andrewsummers.otashu.activity;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.GraphsDataSource;
import com.andrewsummers.otashu.model.Graph;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * View details of a particular Graph.
 * <p>
 * This activity allows a user to see more information about a particular Graph. Graphs are mainly
 * used to store information that the Apprentice has learned. Graphs are fairly complex to display,
 * so this detail view only shows basic Graph information.
 * </p>
 */
public class ViewGraphDetailActivity extends Activity {
    private int graphId = 0;

    /**
     * onCreate override used to get details view.
     * 
     * @param savedInstanceState Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_view_graph_detail);

        graphId = (int) getIntent().getExtras().getLong("list_id");

        Graph graph = new Graph();
        GraphsDataSource lds = new GraphsDataSource(this);
        graph = lds.getGraph(graphId);
        lds.close();

        // fill in form data
        TextView graphName = (TextView) findViewById(R.id.graph_name_value);
        graphName.setText(graph.getName());
    }
}
