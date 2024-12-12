package com.ingressos.models;

import com.ingressos.enums.TipoIngresso;

public class Ingresso {

    private static int contadorId = 1;
    private final int id;
    private final TipoIngresso tipo;
    private final double preco;
    private final double desconto;
    private boolean vendido;

    public Ingresso(TipoIngresso tipo, Double preco, Double desconto) {
        if (tipo == null) {
            throw new NullPointerException("O tipo do ingresso não pode ser nulo");
        }
        this.id = contadorId++;
        this.tipo = tipo;
        this.preco = preco;
        this.desconto = desconto;
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
        if (!vendido) {
            this.vendido = true;
        } else {
            throw new IllegalStateException("O ingresso já foi vendido");
        }
    }

    public void disponibilizar() {
        if (vendido) {
            this.vendido = false;
        } else {
            throw new IllegalStateException("O ingresso já está disponível");
        }
    }

    public double getPrecoFinal() {
        return preco * (1 - desconto);
    }
}
