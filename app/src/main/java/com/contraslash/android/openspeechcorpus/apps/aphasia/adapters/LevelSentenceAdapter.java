package com.contraslash.android.openspeechcorpus.apps.aphasia.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
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
        CardView cardView= (CardView) view.findViewById(R.id.element_level_sentence_cardview);
        text.setText(level.getText());
        Log.i(TAG, level.get_id()+"");
        Log.i(TAG, level.getText());
        Log.i(TAG, level.getUploaded()+"");
        try {

            if (level.getUploaded() == 1) {
//                try {
                cardView.setBackgroundColor(context.getResources().getColor(R.color.teal));
//                } catch (ClassCastException ccext) {
//                    //container.setBackgroundColor(R.color.teal);
//                }
            } else {
//                try {
                cardView.setBackgroundColor(context.getResources().getColor(R.color.white));
//                } catch (ClassCastException ccext) {
//                    //container.setBackgroundColor(R.color.white);
//                }
            }
        }catch (IndexOutOfBoundsException ioe)
        {
            Log.i(TAG,"Position of break: "+position);
            ioe.printStackTrace();
        }
        return view;

    }
}
