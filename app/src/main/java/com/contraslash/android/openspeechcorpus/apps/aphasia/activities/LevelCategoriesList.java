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
import com.contraslash.android.openspeechcorpus.apps.aphasia.adapters.LevelAdapter;
import com.contraslash.android.openspeechcorpus.apps.aphasia.adapters.LevelCategoryAdapter;
import com.contraslash.android.openspeechcorpus.apps.aphasia.models.Level;
import com.contraslash.android.openspeechcorpus.apps.aphasia.models.LevelCategory;
import com.contraslash.android.openspeechcorpus.apps.aphasia.models.LevelCategoryDAO;
import com.contraslash.android.openspeechcorpus.apps.aphasia.models.LevelDAO;
import com.contraslash.android.openspeechcorpus.apps.tales.utils.JSONParser;
import com.contraslash.android.openspeechcorpus.apps.tales.utils.UpdateGUIAsync;
import com.contraslash.android.openspeechcorpus.base.BaseActivity;
import com.contraslash.android.openspeechcorpus.config.Config;
import com.contraslash.android.openspeechcorpus.db.BaseDAO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LevelCategoriesList extends BaseActivity {

    Toolbar toolbar;
    ListView levelCategoryListView;

    LevelCategoryAdapter levelCategoryAdapter;
    ArrayList<LevelCategory> levelCategories;

    LevelCategoryDAO levelcategoryDAO;

    int level_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_level_categories_list;
    }

    @Override
    protected void mapGUI() {
        toolbar = (Toolbar)findViewById(R.id.level_categories_list_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        levelCategoryListView = (ListView)findViewById(R.id.level_categories_list_list_view);
        levelCategoryAdapter =  new LevelCategoryAdapter(this, R.layout.element_level_category, new ArrayList());
        levelCategoryListView.setAdapter(levelCategoryAdapter);

    }

    @Override
    protected void loadEvents() {

        level_id = getPreferences().getInt("level_id", 0);

        Bundle b = getIntent().getExtras();
        if(b!=null)
        {
            Log.i(TAG, "There are bundles");
            level_id = b.getInt(
                    "level_id",
                    level_id
            );
            getPreferences().edit().putInt("level_id", level_id).apply();
            Log.i(TAG,"Level id "+level_id);

        }
        else
        {
            Log.i(TAG, "There are NOT bundles");
        }

        Log.i(TAG, "Level ID "+level_id );
        levelcategoryDAO = new LevelCategoryDAO(this);
        levelCategories = levelcategoryDAO.getCategoriesByLevelId(level_id);
        if ( levelCategories.isEmpty() )
        {
            Log.i(TAG,"Levels is empty");
            getLevelsCategoriesFromServer();
        }
        else
        {
            Log.i(TAG,"Levels NO is empty");
            Log.i(TAG,"Size is"+levelCategories.size());
            levelCategoryAdapter.clear();
            levelCategoryAdapter.addAll(levelCategories);
            levelCategoryAdapter.notifyDataSetChanged();
        }

        levelCategoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle b = new Bundle();
                b.putInt("level_id", ((LevelCategory) (levelCategoryAdapter.getItem(position))).getLevel_id());
                b.putInt("category_id", ((LevelCategory) (levelCategoryAdapter.getItem(position))).getId());
                changeActivity(LevelSentenceList.class, b);
            }
        });
    }


    private void getLevelsCategoriesFromServer()
    {
        HttpConnection httpConnection = new HttpConnection(
                this,
                Config.BASE_URL + Config.API_BASE_URL + "/level/"+level_id+"/",
                new ArrayList<HttpParameter>(),
                new ArrayList<HttpParameter>(),
                HttpConnection.GET,
                new OnServerResponse() {
                    @Override
                    public void ConexionExitosa(int codigoRespuesta, String respuesta) {
                        parseCategoriesLevels(respuesta);
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


    private void parseCategoriesLevels(String json)
    {
        UpdateGUIAsync updater = new UpdateGUIAsync(
                levelCategoryAdapter,
                levelcategoryDAO,
                json,
                new JSONParser() {
                    @Override
                    public ArrayList execute(ArrayAdapter adapter, String json, BaseDAO dao) {
                        ArrayList arrayList = new ArrayList();
                        try {
                            JSONArray levels = new JSONArray(json);
                            adapter.clear();
                            for (int i = 0; i < levels.length(); i++) {
                                JSONObject jsonLevelCategory = levels.getJSONObject(i);
                                LevelCategory new_category_level = new LevelCategory();
                                new_category_level.setId(jsonLevelCategory.getInt("id"));
                                new_category_level.setLevel_id(level_id);
                                new_category_level.setTitle(jsonLevelCategory.getString("title"));

                                dao.create(new_category_level);
                                arrayList.add(new_category_level);
                            }
                        }catch (JSONException jse)
                        {
                            jse.printStackTrace();
                        }
                        return arrayList;
                    }
                });

        updater.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        levelCategoryAdapter.notifyDataSetChanged();

    }

}
