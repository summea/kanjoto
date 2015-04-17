
package com.andrewsummers.otashu.activity;

import java.util.ArrayList;
import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.ApprenticesDataSource;
import com.andrewsummers.otashu.model.Apprentice;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

/**
 * The ChooseApprenticeActivity class provides a form to get User input as to which Apprentice to
 * use for various Apprentice-related areas of the application (e.g. apprentice tests). Choosing an
 * Apprentice also determines what data to use for apprentice scorecards, apprentice scores, edges,
 * emotions, key signatures, and key notes.
 */
public class ChooseApprenticeActivity extends Activity implements OnClickListener {
    private Button buttonChoose = null;
    private SharedPreferences sharedPref;
    private long apprenticeId = 0;

    /**
     * onCreate override that provides apprentice-choose view to user.
     * 
     * @param savedInstanceState Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_choose_apprentice);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        apprenticeId = Long.parseLong(sharedPref.getString(
                "pref_selected_apprentice", "1"));

        // add listeners to buttons
        buttonChoose = (Button) findViewById(R.id.button_choose);
        buttonChoose.setOnClickListener(this);

        // get all apprentices for spinner
        ApprenticesDataSource ads = new ApprenticesDataSource(this);
        List<Apprentice> allApprentices = new ArrayList<Apprentice>();
        allApprentices = ads.getAllApprentices();
        ads.close();

        // locate next spinner in layout
        Spinner spinner = (Spinner) findViewById(R.id.spinner_apprentice);

        // create array adapter for list of apprentices
        ArrayAdapter<Apprentice> apprenticesAdapter = new ArrayAdapter<Apprentice>(this,
                android.R.layout.simple_spinner_item);
        apprenticesAdapter.addAll(allApprentices);

        // specify the default layout when list of choices appears
        apprenticesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // apply this adapter to the spinner
        spinner.setAdapter(apprenticesAdapter);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()) {
            case R.id.button_choose:
                // get all apprentices for spinner list
                List<Integer> allApprenticeIds = new ArrayList<Integer>();
                ApprenticesDataSource ads = new ApprenticesDataSource(this);
                allApprenticeIds = ads.getAllApprenticeIds();

                // set selected apprentice in spinner
                Spinner apprenticeSpinner = (Spinner) findViewById(R.id.spinner_apprentice);
                long selectedApprenticeId = allApprenticeIds.get(apprenticeSpinner
                        .getSelectedItemPosition());
                ads.close();

                // save selected apprentice to preferences
                if (selectedApprenticeId > 0) {
                    SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
                    editor.putString("pref_selected_apprentice",
                            Long.toString(selectedApprenticeId));
                    editor.apply();
                }

                finish();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;

        // handle menu item selection
        switch (item.getItemId()) {
            case R.id.view_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
