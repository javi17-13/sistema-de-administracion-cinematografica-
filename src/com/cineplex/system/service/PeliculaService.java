package com.cineplex.system.service;

import com.cineplex.system.model.Pelicula;
import com.cineplex.system.model.ResultadoOperacion;
import com.cineplex.system.repository.PeliculaRepository;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

public class PeliculaService {

    private final PeliculaRepository peliculaRepo = new PeliculaRepository();

    public String validarDatos(Pelicula pelicula) {
        if (pelicula == null) {
            return "La película no puede ser nula.";
        }
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
            return "El póster es obligatorio.";
        }
        if (pelicula.getDuracion() <= 0) {
            return "La duración debe ser mayor que 0.";
        }
        return null;
    }

    public List<Pelicula> obtenerCartelera() {
        try {
            return peliculaRepo.listar();
        } catch (RuntimeException e) {
            return new ArrayList<>();
        }
    }

    public List<Pelicula> listar() {
        return obtenerCartelera();
    }

    public Pelicula buscarPorId(int idPelicula) {
        return peliculaRepo.buscarPorId(idPelicula);
    }

    public ResultadoOperacion registrar(Pelicula pelicula) {
        String errorValidacion = validarDatos(pelicula);
        if (errorValidacion != null) {
            return ResultadoOperacion.ERROR;
        }

        if (peliculaRepo.existeTitulo(pelicula.getTitulo())) {
            return ResultadoOperacion.TITULO_DUPLICADO;
        }

        try {
            peliculaRepo.agregar(pelicula);
            return ResultadoOperacion.EXITO;
        } catch (RuntimeException e) {
            return traducirError(e);
        }
    }

    public ResultadoOperacion editar(Pelicula pelicula) {
        String errorValidacion = validarDatos(pelicula);
        if (errorValidacion != null) {
            return ResultadoOperacion.ERROR;
        }

        if (peliculaRepo.existeTituloExceptoId(pelicula.getTitulo(), pelicula.getIdPelicula())) {
            return ResultadoOperacion.TITULO_DUPLICADO;
        }

        try {
            peliculaRepo.editar(pelicula);
            return ResultadoOperacion.EXITO;
        } catch (RuntimeException e) {
            return traducirError(e);
        }
    }

    public ResultadoOperacion eliminar(int idPelicula) {
        if (idPelicula <= 0) {
            return ResultadoOperacion.ERROR;
        }

        try {
            peliculaRepo.eliminar(idPelicula);
            return ResultadoOperacion.EXITO;
        } catch (RuntimeException e) {
            if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                return ResultadoOperacion.REGISTRO_EN_USO;
            }
            return ResultadoOperacion.ERROR;
        }
    }

    private ResultadoOperacion traducirError(RuntimeException e) {
        if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
            return ResultadoOperacion.TITULO_DUPLICADO;
        }
        return ResultadoOperacion.ERROR;
    }
}