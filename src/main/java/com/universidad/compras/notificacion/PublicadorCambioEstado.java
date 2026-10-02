package com.universidad.compras.notificacion;

import com.universidad.compras.modelo.Solicitud;

import java.util.ArrayList;
import java.util.List;

/**
 * Necesidad 3 — Sujeto del patron Observer.
 *
 * Es el unico punto que cambia el estado de una Solicitud y, acto seguido,
 * notifica a todos los observadores suscritos. El codigo que dispara el cambio
 * no conoce a los observadores concretos (correo, contabilidad, auditoria): los
 * publica contra la interfaz {@link ObservadorSolicitud}. Agregar un cuarto
 * observador se hace con {@link #suscribir}, sin modificar esta clase.
 */
public class PublicadorCambioEstado {

    private final List<ObservadorSolicitud> observadores = new ArrayList<>();

    /** Constructor por defecto: deja suscritas las tres reacciones estandar. */
    public PublicadorCambioEstado() {
        suscribir(new ObservadorCorreo());
        suscribir(new ObservadorDashboard());
        suscribir(new ObservadorAuditoria());
    }

    /** Suscribe un observador adicional sin tocar el mecanismo central. */
    public void suscribir(ObservadorSolicitud observador) {
        observadores.add(observador);
    }

    /**
     * Cambia el estado de la solicitud y notifica a todos los observadores.
     * Este es el punto unico de cambio de estado del sistema: lo usan tanto
     * las reglas de transicion (Necesidad 4) como, a traves de ellas, la
     * aprobacion (Necesidad 1) y la ejecucion (Necesidad 2).
     */
    public void cambiarEstado(Solicitud solicitud, String estadoNuevo, String detalle) {
        String estadoAnterior = solicitud.getEstado();
        solicitud.setEstado(estadoNuevo);
        for (ObservadorSolicitud observador : observadores) {
            observador.alCambiarEstado(solicitud, estadoAnterior, estadoNuevo, detalle);
        }
    }
}
