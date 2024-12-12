package com.ingressos.models;

import com.ingressos.enums.StatusFinanceiro;

public class Relatorio {

    private int ingressosVipVendidos;
    private int ingressosMeiaVendidos;
    private int ingressosNormaisVendidos;
    private double receitaLiquida;
    private StatusFinanceiro statusFinanceiro;
    private String artista;
    private String data;

    public Relatorio(Show show) {
        int ingressosVipVendidos = 0;
        int ingressosMeiaVendidos = 0;
        int ingressosNormaisVendidos = 0;
        double receita = 0;

        for (Lote lote : show.getLotes()) {
            for (Ingresso ingresso : lote.getIngressos()) {
                if (ingresso.isVendido()) {
                    receita += ingresso.getPrecoFinal();
                    switch (ingresso.getTipo()) {
                        case VIP -> ingressosVipVendidos++;
                        case MEIA_ENTRADA -> ingressosMeiaVendidos++;
                        case NORMAL -> ingressosNormaisVendidos++;
                    }
                }
            }
        }

        double despesas = calcularDespesas(show);

        this.receitaLiquida = receita - despesas;

        this.statusFinanceiro = receitaLiquida > 0 ? StatusFinanceiro.LUCRO
                : receitaLiquida == 0 ? StatusFinanceiro.ESTAVEL : StatusFinanceiro.PREJUÍZO;

        this.artista = show.getArtista();
        this.data = show.getData();
        this.ingressosVipVendidos = ingressosVipVendidos;
        this.ingressosMeiaVendidos = ingressosMeiaVendidos;
        this.ingressosNormaisVendidos = ingressosNormaisVendidos;
    }

    private double calcularDespesas(Show show) {
        double despesas = show.getDespesasInfraestrutura();
        if (show.isDataEspecial()) {
            despesas += show.getDespesasInfraestrutura() * 0.15;
        }
        return despesas + show.getCache();
    }

    public double getReceitaLiquida() {
        return receitaLiquida;
    }

    public StatusFinanceiro getStatusFinanceiro() {
        return statusFinanceiro;
    }

    public String getArtista() {
        return artista;
    }

    public String getData() {
        return data;
    }

    public int getIngressosVipVendidos() {
        return ingressosVipVendidos;
    }

    public int getIngressosMeiaVendidos() {
        return ingressosMeiaVendidos;
    }

    public int getIngressosNormaisVendidos() {
        return ingressosNormaisVendidos;
    }

    @Override
    public String toString() {
        return String.format(
                "Relatório do Show\nData: %s\nArtista: %s\nIngressos VIP vendidos: %d\nIngressos Meia vendidos: %d\nIngressos Normais vendidos: %d\nReceita líquida: %.2f\nStatus financeiro: %s",
                data, artista, ingressosVipVendidos, ingressosMeiaVendidos, ingressosNormaisVendidos, receitaLiquida,
                statusFinanceiro);
    }

}
