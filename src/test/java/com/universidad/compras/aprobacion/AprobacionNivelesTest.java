package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AprobacionNivelesTest {

    @Test
    void solicitudDentroDeAutoridadDelSupervisorSeAprueba() {
        ServicioAprobacion servicio = new ServicioAprobacionEncadenado();
        Solicitud s = new Solicitud("S-001", "ana@udes.edu.co", 1500000, "MATERIAL_OFICINA", "CC-100");
        ResultadoAprobacion r = servicio.evaluar(s);
        assertTrue(r.isAprobada());
        assertEquals("Supervisor de Área", r.getNivelResolutor());
    }

    @Test
    void solicitudQueSuperaAlSupervisorEscalaAlGerente() {
        ServicioAprobacion servicio = new ServicioAprobacionEncadenado();
        Solicitud s = new Solicitud("S-002", "luis@udes.edu.co", 6000000, "SOFTWARE", "CC-200");
        ResultadoAprobacion r = servicio.evaluar(s);
        assertTrue(r.isAprobada());
        assertEquals("Gerente de Área", r.getNivelResolutor());
    }

    @Test
    void solicitudInternacionalPasaPorCumplimientoAntesDelNivelPorMonto() {
        ServicioAprobacion servicio = new ServicioAprobacionEncadenado();
        Solicitud s = new Solicitud("S-003", "gerencia@udes.edu.co", 1000000, "INTERNACIONAL", "CC-300");
        ResultadoAprobacion r = servicio.evaluar(s);
        assertEquals("Revisor de Cumplimiento Normativo", r.getNivelResolutor());
    }

    // ---- Casos limite agregados ----

    @Test
    void solicitudQueSuperaAlGerenteEscalaAlDirectorFinanciero() {
        ServicioAprobacion servicio = new ServicioAprobacionEncadenado();
        Solicitud s = new Solicitud("S-004", "cfo@udes.edu.co", 50000000, "SOFTWARE", "CC-400");
        ResultadoAprobacion r = servicio.evaluar(s);
        assertTrue(r.isAprobada());
        assertEquals("Director Financiero", r.getNivelResolutor());
    }

    @Test
    void solicitudInternacionalDeMontoAltoLaResuelveCumplimientoNoElDirector() {
        // El eslabon de cumplimiento esta antes que los niveles por monto: aun
        // con un monto que normalmente iria al Director, resuelve Cumplimiento.
        ServicioAprobacion servicio = new ServicioAprobacionEncadenado();
        Solicitud s = new Solicitud("S-005", "compras@udes.edu.co", 80000000, "INTERNACIONAL", "CC-500");
        ResultadoAprobacion r = servicio.evaluar(s);
        assertTrue(r.isAprobada());
        assertEquals("Revisor de Cumplimiento Normativo", r.getNivelResolutor());
    }

    @Test
    void elNivelResolutorQuedaRegistradoEnLaSolicitud() {
        ServicioAprobacion servicio = new ServicioAprobacionEncadenado();
        Solicitud s = new Solicitud("S-006", "ana@udes.edu.co", 2000000, "MATERIAL_OFICINA", "CC-600");
        servicio.evaluar(s);
        assertEquals("Supervisor de Área", s.getNivelResolutor());
        assertEquals("APROBADA", s.getEstado());
    }
}
