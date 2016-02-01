package com.contraslash.android.openspeechcorpus.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ma0 on 12/2/15.
 */
public class BaseCustomAdapter extends ArrayAdapter {
    protected String TAG;


    protected Context context;
    protected ArrayList objects;
    protected int resource;


    public  BaseCustomAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = (ArrayList)objects;
        TAG = getClass().getSimpleName();
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public void clear() {
        objects.clear();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=convertView;
        if(view==null)
        {
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(this.resource,null);
        }

        return view;
    }
}
