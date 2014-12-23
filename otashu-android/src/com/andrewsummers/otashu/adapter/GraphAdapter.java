
package com.andrewsummers.otashu.adapter;

import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.model.Graph;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GraphAdapter extends BaseAdapter {

    private Context mContext;
    private List<Graph> graphs;

    public GraphAdapter(Context context, List<Graph> allGraphs) {
        mContext = context;
        graphs = allGraphs;
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

        return convertView;
    }

    public Object removeItem(int position) {
        return graphs.remove(position);
    }

    public void clear() {
        graphs.clear();
    }
}
