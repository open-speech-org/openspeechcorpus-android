package com.contraslash.android.openspeechcorpus.apps.history.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.contraslash.android.openspeechcorpus.R;
import com.contraslash.android.openspeechcorpus.apps.core.models.AudioData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ma0 on 11/10/15.
 */
public class HistoryAdapter extends ArrayAdapter<AudioData> {

    Context context;
    ArrayList<AudioData> history;
    int layout;

    public HistoryAdapter(Context context, int layout, List<AudioData> audioData)
    {
        super(context, layout,audioData);
        this.context=context;
        this.layout=layout;
        this.history = (ArrayList<AudioData>)audioData;

    }

}
