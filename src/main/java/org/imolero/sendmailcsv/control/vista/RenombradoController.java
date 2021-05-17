package org.imolero.sendmailcsv.control.vista;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.imolero.sendmailcsv.control.Herramienta;
import org.imolero.sendmailcsv.modelo.PosicionDato;
import org.imolero.sendmailcsv.modelo.RegistroArchivo;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;

/**
 * Controlador de la interfaz renombrado.fxml
 * Divide el archivo pdf y controla el renombrado del archivo dividido
 * usando un archivo excel como base
 */
public class RenombradoController implements Initializable {
    public TextField txtExcel;
    public Button btnExcel;
    public TextField txtEtiqueta;
    public ComboBox<String> cmbPosicion;
    public ComboBox<PosicionDato> cmbColumna;
    public Button btnRenombrar;
    public TableView<PosicionDato> tblExcel;
    public TableColumn<PosicionDato, Long> colExcelNro;
    public TableColumn<PosicionDato, String> colExcelNombre;
    public TableView<RegistroArchivo> tblArchivo;
    public TableColumn<RegistroArchivo, Integer> colArchivoNro;
    public TableColumn<RegistroArchivo, String> colArchivoNombre;
    public Button btnArchivopdf;
    public Spinner<Integer> spnNroPaginas;
    public Button btnDividir;
    public Button btnArchivos;
    public TextField txtArchivos;
    public TextField txtArchivopdf;
    public ComboBox<Sheet> cmbHoja;
    private File archivos;
    private File archivopdf;
    private File excel;
    private FileChooser fc;
    private DirectoryChooser dc;

    /**
     * Inicia los elementos de la interfaz para poder hacerlos funcionales
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fc = new FileChooser();
        dc = new DirectoryChooser();
        spnNroPaginas.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,5,1));
        cmbPosicion.getItems().add("PREFIJO");
        cmbPosicion.getItems().add("SUFIJO");
        colArchivoNro.setCellValueFactory(new PropertyValueFactory<>("id"));
        colArchivoNombre.setCellValueFactory(new PropertyValueFactory<>("nombreArchivo"));
        colExcelNro.setCellValueFactory(new PropertyValueFactory<>("posicion"));
        colExcelNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        Herramienta.conversorComboHoja(cmbHoja);
        Herramienta.conversorComboPosicionDato(cmbColumna);
        cmbHoja.valueProperty().addListener(new ChangeListener<Sheet>() {
            @Override
            public void changed(ObservableValue<? extends Sheet> observableValue, Sheet t1, Sheet t2) {
                cmbColumna.getItems().clear();
                if (t2 != null) {
                    DataFormatter dataFormatter = new DataFormatter();
                    Iterator it = t2.getRow(0).cellIterator();
                    while(it.hasNext()){
                        Cell celda = (Cell)it.next();
                        cmbColumna.getItems().add(new PosicionDato(celda.getColumnIndex(), dataFormatter.formatCellValue(celda), ""));
                    }
                }
            }
        });
        cmbColumna.valueProperty().addListener(new ChangeListener<PosicionDato>() {
            @Override
            public void changed(ObservableValue<? extends PosicionDato> observableValue, PosicionDato t1, PosicionDato t2) {
                tblExcel.getItems().clear();
                if (t2 != null) {
                    Sheet hoja = cmbHoja.getValue();
                    long columna = t2.getPosicion();
                    DataFormatter dataFormatter = new DataFormatter();
                    Iterator it = hoja.rowIterator();
                    while (it.hasNext()) {
                        Row fila = (Row)it.next();
                        if (fila.getRowNum() > 0) {
                            tblExcel.getItems().add(new PosicionDato(fila.getRowNum(), dataFormatter.formatCellValue(fila.getCell((int) columna)), ""));
                        }
                    }
                }

            }
        });
    }

    /**
     * Selecciona y muestra la ruta del archivo pdf a dividir
     * @param actionEvent
     */
    public void archivopdf(ActionEvent actionEvent) {
        archivopdf = null;
        fc.setTitle("Seleccione el archivo pdf");
        archivopdf = fc.showOpenDialog(new Stage());
        if (archivopdf != null) {
            txtArchivopdf.setText(archivopdf.getPath());
        } else {
            txtArchivopdf.clear();
        }
    }

    /**
     * Selecciona la carpeta de destino para la division del
     * archivo pdf
     * @param actionEvent
     */
    public void dividir(ActionEvent actionEvent) {
        dc.setTitle("Elija directorio de destino");
        archivos = null;
        archivos = dc.showDialog(new Stage());
        if (archivos != null) {
            txtArchivos.setText(archivos.getPath());
            Herramienta.dividepdf(spnNroPaginas.getValue(), archivopdf, archivos);
            Herramienta.cargaCarpeta(archivos, tblArchivo);
        } else {
            txtArchivos.clear();
        }
    }

    public void renombrar(ActionEvent actionEvent) {
        List<RegistroArchivo> archivos = tblArchivo.getItems();
        for (RegistroArchivo registroArchivo: archivos) {
            File archivo = registroArchivo.getFile();
            String nuevonombre = tblExcel.getItems().get(archivos.indexOf(registroArchivo)).getNombre().trim().toUpperCase();
            if (cmbPosicion.getValue().equals("PREFIJO")) {
                nuevonombre = txtEtiqueta.getText() + nuevonombre;
            } else {
                nuevonombre = nuevonombre + txtEtiqueta.getText();
            }
            nuevonombre = nuevonombre.trim() + ".pdf";
            try {
                Files.move(archivo.toPath(), archivo.toPath().resolveSibling(nuevonombre));
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR IOEXCEPTION");
                alert.setContentText(e.getMessage());
            }
        }
    }

    public void excel(ActionEvent actionEvent) {
        excel = null;
        excel = fc.showOpenDialog(new Stage());
        if (excel != null) {
            txtExcel.setText(excel.getPath());
            Herramienta.procesarExcel(excel, cmbHoja);
        } else {
            txtExcel.clear();
        }
    }

    public void archivos(ActionEvent actionEvent) {
        dc.setTitle("");
        archivos = null;
        archivos = dc.showDialog(new Stage());
        if (archivos != null) {
            txtArchivos.setText(archivos.getPath());
            Herramienta.cargaCarpeta(archivos, tblArchivo);
        } else {
            txtArchivos.clear();
        }
    }

}
