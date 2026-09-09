package com.cineplex.system.service;

/**
 * TEMPORAL: mientras no exista la base de datos (siguiente historia de
 * usuario del backlog), las credenciales del Administrador del Cine
 * viven aqui, en memoria. El dia que se conecte la base de datos, este
 * es el UNICO archivo que se reemplaza (por una version que consulte una
 * tabla de administradores real) — LoginController no tiene que cambiar
 * ni una linea, porque solo conoce este metodo, no como esta implementado.
 */
public class AuthService {

    private static final String USUARIO_ADMIN = "admin";
    private static final String PASSWORD_ADMIN = "cine123";

    public boolean validarCredenciales(String usuario, String password) {
        return USUARIO_ADMIN.equals(usuario) && PASSWORD_ADMIN.equals(password);
    }
}
