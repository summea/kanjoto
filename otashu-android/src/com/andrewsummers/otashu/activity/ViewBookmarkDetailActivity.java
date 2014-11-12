package com.andrewsummers.otashu.activity;

import java.util.LinkedList;
import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.BookmarksDataSource;
import com.andrewsummers.otashu.model.Bookmark;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * View details of a particular bookmark.
 */
public class ViewBookmarkDetailActivity extends Activity {
    
    private int bookmarkId = 0;
    
    /**
     * onCreate override used to get details view.
     * 
     * @param savedInstanceState
     *            Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_view_bookmark_detail);
        
        Log.d("MYLOG", "got list item id: " + getIntent().getExtras().getLong("list_id"));
        bookmarkId = (int) getIntent().getExtras().getLong("list_id");
        
        List<Long> allBookmarksData = new LinkedList<Long>();
        BookmarksDataSource ds = new BookmarksDataSource(this);

        allBookmarksData = ds.getAllBookmarkListDBTableIds();

        // prevent crashes due to lack of database data
        if (allBookmarksData.isEmpty())
            allBookmarksData.add((long) 0);

        
        Long[] allBookmarks = allBookmarksData
                .toArray(new Long[allBookmarksData.size()]);
        
        Bookmark bookmark = new Bookmark();
        bookmark = ds.getBookmark(allBookmarks[bookmarkId]);
        
        ds.close();

        TextView bookmarkName = (TextView) findViewById(R.id.bookmark_detail_name_value);
        bookmarkName.setText(bookmark.getName());
        
        TextView bookmarkSerializedValue = (TextView) findViewById(R.id.bookmark_detail_serialized_value_value);
        bookmarkSerializedValue.setText(bookmark.getSerializedValue());
    }
}