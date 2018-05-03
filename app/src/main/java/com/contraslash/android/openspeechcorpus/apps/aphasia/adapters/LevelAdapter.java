package com.contraslash.android.openspeechcorpus.apps.aphasia.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.contraslash.android.openspeechcorpus.R;
import com.contraslash.android.openspeechcorpus.apps.aphasia.models.Level;
import com.contraslash.android.openspeechcorpus.base.BaseCustomAdapter;

import java.util.List;

/**
 * Created by ma0 on 4/5/18.
 */

public class LevelAdapter extends BaseCustomAdapter {
    public LevelAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position,convertView,parent);

        Level level = (Level)objects.get(position);
        TextView title = (TextView)view.findViewById(R.id.element_level_title);
        title.setText(level.getTitle());
        Log.i(TAG, level.getTitle());
        return view;

    }
}
