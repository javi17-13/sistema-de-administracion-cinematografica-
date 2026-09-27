package com.cineplex.system.service;

import java.sql.SQLIntegrityConstraintViolationException;
import com.cineplex.system.model.ResultadoOperacion;
import com.cineplex.system.repository.BoletoRepository;

public class BoletoService {

    private final BoletoRepository boletoRepo = new BoletoRepository();

    public ResultadoOperacion comprar(int idFuncion, int idCliente, String asiento, String contenidoQR) {
        if (idFuncion <= 0 || idCliente <= 0 || asiento == null || asiento.isBlank()) {
            return ResultadoOperacion.DATOS_INVALIDOS;
        }

        try {
            boletoRepo.crear(idFuncion, idCliente, asiento, contenidoQR);
            return ResultadoOperacion.EXITO;
        } catch (RuntimeException e) {
            e.printStackTrace();
            // UNIQUE(ID_Funcion, Asiento): alguien mas compro ese asiento primero
            if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                return ResultadoOperacion.ASIENTO_OCUPADO;
            }
            return ResultadoOperacion.ERROR;
        }
    }
}
