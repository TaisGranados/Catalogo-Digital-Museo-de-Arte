package com.museo.catalogoarte.controller;

import com.museo.catalogoarte.db.UsuarioDAO;
import com.museo.catalogoarte.model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterController {

    @FXML
    private Label mensajeLabel;
    @FXML
    private TextField nombreField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;

    private UsuarioDAO usuarioDAO;

    public void initialize() {
        this.usuarioDAO = new UsuarioDAO();
    }

    @FXML
    private void handleRegisterButtonAction() {
        String nombre = nombreField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (nombre.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            mensajeLabel.setText("Todos los campos son obligatorios.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            mensajeLabel.setText("Las contraseñas no coinciden.");
            return;
        }

        // Comprobar si el email ya existe
        if (usuarioDAO.buscarPorEmail(email) != null) {
            mensajeLabel.setText("El correo electrónico ya está registrado.");
            return;
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setCorreoElectronico(email);
        nuevoUsuario.setRol("visitante"); // Todos los nuevos registros son visitantes

        usuarioDAO.insertar(nuevoUsuario, password);

        // Cerrar la ventana de registro
        Stage stage = (Stage) nombreField.getScene().getWindow();
        stage.close();
    }
}