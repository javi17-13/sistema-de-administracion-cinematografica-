package com.cineplex.system.model;

/**
 * Resultado de guardar/editar/eliminar un registro. En vez de devolver
 * true/false (que no dice POR QUE fallo) o dejar reventar la excepcion
 * de SQL hasta la pantalla, los Service devuelven uno de estos valores
 * y el controlador decide que alerta mostrar.
 */
public enum ResultadoOperacion {
    EXITO,
    USUARIO_DUPLICADO,
    TITULO_DUPLICADO,
    ASIENTO_OCUPADO,
    REGISTRO_EN_USO,
    DATOS_INVALIDOS,
    ERROR
}
