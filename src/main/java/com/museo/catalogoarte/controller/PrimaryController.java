package com.museo.catalogoarte.controller;

import com.museo.catalogoarte.App;
import com.museo.catalogoarte.db.ObraDeArteDAO;
import com.museo.catalogoarte.db.SalaDAO;
import com.museo.catalogoarte.model.ObraDeArte;
import com.museo.catalogoarte.model.Sala;
import java.io.IOException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PrimaryController {

    @FXML
    private TextField busquedaField;
    @FXML
    private ComboBox<Sala> salaFilterComboBox;
    @FXML
    private ListView<ObraDeArte> obrasListView;

    private ObraDeArteDAO obraDeArteDAO;
    private SalaDAO salaDAO;

    public void initialize() {
        this.obraDeArteDAO = new ObraDeArteDAO();
        this.salaDAO = new SalaDAO();
        
        configurarFiltros();
        configurarLista();
        
        // Carga inicial de todas las obras
        cargarObras();
    }

    private void configurarFiltros() {
        // Llenar el ComboBox con las salas desde la BD
        salaFilterComboBox.setItems(FXCollections.observableArrayList(salaDAO.obtenerTodas()));
        
        // Añadir un "listener" que se activa cada vez que el texto de búsqueda cambia
        busquedaField.textProperty().addListener((observable, oldValue, newValue) -> {
            cargarObras(); 
        });

        // Añadir un "listener" para el ComboBox de salas
        salaFilterComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            cargarObras();
        });
    }
    
    private void configurarLista() {
        obrasListView.setCellFactory(param -> new ListCell<ObraDeArte>() {
            @Override
            protected void updateItem(ObraDeArte obra, boolean empty) {
                super.updateItem(obra, empty);
                if (empty || obra == null || obra.getTitulo() == null) {
                    setText(null);
                } else {
                    setText(obra.getTitulo());
                }
            }
        });

        obrasListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                ObraDeArte obraSeleccionada = obrasListView.getSelectionModel().getSelectedItem();
                if (obraSeleccionada != null) {
                    mostrarDetalleObra(obraSeleccionada);
                }
            }
        });
    }

    private void cargarObras() {
        String busqueda = busquedaField.getText();
        Sala salaSeleccionada = salaFilterComboBox.getSelectionModel().getSelectedItem();
        
        obrasListView.getItems().clear();
        List<ObraDeArte> catalogo = obraDeArteDAO.buscarObras(busqueda, salaSeleccionada);
        obrasListView.getItems().addAll(catalogo);
    }

    private void mostrarDetalleObra(ObraDeArte obra) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("detail-view.fxml"));
            Parent root = loader.load();
            DetailController controller = loader.getController();
            controller.setObraDeArte(obra);
            Stage stage = new Stage();
            stage.setTitle("Detalle de la Obra");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}