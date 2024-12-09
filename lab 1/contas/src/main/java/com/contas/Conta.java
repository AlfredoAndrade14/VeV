package com.contas;

import java.util.Date;

public class Conta {
    private String codigo;
    private Date data;
    private double valorPago;

    public Conta(String codigo, Date data, double valorPago) {
        this.codigo = codigo;
        this.data = data;
        this.valorPago = valorPago;
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
}
