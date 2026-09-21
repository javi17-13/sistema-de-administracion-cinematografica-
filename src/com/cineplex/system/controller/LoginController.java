package com.cineplex.system.controller;

import com.cineplex.system.model.Usuario;
import com.cineplex.system.repository.UsuarioRepository;
import com.cineplex.system.utils.AlertInformation;
import com.cineplex.system.utils.Session;
import com.cineplex.system.utils.ViewFactory;
import java.sql.SQLException;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class LoginController {

    // ---------- Componentes del FXML (deben coincidir con los fx:id de Login.fxml) ----------
    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private ComboBox<String> cmbTipoUsuario;

    @FXML
    private Button btnIniciarSesion;

    @FXML
    private AnchorPane panelLogin;

    // ---------- Dependencias ----------
    private final UsuarioRepository usuarioRepository = new UsuarioRepository();
    private final AlertInformation alertInfo = new AlertInformation();

    // ---------- Inicialización ----------
    @FXML
    public void initialize() {
        cmbTipoUsuario.getItems().addAll(
                "Gerente",
                "Administrador",
                "Administrador de Cine"
        );

        crearBolitas();
    }

    // ---------- Burbujas animadas (de la rama 2026467) ----------
    // OJO: los valores están copiados de la captura; compáralos con tu código original.
    private void crearBolitas() {
        crearBolita(45, "#E5A8A8", 80, 100, 40, 60, 4);
        crearBolita(35, "#9B9BB4", 300, 150, -40, -50, 5);
    }

    private void crearBolita(
            double radio,
            String color,
            double x,
            double y,
            double movimientoX,
            double movimientoY,
            double segundos) {

        Circle bolita = new Circle(radio);

        bolita.setFill(Color.web(color, 0.35));

        bolita.setLayoutX(x);
        bolita.setLayoutY(y);

        panelLogin.getChildren().add(bolita);

        TranslateTransition movimiento = new TranslateTransition(
                Duration.seconds(segundos),
                bolita
        );

        movimiento.setByX(movimientoX);
        movimiento.setByY(movimientoY);
        movimiento.setCycleCount(TranslateTransition.INDEFINITE);
        movimiento.setAutoReverse(true);
        movimiento.play();
    }

    // ---------- Inicio de sesión (flujo de Gustavo, ahora contra la base de datos) ----------
    @FXML
    private void iniciarSesion() {
        String usuario = txtUsuario.getText().trim();
        String password = txtPassword.getText().trim();
        String rol = cmbTipoUsuario.getValue();

        // 1. Validar que no falten datos
        if (usuario.isEmpty() || password.isEmpty() || rol == null) {
            alertInfo.viewAlert("WARNING", "CAMPOS INCOMPLETOS", "FALTAN DATOS",
                    "Ingresa tu usuario, tu contraseña y el tipo de usuario.");
            return;
        }

        try {
            // 2. Consultar la base de datos
            Usuario usuarioEncontrado = usuarioRepository.login(usuario, password, rol);

            if (usuarioEncontrado == null) {
                alertInfo.viewAlert("WARNING", "ACCESO DENEGADO", "CREDENCIALES INCORRECTAS",
                        "El usuario, la contraseña o el tipo de usuario no son correctos.");
                return;
            }

            // 3. Guardar la sesión y abrir la pantalla principal
            Session.setUsuarioActual(usuario);

            ViewFactory viewFactory = new ViewFactory();
            viewFactory.viewHome();

        } catch (SQLException e) {
            e.printStackTrace();
            alertInfo.viewAlert("WARNING", "ERROR DE CONEXIÓN", "BASE DE DATOS",
                    "No se pudo consultar la base de datos. Intenta de nuevo.");
        }
    }
}
