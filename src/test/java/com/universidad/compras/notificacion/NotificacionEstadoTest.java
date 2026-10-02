package com.universidad.compras.notificacion;

import com.universidad.compras.modelo.Solicitud;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NotificacionEstadoTest {

    @Test
    void cambiarEstadoDisparaLasTresReaccionesSinLanzarExcepcion() {
        Solicitud s = new Solicitud("S-020", "ana@udes.edu.co", 2500000, "SOFTWARE", "CC-100");
        PublicadorCambioEstado mecanismo = new PublicadorCambioEstado();
        // cambiar el estado de "s" a traves de "mecanismo" (correo + tablero + auditoria)
        assertDoesNotThrow(() -> mecanismo.cambiarEstado(s, "APROBADA", "Aprobada por el supervisor"));
        assertEquals("APROBADA", s.getEstado());
    }

    @Test
    void agregarUnCuartoSuscriptorDePruebaNoRequiereModificarElMecanismo() {
        Solicitud s = new Solicitud("S-021", "luis@udes.edu.co", 3000000, "SOFTWARE", "CC-200");
        PublicadorCambioEstado mecanismo = new PublicadorCambioEstado();
        // Colector de prueba como cuarto observador, suscrito sin tocar el publicador.
        ColectorPrueba colector = new ColectorPrueba();
        mecanismo.suscribir(colector);
        assertDoesNotThrow(() -> mecanismo.cambiarEstado(s, "EJECUTADA", "Ejecutada"));
        // El cuarto suscriptor tambien reacciono al cambio de estado.
        assertEquals(1, colector.getConteo());
        assertEquals("EJECUTADA", colector.getUltimoEstado());
    }

    /** Observador de prueba: cuenta cuantas veces reacciona y guarda el ultimo estado. */
    private static class ColectorPrueba implements ObservadorSolicitud {
        private int conteo = 0;
        private String ultimoEstado;

        @Override
        public void alCambiarEstado(Solicitud solicitud, String estadoAnterior, String estadoNuevo, String detalle) {
            conteo++;
            ultimoEstado = estadoNuevo;
        }

        int getConteo()          { return conteo; }
        String getUltimoEstado() { return ultimoEstado; }
    }
}
