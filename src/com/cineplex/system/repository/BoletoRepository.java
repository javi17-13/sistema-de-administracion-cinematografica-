package com.cineplex.system.repository;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.cineplex.system.config.ConexionDB;

public class BoletoRepository {

    private final ConexionDB conexionDB = ConexionDB.getInstanciaConexionDB();

    /**
     * Inserta el boleto y devuelve el ID que le asigno MySQL.
     * Si el asiento ya estaba vendido para esa funcion, esto lanza
     * SQLIntegrityConstraintViolationException (por el UNIQUE de la
     * tabla) envuelta en RuntimeException -- BoletoService la traduce
     * a ResultadoCompra.ASIENTO_OCUPADO.
     */
    public int crear(int idFuncion, int idCliente, String asiento, String contenidoQR) {
        try (CallableStatement callSP = conexionDB.getConnection()
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
