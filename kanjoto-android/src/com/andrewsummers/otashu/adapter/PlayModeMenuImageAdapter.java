/*
 * Initial version of this adapter based on tutorial example code here:
 * http://developer.android.com/guide/topics/ui/layout/gridview.html
 * License: http://creativecommons.org/licenses/by/2.5/
 * Material has not yet been modified (other than resource naming).
 */

package com.andrewsummers.otashu.adapter;

import com.andrewsummers.otashu.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class PlayModeMenuImageAdapter extends BaseAdapter {
    private Context mContext;

    public PlayModeMenuImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) { // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            int width = (int) mContext.getResources().getDimension(R.dimen.image_width);
            int height = (int) mContext.getResources().getDimension(R.dimen.image_height);
            imageView.setLayoutParams(new GridView.LayoutParams(width, height));
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.apprentice_alt,
            R.drawable.train_alt,
            R.drawable.emotions_alt,
            R.drawable.generate_alt,
    };
}
