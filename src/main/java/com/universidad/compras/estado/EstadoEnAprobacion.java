package com.universidad.compras.estado;

/**
 * Estado intermedio opcional mientras la solicitud recorre la cadena de
 * aprobacion. Puede aprobarse, rechazarse o cancelarse; no ejecutarse aun.
 */
public class EstadoEnAprobacion extends EstadoBase {

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
        return "EN_APROBACION";
    }
}
