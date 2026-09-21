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

    private void loadScene(String vista) {
        Scene scene;

        switch (vista) {
            case "login" -> scene = loadFileFXML("LoginView.fxml", 420, 520);
            case "home" -> scene = loadFileFXML("HomeView.fxml", 860, 540);
            default -> scene = loadFileFXML("LoginView.fxml", 420, 520);
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
