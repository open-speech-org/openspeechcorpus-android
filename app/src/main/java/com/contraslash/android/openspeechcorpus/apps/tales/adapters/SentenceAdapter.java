package com.contraslash.android.openspeechcorpus.apps.tales.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.contraslash.android.openspeechcorpus.R;
import com.contraslash.android.openspeechcorpus.apps.tales.models.Sentence;
import com.contraslash.android.openspeechcorpus.base.BaseCustomAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ma0 on 12/2/15.
 */
public class SentenceAdapter extends BaseCustomAdapter {

    ArrayList<Boolean> uploaded;

    public SentenceAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        uploaded = new ArrayList<>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position,convertView,parent);
        Sentence sentence = (Sentence)objects.get(position);
        TextView text = (TextView)view.findViewById(R.id.element_sentence_text);
        text.setText(sentence.getText());
        RelativeLayout cardView = (RelativeLayout)view.findViewById(R.id.element_sentence_card_view);
        LinearLayout container = (LinearLayout)view.findViewById(R.id.element_sentence_linear_container);
        try {

            if (uploaded.get(position)) {
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

    public void addItemUploaded(boolean bool)
    {
        uploaded.add(bool);
    }
}
