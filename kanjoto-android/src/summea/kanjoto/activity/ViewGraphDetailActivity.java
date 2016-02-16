
package summea.kanjoto.activity;

import summea.kanjoto.data.GraphsDataSource;
import summea.kanjoto.data.LabelsDataSource;
import summea.kanjoto.model.Graph;
import summea.kanjoto.model.Label;

import summea.kanjoto.R;

import android.app.Activity;
import android.graphics.Color;
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
    private long graphId = 0;

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

        graphId = getIntent().getExtras().getLong("list_id");

        Graph graph = new Graph();
        GraphsDataSource gds = new GraphsDataSource(this);
        graph = gds.getGraph(graphId);
        gds.close();

        LabelsDataSource lds = new LabelsDataSource(this);
        Label label = lds.getLabel(graph.getLabelId());
        lds.close();

        // fill in form data
        TextView graphName = (TextView) findViewById(R.id.graph_name_value);
        graphName.setText(graph.getName());

        TextView graphLabel = (TextView) findViewById(R.id.graph_label_value);
        graphLabel.setText(lds.getLabel(graph.getLabelId()).getName());

        // get label background color, if available
        if (label.getColor() != null) {
            graphLabel.setBackgroundColor(Color.parseColor(label.getColor()));
        }
    }
}
