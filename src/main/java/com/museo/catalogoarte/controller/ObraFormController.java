package com.museo.catalogoarte.controller;

import com.museo.catalogoarte.db.ObraDeArteDAO;
import com.museo.catalogoarte.db.SalaDAO;
import com.museo.catalogoarte.model.ObraDeArte;
import com.museo.catalogoarte.model.Sala;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ObraFormController {

    @FXML private Label tituloLabel;
    @FXML private TextField tituloField;
    @FXML private TextField artistaField;
    @FXML private TextField tecnicaField;
    @FXML private TextField anioField;
    @FXML private TextArea descripcionArea;
    @FXML private TextField rutaImagenField;
    @FXML private TextField rutaAudioField;
    @FXML private ComboBox<Sala> salaComboBox;
    @FXML private Label errorLabel;

    private ObraDeArteDAO obraDeArteDAO;
    private SalaDAO salaDAO;
    private ObraDeArte obraEditable;

    public void initialize() {
        obraDeArteDAO = new ObraDeArteDAO();
        salaDAO = new SalaDAO();
        // Llenar el ComboBox con las salas desde la base de datos
        salaComboBox.setItems(FXCollections.observableArrayList(salaDAO.obtenerTodas()));
    }

    public void setObraParaEditar(ObraDeArte obra) {
        this.obraEditable = obra;
        
        tituloLabel.setText("Editar Obra de Arte");
        tituloField.setText(obra.getTitulo());
        artistaField.setText(obra.getArtista());
        tecnicaField.setText(obra.getTecnica());
        anioField.setText(String.valueOf(obra.getAnioCreacion()));
        descripcionArea.setText(obra.getDescripcion());
        rutaImagenField.setText(obra.getRutaImagen());
        rutaAudioField.setText(obra.getRutaAudio());
        
        // Seleccionar la sala correcta en el ComboBox
        for (Sala sala : salaComboBox.getItems()) {
            if (sala.getId() == obra.getIdSala()) {
                salaComboBox.getSelectionModel().select(sala);
                break;
            }
        }
    }

    @FXML
    private void handleGuardar() {
        // Validación básica
        if (tituloField.getText().isEmpty() || anioField.getText().isEmpty() || rutaImagenField.getText().isEmpty() || salaComboBox.getValue() == null) {
            errorLabel.setText("Los campos Título, Año, Ruta de Imagen y Sala son obligatorios.");
            return;
        }

        try {
            ObraDeArte obra = (obraEditable != null) ? obraEditable : new ObraDeArte();
            
            obra.setTitulo(tituloField.getText());
            obra.setArtista(artistaField.getText());
            obra.setTecnica(tecnicaField.getText());
            obra.setAnioCreacion(Integer.parseInt(anioField.getText()));
            obra.setDescripcion(descripcionArea.getText());
            obra.setRutaImagen(rutaImagenField.getText());
            obra.setRutaAudio(rutaAudioField.getText());
            obra.setIdSala(salaComboBox.getValue().getId());
            
            // Asignar una categoría por defecto si es una obra nueva
            if (obraEditable == null) {
                obra.setIdCategoria(1); // "Pintura"
            }
            
            if (obraEditable == null) {
                obraDeArteDAO.insertar(obra);
} else {
                obraDeArteDAO.actualizar(obra);
            }
            
            cerrarVentana();
            
        } catch (NumberFormatException e) {
            errorLabel.setText("Error: El año debe ser un número.");
        } catch (Exception e) {
            errorLabel.setText("Error al guardar la obra.");
            e.printStackTrace();
        }
    }
    
    private void cerrarVentana() {
        Stage stage = (Stage) tituloLabel.getScene().getWindow();
        stage.close();
    }
}