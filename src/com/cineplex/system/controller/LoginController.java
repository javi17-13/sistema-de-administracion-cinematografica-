package com.cineplex.system.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import com.cineplex.system.service.AuthService;
import com.cineplex.system.utils.AlertInformation;
import com.cineplex.system.utils.SesionPreferencias;
import com.cineplex.system.utils.Session;
import com.cineplex.system.utils.ViewFactory;

public class LoginController implements Initializable {

    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField pwdPassword;
    @FXML
    private CheckBox chkRecordarSesion;

    private AlertInformation alertInfo = new AlertInformation();
    
    private final AuthService authService = new AuthService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Si ya habia una sesion recordada de una vez anterior, dejamos el
        //campo de usuario precargado para que sea mas rapido volver a entrar
        String usuarioRecordado = SesionPreferencias.obtenerUsuarioRecordado();
        if (usuarioRecordado != null) {
            txtUsuario.setText(usuarioRecordado);
            chkRecordarSesion.setSelected(true);
        }
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

        //Login correcto: se guarda en la sesion de esta ejecucion...
        Session.setUsuarioActual(usuario);

        //...y, si el Administrador marco la casilla, tambien se recuerda
        //para que la proxima vez que se abra la app entre directo, sin
        //volver a pedir usuario y contraseña.
        if (chkRecordarSesion.isSelected()) {
            SesionPreferencias.recordarUsuario(usuario);
        } else {
            SesionPreferencias.olvidarUsuario();
        }

        ViewFactory viewFactory = new ViewFactory();
        viewFactory.viewHome();
    }
}
