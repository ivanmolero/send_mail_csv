module org.imolero {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.imolero to javafx.fxml;
    exports org.imolero;
}