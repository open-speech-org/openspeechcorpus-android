package com.contraslash.android.openspeechcorpus.db;

/**
 * Created by ma0 on 11/3/15.
 */
public abstract class Table {

    protected int _id;

    public static String DB_PREFIX = "OPS_";


    public abstract String getTableName();


}
