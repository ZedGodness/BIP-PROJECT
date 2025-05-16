module com.mycompany.kvk {
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;
    requires okhttp3;
    requires java.net.http;
    requires org.eclipse.scout.json;
    opens com.mycompany.kvk to javafx.fxml;
    exports com.mycompany.kvk;
}
