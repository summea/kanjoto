
package com.andrewsummers.otashu.adapter;

import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.model.Label;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LabelAdapter extends BaseAdapter {

    private Context mContext;
    private List<Label> labels;

    public LabelAdapter(Context context, List<Label> allLabels) {
        mContext = context;
        labels = allLabels;
    }

    @Override
    public int getCount() {
        return labels.size();
    }

    @Override
    public Object getItem(int position) {
        return labels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return labels.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.row_label,
                    null);
        }

        TextView label = (TextView) convertView.findViewById(R.id.label);
        label.setText(labels.get(position).getName());

        // set relevant background color, if available
        if (labels.get(position).getColor() != null) {
            label.setBackgroundColor(Color.parseColor(labels.get(position).getColor()));
        } else {
            label.setBackgroundColor(Color.parseColor("#dddddd"));
        }

        return convertView;
    }

    public Object removeItem(int position) {
        return labels.remove(position);
    }

    public void clear() {
        labels.clear();
    }
}
