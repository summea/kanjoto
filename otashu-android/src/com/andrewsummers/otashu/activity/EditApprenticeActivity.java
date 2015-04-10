
package com.andrewsummers.otashu.activity;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.ApprenticesDataSource;
import com.andrewsummers.otashu.model.Apprentice;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * EditApprenticeActivity is an Activity which provides users the ability to edit apprentices.
 */
public class EditApprenticeActivity extends Activity implements OnClickListener {
    private Button buttonSave = null;
    private Apprentice editApprentice; // keep track of which Apprentice is currently being edited

    /**
     * onCreate override that provides apprentice creation view to user .
     * 
     * @param savedInstanceState Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_edit_apprentice);

        // add listeners to buttons
        buttonSave = (Button) findViewById(R.id.button_save);
        buttonSave.setOnClickListener(this);

        int apprenticeId = (int) getIntent().getExtras().getLong("list_id");

        // open data source handle
        ApprenticesDataSource lds = new ApprenticesDataSource(this);
        editApprentice = lds.getApprentice(apprenticeId);
        lds.close();

        // fill in existing form data
        EditText apprenticeNameText = (EditText) findViewById(R.id.edittext_apprentice_name);
        apprenticeNameText.setText(editApprentice.getName());
    }

    /**
     * onClick override used to save apprentice data once user clicks save button.
     * 
     * @param view Incoming view.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_save:
                // gather apprentice data from form
                Apprentice apprenticeToUpdate = new Apprentice();
                apprenticeToUpdate.setId(editApprentice.getId());

                String apprenticeName = ((EditText) findViewById(R.id.edittext_apprentice_name))
                        .getText()
                        .toString();

                apprenticeToUpdate.setName(apprenticeName.toString());

                // first insert new apprentice (parent of all related notes)
                saveApprenticeUpdates(v, apprenticeToUpdate);

                // close activity
                finish();
                break;
        }
    }

    /**
     * onResume override used to open up data source when resuming activity.
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * onPause override used to close data source when activity paused.
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Save apprentice data.
     * 
     * @param v Incoming view.
     * @param data Incoming string of data to be saved.
     */
    private void saveApprenticeUpdates(View v, Apprentice apprentice) {
        // save apprentice in database
        ApprenticesDataSource lds = new ApprenticesDataSource(this);
        lds.updateApprentice(apprentice);
        lds.close();

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context,
                context.getResources().getString(R.string.apprentice_saved),
                duration);
        toast.show();
    }
}
