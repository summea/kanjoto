
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
 * CreateApprenticeActivity is an Activity which provides users the ability to create new
 * apprentices.
 * <p>
 * This activity provides a form for creating a new Apprentice. User may want to create separate
 * apprentices for different purposes. For example, there might be a apprentice for
 * "general note relationships" (i.e. how music notes typically fit together in sequence for certain
 * keys) or a apprentice for "emotions" (i.e. how notes fit together in sequence in relation to a
 * specific emotion).
 * </p>
 */
public class CreateApprenticeActivity extends Activity implements OnClickListener {
    private Button buttonSave = null;
    private Apprentice newlyInsertedApprentice = new Apprentice();

    /**
     * onCreate override that provides apprentice creation view to user .
     * 
     * @param savedInstanceState Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_create_apprentice);

        // add listeners to buttons
        buttonSave = (Button) findViewById(R.id.button_save);
        buttonSave.setOnClickListener(this);
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
                Apprentice apprenticeToInsert = new Apprentice();

                String apprenticeName = ((EditText) findViewById(R.id.edittext_apprentice_name))
                        .getText()
                        .toString();

                apprenticeToInsert.setName(apprenticeName.toString());

                // first insert new apprentice (parent of all related notes)
                saveApprentice(v, apprenticeToInsert);

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
    private void saveApprentice(View v, Apprentice apprentice) {
        // save apprentice in database
        ApprenticesDataSource gds = new ApprenticesDataSource(this);
        setNewlyInsertedApprentice(gds.createApprentice(apprentice));
        gds.close();

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context,
                context.getResources().getString(R.string.apprentice_saved),
                duration);
        toast.show();
    }

    public Apprentice getNewlyInsertedApprentice() {
        return newlyInsertedApprentice;
    }

    public void setNewlyInsertedApprentice(Apprentice newlyInsertedApprentice) {
        this.newlyInsertedApprentice = newlyInsertedApprentice;
    }
}
