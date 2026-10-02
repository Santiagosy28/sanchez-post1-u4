package com.universidad.compras.estado;

/**
 * Base de los estados: por defecto toda operacion es invalida (se rechaza sin
 * cambiar el estado). Cada estado concreto solo sobrescribe las operaciones que
 * SI permite. Asi, agregar un estado nuevo no obliga a revisar los demas.
 */
public abstract class EstadoBase implements EstadoSolicitud {

    @Override
    public void aprobar(ContextoSolicitud contexto)  { rechazarOperacion("aprobar"); }

    @Override
    public void rechazar(ContextoSolicitud contexto) { rechazarOperacion("rechazar"); }

    @Override
    public void ejecutar(ContextoSolicitud contexto) { rechazarOperacion("ejecutar"); }

    @Override
    public void cancelar(ContextoSolicitud contexto) { rechazarOperacion("cancelar"); }

    /** Operacion no permitida en este estado: no cambia el estado. */
    protected void rechazarOperacion(String operacion) {
        System.out.println("[ESTADO] Operacion '" + operacion
                + "' no valida en estado " + nombre() + "; no se cambia el estado.");
    }
}
