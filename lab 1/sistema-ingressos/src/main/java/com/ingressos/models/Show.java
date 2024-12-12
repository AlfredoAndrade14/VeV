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
        if (data == null || data.trim().isEmpty()) {
            throw new IllegalArgumentException("A data do show não pode ser nula ou vazia");
        }
        if (artista == null || artista.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do artista não pode ser nulo ou vazio");
        }
        if (cache == null || cache <= 0) {
            throw new IllegalArgumentException("O cache deve ser um valor positivo");
        }
        if (despesasInfraestrutura == null || despesasInfraestrutura < 0) {
            throw new IllegalArgumentException("As despesas de infraestrutura não podem ser nulas ou negativas");
        }
        
        this.data = data;
        this.artista = artista;
        this.cache = cache;
        this.despesasInfraestrutura = despesasInfraestrutura;
        this.dataEspecial = dataEspecial;
        this.lotes = new ArrayList<>();
    }

    public void adicionarLote(Lote lote) {
        this.lotes.add(lote);
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