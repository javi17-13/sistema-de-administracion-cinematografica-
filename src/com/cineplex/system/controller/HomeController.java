package com.cineplex.system.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import com.cineplex.system.utils.AlertInformation;
import com.cineplex.system.utils.Session;
import com.cineplex.system.utils.ViewFactory;
import javafx.event.ActionEvent;

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
  @FXML
public void abrirPeliculas(ActionEvent event) {
    ViewFactory viewFactory = new ViewFactory();
    viewFactory.viewPeliculas();
}

    @FXML
    public void abrirCompraBoletos(ActionEvent event) {
        ViewFactory viewFactory = new ViewFactory();
        viewFactory.viewCompraBoleto();
    }

    @FXML
    public void abrirCartelera(ActionEvent event) {
        mostrarProximamente("Cartelera");
    }

    @FXML
    public void abrirRegistrarPelicula(ActionEvent event) {
        mostrarProximamente("Registrar película");
    }

    @FXML
    public void abrirAdministradores(ActionEvent event) {
        mostrarProximamente("Administradores");
    }

    private void mostrarProximamente(String opcion) {
        new AlertInformation().viewAlert("INFORMATION", "PRÓXIMAMENTE", opcion,
                "Esta opción aún no está disponible.");
    }

}
