package com.cineplex.system.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import com.cineplex.system.service.AuthService;
import com.cineplex.system.utils.AlertInformation;
import com.cineplex.system.utils.Session;
import com.cineplex.system.utils.ViewFactory;

public class LoginController implements Initializable {

    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField pwdPassword;

    private AlertInformation alertInfo = new AlertInformation();

    private final AuthService authService = new AuthService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    @FXML
    public void onIngresar(MouseEvent event) {
        String usuario = txtUsuario.getText().trim();
        String password = pwdPassword.getText().trim();

        if (usuario.isEmpty() || password.isEmpty()) {
            alertInfo.viewAlert("WARNING", "CAMPOS INCOMPLETOS",
                    "FALTAN DATOS",
                    "Ingresa tu usuario y tu contraseña.");
            return;
        }

        if (!authService.validarCredenciales(usuario, password)) {
            alertInfo.viewAlert("WARNING", "ACCESO DENEGADO",
                    "CREDENCIALES INCORRECTAS",
                    "El usuario o la contraseña no son correctos.");
            return;
        }

        Session.setUsuarioActual(usuario);

        ViewFactory viewFactory = new ViewFactory();
        viewFactory.viewHome();
    }
}