package com.museo.catalogoarte.controller;

import com.museo.catalogoarte.db.UsuarioDAO;
import com.museo.catalogoarte.model.Usuario;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UserFormController {

    @FXML
    private Label tituloLabel;
    @FXML
    private TextField nombreField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ComboBox<String> rolComboBox;
    @FXML
    private Label errorLabel;

    private UsuarioDAO usuarioDAO;
    private Usuario usuarioEditable;

    public void initialize() {
        usuarioDAO = new UsuarioDAO();
        rolComboBox.setItems(FXCollections.observableArrayList("visitante", "administrador"));
    }

    // Método para recibir un usuario y poblar el formulario para edición
    public void setUsuarioParaEditar(Usuario usuario) {
        this.usuarioEditable = usuario;

        tituloLabel.setText("Editar Usuario");
        nombreField.setText(usuario.getNombre());
        emailField.setText(usuario.getCorreoElectronico());
        rolComboBox.setValue(usuario.getRol());

        // Deshabilitamos el campo de contraseña en modo edición
        passwordField.setDisable(true);
        passwordField.setPromptText("La contraseña no se puede cambiar aquí");
    }

    @FXML
    private void handleGuardarUsuario() {
        String nombre = nombreField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String rol = rolComboBox.getSelectionModel().getSelectedItem();

        if (nombre.isEmpty() || email.isEmpty() || rol == null || (usuarioEditable == null && password.isEmpty())) {
            errorLabel.setText("Todos los campos son obligatorios.");
            return;
        }

        if (usuarioEditable == null) { // Modo Añadir
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombre(nombre);
            nuevoUsuario.setCorreoElectronico(email);
            nuevoUsuario.setRol(rol);
            usuarioDAO.insertar(nuevoUsuario, password);
        } else { // Modo Editar
            usuarioEditable.setNombre(nombre);
            usuarioEditable.setCorreoElectronico(email);
            usuarioEditable.setRol(rol);
            usuarioDAO.actualizar(usuarioEditable);
        }

        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) tituloLabel.getScene().getWindow();
        stage.close();
    }
}