package com.contraslash.android.openspeechcorpus.apps.suggestions.activities;

import android.hardware.Sensor;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.contraslash.android.network.HttpConnection;
import com.contraslash.android.network.HttpParameter;
import com.contraslash.android.network.OnServerResponse;
import com.contraslash.android.network.Util;
import com.contraslash.android.openspeechcorpus.R;
import com.contraslash.android.openspeechcorpus.base.BaseActivity;
import com.contraslash.android.openspeechcorpus.config.Config;

import java.util.ArrayList;

public class SendSuggestion extends BaseActivity {


    Toolbar toolbar;
    ImageButton send;
    EditText text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_send_suggestion;
    }

    @Override
    protected void mapearGUI() {
        toolbar = (Toolbar)findViewById(R.id.send_suggestion_toolbar);
        text = (EditText)findViewById(R.id.send_suggestion_text);
        send = (ImageButton)findViewById(R.id.send_suggestion_send_button);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void cargarEventos()
    {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSuggestion();
            }
        });
    }

    private void sendSuggestion()
    {
        ArrayList<HttpParameter> parameters = new ArrayList<>();
        parameters.add(new HttpParameter(Config.SUGGESTION,text.getText()+""));
        if(getPreferencias().getInt(Config.USER_ID,-1) > 0)
        {
            parameters.add(new HttpParameter(Config.ANONYMOUS_USER,getPreferencias().getInt(Config.USER_ID,-1)+""));
        }
        HttpConnection httpConnection = new HttpConnection(
                this,
                Config.BASE_URL + Config.API_BASE_URL + "/suggestion/upload/",
                null,
                parameters,
                HttpConnection.POST,
                new OnServerResponse() {
                    @Override
                    public void ConexionExitosa(int codigoRespuesta, String respuesta) {
                        Toast.makeText(
                                SendSuggestion.this,
                                getString(R.string.upload_successful),
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                    @Override
                    public void ConexionFallida(int codigoRespuesta) {
                        new Util(SendSuggestion.this).mostrarErrores(codigoRespuesta);
                    }

                    @Override
                    public void MalaParametrizacion() {

                    }
                }
        );

        httpConnection.initDialog(getResources().getString(R.string.uploading_suggestion), getResources().getString(R.string.may_take_few_seconds));
        httpConnection.setDialogCancelable(false);
        httpConnection.setShowDialog(true);
        httpConnection.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }


}
