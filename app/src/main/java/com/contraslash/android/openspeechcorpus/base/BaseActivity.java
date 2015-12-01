package com.contraslash.android.openspeechcorpus.base;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by ma0 on 9/1/15.
 */
public abstract class BaseActivity extends ActionBarActivity {

    protected String TAG;

    private SharedPreferences preferencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        TAG = getLocalClassName();
        preferencias = PreferenceManager.getDefaultSharedPreferences(this);

        mapearGUI();

        cargarEventos();
    }

    protected abstract int getLayoutResourceId();

    public SharedPreferences getPreferencias() {
        return preferencias;
    }

    protected abstract void mapearGUI();


    protected abstract void cargarEventos();

    public void cambiarDeActividad(Class destino)
    {
        Intent cambioDeActividad = new Intent(this, destino);
        startActivity(cambioDeActividad);
    }

    public void cambiarDeActividad(Class destino, Bundle extras)
    {
        Intent cambioDeActividad = new Intent(this, destino);
        cambioDeActividad.putExtras(extras);
        startActivity(cambioDeActividad);
    }
}
