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

    public void viewPeliculas() {
        loadScene("peliculas");
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

    public void viewCompraBoleto() {
        loadScene("compraboletos");
    }

    private void loadScene(String vista) {
        Scene scene = switch (vista) {
            case "login" -> loadFileFXML("Login.fxml", 800, 500);
            case "home" -> loadFileFXML("HomeView.fxml", 860, 540);
            case "peliculas" -> loadFileFXML("Pelicula.fxml", 900, 600);
            case "cartelera" -> loadFileFXML("CarteleraView.fxml", 1000, 600);
            case "registrarpelicula" -> loadFileFXML("RegistrarPeliculaView.fxml", 720, 560);
            case "administradores" -> loadFileFXML("GestionarAdministradoresView.fxml", 1000, 600);
            case "compraboletos", "compraboleto" -> loadFileFXML("CompraBoletoView.fxml", 1180, 720);
            default -> loadFileFXML("Login.fxml", 420, 520);
        };

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