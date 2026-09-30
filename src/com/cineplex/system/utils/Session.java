package com.cineplex.system.utils;

/**
 * Guarda el usuario que inicio sesion (y con que rol), para que
 * cualquier pantalla pueda preguntar "quien esta conectado" y "que
 * puede hacer" sin tener que pasarse el dato de controlador en
 * controlador.
 */
public class Session {

    private static String usuarioActual;
    private static String rolActual;

    private Session() {
    }

    public static String getUsuarioActual() {
        return usuarioActual;
    }

    public static void setUsuarioActual(String usuario) {
        usuarioActual = usuario;
    }

    public static String getRolActual() {
        return rolActual;
    }

    public static void setRolActual(String rol) {
        rolActual = rol;
    }

    public static boolean haySesionActiva() {
        return usuarioActual != null;
    }

    public static void cerrarSesion() {
        usuarioActual = null;
        rolActual = null;
    }
}