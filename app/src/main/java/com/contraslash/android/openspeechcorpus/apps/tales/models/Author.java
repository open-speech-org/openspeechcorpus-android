package com.contraslash.android.openspeechcorpus.apps.tales.models;

import com.contraslash.android.openspeechcorpus.db.Table;

/**
 * Created by ma0 on 12/2/15.
 */
public class Author extends Table {

    public static String TABLE_NAME = DB_PREFIX + "author";
    int id;
    String name;
    String biography;
    String birth;
    String death;

    public Author() {}

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getDeath() {
        return death;
    }

    public void setDeath(String death) {
        this.death = death;
    }

    @Override
    public String toString() {
        return getName();
    }
}
