package com.cineplex.system.service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import com.cineplex.system.model.Pelicula;
import com.cineplex.system.model.ResultadoOperacion;
import com.cineplex.system.repository.PeliculaRepository;

/**
 * Logica de negocio de Peliculas. Los controladores nunca llaman al
 * repositorio directamente: pasan por aqui, para que la pantalla no
 * tenga que saber nada de SQL ni de excepciones de base de datos.
 */
public class PeliculaService {

    private final PeliculaRepository peliculaRepo = new PeliculaRepository();

    public List<Pelicula> obtenerCartelera() {
        try {
            return peliculaRepo.listar();
        } catch (RuntimeException e) {
            return List.of();
        }
    }

    public Pelicula obtenerDetalle(int idPelicula) {
        try {
            return peliculaRepo.buscarPorId(idPelicula);
        } catch (RuntimeException e) {
            return null;
        }
    }

    public ResultadoOperacion registrar(Pelicula pelicula) {
        try {
            peliculaRepo.agregar(pelicula);
            return ResultadoOperacion.EXITO;
        } catch (RuntimeException e) {
            return ResultadoOperacion.ERROR;
        }
    }

    public ResultadoOperacion editar(Pelicula pelicula) {
        try {
            peliculaRepo.editar(pelicula);
            return ResultadoOperacion.EXITO;
        } catch (RuntimeException e) {
            return ResultadoOperacion.ERROR;
        }
    }

    public ResultadoOperacion eliminar(int idPelicula) {
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
}
