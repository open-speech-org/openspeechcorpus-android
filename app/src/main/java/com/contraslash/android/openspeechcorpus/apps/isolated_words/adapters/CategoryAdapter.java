package com.contraslash.android.openspeechcorpus.apps.isolated_words.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.contraslash.android.openspeechcorpus.R;
import com.contraslash.android.openspeechcorpus.apps.aphasia.models.Level;
import com.contraslash.android.openspeechcorpus.apps.isolated_words.models.Category;
import com.contraslash.android.openspeechcorpus.base.BaseCustomAdapter;

import java.util.List;

public class CategoryAdapter extends BaseCustomAdapter {
    public CategoryAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position,convertView,parent);

        Category category = (Category)objects.get(position);
        TextView title = view.findViewById(R.id.element_isolated_category_title);
        title.setText(category.getTitle());
        Log.i(TAG, category.getTitle());
        return view;

    }
}