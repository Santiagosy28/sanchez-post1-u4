package com.universidad.compras.ejecucion;

import com.universidad.compras.modelo.Solicitud;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EjecucionSolicitudTest {

    @Test
    void ejecutarReservaPresupuestoYGeneraOrden() {
        Solicitud s = new Solicitud("S-010", "ana@udes.edu.co", 3000000, "SOFTWARE", "CC-100");
        s.setEstado("APROBADA");
        EjecutorSolicitud ejecutor = new EjecutorSolicitud(s);
        // Las dos operaciones de ejecucion sobre "s" a traves de "ejecutor":
        ejecutor.ejecutarCompra("Proveedor Alpha");
        assertEquals("EJECUTADA", s.getEstado());
        assertEquals(2, ejecutor.getHistorial().size());
    }

    @Test
    void deshacerSoloLaUltimaOperacionNoAfectaLaAnterior() {
        // ejecutar reservar presupuesto, luego generar orden; deshacer una vez
        // debe revertir solo la generacion de la orden, no la reserva.
        Solicitud s = new Solicitud("S-011", "luis@udes.edu.co", 4000000, "MATERIAL_OFICINA", "CC-200");
        EjecutorSolicitud ejecutor = new EjecutorSolicitud(s);
        assertDoesNotThrow(() -> {
            ejecutor.ejecutar(new ReservarPresupuestoComando(s, new PresupuestoService()));
            ejecutor.ejecutar(new GenerarOrdenComando(s, new OrdenCompraService(), "Proveedor Beta"));
            ejecutor.deshacerUltima();
        });
        // Solo queda la reserva de presupuesto en el historial.
        assertEquals(1, ejecutor.getHistorial().size());
        assertEquals("Reserva de presupuesto en CC-200", ejecutor.getHistorial().get(0).descripcion());
    }

    @Test
    void elHistorialConservaTodasLasOperacionesNoSoloLaUltima() {
        Solicitud s = new Solicitud("S-012", "ana@udes.edu.co", 2500000, "SOFTWARE", "CC-300");
        EjecutorSolicitud ejecutor = new EjecutorSolicitud(s);
        // tras ejecutar dos operaciones sobre una misma solicitud, el historial
        // consultable debe reportar tamano 2, no solo la ultima operacion.
        assertDoesNotThrow(() -> {
            ejecutor.ejecutar(new ReservarPresupuestoComando(s, new PresupuestoService()));
            ejecutor.ejecutar(new GenerarOrdenComando(s, new OrdenCompraService(), "Proveedor Gamma"));
        });
        assertEquals(2, ejecutor.getHistorial().size());
    }

    // ---- Caso limite agregado ----

    @Test
    void deshacerTodasLasOperacionesDejaElHistorialVacioYNoFalla() {
        Solicitud s = new Solicitud("S-013", "luis@udes.edu.co", 1800000, "MATERIAL_OFICINA", "CC-400");
        EjecutorSolicitud ejecutor = new EjecutorSolicitud(s);
        ejecutor.ejecutar(new ReservarPresupuestoComando(s, new PresupuestoService()));
        ejecutor.ejecutar(new GenerarOrdenComando(s, new OrdenCompraService(), "Proveedor Delta"));
        assertDoesNotThrow(() -> {
            ejecutor.deshacerUltima();
            ejecutor.deshacerUltima();
            ejecutor.deshacerUltima(); // deshacer sobre historial vacio no debe fallar
        });
        assertEquals(0, ejecutor.getHistorial().size());
    }
}
