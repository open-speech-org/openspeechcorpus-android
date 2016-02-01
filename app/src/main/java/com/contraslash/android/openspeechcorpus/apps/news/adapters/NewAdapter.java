package com.contraslash.android.openspeechcorpus.apps.news.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.contraslash.android.openspeechcorpus.R;
import com.contraslash.android.openspeechcorpus.apps.news.models.New;
import com.contraslash.android.openspeechcorpus.apps.tales.models.Author;
import com.contraslash.android.openspeechcorpus.base.BaseCustomAdapter;

import java.util.List;

/**
 * Created by ma0 on 1/5/16.
 */
public class NewAdapter extends BaseCustomAdapter {



    public NewAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position,convertView,parent);
        final New element=(New) objects.get(position);
        TextView title = (TextView)view.findViewById(R.id.element_new_title);
        title.setText(element.getTitle());
        return view;
    }
}
