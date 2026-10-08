package com.museo.catalogoarte.controller;

import com.museo.catalogoarte.App;
import com.museo.catalogoarte.db.ObraDeArteDAO;
import com.museo.catalogoarte.model.ObraDeArte;
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

public class AdminController {

    @FXML
    private TableView<ObraDeArte> obrasTableView;
    @FXML
    private TableColumn<ObraDeArte, Integer> idColumn;
    @FXML
    private TableColumn<ObraDeArte, String> tituloColumn;
    @FXML
    private TableColumn<ObraDeArte, String> artistaColumn;
    @FXML
    private TableColumn<ObraDeArte, Integer> anioColumn;
    @FXML
    private TableColumn<ObraDeArte, String> descripcionColumn;
    
    @FXML
    private Button anadirButton;
    @FXML
    private Button editarButton;
    @FXML
    private Button eliminarButton;

    private ObraDeArteDAO obraDeArteDAO;

    public void initialize() {
        obraDeArteDAO = new ObraDeArteDAO();
        
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        tituloColumn.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        artistaColumn.setCellValueFactory(new PropertyValueFactory<>("artista"));
        anioColumn.setCellValueFactory(new PropertyValueFactory<>("anioCreacion"));
        descripcionColumn.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        
        cargarDatosTabla();
    }
    
    private void cargarDatosTabla() {
        ObservableList<ObraDeArte> listaObras = FXCollections.observableArrayList(obraDeArteDAO.obtenerTodas());
        obrasTableView.setItems(listaObras);
    }

    @FXML
    private void handleAnadirObra() {
        abrirFormularioObra(null);
    }

    @FXML
    private void handleEditarObra() {
        ObraDeArte obraSeleccionada = obrasTableView.getSelectionModel().getSelectedItem();
        if (obraSeleccionada == null) {
            mostrarAlerta("Ninguna selección", "Por favor, seleccione una obra para editar.");
            return;
        }
        abrirFormularioObra(obraSeleccionada);
    }
    
    private void abrirFormularioObra(ObraDeArte obra) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("obra-form-view.fxml"));
            Parent root = loader.load();

            ObraFormController controller = loader.getController();
            if (obra != null) {
                controller.setObraParaEditar(obra);
            }

            Stage stage = new Stage();
            stage.setTitle(obra != null ? "Editar Obra" : "Añadir Nueva Obra");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            
            cargarDatosTabla();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEliminarObra() {
        ObraDeArte obraSeleccionada = obrasTableView.getSelectionModel().getSelectedItem();
        
        if (obraSeleccionada == null) {
            mostrarAlerta("Ninguna selección", "Por favor, seleccione una obra de la tabla para eliminar.");
            return;
        }
        
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Está seguro de que desea eliminar la obra?");
        confirmacion.setContentText(obraSeleccionada.getTitulo() + " por " + obraSeleccionada.getArtista());
        
        Optional<ButtonType> resultado = confirmacion.showAndWait();
        
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            obraDeArteDAO.eliminar(obraSeleccionada.getId());
            cargarDatosTabla();
        }
    }
    
    @FXML
    private void handleGestionUsuarios() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("user-admin-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Gestión de Usuarios");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
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