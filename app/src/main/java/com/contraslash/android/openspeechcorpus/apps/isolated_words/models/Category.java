package com.contraslash.android.openspeechcorpus.apps.isolated_words.models;

import com.contraslash.android.openspeechcorpus.db.Table;

public class Category extends  Table
{

    public static String TABLE_NAME = DB_PREFIX + "isolated_category";
    int id;
    String title;
    String description;

    public Category(){}

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    public Category(int id, String title, String description) {
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
