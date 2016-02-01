package com.contraslash.android.openspeechcorpus.apps.news.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;


import com.contraslash.android.openspeechcorpus.R;
import com.contraslash.android.openspeechcorpus.apps.news.models.New;
import com.contraslash.android.openspeechcorpus.apps.news.models.NewDAO;
import com.contraslash.android.openspeechcorpus.base.BaseActivity;
import com.contraslash.android.openspeechcorpus.config.Config;

import java.util.ArrayList;

public class NewsDetail extends BaseActivity {

    NewDAO newDAO;
    TextView title;
    TextView body;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_news_detail;
    }

    @Override
    protected void mapGUI() {
        toolbar = (Toolbar)findViewById(R.id.news_detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();

        body = (TextView)findViewById(R.id.news_detail_body);
        title = (TextView)findViewById(R.id.news_detail_title);


        if(bundle!=null)
        {
            Log.i(TAG,"Bundle not null");
            int newID =bundle.getInt(Config.NEW_ID,-1);
            if(newID>=0)
            {
                Log.i(TAG,"New id > 0");
                newDAO = new NewDAO(this);
                New newNew = (New) newDAO.read(newID);
//                Log.i(TAG,newNew.getTitle());
//                Log.i(TAG,newNew.getBody());
                title.setText(newNew.getTitle());
                body.setText(newNew.getBody());


            }
            else
            {
                Log.i(TAG,"New id NOT > 0");
            }
        }
        else
        {
            Log.i(TAG,"Bundle NULL");
        }



    }

    @Override
    protected void loadEvents() {

    }
}
