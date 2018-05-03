package com.contraslash.android.openspeechcorpus.apps.aphasia.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.contraslash.android.network.HttpConnection;
import com.contraslash.android.network.HttpParameter;
import com.contraslash.android.network.OnServerResponse;
import com.contraslash.android.openspeechcorpus.R;
import com.contraslash.android.openspeechcorpus.apps.aphasia.adapters.LevelAdapter;
import com.contraslash.android.openspeechcorpus.apps.aphasia.models.Level;
import com.contraslash.android.openspeechcorpus.apps.aphasia.models.LevelDAO;
import com.contraslash.android.openspeechcorpus.apps.tales.activities.TalesList;
import com.contraslash.android.openspeechcorpus.apps.tales.adapters.AuthorAdapter;
import com.contraslash.android.openspeechcorpus.apps.tales.models.Author;
import com.contraslash.android.openspeechcorpus.apps.tales.models.AuthorDAO;
import com.contraslash.android.openspeechcorpus.apps.tales.models.Tale;
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

public class LevelList extends BaseActivity {

    Toolbar toolbar;
    ListView levelListView;

    LevelAdapter levelAdapter;
    ArrayList<Level> levels;

    LevelDAO levelDAO;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_level_list;
    }

    @Override
    protected void mapGUI() {
        toolbar = (Toolbar)findViewById(R.id.level_list_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        levelListView = (ListView)findViewById(R.id.level_list_list_view);
        levelAdapter =  new LevelAdapter(this, R.layout.element_level, new ArrayList());
        levelListView.setAdapter(levelAdapter);

    }

    @Override
    protected void loadEvents() {
        levelDAO = new LevelDAO(this);
        levels = levelDAO.all();
        if ( levels.isEmpty() )
        {
            Log.i(TAG,"Levels is empty");
            getLevelsFromServer();
        }
        else
        {
            Log.i(TAG,"Levels NO is empty");
            Log.i(TAG,"Size is"+levels.size());
            levelAdapter.clear();
            levelAdapter.addAll(levels);
            levelAdapter.notifyDataSetChanged();
        }



        levelListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle b = new Bundle();
                b.putInt("level_id", ((Level) (levelAdapter.getItem(position))).getId());
                changeActivity(LevelCategoriesList.class, b);
            }
        });

    }


    private void getLevelsFromServer()
    {
        HttpConnection httpConnection = new HttpConnection(
                this,
                Config.BASE_URL + Config.API_BASE_URL + "/levels/",
                new ArrayList<HttpParameter>(),
                new ArrayList<HttpParameter>(),
                HttpConnection.GET,
                new OnServerResponse() {
                    @Override
                    public void ConexionExitosa(int codigoRespuesta, String respuesta) {
                        parseLevels(respuesta);
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


    private void parseLevels(String json)
    {
        UpdateGUIAsync updater = new UpdateGUIAsync(
                levelAdapter,
                levelDAO,
                json,
                new JSONParser() {
                    @Override
                    public ArrayList execute(ArrayAdapter adapter, String json, BaseDAO dao) {
                        ArrayList arrayList = new ArrayList();
                        try {
                            JSONArray levels = new JSONArray(json);
                            adapter.clear();
                            for (int i = 0; i < levels.length(); i++) {
                                JSONObject jsonLevel = levels.getJSONObject(i);
                                Level new_level = new Level();
                                new_level.setId(jsonLevel.getInt("id"));
                                new_level.setTitle(jsonLevel.getString("title"));
                                new_level.setDescription(jsonLevel.getString("description"));

                                dao.create(new_level);
                                arrayList.add(new_level);
                            }
                        }catch (JSONException jse)
                        {
                            jse.printStackTrace();
                        }
                        return arrayList;
                    }
                });

        updater.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        levelAdapter.notifyDataSetChanged();

    }

}
