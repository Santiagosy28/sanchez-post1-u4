package com.universidad.compras.ejecucion;

import com.universidad.compras.modelo.Solicitud;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/**
 * Necesidad 2 — Invocador del patron Command.
 *
 * Ejecuta comandos sobre una solicitud y mantiene un historial ORDENADO de
 * todos los ejecutados (no solo el ultimo), de modo que cualquiera pueda
 * inspeccionarse despues. Permite deshacer el ultimo comando de forma
 * independiente, sin afectar a los anteriores.
 */
public class EjecutorSolicitud {

    private final Solicitud solicitud;
    /** Historial ordenado: el primero ejecutado queda al frente, el ultimo al final. */
    private final Deque<ComandoEjecucion> historial = new ArrayDeque<>();

    public EjecutorSolicitud(Solicitud solicitud) {
        this.solicitud = solicitud;
    }

    /** Ejecuta un comando y lo registra en el historial. */
    public void ejecutar(ComandoEjecucion comando) {
        comando.ejecutar();
        historial.addLast(comando);
    }

    /** Deshace unicamente el ultimo comando ejecutado; el resto no se altera. */
    public void deshacerUltima() {
        if (!historial.isEmpty()) {
            historial.removeLast().deshacer();
        }
    }

    /** Historial consultable, en orden de ejecucion, con todas las operaciones. */
    public List<ComandoEjecucion> getHistorial() {
        return List.copyOf(historial);
    }

    /**
     * Ejecuta la compra completa sobre una solicitud aprobada: reserva el
     * presupuesto y genera la orden, dejando ambos comandos en el historial.
     * Al completarse con exito, la solicitud queda EJECUTADA.
     *
     * Este es el punto de cambio de estado de la Necesidad 2; en la Parte 2 se
     * conecta con las notificaciones (Necesidad 3) y las reglas de transicion
     * (Necesidad 4).
     */
    public void ejecutarCompra(String proveedor) {
        ejecutar(new ReservarPresupuestoComando(solicitud, new PresupuestoService()));
        ejecutar(new GenerarOrdenComando(solicitud, new OrdenCompraService(), proveedor));
        solicitud.setEstado("EJECUTADA");
    }
}
