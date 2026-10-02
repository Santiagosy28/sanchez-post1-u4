package com.universidad.compras.estado;

/**
 * Fabrica que traduce el String de estado de una Solicitud al objeto de estado
 * correspondiente. Es el unico lugar que conoce el mapa nombre -> clase de
 * estado; agregar un estado nuevo (por ejemplo EN_ESPERA_PROVEEDOR) se reduce a
 * crear su clase y registrarla aqui.
 */
final class EstadosSolicitud {

    private EstadosSolicitud() {}

    static EstadoSolicitud desde(String estado) {
        return switch (estado) {
            case "PENDIENTE"     -> new EstadoPendiente();
            case "EN_APROBACION" -> new EstadoEnAprobacion();
            case "APROBADA"      -> new EstadoAprobada();
            case "RECHAZADA"     -> new EstadoRechazada();
            case "EJECUTADA"     -> new EstadoEjecutada();
            case "CANCELADA"     -> new EstadoCancelada();
            default -> throw new IllegalArgumentException("Estado desconocido: " + estado);
        };
    }
}
