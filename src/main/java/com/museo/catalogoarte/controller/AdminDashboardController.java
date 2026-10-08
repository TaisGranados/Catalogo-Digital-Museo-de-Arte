package com.museo.catalogoarte.controller;

import com.museo.catalogoarte.App;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AdminDashboardController {

    @FXML
    private void handleGestionObras() {
        abrirVentana("admin-view.fxml", "Gestión de Obras de Arte");
    }

    @FXML
    private void handleGestionUsuarios() {
        abrirVentana("user-admin-view.fxml", "Gestión de Usuarios");
    }

    @FXML
    private void handleCerrarSesion() {
        try {
            App.setRoot("login-view");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void abrirVentana(String fxmlFile, String title) {
        try {
            // Es importante quitar el prefijo de paquete aquí si los FXML están en la misma carpeta que App.java
            FXMLLoader loader = new FXMLLoader(App.class.getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}