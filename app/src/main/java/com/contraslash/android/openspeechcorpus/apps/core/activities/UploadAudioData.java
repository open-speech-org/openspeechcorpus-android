package com.contraslash.android.openspeechcorpus.apps.core.activities;

import android.content.Context;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.contraslash.android.network.HttpConnection;
import com.contraslash.android.network.HttpConnectionMultipart;
import com.contraslash.android.network.HttpParameter;
import com.contraslash.android.network.MultipartParameter;
import com.contraslash.android.network.OnServerResponse;
import com.contraslash.android.network.Util;
import com.contraslash.android.openspeechcorpus.R;
import com.contraslash.android.openspeechcorpus.apps.authentication.activities.SplashScreen;
import com.contraslash.android.openspeechcorpus.apps.core.animations.CircleAnimation;
import com.contraslash.android.openspeechcorpus.apps.core.animations.CircleView;
import com.contraslash.android.openspeechcorpus.apps.core.models.AudioData;
import com.contraslash.android.openspeechcorpus.apps.core.models.AudioDataDAO;
import com.contraslash.android.openspeechcorpus.apps.history.activities.History;
import com.contraslash.android.openspeechcorpus.apps.profile.activities.MyProfile;
import com.contraslash.android.openspeechcorpus.apps.suggestions.activities.SendSuggestion;
import com.contraslash.android.openspeechcorpus.base.BaseActivity;
import com.contraslash.android.openspeechcorpus.config.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class UploadAudioData extends BaseActivity implements
        NavigationView.OnNavigationItemSelectedListener{
//public class UploadAudioData extends Activity {
    //http://android-er.blogspot.com.co/2015/02/create-audio-visualizer-for-mediaplayer.html


    //GUI Elemets

    Toolbar toolbar;

    static String mFileName = null;
    static String dirPath = "";

    ImageButton playButton;
    boolean mStartRecording = true;
    MediaRecorder mRecorder;

    ImageButton sendButton;
    boolean mStartPlaying = true;
    MediaPlayer mPlayer;

    CircleView circle;
    TextView recordStateText;

    TextView taleText;

    ImageView changeText;

    DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private final Handler mDrawerActionHandler = new Handler();
    private int mNavItemId;

    TextView helperMenu;

    //End of GUI Elements

    AudioDataDAO audioDataDAO;
    ArrayList<AudioData> records;
    AudioData record;


    boolean newUserRequested = false;


    boolean canUpload = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (null == savedInstanceState) {
            mNavItemId = R.id.menu_drawer_record;
        } else {
            mNavItemId = savedInstanceState.getInt(Config.NAV_ITEM_ID);
        }

        super.onCreate(savedInstanceState);

        //getPreferencias().edit().putInt(Config.USER_ID,11).apply();

        if(!getPreferencias().getBoolean(Config.SPLASH_SCREEN_SHOWED,false))
        {
            getPreferencias().edit().putBoolean(Config.SPLASH_SCREEN_SHOWED,true).apply();
            cambiarDeActividad(SplashScreen.class);
        }


        audioDataDAO=new AudioDataDAO(this);
        records = audioDataDAO.readAll();
        configureRecord(getRecord());

        //deleteAllNotUploaded();

        show_tutorial();

    }

    private void show_tutorial()
    {
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500); // half second between each showcase view

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, Config.SHOW_TUTORIAL);

        sequence.setConfig(config);

        sequence.addSequenceItem(
                circle,
                getString(R.string.hold_to_record),
                getString(R.string.understood)
        );

        sequence.addSequenceItem(
                playButton,
                getString(R.string.touch_to_play),
                getString(R.string.understood)
        );

        sequence.addSequenceItem(
                sendButton,
                getString(R.string.touch_to_upload),
                getString(R.string.understood)
        );

        sequence.addSequenceItem(
                helperMenu,
                getString(R.string.touh_to_open_menu),
                getString(R.string.understood)
        );

        sequence.start();

    }

    private void deleteAllNotUploaded()
    {
        for(AudioData record:records)
        {
            if(record.getUploaded()==0)
            {
                audioDataDAO.delete(record);
            }
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_upload_audio_data;
    }




    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
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
        }
    }

    private void stopPlaying() {
        if(mPlayer!=null)
        {
            mPlayer.release();

            mPlayer = null;
        }
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        try {
            mRecorder.prepare();
            mRecorder.start();
            Log.i(TAG, "Iniciando grabación");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "prepare() failed");
            Toast.makeText(this,getResources().getString(R.string.failed_start_recording), Toast.LENGTH_SHORT).show();
        }



    }

    private void stopRecording() {
        if(mRecorder!=null )
        {
            try
            {
                mRecorder.stop();
            }catch (RuntimeException rte)
            {
                rte.printStackTrace();
            }
            mRecorder.release();
            mRecorder = null;
            canUpload = true;
            Log.i(TAG, "Deteninedo grabación");
        }
        else
        {
            Log.i(TAG, "Grabación es NULL");
        }

    }


    public UploadAudioData() {
        dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/openspeechcorpus/records";
        File dir = new File(dirPath);
        Log.i(TAG, dir.mkdirs() + "");

    }


    @Override
    public void onPause() {
        super.onPause();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    protected void mapearGUI()
    {
        toolbar = (Toolbar)findViewById(R.id.upload_audio_data_toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.upload_audio_data_drawer_layout);

        playButton = (ImageButton)findViewById(R.id.upload_audio_data_play_button);
        sendButton = (ImageButton)findViewById(R.id.upload_audio_data_send_button);

        circle = (CircleView)findViewById(R.id.upload_audio_data_circle_view);
        int canvasSize = (int)getResources().getDimension(R.dimen.circle_animation_canvas_view_size);
        circle.setWidth(canvasSize);
        circle.setHeight(canvasSize);

        recordStateText = (TextView)findViewById(R.id.upload_audio_data_recording_text);

        taleText = (TextView)findViewById(R.id.upload_audio_data_tale_text);

        changeText = (ImageView)findViewById(R.id.upload_audio_data_change_text);

        helperMenu = (TextView)findViewById(R.id.upload_audio_data_helper_menu_for_showcase);

        //Drawer Layot configuration



        // listen for navigation events
        NavigationView navigationView = (NavigationView) findViewById(R.id.upload_audio_data_navigation);
//        LayoutInflater inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        LinearLayout layoutHeader = (LinearLayout) inflater.inflate(R.layout.drawer_header, null);
//        TextView nombre = (TextView)layoutHeader.findViewById(R.id.drawer_header_nombre);
//        nombre.setText(getPreferencias().getString("nombre", "Yo"));
//        navigationView.addHeaderView(layoutHeader);
        navigationView.setNavigationItemSelectedListener(this);

        // select the correct nav menu item
        MenuItem menuItem = navigationView.getMenu().findItem(mNavItemId);
        if(menuItem!=null)
        {
            menuItem.setChecked(true);
        }
        else
        {
            Log.i(TAG,"NAV id"+mNavItemId+"");
        }


        // set up the hamburger icon to open and close the drawer
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                toolbar,
                R.string.open,
                R.string.close
        );
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        navigate(mNavItemId);
    }

    @Override
    protected void cargarEventos()
    {



        getSupportActionBar().getThemedContext();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

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



        circle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        circle.setRadious(0);
                        CircleAnimation animation = new CircleAnimation(circle, 240);
                        animation.setDuration(1000);
                        animation.setRepeatCount(Animation.INFINITE);

                        circle.startAnimation(animation);
                        recordStateText.setText(getResources().getText(R.string.recording));
                        startRecording();
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        circle.clearAnimation();
                        circle.animate().cancel();
                        circle.setRadious(0);
                        circle.invalidate();
                        recordStateText.setText(getResources().getText(R.string.record));
                        stopRecording();
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();
            }
        });

        changeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                record.setUploaded(2);
                audioDataDAO.update(record);
                configureRecord(getRecord());

            }
        });
    }

    private void uploadData()
    {

        if(canUpload) {
            ArrayList<HttpParameter> parameters = new ArrayList<>();
            parameters.add(new HttpParameter("tale_sentence_id", record.getSentence_id() + ""));
            if (getPreferencias().getInt(Config.USER_ID, -1) > 0) {
                parameters.add(new HttpParameter(Config.ANONYMOUS_USER, getPreferencias().getInt(Config.USER_ID, 1) + ""));
            }

            ArrayList<MultipartParameter> multiparPatameters = new ArrayList<>();
            multiparPatameters.add(new MultipartParameter("audio", mFileName, "video/mp4"));

            HttpConnectionMultipart uploadAudio = new HttpConnectionMultipart(
                    this,
                    Config.BASE_URL + Config.API_BASE_URL + "/sentences/upload/",
                    null,
                    parameters,
                    multiparPatameters,
                    HttpConnection.POST,
                    new OnServerResponse() {
                        @Override
                        public void ConexionExitosa(int codigoRespuesta, String respuesta) {

                            Toast.makeText(UploadAudioData.this, UploadAudioData.this.getResources().getString(R.string.upload_successful), Toast.LENGTH_SHORT).show();
                            record.setUploaded(1);
                            audioDataDAO.update(record);
                            configureRecord(getRecord());
                        }

                        @Override
                        public void ConexionFallida(int codigoRespuesta) {
                            new Util(UploadAudioData.this).mostrarErrores(codigoRespuesta);
                        }

                        @Override
                        public void MalaParametrizacion() {
                            Log.i(TAG, "Bad Parametriation");
                        }
                    }
            );

            uploadAudio.initDialog(getResources().getString(R.string.uploading_audio), getResources().getString(R.string.may_take_few_seconds));
            uploadAudio.setDialogCancelable(false);
            uploadAudio.setShowDialog(true);
            uploadAudio.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else
        {
            Toast.makeText(this,getResources().getString(R.string.record_before_upload), Toast.LENGTH_SHORT).show();
        }
    }


    private void configureRecord(AudioData record)
    {
        if(record != null)
        {
            taleText.setText(record.getSentence_text());
            mFileName = dirPath + "/tale_" + record.getSentence_id() + ".mp4";
            record.setFileLocation(mFileName);

            Log.i(TAG, mFileName);

            audioDataDAO.update(record);

            this.record = record;
        }
        else
        {
            int offset=0;
            for(AudioData lastRecord:records)
            {
                if(lastRecord.getSentence_id()>offset)
                {
                    offset=lastRecord.getSentence_id();
                }
            }
            getRecordsFromServer(offset);
        }
        canUpload = false;
    }

    private void parseData(String json)
    {
        try
        {
            if(newUserRequested)
            {
                JSONObject response = new JSONObject(json);
                int userId = response.getInt(Config.ANONYMOUS_USER);
                getPreferencias().edit().putInt(Config.USER_ID,userId).apply();
                json = response.getString("sentences");
            }
            JSONArray sentences = new JSONArray(json);
            for(int i=0;i<sentences.length();i++)
            {
                JSONObject sentence = sentences.getJSONObject(i);
                Log.i(TAG, sentence.toString());
                AudioData record = new AudioData(-1,sentence.getInt("id"),sentence.getString("text"),"",0);
                audioDataDAO.createObject(record);
                records.add(record);

            }
        }catch (JSONException jse)
        {
            jse.printStackTrace();
        }
    }

    private AudioData getRecord()
    {
        AudioData emptyRecord= null;
        for(AudioData record:records)
        {
            if(record.getFileLocation().isEmpty() || record.getUploaded() == 0)
            {
                emptyRecord=record;
                break;
            }
        }

        return emptyRecord;
    }

    private void getRecordsFromServer(int offset)
    {
        String path = "/sentences/?offset="+offset;
        if(getPreferencias().getInt(Config.USER_ID,-1)<0)
        {
            newUserRequested=true;
            path+="&new_user=true";
        }
        HttpConnection getTaleText = new HttpConnection(
                this,
                Config.BASE_URL + Config.API_BASE_URL + path,
                new ArrayList<HttpParameter>(),
                new ArrayList<HttpParameter>(),
                HttpConnection.GET,
                new OnServerResponse() {
                    @Override
                    public void ConexionExitosa(int codigoRespuesta, String respuesta) {
                        parseData(respuesta);
                        AudioData record = getRecord();
                        configureRecord(record);
                    }

                    @Override
                    public void ConexionFallida(int codigoRespuesta) {
                        new Util(UploadAudioData.this).mostrarErrores(codigoRespuesta);
                    }

                    @Override
                    public void MalaParametrizacion() {

                    }
                }
        );

        getTaleText.initDialog(getResources().getString(R.string.downloading_text), getResources().getString(R.string.wait_please));

        getTaleText.setShowDialog(true);

        getTaleText.setDialogCancelable(false);

        getTaleText.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem menuItem) {
        // update highlighted item in the navigation menu
        menuItem.setChecked(true);
        mNavItemId = menuItem.getItemId();

        // allow some time after closing the drawer before performing real navigation
        // so the user can see what is happening
        mDrawerLayout.closeDrawer(GravityCompat.START);
        mDrawerActionHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                navigate(menuItem.getItemId());
            }
        }, Config.DRAWER_CLOSE_DELAY_MS);
        return true;
    }

    private void navigate(final int itemId) {
        // perform the actual navigation logic, updating the main content fragment etc
        switch (itemId) {
            case R.id.menu_drawer_custom_record:
                cambiarDeActividad(UploadCustomAudioData.class);
                break;
            case R.id.menu_drawer_history:
                cambiarDeActividad(History.class);
                break;
            case R.id.menu_drawer_suggestion:
                cambiarDeActividad(SendSuggestion.class);
                break;
            case R.id.menu_drawer_profile:
                cambiarDeActividad(MyProfile.class);
                break;
        }
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == android.support.v7.appcompat.R.id.home) {
            return mDrawerToggle.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Config.NAV_ITEM_ID, mNavItemId);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
}
