package com.contraslash.android.openspeechcorpus.apps.aphasia.models;

import android.content.Context;

import com.contraslash.android.openspeechcorpus.apps.tales.models.Tale;
import com.contraslash.android.openspeechcorpus.db.BaseDAO;
import com.contraslash.android.openspeechcorpus.db.Table;

import java.util.ArrayList;

/**
 * Created by ma0 on 4/5/18.
 */

public class LevelCategoryDAO extends BaseDAO {

    public LevelCategoryDAO(Context context) {
        super(context);
    }

    @Override
    protected String getTableName() {
        return LevelCategory.TABLE_NAME;
    }

    @Override
    protected Object getSelf() {
        return this;
    }

    @Override
    protected Table getPrototype() {
        return new LevelCategory();
    }

    public ArrayList getCategoriesByLevelId(int authorId)
    {
        ArrayList<LevelCategory> allCategories = all();
        ArrayList categoriesByLevel = new ArrayList();
        for(LevelCategory levelCategory: allCategories)
        {
            if(levelCategory.getLevel_id() == authorId)
            {
                categoriesByLevel.add(levelCategory);
            }
        }
        return categoriesByLevel;

    }
}
