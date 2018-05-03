package com.contraslash.android.openspeechcorpus.apps.aphasia.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.contraslash.android.openspeechcorpus.R;
import com.contraslash.android.openspeechcorpus.apps.aphasia.models.LevelCategory;
import com.contraslash.android.openspeechcorpus.apps.aphasia.models.LevelSentence;
import com.contraslash.android.openspeechcorpus.base.BaseCustomAdapter;

import java.util.List;

/**
 * Created by ma0 on 4/5/18.
 */

public class LevelSentenceAdapter extends BaseCustomAdapter {

    public LevelSentenceAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        LevelSentence level = (LevelSentence) objects.get(position);
        TextView text = (TextView) view.findViewById(R.id.element_level_sentence_text);
        text.setText(level.getText());
        Log.i(TAG, level.getText());
        return view;

    }
}
