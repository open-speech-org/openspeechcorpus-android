package com.contraslash.android.openspeechcorpus.apps.news.models;

import android.content.Context;

import com.contraslash.android.openspeechcorpus.apps.tales.models.Author;
import com.contraslash.android.openspeechcorpus.db.BaseDAO;
import com.contraslash.android.openspeechcorpus.db.Table;

/**
 * Created by ma0 on 1/5/16.
 */
public class NewDAO extends BaseDAO {

    static String TABLE_NAME = New.TABLE_NAME;

    public NewDAO(Context context) {
        super(context);
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected Object getSelf() {
        return this;
    }

    @Override
    protected Table getPrototype() {
        return new New();
    }
}
