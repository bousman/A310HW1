package com.bousman.hw.a310hw1;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with main activity action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        createTestDb();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            // Get the SearchView and set the searchable configuration
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            MenuItem menuItem = menu.findItem(R.id.action_search);
            SearchView searchView = (SearchView) menuItem.getActionView();

            // Assumes current activity is the searchable activity
            SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
            searchView.setSearchableInfo(info);
            searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
            searchView.setSubmitButtonEnabled(true);

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_search) {
            onSearchRequested();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void createTestDb()
    {
        Log.d("main","start creating DB");
        // create database first time only
        DbHelper db = DbHelper.getInstance(this);
        ArrayList<DBTask> recs = db.getRecordList();
        if (recs.size() < 48) {
            Log.d("main","adding states to database");
            db.deleteRecords();
            db.addRecord("Alabama");
            db.addRecord("Alaska");
            db.addRecord("Arizona");
            db.addRecord("Arkansas");
            db.addRecord("California");
            db.addRecord("Colorado");
            db.addRecord("Connecticut");
            db.addRecord("Delaware");
            db.addRecord("Florida");
            db.addRecord("Georgia");
            db.addRecord("Hawaii");
            db.addRecord("Idaho");
            db.addRecord("Illinois");
            db.addRecord("Indiana");
            db.addRecord("Iowa");
            db.addRecord("Kansas");
            db.addRecord("Kentucky");
            db.addRecord("Louisiana");
            db.addRecord("Maine");
            db.addRecord("Maryland");
            db.addRecord("Massachusetts");
            db.addRecord("Michigan");
            db.addRecord("Minnesota");
            db.addRecord("Mississippi");
            db.addRecord("Missouri");
            db.addRecord("Montana");
            db.addRecord("Nebraska");
            db.addRecord("Nevada");
            db.addRecord("New Hampshire");
            db.addRecord("New Jersery");
            db.addRecord("New Mexico");
            db.addRecord("New York");
            db.addRecord("North Carolina");
            db.addRecord("North Dakota");
            db.addRecord("Ohio");
            db.addRecord("Oklahoma");
            db.addRecord("Oregon");
            db.addRecord("Pennsylvania");
            db.addRecord("Rhode Island");
            db.addRecord("South Carolina");
            db.addRecord("South Dakota");
            db.addRecord("Tennessee");
            db.addRecord("Texas");
            db.addRecord("Utah");
            db.addRecord("Vermont");
            db.addRecord("Virginia");
            db.addRecord("Washington");
            db.addRecord("West Virginia");
            db.addRecord("Wisconsin");
            db.addRecord("Wyoming");

            Log.d("main", "end creating DB");
        }

        recs = db.getRecordList();
        for ( DBTask r : recs )
        {
            Log.d("main", r.toString() );
        }


    }
}
