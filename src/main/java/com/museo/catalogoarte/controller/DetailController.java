package com.museo.catalogoarte.controller;

import com.museo.catalogoarte.model.ObraDeArte;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
// Importa Image y MediaPlayer cuando los necesites
import javafx.scene.image.Image;
// import javafx.scene.media.MediaPlayer;

public class DetailController {

    @FXML private Label tituloLabel;
    @FXML private ImageView obraImageView;
    @FXML private Label artistaLabel;
    @FXML private Label tecnicaLabel;
    @FXML private Label anioLabel;
    @FXML private Text descripcionText;
    @FXML private Button audioButton;

    public void setObraDeArte(ObraDeArte obra) {
        tituloLabel.setText(obra.getTitulo());
        artistaLabel.setText("Artista: " + obra.getArtista());
        tecnicaLabel.setText("Técnica: " + obra.getTecnica());
        anioLabel.setText("Año: " + obra.getAnioCreacion());
        descripcionText.setText(obra.getDescripcion());

        // Lógica para cargar la imagen (requiere que la ruta sea correcta)
        try {
             Image image = new Image(getClass().getResourceAsStream("/" + obra.getRutaImagen()));
             obraImageView.setImage(image);
         } catch (Exception e) {
             System.err.println("No se pudo cargar la imagen: " + obra.getRutaImagen());
         }

        // Lógica para el botón de audio
        if (obra.getRutaAudio() == null || obra.getRutaAudio().isEmpty()) {
            audioButton.setVisible(false);
        }
    }
}
