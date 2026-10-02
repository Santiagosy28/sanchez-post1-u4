package com.universidad.compras.ejecucion;

import com.universidad.compras.modelo.Solicitud;

/**
 * Comando concreto: generar la orden de compra con el proveedor. Recuerda el
 * numero de orden devuelto para poder cancelarlo al deshacer. Se apoya en
 * OrdenCompraService (codigo dado) sin reimplementar su logica.
 */
public class GenerarOrdenComando implements ComandoEjecucion {

    private final Solicitud solicitud;
    private final OrdenCompraService ordenes;
    private final String proveedor;
    private String numeroOrden;

    public GenerarOrdenComando(Solicitud solicitud, OrdenCompraService ordenes, String proveedor) {
        this.solicitud = solicitud;
        this.ordenes = ordenes;
        this.proveedor = proveedor;
    }

    @Override
    public void ejecutar() {
        this.numeroOrden = ordenes.generar(solicitud.getId(), proveedor);
    }

    @Override
    public void deshacer() {
        if (numeroOrden != null) {
            ordenes.cancelar(numeroOrden);
            numeroOrden = null;
        }
    }

    @Override
    public String descripcion() {
        return "Orden de compra para proveedor " + proveedor;
    }
}
