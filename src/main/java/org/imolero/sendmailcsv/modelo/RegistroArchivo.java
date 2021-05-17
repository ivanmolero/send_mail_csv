package org.imolero.sendmailcsv.modelo;

import java.io.File;

public class RegistroArchivo {
    private int id;
    private File file;
    private String nombreArchivo;

    public RegistroArchivo() {
    }

    public RegistroArchivo(int id, File file) {
        this.id = id;
        this.file = file;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getNombreArchivo() {
        return file.getName();
    }
}
