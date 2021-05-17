module org.imolero.sendmailcsv {
    requires javafx.controls;
    requires javafx.fxml;
    requires pdfbox;
    requires fontbox;
    requires org.apache.poi.poi;
    requires mail;
    requires jakarta.activation;

    opens org.imolero.sendmailcsv to javafx.fxml, javafx.base;
    opens org.imolero.sendmailcsv.modelo to javafx.base;
    exports org.imolero.sendmailcsv;
    exports org.imolero.sendmailcsv.control.vista;
    exports org.imolero.sendmailcsv.modelo;
}