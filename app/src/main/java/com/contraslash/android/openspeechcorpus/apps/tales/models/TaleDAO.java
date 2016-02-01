package com.contraslash.android.openspeechcorpus.apps.tales.models;

import android.content.Context;

import com.contraslash.android.openspeechcorpus.db.BaseDAO;
import com.contraslash.android.openspeechcorpus.db.Table;

import java.util.ArrayList;

/**
 * Created by ma0 on 12/7/15.
 */
public class TaleDAO extends BaseDAO {
    public TaleDAO(Context context) {
        super(context);
    }

    @Override
    protected String getTableName() {
        return Tale.TABLE_NAME;
    }

    @Override
    protected Object getSelf() {
        return this;
    }

    @Override
    protected Table getPrototype() {
        return new Tale();
    }


    public ArrayList getTalesByAuthorId(int authorId)
    {
        ArrayList<Tale> allTales = all();
        ArrayList authorTales = new ArrayList();
        for(Tale tale: allTales)
        {
            if(tale.getAuthor_id() == authorId)
            {
                authorTales.add(tale);
            }
        }
        return authorTales;

    }
}
