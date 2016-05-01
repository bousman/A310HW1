package com.bousman.hw.a310hw1;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;


public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "a310hw1b.db";
    private static final int DB_VERSION = 7;
    private static DbHelper myDb = null;
    private SQLiteDatabase db;
    public static final String DB_SESSION_TABLE = "searches";
    private static final String CreateSessionTable = "create table "
            + DB_SESSION_TABLE
            + "(_id integer primary key autoincrement, "+ SearchManager.SUGGEST_COLUMN_TEXT_1+" text); ";


    private DbHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);

        open();
    }


    public static DbHelper getInstance(Context context)
    {
        if (myDb == null)
        {
            myDb = new DbHelper(context);
        }

        return myDb;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CreateSessionTable);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + DB_SESSION_TABLE);
        onCreate(db);
    }


    private void open() throws SQLiteException
    {
        db = getWritableDatabase();
    }


    public long addRecord(String state)
    {
        ContentValues values = new ContentValues();

        values.put(SearchManager.SUGGEST_COLUMN_TEXT_1, state);

        // returns the row ID
        return db.insert(DB_SESSION_TABLE, null, values);
    }


    public long addRecord( ContentValues values)
    {
        return db.insert(DB_SESSION_TABLE, null, values);
    }


    public ArrayList<DBTask> getRecordList()
    {
        ArrayList<DBTask> recordList = new ArrayList<>();

        Cursor cursor = db.query(DB_SESSION_TABLE, null, null, null, null,
                null, null);

        if (cursor.moveToFirst()) {
            while (true)
            {
                String state = cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1));
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                DBTask task = new DBTask( state );
                task.setId( id );

                recordList.add(task);
                if (!cursor.moveToNext())
                {
                    break;
                }
            }
        }
        cursor.close();

        return recordList;
    }


    public DBTask[] getRecords()
    {
        ArrayList<DBTask> recordList = getRecordList();

        DBTask[] dbRecords = new DBTask[recordList.size()];

        recordList.toArray(dbRecords);

        return dbRecords;
    }


    public int updateRecord(long id, String state)
    {
        ContentValues values = new ContentValues();

        values.put(SearchManager.SUGGEST_COLUMN_TEXT_1, state);

        //db.update(DB_SESSION_TABLE, values, "_id=" + id, null);
        return updateRecord(id,values,"_id="+id,null);
    }


    public int updateRecord(long id, ContentValues values, String selection, String[] selectionArgs)
    {
        String sel = "_id=" + id + selection;
        return db.update(DB_SESSION_TABLE, values, sel, selectionArgs);
    }


    public int updateRecord(ContentValues values, String selection, String[] selectionArgs)
    {
        return db.update(DB_SESSION_TABLE, values, selection, selectionArgs);
    }


    public int deleteRecord(long id)
    {
        return db.delete(DB_SESSION_TABLE, "_id=" + id, null);
    }


    public int deleteRecords(String selection, String[] selectionArgs)
    {
        return db.delete(DB_SESSION_TABLE,selection,selectionArgs);
    }


    public void deleteRecords()
    {
        db.delete(DB_SESSION_TABLE, null, null);
    }


    public Cursor getCursor(String[] projection, String selection,
                            String[] selectionArgs, String sortOrder)
    {
        //Log.d("DbHelper","projection="+ Arrays.toString(projection));
        //Log.d("DbHelper", "selection=" + selection);
        //Log.d("DbHelper","selectionArgs="+ Arrays.toString(selectionArgs));
        //Log.d("DbHelper", "sortOrder=" + sortOrder);

        Log.d("getCursor","about to make db query");
        Cursor cursor = db.query(DB_SESSION_TABLE,projection,selection,selectionArgs,null,null,sortOrder);
        Log.d("getCursor","done with db query");
        return cursor;
    }
}
