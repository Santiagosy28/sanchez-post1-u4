package com.universidad.compras.estado;

/**
 * Necesidad 4 — Patron State (interfaz del estado).
 *
 * Cada estado concreto sabe que operaciones son validas cuando la solicitud se
 * encuentra en el; una operacion valida transiciona el contexto a otro estado,
 * una invalida se rechaza sin cambiar nada. Agregar un estado nuevo o cambiar
 * sus reglas se hace creando/editando una clase de estado, no modificando
 * if/else dispersos.
 */
public interface EstadoSolicitud {

    void aprobar(ContextoSolicitud contexto);

    void rechazar(ContextoSolicitud contexto);

    void ejecutar(ContextoSolicitud contexto);

    void cancelar(ContextoSolicitud contexto);

    /** Nombre del estado, coincide con el String de Solicitud.getEstado(). */
    String nombre();
}
