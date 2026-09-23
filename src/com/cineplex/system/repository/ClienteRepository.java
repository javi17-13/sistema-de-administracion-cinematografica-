package com.cineplex.system.repository;

import com.cineplex.system.config.ConexionDB;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClienteRepository {

    private Connection conexion;

    public ClienteRepository() {
        conexion = ConexionDB.getInstanciaConexionDB().getConnection();
    }

   
    public int crear(String nombre, String correo) {
        try (CallableStatement callSP = conexion
                .prepareCall("{call sp_Crear_Cliente(?,?)}")) {
            callSP.setString(1, nombre);
            callSP.setString(2, correo);
            try (ResultSet resultado = callSP.executeQuery()) {
                if (resultado.next()) {
                    return resultado.getInt("ID_Cliente");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al crear el cliente: " + e.getMessage());
            throw new RuntimeException(e);
        }
        throw new RuntimeException("No se pudo obtener el ID del cliente recien creado.");
    }
}
