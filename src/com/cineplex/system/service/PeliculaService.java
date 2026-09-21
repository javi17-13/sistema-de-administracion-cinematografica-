package com.cineplex.system.service;

import com.cineplex.system.model.Pelicula;
import com.cineplex.system.repository.PeliculaRepository;
import java.util.List;

public class PeliculaService {

    private PeliculaRepository peliculaRepository;

    public PeliculaService() {
        peliculaRepository = new PeliculaRepository();
    }
  public String validarDatos(Pelicula pelicula) {

    if (pelicula.getTitulo() == null || pelicula.getTitulo().trim().isEmpty()) {
        return "El título es obligatorio.";
    }

    if (pelicula.getGenero() == null || pelicula.getGenero().trim().isEmpty()) {
        return "El género es obligatorio.";
    }

    if (pelicula.getCategoria() == null || pelicula.getCategoria().trim().isEmpty()) {
        return "La categoría es obligatoria.";
    }

    if (pelicula.getDirector() == null || pelicula.getDirector().trim().isEmpty()) {
        return "El director es obligatorio.";
    }

    if (pelicula.getPoster() == null || pelicula.getPoster().trim().isEmpty()) {
        return "El poster es obligatorio.";
    }

    if (pelicula.getDuracion() <= 0) {
        return "La duración debe ser mayor que 0.";
    }

    return null;
}
  
  public boolean agregar(Pelicula pelicula) {

    if (validarDatos(pelicula) != null) {
    return false;
}

    if (peliculaRepository.existeTitulo(pelicula.getTitulo())) {
        return false;
    }

    return peliculaRepository.agregar(pelicula);
}
  
  
  public List<Pelicula> listar() {
    return peliculaRepository.listar();
}
  
  public boolean editar(Pelicula pelicula) {

    if (validarDatos(pelicula) != null) {
    return false;
}

    if (peliculaRepository.existeTituloExceptoId(
            pelicula.getTitulo(),
            pelicula.getIdPelicula())) {
        return false;
    }

    return peliculaRepository.editar(pelicula);
}
  
  public boolean eliminar(int idPelicula) {

    if (idPelicula <= 0) {
        return false;
    }

    return peliculaRepository.eliminar(idPelicula);
}
    

    
}
