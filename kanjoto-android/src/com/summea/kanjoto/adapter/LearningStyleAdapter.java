
package com.summea.kanjoto.adapter;

import java.util.List;

import com.summea.kanjoto.R;
import com.summea.kanjoto.model.LearningStyle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LearningStyleAdapter extends BaseAdapter {

    private Context mContext;
    private List<LearningStyle> learningStyles;

    public LearningStyleAdapter(Context context, List<LearningStyle> allLearningStyles) {
        mContext = context;
        learningStyles = allLearningStyles;
    }

    @Override
    public int getCount() {
        return learningStyles.size();
    }

    @Override
    public Object getItem(int position) {
        return learningStyles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return learningStyles.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                    R.layout.row_learning_style, null);
        }

        TextView learningStyle = (TextView) convertView.findViewById(R.id.learning_style);
        learningStyle.setText(learningStyles.get(position).getName());

        return convertView;
    }

    public Object removeItem(int position) {
        return learningStyles.remove(position);
    }

    public void clear() {
        learningStyles.clear();
    }
}
