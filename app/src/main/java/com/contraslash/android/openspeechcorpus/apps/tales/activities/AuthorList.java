package com.contraslash.android.openspeechcorpus.apps.tales.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.contraslash.android.common.DateUtils;
import com.contraslash.android.network.HttpConnection;
import com.contraslash.android.network.HttpParameter;
import com.contraslash.android.network.OnServerResponse;
import com.contraslash.android.openspeechcorpus.R;
import com.contraslash.android.openspeechcorpus.apps.news.models.New;
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

public class AuthorList extends BaseActivity {

    Toolbar toolbar;
    ListView authorListView;

    AuthorAdapter authorAdapter;
    ArrayList<Author> authors;

    AuthorDAO authorDAO;

    ArrayList<Tale> tales;

    TaleDAO taleDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_author_list;
    }

    @Override
    protected void mapGUI() {
        toolbar = (Toolbar)findViewById(R.id.author_list_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        authorListView = (ListView)findViewById(R.id.author_list_list_view);
        authorAdapter =  new AuthorAdapter(this, R.layout.element_author, new ArrayList());
        authorListView.setAdapter(authorAdapter);

        taleDAO = new TaleDAO(this);
        tales = new ArrayList<>();
    }

    @Override
    protected void loadEvents() {
        authorDAO = new AuthorDAO(this);
        authors =  authorDAO.all();
        if(authors.isEmpty())
        {
            Log.i(TAG,"Authors is empty");
            getAuthorsFromServer(0);
        }
        else
        {
            Log.i(TAG,"Authors NO is empty");
            authorAdapter.clear();
            authorAdapter.addAll(authors);
        }

        authorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle b = new Bundle();
                b.putInt("author_id", ((Author) (authorAdapter.getItem(position))).getId());
                changeActivity(TalesList.class, b);
            }
        });


        if (DateUtils.isAfterLastUpdate(getPreferences().getString(Config.TALES_LAST_UPDATE,"2016-01-01")))
        {
            tales = taleDAO.all();
            int maxNewId = -1;
            for(Tale newObject:tales)
            {
                if(newObject.getId()>maxNewId)
                {
                    maxNewId = newObject.getId();
                }
            }
            Log.i(TAG,"DATE AFTER LAST UPDATE");
            getTalesFromServer(maxNewId);
            getPreferences().edit().putString(Config.TALES_LAST_UPDATE, DateUtils.getSimpleTodayString()).apply();
        }
        else
        {
            Log.i(TAG,"DATE NOT AFTER LAST UPDATE");
        }
    }

    private void getAuthorsFromServer(int offset)
    {
        HttpConnection httpConnection = new HttpConnection(
                this,
                Config.BASE_URL + Config.API_BASE_URL + "/authors/?offset="+offset,
                new ArrayList<HttpParameter>(),
                new ArrayList<HttpParameter>(),
                HttpConnection.GET,
                new OnServerResponse() {
                    @Override
                    public void ConexionExitosa(int codigoRespuesta, String respuesta) {
                        parseAuthors(respuesta);
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

    private void getTalesFromServer(int offset)
    {
        HttpConnection httpConnection = new HttpConnection(
                this,
                Config.BASE_URL + Config.API_BASE_URL + "/tales/?offset="+offset,
                new ArrayList<HttpParameter>(),
                new ArrayList<HttpParameter>(),
                HttpConnection.GET,
                new OnServerResponse() {
                    @Override
                    public void ConexionExitosa(int codigoRespuesta, String respuesta) {

                        if(respuesta.compareTo("[]")!=0)
                        {
                            parseTales(respuesta);

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

    private void parseAuthors(String json)
    {
        try
        {
            JSONObject response= new JSONObject(json);
            if(response.getInt(Config.ERROR_TEXT)==0)
            {
                UpdateGUIAsync updater = new UpdateGUIAsync(
                        authorAdapter,
                        authorDAO,
                        response.getString("authors"),
                        new JSONParser() {
                            @Override
                            public ArrayList execute(ArrayAdapter adapter, String json, BaseDAO dao) {
                                ArrayList arrayList = new ArrayList();
                                try {
                                    JSONArray authors = new JSONArray(json);
                                    adapter.clear();
                                    for (int i = 0; i < authors.length(); i++) {
                                        JSONObject jsonAuthor = authors.getJSONObject(i);
                                        Author new_author = new Author();
                                        new_author.setId(jsonAuthor.getInt("id"));
                                        new_author.setName(jsonAuthor.getString("name"));
                                        new_author.setBiography(jsonAuthor.getString("biography"));
                                        new_author.setBirth(jsonAuthor.getString("birth"));
                                        new_author.setDeath(jsonAuthor.getString("death"));
                                        dao.create(new_author);
                                        arrayList.add(new_author);
                                    }
                                }catch (JSONException jse)
                                {
                                    jse.printStackTrace();
                                }
                                return arrayList;
                            }
                        });

                updater.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                JSONArray authors = response.getJSONArray("authors");
//                ArrayList<Author> authorsList = new ArrayList<>();
//                authorAdapter.clear();
//                for(int i=0;i<authors.length();i++)
//                {
//                    JSONObject jsonAuthor = authors.getJSONObject(i);
//                    Author new_author = new Author();
//                    new_author.setId(jsonAuthor.getInt("id"));
//                    new_author.setName(jsonAuthor.getString("name"));
//                    new_author.setBiography(jsonAuthor.getString("biography"));
//                    new_author.setBirth(jsonAuthor.getString("birth"));
//                    new_author.setDeath(jsonAuthor.getString("death"));
//                    authorsList.add(new_author);
//                    authorDAO.create(new_author);
//                    authorAdapter.add(new_author);
//                }
                authorAdapter.notifyDataSetChanged();

            }
            else
            {
                Toast.makeText(this, response.getString(Config.STATUS_TEXT),Toast.LENGTH_SHORT).show();
            }
        }catch (JSONException jse)
        {
            jse.printStackTrace();
        }
    }

    private void parseTales(String json)
    {
        try
        {
            JSONObject response= new JSONObject(json);
            if(response.getInt(Config.ERROR_TEXT)==0)
            {
                JSONArray tales = response.getJSONArray("tales");
                if(tales.length()>0)
                {
                    int maxNewId = -1;
                    for(Author newObject:authors)
                    {
                        if(newObject.getId()>maxNewId)
                        {
                            maxNewId = newObject.getId();
                        }
                    }
                    getAuthorsFromServer(maxNewId);
                }
                UpdateGUIAsync talesUpdater = new UpdateGUIAsync(null,
                        taleDAO,
                        response.getString("tales"),
                        new JSONParser() {
                            @Override
                            public ArrayList execute(ArrayAdapter adapter, String json, BaseDAO dao) {
                                try {
                                    JSONArray tales = new JSONArray(json);
                                    for (int i = 0; i < tales.length(); i++) {
                                        JSONObject jsonTale = tales.getJSONObject(i);
                                        Tale new_tale = new Tale();
                                        new_tale.setId(jsonTale.getInt("id"));
                                        new_tale.setTitle(jsonTale.getString("title"));
                                        new_tale.setAuthor(jsonTale.getJSONObject("author").getString("name"));
                                        new_tale.setAuthor_id(jsonTale.getJSONObject("author").getInt("id"));
                                        new_tale.setDescription(jsonTale.getString("description"));
                                        new_tale.setTotalVotes(jsonTale.getInt("total_votes"));
                                        new_tale.setCalification((float) jsonTale.getDouble("calification"));
                                        dao.create(new_tale);
                                    }
                                }catch (JSONException jse)
                                {
                                    jse.printStackTrace();
                                }

                                return null;
                            }

                        }
                );

                talesUpdater.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                for(int i=0;i<tales.length();i++)
//                {
//                    JSONObject jsonTale = tales.getJSONObject(i);
//                    Tale new_tale = new Tale();
//                    new_tale.setId(jsonTale.getInt("id"));
//                    new_tale.setTitle(jsonTale.getString("title"));
//                    new_tale.setAuthor(jsonTale.getJSONObject("author").getString("name"));
//                    new_tale.setAuthor_id(jsonTale.getJSONObject("author").getInt("id"));
//                    new_tale.setDescription(jsonTale.getString("description"));
//                    new_tale.setTotalVotes(jsonTale.getInt("total_votes"));
//                    new_tale.setCalification((float) jsonTale.getDouble("calification"));
//                    taleDAO.create(new_tale);
//                }
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


}
