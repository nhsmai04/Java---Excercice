module org.lab2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.desktop;
    requires java.sql;

    opens org.lab2 to javafx.fxml;
    exports org.lab2;
    exports org.lab3;
    exports org.lab4;
}
