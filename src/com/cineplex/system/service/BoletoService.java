package com.cineplex.system.service;

import java.sql.SQLIntegrityConstraintViolationException;
import com.cineplex.system.model.ResultadoCompra;
import com.cineplex.system.repository.BoletoRepository;

public class BoletoService {

    private final BoletoRepository boletoRepo = new BoletoRepository();

    public ResultadoCompra comprar(int idFuncion, int idCliente, String asiento, String contenidoQR) {
        try {
            boletoRepo.crear(idFuncion, idCliente, asiento, contenidoQR);
            return ResultadoCompra.EXITO;
        } catch (RuntimeException e) {
            e.printStackTrace();
            // UNIQUE(ID_Funcion, Asiento): alguien mas compro ese asiento primero
            if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                return ResultadoCompra.ASIENTO_OCUPADO;
            }
            return ResultadoCompra.ERROR;
        }
    }
}
