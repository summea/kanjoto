package com.andrewsummers.otashu.adapter;

import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.model.Bookmark;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BookmarkAdapter extends BaseAdapter {

    private Context mContext;
    private List<Bookmark> bookmarks;
    
    public BookmarkAdapter(Context context, List<Bookmark> allBookmarks) {
        mContext = context;
        bookmarks = allBookmarks;
    }
    
    @Override
    public int getCount() {
        return bookmarks.size();
    }

    @Override
    public Object getItem(int position) {
        return bookmarks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return bookmarks.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {        
        if (convertView == null) {
            convertView = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.row_bookmark, null);
        }
        
        TextView bookmark = (TextView) convertView.findViewById(R.id.bookmark);
        bookmark.setText(bookmarks.get(position).getName());

        return convertView;
    }
    
    public Object removeItem(int position) {
        return bookmarks.remove(position);
    }

    public void clear() {
        bookmarks.clear();
    }
}