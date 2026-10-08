package com.museo.catalogoarte.controller;

import com.museo.catalogoarte.db.UsuarioDAO;
import com.museo.catalogoarte.util.PasswordManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ForgotPasswordController {

    @FXML
    private VBox step1Box;
    @FXML
    private Label infoLabel;
    @FXML
    private TextField emailField;

    @FXML
    private VBox step2Box;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Label mensajeFinalLabel;

    private UsuarioDAO usuarioDAO;
    private String emailVerificado;

    public void initialize() {
        this.usuarioDAO = new UsuarioDAO();
    }

    @FXML
    private void handleVerificarEmail() {
        String email = emailField.getText();
        if (email.isEmpty()) {
            infoLabel.setText("Por favor, introduce un correo.");
            return;
        }

        if (usuarioDAO.buscarPorEmail(email) != null) {
            // Correo válido, pasamos al paso 2
            this.emailVerificado = email;
            step1Box.setVisible(false);
            step1Box.setManaged(false); // Colapsa el espacio del VBox

            step2Box.setVisible(true);
            step2Box.setManaged(true); // Muestra el VBox de la nueva contraseña
        } else {
            infoLabel.setText("No se encontró ningún usuario con ese correo.");
        }
    }

    @FXML
    private void handleGuardarNuevaContrasena() {
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (newPassword.isEmpty() || !newPassword.equals(confirmPassword)) {
            mensajeFinalLabel.setText("Las contraseñas no coinciden o están vacías.");
            return;
        }

        // Encriptar y guardar la nueva contraseña
        String nuevoHash = PasswordManager.hashPassword(newPassword);
        usuarioDAO.actualizarContrasena(emailVerificado, nuevoHash);

        // Ocultar todo y mostrar mensaje final
        step2Box.setVisible(false);
        step2Box.setManaged(false);
        mensajeFinalLabel.setText("¡Contraseña actualizada con éxito! Ya puedes cerrar esta ventana.");
    }
}