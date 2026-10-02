package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;

/**
 * Necesidad 1 — Patron Chain of Responsibility (manejador abstracto).
 *
 * Cada nivel de aprobacion es un eslabon de la cadena: evalua la solicitud y,
 * si esta dentro de su autoridad, la resuelve; si no, la delega al siguiente
 * eslabon mediante {@link #delegar}. Quien dispara la evaluacion no sabe
 * cuantos niveles hay ni en que orden estan: solo conoce la cabeza de la cadena.
 */
public abstract class NivelAprobacion {

    /** Siguiente eslabon al que delegar cuando este nivel no resuelve. */
    protected NivelAprobacion siguiente;

    /**
     * Enlaza este nivel con el siguiente y devuelve el siguiente para poder
     * encadenar de forma fluida: a.enlazarCon(b).enlazarCon(c).
     */
    public NivelAprobacion enlazarCon(NivelAprobacion siguiente) {
        this.siguiente = siguiente;
        return siguiente;
    }

    /** Cada nivel concreto decide aqui si resuelve o delega. */
    public abstract ResultadoAprobacion evaluar(Solicitud solicitud);

    /**
     * Pasa la solicitud al siguiente eslabon. Si no hay siguiente, la cadena se
     * agoto sin resolver: se devuelve un resultado no aprobado.
     */
    protected ResultadoAprobacion delegar(Solicitud solicitud) {
        if (siguiente != null) {
            return siguiente.evaluar(solicitud);
        }
        return new ResultadoAprobacion(false, "SIN_RESOLUTOR",
                "Ningun nivel pudo resolver la solicitud");
    }
}
