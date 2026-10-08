module CatalogoArte {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    

    opens com.museo.catalogoarte.controller to javafx.fxml;
    opens com.museo.catalogoarte.model to javafx.base;
    exports com.museo.catalogoarte;
}