module com.example.test {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires org.json;
    //requires org.json;

    opens com.example.test to javafx.fxml;
    exports com.example.test;
}