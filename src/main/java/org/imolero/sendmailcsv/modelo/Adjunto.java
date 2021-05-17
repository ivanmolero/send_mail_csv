package org.imolero.sendmailcsv.modelo;

import java.io.File;

public class Adjunto {
    private long orden;
    private File ruta;
    private long cantidad;

    public Adjunto() {
    }

    public Adjunto(long orden, File ruta, long cantidad) {
        this.orden = orden;
        this.ruta = ruta;
        this.cantidad = cantidad;
    }

    public long getOrden() {
        return orden;
    }

    public void setOrden(long orden) {
        this.orden = orden;
    }

    public File getRuta() {
        return ruta;
    }

    public void setRuta(File ruta) {
        this.ruta = ruta;
    }

    public long getCantidad() {
        return cantidad;
    }

    public void setCantidad(long cantidad) {
        this.cantidad = cantidad;
    }
}
