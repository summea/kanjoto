
package com.andrewsummers.otashu.activity;

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
 * EditBookmarkActivity is an Activity which provides users the ability to edit bookmarks.
 * <p>
 * This activity provides a form for editing existing Bookmarks. Bookmark to edit is selected either
 * via the "view all bookmarks" activity or by the related "edit" context menu. The edit form fills
 * in data found (from the database) for specified Bookmark to edit and (if successful) any saved
 * updates will then be saved in the database.
 * </p>
 */
public class EditBookmarkActivity extends Activity implements OnClickListener {
    private Button buttonSave = null;
    private Bookmark editBookmark; // keep track of which Bookmark is currently being edited

    /**
     * onCreate override that provides bookmark creation view to user .
     * 
     * @param savedInstanceState Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_edit_bookmark);

        // add listeners to buttons
        buttonSave = (Button) findViewById(R.id.button_save);
        buttonSave.setOnClickListener(this);

        // open data source handle
        BookmarksDataSource bds = new BookmarksDataSource(this);
        long bookmarkId = getIntent().getExtras().getLong("list_id");

        editBookmark = bds.getBookmark(bookmarkId);
        bds.close();

        // fill in existing form data
        EditText bookmarkNameText = (EditText) findViewById(R.id.edittext_bookmark_name);
        bookmarkNameText.setText(editBookmark.getName());

        TextView bookmarkColorText = (TextView) findViewById(R.id.textview_bookmark_serialized_value);
        bookmarkColorText.setText(editBookmark.getSerializedValue());
    }

    /**
     * onClick override used to save bookmark data once user clicks save button.
     * 
     * @param view Incoming view.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_save:
                // gather bookmark data from form
                Bookmark bookmarkToUpdate = new Bookmark();
                bookmarkToUpdate.setId(editBookmark.getId());

                String bookmarkName = ((EditText) findViewById(R.id.edittext_bookmark_name))
                        .getText()
                        .toString();
                String bookmarkSerializedValue = ((TextView) findViewById(R.id.textview_bookmark_serialized_value))
                        .getText().toString();

                bookmarkToUpdate.setName(bookmarkName.toString());
                bookmarkToUpdate.setSerializedValue(bookmarkSerializedValue.toString());

                // first insert new bookmark (parent of all related notes)
                saveBookmarkUpdates(v, bookmarkToUpdate);

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
     * Save bookmark data.
     * 
     * @param v Incoming view.
     * @param data Incoming string of data to be saved.
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
