package com.ingressos.models;

import com.ingressos.enums.TipoIngresso;

public class Ingresso {

    private static int contadorId = 1;
    private final int id;
    private final TipoIngresso tipo;
    private boolean vendido;

    public Ingresso(TipoIngresso tipo) {
        if (tipo == null) {
            throw new NullPointerException("O tipo do ingresso não pode ser nulo");
        }
        this.id = contadorId++;
        this.tipo = tipo;
        this.vendido = false;
    }

    public int getId() {
        return id;
    }

    public TipoIngresso getTipo() {
        return tipo;
    }

    public boolean isVendido() {
        return vendido;
    }

    public void vender() {
        vendido = true;
    }
}
