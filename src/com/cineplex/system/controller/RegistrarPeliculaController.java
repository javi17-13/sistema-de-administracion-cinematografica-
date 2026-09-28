package com.cineplex.system.controller;
 
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import com.cineplex.system.model.Pelicula;
import com.cineplex.system.model.ResultadoOperacion;
import com.cineplex.system.service.PeliculaService;
import com.cineplex.system.utils.AlertInformation;
import com.cineplex.system.utils.Roles;
import com.cineplex.system.utils.Session;
import com.cineplex.system.utils.Validations;
import com.cineplex.system.utils.ViewFactory;
 
/**
* Pantalla de "Registrar pelicula" -- y tambien la edicion (US-03),
* porque el formulario es EXACTAMENTE el mismo; lo unico que cambia es
* si al final se llama a registrar(...) o a editar(...).
*
* Como saber en que modo abrir: antes de navegar hacia aca, la pantalla
* de cartelera llama a prepararEdicion(pelicula). Si nadie lo llamo, se
* abre en modo registro (formulario vacio).
*/
public class RegistrarPeliculaController implements Initializable {
 
    /**
     * Pelicula que se va a editar. Es static porque el FXMLLoader crea
     * el controlador el mismo (no podemos pasarle parametros al
     * constructor), asi que se deja aqui ANTES de cargar la vista.
     */
    private static Pelicula peliculaEnEdicion;
 
    @FXML
    private Label lblTituloPantalla;
    @FXML
    private TextField txtTitulo;
    @FXML
    private ComboBox<String> cmbGenero;
    @FXML
    private ComboBox<String> cmbCategoria;
    @FXML
    private TextField txtDuracion;
    @FXML
    private TextField txtDirector;
    @FXML
    private TextField txtPoster;
    @FXML
    private Button btnGuardar;
 
    private final PeliculaService peliculaService = new PeliculaService();
    private final AlertInformation alertInfo = new AlertInformation();
    private final Validations validate = new Validations();
 
    /** La llama CarteleraController justo antes de abrir esta pantalla en modo edicion. */
    public static void prepararEdicion(Pelicula pelicula) {
        peliculaEnEdicion = pelicula;
    }
 
    /** La llama CarteleraController antes de abrirla en modo registro, para limpiar el modo anterior. */
    public static void prepararRegistro() {
        peliculaEnEdicion = null;
    }
 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String rol = Session.getRolActual();
 
        // Segunda barrera: registrar (US-02) lo hacen Administrador y
        // Administrador de Cine. El boton del Home ya lo bloquea, esto
        // cubre cualquier otra via de acceso (ej. Gerente).
        if (!Roles.puedeRegistrarPelicula(rol)) {
            alertInfo.viewAlert("WARNING", "ACCESO DENEGADO", "MÓDULO EXCLUSIVO",
                    "Solo el Administrador o el Administrador de Cine pueden registrar películas.");
            peliculaEnEdicion = null;
            new ViewFactory().viewHome();
            return;
        }
 
        // Editar una pelicula EXISTENTE es parte de la Cartelera (US-01),
        // exclusiva del Administrador de Cine. El Administrador normal
        // solo debe poder llegar aqui en modo registro (formulario vacio);
        // si por alguna via quedo una edicion pendiente, se le niega.
        if (peliculaEnEdicion != null && !Roles.puedeEditarPelicula(rol)) {
            alertInfo.viewAlert("WARNING", "ACCESO DENEGADO", "MÓDULO EXCLUSIVO",
                    "Solo el Administrador de Cine puede editar una película existente.");
            peliculaEnEdicion = null;
            new ViewFactory().viewHome();
            return;
        }
 
        cmbGenero.setItems(FXCollections.observableArrayList(
                "Acción", "Animación", "Aventura", "Ciencia ficción", "Comedia",
                "Documental", "Drama", "Fantasía", "Musical", "Suspenso", "Terror"));
        cmbCategoria.setItems(FXCollections.observableArrayList(
                "A", "B", "B-15", "C", "D"));
 
        if (peliculaEnEdicion != null) {
            lblTituloPantalla.setText("EDITAR PELÍCULA");
            btnGuardar.setText("Guardar cambios");
            llenarFormulario(peliculaEnEdicion);
        }
    }
 
    private void llenarFormulario(Pelicula pelicula) {
        txtTitulo.setText(pelicula.getTitulo());
        cmbGenero.setValue(pelicula.getGenero());
        cmbCategoria.setValue(pelicula.getCategoria());
        txtDuracion.setText(String.valueOf(pelicula.getDuracion()));
        txtDirector.setText(pelicula.getDirector());
        txtPoster.setText(pelicula.getPoster());
    }
 
    @FXML
    public void onGuardar(MouseEvent event) {
        String titulo = txtTitulo.getText().trim();
        String genero = cmbGenero.getValue();
        String categoria = cmbCategoria.getValue();
        String duracionTexto = txtDuracion.getText().trim();
        String director = txtDirector.getText().trim();
        String poster = txtPoster.getText().trim();
 
        if (validate.validateTextEmpty(titulo) || genero == null || categoria == null
                || validate.validateTextEmpty(duracionTexto) || validate.validateTextEmpty(director)) {
            alertInfo.viewAlert("WARNING", "CAMPOS INCOMPLETOS", "FALTAN DATOS",
                    "Llena el título, género, categoría, duración y director.");
            return;
        }
 
        if (!validate.validateNumber(duracionTexto)) {
            alertInfo.viewAlert("WARNING", "DURACIÓN INVÁLIDA", "REVISA LA DURACIÓN",
                    "La duración debe ser un número de minutos mayor a cero.");
            return;
        }
 
        //los limites vienen del DDL: Titulo 100, Genero 70, Categoria 70, Director 100, Poster 250
        String campoLargo = "";
        if (!validate.validateTextLength(titulo, 100)) {
            campoLargo = "El TÍTULO supera los 100 caracteres.";
        } else if (!validate.validateTextLength(director, 100)) {
            campoLargo = "El DIRECTOR supera los 100 caracteres.";
        } else if (!validate.validateTextLength(poster, 250)) {
            campoLargo = "La URL del PÓSTER supera los 250 caracteres.";
        }
        if (!campoLargo.isEmpty()) {
            alertInfo.viewAlert("WARNING", "CAMPO DEMASIADO LARGO", "ERROR DE LONGITUD", campoLargo);
            return;
        }
 
        Pelicula pelicula = new Pelicula();
        pelicula.setTitulo(titulo);
        pelicula.setGenero(genero);
        pelicula.setCategoria(categoria);
        pelicula.setDuracion(Integer.parseInt(duracionTexto));
        pelicula.setDirector(director);
        pelicula.setPoster(poster);
 
        boolean esEdicion = peliculaEnEdicion != null;
        ResultadoOperacion resultado;
        if (esEdicion) {
            pelicula.setID_Pelicula(peliculaEnEdicion.getID_Pelicula());
            resultado = peliculaService.editar(pelicula);
        } else {
            resultado = peliculaService.registrar(pelicula);
        }
 
        if (resultado == ResultadoOperacion.EXITO) {
            alertInfo.viewAlert("INFORMATION",
                    esEdicion ? "PELÍCULA ACTUALIZADA" : "PELÍCULA REGISTRADA",
                    "LISTO",
                    esEdicion
                            ? "Los datos de \"" + titulo + "\" se actualizaron correctamente."
                            : "\"" + titulo + "\" se agregó a la cartelera.");
            peliculaEnEdicion = null;
            volverSegunRol();
        } else if (resultado == ResultadoOperacion.TITULO_DUPLICADO) {
            // Antes esta validacion existia en el repositorio pero
            // nadie la llamaba: se podia registrar la misma pelicula
            // dos veces sin ningun aviso.
            alertInfo.viewAlert("WARNING", "TÍTULO EN USO", "ESA PELÍCULA YA ESTÁ REGISTRADA",
                    "Ya hay una película con el título \"" + titulo + "\". Revisa la cartelera.");
        } else {
            alertInfo.viewAlert("ERROR", "NO SE PUDO GUARDAR", "ERROR AL GUARDAR",
                    "Ocurrió un error al guardar la película. Revisa la conexión a la base de datos.");
        }
    }
 
    @FXML
    public void onLimpiar(MouseEvent event) {
        txtTitulo.clear();
        cmbGenero.setValue(null);
        cmbCategoria.setValue(null);
        txtDuracion.clear();
        txtDirector.clear();
        txtPoster.clear();
    }
 
    @FXML
    public void onVolver(MouseEvent event) {
        peliculaEnEdicion = null;
        volverSegunRol();
    }
 
    /**
     * A donde volver depende del rol: el Administrador de Cine viene de
     * (y regresa a) la Cartelera, que es su modulo exclusivo. El
     * Administrador normal NO tiene acceso a la Cartelera (US-01), asi
     * que regresa al Home.
     */
    private void volverSegunRol() {
        if (Roles.puedeVerCartelera(Session.getRolActual())) {
            new ViewFactory().viewCartelera();
        } else {
            new ViewFactory().viewHome();
        }
    }
}