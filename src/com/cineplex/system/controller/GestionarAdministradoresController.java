package com.cineplex.system.controller;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import com.cineplex.system.model.ResultadoOperacion;
import com.cineplex.system.model.Usuarios;
import com.cineplex.system.service.UsuarioService;
import com.cineplex.system.utils.AlertInformation;
import com.cineplex.system.utils.Session;
import com.cineplex.system.utils.Validations;
import com.cineplex.system.utils.ViewFactory;

/**
 * Pantalla de "Gestionar Administradores": lista las cuentas y permite
 * crear, editar y dar de baja. El formulario de la derecha sirve para
 * las dos cosas -- si hay una fila seleccionada pasa a modo edicion, y
 * con "Limpiar" vuelve a modo creacion.
 */
public class GestionarAdministradoresController implements Initializable {

    @FXML
    private TableView<Usuarios> tablaUsuarios;
    @FXML
    private TableColumn<Usuarios, String> colId;
    @FXML
    private TableColumn<Usuarios, String> colNombre;
    @FXML
    private TableColumn<Usuarios, String> colUsuario;
    @FXML
    private TableColumn<Usuarios, String> colRol;

    @FXML
    private Label lblTituloFormulario;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField pwdClave;
    @FXML
    private ComboBox<String> cmbRol;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnDarDeBaja;

    private final UsuarioService usuarioService = new UsuarioService();
    private final AlertInformation alertInfo = new AlertInformation();
    private final Validations validate = new Validations();

    /** null = modo creacion; con valor = se esta editando esa cuenta. */
    private Usuarios usuarioEnEdicion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getID_Usuario())));
        colNombre.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNombre()));
        colUsuario.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getUsuario()));
        colRol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getRol()));

        tablaUsuarios.setPlaceholder(new Label("No hay cuentas registradas todavía."));
        cmbRol.setItems(FXCollections.observableArrayList("Administrador", "Empleado"));

        tablaUsuarios.getSelectionModel().selectedItemProperty()
                .addListener((obs, anterior, seleccionado) -> pasarAModoEdicion(seleccionado));

        cargarTabla();
    }

    private void cargarTabla() {
        tablaUsuarios.setItems(FXCollections.observableArrayList(usuarioService.obtenerTodos()));
    }

    private void pasarAModoEdicion(Usuarios usuario) {
        usuarioEnEdicion = usuario;

        if (usuario == null) {
            lblTituloFormulario.setText("NUEVA CUENTA");
            btnGuardar.setText("Crear cuenta");
            limpiarFormulario();
            return;
        }

        lblTituloFormulario.setText("EDITAR CUENTA");
        btnGuardar.setText("Guardar cambios");
        txtNombre.setText(usuario.getNombre());
        txtUsuario.setText(usuario.getUsuario());
        pwdClave.clear(); //vacia a proposito: solo se cambia si escriben una nueva
        cmbRol.setValue(usuario.getRol());
    }

    private void limpiarFormulario() {
        txtNombre.clear();
        txtUsuario.clear();
        pwdClave.clear();
        cmbRol.setValue(null);
    }

    @FXML
    public void onGuardar(MouseEvent event) {
        String nombre = txtNombre.getText().trim();
        String usuarioTexto = txtUsuario.getText().trim();
        String clave = pwdClave.getText().trim();
        String rol = cmbRol.getValue();

        boolean esEdicion = usuarioEnEdicion != null;

        if (validate.validateTextEmpty(nombre) || validate.validateTextEmpty(usuarioTexto) || rol == null) {
            alertInfo.viewAlert("WARNING", "CAMPOS INCOMPLETOS", "FALTAN DATOS",
                    "Llena el nombre, el usuario y el rol.");
            return;
        }

        //al crear la clave es obligatoria; al editar, vacia significa "dejala igual"
        if (!esEdicion && validate.validateTextEmpty(clave)) {
            alertInfo.viewAlert("WARNING", "FALTA LA CONTRASEÑA", "CONTRASEÑA REQUERIDA",
                    "Escribe una contraseña para la cuenta nueva.");
            return;
        }

        //limites del DDL: Nombre 100, Usuario 100, Clave 20, Rol 50
        String campoLargo = "";
        if (!validate.validateTextLength(nombre, 100)) {
            campoLargo = "El NOMBRE supera los 100 caracteres.";
        } else if (!validate.validateTextLength(usuarioTexto, 100)) {
            campoLargo = "El USUARIO supera los 100 caracteres.";
        } else if (!clave.isEmpty() && !validate.validateTextLength(clave, 20)) {
            campoLargo = "La CONTRASEÑA supera los 20 caracteres.";
        }
        if (!campoLargo.isEmpty()) {
            alertInfo.viewAlert("WARNING", "CAMPO DEMASIADO LARGO", "ERROR DE LONGITUD", campoLargo);
            return;
        }

        Usuarios usuario = new Usuarios();
        usuario.setNombre(nombre);
        usuario.setUsuario(usuarioTexto);
        usuario.setRol(rol);
        //sp_Editar_Usuarios siempre pide la clave, asi que si la dejaron
        //vacia se reenvia la que ya tenia guardada
        usuario.setClave(clave.isEmpty() && esEdicion ? usuarioEnEdicion.getClave() : clave);

        ResultadoOperacion resultado;
        if (esEdicion) {
            usuario.setID_Usuario(usuarioEnEdicion.getID_Usuario());
            resultado = usuarioService.editar(usuario);
        } else {
            resultado = usuarioService.crear(usuario);
        }

        switch (resultado) {
            case EXITO -> {
                alertInfo.viewAlert("INFORMATION",
                        esEdicion ? "CUENTA ACTUALIZADA" : "CUENTA CREADA", "LISTO",
                        esEdicion
                                ? "Los datos de \"" + usuarioTexto + "\" se actualizaron."
                                : "La cuenta \"" + usuarioTexto + "\" se creó correctamente.");
                tablaUsuarios.getSelectionModel().clearSelection();
                pasarAModoEdicion(null);
                cargarTabla();
            }
            case USUARIO_DUPLICADO -> alertInfo.viewAlert("WARNING", "USUARIO EN USO",
                    "ESE NOMBRE DE USUARIO YA EXISTE",
                    "Ya hay una cuenta con el usuario \"" + usuarioTexto + "\". Elige otro.");
            case ERROR -> alertInfo.viewAlert("ERROR", "NO SE PUDO GUARDAR", "ERROR AL GUARDAR",
                    "Ocurrió un error al guardar la cuenta. Revisa la conexión a la base de datos.");
        }
    }

    @FXML
    public void onDarDeBaja(MouseEvent event) {
        Usuarios seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            alertInfo.viewAlert("WARNING", "SIN SELECCIÓN", "NINGUNA CUENTA SELECCIONADA",
                    "Selecciona una cuenta de la tabla primero.");
            return;
        }

        //evita que alguien se deje a si mismo fuera del sistema
        if (seleccionado.getUsuario().equals(Session.getUsuarioActual())) {
            alertInfo.viewAlert("WARNING", "NO PERMITIDO", "ES TU PROPIA CUENTA",
                    "No puedes dar de baja la cuenta con la que iniciaste sesión.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("DAR DE BAJA");
        confirmacion.setHeaderText("¿Dar de baja la cuenta \"" + seleccionado.getUsuario() + "\"?");
        confirmacion.setContentText("Esa persona ya no podrá iniciar sesión. Esta acción no se puede deshacer.");
        Optional<ButtonType> respuesta = confirmacion.showAndWait();

        if (respuesta.isEmpty() || respuesta.get() != ButtonType.OK) {
            return;
        }

        ResultadoOperacion resultado = usuarioService.darDeBaja(seleccionado.getID_Usuario());
        if (resultado == ResultadoOperacion.EXITO) {
            alertInfo.viewAlert("INFORMATION", "CUENTA DADA DE BAJA", "LISTO",
                    "La cuenta \"" + seleccionado.getUsuario() + "\" se dio de baja.");
            tablaUsuarios.getSelectionModel().clearSelection();
            pasarAModoEdicion(null);
            cargarTabla();
        } else {
            alertInfo.viewAlert("ERROR", "NO SE PUDO DAR DE BAJA", "ERROR AL ELIMINAR",
                    "Ocurrió un error al dar de baja la cuenta.");
        }
    }

    @FXML
    public void onCancelarEdicion(MouseEvent event) {
        tablaUsuarios.getSelectionModel().clearSelection();
        pasarAModoEdicion(null);
    }

    @FXML
    public void onRefrescar(MouseEvent event) {
        cargarTabla();
    }

    @FXML
    public void onVolver(MouseEvent event) {
        new ViewFactory().viewHome();
    }
}
