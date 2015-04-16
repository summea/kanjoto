
package com.andrewsummers.otashu.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.BookmarksDataSource;
import com.andrewsummers.otashu.data.ApprenticesDataSource;
import com.andrewsummers.otashu.model.Bookmark;
import com.andrewsummers.otashu.model.Apprentice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class ChooseApprenticeActivity extends Activity implements OnClickListener {
    private Button buttonGo = null;
    private Button buttonBookmark = null;
    private String lastSerializedNotes = "";
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
        buttonBookmark = (Button) findViewById(R.id.button_bookmark);
        buttonBookmark.setOnClickListener(this);

        buttonGo = (Button) findViewById(R.id.button_go);
        buttonGo.setOnClickListener(this);

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
            case R.id.button_go:
                // get all apprentices for spinner list
                List<Integer> allApprenticeIds = new ArrayList<Integer>();
                ApprenticesDataSource ads = new ApprenticesDataSource(this);
                allApprenticeIds = ads.getAllApprenticeIds();

                // set selected apprentice in spinner
                Spinner apprenticeSpinner = (Spinner) findViewById(R.id.spinner_apprentice);
                int selectedApprenticeValue = allApprenticeIds.get(apprenticeSpinner
                        .getSelectedItemPosition());
                ads.close();

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if we have a successful result returned from our child activity
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // save last generated note sequence for saving bookmark (if necessary)
                lastSerializedNotes = data.getStringExtra("serialized_notes");
            }
        }
    }

    public int save_bookmark() {
        Bookmark bookmark = new Bookmark();
        bookmark.setName("Untitled");
        bookmark.setSerializedValue(lastSerializedNotes);
        saveBookmark(bookmark);
        return 0;
    }

    private void saveBookmark(Bookmark bookmark) {
        // save bookmark in database
        BookmarksDataSource bds = new BookmarksDataSource(this);
        bds.createBookmark(bookmark);
        bds.close();

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context,
                context.getResources().getString(R.string.bookmark_saved),
                duration);
        toast.show();
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
