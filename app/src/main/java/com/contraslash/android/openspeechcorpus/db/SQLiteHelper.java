package com.contraslash.android.openspeechcorpus.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.contraslash.android.network.Util;
import com.contraslash.android.openspeechcorpus.apps.core.models.AudioData;

/**
 * Created by ma0 on 11/3/15.
 */
public class SQLiteHelper extends SQLiteOpenHelper{


    private String TAG = "SQLiteHelper";
    private static final String DATABASE_NAME = "openspeechcorpus.db";
    private static final int DATABASE_VERSION = 3;


    Table [] tables = {
            new AudioData()
    };

    // Database creation sql statement
    private static final String DATABASE_CREATE = "";

    public SQLiteHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database)
    {
        for(Table table: tables)
        {
            String sentence = Utils.createTable(table);
            Log.i(TAG,sentence);
            database.execSQL(sentence);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {

        for(Table table: tables)
        {
            String sentence = Utils.deleteTable(table);
            Log.i(TAG, sentence);
            database.execSQL(sentence);
        }
        onCreate(database);
    }
}
