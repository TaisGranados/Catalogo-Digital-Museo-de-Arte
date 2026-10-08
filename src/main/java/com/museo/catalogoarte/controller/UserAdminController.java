package com.museo.catalogoarte.controller;

import com.museo.catalogoarte.App;
import com.museo.catalogoarte.db.UsuarioDAO;
import com.museo.catalogoarte.model.Usuario;
import java.io.IOException;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class UserAdminController {

    @FXML
    private TableView<Usuario> usuariosTableView;
    @FXML
    private TableColumn<Usuario, Integer> idColumn;
    @FXML
    private TableColumn<Usuario, String> nombreColumn;
    @FXML
    private TableColumn<Usuario, String> emailColumn;
    @FXML
    private TableColumn<Usuario, String> rolColumn;
    @FXML
    private Button anadirButton;
    @FXML
    private Button editarButton; // Botón nuevo
    @FXML
    private Button eliminarButton;

    private UsuarioDAO usuarioDAO;

    public void initialize() {
        usuarioDAO = new UsuarioDAO();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("correoElectronico"));
        rolColumn.setCellValueFactory(new PropertyValueFactory<>("rol"));

        cargarDatosTabla();
    }

    private void cargarDatosTabla() {
        ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList(usuarioDAO.obtenerTodos());
        usuariosTableView.setItems(listaUsuarios);
    }

    @FXML
    private void handleAnadirUsuario() {
        abrirFormularioUsuario(null);
    }

    @FXML
    private void handleEditarUsuario() {
        Usuario usuarioSeleccionado = usuariosTableView.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado == null) {
            mostrarAlerta("Ninguna selección", "Por favor, seleccione un usuario para editar.");
            return;
        }
        abrirFormularioUsuario(usuarioSeleccionado);
    }

    private void abrirFormularioUsuario(Usuario usuario) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("user-form-view.fxml"));
            Parent root = loader.load();

            UserFormController controller = loader.getController();
            if (usuario != null) {
                controller.setUsuarioParaEditar(usuario);
            }

            Stage stage = new Stage();
            stage.setTitle(usuario != null ? "Editar Usuario" : "Añadir Nuevo Usuario");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            cargarDatosTabla();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEliminarUsuario() {
        Usuario usuarioSeleccionado = usuariosTableView.getSelectionModel().getSelectedItem();

        if (usuarioSeleccionado == null) {
            mostrarAlerta("Ninguna selección", "Por favor, seleccione un usuario de la tabla.");
            return;
        }

        if (usuarioSeleccionado.getCorreoElectronico().equals("admin@museo.com")) {
            mostrarAlerta("Acción no permitida", "No puedes eliminar al usuario administrador principal.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Está seguro de que desea eliminar al usuario?");
        confirmacion.setContentText(usuarioSeleccionado.getNombre() + " (" + usuarioSeleccionado.getCorreoElectronico() + ")");

        Optional<ButtonType> resultado = confirmacion.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            usuarioDAO.eliminar(usuarioSeleccionado.getId());
            cargarDatosTabla();
        }
    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}