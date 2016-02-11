
package com.andrewsummers.kanjoto.test.unit;

import java.util.ArrayList;
import java.util.List;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import com.andrewsummers.kanjoto.data.BookmarksDataSource;
import com.andrewsummers.kanjoto.data.KanjotoDatabaseHelper;
import com.andrewsummers.kanjoto.model.Bookmark;

public class BookmarksDataSourceTest extends AndroidTestCase {

    private BookmarksDataSource bds;
    private KanjotoDatabaseHelper db;
    private RenamingDelegatingContext context;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        context = new RenamingDelegatingContext(getContext(), "test_");
        db = new KanjotoDatabaseHelper(context);
        bds = new BookmarksDataSource(context);
    }

    @Override
    public void tearDown() throws Exception {
        db.close();
        super.tearDown();
    }

    public void test_createBookmark_paramBookmark() throws Throwable {
        Bookmark bookmark = new Bookmark();
        bookmark.setName("test bookmark");
        bookmark.setSerializedValue("test serialized value");
        Bookmark result = bds.createBookmark(bookmark);
        assertNotNull("created bookmark is not null", result);
    }

    public void test_deleteBookmark_paramBookmark() throws Throwable {
        Bookmark bookmark = new Bookmark();
        bookmark.setId(1);
        bookmark.setName("test bookmark");
        bookmark.setSerializedValue("test serialized value");
        Bookmark result = bds.createBookmark(bookmark);
        assertNotNull("created bookmark is not null", result);
        bds.deleteBookmark(bookmark);
        bookmark = new Bookmark();
        bookmark = bds.getBookmark(1);
        assertTrue(bookmark.getId() != 1);
    }

    public void test_getAllBookmarks() throws Throwable {
        test_createBookmark_paramBookmark();
        List<Bookmark> bookmarks = new ArrayList<Bookmark>();
        bookmarks = bds.getAllBookmarks();
        assertNotNull("get all bookmarks is not null", bookmarks);
        assertFalse(bookmarks.isEmpty());
    }

    public void test_getBookmark_paramBookmarkId() throws Throwable {
        test_createBookmark_paramBookmark();
        Bookmark bookmark = bds.getBookmark(1);
        assertNotNull("get bookmark is not null", bookmark);
    }

    public void test_updateBookmark_paramBookmark() throws Throwable {
        test_createBookmark_paramBookmark();
        Bookmark bookmark = bds.getBookmark(1);
        bookmark.setName("another");
        bds.updateBookmark(bookmark);
        Bookmark bookmark2 = bds.getBookmark(1);
        assertNotNull("update bookmark is not null", bookmark2);
        assertTrue(bookmark.getName().equals("another"));
    }
}
