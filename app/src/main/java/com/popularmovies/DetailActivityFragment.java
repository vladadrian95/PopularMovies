package com.popularmovies;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_detail, container, false);

        // Receive data from intent
        Intent intent = getActivity().getIntent();
        String mPosterURL = intent.getStringExtra("POSTER");
        String mOriginalTitle = intent.getStringExtra("TITLE");
        String mPlot = intent.getStringExtra("PLOT");
        String mRating = intent.getStringExtra("RATING");
        String mReleaseDate = intent.getStringExtra("DATE");

        // Display movie details
        ((TextView) rootView.findViewById(R.id.textViewTitle)).setText(mOriginalTitle);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);
        Picasso.with(getActivity()).load(mPosterURL).into(imageView);
        ((TextView) rootView.findViewById(R.id.textViewPlot)).setText(mPlot);
        ((TextView) rootView.findViewById(R.id.textViewRating)).setText(mRating);
        ((TextView) rootView.findViewById(R.id.textViewDate)).setText(mReleaseDate);

        return rootView;
    }

}
