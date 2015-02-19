
package com.andrewsummers.otashu.activity;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.BookmarksDataSource;
import com.andrewsummers.otashu.data.OtashuDatabaseHelper;
import com.andrewsummers.otashu.model.Bookmark;

public class DatabaseDumperBookmarksActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_database_dumper);

        BookmarksDataSource bds = new BookmarksDataSource(this);
        List<Bookmark> allBookmarks = bds.getAllBookmarks();
        bds.close();

        TextView debugText = (TextView) findViewById(R.id.debug_text);

        debugText.setText(debugText.getText().toString() + "Table: Bookmarks\n"
                + OtashuDatabaseHelper.COLUMN_ID + "|" + OtashuDatabaseHelper.COLUMN_NAME + "|"
                + OtashuDatabaseHelper.COLUMN_SERIALIZED_VALUE + "\n");

        for (Bookmark bookmark : allBookmarks) {

            String newText = debugText.getText().toString();
            newText += bookmark.getId() + "|" + bookmark.getName() + "|"
                    + bookmark.getSerializedValue() + "\n";

            debugText.setText(newText);
        }
    }
}
