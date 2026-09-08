package com.cineplex.system.repository;

import com.cineplex.system.config.ConexionDB;
import com.cineplex.system.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioRepository {

    private Connection conexion;

    public UsuarioRepository() {
        conexion = ConexionDB.getConnection();
    }

    public Usuario login(String usuario, String clave) {

        String sql = "SELECT * FROM Usuarios WHERE Usuario = ? AND Clave = ?";

        try {

            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setString(1, usuario);
            ps.setString(2, clave);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Usuario usuarioEncontrado = new Usuario();

                usuarioEncontrado.setIdUsuario(rs.getInt("ID_Usuario"));
                usuarioEncontrado.setNombre(rs.getString("Nombre"));
                usuarioEncontrado.setUsuario(rs.getString("Usuario"));
                usuarioEncontrado.setClave(rs.getString("Clave"));
                usuarioEncontrado.setRol(rs.getString("Rol"));

                return usuarioEncontrado;
            }

        } catch (SQLException e) {
            System.out.println("Error al iniciar sesión: " + e.getMessage());
        }

        return null;
    }
}