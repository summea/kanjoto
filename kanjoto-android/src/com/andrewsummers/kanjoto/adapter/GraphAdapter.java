
package com.andrewsummers.kanjoto.adapter;

import java.util.HashMap;
import java.util.List;

import com.andrewsummers.kanjoto.R;
import com.andrewsummers.kanjoto.data.LabelsDataSource;
import com.andrewsummers.kanjoto.model.Graph;
import com.andrewsummers.kanjoto.model.Label;

import android.content.Context;
import android.graphics.Color;
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
        if (graphs.get(position).getLabelId() > 0) {
            if (allLabelsMap.get(graphs.get(position).getLabelId()).getColor() != null) {
                graph.setBackgroundColor(Color.parseColor(allLabelsMap.get(
                        graphs.get(position).getLabelId()).getColor()));
            }
        }

        return convertView;
    }

    public Object removeItem(int position) {
        return graphs.remove(position);
    }

    public void clear() {
        graphs.clear();
    }
}
