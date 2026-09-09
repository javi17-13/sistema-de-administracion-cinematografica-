package com.cineplex.system.utils;

import java.util.prefs.Preferences;

/**
 * Recuerda quien inicio sesion INCLUSO despues de cerrar y volver a abrir
 * la aplicacion, sin depender de una base de datos. Usa la API de
 * Preferences de Java: un almacen de configuracion clave-valor que el
 * propio sistema operativo administra por nosotros (en Windows vive en
 * el registro, dentro de HKEY_CURRENT_USER; en Linux/Mac en un archivo de
 * configuracion del usuario). Es exactamente para esto para lo que existe:
 * "recordar" datos pequeños entre ejecuciones sin montar una BD.
 */
public class SesionPreferencias {

    private static final Preferences PREFS = Preferences.userNodeForPackage(SesionPreferencias.class);
    private static final String CLAVE_USUARIO_RECORDADO = "usuarioRecordado";

    private SesionPreferencias() {
    }

    public static void recordarUsuario(String usuario) {
        PREFS.put(CLAVE_USUARIO_RECORDADO, usuario);
    }

    public static String obtenerUsuarioRecordado() {
        //el segundo parametro es el valor por defecto si la clave no existe todavia
        return PREFS.get(CLAVE_USUARIO_RECORDADO, null);
    }

    public static boolean haySesionRecordada() {
        return obtenerUsuarioRecordado() != null;
    }

    public static void olvidarUsuario() {
        PREFS.remove(CLAVE_USUARIO_RECORDADO);
    }
}
