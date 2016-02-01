package com.contraslash.android.openspeechcorpus.apps.profile.dialogs;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.contraslash.android.network.HttpConnection;
import com.contraslash.android.network.HttpConnectionMultipart;
import com.contraslash.android.network.HttpParameter;
import com.contraslash.android.network.MultipartParameter;
import com.contraslash.android.network.OnServerResponse;
import com.contraslash.android.network.Util;
import com.contraslash.android.openspeechcorpus.R;
import com.contraslash.android.openspeechcorpus.base.BaseActivity;
import com.contraslash.android.openspeechcorpus.base.BaseDialog;
import com.contraslash.android.openspeechcorpus.config.Config;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FillNick extends BaseDialog {

    View view;
    EditText nick;






    public FillNick() {
        super();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) baseActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.fragment_fill_nick,null);
        nick = (EditText)view.findViewById(R.id.dialog_fill_nick_nick);
        builder.setView(view);
        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                uploadName();
            }
        })
                .setNegativeButton(R.string.maybe_later, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        baseActivity.getPreferences().edit().putBoolean(Config.NAME_REQUESTED,true).apply();
                        dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }


    private void uploadName()
    {
        ArrayList<HttpParameter> encabezados = new ArrayList<>();
        ArrayList<HttpParameter> parametros=new ArrayList<>();
        parametros.add(new HttpParameter(Config.ANONYMOUS_USER, baseActivity.getPreferences().getInt(Config.USER_ID, -1)+""));
        parametros.add(new HttpParameter(Config.ANONYMOUS_USER_NAME, nick.getText()+""));
        ArrayList<MultipartParameter> parametroImagens = new ArrayList<>();


        HttpConnectionMultipart cargarImagenes = new HttpConnectionMultipart(
                baseActivity,
                Config.BASE_URL + Config.API_BASE_URL + "/anonymous-user/update/",
                encabezados,
                parametros,
                parametroImagens,
                HttpConnection.POST,
                new OnServerResponse() {


                    @Override
                    public void ConexionExitosa(int codigoRespuesta, String respuesta) {
                        baseActivity.getPreferences().edit().putString(Config.ANONYMOUS_USER_NAME,nick.getText()+"").apply();
                        dismiss();
                    }

                    @Override
                    public void ConexionFallida(int codigoRespuesta) {
                        new Util(baseActivity).mostrarErrores(codigoRespuesta);
                        dismiss();
                    }

                    @Override
                    public void MalaParametrizacion() {
                        Log.i(TAG, "Mala Parametrización");
                    }
                }
        );

        cargarImagenes.setShowDialog(true);

        cargarImagenes.initDialog("Cargando las imagenes", getString(R.string.may_take_few_seconds));

        cargarImagenes.setDialogCancelable(false);

        cargarImagenes.execute();
    }


}
