package com.cineplex.system.service;

import com.cineplex.system.model.Usuario;
import com.cineplex.system.repository.UsuarioRepository;

public class UsuarioService {

    private UsuarioRepository usuarioRepository;

    public UsuarioService() {
        usuarioRepository = new UsuarioRepository();
    }

    public Usuario iniciarSesion(String usuario, String clave, String rol) {
        return usuarioRepository.login(usuario, clave);
    }
}
