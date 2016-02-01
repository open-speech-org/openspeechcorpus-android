package com.contraslash.android.openspeechcorpus.apps.history.activities;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.contraslash.android.openspeechcorpus.R;
import com.contraslash.android.openspeechcorpus.base.BaseActivity;
import com.contraslash.android.openspeechcorpus.config.Config;

import java.io.IOException;

public class PlayHistoryRecord extends BaseActivity {

    //GUI


    Toolbar toolbar;

    TextView recordedText;

    ImageButton playButton;

    //End GUI

    boolean mStartRecording = true;
    MediaPlayer mPlayer;
    String mFileName;
    String textInFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        if(extras!=null)
        {
            Log.i(TAG,"Extras not null");
            mFileName = extras.getString(Config.FILE_TO_PLAY);
            textInFile = extras.getString(Config.TEXT_IN_FILE);
            Log.i(TAG, mFileName);
            Log.i(TAG, textInFile);
        }
        else
        {
            Log.i(TAG,"Extras NULL");
        }
        super.onCreate(savedInstanceState);


    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_play_history_record;
    }

    @Override
    protected void mapGUI() {
        toolbar = (Toolbar)findViewById(R.id.play_history_record_toolbar);

        recordedText = (TextView)findViewById(R.id.play_history_record_text);

        playButton = (ImageButton)findViewById(R.id.play_history_record_play_button);

    }

    @Override
    protected void loadEvents() {

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        playButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onPlay(mStartRecording);
                if (mStartRecording) {
                    playButton.setImageResource(R.drawable.ic_stop_black_18dp);
                } else {
                    playButton.setImageResource(R.drawable.ic_play_arrow_black_18dp);
                }
                mStartRecording = !mStartRecording;
            }
        });

        recordedText.setText(textInFile);
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    playButton.setImageResource(R.drawable.ic_play_arrow_black_18dp);
                    mStartRecording = !mStartRecording;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "prepare() failed");
            Toast.makeText(this,R.string.record_does_not_exist,Toast.LENGTH_SHORT).show();
            playButton.setImageResource(R.drawable.ic_play_arrow_black_18dp);
        }
    }

    private void stopPlaying() {
        if(mPlayer!=null)
        {
            mPlayer.release();

            mPlayer = null;
        }
    }


}
