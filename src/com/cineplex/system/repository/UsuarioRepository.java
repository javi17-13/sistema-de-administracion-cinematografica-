package com.cineplex.system.repository;

import com.cineplex.system.config.ConexionDB;
import com.cineplex.system.model.Usuario;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepository {

    private Connection conexion;

    public UsuarioRepository() {
        conexion = ConexionDB.getInstanciaConexionDB().getConnection();
    }

    public void crear(Usuario usuario) {
        try (CallableStatement callSP = conexion
                .prepareCall("{call sp_Crear_Usuarios(?,?,?,?)}")) {

            callSP.setString(1, usuario.getNombre());
            callSP.setString(2, usuario.getUsuario());
            callSP.setString(3, usuario.getClave());
            callSP.setString(4, usuario.getRol());

            callSP.execute();

        } catch (SQLException e) {
            System.out.println("Error al crear el usuario: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<Usuario> leerTodos() {
        List<Usuario> usuarios = new ArrayList<>();

        try (CallableStatement callSP = conexion
                .prepareCall("{call sp_Leer_Usuarios()}")) {

            try (ResultSet resultado = callSP.executeQuery()) {
                while (resultado.next()) {
                    usuarios.add(mapearUsuario(resultado));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al leer los usuarios: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return usuarios;
    }

    public Usuario buscarPorId(int idUsuario) {
        try (CallableStatement callSP = conexion
                .prepareCall("{call sp_Buscar_Usuarios(?)}")) {

            callSP.setInt(1, idUsuario);

            try (ResultSet resultado = callSP.executeQuery()) {
                if (resultado.next()) {
                    return mapearUsuario(resultado);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar el usuario: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return null;
    }

    public void editar(Usuario usuario) {
        try (CallableStatement callSP = conexion
                .prepareCall("{call sp_Editar_Usuarios(?,?,?,?,?)}")) {

            callSP.setInt(1, usuario.getIdUsuario());
            callSP.setString(2, usuario.getNombre());
            callSP.setString(3, usuario.getUsuario());
            callSP.setString(4, usuario.getClave());
            callSP.setString(5, usuario.getRol());

            callSP.execute();

        } catch (SQLException e) {
            System.out.println("Error al editar el usuario: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void eliminar(int idUsuario) {
        try (CallableStatement callSP = conexion
                .prepareCall("{call sp_Eliminar_Usuarios(?)}")) {

            callSP.setInt(1, idUsuario);
            callSP.execute();

        } catch (SQLException e) {
            System.out.println("Error al eliminar el usuario: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private Usuario mapearUsuario(ResultSet resultado) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(resultado.getInt("ID_Usuario"));
        usuario.setNombre(resultado.getString("Nombre"));
        usuario.setUsuario(resultado.getString("Usuario"));
        usuario.setClave(resultado.getString("Clave"));
        usuario.setRol(resultado.getString("Rol"));
        return usuario;
    }

    public Usuario login(String usuario, String clave) {

        String sql = "SELECT * FROM Usuarios WHERE Usuario = ? AND Clave = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, usuario);
            ps.setString(2, clave);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al iniciar sesión: " + e.getMessage());
        }

        return null;
    }

    public Usuario login(String usuario, String clave, String rol) throws SQLException {

        String sql = "SELECT * FROM Usuarios WHERE Usuario = ? AND Clave = ? AND Rol = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, usuario);
            ps.setString(2, clave);
            ps.setString(3, rol);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
        }

        return null;
    }
}
