package com.universidad.compras.estado;

/** Estado terminal: ninguna operacion es valida (hereda el rechazo de EstadoBase). */
public class EstadoRechazada extends EstadoBase {

    @Override
    public String nombre() {
        return "RECHAZADA";
    }
}
