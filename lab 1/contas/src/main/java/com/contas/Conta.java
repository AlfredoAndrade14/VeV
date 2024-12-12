package com.contas;

import java.util.Date;

public class Conta {
    private String codigo;
    private Date data;
    private double valorPago;
    private Fatura fatura;

    public Conta(String codigo, Date data, double valorPago, Fatura fatura) {
        this.codigo = codigo;
        this.data = data;
        this.valorPago = valorPago;
        this.fatura = fatura;
    }

    public String getCodigo() {
        return codigo;
    }

    public Date getData() {
        return data;
    }

    public double getValorPago() {
        return valorPago;
    }

    public Fatura geFatura() {
        return fatura;
    }
}
