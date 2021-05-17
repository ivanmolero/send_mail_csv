package org.imolero.sendmailcsv.modelo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AccesoGeneral implements Serializable {
    private static AccesoGeneral instancia;
    private List<PlantillaCorreo> plantillas;

    private AccesoGeneral() {
        plantillas = new ArrayList<>();
    }

    public static boolean cargaArchivo() {
        boolean salida = false;
        try {
            FileInputStream fis = new FileInputStream("datos.obj");
            ObjectInputStream ois = new ObjectInputStream(fis);
            instancia = (AccesoGeneral) ois.readObject();
            ois.close();
            fis.close();
            salida = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return salida;
    }

    public static boolean guardaArchivo(){
        boolean salida = false;
        try {
            FileOutputStream fos = new FileOutputStream("datos.obj");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(instancia);
            oos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return salida;
    }

    public static AccesoGeneral getInstancia() {
        if (instancia == null) {
            if (!cargaArchivo()) {
                instancia = new AccesoGeneral();
            }
        }
        return instancia;
    }

    public List<PlantillaCorreo> getPlantillas() {
        return plantillas;
    }
}
