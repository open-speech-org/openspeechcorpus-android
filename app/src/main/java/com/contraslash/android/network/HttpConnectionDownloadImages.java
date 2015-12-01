package com.contraslash.android.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by ma0 on 8/19/15.
 */
public class HttpConnectionDownloadImages extends AsyncTask<String, Void, Bitmap> {



    private static final String TAG = "HttpConnectionImages";

    /**
     * Constante que representa una petición POST
     */
    public static final int POST=0;

    /**
     * Constante que representa una petición GET
     */
    public static final int GET=1;

    /**
     * Constante que representa una petición PUT
     */
    public static final int PUT=1;

    /**
     * Constante que representa una petición DELETE
     */
    public static final int DELETE=1;


    /**
     * String que indica que existieron errores en la conexión
     */
    private static final String CONEXION_FALLIDA="CONEXION_FALLIDA";

    /**
     * String que indica si el método requerido no está soportado
     */
    private static final String TIPO_PETICION_NO_VALIDO="TIPO_PETICION_NO_VALIDO";

    /**
     * Contexto desde el cual se llama la aplicación
     */
    Context context;

    /**
     * Host al cual se hará la petición
     */
    String host;

    /**
     * Lista de parámetros
     */
    ArrayList<HttpParameter> httpParameters;

    /**
     * Lista de httpHeaderParams
     */
    ArrayList<HttpParameter> encabezados;

    /**
     * Tipo de petición que se realizará
     */
    int tipo;

    /**
     * Delegado de ejecutará la respuesta del servidor
     */
    OnServerResponseImages resolutor;

    /**
     * Bandera que determina si se muestra el dialogo o no, Por defecto False
     */
    boolean showDialog;



    /**
     * Dialogo de espera mientras se realiza el llamado al servidor
     */
    ProgressDialog dialogo;

    /**
     * Código de respuesta del servidor
     */
    int responseCode;

    /**
     * Constructor principal de la clase
     * @param context Contexto desde el que se llama la asyncTask
     * @param host Host a donde se realiza la petición
     * @param httpParameters lista de httpParameters tipo llave-valor de la petición
     * @param tipo Tipo de peticion GET/POST
     */

    public HttpConnectionDownloadImages(Context context, String host, ArrayList<HttpParameter> encabezados, ArrayList<HttpParameter> httpParameters, int tipo, OnServerResponseImages resolutor)
    {
        this.context=context;
        this.host=host;
        this.httpParameters = httpParameters;
        this.encabezados=encabezados;
        this.tipo=tipo;
        this.resolutor=resolutor;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String resultado = "";


        Bitmap img = null;
        try {
            Log.i(TAG, host);
            URL url = new URL(host);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            this.responseCode = conn.getResponseCode();
            InputStream is = conn.getInputStream();

            img = BitmapFactory.decodeStream(is);

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return img;
    }

    @Override
    protected void onPostExecute(Bitmap img) {
        if(img != null)
        {
            resolutor.ConexionExitosa(responseCode,img);
        }
        else
        {
            resolutor.ConexionFallida(responseCode);
        }
    }
}