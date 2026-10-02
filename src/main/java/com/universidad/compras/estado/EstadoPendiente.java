package com.universidad.compras.estado;

/** Estado inicial. Puede aprobarse, rechazarse o cancelarse; no ejecutarse. */
public class EstadoPendiente extends EstadoBase {

    @Override
    public void aprobar(ContextoSolicitud contexto) {
        contexto.transicionarA(new EstadoAprobada(), "Solicitud aprobada");
    }

    @Override
    public void rechazar(ContextoSolicitud contexto) {
        contexto.transicionarA(new EstadoRechazada(), "Solicitud rechazada");
    }

    @Override
    public void cancelar(ContextoSolicitud contexto) {
        contexto.transicionarA(new EstadoCancelada(), "Solicitud cancelada");
    }

    @Override
    public String nombre() {
        return "PENDIENTE";
    }
}
