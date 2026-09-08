package com.cineplex.system.controller;

import com.cineplex.system.model.Usuario;
import com.cineplex.system.repository.UsuarioRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private ComboBox<String> cmbTipoUsuario;

    @FXML
    private Button btnIniciarSesion;

    private UsuarioRepository usuarioRepository;

    @FXML
    public void initialize() {

        cmbTipoUsuario.getItems().addAll(
            "Gerente",
            "Administrador",
            "Administrador de Cine"
        );

        usuarioRepository = new UsuarioRepository();
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
