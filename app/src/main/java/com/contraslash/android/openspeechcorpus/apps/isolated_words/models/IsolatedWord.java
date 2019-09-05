package com.contraslash.android.openspeechcorpus.apps.isolated_words.models;

import com.contraslash.android.openspeechcorpus.db.Table;

public class IsolatedWord extends Table {

    public static String TABLE_NAME = DB_PREFIX + "isolated_word";

    int id;
    int category_id;
    String text;

    /**
     * Int determinate state of record: 0 Not recorded, 1 Uploaded, 2 Skipped
     */
    int uploaded;

    public IsolatedWord(){}

    public IsolatedWord(int id, int category_id, String text) {
        this.id = id;
        this.category_id = category_id;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int get_category_id() {
        return category_id;
    }

    public void set_category_id(int category_id) {
        this.category_id = category_id;
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

    public int getUploaded() {
        return uploaded;
    }

    public void setUploaded(int uploaded) {
        this.uploaded = uploaded;
    }
}
