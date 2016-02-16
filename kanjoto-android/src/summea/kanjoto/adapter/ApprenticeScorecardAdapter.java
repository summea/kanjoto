
package summea.kanjoto.adapter;

import java.util.List;

import summea.kanjoto.data.GraphsDataSource;
import summea.kanjoto.data.LabelsDataSource;
import summea.kanjoto.model.ApprenticeScorecard;
import summea.kanjoto.model.Graph;
import summea.kanjoto.model.Label;

import summea.kanjoto.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * This adapter provides a customized way of displaying Apprentice Scorecard list information.
 * <p>
 * Using this custom adapter allows for a more personalized list for Apprentice Scorecards.
 * </p>
 */
public class ApprenticeScorecardAdapter extends BaseAdapter {
    private Context mContext;
    private List<ApprenticeScorecard> apprenticeScorecards;

    public ApprenticeScorecardAdapter(Context context,
            List<ApprenticeScorecard> allApprenticeScorecards) {
        mContext = context;
        apprenticeScorecards = allApprenticeScorecards;
    }

    @Override
    public int getCount() {
        return apprenticeScorecards.size();
    }

    @Override
    public Object getItem(int position) {
        return apprenticeScorecards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return apprenticeScorecards.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                    R.layout.row_apprentice_scorecard,
                    null);
        }

        TextView apprenticeScorecard = (TextView) convertView
                .findViewById(R.id.apprentice_scorecard);

        apprenticeScorecard.setText(apprenticeScorecards.get(position).toString());

        GraphsDataSource gds = new GraphsDataSource(mContext);
        Graph graph = gds.getGraph(apprenticeScorecards.get(position).getGraphId());
        gds.close();

        LabelsDataSource lds = new LabelsDataSource(mContext);
        Label label = lds.getLabel(graph.getLabelId());
        lds.close();

        // set relevant background color, if available
        if (label.getColor() != null) {
            apprenticeScorecard.setBackgroundColor(Color.parseColor(label.getColor()));
        }

        return convertView;
    }

    public Object removeItem(int position) {
        return apprenticeScorecards.remove(position);
    }

    public void clear() {
        apprenticeScorecards.clear();
    }
}
