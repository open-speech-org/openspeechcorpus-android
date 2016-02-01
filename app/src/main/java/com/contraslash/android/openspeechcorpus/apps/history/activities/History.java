package com.contraslash.android.openspeechcorpus.apps.history.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.contraslash.android.openspeechcorpus.R;
import com.contraslash.android.openspeechcorpus.apps.core.models.AudioData;
import com.contraslash.android.openspeechcorpus.apps.core.models.AudioDataDAO;
import com.contraslash.android.openspeechcorpus.base.BaseActivity;
import com.contraslash.android.openspeechcorpus.config.Config;

import java.util.ArrayList;

public class History extends BaseActivity {

    Toolbar toolbar;
    ListView historyListView;
    ArrayList<AudioData> history;
    ArrayAdapter<AudioData> historyAdapter;

    AudioDataDAO audioDataDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_history;
    }

    @Override
    protected void mapGUI() {
        toolbar = (Toolbar)findViewById(R.id.history_toolbar);

        historyListView = (ListView)findViewById(R.id.history_list_view);


    }

    @Override
    protected void loadEvents() {
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        audioDataDAO = new AudioDataDAO(this);
        history = audioDataDAO.readRecorded();

        historyAdapter = new ArrayAdapter<AudioData>(this, R.layout.element_history, R.id.element_history_text,history);

        historyListView.setAdapter(historyAdapter);


        historyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle=new Bundle();
                bundle.putString(Config.FILE_TO_PLAY,historyAdapter.getItem(position).getFileLocation());
                bundle.putString(Config.TEXT_IN_FILE,historyAdapter.getItem(position).getSentence_text());
                History.this.changeActivity(PlayHistoryRecord.class, bundle);
            }
        });

    }


}
