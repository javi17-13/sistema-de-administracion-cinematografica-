package com.cineplex.system.controller;

import com.cineplex.system.model.Pelicula;
import com.cineplex.system.service.PeliculaService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class PeliculaController {

    @FXML
    private TextField txtTitulo;

    @FXML
    private ComboBox<String> cmbGenero;

    @FXML
    private TextField txtCategoria;

    @FXML
    private TextField txtDirector;

    @FXML
    private TextField txtDuracion;
    
    @FXML
    private TextField txtMinutos;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnLimpiar;

    @FXML
    private TableView<Pelicula> tblPeliculas;

    // COLUMNAS DE LA TABLA
    @FXML
    private TableColumn<Pelicula, String> colTitulo;

    @FXML
    private TableColumn<Pelicula, String> colGenero;

    @FXML
    private TableColumn<Pelicula, String> colCategoria;

    @FXML
    private TableColumn<Pelicula, String> colDirector;

    @FXML
    private TableColumn<Pelicula, Integer> colDuracion;

    @FXML
    private TableColumn<Pelicula, String> colPoster;

    private PeliculaService peliculaService;

    @FXML
    public void initialize() {

        peliculaService = new PeliculaService();

        cmbGenero.getItems().addAll(
                "Acción",
                "Animación",
                "Aventura",
                "Comedia",
                "Drama",
                "Terror",
                "Ciencia ficción"
        );

        colTitulo.setCellValueFactory(
                new PropertyValueFactory<>("titulo")
        );

        colGenero.setCellValueFactory(
                new PropertyValueFactory<>("genero")
        );

        colCategoria.setCellValueFactory(
                new PropertyValueFactory<>("categoria")
        );

        colDirector.setCellValueFactory(
                new PropertyValueFactory<>("director")
        );

        colDuracion.setCellValueFactory(
                new PropertyValueFactory<>("duracion")
        );

        colPoster.setCellValueFactory(
                new PropertyValueFactory<>("poster")
        );
        tblPeliculas.getItems().setAll(peliculaService.listar());
        tblPeliculas.setOnMouseClicked(event -> seleccionarPelicula());
    }

    @FXML
    private void seleccionarPelicula() {
            

        Pelicula pelicula = tblPeliculas.getSelectionModel().getSelectedItem();

        if (pelicula != null) {
                       

            txtTitulo.setText(pelicula.getTitulo());
            cmbGenero.setValue(pelicula.getGenero());
            txtCategoria.setText(pelicula.getCategoria());
            txtDirector.setText(pelicula.getDirector());
            txtDuracion.setText(String.valueOf(pelicula.getDuracion()));
        }
    }

    @FXML
    private void guardar() {

        Pelicula pelicula = new Pelicula();

        pelicula.setTitulo(txtTitulo.getText());
        pelicula.setGenero(cmbGenero.getValue());
        pelicula.setCategoria(txtCategoria.getText());
        pelicula.setDirector(txtDirector.getText());

        String textoHoras = txtDuracion.getText().trim();
        String textoMinutos = txtMinutos.getText().trim();

        try {

            int horas = textoHoras.isEmpty() ? 0 : Integer.parseInt(textoHoras);
            int minutos = textoMinutos.isEmpty() ? 0 : Integer.parseInt(textoMinutos);

            if (horas < 0 || minutos < 0 || minutos > 59) {
                System.out.println("La duración no es válida.");
                return;
            }

            int duracionTotal = (horas * 60) + minutos;

            pelicula.setDuracion(duracionTotal);

        } catch (NumberFormatException e) {
            System.out.println("Las horas y los minutos deben ser números.");
            return;
        }

        pelicula.setPoster("poster.jpg");

        String error = peliculaService.agregar(pelicula);

        if (error == null) {
            System.out.println("Película guardada correctamente.");
            tblPeliculas.getItems().setAll(peliculaService.listar());
        } else {
            System.out.println(error);
        }
    }

    @FXML
    private void editar() {

        Pelicula pelicula = tblPeliculas.getSelectionModel().getSelectedItem();

        if (pelicula == null) {
            System.out.println("Seleccione una película para editar.");
            return;
        }

        pelicula.setTitulo(txtTitulo.getText());
        pelicula.setGenero(cmbGenero.getValue());
        pelicula.setCategoria(txtCategoria.getText());
        pelicula.setDirector(txtDirector.getText());

        try {
            pelicula.setDuracion(Integer.parseInt(txtDuracion.getText().trim()));
        } catch (NumberFormatException e) {
            System.out.println("La duración debe ser un número.");
            return;
        }

        pelicula.setPoster("poster.jpg");

        String error = peliculaService.editar(pelicula);

        if (error == null) {
            System.out.println("Película editada correctamente.");

            tblPeliculas.getItems().setAll(peliculaService.listar());

        } else {
            System.out.println("No se pudo editar la película.");
        }
    }
        @FXML

private void eliminar() {

    Pelicula pelicula = tblPeliculas.getSelectionModel().getSelectedItem();

    if (pelicula == null) {
        System.out.println("Seleccione una película para eliminar.");
        return;
    }

    

    boolean resultado = peliculaService.eliminar(pelicula.getIdPelicula());

    

    if (resultado) {
        System.out.println("Película eliminada correctamente.");

        tblPeliculas.getItems().setAll(peliculaService.listar());

    } else {
        System.out.println("No se pudo eliminar la película.");
    }
}
    @FXML
    private void limpiar() {

        txtTitulo.clear();
        cmbGenero.setValue(null);
        txtCategoria.clear();
        txtDirector.clear();
        txtDuracion.clear();

        tblPeliculas.getSelectionModel().clearSelection();

        System.out.println("Campos limpiados.");
    }
    

}
