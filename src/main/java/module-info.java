module org.btqt3 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens org.btqt3 to javafx.fxml;
    exports org.btqt3;
    exports org.btqt3.Controller;
    opens org.btqt3.Controller to javafx.fxml;
    exports org.btqt3.Model;
    opens org.btqt3.Model to javafx.fxml;
}