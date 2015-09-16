package com.popularmovies;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.OnHeadlineSelectedListener {

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check if the app runs on tablet
        //if it does then it will add the details fragment to the main activity
        if (findViewById(R.id.details) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.details, new DetailActivityFragment()).commit();
            }
        } else {
            mTwoPane = false;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onArticleSelected(String poster, String title, String plot, String rating, String date) {
        if (mTwoPane) {
            Bundle args = new Bundle();
            args.putString("POSTER-ARG",poster);
            args.putString("TITLE-ARG", title);
            args.putString("PLOT-ARG", plot);
            args.putString("RATING-ARG", rating);
            args.putString("DATE-ARG", date);

            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.details,fragment).commit();
        } else {
            Intent detailIntent = new Intent(this,DetailActivity.class);
            detailIntent.putExtra("POSTER",poster);
            detailIntent.putExtra("TITLE",title);
            detailIntent.putExtra("PLOT",plot);
            detailIntent.putExtra("RATING",rating);
            detailIntent.putExtra("DATE",date);
            startActivity(detailIntent);
        }
    }
}