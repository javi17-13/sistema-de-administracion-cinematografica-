package com.cineplex.system.utils;

import java.util.prefs.Preferences;

/**
 * Recuerda el usuario del ultimo login exitoso, guardado localmente en
 * esta computadora (java.util.prefs.Preferences -- en Windows vive en
 * el Registro, bajo el nodo de esta clase). NO tiene nada que ver con
 * la sesion de la aplicacion en si (eso lo maneja Session): esto es
 * solo para poder mostrar el usuario ya escrito la proxima vez que se
 * abra el programa, si el Administrador marco "Recordarme".
 */
public class SesionPreferencias {

    private static final String CLAVE_USUARIO = "ultimoUsuario";
    private static final Preferences PREFERENCIAS = Preferences.userNodeForPackage(SesionPreferencias.class);

    private SesionPreferencias() {
    }

    public static void recordarUsuario(String usuario) {
        PREFERENCIAS.put(CLAVE_USUARIO, usuario);
    }

    public static void olvidarUsuario() {
        PREFERENCIAS.remove(CLAVE_USUARIO);
    }

    /** Devuelve el usuario recordado, o cadena vacia si no hay ninguno guardado. */
    public static String obtenerUsuarioRecordado() {
        return PREFERENCIAS.get(CLAVE_USUARIO, "");
    }
}
