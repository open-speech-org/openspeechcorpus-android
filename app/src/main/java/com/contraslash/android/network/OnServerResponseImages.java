package com.contraslash.android.network;

import android.graphics.Bitmap;

/**
 * Created by ma0 on 8/19/15.
 */
public interface OnServerResponseImages {

    /**
     * Método llamado cuando se tiene una respuesta efectiva del servidor
     * @param codigoRespuesta Entero que representa el código de respuesta del servidor
     * @param respuesta String con respuesta del servidor
     */
    public void ConexionExitosa(int codigoRespuesta, Bitmap respuesta);

    /**
     * Método llamado cuando no se puede ejecutar una petición
     * @param codigoRespuesta Entero con el código de respuesta del servidor
     */
    public void ConexionFallida(int codigoRespuesta);

    /**
     * Método llamado cuando no se configuró adecuadamente la peticion http
     */
    public void MalaParametrizacion();
}
