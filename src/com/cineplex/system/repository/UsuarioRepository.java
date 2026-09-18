package com.cineplex.system.repository;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.cineplex.system.config.ConexionDB;
import com.cineplex.system.model.Usuarios;

/**
 * Unica clase que habla con la tabla Usuarios. Usa los procedimientos
 * que ya estan en "DML Cineplex.sql" (sp_Crear_Usuarios,
 * sp_Leer_Usuarios, sp_Editar_Usuarios, sp_Eliminar_Usuarios,
 * sp_Buscar_Usuarios).
 */
public class UsuarioRepository {

    private final ConexionDB conexionDB = ConexionDB.getInstanciaConexionDB();

    public void crear(Usuarios usuario) {
        try (CallableStatement callSP = conexionDB.getConnection()
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

    public List<Usuarios> leerTodos() {
        List<Usuarios> usuarios = new ArrayList<>();
        try (CallableStatement callSP = conexionDB.getConnection()
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

    public Usuarios buscarPorId(int idUsuario) {
        try (CallableStatement callSP = conexionDB.getConnection()
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

    public void editar(Usuarios usuario) {
        try (CallableStatement callSP = conexionDB.getConnection()
                     .prepareCall("{call sp_Editar_Usuarios(?,?,?,?,?)}")) {
            callSP.setInt(1, usuario.getID_Usuario());
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
        try (CallableStatement callSP = conexionDB.getConnection()
                     .prepareCall("{call sp_Eliminar_Usuarios(?)}")) {
            callSP.setInt(1, idUsuario);
            callSP.execute();
        } catch (SQLException e) {
            System.out.println("Error al eliminar el usuario: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private Usuarios mapearUsuario(ResultSet resultado) throws SQLException {
        Usuarios usuario = new Usuarios();
        usuario.setID_Usuario(resultado.getInt("ID_Usuario"));
        usuario.setNombre(resultado.getString("Nombre"));
        usuario.setUsuario(resultado.getString("Usuario"));
        usuario.setClave(resultado.getString("Clave"));
        usuario.setRol(resultado.getString("Rol"));
        return usuario;
    }
}
