package com.contraslash.android.openspeechcorpus.apps.tales.utils;

import android.widget.ArrayAdapter;

import com.contraslash.android.openspeechcorpus.db.BaseDAO;

import java.util.ArrayList;

/**
 * Created by ma0 on 2/18/16.
 */
public interface JSONParser {
    public ArrayList execute(ArrayAdapter adapter, String json, BaseDAO dao);
}
