package com.ingressos.models;

import java.util.ArrayList;
import java.util.List;

public class Show {

    private String data;
    private String artista;
    private Double cache;
    private Double despesasInfraestrutura;
    private boolean dataEspecial;
    private List<Lote> lotes;

    public Show(String data, String artista, Double cache, Double despesasInfraestrutura, boolean dataEspecial) {
        this.data = data;
        this.artista = artista;
        this.cache = cache;
        this.despesasInfraestrutura = despesasInfraestrutura;
        this.dataEspecial = dataEspecial;
        this.lotes = new ArrayList<>();
    }

    public String getData() {
        return data;
    }

    public String getArtista() {
        return artista;
    }

    public Double getCache() {
        return cache;
    }

    public Double getDespesasInfraestrutura() {
        return despesasInfraestrutura;
    }

    public boolean isDataEspecial() {
        return dataEspecial;
    }

    public List<Lote> getLotes() {
        return lotes;
    }

    
}