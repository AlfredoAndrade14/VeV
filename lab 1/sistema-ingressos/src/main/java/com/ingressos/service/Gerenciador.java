package com.ingressos.service;

import com.ingressos.models.*;
import com.ingressos.enums.TipoIngresso;
import java.util.ArrayList;
import java.util.List;

public class Gerenciador {

    private List<Show> shows;

    public Gerenciador() {
        this.shows = new ArrayList<>();
    }

    public Show criarShow(String artista, String data, double despesasInfraestrutura, double cache,
            boolean dataEspecial) {
        if (artista == null || data == null || despesasInfraestrutura < 0 || cache < 0) {
            throw new IllegalArgumentException("Dados inválidos para criar o show");
        }

        if (this.shows.stream().anyMatch(
                s -> s.getData().equals(data) && s.getArtista().equalsIgnoreCase(artista))) {
            throw new IllegalArgumentException("O show já está registrado no sistema.");
        }

        Show novoShow = new Show(artista, data, despesasInfraestrutura, cache, dataEspecial);
        this.shows.add(novoShow);
        return novoShow;
    }

    public void removerShow(Show show) {
        if (!shows.contains(show)) {
            throw new IllegalArgumentException("O show não está registrado no sistema.");
        }
        this.shows.remove(show);
    }

    public Show buscarShow(String artista, String data) {
        if (artista == null || artista.trim().isEmpty()) {
            throw new IllegalArgumentException("O artista não pode ser nulo ou vazio");
        }
        if (data == null || data.trim().isEmpty()) {
            throw new IllegalArgumentException("A data não pode ser nula ou vazia");
        }

        return this.shows.stream()
                .filter(s -> s.getArtista().equalsIgnoreCase(artista) && s.getData().equals(data))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Nenhum show encontrado para o artista " + artista + " na data " + data));
    }

    public List<Show> listarShows() {
        return this.shows;
    }

    public Relatorio gerarRelatorioShow(String artista, String data) {
        Show show = buscarShow(artista, data);
        return show.gerarRelatorio();
    }

    public void adicionarLoteAoShow(String artista, String data, Lote lote) {
        Show show = buscarShow(artista, data);
        show.adicionarLote(lote);
    }

    public void venderIngressos(String artista, String data, TipoIngresso tipo, int quantidade) {
        Show show = buscarShow(artista, data);

        boolean vendido = false;

        for (Lote lote : show.getLotes()) {
            try {
                quantidade -= lote.venderIngressos(tipo, quantidade);
                if (quantidade <= 0) {
                    vendido = true;
                    break;
                }
            } catch (IllegalArgumentException e) {
            }
        }

        if (!vendido || quantidade > 0) {
            throw new IllegalArgumentException("Não há ingressos suficientes do tipo " + tipo + " disponíveis");
        }
    }
}
