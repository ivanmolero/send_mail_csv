package org.imolero.sendmailcsv.control;

import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.imolero.sendmailcsv.modelo.AccesoGeneral;
import org.imolero.sendmailcsv.modelo.PlantillaCorreo;
import org.imolero.sendmailcsv.modelo.PosicionDato;
import org.imolero.sendmailcsv.modelo.RegistroArchivo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Herramienta {

    public static String complemento(int nrodocumento, int limite) {
        String salida = String.valueOf(nrodocumento);
        while (limite >= salida.length()) {
            salida = "0" + salida;
        }
        return salida;
    }

    public static void dividepdf(int paginas, File archivopdf, File archivos){
        try {
            PDDocument documentopdf = Loader.loadPDF(archivopdf);
            PDDocument temporal = new PDDocument();
            int nrodocumento = 1;
            for (int i = 0; i < documentopdf.getNumberOfPages(); i++) {
                if ((i+1) % paginas == 0) {
                    temporal.addPage(documentopdf.getPage(i));
                    temporal.save(archivos.getPath() + "/pg-" + Herramienta.complemento(nrodocumento, 4)+ ".pdf");
                    nrodocumento++;
                    temporal = new PDDocument();
                } else {
                    temporal.addPage(documentopdf.getPage(i));
                }
            }
        } catch (IOException e) {
            System.out.println("mensaje: " + e.getMessage());
        }
    }

    public static void cargaCarpeta(File archivos, TableView tblArchivo) {
        List<RegistroArchivo> listaArchivos = new ArrayList<>();
        int orden = 1;
        Stream<File> stream = Arrays.stream(archivos.listFiles()).sorted();
        Object[] lista = stream.toArray();
        for(int i = 0; i < archivos.listFiles().length; i++){
            listaArchivos.add(new RegistroArchivo(orden, (File)lista[i]));
            orden++;
        }
        tblArchivo.getItems().clear();
        tblArchivo.getItems().addAll(listaArchivos);
    }

    public static void procesarExcel(File excel, ComboBox cmbHoja){
        try {
            System.out.printf("archivo excel: " + excel.toString());
            Workbook workbook = WorkbookFactory.create(excel);
            System.out.println("workbook: " + workbook.toString());
            List<Sheet> hojas = new ArrayList<>();
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                hojas.add(workbook.getSheetAt(i));
            }
            cmbHoja.getItems().addAll(hojas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void conversorComboHoja(ComboBox<Sheet> cmbCombo) {
        cmbCombo.setConverter(new StringConverter<Sheet>() {
            @Override
            public String toString(Sheet sheet) {
                String salida = null;
                if (sheet != null) {
                    salida = sheet.getSheetName();
                }
                return salida;
            }

            @Override
            public Sheet fromString(String s) {
                return null;
            }
        });
    }

    public static void conversorComboPosicionDato(ComboBox<PosicionDato> cmbCombo) {
        cmbCombo.setConverter(new StringConverter<PosicionDato>() {
            @Override
            public String toString(PosicionDato posicionDato) {
                String salida = null;
                if (posicionDato != null) {
                    salida = posicionDato.getNombre();
                }
                return salida;
            }

            @Override
            public PosicionDato fromString(String s) {
                return null;
            }
        });
    }

    public static void conversorComboPlantillaCorreo(ComboBox<PlantillaCorreo> cmbCombo){
        cmbCombo.setConverter(new StringConverter<PlantillaCorreo>() {
            @Override
            public String toString(PlantillaCorreo plantillaCorreo) {
                String salida = null;
                if (plantillaCorreo != null) {
                    salida = plantillaCorreo.getNombre();
                }
                return salida;
            }

            @Override
            public PlantillaCorreo fromString(String s) {
                return null;
            }
        });
    }

    public static String textInputDialog(String titulo, String cabecera, String contenido) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(titulo);
        dialog.setHeaderText(cabecera);
        dialog.setContentText(contenido);
        String salida = null;
        Optional<String> resultado = dialog.showAndWait();
        if (resultado.isPresent()) {
            salida = resultado.get();
        }
        return salida;
    }

    public static Boolean confirmationDialog(String titulo, String cabecera, String contenido) {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle(titulo);
        dialog.setHeaderText(cabecera);
        dialog.setContentText(contenido);
        Boolean salida = false;
        Optional<ButtonType> resultado = dialog.showAndWait();
        if (resultado.get() == ButtonType.OK) {
            salida = true;
        }
        return salida;
    }

    public static void cargarComboPlantillaCorreo(ComboBox<PlantillaCorreo> cmbPlantilla) {
        cmbPlantilla.getItems().clear();
        cmbPlantilla.getItems().addAll(AccesoGeneral.getInstancia().getPlantillas());
    }

    public static long contarArchivos(File folder) {
        long salida = 0;
        for (File archivo:folder.listFiles()) {
            salida++;
        }
        return salida;
    }
}
