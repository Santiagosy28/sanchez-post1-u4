package com.universidad.compras.estado;

import com.universidad.compras.modelo.Solicitud;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TransicionEstadoTest {

    @Test
    void ejecutarUnaSolicitudAprobadaLaDejaEjecutada() {
        Solicitud s = new Solicitud("S-030", "luis@udes.edu.co", 3000000, "MATERIAL_OFICINA", "CC-200");
        s.setEstado("APROBADA");
        ContextoSolicitud contexto = new ContextoSolicitud(s);
        contexto.ejecutar();
        assertEquals("EJECUTADA", s.getEstado());
    }

    @Test
    void ejecutarUnaSolicitudPendienteSeRechazaSinCambiarElEstado() {
        Solicitud s = new Solicitud("S-031", "ana@udes.edu.co", 1000000, "SOFTWARE", "CC-100");
        ContextoSolicitud contexto = new ContextoSolicitud(s);
        contexto.ejecutar(); // invalido en PENDIENTE: no debe cambiar el estado
        assertEquals("PENDIENTE", s.getEstado());
    }

    @Test
    void unaSolicitudEjecutadaNoPuedeVolverAEjecutarse() {
        Solicitud s = new Solicitud("S-032", "ana@udes.edu.co", 1000000, "SOFTWARE", "CC-100");
        s.setEstado("EJECUTADA");
        ContextoSolicitud contexto = new ContextoSolicitud(s);
        contexto.ejecutar(); // invalido en EJECUTADA: estado terminal
        assertEquals("EJECUTADA", s.getEstado());
    }

    // ---- Casos limite agregados ----

    @Test
    void aprobarUnaPendienteLaDejaAprobadaYLuegoCancelarLaDejaCancelada() {
        Solicitud s = new Solicitud("S-033", "ana@udes.edu.co", 1500000, "SOFTWARE", "CC-300");
        ContextoSolicitud contexto = new ContextoSolicitud(s);
        contexto.aprobar();
        assertEquals("APROBADA", s.getEstado());
        contexto.cancelar();
        assertEquals("CANCELADA", s.getEstado());
    }

    @Test
    void rechazarUnaPendienteLaDejaRechazadaYYaNoAdmiteOperaciones() {
        Solicitud s = new Solicitud("S-034", "luis@udes.edu.co", 1200000, "SOFTWARE", "CC-400");
        ContextoSolicitud contexto = new ContextoSolicitud(s);
        contexto.rechazar();
        assertEquals("RECHAZADA", s.getEstado());
        contexto.ejecutar(); // terminal: no cambia
        contexto.aprobar();  // terminal: no cambia
        assertEquals("RECHAZADA", s.getEstado());
    }

    @Test
    void ejecutarDejaElObjetoDeEstadoActualizadoEnElContexto() {
        Solicitud s = new Solicitud("S-035", "ana@udes.edu.co", 2000000, "SOFTWARE", "CC-500");
        s.setEstado("APROBADA");
        ContextoSolicitud contexto = new ContextoSolicitud(s);
        contexto.ejecutar();
        assertEquals("EJECUTADA", contexto.getEstadoActual().nombre());
    }
}
