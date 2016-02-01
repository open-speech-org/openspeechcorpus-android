package com.contraslash.android.openspeechcorpus.apps.miscellany.models;

import android.content.Context;

import com.contraslash.android.openspeechcorpus.apps.tales.models.Author;
import com.contraslash.android.openspeechcorpus.db.BaseDAO;
import com.contraslash.android.openspeechcorpus.db.Table;

/**
 * Created by ma0 on 1/8/16.
 */
public class CommandDAO extends BaseDAO {

    static String TABLE_NAME = Command.TABLE_NAME;

    public CommandDAO(Context context) {
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
        return new Command();
    }
}
