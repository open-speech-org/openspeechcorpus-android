package com.contraslash.android.network;

/**
 * @author Mauricio Collazos
 * Elemento Parámetro tipo llave Valor para una petición HTTP
 */
public class HttpParameter {

    /**
     * Llave de la petición
     */
    String llave;

    /**
     * Valor de la llave
     */
    String valor;

    public HttpParameter(String llave, String valor) {
        this.llave = llave;
        this.valor = valor;
    }

    public String getLlave() {
        return llave;
    }

    public void setLlave(String llave) {
        this.llave = llave;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
