package com.cineplex.system;

import javafx.application.Application;
import javafx.stage.Stage;
import com.cineplex.system.utils.SceneManager;
import com.cineplex.system.utils.SesionPreferencias;
import com.cineplex.system.utils.Session;
import com.cineplex.system.utils.ViewFactory;

public class Main extends Application {

    @Override
    public void start(Stage stagePrincipal) {
        stagePrincipal.setTitle("CinePlex - Administración Cinematográfica");
        SceneManager.getInstanciaSceneManager().setStagePrincipal(stagePrincipal);

        ViewFactory viewFactory = new ViewFactory();

        String usuarioRecordado = SesionPreferencias.obtenerUsuarioRecordado();
        if (usuarioRecordado != null) {
            //Hay una sesion recordada de una ejecucion anterior: se entra
            //directo, sin pasar por el Login de nuevo (US-01: "que el
            //sistema recuerde su sesion activa").
            Session.setUsuarioActual(usuarioRecordado);
            viewFactory.viewHome();
        } else {
            viewFactory.viewLogin();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
