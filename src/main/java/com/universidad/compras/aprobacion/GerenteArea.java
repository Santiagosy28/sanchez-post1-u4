package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;

/** Segundo nivel por monto: aprueba hasta $10.000.000; en otro caso delega. */
public class GerenteArea extends NivelAprobacion {

    private static final double LIMITE = 10_000_000;

    @Override
    public ResultadoAprobacion evaluar(Solicitud solicitud) {
        if (solicitud.getMonto() <= LIMITE) {
            return new ResultadoAprobacion(true, "Gerente de Área",
                    "Monto dentro de la autoridad del gerente de area");
        }
        return delegar(solicitud);
    }
}
