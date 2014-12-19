
package com.andrewsummers.otashu.activity;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.LabelsDataSource;
import com.andrewsummers.otashu.model.Label;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * View details of a particular label.
 */
public class ViewLabelDetailActivity extends Activity {

    private int labelId = 0;

    /**
     * onCreate override used to get details view.
     * 
     * @param savedInstanceState Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_view_label_detail);

        Log.d("MYLOG", "got list item id: " + getIntent().getExtras().getLong("list_id"));
        labelId = (int) getIntent().getExtras().getLong("list_id");

        /*
         * // prevent crashes due to lack of database data if (allLabelsData.isEmpty())
         * allLabelsData.add((long) 0);
         */

        Label label = new Label();
        LabelsDataSource lds = new LabelsDataSource(this);
        label = lds.getLabel(labelId);
        lds.close();

        TextView labelName = (TextView) findViewById(R.id.label_name_value);
        labelName.setText(label.getName());

        TextView labelColor = (TextView) findViewById(R.id.label_color_value);
        labelColor.setText(label.getColor());

        if (label.getColor() != null)
            labelColor.setBackgroundColor(Color.parseColor(label.getColor()));
    }
}
