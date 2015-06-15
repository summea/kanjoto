
package com.andrewsummers.otashu.adapter;

import java.util.HashMap;
import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.LabelsDataSource;
import com.andrewsummers.otashu.model.Graph;
import com.andrewsummers.otashu.model.Label;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GraphAdapter extends BaseAdapter {

    private Context mContext;
    private List<Graph> graphs;
    private List<Label> allLabels;
    private HashMap<Long, Label> allLabelsMap;

    public GraphAdapter(Context context, List<Graph> allGraphs) {
        mContext = context;
        graphs = allGraphs;
        LabelsDataSource lds = new LabelsDataSource(mContext);
        allLabels = lds.getAllLabels();
        lds.close();

        allLabelsMap = new HashMap<Long, Label>();
        for (Label label : allLabels) {
            allLabelsMap.put(label.getId(), label);
            Log.d("MYLOG", "putting label: " + label.getId() + " putting color: " + label.getColor());
        }
    }

    @Override
    public int getCount() {
        return graphs.size();
    }

    @Override
    public Object getItem(int position) {
        return graphs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return graphs.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.row_graph,
                    null);
        }

        TextView graph = (TextView) convertView.findViewById(R.id.graph);
        graph.setText(graphs.get(position).getId() + " " + graphs.get(position).getName());

        // set relevant background color, if available
        /*
        if (allLabelsMap.get(graphs.get(position).getLabelId()).getColor() != null) {
            graph.setBackgroundColor(Color.parseColor(allLabelsMap.get(
                    graphs.get(position).getLabelId()).getColor()));
        }
        */
        Log.d("MYLOG", "color: " + allLabelsMap.get(1));

        return convertView;
    }

    public Object removeItem(int position) {
        return graphs.remove(position);
    }

    public void clear() {
        graphs.clear();
    }
}
