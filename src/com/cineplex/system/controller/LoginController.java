package com.cineplex.system.controller;

import com.cineplex.system.model.Usuario;
import com.cineplex.system.repository.UsuarioRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class LoginController {

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private ComboBox<String> cmbTipoUsuario;

    @FXML
    private Button btnIniciarSesion;
    
    @FXML
private AnchorPane paneLogin;

    private UsuarioRepository usuarioRepository;

    @FXML
    public void initialize() {

        cmbTipoUsuario.getItems().addAll(
            "Gerente",
            "Administrador",
            "Administrador de Cine"
        );

        usuarioRepository = new UsuarioRepository();
        crearBolita();
    }
    
  
  private void crearBolita() {
    crearBolita(45, "#C58A19", 80, 100, 60, 60, 4);
    crearBolita(30, "#4F6D8A", 300, 150, -60, -60, 5);
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

    bolita.setFill(Color.web(color, 0.25));

    bolita.setLayoutX(x);
    bolita.setLayoutY(y);

    paneLogin.getChildren().add(bolita);

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
    
    @FXML
    private void iniciarSesion() {

        String usuario = txtUsuario.getText();
        String clave = txtPassword.getText();
        String rol = cmbTipoUsuario.getValue();

       Usuario usuarioEncontrado = usuarioRepository.login(
        usuario,
        clave

        );

        if (usuarioEncontrado != null) {
            System.out.println("Inicio de sesión exitoso.");
            System.out.println("Bienvenido: " + usuarioEncontrado.getNombre());
            System.out.println("Rol: " + usuarioEncontrado.getRol());
        } else {
            System.out.println("Usuario, clave o rol incorrectos.");
        }
    }
}
