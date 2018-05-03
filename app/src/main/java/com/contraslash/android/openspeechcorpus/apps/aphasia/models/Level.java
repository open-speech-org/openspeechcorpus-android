package com.contraslash.android.openspeechcorpus.apps.aphasia.models;

import com.contraslash.android.openspeechcorpus.db.Table;

import static com.contraslash.android.openspeechcorpus.db.Table.DB_PREFIX;

/**
 * Created by ma0 on 4/5/18.
 */

public class Level extends Table {

    public static String TABLE_NAME = DB_PREFIX + "level";
    int id;
    String title;
    String description;

    public Level(){}

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    public Level(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
