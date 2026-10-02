package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;

/**
 * Ultimo nivel por monto: el Director Financiero aprueba cualquier monto sin
 * limite superior, por lo que siempre resuelve y cierra la cadena.
 */
public class DirectorFinanciero extends NivelAprobacion {

    @Override
    public ResultadoAprobacion evaluar(Solicitud solicitud) {
        return new ResultadoAprobacion(true, "Director Financiero",
                "Aprobada por el Director Financiero (sin limite de monto)");
    }
}
