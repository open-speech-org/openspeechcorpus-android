package com.contraslash.android.openspeechcorpus.apps.tales.models;

import com.contraslash.android.openspeechcorpus.db.Table;

/**
 * Created by ma0 on 12/2/15.
 */
public class Sentence extends Table {

    public  static String TABLE_NAME = DB_PREFIX + "sentence";
    int id;
    String tale;
    int tale_id;
    String text;

    /**
     * Int determinate state of record: 0 Not recorded, 1 Uploaded, 2 Skipped
     */
    int uploaded;

    public Sentence(){}

    @Override
    public  String getTableName() {
        return TABLE_NAME;
    }

    public static void setTableName(String tableName) {
        TABLE_NAME = tableName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTale() {
        return tale;
    }

    public void setTale(String tale) {
        this.tale = tale;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getTale_id() {
        return tale_id;
    }

    public void setTale_id(int tale_id) {
        this.tale_id = tale_id;
    }

    @Override
    public String toString() {
        return getText();
    }


    public int getUploaded() {
        return uploaded;
    }

    public void setUploaded(int uploaded) {
        this.uploaded = uploaded;
    }
}
