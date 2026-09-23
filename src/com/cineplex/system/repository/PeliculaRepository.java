package com.cineplex.system.repository;

import com.cineplex.system.config.ConexionDB;
import com.cineplex.system.model.Pelicula;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

public class PeliculaRepository {

    private Connection conexion;

    public PeliculaRepository() {
        conexion = ConexionDB.getConnection();
    }
    
    
    public boolean agregar(Pelicula pelicula) {
    String sql = "INSERT INTO Peliculas (Titulo, Genero, Duracion, Categoria, Director, Poster) "
               + "VALUES (?, ?, ?, ?, ?, ?)";

    try {
        PreparedStatement ps = conexion.prepareStatement(sql);

        ps.setString(1, pelicula.getTitulo());
        ps.setString(2, pelicula.getGenero());
        ps.setInt(3, pelicula.getDuracion());
        ps.setString(4, pelicula.getCategoria());
        ps.setString(5, pelicula.getDirector());
        ps.setString(6, pelicula.getPoster());

       int filas = ps.executeUpdate();

System.out.println("Filas insertadas: " + filas);

return true;

    } catch (SQLException e) {
        System.out.println("Error al agregar película: " + e.getMessage());
        return false;
    }
}
   public boolean editar(Pelicula pelicula) {
    String sql = "UPDATE Peliculas SET Titulo = ?, Genero = ?, Duracion = ?, "
               + "Categoria = ?, Director = ?, Poster = ? "
               + "WHERE ID_Pelicula = ?";

    try {
        PreparedStatement ps = conexion.prepareStatement(sql);

        ps.setString(1, pelicula.getTitulo());
        ps.setString(2, pelicula.getGenero());
        ps.setInt(3, pelicula.getDuracion());
        ps.setString(4, pelicula.getCategoria());
        ps.setString(5, pelicula.getDirector());
        ps.setString(6, pelicula.getPoster());
        ps.setInt(7, pelicula.getIdPelicula());

        int filasActualizadas = ps.executeUpdate();

        System.out.println("ID de película editada: " + pelicula.getIdPelicula());
        System.out.println("Filas actualizadas: " + filasActualizadas);

        return filasActualizadas > 0;

    } catch (SQLException e) {
        System.out.println("Error al editar película: " + e.getMessage());
        return false;
    }
}
    public List<Pelicula> listar() {
    List<Pelicula> peliculas = new ArrayList<>();

    String sql = "SELECT * FROM Peliculas";

    try {
        PreparedStatement ps = conexion.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Pelicula pelicula = new Pelicula();

            pelicula.setIdPelicula(rs.getInt("ID_Pelicula"));
            pelicula.setTitulo(rs.getString("Titulo"));
            pelicula.setGenero(rs.getString("Genero"));
            pelicula.setDuracion(rs.getInt("Duracion"));
            pelicula.setCategoria(rs.getString("Categoria"));
            pelicula.setDirector(rs.getString("Director"));
            pelicula.setPoster(rs.getString("Poster"));

            peliculas.add(pelicula);
        }

    } catch (SQLException e) {
        System.out.println("Error al listar películas: " + e.getMessage());
    }

    return peliculas;
}
    
    public boolean existeTitulo(String titulo) {
    String sql = "SELECT * FROM Peliculas WHERE Titulo = ?";

    try {
        PreparedStatement ps = conexion.prepareStatement(sql);

        ps.setString(1, titulo);

        ResultSet rs = ps.executeQuery();

        return rs.next();

    } catch (SQLException e) {
        System.out.println("Error al comprobar título: " + e.getMessage());
        return false;
    }
}
    
    public boolean existeTituloExceptoId(String titulo, int idPelicula) {
    String sql = "SELECT * FROM Peliculas WHERE Titulo = ? AND ID_Pelicula <> ?";

    try {
        PreparedStatement ps = conexion.prepareStatement(sql);

        ps.setString(1, titulo);
        ps.setInt(2, idPelicula);

        ResultSet rs = ps.executeQuery();

        return rs.next();

    } catch (SQLException e) {
        System.out.println("Error al comprobar título: " + e.getMessage());
        return false;
    }
}
    
    
    
    
    
    
    
    
    public boolean eliminar(int idPelicula) {
    String sql = "DELETE FROM Peliculas WHERE ID_Pelicula = ?";

    try {
        PreparedStatement ps = conexion.prepareStatement(sql);

        ps.setInt(1, idPelicula);

        ps.executeUpdate();

        return true;

    } catch (SQLException e) {
        System.out.println("Error al eliminar película: " + e.getMessage());
        return false;
    }
}


    
}
