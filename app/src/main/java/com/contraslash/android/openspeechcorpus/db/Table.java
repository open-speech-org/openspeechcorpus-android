package com.contraslash.android.openspeechcorpus.db;

/**
 * Created by ma0 on 11/3/15.
 */
public abstract class Table {

    protected int _id;


    public static String DB_PREFIX = "OPS_";

    public Table(){}

    public abstract String getTableName();

    public void set_id(int id)
    {
        this._id=id;
    }

    public int get_id()
    {
        return this._id;
    }

    public Table getPrototype()
    {
        return this;
    }


}
