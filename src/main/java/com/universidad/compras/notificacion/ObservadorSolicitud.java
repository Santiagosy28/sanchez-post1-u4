package com.universidad.compras.notificacion;

import com.universidad.compras.modelo.Solicitud;

/**
 * Necesidad 3 — Patron Observer (interfaz del suscriptor).
 *
 * Cada observador reacciona a un cambio de estado de una solicitud. El emisor
 * no conoce las implementaciones concretas: solo publica el cambio contra esta
 * interfaz, de modo que agregar un nuevo observador no obliga a modificar el
 * emisor ni los demas observadores.
 */
public interface ObservadorSolicitud {

    void alCambiarEstado(Solicitud solicitud, String estadoAnterior, String estadoNuevo, String detalle);
}
