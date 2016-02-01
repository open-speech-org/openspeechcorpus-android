package com.contraslash.android.openspeechcorpus.apps.tales.models;

import android.content.Context;

import com.contraslash.android.openspeechcorpus.db.BaseDAO;
import com.contraslash.android.openspeechcorpus.db.Table;

import java.util.ArrayList;

/**
 * Created by ma0 on 12/7/15.
 */
public class SentenceDAO extends BaseDAO {
    public SentenceDAO(Context context) {
        super(context);
    }

    @Override
    protected String getTableName() {
        return Sentence.TABLE_NAME;
    }

    @Override
    protected Object getSelf() {
        return this;
    }

    @Override
    protected Table getPrototype() {
        return new Sentence();
    }


    public ArrayList getSentencesByTaleId(int taleId)
    {
        ArrayList sentences = new ArrayList();
        ArrayList<Sentence> allSentences = all();
        for(Sentence sentence:allSentences)
        {
            if(sentence.getTale_id()==taleId)
            {
                sentences.add(sentence);
            }
        }

        return sentences;
    }
}
