package com.contraslash.android.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;

/**
 * @author Mauricio Collazos
 * AsyncTask para la petición HTTP
 */
public class HttpConnection extends AsyncTask<String, Void, String>
{



    private static final String TAG = "HttpConnection";

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
    public static final int PUT=2;

    /**
     * Constante que representa una petición DELETE
     */
    public static final int DELETE=3;


    /**
     * String que indica que existieron errores en la conexión
     */
    private static final String CONEXION_FALLIDA="CONEXION_FALLIDA";

    /**
     * String que indica que existieron errores en la conexión
     */
    private static final String SOCKET_TIMEOUT="SOCKET_TIMEOUT";


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
    ArrayList<HttpParameter> httpHeaderParams;

    /**
     * Tipo de petición que se realizará
     */
    int tipo;

    /**
     * Delegado de ejecutará la respuesta del servidor
     */
    OnServerResponse resolutor;

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
     * Tiempo de espera de conexión en milisegundos
     */
    int connectTimeout;

    /**
     * Tiempo de espera de respuesta de conexión en milisegundos
     */
    int responseTimeout;

    /**
     * Constructor principal de la clase
     * @param context Contexto desde el que se llama la asyncTask
     * @param host Host a donde se realiza la petición
     * @param httpParameters lista de httpParameters tipo llave-valor de la petición
     * @param tipo Tipo de peticion GET/POST
     */
    public HttpConnection(Context context, String host, ArrayList<HttpParameter> httpHeaderParams, ArrayList<HttpParameter> httpParameters, int tipo, OnServerResponse resolutor)
    {
        this.context=context;
        this.host=host;
        this.httpParameters = httpParameters;
        this.httpHeaderParams = httpHeaderParams;
        this.tipo=tipo;
        this.resolutor=resolutor;

        connectTimeout = 5000;

        responseTimeout = 5000;

        showDialog=false;
    }


    /**
     * Método que transforma un ArrayList de Parámetros en un string válido de formato www form urlencoded
     * @param httpParameters ArrayList de Parametros
     * @return String con parámetros válidos
     */
    private String transformarParametros(ArrayList<HttpParameter> httpParameters)
    {
        String resultado = "";
        for(HttpParameter httpParameter : httpParameters)
        {
            resultado+= httpParameter.getLlave()+"="+ httpParameter.getValor()+"&";
        }
        if(!resultado.isEmpty())
        {
            resultado=resultado.substring(0,resultado.length()-1);
        }

        return resultado;
    }

    @Override
    protected void onPreExecute() {
        if(showDialog && dialogo!=null)
        {
            dialogo.show();
        }

    }

    @Override
    protected String doInBackground(String... params)
    {
        String resultado="";
        URL url;

        HttpURLConnection connection = null;

        try
        {
            url = new URL(host);
            String parameters="";
            if(httpParameters !=null)
            {

                parameters=transformarParametros(httpParameters);

            }
            if(tipo==GET&&!parameters.isEmpty())
            {
                Log.i(TAG,"Creando httpParameters en URL para el GET");
                Log.i(TAG,"URL: "+host+"?"+parameters);
                url = new URL(host+"?"+parameters);
            }


            Log.i(TAG,"Peticion: "+tipo);
            Log.i(TAG,"Host: "+host);

            Log.i(TAG, "Parametros: " + parameters);

            //System.setProperty("http.keepAlive", "false");

            connection = (HttpURLConnection) url.openConnection();
            //connection.setDoOutput(true);
            //connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(false);
            connection.setConnectTimeout(15000);

            connection.setReadTimeout(15000);

            switch (tipo)
            {
                case GET:
                    Log.i(TAG, "GET");
                    connection.setDoOutput(false);
                    break;
                case POST:
                    Log.i(TAG,"POST");
                    connection.setRequestMethod("POST");
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    break;
                default:
                    return TIPO_PETICION_NO_VALIDO;

            }
            //connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Connection","close");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("charset", "UTF-8");
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(parameters.getBytes().length));

            connection.setUseCaches(false);
            if(httpHeaderParams !=null)
            {
                for(HttpParameter encabezado: httpHeaderParams)
                {
                    connection.setRequestProperty(encabezado.getLlave(),encabezado.getValor());
                    Log.i(TAG,"Encabezado: "+encabezado.getLlave()+": "+encabezado.getValor());
                }
            }

            if(tipo==POST)
            {

                DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
                writer.write(parameters);
                writer.close();
                wr.close();
            }


            this.responseCode = connection.getResponseCode();

            Log.i(TAG,"Codigo de respuesta de la peticion a "+host+" : "+this.responseCode+"");

            if(this.responseCode>=400)
            {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "UTF-8"));
                String line = "";
                Log.i(TAG,"Antes del readline");
                while((line=in.readLine())!=null) {
                    resultado+=line;
                }
                Log.i(TAG,"Error:" + resultado);
                Log.i(TAG, "Antes del Close");
                in.close();

                return CONEXION_FALLIDA;
            }

            BufferedReader in  = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line = "";
            Log.i(TAG, "Antes del readline");
            while((line=in.readLine())!=null) {
                resultado+=line;
            }
            Log.i(TAG, "Antes del Close");

            in.close();


        }catch (SocketTimeoutException ste)
        {
            ste.printStackTrace();
            resultado = SOCKET_TIMEOUT;
        }
        catch (IOException ex ) {
            ex.printStackTrace();
            resultado = CONEXION_FALLIDA;
        }
        finally
        {

            if(connection!=null)
            {
                Log.i(TAG,"Disconnecting connection");
                connection.disconnect();
            }
        }


        return resultado;
    }

    @Override
    protected void onPostExecute(String s)
    {
        Log.i(TAG,"Respuesta: "+s);

        if(s.compareTo(TIPO_PETICION_NO_VALIDO)==0)
        {
            resolutor.MalaParametrizacion();
        }
        else if(s.compareTo(CONEXION_FALLIDA)==0)
        {
            resolutor.ConexionFallida(this.responseCode);
        }
        else if(s.compareTo(SOCKET_TIMEOUT)==0)
        {
            resolutor.ConexionFallida(999);
        }
        else
        {
            resolutor.ConexionExitosa(this.responseCode, s);
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
     */
    public void initDialog()
    {
        dialogo=new ProgressDialog(context);
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
     * Funcion que determina el titulo del dialogo
     * @param dialogTitle String con el titulo del dialogo
     */
    public void setDialogTitle(String dialogTitle) {
        this.dialogo.setTitle( dialogTitle);
    }

    /**
     * Función que determina el titulo del dialogo
     * @param dialogMessage String con el titulo del dialogo
     */
    public void setDialogMessage(String dialogMessage) {
        this.dialogo.setMessage( dialogMessage);
    }

    /**
     * funcion que determina si un dialogo se puede cancelar o no, use con precausión
     * @param cancelable booleano si dialogo es cancelable o no
     */
    public void setDialogCancelable(boolean cancelable)
    {
        this.dialogo.setCancelable(cancelable);
    }


    /**
     * Definimos el tiempo de espera para una respuesta de conexión
     * @param connectTimeout tiempo en milisegundos
     */
    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    /**
     * Definimos el tiempo de espera para una respuesta de conexión
     * @param responseTimeout tiempo en milisegundos
     */
    public void setResponseTimeout(int responseTimeout) {
        this.responseTimeout = responseTimeout;
    }

    /**
     * Obtiene el tiempo de espera de conexion
     * @return tiempo de espera de conexión en milisegundos
     */
    public int getConnectTimeout() {
        return connectTimeout;
    }

    /**
     * Obtiene el tiempo de espera de respuesta
     * @return tiempo de espera de respuesta en milisegundos
     */
    public int getResponseTimeout() {
        return responseTimeout;
    }
}
