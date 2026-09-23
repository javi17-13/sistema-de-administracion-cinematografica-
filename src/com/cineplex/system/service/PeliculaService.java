package com.cineplex.system.service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import com.cineplex.system.model.Pelicula;
import com.cineplex.system.model.ResultadoOperacion;
import com.cineplex.system.repository.PeliculaRepository;


public class PeliculaService {

    private final PeliculaRepository peliculaRepo = new PeliculaRepository();

    public List<Pelicula> obtenerCartelera() {
        try {
            return peliculaRepo.listar();
        } catch (RuntimeException e) {
            return new ArrayList<>();
        }
    }

    public Pelicula buscarPorId(int idPelicula) {
        return peliculaRepo.buscarPorId(idPelicula);
    }

    public ResultadoOperacion registrar(Pelicula pelicula) {
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
        if (peliculaRepo.existeTituloExceptoId(pelicula.getTitulo(), pelicula.getID_Pelicula())) {
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
        try {
            peliculaRepo.eliminar(idPelicula);
            return ResultadoOperacion.EXITO;
        } catch (RuntimeException e) {
            // Si la pelicula ya tiene funciones programadas, la tabla
            // Funciones tiene una llave foranea hacia Peliculas y MySQL
            // rechaza el DELETE. Antes esto no se enteraba nunca porque
            // PeliculaRepository.eliminar() atrapaba el error por dentro
            // y no dejaba pasar la excepcion.
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