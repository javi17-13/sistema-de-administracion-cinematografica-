package com.cineplex.system.controller;
 
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import com.cineplex.system.model.Pelicula;
import com.cineplex.system.model.ResultadoOperacion;
import com.cineplex.system.service.PeliculaService;
import com.cineplex.system.utils.AlertInformation;
import com.cineplex.system.utils.Roles;
import com.cineplex.system.utils.Session;
import com.cineplex.system.utils.ViewFactory;
 
/**
* US-05 (listado de cartelera) y US-06 (detalle de una pelicula) en una
* sola pantalla: la tabla de la izquierda lista todas las peliculas y el
* panel de la derecha muestra el detalle completo de la seleccionada.
*
* Tambien es el punto de entrada a las otras dos pantallas de pelicula:
* "Nueva pelicula" abre el formulario en modo registro y "Editar" lo
* abre en modo edicion con los datos ya cargados.
*/
public class CarteleraController implements Initializable {
 
    @FXML
    private TableView<Pelicula> tablaPeliculas;
    @FXML
    private TableColumn<Pelicula, String> colTitulo;
    @FXML
    private TableColumn<Pelicula, String> colGenero;
    @FXML
    private TableColumn<Pelicula, String> colDuracion;
    @FXML
    private TableColumn<Pelicula, String> colCategoria;
    @FXML
    private TableColumn<Pelicula, String> colDirector;
    @FXML
    private TextField txtBuscar;
 
    @FXML
    private Label lblSinSeleccion;
    @FXML
    private ImageView imgPoster;
    @FXML
    private Label lblTitulo;
    @FXML
    private Label lblGenero;
    @FXML
    private Label lblDuracion;
    @FXML
    private Label lblCategoria;
    @FXML
    private Label lblDirector;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
 
    private final PeliculaService peliculaService = new PeliculaService();
    private final AlertInformation alertInfo = new AlertInformation();
 
    /** Copia completa de la cartelera; el buscador filtra sobre esta lista sin volver a consultar la BD. */
    private List<Pelicula> carteleraCompleta = new ArrayList<>();
 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Segunda barrera (US-01): la Cartelera es EXCLUSIVA del
        // Administrador de Cine. El boton en el Home ya la bloquea, pero
        // esto evita que alguien entre por otra via (ej. onVolver desde
        // Registrar Pelicula, o un cambio futuro en el codigo).
        if (!Roles.puedeVerCartelera(Session.getRolActual())) {
            alertInfo.viewAlert("WARNING", "ACCESO DENEGADO", "MÓDULO EXCLUSIVO",
                    "Solo el Administrador de Cine puede acceder a la cartelera.");
            new ViewFactory().viewHome();
            return;
        }
 
        colTitulo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTitulo()));
        colGenero.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getGenero()));
        colDuracion.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDuracion() + " min"));
        colCategoria.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCategoria()));
        colDirector.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDirector()));
 
        tablaPeliculas.setPlaceholder(new Label("No hay películas registradas todavía."));
 
        //US-06: al seleccionar una fila se llena el panel de detalle
        tablaPeliculas.getSelectionModel().selectedItemProperty()
                .addListener((obs, anterior, seleccionada) -> mostrarDetalle(seleccionada));
 
        txtBuscar.textProperty().addListener((obs, anterior, texto) -> filtrar(texto));
 
        mostrarDetalle(null);
        cargarCartelera();
    }
 
    private void cargarCartelera() {
        carteleraCompleta = peliculaService.obtenerCartelera();
        tablaPeliculas.setItems(FXCollections.observableArrayList(carteleraCompleta));
    }
 
    private void filtrar(String texto) {
        if (texto == null || texto.isBlank()) {
            tablaPeliculas.setItems(FXCollections.observableArrayList(carteleraCompleta));
            return;
        }
 
        String busqueda = texto.toLowerCase().trim();
        List<Pelicula> filtradas = new ArrayList<>();
 
        for (Pelicula pelicula : carteleraCompleta) {
            boolean coincideTitulo = pelicula.getTitulo().toLowerCase().contains(busqueda);
            boolean coincideDirector = pelicula.getDirector().toLowerCase().contains(busqueda);
            if (coincideTitulo || coincideDirector) {
                filtradas.add(pelicula);
            }
        }
 
        tablaPeliculas.setItems(FXCollections.observableArrayList(filtradas));
    }
 
    /** US-06: detalle completo de la pelicula seleccionada (o panel vacio si no hay ninguna). */
    private void mostrarDetalle(Pelicula pelicula) {
        boolean haySeleccion = pelicula != null;
 
        lblSinSeleccion.setVisible(!haySeleccion);
        lblSinSeleccion.setManaged(!haySeleccion);
 
        Node[] nodosDetalle = {imgPoster, lblTitulo, lblGenero, lblDuracion, lblCategoria, lblDirector,
                btnEditar, btnEliminar};
        for (Node nodo : nodosDetalle) {
            nodo.setVisible(haySeleccion);
            nodo.setManaged(haySeleccion);
        }
 
        if (!haySeleccion) {
            return;
        }
 
        lblTitulo.setText(pelicula.getTitulo());
        lblGenero.setText("Género: " + pelicula.getGenero());
        lblDuracion.setText("Duración: " + pelicula.getDuracion() + " minutos");
        lblCategoria.setText("Categoría: " + pelicula.getCategoria());
        lblDirector.setText("Director: " + pelicula.getDirector());
        cargarPoster(pelicula.getPoster());
    }
 
    /**
     * El poster es opcional y la URL puede estar rota o el equipo sin
     * internet, asi que si falla simplemente no se muestra imagen --
     * nunca debe tronar la pantalla por un poster.
     */
    private void cargarPoster(String urlPoster) {
        if (urlPoster == null || urlPoster.isBlank()) {
            imgPoster.setImage(null);
            return;
        }
        try {
            imgPoster.setImage(new Image(urlPoster, true));
        } catch (Exception e) {
            imgPoster.setImage(null);
        }
    }
 
    @FXML
    public void onNuevaPelicula(MouseEvent event) {
        RegistrarPeliculaController.prepararRegistro();
        new ViewFactory().viewRegistrarPelicula();
    }
 
    @FXML
    public void onEditar(MouseEvent event) {
        Pelicula seleccionada = tablaPeliculas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            alertInfo.viewAlert("WARNING", "SIN SELECCIÓN", "NINGUNA PELÍCULA SELECCIONADA",
                    "Selecciona una película de la tabla primero.");
            return;
        }
        RegistrarPeliculaController.prepararEdicion(seleccionada);
        new ViewFactory().viewRegistrarPelicula();
    }
 
    @FXML
    public void onEliminar(MouseEvent event) {
        Pelicula seleccionada = tablaPeliculas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            alertInfo.viewAlert("WARNING", "SIN SELECCIÓN", "NINGUNA PELÍCULA SELECCIONADA",
                    "Selecciona una película de la tabla primero.");
            return;
        }
 
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("ELIMINAR PELÍCULA");
        confirmacion.setHeaderText("¿Eliminar \"" + seleccionada.getTitulo() + "\"?");
        confirmacion.setContentText("Esta acción no se puede deshacer.");
        Optional<ButtonType> respuesta = confirmacion.showAndWait();
 
        if (respuesta.isEmpty() || respuesta.get() != ButtonType.OK) {
            return;
        }
 
        ResultadoOperacion resultado = peliculaService.eliminar(seleccionada.getID_Pelicula());
        if (resultado == ResultadoOperacion.EXITO) {
            alertInfo.viewAlert("INFORMATION", "PELÍCULA ELIMINADA", "LISTO",
                    "\"" + seleccionada.getTitulo() + "\" se quitó de la cartelera.");
            cargarCartelera();
            mostrarDetalle(null);
        } else if (resultado == ResultadoOperacion.REGISTRO_EN_USO) {
            alertInfo.viewAlert("WARNING", "NO SE PUEDE ELIMINAR", "PELÍCULA CON FUNCIONES PROGRAMADAS",
                    "No se puede eliminar \"" + seleccionada.getTitulo()
                    + "\" porque tiene funciones o boletos asociados en el sistema.");
        } else {
            alertInfo.viewAlert("ERROR", "NO SE PUDO ELIMINAR", "ERROR AL ELIMINAR",
                    "Ocurrió un error al eliminar la película.");
        }
    }
 
    @FXML
    public void onVolver(MouseEvent event) {
        new ViewFactory().viewHome();
    }
}
