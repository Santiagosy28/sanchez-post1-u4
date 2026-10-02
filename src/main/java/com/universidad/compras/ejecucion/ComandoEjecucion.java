package com.universidad.compras.ejecucion;

/**
 * Necesidad 2 — Patron Command.
 *
 * Encapsula una operacion de ejecucion sobre una solicitud aprobada como un
 * objeto con capacidad de ejecutarse y de deshacerse de forma independiente.
 * El invocador ({@link EjecutorSolicitud}) no necesita conocer que hace cada
 * comando: solo lo ejecuta, lo guarda en el historial y, si hace falta, lo
 * revierte.
 */
public interface ComandoEjecucion {

    /** Ejecuta la operacion. */
    void ejecutar();

    /** Revierte la operacion ejecutada previamente. */
    void deshacer();

    /** Descripcion legible para el historial. */
    String descripcion();
}
