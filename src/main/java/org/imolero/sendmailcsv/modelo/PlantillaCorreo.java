package org.imolero.sendmailcsv.modelo;

import java.io.Serializable;

public class PlantillaCorreo implements Serializable {
    private String nombre;
    private String asunto;
    private String contenido;

    public PlantillaCorreo() {
    }

    public PlantillaCorreo(String nombre, String asunto, String contenido) {
        this.nombre = nombre;
        this.asunto = asunto;
        this.contenido = contenido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
}
