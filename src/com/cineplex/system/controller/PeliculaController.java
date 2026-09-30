package com.cineplex.system.controller;

import com.cineplex.system.model.Pelicula;
import com.cineplex.system.model.ResultadoOperacion;
import com.cineplex.system.service.PeliculaService;
import com.cineplex.system.utils.AlertInformation;
import com.cineplex.system.utils.Roles;
import com.cineplex.system.utils.Session;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Pantalla rapida de gestion de peliculas: tabla + formulario en la
 * misma vista (crear, editar y eliminar sin cambiar de pantalla).
 */
public class PeliculaController {

    @FXML
    private TextField txtTitulo;

    @FXML
    private ComboBox<String> cmbGenero;

    @FXML
    private ComboBox<String> cmbCategoria;

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

    private final PeliculaService peliculaService = new PeliculaService();
    private final AlertInformation alertInfo = new AlertInformation();

    @FXML
    public void initialize() {

        // Misma restriccion que RegistrarPeliculaController: solo
        // Administrador y Administrador de Cine pueden entrar aqui.
        if (!Roles.puedeRegistrarPelicula(Session.getRolActual())) {
            alertInfo.viewAlert("WARNING", "ACCESO DENEGADO", "MÓDULO EXCLUSIVO",
                    "No tienes permiso para gestionar películas.");
            btnGuardar.setDisable(true);
            btnEditar.setDisable(true);
            btnEliminar.setDisable(true);
        }

        cmbGenero.setItems(FXCollections.observableArrayList(
                "Acción", "Animación", "Aventura", "Ciencia ficción", "Comedia",
                "Documental", "Drama", "Fantasía", "Musical", "Suspenso", "Terror"));

        cmbCategoria.setItems(FXCollections.observableArrayList(
                "A", "B", "B-15", "C", "D"));

        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colDirector.setCellValueFactory(new PropertyValueFactory<>("director"));
        colDuracion.setCellValueFactory(new PropertyValueFactory<>("duracion"));
        colPoster.setCellValueFactory(new PropertyValueFactory<>("poster"));

        tblPeliculas.getItems().setAll(peliculaService.listar());
        tblPeliculas.setOnMouseClicked(event -> seleccionarPelicula());
    }

    @FXML
    private void seleccionarPelicula() {

        Pelicula pelicula = tblPeliculas.getSelectionModel().getSelectedItem();

        if (pelicula != null) {
            txtTitulo.setText(pelicula.getTitulo());
            cmbGenero.setValue(pelicula.getGenero());
            cmbCategoria.setValue(pelicula.getCategoria());
            txtDirector.setText(pelicula.getDirector());

            //la duracion se guarda en minutos totales; aqui se separa en horas y minutos para mostrarla
            int horas = pelicula.getDuracion() / 60;
            int minutos = pelicula.getDuracion() % 60;
            txtDuracion.setText(String.valueOf(horas));
            txtMinutos.setText(String.valueOf(minutos));
        }
    }

    /** Junta horas + minutos en la duracion total (en minutos) que usa la base de datos. */
    private Integer calcularDuracionTotal() {
        String textoHoras = txtDuracion.getText().trim();
        String textoMinutos = txtMinutos.getText().trim();

        try {
            int horas = textoHoras.isEmpty() ? 0 : Integer.parseInt(textoHoras);
            int minutos = textoMinutos.isEmpty() ? 0 : Integer.parseInt(textoMinutos);

            if (horas < 0 || minutos < 0 || minutos > 59) {
                alertInfo.viewAlert("WARNING", "DURACIÓN INVÁLIDA", "REVISA LA DURACIÓN",
                        "Los minutos deben estar entre 0 y 59, y las horas no pueden ser negativas.");
                return null;
            }

            return (horas * 60) + minutos;

        } catch (NumberFormatException e) {
            alertInfo.viewAlert("WARNING", "DURACIÓN INVÁLIDA", "REVISA LA DURACIÓN",
                    "Las horas y los minutos deben ser números.");
            return null;
        }
    }

    private Pelicula leerFormulario() {
        Pelicula pelicula = new Pelicula();
        pelicula.setTitulo(txtTitulo.getText().trim());
        pelicula.setGenero(cmbGenero.getValue());
        pelicula.setCategoria(cmbCategoria.getValue());
        pelicula.setDirector(txtDirector.getText().trim());
        pelicula.setPoster("poster.jpg");
        return pelicula;
    }

    @FXML
    private void guardar() {

        Integer duracionTotal = calcularDuracionTotal();
        if (duracionTotal == null) {
            return;
        }

        Pelicula pelicula = leerFormulario();
        pelicula.setDuracion(duracionTotal);

        ResultadoOperacion resultado = peliculaService.registrar(pelicula);
        mostrarResultado(resultado, "registrada");
    }

    @FXML
    private void editar() {

        Pelicula pelicula = tblPeliculas.getSelectionModel().getSelectedItem();

        if (pelicula == null) {
            alertInfo.viewAlert("WARNING", "SIN SELECCIÓN", "NINGUNA PELÍCULA SELECCIONADA",
                    "Selecciona una película de la tabla primero.");
            return;
        }

        Integer duracionTotal = calcularDuracionTotal();
        if (duracionTotal == null) {
            return;
        }

        Pelicula datosNuevos = leerFormulario();
        datosNuevos.setIdPelicula(pelicula.getIdPelicula());
        datosNuevos.setDuracion(duracionTotal);

        ResultadoOperacion resultado = peliculaService.editar(datosNuevos);
        mostrarResultado(resultado, "editada");
    }

    @FXML
    private void eliminar() {

        Pelicula pelicula = tblPeliculas.getSelectionModel().getSelectedItem();

        if (pelicula == null) {
            alertInfo.viewAlert("WARNING", "SIN SELECCIÓN", "NINGUNA PELÍCULA SELECCIONADA",
                    "Selecciona una película de la tabla primero.");
            return;
        }

        ResultadoOperacion resultado = peliculaService.eliminar(pelicula.getIdPelicula());

        if (resultado == ResultadoOperacion.EXITO) {
            alertInfo.viewAlert("INFORMATION", "PELÍCULA ELIMINADA", "LISTO",
                    "\"" + pelicula.getTitulo() + "\" se eliminó correctamente.");
            tblPeliculas.getItems().setAll(peliculaService.listar());
            limpiar();
        } else if (resultado == ResultadoOperacion.REGISTRO_EN_USO) {
            alertInfo.viewAlert("WARNING", "NO SE PUEDE ELIMINAR", "PELÍCULA CON FUNCIONES PROGRAMADAS",
                    "No se puede eliminar \"" + pelicula.getTitulo()
                    + "\" porque tiene funciones o boletos asociados en el sistema.");
        } else {
            alertInfo.viewAlert("ERROR", "NO SE PUDO ELIMINAR", "ERROR AL ELIMINAR",
                    "Ocurrió un error al eliminar la película.");
        }
    }

    /** Un solo lugar para traducir el resultado de registrar/editar en la alerta correcta. */
    private void mostrarResultado(ResultadoOperacion resultado, String accion) {
        if (resultado == ResultadoOperacion.EXITO) {
            alertInfo.viewAlert("INFORMATION", "PELÍCULA " + accion.toUpperCase(), "LISTO",
                    "La película se " + accion + " correctamente.");
            tblPeliculas.getItems().setAll(peliculaService.listar());
            limpiar();
        } else if (resultado == ResultadoOperacion.TITULO_DUPLICADO) {
            alertInfo.viewAlert("WARNING", "TÍTULO EN USO", "ESA PELÍCULA YA ESTÁ REGISTRADA",
                    "Ya hay una película con ese título. Revisa la tabla.");
        } else {
            alertInfo.viewAlert("ERROR", "NO SE PUDO GUARDAR", "ERROR AL GUARDAR",
                    "Verifica que todos los campos estén completos y llenos correctamente.");
        }
    }

    @FXML
    private void limpiar() {
        txtTitulo.clear();
        cmbGenero.setValue(null);
        cmbCategoria.setValue(null);
        txtDirector.clear();
        txtDuracion.clear();
        txtMinutos.clear();
        tblPeliculas.getSelectionModel().clearSelection();
    }
}