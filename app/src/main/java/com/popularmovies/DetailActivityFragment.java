package com.popularmovies;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

        String mPosterURL;
        String mOriginalTitle;
        String mPlot;
        String mRating;
        String mReleaseDate;

        // Read arguments
        if (getArguments() == null) {
            Intent intent = getActivity().getIntent();
            mPosterURL = intent.getStringExtra("POSTER");
            mOriginalTitle = intent.getStringExtra("TITLE");
            mPlot = intent.getStringExtra("PLOT");
            mRating = intent.getStringExtra("RATING");
            mReleaseDate = intent.getStringExtra("DATE");

        } else {
            mPosterURL = getArguments().getString("POSTER-ARG");
            mOriginalTitle = getArguments().getString("TITLE-ARG");
            mPlot = getArguments().getString("PLOT-ARG");
            mRating = getArguments().getString("RATING-ARG");
            mReleaseDate = getArguments().getString("DATE-ARG");
        }

        // Display movie details
        ((TextView) rootView.findViewById(R.id.textViewTitle)).setText(mOriginalTitle);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.imageViewPoster);
        Picasso.with(getActivity()).load(mPosterURL).into(imageView);
        ((TextView) rootView.findViewById(R.id.textViewRating)).setText(mRating);
        ((TextView) rootView.findViewById(R.id.textViewDate)).setText(mReleaseDate);
        ((TextView) rootView.findViewById(R.id.textViewPlot)).setText(mPlot);

        return rootView;
    }

}
