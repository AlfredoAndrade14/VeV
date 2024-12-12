package com.ingressos.models;

import java.util.ArrayList;
import java.util.List;

import com.ingressos.enums.TipoIngresso;

public class Lote {

    private static int contadorId = 1;
    private final int id;
    private List<Ingresso> ingressos;

    public Lote(Integer quantidade, Double percentualVip, Double precoNormal, Double desconto) {
        this.id = contadorId++;
        this.ingressos = new ArrayList<>();

        int qtdeVip = (int) (quantidade * percentualVip);
        int qtdeMeia = (int) (quantidade * 0.1);
        int qtdeNormal = quantidade - qtdeVip - qtdeMeia;

        for (int i = 0; i < qtdeVip; i++) {
            ingressos.add(new Ingresso(TipoIngresso.VIP));
        }
        for (int i = 0; i < qtdeMeia; i++) {
            ingressos.add(new Ingresso(TipoIngresso.MEIA_ENTRADA));
        }
        for (int i = 0; i < qtdeNormal; i++) {
            ingressos.add(new Ingresso(TipoIngresso.NORMAL));
        }
    }

    public int getId() {
        return id;
    }

    public List<Ingresso> getIngressos() {
        return ingressos;
    }

}
