package com.contraslash.android.openspeechcorpus.apps.tales.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.contraslash.android.openspeechcorpus.R;
import com.contraslash.android.openspeechcorpus.apps.tales.models.Author;
import com.contraslash.android.openspeechcorpus.base.BaseCustomAdapter;

import java.util.List;

/**
 * Created by ma0 on 12/2/15.
 */
public class AuthorAdapter extends BaseCustomAdapter {

    public AuthorAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position,convertView,parent);
        final Author element=(Author) objects.get(position);
        TextView name = (TextView)view.findViewById(R.id.element_author_name);
        name.setText(element.getName());
        TextView birth = (TextView)view.findViewById(R.id.element_author_birth);
        birth.setText(element.getBirth()+" - "+element.getDeath());
        TextView biography = (TextView)view.findViewById(R.id.element_author_biography);
        biography.setText(element.getBiography());


        return view;
    }
}
