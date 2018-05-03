package com.contraslash.android.openspeechcorpus.apps.aphasia.models;

import android.content.Context;

import com.contraslash.android.openspeechcorpus.db.BaseDAO;
import com.contraslash.android.openspeechcorpus.db.Table;

/**
 * Created by ma0 on 4/5/18.
 */

public class LevelDAO extends BaseDAO {
    public LevelDAO(Context context) {
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
        return new Level();
    }
}
