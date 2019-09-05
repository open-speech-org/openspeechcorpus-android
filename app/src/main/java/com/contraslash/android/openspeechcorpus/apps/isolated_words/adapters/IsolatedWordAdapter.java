package com.contraslash.android.openspeechcorpus.apps.isolated_words.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.contraslash.android.openspeechcorpus.R;
import com.contraslash.android.openspeechcorpus.apps.aphasia.models.LevelSentence;
import com.contraslash.android.openspeechcorpus.apps.isolated_words.models.IsolatedWord;
import com.contraslash.android.openspeechcorpus.apps.isolated_words.models.IsolatedWordDAO;
import com.contraslash.android.openspeechcorpus.base.BaseCustomAdapter;

import java.util.List;

public class IsolatedWordAdapter extends BaseCustomAdapter {

    public IsolatedWordAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        IsolatedWord isolatedWord = (IsolatedWord) objects.get(position);
        TextView text = view.findViewById(R.id.element_isolated_word_test);
        CardView cardView= view.findViewById(R.id.element_isolated_word_cardview);
        text.setText(isolatedWord.getText());
        Log.i(TAG, isolatedWord.get_id()+"");
        Log.i(TAG, isolatedWord.getText());
        Log.i(TAG, isolatedWord.getUploaded()+"");
        try {

            if (isolatedWord.getUploaded() == 1) {
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
