package org.imolero.sendmailcsv.modelo;

public class EtiquetaCampo {
    private String etiqueta;
    private String campo;
    private long posicion;

    public EtiquetaCampo(String etiqueta, String campo, long posicion) {
        this.etiqueta = etiqueta;
        this.campo = campo;
        this.posicion = posicion;
    }

    public EtiquetaCampo() {
    }

    public long getPosicion() {
        return posicion;
    }

    public void setPosicion(long posicion) {
        this.posicion = posicion;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }
}
