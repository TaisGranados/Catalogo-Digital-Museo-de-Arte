package com.museo.catalogoarte.controller;

import com.museo.catalogoarte.App;
import com.museo.catalogoarte.db.UsuarioDAO;
import com.museo.catalogoarte.model.Usuario;
import com.museo.catalogoarte.util.PasswordManager; // Asegúrate de que este import esté presente
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private Label mensajeLabel;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;

    private UsuarioDAO usuarioDAO;

    public void initialize() {
        this.usuarioDAO = new UsuarioDAO();
    }

    @FXML
    private void handleLoginButtonAction(ActionEvent event) throws IOException {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            mensajeLabel.setText("Por favor, ingrese email y contraseña.");
            return;
        }

        Usuario usuario = usuarioDAO.buscarPorEmail(email);

        if (usuario == null) {
            mensajeLabel.setText("Email o contraseña incorrectos.");
        } else {
            // Verificación de contraseña REAL
            boolean passwordMatch = PasswordManager.checkPassword(password, usuario.getContrasenaHash());
            
            if (passwordMatch) {
                mensajeLabel.setText("¡Bienvenido " + usuario.getNombre() + "!");
                
                if ("administrador".equals(usuario.getRol())) {
                    App.setRoot("admin-dashboard-view");
                } else {
                    App.setRoot("primary");
                }

            } else {
                mensajeLabel.setText("Email o contraseña incorrectos.");
            }
        }
    }
    
    @FXML
    private void handleRegistroLink() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("register-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Registro de Nuevo Usuario");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
private void handleForgotPasswordLink() {
    try {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("forgot-password-view.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Recuperar Contraseña");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
}