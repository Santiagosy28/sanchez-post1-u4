package com.universidad.compras.ejecucion;

import com.universidad.compras.modelo.Solicitud;

/**
 * Comando concreto: reservar el presupuesto del centro de costo de la solicitud.
 * Deshacerlo libera exactamente lo reservado. Se apoya en PresupuestoService
 * (codigo dado) sin reimplementar su logica.
 */
public class ReservarPresupuestoComando implements ComandoEjecucion {

    private final Solicitud solicitud;
    private final PresupuestoService presupuesto;

    public ReservarPresupuestoComando(Solicitud solicitud, PresupuestoService presupuesto) {
        this.solicitud = solicitud;
        this.presupuesto = presupuesto;
    }

    @Override
    public void ejecutar() {
        presupuesto.reservar(solicitud.getCentroCosto(), solicitud.getMonto());
    }

    @Override
    public void deshacer() {
        presupuesto.liberar(solicitud.getCentroCosto(), solicitud.getMonto());
    }

    @Override
    public String descripcion() {
        return "Reserva de presupuesto en " + solicitud.getCentroCosto();
    }
}
