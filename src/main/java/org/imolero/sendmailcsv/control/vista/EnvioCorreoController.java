package org.imolero.sendmailcsv.control.vista;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.imolero.sendmailcsv.control.Herramienta;
import org.imolero.sendmailcsv.modelo.*;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class EnvioCorreoController implements Initializable {
    public PasswordField txtContrasena;
    public TextField txtCorreo;
    public Button btnExcel;
    public TextField txtExcel;
    public TableView<PosicionDato> tblExcel;
    public TableColumn<PosicionDato, Long> colExcelId;
    public TableColumn<PosicionDato, String> colExcelClave;
    public TableColumn<PosicionDato, String> colExcelEmail;
    public ComboBox<Sheet> cmbHoja;
    public ComboBox<PosicionDato> cmbClave;
    public ComboBox<PosicionDato> cmbCorreo;
    public ComboBox<PlantillaCorreo> cmbPlantilla;
    public Button btnNuevoPlantilla;
    public Button btnEliminarPlantilla;
    public TextField txtPlantillaAsunto;
    public TextArea txtPlantillaMensaje;
    public TableView<Adjunto> tblAdjuntos;
    public TableColumn<Adjunto, Long> colAdjuntoId;
    public TableColumn<Adjunto, String> colAdjuntoRuta;
    public TableColumn<Adjunto, Long> colAdjuntoArchivos;
    public Button btnAgregaAdjunto;
    public Button btnEliminaAdjunto;
    public Button btnCargarDatos;
    public Button btnEnviarCorreos;
    public TableView<EnvioCorreo> tblCorreo;
    public TableColumn<EnvioCorreo, String> colCorreoId;
    public TableColumn<EnvioCorreo, String> colCorreoEstado;
    public TableColumn<EnvioCorreo, String> colCorreoEmail;
    public TableColumn<EnvioCorreo, String> colCorreoAdjuntos;
    public Button btnCargarExcel;
    public ComboBox<PosicionDato> cmbColumnaPlantilla;
    public Button btnAgregarColumna;
    public Button btnRemoverColumna;
    public TableView<EtiquetaCampo> tblColumnas;
    public TableColumn<EtiquetaCampo, String> colIdColumnaPlantilla;
    public TableColumn<EtiquetaCampo, String> colColumnaPlantilla;
    private FileChooser fc;
    private DirectoryChooser dc;
    private File excel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fc = new FileChooser();
        dc = new DirectoryChooser();
        Herramienta.conversorComboHoja(cmbHoja);
        Herramienta.conversorComboPosicionDato(cmbClave);
        Herramienta.conversorComboPosicionDato(cmbCorreo);
        Herramienta.conversorComboPosicionDato(cmbColumnaPlantilla);
        Herramienta.conversorComboPlantillaCorreo(cmbPlantilla);
        Herramienta.cargarComboPlantillaCorreo(cmbPlantilla);
        colExcelId.setCellValueFactory(new PropertyValueFactory<>("posicion"));
        colExcelClave.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colExcelEmail.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colIdColumnaPlantilla.setCellValueFactory(new PropertyValueFactory<>("etiqueta"));
        colColumnaPlantilla.setCellValueFactory(new PropertyValueFactory<>("campo"));
        colAdjuntoId.setCellValueFactory(new PropertyValueFactory<>("orden"));
        colAdjuntoArchivos.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colAdjuntoRuta.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Adjunto, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Adjunto, String> adjuntoStringCellDataFeatures) {
                String salida = "";
                salida = adjuntoStringCellDataFeatures.getValue().getRuta().getPath();
                return new ReadOnlyObjectWrapper<>(salida);
            }
        });
        colCorreoId.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<EnvioCorreo, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<EnvioCorreo, String> envioCorreoStringCellDataFeatures) {
                String salida = "";
                salida = String.valueOf(envioCorreoStringCellDataFeatures.getValue().getPosicionDato().getPosicion());
                return new ReadOnlyObjectWrapper<>(salida);
            }
        });
        colCorreoEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colCorreoEmail.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<EnvioCorreo, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<EnvioCorreo, String> envioCorreoStringCellDataFeatures) {
                String salida = "";
                salida = envioCorreoStringCellDataFeatures.getValue().getPosicionDato().getCorreo();
                return new ReadOnlyObjectWrapper<>(salida);
            }
        });
        colCorreoAdjuntos.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<EnvioCorreo, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<EnvioCorreo, String> envioCorreoStringCellDataFeatures) {
                String salida = "";
                for (String nombre : envioCorreoStringCellDataFeatures.getValue().getAdjuntoNombres()) {
                    salida += nombre + " | ";
                }
                return new ReadOnlyObjectWrapper<>(salida);
            }
        });
        cmbHoja.valueProperty().addListener(new ChangeListener<Sheet>() {
            @Override
            public void changed(ObservableValue<? extends Sheet> observableValue, Sheet t1, Sheet t2) {
                cmbClave.getItems().clear();
                if (t2 != null) {
                    DataFormatter dataFormatter = new DataFormatter();
                    Iterator it = t2.getRow(0).cellIterator();
                    while(it.hasNext()){
                        org.apache.poi.ss.usermodel.Cell celda = (Cell)it.next();
                        cmbClave.getItems().add(new PosicionDato(celda.getColumnIndex(), dataFormatter.formatCellValue(celda), ""));
                        cmbCorreo.getItems().add(new PosicionDato(celda.getColumnIndex(), dataFormatter.formatCellValue(celda), ""));
                        cmbColumnaPlantilla.getItems().add(new PosicionDato(celda.getColumnIndex(), dataFormatter.formatCellValue(celda), ""));
                    }
                }
            }
        });
        cmbPlantilla.valueProperty().addListener(new ChangeListener<PlantillaCorreo>() {
            @Override
            public void changed(ObservableValue<? extends PlantillaCorreo> observableValue, PlantillaCorreo t1, PlantillaCorreo t2) {
                if (t1 != null) {
                    t1.setAsunto(txtPlantillaAsunto.getText());
                    t1.setContenido(txtPlantillaMensaje.getText());
                    AccesoGeneral.guardaArchivo();
                }
                if (t2 != null) {
                    txtPlantillaAsunto.setText(t2.getAsunto());
                    txtPlantillaMensaje.setText(t2.getContenido());
                }
            }
        });
        txtPlantillaAsunto.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean t1, Boolean t2) {
                if (!t2) {
                    PlantillaCorreo plantillaCorreo = cmbPlantilla.getValue();
                    if (!plantillaCorreo.getAsunto().equals(txtPlantillaAsunto.getText()) ||
                    !plantillaCorreo.getContenido().equals(txtPlantillaMensaje.getText()))
                    plantillaCorreo.setAsunto(txtPlantillaAsunto.getText());
                    plantillaCorreo.setContenido(txtPlantillaMensaje.getText());
                    AccesoGeneral.guardaArchivo();
                }
            }
        });
    }

    public void cargarExcel(ActionEvent actionEvent) {
        tblExcel.getItems().clear();
        Sheet hoja = cmbHoja.getValue();
        long columnaClave = cmbClave.getValue().getPosicion();
        long columnaCorreo = cmbCorreo.getValue().getPosicion();
        DataFormatter dataFormatter = new DataFormatter();
        Iterator it = hoja.rowIterator();
        while (it.hasNext()) {
            Row fila = (Row) it.next();
            if (fila.getRowNum() > 0) {
                if (fila.getCell((int) columnaClave) != null) {
                    tblExcel.getItems().add(
                            new PosicionDato(
                                    fila.getRowNum(),
                                    dataFormatter.formatCellValue(fila.getCell((int) columnaClave)).trim().toUpperCase(),
                                    fila.getCell((int) columnaCorreo).getStringCellValue().trim().toUpperCase()
                            )
                    );
                }
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

    public void nuevoPlantilla(ActionEvent actionEvent) {
        String nombrePlantilla = Herramienta.textInputDialog("Nombre Plantilla", "Nuevo nombre plantilla", "Ingrese el nombre para la nueva plantilla: ");
        AccesoGeneral.getInstancia().getPlantillas().add(new PlantillaCorreo(nombrePlantilla, "", ""));
        AccesoGeneral.guardaArchivo();
    }

    public void eliminarPlantilla(ActionEvent actionEvent) {
        if (Herramienta.confirmationDialog("Eliminar Registro", "Eliminar Plantilla", "¿Está seguro de eliminar la plantilla?")) {
            AccesoGeneral.getInstancia().getPlantillas().remove(cmbPlantilla.getValue());
            AccesoGeneral.guardaArchivo();
            Herramienta.cargarComboPlantillaCorreo(cmbPlantilla);
        }
    }

    public void agregarColumna(ActionEvent actionEvent) {
        String etiqueta = Herramienta.textInputDialog("Etiqueta", "Ingreso de Etiqueta", "Ingrese la etiqueta a reemplazar: ");
        EtiquetaCampo etiquetaCampo = new EtiquetaCampo(etiqueta, cmbColumnaPlantilla.getValue().getNombre(), cmbColumnaPlantilla.getValue().getPosicion());
        tblColumnas.getItems().add(etiquetaCampo);
    }

    public void removerColumna(ActionEvent actionEvent) {
        EtiquetaCampo etiquetaCampo = tblColumnas.getSelectionModel().getSelectedItem();
        tblColumnas.getItems().remove(etiquetaCampo);
    }

    public void agregaAdjunto(ActionEvent actionEvent) {
        dc.setTitle("Seleccione carpeta");
        File nuevoAdjunto = dc.showDialog(new Stage());
        Adjunto adjunto = new Adjunto(tblAdjuntos.getItems().size() + 1, nuevoAdjunto, Herramienta.contarArchivos(nuevoAdjunto));
        tblAdjuntos.getItems().add(adjunto);
    }

    public void eliminaAdjunto(ActionEvent actionEvent) {
        if (Herramienta.confirmationDialog("Confirmar Acción", "Eliminar elemento", "¿Seguro de remover el elemento seleccionado?")) {
            tblAdjuntos.getItems().remove(tblAdjuntos.getSelectionModel().getSelectedItem());
        }
    }

    public void cargarDatos(ActionEvent actionEvent) {
        tblCorreo.getItems().clear();
        for (PosicionDato posicionDato: tblExcel.getItems()) {
            EnvioCorreo envioCorreo = new EnvioCorreo(posicionDato, "PENDIENTE");
            List<File> adjuntos = envioCorreo.getAdjuntos();
            List<String> adjuntoNombres = envioCorreo.getAdjuntoNombres();
            FileFilter filtro = new FileFilter() {
                @Override
                public boolean accept(File file) {
                    if (file.getName().contains(envioCorreo.getPosicionDato().getNombre()))
                        return true;
                    return false;
                }
            };
            for (Adjunto adjunto : tblAdjuntos.getItems()) {
                File[] archivos = adjunto.getRuta().listFiles(filtro);
                File adj = null;
                try{
                    adj = archivos[0];
                    adjuntoNombres.add(adj.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                    adjuntoNombres.add(null);
                }
                adjuntos.add(adj);
            }
            tblCorreo.getItems().add(envioCorreo);
        }
    }

    public void enviarCorreos(ActionEvent actionEvent) throws InterruptedException {
        /**
         * preparar los elementos de reemplazo
         */
        Iterator<List<String>> valores = obtenerValores().iterator();
        Iterator<EnvioCorreo> correos = tblCorreo.getItems().iterator();
        List<EtiquetaCampo> columnas = tblColumnas.getItems();
        int conteo = 1;
        while (correos.hasNext()) {
            try {
                EnvioCorreo correo = correos.next();
                Iterator<String> valor = valores.next().iterator();
                String asunto = cmbPlantilla.getValue().getAsunto();
                String mensaje = cmbPlantilla.getValue().getContenido();
                for (EtiquetaCampo columna : columnas) {
                    String etiqueta = columna.getEtiqueta();
                    String val = valor.next();
                    asunto = asunto.replaceAll(etiqueta, val);
                    mensaje = mensaje.replaceAll(etiqueta, val);
                }
                enviarMensajeCorreo(correo, asunto, mensaje);
                correo.setEstado("ENVIADO");
                tblCorreo.refresh();
                System.out.println("enviado " + conteo + "/"+ tblCorreo.getItems().size() );
                conteo++;
                TimeUnit.SECONDS.sleep(2);
            } catch (NoSuchElementException e) {
                e.printStackTrace();
            }
        }
    }

    private void enviarMensajeCorreo(EnvioCorreo correo, String asunto, String mensaje) {
        String to = correo.getPosicionDato().getCorreo().toLowerCase();
        String from = txtCorreo.getText().toLowerCase();
        String username = from;
        String password = txtContrasena.getText();
        String host = "smtp.gmail.com";
        Properties propiedades = new Properties();
        propiedades.put("mail.smtp.auth", "true");
        propiedades.put("mail.smtp.ssl.trust", host);
        propiedades.put("mail.smtp.starttls.enable", "true");
        propiedades.put("mail.smtp.host", host);
        propiedades.put("mail.smtp.port", "587");
        propiedades.put("mail.smtp.debug", "true");
        propiedades.put("mail.smtp.ssl.protocols", "TLSv1.1 TLSv1.2");
        Session sesion = Session.getInstance(propiedades, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        try {
            Message message = new MimeMessage(sesion);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(asunto);
            BodyPart messageCuerpo = new MimeBodyPart();
            messageCuerpo.setText(mensaje);
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageCuerpo);
            for (File adjunto : correo.getAdjuntos()) {
                messageCuerpo = new MimeBodyPart();
                DataSource source = new FileDataSource(adjunto.getPath());
                messageCuerpo.setDataHandler(new DataHandler(source));
                messageCuerpo.setFileName(adjunto.getName());
                multipart.addBodyPart(messageCuerpo);
            }
            message.setContent(multipart);
            Transport.send(message);
            System.out.println("correo enviado");
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private List<List<String>> obtenerValores() {
        Sheet hoja = cmbHoja.getValue();
        DataFormatter formatter = new DataFormatter();
        List<Integer> posiciones = new ArrayList<>();
        for (EtiquetaCampo etiquetaCampo : tblColumnas.getItems()) {
            posiciones.add((int) etiquetaCampo.getPosicion());
        }
        List<List<String>> valores = new ArrayList<>();
        Iterator it = hoja.rowIterator();
        while (it.hasNext()) {
            Row fila = (Row) it.next();
            if (fila.getRowNum() > 0 && fila != null) {
                List<String> datos = new ArrayList<>();
                for (int posicion : posiciones) {
                    if (fila.getCell(posicion) != null) {
                        datos.add(formatter.formatCellValue(fila.getCell(posicion)));
                    }
                }
                valores.add(datos);
            }
        }
        return valores;
    }

}
