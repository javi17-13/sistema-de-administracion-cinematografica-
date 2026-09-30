package com.cineplex.system.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import com.cineplex.system.utils.AlertInformation;
import com.cineplex.system.utils.Roles;
import com.cineplex.system.utils.Session;
import com.cineplex.system.utils.ViewFactory;

public class HomeController implements Initializable {

    @FXML
    private Label lblUsuario;

    @FXML
    private Button btnCartelera;

    @FXML
    private Button btnRegistrarPelicula;

    @FXML
    private Button btnAdministradores;

    @FXML
    private Button btnCompraBoletos;

    

    private final AlertInformation alertInfo = new AlertInformation();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String rol = Session.getRolActual();
        lblUsuario.setText(rol + ": " + Session.getUsuarioActual());

        // Deshabilitar botones visualmente según el rol
        btnCartelera.setDisable(!Roles.puedeVerCartelera(rol));
        btnRegistrarPelicula.setDisable(!Roles.puedeRegistrarPelicula(rol));
        btnAdministradores.setDisable(!Roles.puedeGestionarAdministradores(rol));
        btnCompraBoletos.setDisable(!Roles.puedeComprarBoletos(rol));
        btnRegistrarPelicula.setDisable(!Roles.puedeRegistrarPelicula(rol));
    }

    // --- Métodos de navegación del menú ---

    @FXML
    public void abrirCartelera(ActionEvent event) {
        if (!Roles.puedeVerCartelera(Session.getRolActual())) {
            alertInfo.viewAlert("WARNING", "ACCESO DENEGADO", "MÓDULO EXCLUSIVO",
                    "Solo el Administrador de Cine puede acceder a la cartelera.");
            return;
        }
        new ViewFactory().viewCartelera();
    }

    @FXML
    public void abrirRegistrarPelicula(ActionEvent event) {
        if (!Roles.puedeRegistrarPelicula(Session.getRolActual())) {
            alertInfo.viewAlert("WARNING", "ACCESO DENEGADO", "MÓDULO EXCLUSIVO",
                    "Solo el Administrador o el Administrador de Cine pueden registrar películas.");
            return;
        }
        RegistrarPeliculaController.prepararRegistro();
        new ViewFactory().viewRegistrarPelicula();
    }

    @FXML
    public void abrirAdministradores(ActionEvent event) {
        if (!Roles.puedeGestionarAdministradores(Session.getRolActual())) {
            alertInfo.viewAlert("WARNING", "ACCESO DENEGADO", "MÓDULO EXCLUSIVO",
                    "Solo el Administrador de Cine puede administrar cuentas.");
            return;
        }
        new ViewFactory().viewGestionarAdministradores();
    }

    @FXML
    public void abrirCompraBoletos(ActionEvent event) {
        if (!Roles.puedeComprarBoletos(Session.getRolActual())) {
            alertInfo.viewAlert("WARNING", "ACCESO DENEGADO", "MÓDULO EXCLUSIVO",
                    "Solo el Gerente o el Administrador de Cine pueden emitir boletos.");
            return;
        }
        new ViewFactory().viewCompraBoletos();
    }

    @FXML
    public void abrirPeliculas(ActionEvent event) {
        if (!Roles.puedeRegistrarPelicula(Session.getRolActual())) {
            alertInfo.viewAlert("WARNING", "ACCESO DENEGADO", "MÓDULO EXCLUSIVO",
                    "Solo el Administrador o el Administrador de Cine pueden registrar películas.");
            return;
        }
        RegistrarPeliculaController.prepararRegistro();
        new ViewFactory().viewRegistrarPelicula();
    }

    

    @FXML
    public void onCerrarSesion(ActionEvent event) {
        Session.cerrarSesion();
        new ViewFactory().viewLogin();
    }
}