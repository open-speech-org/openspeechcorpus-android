package com.contraslash.android.openspeechcorpus.apps.isolated_words.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.contraslash.android.openspeechcorpus.apps.aphasia.activities.LevelCategoriesList;
import com.contraslash.android.openspeechcorpus.apps.aphasia.activities.LevelList;
import com.contraslash.android.openspeechcorpus.apps.aphasia.adapters.LevelAdapter;
import com.contraslash.android.openspeechcorpus.apps.aphasia.models.Level;
import com.contraslash.android.openspeechcorpus.apps.aphasia.models.LevelDAO;
import com.contraslash.android.openspeechcorpus.apps.core.activities.UploadAudioData;
import com.contraslash.android.openspeechcorpus.apps.isolated_words.adapters.CategoryAdapter;
import com.contraslash.android.openspeechcorpus.apps.isolated_words.models.Category;
import com.contraslash.android.openspeechcorpus.apps.isolated_words.models.CategoryDAO;
import com.contraslash.android.openspeechcorpus.apps.tales.utils.JSONParser;
import com.contraslash.android.openspeechcorpus.apps.tales.utils.UpdateGUIAsync;
import com.contraslash.android.openspeechcorpus.base.BaseActivity;
import com.contraslash.android.openspeechcorpus.config.Config;
import com.contraslash.android.openspeechcorpus.db.BaseDAO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CategoryList extends BaseActivity {

    Toolbar toolbar;
    SwipeRefreshLayout swipeRefreshLayout;
    ListView cateogiresListView;

    CategoryAdapter categoryAdapter;
    ArrayList<Category> categories;

    CategoryDAO categoryDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_category_list;
    }

    @Override
    protected void mapGUI() {
        toolbar = findViewById(R.id.level_list_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.category_list_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)
        );


        cateogiresListView = (ListView)findViewById(R.id.category_list_list_view);
        categoryAdapter =  new CategoryAdapter(this, R.layout.element_isolated_category, new ArrayList());
        cateogiresListView.setAdapter(categoryAdapter);

    }

    @Override
    protected void loadEvents() {

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

//                getLevelsFromServer();
                CategoryList.this.swipeRefreshLayout.setRefreshing(false);
            }
        });
        categoryDAO = new CategoryDAO(this);
        categories = categoryDAO.all();
        if ( categories.isEmpty() )
        {
            Log.i(TAG,"Levels is empty");
            getLevelsFromServer();
        }
        else
        {
            Log.i(TAG,"Levels NO is empty");
            Log.i(TAG,"Size is"+categories.size());
            categoryAdapter.clear();
            Collections.sort(categories, new Comparator<Category>() {
                @Override
                public int compare(Category s1, Category s2) {
                    return s1.getTitle().compareToIgnoreCase(s2.getTitle());
                }
            });
            categoryAdapter.addAll(categories);
            categoryAdapter.notifyDataSetChanged();
        }



        cateogiresListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle b = new Bundle();
                b.putInt("category_id", ((Category) (categoryAdapter.getItem(position))).getId());
                changeActivity(IsolatedWordList.class, b);
            }
        });

    }


    private void getLevelsFromServer()
    {
        HttpConnection httpConnection = new HttpConnection(
                this,
                Config.BASE_URL + Config.API_BASE_URL + "/isolated-words/categories/",
                new ArrayList<HttpParameter>(),
                new ArrayList<HttpParameter>(),
                HttpConnection.GET,
                new OnServerResponse() {
                    @Override
                    public void ConexionExitosa(int codigoRespuesta, String respuesta) {
                        parseCategories(respuesta);
                        if (CategoryList.this.swipeRefreshLayout != null)
                        {
                            if (CategoryList.this.swipeRefreshLayout.isRefreshing())
                            {
                                CategoryList.this.swipeRefreshLayout.setRefreshing(false);
                            }
                        }
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


    private void parseCategories(String json)
    {
        UpdateGUIAsync updater = new UpdateGUIAsync(
                categoryAdapter,
                categoryDAO,
                json,
                new JSONParser() {
                    @Override
                    public ArrayList execute(ArrayAdapter adapter, String json, BaseDAO dao) {
                        ArrayList arrayList = new ArrayList();
                        try {
                            JSONArray categories = new JSONArray(json);
                            adapter.clear();
                            for (int i = 0; i < categories.length(); i++) {
                                JSONObject categoriesLevel = categories.getJSONObject(i);
                                Category new_category = new Category();
                                new_category.setId(categoriesLevel.getInt("id"));
                                new_category.setTitle(categoriesLevel.getString("title"));
                                new_category.setDescription(categoriesLevel.getString("description"));

                                dao.create(new_category);
                                arrayList.add(new_category);
                            }
                        }catch (JSONException jse)
                        {
                            jse.printStackTrace();
                        }
                        Collections.sort(arrayList, new Comparator<Category>() {
                            @Override
                            public int compare(Category s1, Category s2) {
                                return s1.getTitle().compareToIgnoreCase(s2.getTitle());
                            }
                        });
                        return arrayList;
                    }
                });

        updater.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        categoryAdapter.notifyDataSetChanged();

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
        changeActivity(UploadAudioData.class, b);
    }

}
