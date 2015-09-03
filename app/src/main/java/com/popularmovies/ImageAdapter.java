package com.popularmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private String[] mThumbsString = {
            "R.drawable.sample_0", "R.drawable.sample_0",
            "R.drawable.sample_0", "R.drawable.sample_0",
            "R.drawable.sample_0", "R.drawable.sample_0",
            "R.drawable.sample_0", "R.drawable.sample_0",
            "R.drawable.sample_0", "R.drawable.sample_0"
    };

    public ImageAdapter(Context c) {
        mContext = c;
    }

    @Override
    public int getCount() {
        return mThumbsString.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }
        Picasso.with(mContext).load(mThumbsString[position]).into(imageView);
        return imageView;
    }

    public void setThumbsString(String[] urls) {
        mThumbsString = urls;
    }

}
