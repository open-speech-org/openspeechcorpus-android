package com.contraslash.android.network;

/**
 * Created by ma0 on 8/21/15.
 */
public class MultipartParameter {

    /**
     * Llave de la petición
     */
    String llave;

    /**
     * Valor de la llave
     */
    String rutaImagen;

    /**
     * Tipo de la foto
     */
    String contentType;

    public MultipartParameter(String llave, String rutaImagen, String contentType) {
        this.llave = llave;
        this.rutaImagen = rutaImagen;
        this.contentType = contentType;
    }

    public MultipartParameter(String llave, String rutaImagen) {
        this.llave = llave;
        this.rutaImagen = rutaImagen;
        this.contentType = "image/jpeg";
    }



    public String getLlave() {
        return llave;
    }

    public void setLlave(String llave) {
        this.llave = llave;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
