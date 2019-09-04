package com.contraslash.android.openspeechcorpus.apps.isolated_words.models;

import android.content.Context;

import com.contraslash.android.openspeechcorpus.apps.aphasia.models.Level;
import com.contraslash.android.openspeechcorpus.db.BaseDAO;
import com.contraslash.android.openspeechcorpus.db.Table;

public class CategoryDAO extends BaseDAO {
    public CategoryDAO(Context context) {
        super(context);
    }

    @Override
    protected String getTableName() {
        return Level.TABLE_NAME;
    }

    @Override
    protected Object getSelf() {
        return this;
    }

    @Override
    protected Table getPrototype() {
        return new Category();
    }
}
