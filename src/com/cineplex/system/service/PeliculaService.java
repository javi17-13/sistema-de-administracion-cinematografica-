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

    // Antes devolvía boolean. Ahora devuelve el mensaje de error (o null si se guardó bien),
    // para que el controlador de la pantalla pueda mostrar por qué falló.
    public String agregar(Pelicula pelicula) {

        String error = validarDatos(pelicula);
        if (error != null) {
            return error;
        }

        if (peliculaRepository.existeTitulo(pelicula.getTitulo())) {
            return "Ya existe una película con ese título.";
        }

        return peliculaRepository.agregar(pelicula)
                ? null
                : "No se pudo guardar la película. Intenta de nuevo.";
    }

    public List<Pelicula> listar() {
        return peliculaRepository.listar();
    }

    // Antes devolvía boolean. Ahora devuelve el mensaje de error (o null si se editó bien).
    public String editar(Pelicula pelicula) {

        String error = validarDatos(pelicula);
        if (error != null) {
            return error;
        }

        if (peliculaRepository.existeTituloExceptoId(
                pelicula.getTitulo(),
                pelicula.getIdPelicula())) {
            return "Ya existe otra película con ese título.";
        }

        return peliculaRepository.editar(pelicula)
                ? null
                : "No se pudo editar la película. Intenta de nuevo.";
    }

    public boolean eliminar(int idPelicula) {

        if (idPelicula <= 0) {
            return false;
        }

        return peliculaRepository.eliminar(idPelicula);
    }
}
