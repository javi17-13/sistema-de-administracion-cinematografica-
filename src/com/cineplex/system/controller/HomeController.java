package com.cineplex.system.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import com.cineplex.system.utils.Session;
import com.cineplex.system.utils.ViewFactory;

public class HomeController implements Initializable {

    @FXML
    private Label lblUsuario;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblUsuario.setText("Administrador: " + Session.getUsuarioActual());
    }

    @FXML
    public void onCerrarSesion(MouseEvent event) {
        Session.cerrarSesion();

        ViewFactory viewFactory = new ViewFactory();
        viewFactory.viewLogin();
    }
}
