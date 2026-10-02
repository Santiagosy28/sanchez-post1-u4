package com.universidad.compras.notificacion;

import com.universidad.compras.modelo.Solicitud;

/** Observador que actualiza el tablero de contabilidad. Usa ClientesNotificacion. */
public class ObservadorDashboard implements ObservadorSolicitud {

    @Override
    public void alCambiarEstado(Solicitud solicitud, String estadoAnterior, String estadoNuevo, String detalle) {
        ClientesNotificacion.actualizarDashboardContabilidad(
                solicitud.getId(), estadoNuevo, solicitud.getMonto());
    }
}
