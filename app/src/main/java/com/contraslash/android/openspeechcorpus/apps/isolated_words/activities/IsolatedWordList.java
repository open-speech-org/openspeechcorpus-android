package com.contraslash.android.openspeechcorpus.apps.isolated_words.activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.contraslash.android.network.HttpConnection;
import com.contraslash.android.network.HttpParameter;
import com.contraslash.android.network.OnServerResponse;
import com.contraslash.android.openspeechcorpus.R;
import com.contraslash.android.openspeechcorpus.apps.aphasia.activities.UploadWordAudioData;
import com.contraslash.android.openspeechcorpus.apps.aphasia.adapters.LevelSentenceAdapter;
import com.contraslash.android.openspeechcorpus.apps.aphasia.models.LevelSentence;
import com.contraslash.android.openspeechcorpus.apps.aphasia.models.LevelSentenceDAO;
import com.contraslash.android.openspeechcorpus.apps.isolated_words.adapters.IsolatedWordAdapter;
import com.contraslash.android.openspeechcorpus.apps.isolated_words.models.IsolatedWord;
import com.contraslash.android.openspeechcorpus.apps.isolated_words.models.IsolatedWordDAO;
import com.contraslash.android.openspeechcorpus.apps.tales.utils.JSONParser;
import com.contraslash.android.openspeechcorpus.apps.tales.utils.UpdateGUIAsync;
import com.contraslash.android.openspeechcorpus.base.BaseActivity;
import com.contraslash.android.openspeechcorpus.config.Config;
import com.contraslash.android.openspeechcorpus.db.BaseDAO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class IsolatedWordList extends BaseActivity {

    Toolbar toolbar;
    ListView isolatedWordListView;

    IsolatedWordAdapter isolatedWordAdapter;
    ArrayList<IsolatedWord> isolatedWords;

    IsolatedWordDAO isolatedWordDAO;

    int category_id;

    ArrayList<Integer> words_ids;
    ArrayList<String> words_texts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_isolated_word_list;
    }

    @Override
    protected void mapGUI() {
        toolbar = (Toolbar)findViewById(R.id.isolated_words_list_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        isolatedWordListView = findViewById(R.id.isolated_words_list_list_view);
        isolatedWordAdapter =  new IsolatedWordAdapter(this, R.layout.element_isolated_word, new ArrayList());
        isolatedWordListView.setAdapter(isolatedWordAdapter);

        words_ids = new ArrayList<>();
        words_texts = new ArrayList<>();

    }

    @Override
    protected void loadEvents() {

        Bundle b = getIntent().getExtras();

        category_id = getPreferences().getInt("category_id", 0);

        if(b!=null)
        {
            category_id = b.getInt(
                "category_id",
                category_id
            );
            getPreferences().edit().putInt("category_id", category_id).apply();
            Log.i(TAG, "category_id"+category_id);


        }
        else
        {
            Log.i(TAG, "There are NOT bundles");
        }

        Log.i(TAG,  " Category id " + category_id);

        isolatedWordDAO = new IsolatedWordDAO(this);
        isolatedWords = isolatedWordDAO.getWordsByCategoryId(category_id);

        if ( isolatedWords.isEmpty() )
        {
            Log.i(TAG,"Words is empty");
            getIsolatedWordsFromServer();
        }
        else
        {
            Log.i(TAG,"Levels NO is empty");
            Log.i(TAG,"Size is"+isolatedWords.size());
            isolatedWordAdapter.clear();
            isolatedWordAdapter.addAll(isolatedWords);
            isolatedWordAdapter.notifyDataSetChanged();
            for(IsolatedWord iw: isolatedWords)
            {
                words_ids.add(iw.get_id());
                words_texts.add(iw.getText());
                Log.i(TAG, "id: "+iw.get_id());
                Log.i(TAG, "uploaded: "+iw.getUploaded());
            }
        }


        isolatedWordListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle b = new Bundle();
                IsolatedWord word = (IsolatedWord) isolatedWordAdapter.getItem(position);
                b.putInt("sentence_id", word.get_id());
                b.putString("text", word.getText());
                b.putIntegerArrayList("sentences_ids", words_ids);
                b.putStringArrayList("sentences_texts", words_texts);
                Log.i(TAG, "Size of sentences_ids: "+words_ids.size() + "Size of sentences_texts: "+words_texts.size());
                changeActivity(UploadIsolatedWordAudioData.class, b);
            }
        });
    }



    private void getIsolatedWordsFromServer()
    {
        HttpConnection httpConnection = new HttpConnection(
                this,
                Config.BASE_URL + Config.API_BASE_URL + "/isolated-words/"+category_id,
                new ArrayList<HttpParameter>(),
                new ArrayList<HttpParameter>(),
                HttpConnection.GET,
                new OnServerResponse() {
                    @Override
                    public void ConexionExitosa(int codigoRespuesta, String respuesta) {
                        parseIsolatedWords(respuesta);
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


    private void parseIsolatedWords(String json)
    {
        UpdateGUIAsync updater = new UpdateGUIAsync(
                isolatedWordAdapter,
                isolatedWordDAO,
                json,
                new JSONParser() {
                    @Override
                    public ArrayList execute(ArrayAdapter adapter, String json, BaseDAO dao) {
                        ArrayList arrayList = new ArrayList();
                        try {
                            JSONArray words = new JSONArray(json);
                            adapter.clear();
                            for (int i = 0; i < words.length(); i++) {
                                JSONObject jsonWord = words.getJSONObject(i);
                                IsolatedWord new_word = new IsolatedWord();
                                new_word.setId(jsonWord.getInt("id"));
                                new_word.set_category_id(category_id);
                                new_word.setText(jsonWord.getString("text"));
                                Log.i("Isolated Word","Adding new sentence with id " + new_word.getId());
                                Log.i("Isolated Word","And text" + new_word.getText());

                                dao.create(new_word);
                                arrayList.add(new_word);
                                words_ids.add(new_word.get_id());
                                words_texts.add(new_word.getText());
                            }
                        }catch (JSONException jse)
                        {
                            jse.printStackTrace();
                        }
                        return arrayList;
                    }
                });

        updater.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        isolatedWordAdapter.notifyDataSetChanged();

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
        changeActivity(CategoryList.class, b);
    }
}
