package com.andrewsummers.otashu.activity;

import java.util.ArrayList;
import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.BookmarksDataSource;
import com.andrewsummers.otashu.model.Bookmark;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * CreateBookmarkActivity is an Activity which provides users the ability to
 * create new bookmarks.
 */
public class EditBookmarkActivity extends Activity implements OnClickListener {
    private Button buttonSave = null;
    private Bookmark editBookmark;

    /**
     * onCreate override that provides bookmark creation view to user .
     * 
     * @param savedInstanceState
     *            Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_edit_bookmark);

        // add listeners to buttons
        // have to cast to Button in this case
        buttonSave = (Button) findViewById(R.id.button_save);
        buttonSave.setOnClickListener(this);

        // open data source handle
        BookmarksDataSource bds = new BookmarksDataSource(this);
        bds.open();
        
        int bookmarkId = (int) getIntent().getExtras().getLong("list_id");
        
        List<Bookmark> allBookmarks = new ArrayList<Bookmark>();
        allBookmarks = bds.getAllBookmarks();
        
        bds.close();

        editBookmark = allBookmarks.get(bookmarkId);
        
        EditText bookmarkNameText = (EditText) findViewById(R.id.edittext_bookmark_name);
        bookmarkNameText.setText(allBookmarks.get(bookmarkId).getName());
        
        TextView bookmarkColorText = (TextView) findViewById(R.id.textview_bookmark_serialized_value);
        bookmarkColorText.setText(allBookmarks.get(bookmarkId).getSerializedValue());
    }

    /**
     * onClick override used to save bookmark data once user clicks save button.
     * 
     * @param view
     *            Incoming view.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.button_save:
            // gather bookmark data from form
            String bookmarkName;
            String bookmarkSerializedValue;
            
            Bookmark bookmarkToUpdate = new Bookmark();
            
            bookmarkToUpdate.setId(editBookmark.getId());
            
            bookmarkName = ((EditText) findViewById(R.id.edittext_bookmark_name)).getText().toString();            
            bookmarkSerializedValue = ((TextView) findViewById(R.id.textview_bookmark_serialized_value)).getText().toString();
            
            bookmarkToUpdate.setName(bookmarkName.toString());
            bookmarkToUpdate.setSerializedValue(bookmarkSerializedValue.toString());
            
            // first insert new bookmark (parent of all related notes)
            saveBookmarkUpdates(v, bookmarkToUpdate);
            
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
     * Save bookmark data.
     * 
     * @param v
     *            Incoming view.
     * @param data
     *            Incoming string of data to be saved.
     */
    private void saveBookmarkUpdates(View v, Bookmark bookmark) {

        // save bookmark in database
        BookmarksDataSource bds = new BookmarksDataSource(this);
        bds.updateBookmark(bookmark);
        bds.close();
        
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context,
                context.getResources().getString(R.string.bookmark_saved),
                duration);
        toast.show();
    }
}