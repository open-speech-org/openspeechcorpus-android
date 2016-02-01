package com.contraslash.android.openspeechcorpus.apps.core.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.contraslash.android.openspeechcorpus.db.SQLiteHelper;

import java.util.ArrayList;

/**
 * Created by ma0 on 11/4/15.
 */
public class AudioDataDAO {

    private final String TAG = "AudioDataDAO";
    SQLiteDatabase database;
    SQLiteHelper helper;

    private String[] allColumns =
            {
                "_id",
                "sentence_id",
                "sentence_text",
                "fileLocation",
                "uploaded"
            };

    public AudioDataDAO(Context context) {
        helper = new SQLiteHelper(context);
    }


    public void open() throws SQLException {
        database = helper.getWritableDatabase();
    }

    public void close() {
        helper.close();
    }

    public void createObject(AudioData e)
    {
        open();
        ContentValues values = new ContentValues();
        values.put("sentence_id", e.getSentence_id());
        values.put("sentence_text", e.getSentence_text());
        values.put("fileLocation", e.getFileLocation());
        long insertId = database.insert(e.getTABLE_NAME(), null,
                values);

        e.set_id((int)insertId);

        close();
    }

    public ArrayList<AudioData> readRecorded()
    {
        open();
        ArrayList<AudioData> elementos = new ArrayList<>();

        Cursor cursor = database.query(
                AudioData.TABLE_NAME,
                allColumns,
                "uploaded = ?",
                new String[]{"1"},
                null,
                null,
                null
        );
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            AudioData object = new AudioData(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getInt(4)
            );
            elementos.add(object);
            //Log.i(TAG,object.toString());
            cursor.moveToNext();
        }
        cursor.close();

        close();

        return elementos;

    }

    public ArrayList<AudioData> readAll()
    {
        open();
        ArrayList<AudioData> elementos = new ArrayList<>();

        Cursor cursor = database.query(AudioData.TABLE_NAME,
                allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            AudioData object = new AudioData(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getInt(4)
            );
            elementos.add(object);
            //Log.i(TAG,object.toString());
            cursor.moveToNext();
        }
        cursor.close();

        close();

        return elementos;

    }

    public void update(AudioData e)
    {
        open();
        ContentValues values = new ContentValues();
        values.put("sentence_id", e.getSentence_id());
        values.put("sentence_text", e.getSentence_text());
        values.put("fileLocation", e.getFileLocation());
        values.put("uploaded", e.getUploaded());
        int result = database.update(e.getTABLE_NAME(), values, "_id = " + e.get_id(),null);
        Log.i(TAG,"Update Result: "+result);
        close();

    }

    public void delete(AudioData e)
    {

        open();
        database.delete(e.getTABLE_NAME(), "_id = " + e.get_id(),null);
        close();
    }
}
