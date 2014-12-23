
package com.andrewsummers.otashu.activity;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.GraphsDataSource;
import com.andrewsummers.otashu.model.Graph;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * View details of a particular graph.
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

        Log.d("MYLOG", "got list item id: " + getIntent().getExtras().getLong("list_id"));
        graphId = (int) getIntent().getExtras().getLong("list_id");

        /*
         * // prevent crashes due to lack of database data if (allGraphsData.isEmpty())
         * allGraphsData.add((long) 0);
         */

        Graph graph = new Graph();
        GraphsDataSource lds = new GraphsDataSource(this);
        graph = lds.getGraph(graphId);
        lds.close();

        TextView graphName = (TextView) findViewById(R.id.graph_name_value);
        graphName.setText(graph.getName());     
    }
}
