package com.contraslash.android.openspeechcorpus.apps.isolated_words.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.contraslash.android.network.HttpConnection;
import com.contraslash.android.network.HttpConnectionMultipart;
import com.contraslash.android.network.HttpParameter;
import com.contraslash.android.network.MultipartParameter;
import com.contraslash.android.network.OnServerResponse;
import com.contraslash.android.network.Util;
import com.contraslash.android.openspeechcorpus.R;
import com.contraslash.android.openspeechcorpus.apps.aphasia.activities.LevelSentenceList;
import com.contraslash.android.openspeechcorpus.apps.aphasia.activities.UploadWordAudioData;
import com.contraslash.android.openspeechcorpus.apps.aphasia.models.LevelSentence;
import com.contraslash.android.openspeechcorpus.apps.aphasia.models.LevelSentenceDAO;
import com.contraslash.android.openspeechcorpus.apps.core.animations.CircleAnimation;
import com.contraslash.android.openspeechcorpus.apps.core.animations.CircleView;
import com.contraslash.android.openspeechcorpus.apps.core.models.AudioData;
import com.contraslash.android.openspeechcorpus.apps.core.models.AudioDataDAO;
import com.contraslash.android.openspeechcorpus.apps.isolated_words.models.IsolatedWord;
import com.contraslash.android.openspeechcorpus.apps.isolated_words.models.IsolatedWordDAO;
import com.contraslash.android.openspeechcorpus.base.BaseActivity;
import com.contraslash.android.openspeechcorpus.config.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class UploadIsolatedWordAudioData extends BaseActivity {

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

    TextView taleProgress;


    //End of GUI Elements

    AudioDataDAO audioDataDAO;
    IsolatedWordDAO isolatedWordDAO;
    IsolatedWord word;

    ArrayList<AudioData> records;
    AudioData record;

    ArrayList<Integer> sentences_ids;
    ArrayList<String> sentences_texts;
    int author_id;
    int sentence_id;
    String sentence_text;

    int current_id_index;

    boolean newUserRequested = false;


    boolean canUpload = false;
    boolean canRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        audioDataDAO=new AudioDataDAO(this);
        isolatedWordDAO=new IsolatedWordDAO(this);
//        taleDAO=new TaleDAO(this);

        current_id_index = 0;

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
        {

            Log.i(TAG,"Bundle not null");
            sentences_ids = bundle.getIntegerArrayList("sentences_ids");
            sentence_id = bundle.getInt("sentence_id");
            sentence_text = bundle.getString("text");
            sentences_texts = bundle.getStringArrayList("sentences_texts");

            author_id = bundle.getInt("author_id", 0);
            sentence_id = bundle.getInt("sentence_id", 0);
            Log.i(TAG,"SENTENCES ID:"+sentence_id);
            for(int i=0;i<sentences_ids.size(); i++)
            {
                if(sentences_ids.get(i)==sentence_id)
                {
                    current_id_index = i;
                    break;
                }
            }
        }
        else
        {
            Log.i(TAG, "Bundle NULL");
        }

        configureRecord();

        // Check for permissions
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.RECORD_AUDIO,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        },
                        Config.PERMISSION_TO_RECORD_AUDIO);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            canRecord = true;
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Config.PERMISSION_TO_RECORD_AUDIO: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    canRecord = true;
                } else {
                    canRecord = false;
                    Toast.makeText(this, getString(R.string.cant_record_message), Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_upload_tale_audio_data;
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
            playButton.setImageResource(R.drawable.ic_play_arrow_black_18dp);
            mStartRecording = !mStartRecording;
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
        if (!canRecord){
            Toast.makeText(this, getString(R.string.cant_record_message), Toast.LENGTH_LONG).show();
            return;
        }
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
            Toast.makeText(this, getResources().getString(R.string.failed_start_recording), Toast.LENGTH_SHORT).show();
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

    public UploadIsolatedWordAudioData() {
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
    protected void mapGUI()
    {
        toolbar = (Toolbar)findViewById(R.id.upload_tale_audio_data_toolbar);
        setSupportActionBar(toolbar);


        playButton = (ImageButton)findViewById(R.id.upload_tale_audio_data_play_button);
        sendButton = (ImageButton)findViewById(R.id.upload_tale_audio_data_send_button);

        circle = (CircleView)findViewById(R.id.upload_tale_audio_data_circle_view);
        int canvasSize = (int)getResources().getDimension(R.dimen.circle_animation_canvas_view_size);
        circle.setWidth(canvasSize);
        circle.setHeight(canvasSize);

        recordStateText = (TextView)findViewById(R.id.upload_tale_audio_data_recording_text);

        changeText = (ImageView)findViewById(R.id.upload_tale_audio_data_change_text);
//        changeText.setVisibility(View.GONE);

        taleText = (TextView)findViewById(R.id.upload_tale_audio_data_tale_text);

        taleProgress = (TextView)findViewById(R.id.upload_tale_audio_data_tale_progress);
//        taleProgress.setVisibility(View.GONE);


    }

    @Override
    protected void loadEvents()
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
                current_id_index +=1;
                configureRecord();

            }
        });

    }

    private void uploadData()
    {

        if(canUpload) {
            String encoded_text = "";
            try{
                encoded_text = URLEncoder.encode(sentence_text, "utf-8");
            }catch (UnsupportedEncodingException use)
            {
                use.printStackTrace();
            }
            ArrayList<HttpParameter> parameters = new ArrayList<>();
            parameters.add(new HttpParameter("isolated_word_id", word.getId() + ""));
            parameters.add(new HttpParameter("text", encoded_text));
            if (getPreferences().getInt(Config.USER_ID, -1) > 0) {
                parameters.add(new HttpParameter(Config.ANONYMOUS_USER, getPreferences().getInt(Config.USER_ID, 1) + ""));
            }
            Log.i(TAG, "Level Sentence ID: "+sentence_id+" Text: "+encoded_text);
            ArrayList<MultipartParameter> multiparPatameters = new ArrayList<>();
            multiparPatameters.add(new MultipartParameter("audio", mFileName, "video/mp4"));
            record.setSentence_text(taleText.getText()+"");

            HttpConnectionMultipart uploadAudio = new HttpConnectionMultipart(
                    this,
                    Config.BASE_URL + Config.API_BASE_URL + "/isolated-words/upload/",
                    null,
                    parameters,
                    multiparPatameters,
                    HttpConnection.POST,
                    new OnServerResponse() {
                        @Override
                        public void ConexionExitosa(int codigoRespuesta, String respuesta) {
                            try {
                                int status = new JSONObject(respuesta).getInt("error");
                                if(status == 0)
                                {
                                    Toast.makeText(UploadIsolatedWordAudioData.this, UploadIsolatedWordAudioData.this.getResources().getString(R.string.upload_successful), Toast.LENGTH_SHORT).show();
                                    record.setUploaded(1);
                                    word.setUploaded(1);
                                    Log.i(TAG, "setting uploadad to 1 in sentence " + word.get_id() + " And the recor id" + record.get_id());
                                    isolatedWordDAO.update(word);
                                    audioDataDAO.update(record);
                                    current_id_index += 1;
                                    configureRecord();
                                }
                                else
                                {
                                    Toast.makeText(UploadIsolatedWordAudioData.this, UploadIsolatedWordAudioData.this.getResources().getString(R.string.upload_failure), Toast.LENGTH_SHORT).show();
                                }
                            }
                            catch (JSONException jse)
                            {
                                jse.printStackTrace();
                            }

                        }

                        @Override
                        public void ConexionFallida(int codigoRespuesta) {
                            new Util(UploadIsolatedWordAudioData.this).mostrarErrores(codigoRespuesta);
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
            //TODO:  We're not uploading audio yet
            uploadAudio.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else
        {
            Toast.makeText(this,getResources().getString(R.string.record_before_upload), Toast.LENGTH_SHORT).show();
        }
    }

    private void configureRecord()
    {

        taleProgress.setText((current_id_index + 1) + "/" + sentences_ids.size());


        AudioData record = new AudioData();

        Log.i(TAG, "Current Index: " + current_id_index);
        Log.i(TAG, "SentencesIDS Size: " + sentences_ids.size());


        if(current_id_index > sentences_ids.size()-1)
        {
            // This validates end of tale
//            Toast.makeText(this, getString(R.string.tale_end), Toast.LENGTH_SHORT).show();
//            Tale tale = (Tale) taleDAO.read(tale_id);
//            tale.setReaded(1);
//            taleDAO.update(tale);
            return;
        }
        record.setSentence_id(sentences_ids.get(current_id_index));
        sentence_id = sentences_ids.get(current_id_index);
        word  =  (IsolatedWord) isolatedWordDAO.read(sentences_ids.get(current_id_index));
        Log.i(TAG, "Sentence: id"+ word.get_id() + " ID: " + word.getId() +  " text: " + word.getText() );
        taleText.setText(sentences_texts.get(current_id_index));
        sentence_text = sentences_texts.get(current_id_index);
        mFileName = dirPath + "/isolated_word_" + record.getSentence_id() + ".mp4";
        record.setFileLocation(mFileName);

        Log.i(TAG, mFileName);

        audioDataDAO.createObject(record);

        this.record = record;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Bundle b = new Bundle();
        changeActivity(IsolatedWordList.class, b);
    }
}
