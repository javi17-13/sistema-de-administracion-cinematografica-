package com.cineplex.system.service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import com.cineplex.system.model.ResultadoOperacion;
import com.cineplex.system.model.Usuario;
import com.cineplex.system.repository.UsuarioRepository;

/**
 * Logica de negocio de Usuarios (las cuentas de Administrador).
 *
 * La columna Usuario tiene UNIQUE en la tabla, asi que si se intenta
 * repetir un nombre de usuario MySQL rechaza el insert. En vez de dejar
 * reventar esa excepcion hasta la pantalla, aqui se traduce a
 * USUARIO_DUPLICADO para poder mostrar un mensaje claro.
 */
public class UsuarioService {

    private final UsuarioRepository usuarioRepo = new UsuarioRepository();

    public List<Usuario> obtenerTodos() {
        try {
            return usuarioRepo.leerTodos();
        } catch (RuntimeException e) {
            return List.of();
        }
    }

    public ResultadoOperacion crear(Usuario usuario) {
        try {
            usuarioRepo.crear(usuario);
            return ResultadoOperacion.EXITO;
        } catch (RuntimeException e) {
            return traducirError(e);
        }
    }

    public ResultadoOperacion editar(Usuario usuario) {
        try {
            usuarioRepo.editar(usuario);
            return ResultadoOperacion.EXITO;
        } catch (RuntimeException e) {
            return traducirError(e);
        }
    }

    public ResultadoOperacion darDeBaja(int idUsuario) {
        try {
            usuarioRepo.eliminar(idUsuario);
            return ResultadoOperacion.EXITO;
        } catch (RuntimeException e) {
            return ResultadoOperacion.ERROR;
        }
    }

    private ResultadoOperacion traducirError(RuntimeException e) {
        if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
            return ResultadoOperacion.USUARIO_DUPLICADO;
        }
        return ResultadoOperacion.ERROR;
    }
}