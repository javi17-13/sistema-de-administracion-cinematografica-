   package com.cineplex.system.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class ViewFactory {

    public void viewLogin() {
        loadScene("login");
    }

    public void viewHome() {
        loadScene("home");
    }

    public void viewCartelera() {
        loadScene("cartelera");
    }

    public void viewRegistrarPelicula() {
        loadScene("registrarpelicula");
    }

    public void viewGestionarAdministradores() {
        loadScene("administradores");
    }

    public void viewCompraBoletos() {
        loadScene("compraboletos");
    }

    private void loadScene(String vista) {
        Scene scene;

        if (vista.equals("login")) {
            scene = loadFileFXML("Login.fxml", 800, 500);
        } else if (vista.equals("home")) {
            scene = loadFileFXML("HomeView.fxml", 860, 540);
        } else if (vista.equals("cartelera")) {
            scene = loadFileFXML("CarteleraView.fxml", 1000, 600);
        } else if (vista.equals("registrarpelicula")) {
            scene = loadFileFXML("RegistrarPeliculaView.fxml", 720, 560);
        } else if (vista.equals("administradores")) {
            scene = loadFileFXML("GestionarAdministradoresView.fxml", 1000, 600);
        } else if (vista.equals("compraboletos")) {
            scene = loadFileFXML("CompraBoletoView.fxml", 1180, 720);
        } else {
            scene = loadFileFXML("Login.fxml", 420, 520);
        }

        SceneManager.getInstanciaSceneManager().changeScene(scene);
    }

    private Scene loadFileFXML(String nombreArchivo, double width, double height) {
        try {
            String path = "/com/cineplex/system/view/" + nombreArchivo;
            FXMLLoader loadFXML = new FXMLLoader();
            loadFXML.setLocation(getClass().getResource(path));

            Parent root = loadFXML.load();
            return new Scene(root, width, height);

        } catch (Exception e) {
            throw new RuntimeException("No se pudo cargar la vista " + nombreArchivo, e);
        }
    }
}