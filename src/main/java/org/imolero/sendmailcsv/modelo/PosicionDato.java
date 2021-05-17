package org.imolero.sendmailcsv.modelo;

public class PosicionDato {
    private long posicion;
    private String nombre;
    private String correo;

    public PosicionDato() {
    }

    public PosicionDato(long posicion, String nombre, String correo) {
        this.posicion = posicion;
        this.nombre = nombre;
        this.correo = correo;
    }

    public long getPosicion() {
        return posicion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }
}
