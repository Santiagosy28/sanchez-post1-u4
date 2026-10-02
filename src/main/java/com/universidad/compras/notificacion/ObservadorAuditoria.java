package com.universidad.compras.notificacion;

import com.universidad.compras.modelo.Solicitud;

/** Observador que registra el cambio en el log de auditoria. Usa ClientesNotificacion. */
public class ObservadorAuditoria implements ObservadorSolicitud {

    @Override
    public void alCambiarEstado(Solicitud solicitud, String estadoAnterior, String estadoNuevo, String detalle) {
        ClientesNotificacion.registrarAuditoria(
                solicitud.getId(), estadoNuevo,
                "transicion " + estadoAnterior + "->" + estadoNuevo + ": " + detalle);
    }
}
