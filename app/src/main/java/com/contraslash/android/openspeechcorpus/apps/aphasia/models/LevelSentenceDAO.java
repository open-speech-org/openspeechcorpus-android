package com.contraslash.android.openspeechcorpus.apps.aphasia.models;

import android.content.Context;

import com.contraslash.android.openspeechcorpus.db.BaseDAO;
import com.contraslash.android.openspeechcorpus.db.Table;

import java.util.ArrayList;

/**
 * Created by ma0 on 4/5/18.
 */

public class LevelSentenceDAO extends BaseDAO {

    public LevelSentenceDAO(Context context) {
        super(context);
    }

    @Override
    protected String getTableName() {
        return LevelSentence.TABLE_NAME;
    }

    @Override
    protected Object getSelf() {
        return this;
    }

    @Override
    protected Table getPrototype() {
        return new LevelSentence();
    }

    public ArrayList getSentencesByLevelCategoryId(int authorId)
    {
        ArrayList<LevelSentence> allSentences = all();
        ArrayList sentncesByCategory = new ArrayList();
        for(LevelSentence levelSentence: allSentences)
        {
            if(levelSentence.getLevel_category_id() == authorId)
            {
                sentncesByCategory.add(levelSentence);
            }
        }
        return sentncesByCategory;

    }
}
