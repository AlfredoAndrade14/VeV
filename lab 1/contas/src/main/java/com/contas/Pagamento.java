package com.contas;

import java.util.Date;

public class Pagamento {
    public enum TipoPagamento {
        BOLETO, CARTAO_CREDITO, TRANSFERENCIA_BANCARIA
    }

    private double valor;
    private Date data;
    private TipoPagamento tipo;
    private Conta conta;

    public Pagamento(double valor, Date data, TipoPagamento tipo, Conta conta) {
        this.valor = valor;
        this.data = data;
        this.tipo = tipo;
        this.conta = conta;
    }

    public double getValor() {
        return valor;
    }

    public Date getData() {
        return data;
    }

    public TipoPagamento getTipo() {
        return tipo;
    }

    public Conta getConta() {
        return conta;
    }
}