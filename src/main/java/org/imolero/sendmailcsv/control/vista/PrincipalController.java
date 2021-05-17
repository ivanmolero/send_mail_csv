package org.imolero.sendmailcsv.control.vista;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.imolero.sendmailcsv.App;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador de la interfaz principal, botonera lateral y cambio de contenido en el
 * hbox principal
 */
public class PrincipalController implements Initializable {
    public HBox hboxPrincipal;
    public ImageView imgLogo;
    public ToggleButton btnRenombrado;
    public ToggleButton btnEnvio;
    public ToggleGroup principal;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * Cambio de interfaz a renombrado.fxml
     * @param actionEvent
     */
    public void renombrado(ActionEvent actionEvent) {
        cambioInterfaz("renombrado.fxml", actionEvent);
    }

    /**
     * Cambio de interfaz a enviocorreo.fxml
     * @param actionEvent
     */
    public void envio(ActionEvent actionEvent) {
        cambioInterfaz("enviocorreo.fxml", actionEvent);
    }

    /**
     * Elimina la interfaz en la posición 1 del hbox, si existe, luego verifica que
     * el togglebutton este presionado y crea el nodo en base al fxml seleccionado
     * y lo agrega a la posición 1 del hbox.
     * @param path
     * @param actionEvent
     */
    private void cambioInterfaz(String path, ActionEvent actionEvent) {
        if (hboxPrincipal.getChildren().size() > 1) {
            hboxPrincipal.getChildren().remove(1);
        }
        if (((ToggleButton)actionEvent.getSource()).isSelected()) {
            try {
                VBox root = FXMLLoader.load(App.class.getResource(path));
                hboxPrincipal.getChildren().add(root);
                root.prefWidthProperty().bind(hboxPrincipal.widthProperty().add(-200));
                root.prefHeightProperty().bind(hboxPrincipal.heightProperty());
            } catch (IOException e) {
                System.out.println("mensaje: " + e.getMessage());
            }
        }
    }

}
