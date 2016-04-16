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

    public static final String URL = "content://" + AUTHORITY + "/financial";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int FINANCIAL = 1;
    private static final int FINANCIAL_ID = 2;

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
            case FINANCIAL:
                return "vnd.android.cursor.dir/financial";
            case FINANCIAL_ID:
                return "vnd.android.cursor.item/financial";
            default:
                return null;
        }
    }


    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
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
            case FINANCIAL:
                count = db.deleteRecords(selection,selectionArgs);
                break;

            case FINANCIAL_ID:
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
        Log.d("Provider","uri="+uri);
        Log.d("Provider","query="+queryText);
        Log.d("Provider", "selection=" + selection);
        Log.d("Provider","args="+ Arrays.toString(selectionArgs));


/*
        switch (sUriMatcher.match(uri)) {
            // If the incoming URI was for all of table
            case FINANCIAL:
                if (TextUtils.isEmpty(sortOrder)) sortOrder = "_ID ASC";
                break;

            // If the incoming URI was for a single row
            case FINANCIAL_ID:
                selection = selection + "_ID = " + uri.getLastPathSegment();
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri );
                break;
        }
    */
        // call the code to actually do the query
        return db.getCursor(projection,selection,selectionArgs,sortOrder);
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count = 0;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case FINANCIAL:
                count = db.updateRecord(values, selection, selectionArgs);
                break;

            case FINANCIAL_ID:
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
