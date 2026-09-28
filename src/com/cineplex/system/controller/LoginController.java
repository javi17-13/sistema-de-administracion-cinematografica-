package com.cineplex.system.controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import com.cineplex.system.model.Usuario;
import com.cineplex.system.repository.UsuarioRepository;
import com.cineplex.system.utils.AlertInformation;
import com.cineplex.system.utils.Session;
import com.cineplex.system.utils.SesionPreferencias;
import com.cineplex.system.utils.ViewFactory;

public class LoginController implements Initializable {

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private CheckBox chkRecordarSesion;

    @FXML
    private ComboBox<String> cmbTipoUsuario;

    @FXML
    private Button btnIniciarSesion;

    @FXML
    private AnchorPane panelLogin;

    private final UsuarioRepository usuarioRepository = new UsuarioRepository();
    private final AlertInformation alertInfo = new AlertInformation();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        cmbTipoUsuario.getItems().addAll(
                "Gerente",
                "Administrador",
                "Administrador de Cine"
        );

        // Si la última vez marcaron "Recordarme", se precarga el usuario
        String usuarioRecordado = SesionPreferencias.obtenerUsuarioRecordado();
        if (!usuarioRecordado.isEmpty()) {
            txtUsuario.setText(usuarioRecordado);
            chkRecordarSesion.setSelected(true);
        }

        crearBolitas();
    }

    // ---------- Burbujas animadas ----------
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

    // ---------- Inicio de sesión (contra la base de datos) ----------
    @FXML
    private void iniciarSesion() {
        String usuario = txtUsuario.getText().trim();
        String password = txtPassword.getText().trim();
        String rol = cmbTipoUsuario.getValue();

        if (usuario.isEmpty() || password.isEmpty() || rol == null) {
            alertInfo.viewAlert("WARNING", "CAMPOS INCOMPLETOS", "FALTAN DATOS",
                    "Ingresa tu usuario, tu contraseña y el tipo de usuario.");
            return;
        }

        try {
            Usuario usuarioEncontrado = usuarioRepository.login(usuario, password, rol);

            if (usuarioEncontrado == null) {
                alertInfo.viewAlert("WARNING", "ACCESO DENEGADO", "CREDENCIALES INCORRECTAS",
                        "El usuario, la contraseña o el tipo de usuario no son correctos.");
                return;
            }

            // Se guarda el usuario Y el rol: el rol es lo que despues
            // decide a que pantallas puede entrar (ej. Cartelera es
            // exclusiva del Administrador de Cine).
            Session.setUsuarioActual(usuario);
            Session.setRolActual(rol);

            if (chkRecordarSesion.isSelected()) {
                SesionPreferencias.recordarUsuario(usuario);
            } else {
                SesionPreferencias.olvidarUsuario();
            }

            ViewFactory viewFactory = new ViewFactory();
            viewFactory.viewHome();

        } catch (SQLException e) {
            e.printStackTrace();
            alertInfo.viewAlert("WARNING", "ERROR DE CONEXIÓN", "BASE DE DATOS",
                    "No se pudo consultar la base de datos. Intenta de nuevo.");
        }
    }
}