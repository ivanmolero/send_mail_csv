package org.imolero.sendmailcsv.modelo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EnvioCorreo {
    private PosicionDato posicionDato;
    private String estado;
    private List<File> adjuntos;
    private List<String> adjuntoNombres;

    public EnvioCorreo() {
    }

    public EnvioCorreo(PosicionDato posicionDato, String estado) {
        this.posicionDato = posicionDato;
        this.estado = estado;
    }

    public PosicionDato getPosicionDato() {
        return posicionDato;
    }

    public void setPosicionDato(PosicionDato posicionDato) {
        this.posicionDato = posicionDato;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<File> getAdjuntos() {
        if (adjuntos == null){
            adjuntos = new ArrayList<>();
        }
        return adjuntos;
    }

    public void setAdjuntos(List<File> adjuntos) {
        this.adjuntos = adjuntos;
    }

    public List<String> getAdjuntoNombres() {
        if (adjuntoNombres == null) {
            adjuntoNombres = new ArrayList<>();
        }
        return adjuntoNombres;
    }

    public void setAdjuntoNombres(List<String> adjuntoNombres) {
        this.adjuntoNombres = adjuntoNombres;
    }
}
