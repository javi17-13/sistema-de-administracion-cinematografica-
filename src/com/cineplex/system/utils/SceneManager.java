package com.cineplex.system.utils;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Singleton que guarda la referencia al UNICO Stage de la aplicacion.
 * Cambiar de pantalla nunca implica abrir una ventana nueva, solo
 * reemplazar la Scene que el Stage esta mostrando.
 */
public class SceneManager {

    private static SceneManager instanciaSceneManager;
    private Stage stagePrincipal;

    private SceneManager() {
    }

    public static SceneManager getInstanciaSceneManager() {
        if (instanciaSceneManager == null) {
            instanciaSceneManager = new SceneManager();
        }
        return instanciaSceneManager;
    }

    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }

    public void changeScene(Scene scene) {
        stagePrincipal.setScene(scene);
        stagePrincipal.sizeToScene();
        stagePrincipal.show();
    }
}
