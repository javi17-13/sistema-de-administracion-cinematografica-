package com.cineplex.system.model;

public class Clientes {

    private int ID_Cliente;
    private String Nombre;
    private String Correo;

    public Clientes() {
    }

    public Clientes(int ID_Cliente, String Nombre, String Correo) {
        this.ID_Cliente = ID_Cliente;
        this.Nombre = Nombre;
        this.Correo = Correo;
    }

    public int getID_Cliente() {
        return ID_Cliente;
    }

    public void setID_Cliente(int ID_Cliente) {
        this.ID_Cliente = ID_Cliente;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String Correo) {
        this.Correo = Correo;
    }
}
