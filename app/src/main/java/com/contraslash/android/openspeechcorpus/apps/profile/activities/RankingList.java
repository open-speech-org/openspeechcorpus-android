package com.contraslash.android.openspeechcorpus.apps.profile.activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.contraslash.android.network.HttpConnection;
import com.contraslash.android.network.HttpParameter;
import com.contraslash.android.network.OnServerResponse;
import com.contraslash.android.network.Util;
import com.contraslash.android.openspeechcorpus.R;
import com.contraslash.android.openspeechcorpus.apps.miscellany.models.Command;
import com.contraslash.android.openspeechcorpus.apps.profile.adapters.RankingPositionAdapter;
import com.contraslash.android.openspeechcorpus.apps.profile.models.RankingPosition;
import com.contraslash.android.openspeechcorpus.base.BaseActivity;
import com.contraslash.android.openspeechcorpus.config.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RankingList extends BaseActivity {

    Toolbar toolbar;
    ListView rankingList;

    ArrayList<RankingPosition> positions;
    RankingPositionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_ranking_list;
    }

    @Override
    protected void mapGUI() {
        toolbar = (Toolbar)findViewById(R.id.ranking_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rankingList = (ListView)findViewById(R.id.ranking_list_view);

        positions = new ArrayList<>();

        adapter = new RankingPositionAdapter(this,R.layout.element_ranking_position,positions);
        rankingList.setAdapter(adapter);

        getRankingList();


    }

    @Override
    protected void loadEvents() {

    }

    private void getRankingList()
    {
        String path = "/ranking/";

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
                    }

                    @Override
                    public void ConexionFallida(int codigoRespuesta) {
                        new Util(RankingList.this).mostrarErrores(codigoRespuesta);
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


    private void parseData(String data)
    {
        try{
            JSONArray allRankings = new JSONArray(data);
            for(int i=0;i<allRankings.length();i++)
            {
                JSONObject ranking = allRankings.getJSONObject(i);
                RankingPosition rankingPosition = new RankingPosition(
                        i+1,
                        ranking.getString("user"),
                        ranking.getInt("user_count")
                );
                positions.add(rankingPosition);
            }
            adapter.notifyDataSetChanged();
        }catch (JSONException jse)
        {
            jse.printStackTrace();
        }

    }
}
