package com.contraslash.android.openspeechcorpus.apps.news.models;

import com.contraslash.android.openspeechcorpus.db.Table;

/**
 * Created by ma0 on 1/5/16.
 */
public class New extends Table {

    public static String TABLE_NAME = DB_PREFIX + "new";

    String title;
    String body;

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    public  New(){}

    public New(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
