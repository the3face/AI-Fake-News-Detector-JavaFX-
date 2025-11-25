module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires java.xml;

    opens org.example to javafx.fxml;
    exports org.example;
}
