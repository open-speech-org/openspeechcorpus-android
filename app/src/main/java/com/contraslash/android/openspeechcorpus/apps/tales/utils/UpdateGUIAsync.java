package com.contraslash.android.openspeechcorpus.apps.tales.utils;

import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import com.contraslash.android.openspeechcorpus.apps.core.activities.UploadAudioData;
import com.contraslash.android.openspeechcorpus.db.BaseDAO;

import java.util.ArrayList;

/**
 * Created by ma0 on 2/18/16.
 */
public class UpdateGUIAsync extends AsyncTask<Void, Void, ArrayList> {

    ArrayAdapter adaptapter;
    BaseDAO dao;
    String json;
    JSONParser executor;

    public UpdateGUIAsync(ArrayAdapter adaptapter, BaseDAO dao, String json, JSONParser executor) {
        this.adaptapter = adaptapter;
        this.dao = dao;
        this.json = json;
        this.executor = executor;
    }

    @Override
    protected ArrayList doInBackground(Void... params) {
        ArrayList arrayList = executor.execute(adaptapter, json, dao);
        return arrayList;

    }

    @Override
    protected void onPostExecute(ArrayList arrayList) {
        if(adaptapter !=null)
        {
            adaptapter.addAll(arrayList);
            adaptapter.notifyDataSetChanged();
        }
    }
}
