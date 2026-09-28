package com.cineplex.system.repository;

import com.cineplex.system.config.ConexionDB;
import com.cineplex.system.model.Funciones;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FuncionRepository {

    private Connection conexion;

    public FuncionRepository() {
        this.conexion = ConexionDB.getInstanciaConexionDB().getConnection();
    }

    public List<Funciones> obtenerTodas() {
        List<Funciones> funciones = new ArrayList<>();
        try (CallableStatement callSP = conexion.prepareCall("{call sp_Obtener_Funciones()}");
             ResultSet rs = callSP.executeQuery()) {

            while (rs.next()) {
                funciones.add(mapearFuncion(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener las funciones: " + e.getMessage());
        }
        return funciones;
    }

    public List<Funciones> buscarPorTitulo(String titulo) {
        List<Funciones> funciones = new ArrayList<>();
        try (CallableStatement callSP = conexion.prepareCall("{call sp_Buscar_Funcion_Por_Titulo(?)}")) {
            callSP.setString(1, titulo);

            try (ResultSet rs = callSP.executeQuery()) {
                while (rs.next()) {
                    funciones.add(mapearFuncion(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar funcion por titulo: " + e.getMessage());
        }
        return funciones;
    }

    public List<String> obtenerAsientosOcupados(int idFuncion) {
        List<String> asientos = new ArrayList<>();
        try (CallableStatement callSP = conexion.prepareCall("{call sp_Obtener_Asientos_Ocupados(?)}")) {
            callSP.setInt(1, idFuncion);

            try (ResultSet rs = callSP.executeQuery()) {
                while (rs.next()) {
                    asientos.add(rs.getString("Asiento"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener asientos ocupados: " + e.getMessage());
        }
        return asientos;
    }

    private Funciones mapearFuncion(ResultSet rs) throws SQLException {
        Funciones funcion = new Funciones();
        funcion.setID_Funcion(rs.getInt("ID_Funcion"));
        funcion.setFecha(rs.getDate("Fecha"));
        funcion.setHora(rs.getTime("Hora"));
        funcion.setPrecio(rs.getBigDecimal("Precio"));

        funcion.setID_Pelicula(rs.getInt("ID_Pelicula"));
        funcion.setTitulo(rs.getString("Titulo"));
        funcion.setGenero(rs.getString("Genero"));
        funcion.setDuracion(rs.getInt("Duracion"));
        funcion.setCategoria(rs.getString("Categoria"));
        funcion.setPoster(rs.getString("Poster"));

        funcion.setID_Sala(rs.getInt("ID_Sala"));
        funcion.setNombreSala(rs.getString("NombreSala"));
        funcion.setFilas(rs.getInt("Filas"));
        funcion.setColumnas(rs.getInt("Columnas"));

        return funcion;
    }
}