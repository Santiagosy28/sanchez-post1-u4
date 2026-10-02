package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;
import org.springframework.stereotype.Service;

/**
 * Necesidad 1 — Implementacion de {@link ServicioAprobacion} con el patron
 * Chain of Responsibility.
 *
 * Construye la cadena de niveles y delega la evaluacion en su cabeza. El orden
 * es: Revisor de Cumplimiento -> Supervisor -> Gerente -> Director. El revisor
 * solo resuelve las solicitudes INTERNACIONAL y delega el resto, de modo que
 * una unica cadena atiende ambos casos. Agregar, quitar o reordenar un nivel se
 * hace aqui, sin tocar ControladorSolicitudes ni los demas niveles.
 */
@Service
public class ServicioAprobacionEncadenado implements ServicioAprobacion {

    private NivelAprobacion construirCadena() {
        NivelAprobacion revisor   = new RevisorCumplimiento();
        NivelAprobacion supervisor = new SupervisorArea();
        NivelAprobacion gerente    = new GerenteArea();
        NivelAprobacion director   = new DirectorFinanciero();

        // Cabeza -> ... -> cola. Cambiar este encadenamiento es el unico punto
        // a tocar para reconfigurar los niveles.
        revisor.enlazarCon(supervisor).enlazarCon(gerente).enlazarCon(director);
        return revisor;
    }

    @Override
    public ResultadoAprobacion evaluar(Solicitud solicitud) {
        ResultadoAprobacion resultado = construirCadena().evaluar(solicitud);
        aplicarResultado(solicitud, resultado);
        return resultado;
    }

    /**
     * Punto de cambio de estado de la Necesidad 1. En la Parte 2 se conecta con
     * las notificaciones (Necesidad 3) y las reglas de transicion (Necesidad 4).
     */
    private void aplicarResultado(Solicitud solicitud, ResultadoAprobacion resultado) {
        solicitud.setNivelResolutor(resultado.getNivelResolutor());
        solicitud.setEstado(resultado.isAprobada() ? "APROBADA" : "RECHAZADA");
    }
}
