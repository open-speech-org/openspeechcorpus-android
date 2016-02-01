package com.contraslash.android.openspeechcorpus.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TabLayout;
import android.util.Log;

import com.contraslash.android.network.Util;
import com.contraslash.android.openspeechcorpus.apps.core.models.AudioData;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by ma0 on 12/7/15.
 */
public abstract class BaseDAO{

    protected String TAG;
    SQLiteDatabase database;
    SQLiteHelper helper;

    Context context;

    protected abstract String getTableName();
    protected abstract Object getSelf();
    protected abstract Table getPrototype();

    private ArrayList<String> allColumns;
    private ArrayList<Integer> allTypes;

    public BaseDAO(Context context) {

        this.context=context;

        TAG = this.getClass().getSimpleName();
        helper = new SQLiteHelper(context);
        //helper.purgeDatabase();
        allColumns = Utils.getFieldNames(getPrototype());
        allTypes = Utils.getFieldTypes(getPrototype());
    }

    private static Field getField(Class clazz, String fieldName)
            throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Class superClass = clazz.getSuperclass();
            if (superClass == null) {
                throw e;
            } else {
                return getField(superClass, fieldName);
            }
        }
    }

    public void open() throws SQLException {
        database = helper.getWritableDatabase();
    }

    public void close() {
        helper.close();
    }

    public void create(Table object)
    {
        open();
        ContentValues values = new ContentValues();
        // Remove id from allColums

        for(int i=0;i<allColumns.size();i++)
        {
            try {
                Field field = getField(object.getClass(), allColumns.get(i));
                Log.i(TAG,allColumns.get(i)+" "+allTypes.get(i));
                if(allColumns.get(i).compareTo("_id")==0)
                {
                    continue;
                }
                field.setAccessible(true);
                switch (allTypes.get(i))
                {
                    case Utils.INT:
                        values.put(allColumns.get(i),(int)field.get(object));
                        break;
                    case Utils.FLOAT:
                        values.put(allColumns.get(i),(float)field.get(object));
                        break;
                    case Utils.DOUBLE:
                        values.put(allColumns.get(i),(double)field.get(object));
                        break;
                    case Utils.BOOLEAN:
                        values.put(allColumns.get(i), (boolean) field.get(object));
                        break;
                    case Utils.STRING:
                        values.put(allColumns.get(i),(String)field.get(object));
                        break;
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }

        long insertId = database.insert(object.getTableName(), null,
                values);

        object.set_id((int) insertId);

        Log.i(TAG,"Object created with id:"+insertId);

        close();
    }

    public Object read(int id)
    {
        open();
        Object object = null;

        Cursor cursor = database.query(
                getTableName(),
                allColumns.toArray(new String[allColumns.size()]),
                "_id="+id,
//                new String[]{id+""},
                null,
                null,
                null,
                null
        );
        Log.i(TAG,"Table Name: "+getTableName());
        Log.i(TAG,"Id : "+id);
        if(cursor!=null)
        {
            cursor.moveToFirst();
            if(!cursor.moveToFirst())
            {
              //  return null;
            }
            Log.i(TAG,"Cursor Column Count:"+cursor.getColumnCount());
            Log.i(TAG,"Cursor Count:"+cursor.getCount());
//            Log.i(TAG,"Cursor Count:"+cursor.);


            try {
                Constructor<?> constructor = null;
                try{
                    //Log.i(TAG,"Getting default empty constructor");
                    constructor = getPrototype().getClass().getConstructor(Context.class);
                }catch (NoSuchMethodException nse)
                {
                    //Log.i(TAG,"ERROR Getting default empty constructor");
                    Constructor[] constructors = getPrototype().getClass().getConstructors();
                    //Log.i(TAG,"Before Print constructors");
                    for(Constructor c: constructors)
                    {
                        constructor = c;

                        //Log.i(TAG,"Constructor Name"+c);
                        Type [] types = c.getGenericParameterTypes();
                        if(types.length==0)
                        {
                            break;
                        }
                        //Log.i(TAG,"Before Print types");
                        for(Type t:types)
                        {
                            //Log.i(TAG, "Constructor types: "+t);
                        }

                    }

                }
                object = constructor.newInstance();
                for (int i = 0; i < allColumns.size(); i++) {
                    Field field = getField(getPrototype().getClass(), allColumns.get(i));
                    field.setAccessible(true);

                    Log.i(TAG, "Field: " + field.getName() + " " + field.getType().getName());
                    Log.i(TAG, "Column: " + allColumns.get(i));
                    Log.i(TAG,"Type: "+allTypes.get(i));
                    try {
                        switch (allTypes.get(i)) {
                            case Utils.INT:
                                field.set(object, cursor.getInt(i));
                                break;
                            case Utils.FLOAT:
                                field.set(object, cursor.getFloat(i));
                                break;
                            case Utils.DOUBLE:
                                field.set(object, cursor.getDouble(i));
                                break;
                            case Utils.BOOLEAN:
                                field.set(object, (cursor.getInt(i) != 0));
                                break;
                            case Utils.STRING:
                                field.set(object, cursor.getString(i));
                                break;
                        }
                    }catch (CursorIndexOutOfBoundsException ciobe)
                    {
                        ciobe.printStackTrace();
                    }

                }
//                Log.i(TAG, object.toString());
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            cursor.close();
        }
        close();

        return object;
    }

    public ArrayList all()
    {
        open();
        ArrayList objects = new ArrayList<>();

        Cursor cursor = database.query(
                getTableName(),
                allColumns.toArray(new String[allColumns.size()]),
                "",
                null,
                null,
                null,
                null
        );
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            try {

                Constructor<?> constructor = null;
                try{
                    //Log.i(TAG,"Getting default empty constructor");
                    constructor = getPrototype().getClass().getConstructor(Context.class);
                }catch (NoSuchMethodException nse)
                {
                    //Log.i(TAG,"ERROR Getting default empty constructor");
                    Constructor[] constructors = getPrototype().getClass().getConstructors();
                    //Log.i(TAG,"Before Print constructors");
                    for(Constructor c: constructors)
                    {
                        constructor = c;

                        //Log.i(TAG,"Constructor Name"+c);
                        Type [] types = c.getGenericParameterTypes();
                        if(types.length==0)
                        {
                            break;
                        }
                        //Log.i(TAG,"Before Print types");
                        for(Type t:types)
                        {
                        //    Log.i(TAG, "Constructor types: "+t);
                        }

                    }

                }


                Object object = constructor.newInstance();
                for(int i=0;i<allColumns.size();i++)
                {
//                    Log.i(TAG, "Resolving column: " + allColumns.get(i) + " Of type: " + allTypes.get(i));
                    Field field = getField(getPrototype().getClass(), allColumns.get(i));
                    field.setAccessible(true);
                    switch (allTypes.get(i))
                    {
                        case Utils.INT:
                            field.set(object, cursor.getInt(i));
                            break;
                        case Utils.FLOAT:
                            field.set(object, cursor.getFloat(i));
                            break;
                        case Utils.DOUBLE:
                            field.set(object, cursor.getDouble(i));
                            break;
                        case Utils.BOOLEAN:
                            field.set(object, (cursor.getInt(i) != 0));
                            break;
                        case Utils.STRING:
                            field.set(object, cursor.getString(i));
                            break;
                    }

                }


                objects.add(object);
//                Log.i(TAG, object.toString());
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }


            cursor.moveToNext();
        }
        cursor.close();

        close();

        return objects;

    }


    public void update(Table object)
    {
        open();
        ContentValues values = new ContentValues();
        for(int i=0;i<allColumns.size();i++)
        {
            try {
                //Field field = object.getClass().getDeclaredField(allColumns.get(i));
                Field field = getField(object.getClass(), allColumns.get(i));

                field.setAccessible(true);
                switch (allTypes.get(i)) {
                    case Utils.INT:
                        values.put(allColumns.get(i), (int) field.get(object));
                        break;
                    case Utils.FLOAT:
                        values.put(allColumns.get(i), (float) field.get(object));
                        break;
                    case Utils.DOUBLE:
                        values.put(allColumns.get(i), (double) field.get(object));
                        break;
                    case Utils.BOOLEAN:
                        values.put(allColumns.get(i), (boolean) field.get(object));
                        break;
                    case Utils.STRING:
                        values.put(allColumns.get(i), (String) field.get(object));
                        break;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        int result = database.update(object.getTableName(), values, "_id = " + object.get_id(),null);
        Log.i(TAG,"Update Result: "+result);
        close();

    }

    public void delete(Table e)
    {
        open();
        int affectedRows = database.delete(e.getTableName(), "_id=?",new String[]{e.get_id()+""});
        Log.i(TAG," id:"+e.get_id());
        Log.i(TAG,"Afected Rows:"+affectedRows);
        close();
    }
}
