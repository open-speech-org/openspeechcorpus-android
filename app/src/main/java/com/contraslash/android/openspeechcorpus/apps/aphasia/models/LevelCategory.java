package com.contraslash.android.openspeechcorpus.apps.aphasia.models;

import com.contraslash.android.openspeechcorpus.db.Table;

/**
 * Created by ma0 on 4/5/18.
 */

public class LevelCategory extends Table {

    public static String TABLE_NAME = DB_PREFIX + "level_category";
    int id;
    int level_id;
    String title;

    public LevelCategory(){}

    public LevelCategory(int id, int level_id, String title) {
        this.id = id;
        this.level_id = level_id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevel_id() {
        return level_id;
    }

    public void setLevel_id(int level_id) {
        this.level_id = level_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }


}
