package com.universidad.compras.estado;

import com.universidad.compras.modelo.Solicitud;
import com.universidad.compras.notificacion.PublicadorCambioEstado;

/**
 * Necesidad 4 — Contexto del patron State.
 *
 * Envuelve una Solicitud y delega cada operacion (aprobar, rechazar, ejecutar,
 * cancelar) en el objeto de estado actual, que decide si es valida. Las
 * transiciones se realizan a traves del {@link PublicadorCambioEstado} de la
 * Necesidad 3, de modo que todo cambio de estado tambien dispara las
 * notificaciones: aqui se conectan las Necesidades 3 y 4.
 */
public class ContextoSolicitud {

    private final Solicitud solicitud;
    private final PublicadorCambioEstado publicador;
    private EstadoSolicitud estadoActual;

    /** Usa un publicador por defecto (correo, tablero, auditoria). */
    public ContextoSolicitud(Solicitud solicitud) {
        this(solicitud, new PublicadorCambioEstado());
    }

    /** Permite inyectar un publicador (por ejemplo con observadores extra). */
    public ContextoSolicitud(Solicitud solicitud, PublicadorCambioEstado publicador) {
        this.solicitud = solicitud;
        this.publicador = publicador;
        this.estadoActual = EstadosSolicitud.desde(solicitud.getEstado());
    }

    public void aprobar()  { estadoActual.aprobar(this); }
    public void rechazar() { estadoActual.rechazar(this); }
    public void ejecutar() { estadoActual.ejecutar(this); }
    public void cancelar() { estadoActual.cancelar(this); }

    /**
     * Realiza una transicion valida: cambia el estado de la solicitud y notifica
     * (via el publicador) y actualiza el objeto de estado del contexto.
     * Es llamado por los estados concretos, no por el cliente.
     */
    void transicionarA(EstadoSolicitud nuevoEstado, String detalle) {
        publicador.cambiarEstado(solicitud, nuevoEstado.nombre(), detalle);
        this.estadoActual = nuevoEstado;
    }

    public Solicitud getSolicitud()        { return solicitud; }
    public EstadoSolicitud getEstadoActual() { return estadoActual; }
}
