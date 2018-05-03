package com.contraslash.android.openspeechcorpus.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.contraslash.android.network.Util;
import com.contraslash.android.openspeechcorpus.apps.aphasia.models.Level;
import com.contraslash.android.openspeechcorpus.apps.aphasia.models.LevelCategory;
import com.contraslash.android.openspeechcorpus.apps.aphasia.models.LevelSentence;
import com.contraslash.android.openspeechcorpus.apps.core.models.AudioData;
import com.contraslash.android.openspeechcorpus.apps.miscellany.models.Command;
import com.contraslash.android.openspeechcorpus.apps.news.models.New;
import com.contraslash.android.openspeechcorpus.apps.tales.models.Author;
import com.contraslash.android.openspeechcorpus.apps.tales.models.Sentence;
import com.contraslash.android.openspeechcorpus.apps.tales.models.Tale;

/**
 * Created by ma0 on 11/3/15.
 */
public class SQLiteHelper extends SQLiteOpenHelper{


    private String TAG = "SQLiteHelper";
    private static final String DATABASE_NAME = "openspeechcorpus.db";
    private static final int DATABASE_VERSION = 11;


    Table [] tables = {
            new AudioData(),
            new Author(),
            new Sentence(),
            new Tale(),
            new New(),
            new Command(),
            new Level(),
            new LevelCategory(),
            new LevelSentence()
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

//        for(Table table: tables)
//        {
//            String sentence = Utils.deleteTable(table);
//            Log.i(TAG, sentence);
//            database.execSQL(sentence);
//        }
        onCreate(database);
        patches(database, oldVersion);
    }

    Table [] tables1 = {
            new Author(),
            new Sentence(),
            new Tale()
    };


    private void patches(SQLiteDatabase database, int oldVersion)
    {
        if(oldVersion==8)
        {
            database.execSQL("ALTER TABLE OPS_tale ADD COLUMN readed integer");
        }
    }

    public void purgeDatabase()
    {
        SQLiteDatabase db = getWritableDatabase();
        for(Table table: tables1)
        {
            String drop  = Utils.deleteTable(table);
            Log.i(TAG,drop);
            db.execSQL(drop);
            String sentence = Utils.createTable(table);
            Log.i(TAG,sentence);
            db.execSQL(sentence);
        }
    }
}
