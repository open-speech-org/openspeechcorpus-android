package com.contraslash.android.openspeechcorpus.apps.authentication.activities;

import android.os.Bundle;

import com.contraslash.android.openspeechcorpus.R;
import com.contraslash.android.openspeechcorpus.apps.core.activities.UploadAudioData;
import com.contraslash.android.openspeechcorpus.base.BaseActivity;

public class SplashScreen extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                changeActivity(UploadAudioData.class);
            }
        }, 2000);

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_splash_screen;
    }

    @Override
    protected void mapGUI() {

    }

    @Override
    protected void loadEvents() {

    }


}
