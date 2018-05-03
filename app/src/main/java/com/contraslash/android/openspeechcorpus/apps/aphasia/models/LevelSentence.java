package com.contraslash.android.openspeechcorpus.apps.aphasia.models;

import com.contraslash.android.openspeechcorpus.db.Table;

/**
 * Created by ma0 on 4/5/18.
 */

public class LevelSentence extends Table {

    public static String TABLE_NAME = DB_PREFIX + "level_sentence";

    int id;
    int level_category_id;
    String text;

    public LevelSentence(){}

    public LevelSentence(int id, int level_category_id, String text) {
        this.id = id;
        this.level_category_id = level_category_id;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevel_category_id() {
        return level_category_id;
    }

    public void setLevel_category_id(int level_category_id) {
        this.level_category_id = level_category_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
