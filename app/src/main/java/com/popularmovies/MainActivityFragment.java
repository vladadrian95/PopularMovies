package com.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private final String your_personal_api_key = ""; //if you use the code of this app insert your themoviedb.org API key here
    private ImageAdapter mImageAdapter;
    private GridView mGridView;
    private String[] mMovieID;
    private String[] mMovieTitle;
    private String[] mMoviePlot;
    private String[] mMovieRating;
    private String[] mMovieDate;
    OnHeadlineSelectedListener mCallback;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        updateData();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            updateData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mImageAdapter = new ImageAdapter(getActivity());
        mGridView = (GridView) rootView.findViewById(R.id.gridview);
        mGridView.setAdapter(mImageAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                mCallback.onArticleSelected(mImageAdapter.getItem(position), mMovieTitle[position], mMoviePlot[position],
                        mMovieRating[position],mMovieDate[position]);
            }
        });

        return rootView;
    }

    //update the data from themoviedb.org
    private void updateData() {
        DownloadData dd = new DownloadData();
        if (isNetworkAvailable()) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String order_option = sharedPreferences.getString("order_by_select",getString(R.string.order_by_default));
            dd.execute(order_option);
        }
        else {
            Toast.makeText(getActivity(),"No internet connection. Try again when internet is available.",Toast.LENGTH_LONG).show();
        }
    }

    //check if internet is available
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateData();
    }

    // Container Activity must implement this interface
    public interface OnHeadlineSelectedListener {
        public void onArticleSelected(String poster, String title, String plot, String rating, String date);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnHeadlineSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    //this class handles all the data download in background
    private class DownloadData extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = DownloadData.class.getSimpleName();
        private String[] mIDs;
        private String[] mTitle;
        private String[] mPlot;
        private String[] mRating;
        private String[] mDate;

        @Override
        protected String[] doInBackground(String... params) {
            // If we don't have input params then this is the end of our journey
            if (params.length == 0) {
                return null;
            }
            // Now let's add some variables that we'll need
            HttpURLConnection urlConnection = null;
            BufferedReader reader;
            String dataJSONStr = null;
            String postersString[];

            // The app will now try to connect to themoviedb.org
            try {
                // Builds the url
                final String DATA_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
                final String SORT_PARAM = "sort_by";
                final String API_KEY = "api_key";

                Uri builtUri = Uri.parse(DATA_BASE_URL).buildUpon().
                        appendQueryParameter(SORT_PARAM,params[0]).
                        appendQueryParameter(API_KEY,your_personal_api_key).build();

                URL url = new URL(builtUri.toString());

                // Create the request to themoviedb.org, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                dataJSONStr = buffer.toString();
                postersString = getPosterDataFromJSON(dataJSONStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the data, there's no point in attemping
                // to parse it.
                return null;
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error ", e);
                //If code didn't successfully parsed JSON into a string show error
                return null;
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return postersString;
        }

        protected void onPostExecute(String[] result) {
            mImageAdapter.setThumbsString(result);
            mImageAdapter.notifyDataSetChanged();
            mGridView.setAdapter(mImageAdapter);
            mMovieID = mIDs;
            mMovieTitle = mTitle;
            mMoviePlot = mPlot;
            mMovieRating = mRating;
            mMovieDate = mDate;
        }

        private String[] getPosterDataFromJSON(String dataJSON) throws JSONException {
            final String RESULTS = "results";
            final String POSTER = "poster_path";
            final String ID = "id";
            final String TITLE = "original_title";
            final String PLOT = "overview";
            final String RATING = "vote_average";
            final String DATE = "release_date";
            final String poster_url = "http://image.tmdb.org/t/p/w342";
            final String id_url = "http://api.themoviedb.org/3/movie/";

            JSONObject inputJSON = new JSONObject(dataJSON);
            JSONArray moviesArray = inputJSON.getJSONArray(RESULTS);

            String[] posters = new String[moviesArray.length()];
            mIDs = new String[moviesArray.length()];
            mTitle = new String[moviesArray.length()];
            mPlot = new String[moviesArray.length()];
            mRating = new String[moviesArray.length()];
            mDate = new String[moviesArray.length()];
            for (int i = 0; i < moviesArray.length(); i++) {
                String poster;
                JSONObject movieJSON = moviesArray.getJSONObject(i);
                poster = movieJSON.getString(POSTER);
                posters[i] = poster_url + poster;
                mIDs[i] = id_url + movieJSON.getString(ID) + "?api_key=" + your_personal_api_key;
                mTitle[i] = movieJSON.getString(TITLE);
                mPlot[i] = movieJSON.getString(PLOT);
                mRating[i] = movieJSON.getString(RATING);
                mDate[i] = movieJSON.getString(DATE);
            }

            return posters;
        }
    }
}