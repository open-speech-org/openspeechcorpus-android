package com.contraslash.android.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by ma0 on 8/21/15.
 * Requires org.apache.httpcomponents:httpcore:4.3.3+ And org.apache.httpcomponents:httpmime:4.3.6
 */
public class HttpConnectionMultipart extends AsyncTask<Bitmap, Void, String>
{

    private static final String TAG="HTTPCArgarImagen";

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
     * Lista de parámetros Imagen
     */
    ArrayList<MultipartParameter> parametrosImagen;

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
    OnServerResponse resolutor;

    /**
     * Dialogo de espera mientras se realiza el llamado al servidor
     */
    ProgressDialog dialogo;

    /**
     * Bandera que determina si se muestra el dialogo o no, Por defecto False
     */
    boolean showDialog;

    /**
     * Código de respuesta del servidor
     */
    int responseCode;

    Bitmap b;

    /**
     * String que indica que existieron errores en la conexión
     */
    private static final String CONEXION_FALLIDA="CONEXION_FALLIDA";

    /**
     * Constructor principal de la clase
     * @param context Contexto desde el que se llama la asyncTask
     * @param host Host a donde se realiza la petición
     * @param httpParameters lista de httpParameters tipo llave-valor de la petición
     * @param tipo Tipo de peticion GET/POST
     */
    public HttpConnectionMultipart(Context context, String host, ArrayList<HttpParameter> encabezados, ArrayList<HttpParameter> httpParameters, ArrayList<MultipartParameter> parametrosImagen, int tipo, OnServerResponse resolutor)
    {
        this.context=context;
        this.host=host;
        this.httpParameters = httpParameters;
        this.parametrosImagen = parametrosImagen;
        this.encabezados=encabezados;
        this.tipo=tipo;
        this.resolutor=resolutor;



        showDialog=false;
    }
    @Override
    protected void onPreExecute() {
        if(showDialog && dialogo!=null)
        {
            dialogo.show();
        }
    }

    /**
     * Manejador del envío de fotos al servidor
     * @param bitmaps Imagen de perfil
     * @return estado de respuesta del servidor
     */
    protected String doInBackground(Bitmap... bitmaps) {
        String stringResponse="";

        DefaultHttpClient httpclient = new DefaultHttpClient();

        //Construcción y envío de la petición http
        try {
            HttpPost httppost = new HttpPost(
                    host
            );

            Log.i(TAG, "Host:" + host);


            if(encabezados!=null)
            {
                for(HttpParameter header: encabezados)
                {
                    httppost.addHeader(header.getLlave(),header.getValor());
                    Log.i(TAG, "Header"+header.getLlave() + "=" + header.getValor());
                }
            }

            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            for(MultipartParameter imagen: parametrosImagen)
            {

                String rutaFoto = imagen.getRutaImagen();

                File file=new File(rutaFoto);
                Log.i(TAG,"RutaFoto: "+rutaFoto);
                Log.i(TAG,"File Exists: "+file.exists());
                Log.i(TAG, "File Path: " + file.getAbsolutePath());
                entityBuilder.addBinaryBody(
                        imagen.getLlave(),
                        file,
                        ContentType.create(imagen.getContentType()),
                        file.getName()
                );

                Log.i(TAG,"Binary Params: "+ imagen.getLlave()+" - "+imagen.getContentType()+" - "+file.getName());
            }

            for(HttpParameter httpParameter : httpParameters)
            {
                entityBuilder.addTextBody(httpParameter.getLlave(), httpParameter.getValor());
                Log.i(TAG,"HttpParameter: "+ httpParameter.getLlave()+"="+ httpParameter.getValor());
            }

            Log.i(TAG,"Hicimos el Post");
            HttpEntity entity=entityBuilder.build();
            httppost.setEntity(entity);
            HttpResponse response = null;

            try {

                response = httpclient.execute(httppost);
                Log.i(TAG,"Ejecutamos el Post+");
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                Log.i(TAG,"ClientProtocolException");
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                Log.i(TAG,"IOException");
                e.printStackTrace();
            }

            if (response != null)
            {
                //stringResponse=response.getStatusLine().toString();

                responseCode=response.getStatusLine().getStatusCode();
                Log.i(TAG, "response Number " + responseCode);
                stringResponse= EntityUtils.toString(response.getEntity());;

            }
            else
            {
                Log.i(TAG, "response NULL" );
            }


        } catch (IOException e) {
            e.printStackTrace();
            stringResponse = CONEXION_FALLIDA;
        } finally {

        }
        return stringResponse;
    }



    @Override
    protected void onPostExecute(String result)
    {
        Log.i(TAG, result);
        if(result.compareTo(CONEXION_FALLIDA)==0)
        {
            resolutor.ConexionFallida(this.responseCode);
        }
        else
        {
            resolutor.ConexionExitosa(responseCode,result);
        }

        if(showDialog && dialogo!=null)
        {
            if(dialogo.isShowing())
            {
                dialogo.dismiss();
            }
        }
    }


    /**
     * Método para incializar diálogo
     * @param titulo Titulo del dialogo
     * @param mensaje Mensaje del dialog
     */
    public void initDialog(String titulo, String mensaje)
    {
        dialogo=new ProgressDialog(context);
        dialogo.setTitle(titulo);
        dialogo.setMessage(mensaje);
    }

    /**
     * Determina si se muestra el dialogo o no
     * @param showDialog Booleano que determina si se muestra el dialogo o no
     */
    public void setShowDialog(boolean showDialog) {
        this.showDialog = showDialog;
    }

    /**
     * funcion que determina si un dialogo se puede cancelar o no, use con precausión
     * @param cancelable booleano si dialogo es cancelable o no
     */
    public void setDialogCancelable(boolean cancelable)
    {
        this.dialogo.setCancelable(cancelable);
    }

}

