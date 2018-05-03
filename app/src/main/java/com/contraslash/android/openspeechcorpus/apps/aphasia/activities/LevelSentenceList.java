package com.contraslash.android.openspeechcorpus.apps.aphasia.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.contraslash.android.network.HttpConnection;
import com.contraslash.android.network.HttpParameter;
import com.contraslash.android.network.OnServerResponse;
import com.contraslash.android.openspeechcorpus.R;
import com.contraslash.android.openspeechcorpus.apps.aphasia.adapters.LevelCategoryAdapter;
import com.contraslash.android.openspeechcorpus.apps.aphasia.adapters.LevelSentenceAdapter;
import com.contraslash.android.openspeechcorpus.apps.aphasia.models.LevelCategory;
import com.contraslash.android.openspeechcorpus.apps.aphasia.models.LevelCategoryDAO;
import com.contraslash.android.openspeechcorpus.apps.aphasia.models.LevelSentence;
import com.contraslash.android.openspeechcorpus.apps.aphasia.models.LevelSentenceDAO;
import com.contraslash.android.openspeechcorpus.apps.tales.utils.JSONParser;
import com.contraslash.android.openspeechcorpus.apps.tales.utils.UpdateGUIAsync;
import com.contraslash.android.openspeechcorpus.base.BaseActivity;
import com.contraslash.android.openspeechcorpus.config.Config;
import com.contraslash.android.openspeechcorpus.db.BaseDAO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LevelSentenceList extends BaseActivity {


    Toolbar toolbar;
    ListView levelSentenceListView;

    LevelSentenceAdapter levelSentenceAdapter;
    ArrayList<LevelSentence> levelSentences;

    LevelSentenceDAO levelSentenceDAO;

    int level_id;
    int level_category_id;

    ArrayList<Integer> sentences_ids;
    ArrayList<String> sentences_texts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_level_sentence_list;
    }

    @Override
    protected void mapGUI() {
        toolbar = (Toolbar)findViewById(R.id.level_sentences_list_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        levelSentenceListView = (ListView)findViewById(R.id.level_sentences_list_list_view);
        levelSentenceAdapter =  new LevelSentenceAdapter(this, R.layout.element_level_sentence, new ArrayList());
        levelSentenceListView.setAdapter(levelSentenceAdapter);

        sentences_ids = new ArrayList<>();
        sentences_texts = new ArrayList<>();

    }

    @Override
    protected void loadEvents() {

        Bundle b = getIntent().getExtras();

        level_category_id = getPreferences().getInt("category_id", 0);
        level_id = getPreferences().getInt("level_id", 0);

        if(b!=null)
        {
            Log.i(TAG, "There are bundles");
            level_id = b.getInt(
                    "level_id",
                    level_id
            );
            getPreferences().edit().putInt("level_id", level_id).apply();
            level_category_id = b.getInt(
                    "category_id",
                    level_category_id
            );
            getPreferences().edit().putInt("category_id", level_category_id).apply();
            Log.i(TAG,"Level id "+level_id + "category_id"+level_category_id);


        }
        else
        {
            Log.i(TAG, "There are NOT bundles");
        }

        Log.i(TAG, "Level ID "+level_id  + " Category id "+level_category_id);

        levelSentenceDAO = new LevelSentenceDAO(this);
        levelSentences = levelSentenceDAO.getSentencesByLevelCategoryId(level_category_id);

        if ( levelSentences.isEmpty() )
        {
            Log.i(TAG,"Levels is empty");
            getLevelSentencesFromServer();
        }
        else
        {
            Log.i(TAG,"Levels NO is empty");
            Log.i(TAG,"Size is"+levelSentences.size());
            levelSentenceAdapter.clear();
            levelSentenceAdapter.addAll(levelSentences);
            levelSentenceAdapter.notifyDataSetChanged();
            for(LevelSentence ls: levelSentences)
            {
                sentences_ids.add(ls.getId());
                sentences_texts.add(ls.getText());
            }
        }


        levelSentenceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle b = new Bundle();
                LevelSentence sentence = (LevelSentence) levelSentenceAdapter.getItem(position);
                b.putInt("sentence_id", sentence.getId());
                b.putString("text", sentence.getText());
                b.putIntegerArrayList("sentences_ids", sentences_ids);
                b.putStringArrayList("sentences_texts", sentences_texts);
                Log.i(TAG, "Size of sentences_ids: "+sentences_ids.size() + "Size of sentences_texts: "+sentences_texts.size());
                changeActivity(UploadWordAudioData.class, b);
            }
        });
    }


    private void getLevelSentencesFromServer()
    {
        HttpConnection httpConnection = new HttpConnection(
                this,
                Config.BASE_URL + Config.API_BASE_URL + "/level/"+level_id+"/category/"+level_category_id+"",
                new ArrayList<HttpParameter>(),
                new ArrayList<HttpParameter>(),
                HttpConnection.GET,
                new OnServerResponse() {
                    @Override
                    public void ConexionExitosa(int codigoRespuesta, String respuesta) {
                        parseSentenceLevels(respuesta);
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


    private void parseSentenceLevels(String json)
    {
        UpdateGUIAsync updater = new UpdateGUIAsync(
                levelSentenceAdapter,
                levelSentenceDAO,
                json,
                new JSONParser() {
                    @Override
                    public ArrayList execute(ArrayAdapter adapter, String json, BaseDAO dao) {
                        ArrayList arrayList = new ArrayList();
                        try {
                            JSONArray sentences = new JSONArray(json);
                            adapter.clear();
                            for (int i = 0; i < sentences.length(); i++) {
                                JSONObject jsonLevelSentence = sentences.getJSONObject(i);
                                LevelSentence new_level_sentence = new LevelSentence();
                                new_level_sentence.setId(jsonLevelSentence.getInt("id"));
                                new_level_sentence.setLevel_category_id(level_category_id);
                                new_level_sentence.setText(jsonLevelSentence.getString("text"));

                                dao.create(new_level_sentence);
                                arrayList.add(new_level_sentence);
                                sentences_ids.add(new_level_sentence.getId());
                                sentences_texts.add(new_level_sentence.getText());
                            }
                        }catch (JSONException jse)
                        {
                            jse.printStackTrace();
                        }
                        return arrayList;
                    }
                });

        updater.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        levelSentenceAdapter.notifyDataSetChanged();

    }
}
