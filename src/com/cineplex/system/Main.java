package com.cineplex.system;

import javafx.application.Application;
import javafx.stage.Stage;
import com.cineplex.system.utils.SceneManager;
import com.cineplex.system.utils.ViewFactory;

public class Main extends Application {

    @Override
    public void start(Stage stagePrincipal) {
        stagePrincipal.setTitle("CinePlex - Administración Cinematográfica");
        SceneManager.getInstanciaSceneManager().setStagePrincipal(stagePrincipal);

        ViewFactory viewFactory = new ViewFactory();
        viewFactory.viewLogin();
    }

    public static void main(String[] args) {
        launch(args);
    }
}