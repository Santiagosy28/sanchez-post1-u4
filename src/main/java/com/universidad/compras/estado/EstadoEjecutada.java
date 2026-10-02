package com.universidad.compras.estado;

/** Estado terminal: una solicitud ejecutada no puede volver a ejecutarse ni cambiar. */
public class EstadoEjecutada extends EstadoBase {

    @Override
    public String nombre() {
        return "EJECUTADA";
    }
}
