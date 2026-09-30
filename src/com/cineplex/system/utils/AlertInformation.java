package com.cineplex.system.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertInformation {

    public AlertInformation() {
    }

    public void viewAlert(String tipoAlerta, String titulo, String encabezado, String mensaje) {

        AlertType tipo;
        String tipoTexto = tipoAlerta.toUpperCase();

        if (tipoTexto.equals("INFO") || tipoTexto.equals("INFORMATION")) {
            tipo = AlertType.INFORMATION;
        } else if (tipoTexto.equals("WARNING") || tipoTexto.equals("WARN")) {
            tipo = AlertType.WARNING;
        } else if (tipoTexto.equals("ERROR") || tipoTexto.equals("ERR")) {
            tipo = AlertType.ERROR;
        } else if (tipoTexto.equals("CONFIRMATION") || tipoTexto.equals("CONFIRM")) {
            tipo = AlertType.CONFIRMATION;
        } else if (tipoTexto.equals("NONE")) {
            tipo = AlertType.NONE;
        } else {
            tipo = AlertType.INFORMATION;
        }

        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(mensaje);

        alert.showAndWait();
    }
}