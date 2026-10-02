package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;

/** Primer nivel por monto: aprueba hasta $2.000.000; en otro caso delega. */
public class SupervisorArea extends NivelAprobacion {

    private static final double LIMITE = 2_000_000;

    @Override
    public ResultadoAprobacion evaluar(Solicitud solicitud) {
        if (solicitud.getMonto() <= LIMITE) {
            return new ResultadoAprobacion(true, "Supervisor de Área",
                    "Monto dentro de la autoridad del supervisor");
        }
        return delegar(solicitud);
    }
}
