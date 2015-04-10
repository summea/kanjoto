
package com.andrewsummers.otashu.activity;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.ApprenticesDataSource;
import com.andrewsummers.otashu.model.Apprentice;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * View details of a particular Apprentice.
 * <p>
 * This activity allows a user to see more information about a particular Apprentice.
 * </p>
 */
public class ViewApprenticeDetailActivity extends Activity {
    private int apprenticeId = 0;

    /**
     * onCreate override used to get details view.
     * 
     * @param savedInstanceState Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_view_apprentice_detail);

        apprenticeId = (int) getIntent().getExtras().getLong("list_id");

        Apprentice apprentice = new Apprentice();
        ApprenticesDataSource lds = new ApprenticesDataSource(this);
        apprentice = lds.getApprentice(apprenticeId);
        lds.close();

        // fill in form data
        TextView apprenticeName = (TextView) findViewById(R.id.apprentice_name_value);
        apprenticeName.setText(apprentice.getName());
    }
}
