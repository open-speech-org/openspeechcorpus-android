package com.contraslash.android.openspeechcorpus.apps.isolated_words.models;

import android.content.Context;

import com.contraslash.android.openspeechcorpus.apps.aphasia.models.LevelSentence;
import com.contraslash.android.openspeechcorpus.db.BaseDAO;
import com.contraslash.android.openspeechcorpus.db.Table;

import java.util.ArrayList;

public class IsolatedWordDAO extends BaseDAO {

    public IsolatedWordDAO(Context context) {
        super(context);
    }

    @Override
    protected String getTableName() {
        return IsolatedWord.TABLE_NAME;
    }

    @Override
    protected Object getSelf() {
        return this;
    }

    @Override
    protected Table getPrototype() {
        return new IsolatedWord();
    }

    public ArrayList getWordsByCategoryId(int category_id)
    {
        ArrayList<IsolatedWord> allSentences = all();
        ArrayList words_by_category = new ArrayList();
        for(IsolatedWord levelSentence: allSentences)
        {
            if(levelSentence.get_category_id() == category_id)
            {
                words_by_category.add(levelSentence);
            }
        }
        return words_by_category;

    }
}
