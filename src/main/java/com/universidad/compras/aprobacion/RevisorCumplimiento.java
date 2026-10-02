package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;

/**
 * Nivel adicional de la cadena: Revisor de Cumplimiento Normativo.
 *
 * Solo interviene cuando la categoria es INTERNACIONAL; en ese caso resuelve
 * la solicitud (control de cumplimiento superado). Para cualquier otra
 * categoria delega de inmediato, por lo que su presencia en la cabeza de la
 * cadena no afecta a las solicitudes nacionales. Agregar o quitar este eslabon
 * no obliga a tocar los demas niveles ni a ControladorSolicitudes.
 */
public class RevisorCumplimiento extends NivelAprobacion {

    @Override
    public ResultadoAprobacion evaluar(Solicitud solicitud) {
        if ("INTERNACIONAL".equals(solicitud.getCategoria())) {
            return new ResultadoAprobacion(true, "Revisor de Cumplimiento Normativo",
                    "Cumplimiento normativo verificado para solicitud internacional");
        }
        return delegar(solicitud);
    }
}
