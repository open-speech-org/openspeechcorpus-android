package com.contraslash.android.openspeechcorpus.apps.tales.activities;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.contraslash.android.network.HttpConnection;
import com.contraslash.android.network.HttpParameter;
import com.contraslash.android.network.OnServerResponse;
import com.contraslash.android.openspeechcorpus.R;
import com.contraslash.android.openspeechcorpus.apps.tales.adapters.TaleAdapter;
import com.contraslash.android.openspeechcorpus.apps.tales.models.Author;
import com.contraslash.android.openspeechcorpus.apps.tales.models.Sentence;
import com.contraslash.android.openspeechcorpus.apps.tales.models.Tale;
import com.contraslash.android.openspeechcorpus.apps.tales.models.TaleDAO;
import com.contraslash.android.openspeechcorpus.base.BaseActivity;
import com.contraslash.android.openspeechcorpus.config.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TalesList extends BaseActivity {

    Toolbar toolbar;
    ListView talesListView;
    TaleAdapter taleAdapter;
    ArrayList<Tale> tales;

    TaleDAO taleDAO;

    int author_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_tales_list;
    }

    @Override
    protected void mapGUI() {
        toolbar = (Toolbar)findViewById(R.id.tales_list_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        talesListView = (ListView)findViewById(R.id.tales_list_list_view);
        taleAdapter = new TaleAdapter(this, R.layout.element_tale, new ArrayList());
        talesListView.setAdapter(taleAdapter);
    }

    @Override
    protected void loadEvents() {
        taleDAO = new TaleDAO(this);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
        {
            author_id= bundle.getInt("author_id",-1);

            if(author_id>0)
            {

                taleDAO = new TaleDAO(this);
                tales = taleDAO.getTalesByAuthorId(author_id);
                if(tales.isEmpty())
                {
                    Log.i(TAG,"Tales Empty");
                    HttpConnection httpConnection = new HttpConnection(
                            this,
                            Config.BASE_URL + Config.API_BASE_URL + "/tales/" + author_id + "/",
                            new ArrayList<HttpParameter>(),
                            new ArrayList<HttpParameter>(),
                            HttpConnection.GET,
                            new OnServerResponse() {
                                @Override
                                public void ConexionExitosa(int codigoRespuesta, String respuesta) {
                                    parseTales(respuesta);
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
                else
                {
                    Log.i(TAG, "Tales Lis is not empty");
                    taleAdapter.clear();
                    taleAdapter.addAll(tales);
                }

                talesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Bundle b = new Bundle();
                        b.putInt("tale_id",((Tale)(taleAdapter.getItem(position))).getId());
                        b.putInt("author_id",author_id);
                        changeActivity(SentencesList.class, b);
                    }
                });


            }
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
                ArrayList<Tale> authorsList = new ArrayList<>();
                taleAdapter.clear();
                for(int i=0;i<tales.length();i++)
                {
                    JSONObject jsonTale = tales.getJSONObject(i);
                    Tale new_tale = new Tale();
                    new_tale.setId(jsonTale.getInt("id"));
                    new_tale.setTitle(jsonTale.getString("title"));
                    new_tale.setAuthor(jsonTale.getJSONObject("author").getString("name"));
                    new_tale.setAuthor_id(author_id);
                    new_tale.setDescription(jsonTale.getString("description"));
                    new_tale.setTotalVotes(jsonTale.getInt("total_votes"));
                    new_tale.setCalification((float) jsonTale.getDouble("calification"));
                    authorsList.add(new_tale);
                    taleDAO.create(new_tale);
                    taleAdapter.add(new_tale);
                }

                taleAdapter.notifyDataSetChanged();

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
