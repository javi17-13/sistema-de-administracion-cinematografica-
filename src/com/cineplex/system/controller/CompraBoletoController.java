package com.cineplex.system.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import com.cineplex.system.model.Funciones;
import com.cineplex.system.model.ResultadoCompra;
import com.cineplex.system.repository.FuncionRepository;
import com.cineplex.system.service.BoletoService;
import com.cineplex.system.service.ClienteService;
import com.cineplex.system.utils.AlertInformation;
import com.cineplex.system.utils.ViewFactory;

/**
 * Flujo completo de compra:
 *   1) buscar/elegir una funcion (US-09: por titulo de pelicula)
 *   2) elegir un asiento libre en el mapa
 *   3) confirmar la compra (Alert de confirmacion)
 *   4) pagar con tarjeta (Dialog propio, con validacion basica -- esto
 *      es una SIMULACION de cobro para el proyecto, no se conecta a
 *      ninguna pasarela de pago real)
 *   5) se crea el Cliente y el Boleto, y se genera el ticket con su QR
 *
 * El QR se genera con ZXing y se pinta directo en un WritableImage
 * (sin pasar por java.awt/Swing), asi que solo hace falta agregar el
 * jar "core" de ZXing al proyecto -- no el modulo javafx.swing.
 */
public class CompraBoletoController implements Initializable {

    @FXML
    private TextField txtBuscarPelicula;
    @FXML
    private TableView<Funciones> tablaFunciones;
    @FXML
    private TableColumn<Funciones, String> colPelicula;
    @FXML
    private TableColumn<Funciones, String> colSala;
    @FXML
    private TableColumn<Funciones, String> colFecha;
    @FXML
    private TableColumn<Funciones, String> colHora;
    @FXML
    private TableColumn<Funciones, String> colPrecio;

    @FXML
    private VBox panelSeleccion;
    @FXML
    private Label lblSinFuncion;
    @FXML
    private VBox boxDetalleFuncion;
    @FXML
    private Label lblTituloFuncion;
    @FXML
    private Label lblInfoFuncion;
    @FXML
    private GridPane gridAsientos;
    @FXML
    private TextField txtNombreCliente;
    @FXML
    private TextField txtCorreoCliente;
    @FXML
    private Label lblAsientoSeleccionado;
    @FXML
    private Label lblPrecioTotal;
    @FXML
    private Button btnComprar;

    @FXML
    private VBox panelTicket;
    @FXML
    private ImageView imgQR;
    @FXML
    private Text txtResumenTicket;

    private final FuncionRepository funcionRepo = new FuncionRepository();
    private final ClienteService clienteService = new ClienteService();
    private final BoletoService boletoService = new BoletoService();
    private final AlertInformation alertInfo = new AlertInformation();

    private final Map<String, Button> botonesPorAsiento = new HashMap<>();
    private Funciones funcionSeleccionada;
    private String asientoSeleccionado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colPelicula.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTitulo()));
        colSala.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNombreSala()));
        colFecha.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getFecha())));
        colHora.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getHora())));
        colPrecio.setCellValueFactory(d -> new SimpleStringProperty("Q" + d.getValue().getPrecio()));

        tablaFunciones.setPlaceholder(new Label("Busca una película para ver sus funciones."));
        tablaFunciones.getSelectionModel().selectedItemProperty()
                .addListener((obs, anterior, seleccionada) -> {
                    if (seleccionada != null) {
                        mostrarDetalleFuncion(seleccionada);
                    }
                });

        cargarTodasLasFunciones();
        mostrarPanelSeleccion();
    }

    private void cargarTodasLasFunciones() {
        List<Funciones> funciones = funcionRepo.obtenerTodas();
        tablaFunciones.setItems(FXCollections.observableArrayList(funciones));
    }

    /** US-09: busqueda basica por titulo de pelicula. Campo vacio = mostrar todas otra vez. */
    @FXML
    public void onBuscar(MouseEvent event) {
        String titulo = txtBuscarPelicula.getText();
        if (titulo == null || titulo.isBlank()) {
            cargarTodasLasFunciones();
            return;
        }
        List<Funciones> funciones = funcionRepo.buscarPorTitulo(titulo.trim());
        tablaFunciones.setItems(FXCollections.observableArrayList(funciones));
        if (funciones.isEmpty()) {
            alertInfo.viewAlert("INFORMATION", "SIN RESULTADOS", "No se encontraron funciones",
                    "Ninguna película coincide con \"" + titulo.trim() + "\".");
        }
    }

    private void mostrarDetalleFuncion(Funciones funcion) {
        funcionSeleccionada = funcion;
        asientoSeleccionado = null;

        lblSinFuncion.setVisible(false);
        lblSinFuncion.setManaged(false);
        boxDetalleFuncion.setVisible(true);
        boxDetalleFuncion.setManaged(true);

        lblTituloFuncion.setText(funcion.getTitulo());
        lblInfoFuncion.setText(funcion.getNombreSala() + "  ·  " + funcion.getFecha() + " " + funcion.getHora()
                + "  ·  " + funcion.getDuracion() + " min  ·  " + funcion.getCategoria());
        lblAsientoSeleccionado.setText("—");
        lblPrecioTotal.setText("Q" + funcion.getPrecio());

        dibujarMapaAsientos(funcion);
    }

    /** Los asientos se generan al vuelo (Fila+Columna, ej. "C4") a partir de Filas/Columnas de la Sala. */
    private void dibujarMapaAsientos(Funciones funcion) {
        gridAsientos.getChildren().clear();
        botonesPorAsiento.clear();

        Set<String> ocupados = new HashSet<>(funcionRepo.obtenerAsientosOcupados(funcion.getID_Funcion()));

        for (int fila = 0; fila < funcion.getFilas(); fila++) {
            char letraFila = (char) ('A' + fila);
            for (int columna = 0; columna < funcion.getColumnas(); columna++) {
                String etiqueta = letraFila + String.valueOf(columna + 1);

                Button boton = new Button(etiqueta);
                boolean ocupado = ocupados.contains(etiqueta);
                boton.getStyleClass().add(ocupado ? "asiento-ocupado" : "asiento-libre");
                boton.setDisable(ocupado);
                if (!ocupado) {
                    boton.setOnAction(e -> seleccionarAsiento(etiqueta, boton));
                }

                botonesPorAsiento.put(etiqueta, boton);
                gridAsientos.add(boton, columna, fila);
            }
        }
    }

    private void seleccionarAsiento(String etiqueta, Button boton) {
        if (asientoSeleccionado != null) {
            Button anterior = botonesPorAsiento.get(asientoSeleccionado);
            if (anterior != null) {
                anterior.getStyleClass().remove("asiento-seleccionado");
                anterior.getStyleClass().add("asiento-libre");
            }
        }
        asientoSeleccionado = etiqueta;
        boton.getStyleClass().remove("asiento-libre");
        boton.getStyleClass().add("asiento-seleccionado");
        lblAsientoSeleccionado.setText(etiqueta);
    }

    @FXML
    public void onComprar(MouseEvent event) {
        if (funcionSeleccionada == null || asientoSeleccionado == null) {
            alertInfo.viewAlert("WARNING", "FALTAN DATOS", "Selecciona función y asiento",
                    "Elige una función de la tabla y un asiento libre antes de comprar.");
            return;
        }
        String nombre = txtNombreCliente.getText() == null ? "" : txtNombreCliente.getText().trim();
        if (nombre.isBlank()) {
            alertInfo.viewAlert("WARNING", "FALTA EL NOMBRE", "Nombre requerido",
                    "Escribe el nombre completo del cliente.");
            return;
        }
        String correo = txtCorreoCliente.getText() == null ? "" : txtCorreoCliente.getText().trim();

        if (!confirmarCompra(funcionSeleccionada, asientoSeleccionado)) {
            return;
        }
        Optional<DatosTarjeta> tarjeta = pedirDatosTarjeta();
        if (tarjeta.isEmpty()) {
            return; // el cliente cancelo el pago, no se crea nada
        }

        try {
            int idCliente = clienteService.crear(nombre, correo);
            String contenidoQR = construirContenidoQR(funcionSeleccionada, nombre, asientoSeleccionado);

            ResultadoCompra resultado = boletoService.comprar(
                    funcionSeleccionada.getID_Funcion(), idCliente, asientoSeleccionado, contenidoQR);

            switch (resultado) {
                case EXITO -> mostrarTicket(funcionSeleccionada, nombre, asientoSeleccionado,
                        contenidoQR, tarjeta.get());
                case ASIENTO_OCUPADO -> {
                    alertInfo.viewAlert("WARNING", "ASIENTO NO DISPONIBLE", "Alguien más lo compró primero",
                            "Elige otro asiento -- este ya se vendió mientras pagabas.");
                    dibujarMapaAsientos(funcionSeleccionada);
                }
                case ERROR -> alertInfo.viewAlert("ERROR", "ERROR", "No se pudo completar la compra",
                        "Ocurrió un error al procesar tu compra. Intenta de nuevo.");
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            alertInfo.viewAlert("ERROR", "ERROR", "No se pudo completar la compra",
                    "Ocurrió un error al procesar tu compra. Intenta de nuevo.");
        }
    }

    private boolean confirmarCompra(Funciones funcion, String asiento) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("CONFIRMAR COMPRA");
        confirmacion.setHeaderText("¿Confirmas la compra de este boleto?");
        confirmacion.setContentText(funcion.getTitulo()
                + "\n" + funcion.getNombreSala() + "  ·  " + funcion.getFecha() + " " + funcion.getHora()
                + "\nAsiento: " + asiento
                + "\nTotal a pagar: Q" + funcion.getPrecio());
        Optional<ButtonType> respuesta = confirmacion.showAndWait();
        return respuesta.isPresent() && respuesta.get() == ButtonType.OK;
    }

    /**
     * Simulacion de cobro con tarjeta: pide numero/vencimiento/CVV/nombre
     * y no deja cerrar el dialogo con "Pagar" hasta que el formato sea
     * valido (16 digitos, MM/AA, CVV de 3-4 digitos). No se conecta a
     * ninguna pasarela real -- es para que la UI se sienta como una
     * compra real, tal como pediste.
     */
    private Optional<DatosTarjeta> pedirDatosTarjeta() {
        Dialog<DatosTarjeta> dialog = new Dialog<>();
        dialog.setTitle("PAGO CON TARJETA");
        dialog.setHeaderText("Ingresa los datos de tu tarjeta");

        ButtonType botonPagar = new ButtonType("Pagar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(botonPagar, ButtonType.CANCEL);

        TextField txtNumero = new TextField();
        txtNumero.setPromptText("1234 5678 9012 3456");
        TextField txtVencimiento = new TextField();
        txtVencimiento.setPromptText("MM/AA");
        PasswordField txtCvv = new PasswordField();
        txtCvv.setPromptText("CVV");
        TextField txtNombreTarjeta = new TextField();
        txtNombreTarjeta.setPromptText("Nombre como aparece en la tarjeta");

        Label lblError = new Label();
        lblError.setStyle("-fx-text-fill: #ff6b6b; -fx-font-size: 11px;");
        lblError.setWrapText(true);
        lblError.setMaxWidth(260);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.addRow(0, new Label("Número de tarjeta:"), txtNumero);
        grid.addRow(1, new Label("Vencimiento:"), txtVencimiento);
        grid.addRow(2, new Label("CVV:"), txtCvv);
        grid.addRow(3, new Label("Nombre en la tarjeta:"), txtNombreTarjeta);
        grid.add(lblError, 1, 4);
        dialog.getDialogPane().setContent(grid);

        Button botonPagarNodo = (Button) dialog.getDialogPane().lookupButton(botonPagar);
        botonPagarNodo.addEventFilter(ActionEvent.ACTION, event -> {
            String error = validarTarjeta(txtNumero.getText(), txtVencimiento.getText(),
                    txtCvv.getText(), txtNombreTarjeta.getText());
            if (error != null) {
                lblError.setText(error);
                event.consume(); // no deja cerrar el dialogo si hay error
            }
        });

        dialog.setResultConverter(boton -> {
            if (boton == botonPagar) {
                String soloDigitos = txtNumero.getText().replaceAll("[^0-9]", "");
                String ultimos4 = soloDigitos.substring(soloDigitos.length() - 4);
                return new DatosTarjeta(ultimos4, txtNombreTarjeta.getText().trim());
            }
            return null;
        });

        return dialog.showAndWait();
    }

    private String validarTarjeta(String numero, String vencimiento, String cvv, String nombre) {
        String soloDigitos = numero == null ? "" : numero.replaceAll("[^0-9]", "");
        if (soloDigitos.length() != 16) {
            return "El número de tarjeta debe tener 16 dígitos.";
        }
        if (vencimiento == null || !vencimiento.trim().matches("(0[1-9]|1[0-2])/[0-9]{2}")) {
            return "El vencimiento debe tener el formato MM/AA.";
        }
        if (cvv == null || !cvv.trim().matches("[0-9]{3,4}")) {
            return "El CVV debe tener 3 o 4 dígitos.";
        }
        if (nombre == null || nombre.isBlank()) {
            return "Escribe el nombre como aparece en la tarjeta.";
        }
        return null; // sin errores
    }

    /** Lo unico del pago que nos interesa conservar para mostrarlo en el ticket. */
    private record DatosTarjeta(String ultimos4Digitos, String nombreTitular) {
    }

    /**
     * El contenido del QR es SOLO la info del boleto (lo que pediste:
     * funcion, cliente, fecha, etc.) -- los datos de la tarjeta nunca
     * entran aqui.
     */
    private String construirContenidoQR(Funciones funcion, String nombreCliente, String asiento) {
        return "CINEPLEX\n"
                + "Pelicula: " + funcion.getTitulo() + "\n"
                + "Sala: " + funcion.getNombreSala() + "\n"
                + "Fecha: " + funcion.getFecha() + " " + funcion.getHora() + "\n"
                + "Asiento: " + asiento + "\n"
                + "Cliente: " + nombreCliente;
    }

    private void mostrarTicket(Funciones funcion, String nombreCliente, String asiento,
                                String contenidoQR, DatosTarjeta tarjeta) {
        panelSeleccion.setVisible(false);
        panelSeleccion.setManaged(false);
        panelTicket.setVisible(true);
        panelTicket.setManaged(true);

        imgQR.setImage(generarImagenQR(contenidoQR, 220));

        txtResumenTicket.setText(
                funcion.getTitulo() + "\n"
                + funcion.getNombreSala() + "  ·  " + funcion.getFecha() + " " + funcion.getHora() + "\n"
                + "Asiento: " + asiento + "\n"
                + "Cliente: " + nombreCliente + "\n"
                + "Total pagado: Q" + funcion.getPrecio()
                + "  (tarjeta terminada en " + tarjeta.ultimos4Digitos() + ")");
    }

    /**
     * Genera el QR con ZXing y lo pinta pixel por pixel en un
     * WritableImage -- asi evitamos pasar por BufferedImage/Swing y
     * solo se necesita el jar "core" de ZXing, nada de javafx.swing.
     */
    private Image generarImagenQR(String contenido, int tamano) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matriz = writer.encode(contenido, BarcodeFormat.QR_CODE, tamano, tamano);

            WritableImage imagen = new WritableImage(tamano, tamano);
            PixelWriter escritor = imagen.getPixelWriter();
            for (int y = 0; y < tamano; y++) {
                for (int x = 0; x < tamano; x++) {
                    escritor.setColor(x, y, matriz.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            return imagen;
        } catch (WriterException e) {
            System.out.println("Error al generar el QR: " + e.getMessage());
            return null;
        }
    }

    @FXML
    public void onNuevaCompra(MouseEvent event) {
        funcionSeleccionada = null;
        asientoSeleccionado = null;
        txtNombreCliente.clear();
        txtCorreoCliente.clear();
        txtBuscarPelicula.clear();
        cargarTodasLasFunciones();
        mostrarPanelSeleccion();
    }

    private void mostrarPanelSeleccion() {
        panelTicket.setVisible(false);
        panelTicket.setManaged(false);
        panelSeleccion.setVisible(true);
        panelSeleccion.setManaged(true);
        lblSinFuncion.setVisible(true);
        lblSinFuncion.setManaged(true);
        boxDetalleFuncion.setVisible(false);
        boxDetalleFuncion.setManaged(false);
    }

    @FXML
    public void onVolver(MouseEvent event) {
        new ViewFactory().viewHome();
    }
}
