package com.contraslash.android.network;

import android.content.Context;
import android.widget.Toast;

import com.contraslash.android.openspeechcorpus.R;

/**
 * Created by ma0 on 8/21/15.
 */
public class Util {

    Context context;

    public Util(Context context) {
        this.context = context;
    }


    public void mostrarErrores(int codigoRespuesta)
    {
        switch (codigoRespuesta)
        {
            case 0:
                Toast.makeText(
                        context,
                        context.getString(R.string.http_0),
                        Toast.LENGTH_SHORT
                ).show();
                break;
            case 400:
                Toast.makeText(
                        context,
                        context.getString(R.string.http_400),
                        Toast.LENGTH_SHORT
                ).show();
                break;
            case 401:
                Toast.makeText(
                        context,
                        context.getString(R.string.http_401),
                        Toast.LENGTH_SHORT
                ).show();
                break;
            case 500:
                Toast.makeText(
                        context,
                        context.getString(R.string.http_500),
                        Toast.LENGTH_SHORT
                ).show();
                break;
            case 999:
                Toast.makeText(
                        context,
                        context.getString(R.string.http_999),
                        Toast.LENGTH_SHORT
                ).show();
                break;
        }
    }
}
