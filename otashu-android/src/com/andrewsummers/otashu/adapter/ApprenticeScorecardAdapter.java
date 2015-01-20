
package com.andrewsummers.otashu.adapter;

import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.model.ApprenticeScorecard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ApprenticeScorecardAdapter extends BaseAdapter {

    private Context mContext;
    private List<ApprenticeScorecard> apprenticeScorecards;

    public ApprenticeScorecardAdapter(Context context, List<ApprenticeScorecard> allApprenticeScorecards) {
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
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.row_apprentice_scorecard,
                    null);
        }

        TextView apprenticeScorecard = (TextView) convertView.findViewById(R.id.apprentice_scorecard);
        apprenticeScorecard.setText(apprenticeScorecards.toString());

        return convertView;
    }

    public Object removeItem(int position) {
        return apprenticeScorecards.remove(position);
    }

    public void clear() {
        apprenticeScorecards.clear();
    }
}
