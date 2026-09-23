package com.cineplex.system.repository;

import com.cineplex.system.config.ConexionDB;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BoletoRepository {

    private Connection conexion;

    public BoletoRepository() {
        conexion = ConexionDB.getInstanciaConexionDB().getConnection();
    }

    
    public int crear(int idFuncion, int idCliente, String asiento, String contenidoQR) {
        try (CallableStatement callSP = conexion
                .prepareCall("{call sp_Crear_Boleto(?,?,?,?)}")) {
            callSP.setInt(1, idFuncion);
            callSP.setInt(2, idCliente);
            callSP.setString(3, asiento);
            callSP.setString(4, contenidoQR);
            try (ResultSet resultado = callSP.executeQuery()) {
                if (resultado.next()) {
                    return resultado.getInt("ID_Boleto");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al crear el boleto: " + e.getMessage());
            throw new RuntimeException(e);
        }
        throw new RuntimeException("No se pudo obtener el ID del boleto recien creado.");
    }
}