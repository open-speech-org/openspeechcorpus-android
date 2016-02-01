package com.contraslash.android.openspeechcorpus.db;

import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by ma0 on 11/3/15.
 */
public class Utils {

    private static String TAG = "UTILS_DB";

    public static final int INT = 0;
    public static final int FLOAT = 1;
    public static final int DOUBLE = 2;
    public static final int BOOLEAN = 3;
    public static final int STRING = 4;

    public static String createTable(Table table)
    {
        String createSentence = "CREATE TABLE IF NOT EXISTS " + table.getTableName() + " ( _id integer primary key,";
        for(Field f:table.getClass().getDeclaredFields())
        {
            createSentence += f.getName() + " ";
            createSentence += getSQLiteTypes(f.getType())+ ", ";
        }

        createSentence=createSentence.substring(0,createSentence.length()-2);
        createSentence +=  " );";

        return createSentence;
    }

    public static ArrayList<String> getFieldNames(Object object)
    {
        ArrayList<String> fieldNames = new ArrayList<>();
        fieldNames.add("_id");
        for(Field f:object.getClass().getDeclaredFields())
        {
        //    Log.i(TAG,"FIELD_NAME: "+f.getName());
            fieldNames.add(f.getName());
        }
        return fieldNames;
    }

    public static ArrayList<Integer> getFieldTypes(Object object)
    {
        ArrayList<Integer> fieldNames = new ArrayList<>();
        fieldNames.add(INT);
        for(Field f:object.getClass().getDeclaredFields())
        {
        //    Log.i(TAG,"FIELD_TYPE: "+f);
            fieldNames.add(getPrimitiveType(f));
        }
        return fieldNames;
    }

    public static int getPrimitiveType(Field object)
    {
        String class_name = object.getType().getName();
        //Log.i(TAG, "PRIMITIVE TYPE "+class_name);
        if(class_name.contains("int"))
        {
            return INT;
        }else if (class_name.contains("float"))
        {
            return FLOAT;
        }
        else if (class_name.contains("double"))
        {
            return DOUBLE;
        }
        else if (class_name.contains("boolean"))
        {
            return BOOLEAN;
        }
        else
        {
            return STRING;
        }

    }



    public static String deleteTable(Table table)
    {
        return "DROP TABLE IF EXISTS " + table.getTableName() + ";";
    }

    private static String getSQLiteTypes(Class cl)
    {
        String class_name=cl.toString().toLowerCase();
        if(class_name.contains("int"))
        {
            return "int";
        }else if (class_name.contains("string"))
        {
            return "text";
        }
        else if (class_name.contains("double"))
        {
            return "real";
        }
        else
        {
            return "text";
        }
    }
}
