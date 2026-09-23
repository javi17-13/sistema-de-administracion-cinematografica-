package com.cineplex.system.controller;
 
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
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
 
        // Cada boton se habilita SOLO si el rol actual tiene permiso sobre
        // ese modulo (ver Roles.java). Esta es la primera barrera (visual);
        // cada pantalla de destino valida otra vez por su cuenta (segunda
        // barrera), por si alguien llega ahi sin pasar por este boton.
        btnCartelera.setDisable(!Roles.puedeVerCartelera(rol));
        btnRegistrarPelicula.setDisable(!Roles.puedeRegistrarPelicula(rol));
        btnAdministradores.setDisable(!Roles.puedeGestionarAdministradores(rol));
        btnCompraBoletos.setDisable(!Roles.puedeComprarBoletos(rol));
    }
 
    @FXML
    public void onIrCartelera(MouseEvent event) {
        if (!Roles.puedeVerCartelera(Session.getRolActual())) {
            alertInfo.viewAlert("WARNING", "ACCESO DENEGADO", "MÓDULO EXCLUSIVO",
                    "Solo el Administrador de Cine puede acceder a la cartelera.");
            return;
        }
        new ViewFactory().viewCartelera();
    }
 
    @FXML
    public void onIrRegistrarPelicula(MouseEvent event) {
        if (!Roles.puedeRegistrarPelicula(Session.getRolActual())) {
            alertInfo.viewAlert("WARNING", "ACCESO DENEGADO", "MÓDULO EXCLUSIVO",
                    "Solo el Administrador o el Administrador de Cine pueden registrar películas.");
            return;
        }
        //se limpia cualquier edicion anterior para que abra en modo registro
        RegistrarPeliculaController.prepararRegistro();
        new ViewFactory().viewRegistrarPelicula();
    }
 
    @FXML
    public void onIrAdministradores(MouseEvent event) {
        if (!Roles.puedeGestionarAdministradores(Session.getRolActual())) {
            alertInfo.viewAlert("WARNING", "ACCESO DENEGADO", "MÓDULO EXCLUSIVO",
                    "Solo el Administrador de Cine puede administrar cuentas.");
            return;
        }
        new ViewFactory().viewGestionarAdministradores();
    }
 
    @FXML
    public void onIrCompraBoletos(MouseEvent event) {
        if (!Roles.puedeComprarBoletos(Session.getRolActual())) {
            alertInfo.viewAlert("WARNING", "ACCESO DENEGADO", "MÓDULO EXCLUSIVO",
                    "Solo el Gerente o el Administrador de Cine pueden emitir boletos.");
            return;
        }
        new ViewFactory().viewCompraBoletos();
    }
 
    @FXML
    public void onCerrarSesion(MouseEvent event) {
        Session.cerrarSesion();
 
        ViewFactory viewFactory = new ViewFactory();
        viewFactory.viewLogin();
    }
}