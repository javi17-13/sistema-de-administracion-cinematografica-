package com.cineplex.system.utils;

/**
 * Guarda el usuario que inicio sesion, para que cualquier pantalla
 * pueda preguntar "quien esta conectado" sin tener que pasarse el dato
 * de controlador en controlador.
 */
public class Session {

    private static String usuarioActual;

    private Session() {
    }

    public static String getUsuarioActual() {
        return usuarioActual;
    }

    public static void setUsuarioActual(String usuario) {
        usuarioActual = usuario;
    }

    public static boolean haySesionActiva() {
        return usuarioActual != null;
    }

    public static void cerrarSesion() {
        usuarioActual = null;
    }
}
