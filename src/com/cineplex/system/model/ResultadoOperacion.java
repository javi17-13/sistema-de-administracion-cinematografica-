package com.cineplex.system.model;

/**
 * Resultado de guardar/editar/eliminar un registro. En vez de devolver
 * true/false (que no dice POR QUE fallo) o dejar reventar la excepcion
 * de SQL hasta la pantalla, los Service devuelven uno de estos valores
 * y el controlador decide que alerta mostrar.
 */
public enum ResultadoOperacion {
    EXITO("Operación realizada con éxito."),
    USUARIO_DUPLICADO("El nombre de usuario ya está registrado."),
    CORREO_DUPLICADO("El correo electrónico ya está registrado."),
    ASIENTO_OCUPADO("El asiento seleccionado ya fue comprado por otro cliente."),
    REGISTRO_EN_USO("El registro no se puede eliminar porque tiene datos o funciones asociadas."),
    DATOS_INVALIDOS("Los datos ingresados no son válidos o están incompletos."),
    NO_ENCONTRADO("El registro solicitado no fue encontrado."),
    ERROR("Ocurrió un error inesperado al procesar la operación.");

    private final String mensajeDefault;

    ResultadoOperacion(String mensajeDefault) {
        this.mensajeDefault = mensajeDefault;
    }

    public String getMensajeDefault() {
        return mensajeDefault;
    }

    public boolean isExito() {
        return this == EXITO;
    }
}
