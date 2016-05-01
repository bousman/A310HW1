package com.bousman.hw.a310hw1;


import android.app.SearchManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SearchRecentSuggestionsProvider;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.util.Arrays;

public class MySuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "com.bousman.hw.a310hw1.MySuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public static final String URL = "content://" + AUTHORITY + "/state";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int US_STATE = 1;
    private static final int US_STATE_ID = 2;

    private DbHelper db;

    public MySuggestionProvider()
    {
        setupSuggestions(AUTHORITY, MODE);
    }

    @Override
    public boolean onCreate()
    {
        db = DbHelper.getInstance(getContext());
        return true;
    }


    @Nullable
    @Override
    public String getType(Uri uri) {
        int match = sUriMatcher.match(uri);
        Log.d("getType matches", "int="+match);
        switch (match) {
            case US_STATE:
                return "vnd.android.cursor.dir/state";
            case US_STATE_ID:
                return "vnd.android.cursor.item/state";
            default:
                return null;
        }
    }


    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d("insert",values.toString());
        long rowId = db.addRecord(values);
        if (rowId > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(_uri,null);
            return _uri;
        }
        Log.d("BrianProvider","Unable to add record");
        return uri;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case US_STATE:
                count = db.deleteRecords(selection,selectionArgs);
                break;

            case US_STATE_ID:
                String sid = uri.getPathSegments().get(1);
                long id = Integer.getInteger(sid);
                count = db.deleteRecord(id);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        String queryText = uri.getLastPathSegment().toLowerCase();
        Log.d("Provider", "uri=" + uri);
        Log.d("Provider","projection="+Arrays.toString(projection));
        Log.d("Provider", "query=" + queryText);
        Log.d("Provider", "selection=" + selection);
        Log.d("Provider","selectionArgs="+ Arrays.toString(selectionArgs));

        String[] proj = new String[] {"_id", "SUGGEST_COLUMN_TEXT_1"};
        String[] whereArgs = new String[] {"%"+selectionArgs[0]+"%"};

        // call the code to actually do the query
        Cursor testCursor = db.getCursor(proj, selection, whereArgs, sortOrder);
        Log.d("query","back with cursor");
        int counter = 0;
        try {
            testCursor.moveToFirst();
            while (!testCursor.isAfterLast()) {
                ++counter;
                int id = testCursor.getInt(0);
                String state = testCursor.getString(1);
                Log.d("result","ID="+id + " state="+state);
                testCursor.moveToNext();
            }
        } finally {
            //testCursor.close();
        }
        Log.d("Provider query", "cursor count: "+counter);

        //return db.getCursor(proj,selection,whereArgs,sortOrder);
        return testCursor;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count = 0;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case US_STATE:
                count = db.updateRecord(values, selection, selectionArgs);
                break;

            case US_STATE_ID:
                String sid = uri.getPathSegments().get(1);
                long id = Integer.getInteger(sid);
                count = db.updateRecord(id, values, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri );
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
