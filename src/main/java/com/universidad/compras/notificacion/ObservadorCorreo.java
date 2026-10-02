package com.universidad.compras.notificacion;

import com.universidad.compras.modelo.Solicitud;

/** Observador que avisa al solicitante por correo. Usa ClientesNotificacion. */
public class ObservadorCorreo implements ObservadorSolicitud {

    @Override
    public void alCambiarEstado(Solicitud solicitud, String estadoAnterior, String estadoNuevo, String detalle) {
        ClientesNotificacion.enviarCorreo(
                solicitud.getSolicitanteEmail(),
                "Solicitud " + solicitud.getId() + " ahora esta " + estadoNuevo,
                "Su solicitud paso de " + estadoAnterior + " a " + estadoNuevo + ". " + detalle);
    }
}
