package com.contraslash.android.openspeechcorpus.apps.tales.models;

import com.contraslash.android.openspeechcorpus.db.Table;

/**
 * Created by ma0 on 12/2/15.
 */
public class Tale extends Table {

    public static String TABLE_NAME = DB_PREFIX + "tale";
    int id;
    String author;
    int author_id;
    String title;
    String description;
    int totalVotes;
    float calification;
    int readed;

    public Tale(){}

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public int getTotalVotes() {
        return totalVotes;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    public void setTotalVotes(int totalVotes) {
        this.totalVotes = totalVotes;
    }

    public float getCalification() {
        return calification;
    }

    public void setCalification(float calification) {
        this.calification = calification;
    }

    public int getReaded() {
        return readed;
    }

    public void setReaded(int readed) {
        this.readed = readed;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
