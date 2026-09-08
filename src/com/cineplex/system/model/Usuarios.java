package com.cineplex.system.model;

public class Usuarios {
    
    private int ID_Usuario;
    private String Nombre;
    private String Usuario;
    private String Clave;
    private String Rol;
    
    public Usuarios(){ 
    }
    
    public Usuarios(int ID_Usuario, String Nombre, String Usuario, String Clave, String Rol){
        this.ID_Usuario = ID_Usuario;
        this.Nombre = Nombre;
        this.Usuario = Usuario;
        this.Clave = Clave;
        this.Rol = Rol;
    }

    public int getID_Usuario() {
        return ID_Usuario;
    }

    public void setID_Usuario(int ID_Usuario) {
        this.ID_Usuario = ID_Usuario;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String Usuario) {
        this.Usuario = Usuario;
    }

    public String getClave() {
        return Clave;
    }

    public void setClave(String Clave) {
        this.Clave = Clave;
    }

    public String getRol() {
        return Rol;
    }

    public void setRol(String Rol) {
        this.Rol = Rol;
    }
    
}
