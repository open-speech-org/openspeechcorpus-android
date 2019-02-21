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

    /**
     * Int determinate state of record: 0 Not recorded, 1 Uploaded, 2 Skipped
     */
    int uploaded;

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

    public int getUploaded() {
        return uploaded;
    }

    public void setUploaded(int uploaded) {
        this.uploaded = uploaded;
    }
}
