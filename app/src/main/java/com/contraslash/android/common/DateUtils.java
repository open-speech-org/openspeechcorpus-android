package com.contraslash.android.common;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ma0 on 1/12/16.
 */
public class DateUtils {
    private static final String TAG = "DateUtils";

    public static boolean isAfterLastUpdate(String lastUpdate)
    {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date dateLastUpdate = format.parse(lastUpdate);
            Log.i(TAG, "DATE LAST UPDATE: " + format.format(dateLastUpdate));
            Date today = getToday();
            Log.i(TAG, "TODAY: " + format.format(today));
            return today.after(dateLastUpdate);
        }catch (ParseException pse)
        {
            pse.printStackTrace();
        }
        return false;
    }

    public static Date getToday()
    {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static String getSimpleTodayString()
    {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(getToday());
    }
}
