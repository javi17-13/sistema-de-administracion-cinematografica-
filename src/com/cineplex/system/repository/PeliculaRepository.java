package com.cineplex.system.repository;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.cineplex.system.config.ConexionDB;
import com.cineplex.system.model.Peliculas;

/**
 * Unica clase que habla con la tabla Peliculas. Usa los procedimientos
 * almacenados que ya estan escritos en "DML Cineplex.sql"
 * (sp_Crear_Peliculas, sp_Leer_Peliculas, sp_Editar_Peliculas,
 * sp_Eliminar_Peliculas, sp_Buscar_Peliculas).
 */
public class PeliculaRepository {

    private final ConexionDB conexionDB = ConexionDB.getInstanciaConexionDB();

    public void crear(Peliculas pelicula) {
        try (CallableStatement callSP = conexionDB.getConnection()
                     .prepareCall("{call sp_Crear_Peliculas(?,?,?,?,?,?)}")) {
            callSP.setString(1, pelicula.getTitulo());
            callSP.setString(2, pelicula.getGenero());
            callSP.setInt(3, pelicula.getDuracion());
            callSP.setString(4, pelicula.getCategoria());
            callSP.setString(5, pelicula.getDirector());
            callSP.setString(6, pelicula.getPoster());
            callSP.execute();
        } catch (SQLException e) {
            System.out.println("Error al crear la pelicula: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<Peliculas> leerTodas() {
        List<Peliculas> peliculas = new ArrayList<>();
        try (CallableStatement callSP = conexionDB.getConnection()
                     .prepareCall("{call sp_Leer_Peliculas()}")) {
            try (ResultSet resultado = callSP.executeQuery()) {
                while (resultado.next()) {
                    peliculas.add(mapearPelicula(resultado));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al leer las peliculas: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return peliculas;
    }

    public Peliculas buscarPorId(int idPelicula) {
        try (CallableStatement callSP = conexionDB.getConnection()
                     .prepareCall("{call sp_Buscar_Peliculas(?)}")) {
            callSP.setInt(1, idPelicula);
            try (ResultSet resultado = callSP.executeQuery()) {
                if (resultado.next()) {
                    return mapearPelicula(resultado);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar la pelicula: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }

    public void editar(Peliculas pelicula) {
        try (CallableStatement callSP = conexionDB.getConnection()
                     .prepareCall("{call sp_Editar_Peliculas(?,?,?,?,?,?,?)}")) {
            callSP.setInt(1, pelicula.getID_Pelicula());
            callSP.setString(2, pelicula.getTitulo());
            callSP.setString(3, pelicula.getGenero());
            callSP.setInt(4, pelicula.getDuracion());
            callSP.setString(5, pelicula.getCategoria());
            callSP.setString(6, pelicula.getDirector());
            callSP.setString(7, pelicula.getPoster());
            callSP.execute();
        } catch (SQLException e) {
            System.out.println("Error al editar la pelicula: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void eliminar(int idPelicula) {
        try (CallableStatement callSP = conexionDB.getConnection()
                     .prepareCall("{call sp_Eliminar_Peliculas(?)}")) {
            callSP.setInt(1, idPelicula);
            callSP.execute();
        } catch (SQLException e) {
            System.out.println("Error al eliminar la pelicula: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /** Convierte una fila del ResultSet en un objeto Peliculas. */
    private Peliculas mapearPelicula(ResultSet resultado) throws SQLException {
        Peliculas pelicula = new Peliculas();
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
