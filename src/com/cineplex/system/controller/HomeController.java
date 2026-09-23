package com.cineplex.system.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import com.cineplex.system.utils.AlertInformation;
import com.cineplex.system.utils.Session;
import com.cineplex.system.utils.ViewFactory;

public class HomeController implements Initializable {

    private static final String ROL_CON_ACCESO_CARTELERA = "Administrador de Cine";

    @FXML
    private Label lblUsuario;

    @FXML
    private Button btnCartelera;

    private final AlertInformation alertInfo = new AlertInformation();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblUsuario.setText("Administrador: " + Session.getUsuarioActual());

        // La cartelera es EXCLUSIVA del Administrador de Cine (US-01):
        // al resto de roles se les deshabilita el boton directamente.
        boolean puedeVerCartelera = ROL_CON_ACCESO_CARTELERA.equals(Session.getRolActual());
        btnCartelera.setDisable(!puedeVerCartelera);
    }

    @FXML
    public void onIrCartelera(MouseEvent event) {
        // Segunda barrera, por si alguien llega a llamar este metodo
        // sin pasar por el boton (defensa extra, no solo estetica).
        if (!ROL_CON_ACCESO_CARTELERA.equals(Session.getRolActual())) {
            alertInfo.viewAlert("WARNING", "ACCESO DENEGADO", "MÓDULO EXCLUSIVO",
                    "Solo el Administrador de Cine puede acceder a la cartelera.");
            return;
        }
        new ViewFactory().viewCartelera();
    }

    @FXML
    public void onIrRegistrarPelicula(MouseEvent event) {
        //se limpia cualquier edicion anterior para que abra en modo registro
        RegistrarPeliculaController.prepararRegistro();
        new ViewFactory().viewRegistrarPelicula();
    }

    @FXML
    public void onIrAdministradores(MouseEvent event) {
        new ViewFactory().viewGestionarAdministradores();
    }

    @FXML
    public void onIrCompraBoletos(MouseEvent event) {
        new ViewFactory().viewCompraBoletos();
    }

    @FXML
    public void onCerrarSesion(MouseEvent event) {
        Session.cerrarSesion();

        ViewFactory viewFactory = new ViewFactory();
        viewFactory.viewLogin();
    }
}