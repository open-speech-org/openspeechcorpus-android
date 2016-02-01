package com.contraslash.android.openspeechcorpus.apps.tales.models;

import android.content.Context;

import com.contraslash.android.openspeechcorpus.db.BaseDAO;
import com.contraslash.android.openspeechcorpus.db.Table;

/**
 * Created by ma0 on 12/7/15.
 */
public class AuthorDAO extends BaseDAO {

    static String TABLE_NAME = Author.TABLE_NAME;

    public AuthorDAO(Context context) {
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
        return new Author();
    }
}
