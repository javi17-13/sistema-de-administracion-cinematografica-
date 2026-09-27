package com.cineplex.system.utils;
 


public final class Roles {
 
    public static final String ADMINISTRADOR_CINE = "Administrador de Cine";

    public static final String ADMINISTRADOR = "Administrador";

    public static final String GERENTE = "Gerente";
 
    private Roles() {

    }
 


    public static boolean puedeVerCartelera(String rol) {

        return ADMINISTRADOR_CINE.equals(rol);

    }
 
  

    public static boolean puedeRegistrarPelicula(String rol) {

        return ADMINISTRADOR.equals(rol) || ADMINISTRADOR_CINE.equals(rol);

    }
 
   

    public static boolean puedeEditarPelicula(String rol) {

        return ADMINISTRADOR_CINE.equals(rol);

    }
 


    public static boolean puedeGestionarAdministradores(String rol) {

        return ADMINISTRADOR_CINE.equals(rol);

    }
 
  

    public static boolean puedeComprarBoletos(String rol) {

        return GERENTE.equals(rol) || ADMINISTRADOR_CINE.equals(rol);

    }

}
 