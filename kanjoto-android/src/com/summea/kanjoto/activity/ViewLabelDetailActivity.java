
package com.summea.kanjoto.activity;

import com.summea.kanjoto.R;
import com.summea.kanjoto.data.LabelsDataSource;
import com.summea.kanjoto.model.Label;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

/**
 * View details of a particular Label.
 * <p>
 * This activity allows a user to see more information about a particular Label. Labels are tags
 * used to differentiate items based on their assigned label's background color.
 * </p>
 */
public class ViewLabelDetailActivity extends Activity {
    private long labelId = 0;

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

        labelId = getIntent().getExtras().getLong("list_id");

        Label label = new Label();
        LabelsDataSource lds = new LabelsDataSource(this);
        label = lds.getLabel(labelId);
        lds.close();

        // fill in form data
        TextView labelName = (TextView) findViewById(R.id.label_name_value);
        labelName.setText(label.getName());

        TextView labelColor = (TextView) findViewById(R.id.label_color_value);
        labelColor.setText(label.getColor());

        // get label background color, if available
        if (label.getColor() != null) {
            labelColor.setBackgroundColor(Color.parseColor(label.getColor()));
        }
    }
}
