package com.cineplex.system.model;

public class Boletos {

    private int ID_Boleto;
    private int ID_Funcion;
    private int ID_Cliente;
    private String Asiento;
    private String ContenidoQR;

    public Boletos() {
    }

    public int getID_Boleto() {
        return ID_Boleto;
    }

    public void setID_Boleto(int ID_Boleto) {
        this.ID_Boleto = ID_Boleto;
    }

    public int getID_Funcion() {
        return ID_Funcion;
    }

    public void setID_Funcion(int ID_Funcion) {
        this.ID_Funcion = ID_Funcion;
    }

    public int getID_Cliente() {
        return ID_Cliente;
    }

    public void setID_Cliente(int ID_Cliente) {
        this.ID_Cliente = ID_Cliente;
    }

    public String getAsiento() {
        return Asiento;
    }

    public void setAsiento(String Asiento) {
        this.Asiento = Asiento;
    }

    public String getContenidoQR() {
        return ContenidoQR;
    }

    public void setContenidoQR(String ContenidoQR) {
        this.ContenidoQR = ContenidoQR;
    }
}
