package com.ingressos.models;

import java.util.ArrayList;
import java.util.List;

public class Lote {

    private static int contadorId = 1;
    private final int id;
    private List<Ingresso> ingressos;

    public Lote() {
        this.id = contadorId++;
        this.ingressos = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

}
