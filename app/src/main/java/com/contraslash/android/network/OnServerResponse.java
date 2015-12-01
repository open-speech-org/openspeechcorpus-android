package com.contraslash.android.network;

/**
 * @author Mauricio Collazos
 * Interface que debe ser implementada por la clase que llama la petición HTTP
 */
public interface OnServerResponse {

    /**
     * Método llamado cuando se tiene una respuesta efectiva del servidor
     * @param codigoRespuesta Entero que representa el código de respuesta del servidor
     * @param respuesta String con respuesta del servidor
     */
    public void ConexionExitosa(int codigoRespuesta, String respuesta);

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
