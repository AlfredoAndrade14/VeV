package com.contas;

import java.util.Date;

public class Pagamento {
    public enum TipoPagamento {
        BOLETO, CARTAO_CREDITO, TRANSFERENCIA_BANCARIA
    }

    private TipoPagamento tipo;
    private Conta conta;

    public Pagamento(TipoPagamento tipo, Conta conta) {
        this.tipo = tipo;
        this.conta = conta;
    }

    public double getValor() {
        return conta.getValorPago();
    }

    public Date getData() {
        return conta.getData();
    }

    public TipoPagamento getTipo() {
        return tipo;
    }

    public Conta getConta() {
        return conta;
    }
}