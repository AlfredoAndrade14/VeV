package com.contas;

import java.util.Date;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pagamento pagamento = (Pagamento) o;
        if (this.conta == pagamento.conta && this.tipo == pagamento.tipo) return true;
        return false;
    }
}