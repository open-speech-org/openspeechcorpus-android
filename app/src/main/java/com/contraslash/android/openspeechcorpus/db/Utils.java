package com.contraslash.android.openspeechcorpus.db;

import java.lang.reflect.Field;

/**
 * Created by ma0 on 11/3/15.
 */
public class Utils {
    public static String createTable(Table table)
    {
        String createSentence = "CREATE TABLE " + table.getTableName() + " ( _id integer primary key,";
        for(Field f:table.getClass().getDeclaredFields())
        {
            createSentence += f.getName() + " ";
            createSentence += getSQLiteTypes(f.getType())+ ", ";
        }

        createSentence=createSentence.substring(0,createSentence.length()-2);
        createSentence +=  " );";




        return createSentence;
    }

    public static String deleteTable(Table table)
    {
        return "DROP TABLE IF EXISTS " + table.getTableName();
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
