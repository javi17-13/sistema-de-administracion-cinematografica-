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
        conexion = ConexionDB.getConnection();
    }

    public List<Funciones> obtenerTodas() {
        List<Funciones> funciones = new ArrayList<>();
        try (CallableStatement callSP = conexion
                     .prepareCall("{call sp_Obtener_Funciones()}")) {
            try (ResultSet resultado = callSP.executeQuery()) {
                while (resultado.next()) {
                    funciones.add(mapearFuncion(resultado));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener las funciones: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return funciones;
    }

    /** US-09: busca funciones por el titulo de la pelicula (busqueda parcial, no distingue mayusculas). */
    public List<Funciones> buscarPorTitulo(String titulo) {
        List<Funciones> funciones = new ArrayList<>();
        try (CallableStatement callSP = conexion
                     .prepareCall("{call sp_Buscar_Funcion_Por_Titulo(?)}")) {
            callSP.setString(1, titulo);
            try (ResultSet resultado = callSP.executeQuery()) {
                while (resultado.next()) {
                    funciones.add(mapearFuncion(resultado));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar funciones por titulo: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return funciones;
    }

    /** Los asientos (ej. "C4") que ya tienen boleto vendido para esa funcion. */
    public List<String> obtenerAsientosOcupados(int idFuncion) {
        List<String> asientos = new ArrayList<>();
        try (CallableStatement callSP = conexion
                     .prepareCall("{call sp_Obtener_Asientos_Ocupados(?)}")) {
            callSP.setInt(1, idFuncion);
            try (ResultSet resultado = callSP.executeQuery()) {
                while (resultado.next()) {
                    asientos.add(resultado.getString("Asiento"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener los asientos ocupados: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return asientos;
    }

    private Funciones mapearFuncion(ResultSet resultado) throws SQLException {
        Funciones funcion = new Funciones();
        funcion.setID_Funcion(resultado.getInt("ID_Funcion"));
        funcion.setFecha(resultado.getDate("Fecha"));
        funcion.setHora(resultado.getTime("Hora"));
        funcion.setPrecio(resultado.getBigDecimal("Precio"));
        funcion.setID_Pelicula(resultado.getInt("ID_Pelicula"));
        funcion.setTitulo(resultado.getString("Titulo"));
        funcion.setGenero(resultado.getString("Genero"));
        funcion.setDuracion(resultado.getInt("Duracion"));
        funcion.setCategoria(resultado.getString("Categoria"));
        funcion.setPoster(resultado.getString("Poster"));
        funcion.setID_Sala(resultado.getInt("ID_Sala"));
        funcion.setNombreSala(resultado.getString("NombreSala"));
        funcion.setFilas(resultado.getInt("Filas"));
        funcion.setColumnas(resultado.getInt("Columnas"));
        return funcion;
    }
}