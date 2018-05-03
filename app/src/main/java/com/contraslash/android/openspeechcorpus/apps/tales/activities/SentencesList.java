package com.contraslash.android.openspeechcorpus.apps.tales.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.contraslash.android.network.HttpConnection;
import com.contraslash.android.network.HttpParameter;
import com.contraslash.android.network.OnServerResponse;
import com.contraslash.android.openspeechcorpus.R;
import com.contraslash.android.openspeechcorpus.apps.core.models.AudioData;
import com.contraslash.android.openspeechcorpus.apps.core.models.AudioDataDAO;
import com.contraslash.android.openspeechcorpus.apps.core.utils.AudioDataComparator;
import com.contraslash.android.openspeechcorpus.apps.tales.adapters.SentenceAdapter;
import com.contraslash.android.openspeechcorpus.apps.tales.models.Sentence;
import com.contraslash.android.openspeechcorpus.apps.tales.models.SentenceDAO;
import com.contraslash.android.openspeechcorpus.apps.tales.models.TaleDAO;
import com.contraslash.android.openspeechcorpus.apps.tales.utils.JSONParser;
import com.contraslash.android.openspeechcorpus.apps.tales.utils.UpdateGUIAsync;
import com.contraslash.android.openspeechcorpus.base.BaseActivity;
import com.contraslash.android.openspeechcorpus.config.Config;
import com.contraslash.android.openspeechcorpus.db.BaseDAO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class SentencesList extends BaseActivity {

    Toolbar toolbar;
    ListView sentencesListView;
    SentenceAdapter sentenceAdapter;
    ArrayList<Sentence> sentences;
    ArrayList<Integer> sentences_id;
    ArrayList<String> sentences_texts;

    SentenceDAO sentenceDAO;
    AudioDataDAO audioDataDAO;
    ArrayList<AudioData> audioDatas;

    ProgressBar progressBar;

    int tale_id;
    int author_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_sentences_list;
    }

    @Override
    protected void mapGUI() {
        toolbar = (Toolbar)findViewById(R.id.sentences_list_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sentencesListView = (ListView)findViewById(R.id.sentences_list_list_view);
        progressBar = (ProgressBar)findViewById(R.id.sentences_list_progress);

        sentencesListView.setEmptyView(progressBar);

        sentenceAdapter = new SentenceAdapter(this, R.layout.element_sentence, new ArrayList());
        sentencesListView.setAdapter(sentenceAdapter);

        sentences_id = new ArrayList<>();
        sentences_texts = new ArrayList<>();
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
    protected void loadEvents() {
        sentenceDAO = new SentenceDAO(this);
        audioDataDAO = new AudioDataDAO(this);
        sentencesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle b = new Bundle();
                b.putInt("sentence_id",((Sentence)(sentenceAdapter.getItem(position))).getId());
                b.putInt("tale_id",tale_id);
                b.putInt("author_id",author_id);
                b.putIntegerArrayList("sentences_ids", sentences_id);
                b.putStringArrayList("sentences_texts", sentences_texts);
                Log.i(TAG,"Sentence ID"+(((Sentence)(sentenceAdapter.getItem(position))).getId()));
                changeActivity(UploadTaleAudioData.class, b);
            }
        });
        new LoadDataFromDB().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private class LoadDataFromDB extends AsyncTask<Void, Void, ArrayList>
    {

        @Override
        protected ArrayList doInBackground(Void... params) {

            Log.i(TAG, "AudioDataDao Created");
            audioDatas = audioDataDAO.readAll();
            Collections.sort(audioDatas,new AudioDataComparator());
            ArrayList arrayList = new ArrayList();

            return arrayList;
        }

        @Override
        protected void onPostExecute(ArrayList arrayList) {
            if(arrayList != null)
            {
                Bundle bundle = getIntent().getExtras();
                if(bundle!=null)
                {
                    tale_id= bundle.getInt("tale_id",-1);
                    author_id= bundle.getInt("author_id",-1);

                    if(tale_id>0)
                    {
                        arrayList = sentenceDAO.getSentencesByTaleId(tale_id);
                        if(arrayList.isEmpty())
                        {
                            getDataFromServer();
                        }
                        else
                        {
                            sentences = arrayList;
                            sentenceAdapter.clear();
                            calculateReaded(sentences);
                            sentenceAdapter.addAll(sentences);
                        }
                    }
                }


            }
        }
    }

    private void parseSentences(String json)
    {
        try
        {
            JSONObject response= new JSONObject(json);
            if(response.getInt(Config.ERROR_TEXT)==0)
            {

                UpdateGUIAsync updateSentences = new UpdateGUIAsync(
                        sentenceAdapter,
                        sentenceDAO,
                        response.getString("sentences"),
                        new JSONParser() {
                            @Override
                            public ArrayList execute(ArrayAdapter adapter, String json, BaseDAO dao) {
                                ArrayList arrayList = new ArrayList();
                                try {
                                    JSONArray sentences = new JSONArray(json);
                                    ArrayList<Sentence> sentencesList = new ArrayList<>();
                                    adapter.clear();
                                    for (int i = 0; i < sentences.length(); i++) {
                                        JSONObject jsonTale = sentences.getJSONObject(i);
                                        int sentenceId = jsonTale.getInt("id");

                                        Sentence new_sentence = new Sentence();
                                        new_sentence.setId(sentenceId);
                                        new_sentence.setTale_id(tale_id);
                                        new_sentence.setTale(jsonTale.getJSONObject("tale").getString("title"));
                                        new_sentence.setText(jsonTale.getString("text"));
                                        sentencesList.add(new_sentence);
                                        dao.create(new_sentence);
                                        arrayList.add(new_sentence);
                                        sentences_id.add(jsonTale.getInt("id"));
                                        sentences_texts.add(jsonTale.getString("text"));

                                        calculateReaded(sentencesList);

                                    }
                                }catch (JSONException jse)
                                {
                                    jse.printStackTrace();
                                }
                                return arrayList;
                            }

                        }
                );

                updateSentences.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                JSONArray sentences = response.getJSONArray("sentences");
//                ArrayList<Sentence> sentencesList = new ArrayList<>();
//                sentenceAdapter.clear();
////                int currentIndex = -1;
////                boolean audioDataexists = false;
//                for(int i=0;i<sentences.length();i++)
//                {
//                    JSONObject jsonTale = sentences.getJSONObject(i);
//                    int sentenceId =jsonTale.getInt("id");
//
//                    Sentence new_sentence = new Sentence();
//                    new_sentence.setId(sentenceId);
//                    new_sentence.setTale_id(tale_id);
//                    new_sentence.setTale(jsonTale.getJSONObject("tale").getString("title"));
//                    new_sentence.setText(jsonTale.getString("text"));
//                    sentencesList.add(new_sentence);
//                    sentenceDAO.create(new_sentence);
//                    sentenceAdapter.add(new_sentence);
//                    sentences_id.add(jsonTale.getInt("id"));
//                    sentences_texts.add(jsonTale.getString("text"));
//
//                    calculateReaded(sentencesList);



//                    if(i==0)
//                    {
//                        for(int j=0;j<audioDatas.size();j++)
//                        {
//                            AudioData a = audioDatas.get(j);
//                            if(a.getSentence_id()==sentenceId)
//                            {
//                                currentIndex = j;
//                                break;
//                            }
//                        }
//                        if(currentIndex!=-1)
//                        {
//                            audioDataexists = true;
//                        }
//                    }
//
//                    if(audioDataexists)
//                    {
//
//
//                        if(audioDatas.get(currentIndex).getSentence_id()==sentenceId)
//                        {
//                            if(audioDatas.get(currentIndex).getUploaded()==1)
//                            {
//                                sentenceAdapter.addItemUploaded(true);
//                            }
//                        }
//                        else
//                        {
//                            sentenceAdapter.addItemUploaded(false);
//                        }
//                        for(int k=currentIndex;k<audioDatas.size();k++)
//                        {
//                            if(audioDatas.get(k).getSentence_id()!=sentenceId)
//                            {
//                                currentIndex=k;
//                                break;
//                            }
//                        }
//                    }
//                    else
//                    {
//                        sentenceAdapter.addItemUploaded(false);
//                    }
//                }



                sentenceAdapter.notifyDataSetChanged();

            }
            else
            {
                Toast.makeText(this, response.getString(Config.STATUS_TEXT), Toast.LENGTH_SHORT).show();
            }
        }catch (JSONException jse)
        {
            jse.printStackTrace();
        }
    }

    private void getDataFromServer()
    {
        HttpConnection httpConnection = new HttpConnection(
                SentencesList.this,
                Config.BASE_URL + Config.API_BASE_URL + "/sentences/" + tale_id + "/",
                new ArrayList<HttpParameter>(),
                new ArrayList<HttpParameter>(),
                HttpConnection.GET,
                new OnServerResponse() {
                    @Override
                    public void ConexionExitosa(int codigoRespuesta, String respuesta) {
                        parseSentences(respuesta);
                    }

                    @Override
                    public void ConexionFallida(int codigoRespuesta) {

                    }

                    @Override
                    public void MalaParametrizacion() {

                    }
                }

        );


        httpConnection.initDialog(getResources().getString(R.string.downloading_data), getResources().getString(R.string.may_take_few_seconds));
        httpConnection.setDialogCancelable(false);
        httpConnection.setShowDialog(true);
        httpConnection.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void calculateReaded(ArrayList<Sentence> sentences)
    {
        Log.i(TAG,"SENTENCES SIZE: "+sentences.size());
        Log.i(TAG,"AUDIODATAS SIZE: "+audioDatas.size());
        int currentIndex = -1;
        boolean audioDataexists = false;
        int i=0;


        for(Sentence sentence:sentences)
        {
            int sentenceId = sentence.getId();
            sentences_id.add(sentenceId);
            sentences_texts.add(sentence.getText());
            if(i==0)
            {
                for (int j = 0; j < audioDatas.size(); j++) {
                    AudioData a = audioDatas.get(j);
                    if (a.getSentence_id() == sentenceId) {
                        currentIndex = j;
                        break;
                    }
                }
                if (currentIndex != -1) {
                    audioDataexists = true;
                }

            }

            if(audioDataexists)
            {
                if(audioDatas.get(currentIndex).getSentence_id() == sentenceId)
                {
                    if(audioDatas.get(currentIndex).getUploaded()==1)
                    {
                        sentenceAdapter.addItemUploaded(true);
                    }
                    else
                    {
                        sentenceAdapter.addItemUploaded(false);
                    }
                }
                else
                {
                    sentenceAdapter.addItemUploaded(false);
                }
                for(int k=currentIndex;k<audioDatas.size();k++)
                {
                    if(audioDatas.get(k).getSentence_id()!=sentenceId)
                    {
                        currentIndex=k;
                        break;
                    }
                }
            }
            else
            {
                sentenceAdapter.addItemUploaded(false);
            }
            ++i;
        }
    }

    @Override
    public void onBackPressed() {
        Bundle b = new Bundle();
        b.putInt("author_id",author_id);
        changeActivity(TalesList.class, b);
    }
}

