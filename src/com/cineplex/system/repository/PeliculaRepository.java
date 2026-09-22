package com.cineplex.system.repository;

import com.cineplex.system.config.ConexionDB;
import com.cineplex.system.model.Pelicula;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Unica clase que habla con la tabla Peliculas. Usa los procedimientos
 * almacenados de "DML Cineplex.sql" (sp_Crear_Peliculas, sp_Leer_Peliculas,
 * sp_Editar_Peliculas, sp_Eliminar_Peliculas) para el CRUD. Las
 * comprobaciones de titulo repetido usan consultas directas porque no
 * hay un procedimiento para eso.
 */
public class PeliculaRepository {

    private Connection conexion;

    public PeliculaRepository() {
        conexion = ConexionDB.getConnection();
    }

    public boolean agregar(Pelicula pelicula) {
        try (CallableStatement callSP = conexion
                .prepareCall("{call sp_Crear_Peliculas(?,?,?,?,?,?)}")) {

            callSP.setString(1, pelicula.getTitulo());
            callSP.setString(2, pelicula.getGenero());
            callSP.setInt(3, pelicula.getDuracion());
            callSP.setString(4, pelicula.getCategoria());
            callSP.setString(5, pelicula.getDirector());
            callSP.setString(6, pelicula.getPoster());

            callSP.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al agregar película: " + e.getMessage());
            return false;
        }
    }

    public boolean editar(Pelicula pelicula) {
        try (CallableStatement callSP = conexion
                .prepareCall("{call sp_Editar_Peliculas(?,?,?,?,?,?,?)}")) {

            callSP.setInt(1, pelicula.getID_Pelicula());
            callSP.setString(2, pelicula.getTitulo());
            callSP.setString(3, pelicula.getGenero());
            callSP.setInt(4, pelicula.getDuracion());
            callSP.setString(5, pelicula.getCategoria());
            callSP.setString(6, pelicula.getDirector());
            callSP.setString(7, pelicula.getPoster());

            callSP.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al editar película: " + e.getMessage());
            return false;
        }
    }

    public List<Pelicula> listar() {
        List<Pelicula> peliculas = new ArrayList<>();

        try (CallableStatement callSP = conexion
                .prepareCall("{call sp_Leer_Peliculas()}")) {

            try (ResultSet rs = callSP.executeQuery()) {
                while (rs.next()) {
                    peliculas.add(mapearPelicula(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al listar películas: " + e.getMessage());
        }

        return peliculas;
    }

    public Pelicula buscarPorId(int idPelicula) {
        try (CallableStatement callSP = conexion
                .prepareCall("{call sp_Buscar_Peliculas(?)}")) {

            callSP.setInt(1, idPelicula);

            try (ResultSet rs = callSP.executeQuery()) {
                if (rs.next()) {
                    return mapearPelicula(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar la película: " + e.getMessage());
        }

        return null;
    }

    // No hay procedimiento para esto, se usa una consulta directa.
    public boolean existeTitulo(String titulo) {
        String sql = "SELECT * FROM Peliculas WHERE Titulo = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, titulo);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.out.println("Error al comprobar título: " + e.getMessage());
            return false;
        }
    }

    // No hay procedimiento para esto, se usa una consulta directa.
    public boolean existeTituloExceptoId(String titulo, int idPelicula) {
        String sql = "SELECT * FROM Peliculas WHERE Titulo = ? AND ID_Pelicula <> ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, titulo);
            ps.setInt(2, idPelicula);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.out.println("Error al comprobar título: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idPelicula) {
        try (CallableStatement callSP = conexion
                .prepareCall("{call sp_Eliminar_Peliculas(?)}")) {

            callSP.setInt(1, idPelicula);
            callSP.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al eliminar película: " + e.getMessage());
            return false;
        }
    }

    private Pelicula mapearPelicula(ResultSet resultado) throws SQLException {
        Pelicula pelicula = new Pelicula();
        pelicula.setID_Pelicula(resultado.getInt("ID_Pelicula"));
        pelicula.setTitulo(resultado.getString("Titulo"));
        pelicula.setGenero(resultado.getString("Genero"));
        pelicula.setDuracion(resultado.getInt("Duracion"));
        pelicula.setCategoria(resultado.getString("Categoria"));
        pelicula.setDirector(resultado.getString("Director"));
        pelicula.setPoster(resultado.getString("Poster"));
        return pelicula;
    }
}
