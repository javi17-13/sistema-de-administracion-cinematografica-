package com.cineplex.system.repository;

import com.cineplex.system.config.ConexionDB;
import com.cineplex.system.model.Funciones;
import java.sql.Connection;
import java.sql.PreparedStatement;
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

        String sql = "SELECT f.ID_Funcion, f.Fecha, f.Hora, f.Precio, "
                + "p.ID_Pelicula, p.Titulo, p.Genero, p.Duracion, "
                + "p.Categoria, p.Poster, "
                + "s.ID_Sala, s.Nombre AS NombreSala, s.Filas, s.Columnas "
                + "FROM Funciones f "
                + "INNER JOIN Peliculas p ON p.ID_Pelicula = f.ID_Pelicula "
                + "INNER JOIN Salas s ON s.ID_Sala = f.ID_Sala "
                + "ORDER BY f.Fecha, f.Hora";

        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet resultado = ps.executeQuery()) {

            while (resultado.next()) {
                funciones.add(mapearFuncion(resultado));
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener las funciones: " + e.getMessage());
        }

        return funciones;
    }

    public List<Funciones> buscarPorTitulo(String titulo) {

        List<Funciones> funciones = new ArrayList<>();

        String sql = "SELECT f.ID_Funcion, f.Fecha, f.Hora, f.Precio, "
                + "p.ID_Pelicula, p.Titulo, p.Genero, p.Duracion, "
                + "p.Categoria, p.Poster, "
                + "s.ID_Sala, s.Nombre AS NombreSala, s.Filas, s.Columnas "
                + "FROM Funciones f "
                + "INNER JOIN Peliculas p ON p.ID_Pelicula = f.ID_Pelicula "
                + "INNER JOIN Salas s ON s.ID_Sala = f.ID_Sala "
                + "WHERE p.Titulo LIKE ? "
                + "ORDER BY f.Fecha, f.Hora";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, "%" + titulo + "%");

            try (ResultSet resultado = ps.executeQuery()) {

                while (resultado.next()) {
                    funciones.add(mapearFuncion(resultado));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar funciones por titulo: " + e.getMessage());
        }

        return funciones;
    }

    public List<String> obtenerAsientosOcupados(int idFuncion) {

        List<String> asientos = new ArrayList<>();

        String sql = "SELECT Asiento FROM Boletos WHERE ID_Funcion = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idFuncion);

            try (ResultSet resultado = ps.executeQuery()) {

                while (resultado.next()) {
                    asientos.add(resultado.getString("Asiento"));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener los asientos ocupados: " + e.getMessage());
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
