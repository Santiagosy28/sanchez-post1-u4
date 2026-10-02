package com.universidad.compras.estado;

/** Solicitud aprobada: puede ejecutarse o cancelarse; no re-aprobarse. */
public class EstadoAprobada extends EstadoBase {

    @Override
    public void ejecutar(ContextoSolicitud contexto) {
        contexto.transicionarA(new EstadoEjecutada(), "Solicitud ejecutada");
    }

    @Override
    public void cancelar(ContextoSolicitud contexto) {
        contexto.transicionarA(new EstadoCancelada(), "Solicitud cancelada antes de ejecutar");
    }

    @Override
    public String nombre() {
        return "APROBADA";
    }
}
