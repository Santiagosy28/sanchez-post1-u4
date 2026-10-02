package com.universidad.compras.estado;

/** Estado terminal: una solicitud cancelada no admite mas operaciones. */
public class EstadoCancelada extends EstadoBase {

    @Override
    public String nombre() {
        return "CANCELADA";
    }
}
