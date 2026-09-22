package com.cineplex.system.model;

/**
 * Resultado de intentar comprar un boleto. ASIENTO_OCUPADO pasa cuando
 * dos personas intentan comprar el mismo asiento de la misma funcion
 * casi al mismo tiempo -- la tabla Boletos tiene UNIQUE(ID_Funcion,
 * Asiento), asi que la segunda compra la rechaza la base de datos, no
 * hace falta revisarlo "a mano" antes de insertar.
 */
public enum ResultadoCompra {
    EXITO,
    ASIENTO_OCUPADO,
    ERROR
}
